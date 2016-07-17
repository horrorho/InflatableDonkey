/*
 * The MIT License
 *
 * Copyright 2016 Ahseya.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.horrorho.inflatabledonkey.dataprotection;

import com.github.horrorho.inflatabledonkey.args.Property;
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
 * Data Protection AES CBC mode with per block IV generation.
 *
 * @see DPAESCBCBlockIVGenerator
 * @author Ahseya
 */
@NotThreadSafe
public class DPAESCBC implements BlockCipher {

    // 'Every time a file on the data partition is created, Data Protection creates a new 256-bit
    // key (the “per-file” key) and gives it to the hardware AES engine, which uses the key to
    // encrypt the file as it is written to flash memory using AES CBC mode. (On devices with
    // an A8 processor, AES-XTS is used.) The initialization vector (IV) is calculated with the
    // block offset into the file, encrypted with the SHA-1 hash of the per-file key.'
    // Apple: iOS Security iOS 9.3 or later. May 2016. 
    // https://www.apple.com/business/docs/iOS_Security_Guide.pdf
    private static final int BLOCK_SIZE = Property.DP_AESCBC_BLOCK_SIZE.intValue().orElse(4096);

    private final BlockCipher cipher;
    private final int blockLength;
    private IntFunction<byte[]> ivGenerator;
    private KeyParameter key;
    private boolean forEncryption;
    private int index;
    private int offset;

    DPAESCBC(
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

    DPAESCBC(BlockCipher cipher, int blockLength) {
        this(cipher, blockLength, null, null, false, 0, 0);
    }

    public DPAESCBC(int blockSize) {
        this(new CBCBlockCipher(new AESEngine()), blockSize);
    }

    public DPAESCBC() {
        this(BLOCK_SIZE);
    }

    @Override
    public void init(boolean forEncryption, CipherParameters params) throws IllegalArgumentException {
        if (!(params instanceof KeyParameter)) {
            throw new IllegalArgumentException("illegal params class: " + params.getClass());
        }

        key = (KeyParameter) params;
        this.forEncryption = forEncryption;

        ivGenerator = new DPAESCBCBlockIVGenerator(key.getKey());
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
