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
package com.github.horrorho.inflatabledonkey.pcs.xfile;

import java.util.Arrays;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

/**
 * BlockDecrypters.
 *
 * @author Ahseya
 */
@Immutable
public final class BlockDecrypters {

    public static BlockDecrypter create(byte[] key) {
        BufferedBlockCipher cipher = new BufferedBlockCipher(new CBCBlockCipher(new AESFastEngine()));
        SHA1Digest digest = new SHA1Digest();
        return create(cipher, digest, key);
    }

    static BlockDecrypter create(BufferedBlockCipher cipher, Digest digest, byte[] key) {
        ParametersWithIV blockIVKey = blockIVKey(digest, cipher.getBlockSize(), key);
        KeyParameter keyParameter = new KeyParameter(key);

        return new BlockDecrypter(cipher, blockIVKey, keyParameter);
    }

    static ParametersWithIV blockIVKey(Digest digest, int length, byte[] key) {
        byte[] hash = new byte[digest.getDigestSize()];

        digest.reset();
        digest.update(key, 0, key.length);
        digest.doFinal(hash, 0);

        KeyParameter keyParameter = new KeyParameter(Arrays.copyOfRange(hash, 0, length));
        byte[] iv = new byte[length];

        return new ParametersWithIV(keyParameter, iv);
    }
}
