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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;
import net.jcip.annotations.NotThreadSafe;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.params.KeyParameter;

/**
 * LFSR / AES block IV generator.
 *
 * @author Ahseya
 */
@NotThreadSafe
public class DPAESCBCBlockIVGenerator implements IntFunction<byte[]> {

    // 'The initialization vector (IV) is the output of a linear feedback shift register (LFSR) calculated with the 
    // block offset into the file, encrypted with the SHA-1 hash of the per-file key.'
    // Apple: iOS Security. February 2014.
    // https://www.apple.com/br/ipad/business/docs/iOS_Security_EN_Feb14.pdf
    static BlockCipher cipher(byte[] fileKey) {
        Digest digest = new SHA1Digest();
        byte[] hash = new byte[digest.getDigestSize()];
        digest.reset();
        digest.update(fileKey, 0, fileKey.length);
        digest.doFinal(hash, 0);

        AESFastEngine cipher = new AESFastEngine();
        int blockSize = cipher.getBlockSize();

        KeyParameter keyParameter = new KeyParameter(Arrays.copyOfRange(hash, 0, blockSize));
        cipher.init(true, keyParameter);

        return cipher;
    }

    private final BlockCipher cipher;

    DPAESCBCBlockIVGenerator(BlockCipher cipher) {
        this.cipher = Objects.requireNonNull(cipher, "cipher");
    }

    public DPAESCBCBlockIVGenerator(byte[] fileKey) {
        this(cipher(fileKey));
    }

    @Override
    public byte[] apply(int blockOffset) {
        byte[] lfsr = lfsr(blockOffset);
        byte[] out = new byte[cipher.getBlockSize()];

        cipher.processBlock(lfsr, 0, out, 0);

        return out;
    }

    byte[] lfsr(int blockOffset) {
        ByteBuffer buffer = ByteBuffer.allocate(cipher.getBlockSize())
                .order(ByteOrder.LITTLE_ENDIAN);

        int r = blockOffset << 12;
        while (buffer.hasRemaining()) {
            r = r >>> 1;
            if ((r & 1) == 1) {
                r = r ^ 0x80000061;
            }
            buffer.putInt(r);
        }

        return buffer.array();
    }
}
