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
package com.github.horrorho.inflatabledonkey.crypto.xblock;

import java.util.Arrays;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;

/**
 * XBlockCiphers.
 *
 * @author Ahseya
 */
@Immutable
public final class XBlockCiphers {

    private static final int BLOCK_LENGTH = 0x1000;

    public static XBlockCipher create(byte[] key) {
        XBlockIndexHash blockIndexHash = blockIndexHash(key);
        KeyParameter keyParameter = new KeyParameter(key);
        BlockCipher cipher = new CBCBlockCipher(new AESEngine());

        return new XBlockCipher(cipher, blockIndexHash, keyParameter, BLOCK_LENGTH);
    }

    static XBlockIndexHash blockIndexHash(byte[] key) {
        Digest digest = new SHA1Digest();
        byte[] out = new byte[digest.getDigestSize()];
        digest.reset();
        digest.update(key, 0, key.length);
        digest.doFinal(out, 0);

        AESFastEngine cipher = new AESFastEngine();
        int blockSize = cipher.getBlockSize();

        KeyParameter keyParameter = new KeyParameter(Arrays.copyOfRange(out, 0, blockSize));
        cipher.init(true, keyParameter);

        return new XBlockIndexHash(cipher);
    }
}
