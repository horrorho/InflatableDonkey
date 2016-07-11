/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.crypto.xblock;

import java.util.Objects;
import java.util.function.IntFunction;
import net.jcip.annotations.NotThreadSafe;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

/**
 * XBlockCipher.
 *
 * @author Ahseya
 */
@NotThreadSafe
public class XBlockCipher implements BlockCipher {

    private final BlockCipher cipher;
    private final IntFunction<byte[]> hashIndex;
    private final KeyParameter key;
    private final int size;
    private int index;
    private int offset;

    XBlockCipher(BlockCipher cipher, IntFunction<byte[]> hashIndex, KeyParameter key, int size, int index, int offset) {
        this.cipher = Objects.requireNonNull(cipher, "cipher");
        this.hashIndex = Objects.requireNonNull(hashIndex, "hashIndex");
        this.key = Objects.requireNonNull(key, "key");
        this.size = size;
        this.index = index;
        this.offset = offset;
    }

    public XBlockCipher(BlockCipher cipher, IntFunction<byte[]> hashIndex, KeyParameter key, int size) {
        this(cipher, hashIndex, key, size, 0, 0);
    }

    @Override
    public void init(boolean forEncryption, CipherParameters params) throws IllegalArgumentException {
        cipher.init(forEncryption, params);
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
        if (offset == 0) {
            byte[] iv = hashIndex.apply(index);
            ParametersWithIV parameters = new ParametersWithIV(key, iv);
            cipher.init(false, parameters);
        }

        offset += getBlockSize();
        if (offset == size) {
            index++;
            offset = 0;
        }
        return cipher.processBlock(in, inOff, out, outOff);
    }

    @Override
    public void reset() {
        cipher.reset();
        offset = 0;
        index = 0;
    }
}