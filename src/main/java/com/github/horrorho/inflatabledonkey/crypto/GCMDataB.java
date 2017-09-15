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

import javax.annotation.concurrent.Immutable;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Arrays;

/**
 * Unspecified AES GCM data type.
 * <p>
 * Format = 16 byte nonce || ciphertext || 16 byte tag
 *
 * @author Ahseya
 */
@Immutable
public final class GCMDataB {

    private static final int NONCE_LENGTH = 0x10;
    private static final int TAG_LENGTH = 0x10;

    private GCMDataB() {
    }

    public static byte[] decrypt(byte[] key, byte[] data) {
        // TODO utilize GCMAES#decrypt method
        try {
            if (data.length < NONCE_LENGTH + TAG_LENGTH) {
                throw new IllegalArgumentException("data packet too short");
            }

            int cipherTextLength = data.length - NONCE_LENGTH - TAG_LENGTH;

            byte[] nonce = Arrays.copyOf(data, NONCE_LENGTH);

            GCMBlockCipher cipher = new GCMBlockCipher(new AESFastEngine());
            AEADParameters parameters = new AEADParameters(new KeyParameter(key), TAG_LENGTH * 8, nonce);
            cipher.init(false, parameters);

            byte[] out = new byte[cipher.getOutputSize(cipherTextLength + TAG_LENGTH)];

            int pos = cipher.processBytes(data, NONCE_LENGTH, data.length - NONCE_LENGTH, out, 0);
            pos += cipher.doFinal(out, pos);

            return Arrays.copyOf(out, pos);

        } catch (IllegalStateException | InvalidCipherTextException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
