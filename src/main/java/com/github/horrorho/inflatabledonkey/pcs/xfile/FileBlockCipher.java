/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.pcs.xfile;

import java.util.Objects;
import java.util.function.IntFunction;
import net.jcip.annotations.NotThreadSafe;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

/**
 * AES CBC mode with per block IV generation.
 *
 * @see FileBlockIVGenerator
 * @author Ahseya
 */
@NotThreadSafe
public class FileBlockCipher implements BlockCipher {

    // 'Every time a file on the data partition is created, Data Protection creates a new 256-bit
    // key (the “per-file” key) and gives it to the hardware AES engine, which uses the key to
    // encrypt the file as it is written to flash memory using AES CBC mode. (On devices with
    // an A8 processor, AES-XTS is used.) The initialization vector (IV) is calculated with the
    // block offset into the file, encrypted with the SHA-1 hash of the per-file key.'
    // Apple: iOS Security iOS 9.3 or later. May 2016. 
    // https://www.apple.com/business/docs/iOS_Security_Guide.pdf
    private static final int BLOCK_LENGTH = 0x1000;

    private final BlockCipher cipher;
    private final int blockLength;
    private IntFunction<byte[]> ivGenerator;
    private KeyParameter key;
    private boolean forEncryption;
    private int index;
    private int offset;

    FileBlockCipher(
            BlockCipher cipher,
            int blockLength, IntFunction<byte[]> ivGenerator,
            KeyParameter key,
            boolean forEncryption,
            int index,
            int offset) {

        this.cipher = Objects.requireNonNull(cipher, "cipher");
        this.blockLength = blockLength;
        this.ivGenerator = ivGenerator;
        this.key = key;
        this.forEncryption = forEncryption;
        this.index = index;
        this.offset = offset;
    }

    FileBlockCipher(BlockCipher cipher, int blockLength) {
        this(cipher, blockLength, null, null, false, 0, 0);
    }

    public FileBlockCipher(int blockLength) {
        this(new CBCBlockCipher(new AESEngine()), blockLength);
    }

    public FileBlockCipher() {
        this(BLOCK_LENGTH);
    }

    @Override
    public void init(boolean forEncryption, CipherParameters params) throws IllegalArgumentException {
        if (!(params instanceof KeyParameter)) {
            throw new IllegalArgumentException("illegal params class: " + params.getClass());
        }

        key = (KeyParameter) params;
        this.forEncryption = forEncryption;

        ivGenerator = new FileBlockIVGenerator(key.getKey());
        offset = 0;
        index = 0;
    }

    @Override
    public String getAlgorithmName() {
        return cipher.getAlgorithmName();
    }

    @Override
    public int getBlockSize() {
        return cipher.getBlockSize();
    }

    @Override
    public int processBlock(byte[] in, int inOff, byte[] out, int outOff) throws DataLengthException, IllegalStateException {
        if (key == null) {
            throw new IllegalStateException("not initialised");
        }

        if (offset == 0) {
            byte[] iv = ivGenerator.apply(index);
            ParametersWithIV parameters = new ParametersWithIV(key, iv);
            cipher.init(forEncryption, parameters);
        }

        offset += getBlockSize();
        if (offset == blockLength) {
            index++;
            offset = 0;
        }
        return cipher.processBlock(in, inOff, out, outOff);
    }

    @Override
    public void reset() {
        offset = 0;
        index = 0;
    }
}
