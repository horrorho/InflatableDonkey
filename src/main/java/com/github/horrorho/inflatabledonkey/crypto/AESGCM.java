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
package com.github.horrorho.inflatabledonkey.crypto;

import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Arrays;

/**
 * AES GCM. AES Galois/Counter Mode.
 *
 * @author Ahseya
 */
@Immutable
public final class AESGCM {

    /**
     * Returns decrypted data.
     *
     * @param key
     * @param nonce nonce/ IV
     * @param header
     * @param encryptedData
     * @param tag
     * @param optional optional AADBytes (post header)
     * @return decrypted data
     * @throws IllegalArgumentException on decryption exceptions
     * @throws NullPointerException on null arguments
     */
    public static byte[] decrypt(
            byte[] key,
            byte[] nonce,
            byte[] header,
            byte[] encryptedData,
            byte[] tag,
            Optional<byte[]> optional) {

        try {
            GCMBlockCipher cipher = new GCMBlockCipher(new AESFastEngine());
            AEADParameters parameters = new AEADParameters(new KeyParameter(key), tag.length * 8, nonce, header);
            cipher.init(false, parameters);

            if (optional.isPresent()) {
                byte[] aadBytes = optional.get();
                cipher.processAADBytes(aadBytes, 0, aadBytes.length);
            }

            byte[] out = new byte[cipher.getOutputSize(encryptedData.length + tag.length)];

            int pos = cipher.processBytes(encryptedData, 0, encryptedData.length, out, 0);
            pos += cipher.processBytes(tag, 0, tag.length, out, pos);
            pos += cipher.doFinal(out, pos);

            return Arrays.copyOf(out, pos);

        } catch (IllegalStateException | InvalidCipherTextException ex) {
            throw new IllegalStateException("GCM decrypt error", ex);
        }
    }
}
