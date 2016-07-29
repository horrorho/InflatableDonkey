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
package com.github.horrorho.inflatabledonkey.crypto.xts;

import java.util.Arrays;
import java.util.Objects;
import net.jcip.annotations.NotThreadSafe;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.params.KeyParameter;

/**
 *
 * @author Ahseya
 */
@NotThreadSafe
class XTSCore {

    private static final int BLOCK_SIZE = 16;

    private final BlockCipher cipher;
    private final XTSTweak tweak;
    private boolean forEncryption;

    XTSCore(BlockCipher cipher, XTSTweak tweak) {
        this.cipher = Objects.requireNonNull(cipher, "cipher");
        this.tweak = Objects.requireNonNull(tweak, "tweak");
    }

    XTSCore(XTSTweak tweak) {
        this(new AESFastEngine(), tweak);
    }

    XTSCore() {
        this(new XTSTweak());
    }

    XTSCore init(boolean forEncryption, KeyParameter key) throws IllegalArgumentException {
        byte[] k = ((KeyParameter) key).getKey();
        if (k.length != 32 && k.length != 64) {
            throw new IllegalArgumentException("bad key length: " + k.length);
        }

        byte[] key1 = Arrays.copyOfRange(k, 0, k.length / 2);
        byte[] key2 = Arrays.copyOfRange(k, k.length / 2, k.length);

        return init(forEncryption, new KeyParameter(key1), new KeyParameter(key2));
    }

    XTSCore init(boolean forEncryption, KeyParameter key1, KeyParameter key2) throws IllegalArgumentException {
        cipher.init(forEncryption, key1);
        tweak.init(key2);
        this.forEncryption = forEncryption;
        return this;
    }

    XTSCore reset(long tweakValue) throws DataLengthException, IllegalStateException {
        tweak.reset(tweakValue);
        return this;
    }

    String getAlgorithmName() {
        return cipher.getAlgorithmName();
    }

    int getBlockSize() {
        return BLOCK_SIZE;
    }

    int processBlock(byte[] in, int inOff, byte[] out, int outOff) throws DataLengthException, IllegalStateException {
        byte[] tweakValue = tweak.value();
        doProcessBlock(in, inOff, out, outOff, tweakValue);
        tweak.next();
        return BLOCK_SIZE;
    }

    int doProcessBlock(byte[] in, int inOff, byte[] out, int outOff, byte[] tweakValue)
            throws DataLengthException, IllegalStateException {

        merge(in, inOff, out, outOff, tweakValue);
        cipher.processBlock(out, outOff, out, outOff);
        merge(out, outOff, out, outOff, tweakValue);
        return BLOCK_SIZE;
    }

    void merge(byte[] in, int inOff, byte[] out, int outOff, byte[] tweak) {
        for (int i = 0; i < BLOCK_SIZE; i++) {
            out[i + outOff] = (byte) (in[i + inOff] ^ tweak[i]);
        }
    }

    int processPartial(byte[] in, int inOff, byte[] out, int outOff, int length) {
        if (length <= BLOCK_SIZE) {
            throw new DataLengthException("input buffer too small/ missing last two blocks: " + length);
        }

        if (length >= BLOCK_SIZE * 2) {
            throw new DataLengthException("input buffer too large/ non-partial final block: " + length);
        }

        byte[] tweakA = tweak.value();
        byte[] tweakB = tweak.next().value();

        return forEncryption
                ? XTSCore.this.doProcessPartial(in, inOff, out, outOff, length, tweakA, tweakB)
                : XTSCore.this.doProcessPartial(in, inOff, out, outOff, length, tweakB, tweakA);
    }

    int doProcessPartial(byte[] in, int inOff, byte[] out, int outOff, int length, byte[] tweakA, byte[] tweakB)
            throws DataLengthException, IllegalStateException {
        // M-1 block
        doProcessBlock(in, inOff, out, outOff, tweakA);

        // Cipher stealing
        byte[] buffer = Arrays.copyOfRange(out, outOff, outOff + BLOCK_SIZE);
        System.arraycopy(in, inOff + BLOCK_SIZE, buffer, 0, length - BLOCK_SIZE);

        // M block
        doProcessBlock(buffer, 0, buffer, 0, tweakB);

        // Copy blocks
        System.arraycopy(out, outOff, out, outOff + BLOCK_SIZE, length - BLOCK_SIZE);
        System.arraycopy(buffer, 0, out, outOff, BLOCK_SIZE);
        return length;
    }
}
