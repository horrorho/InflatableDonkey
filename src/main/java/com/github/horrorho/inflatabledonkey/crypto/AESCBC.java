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

import java.util.Arrays;
import javax.annotation.concurrent.Immutable;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

/**
 * AES CBC.
 *
 * @author Ahseya
 */
@Immutable
public final class AESCBC {

    private AESCBC() {
    }

    public static byte[] decryptAESCBC(byte[] key, byte[] iv, byte[] data) {
        // AES CBC PKCS7 decrypt
        try {
            CipherParameters cipherParameters = new ParametersWithIV(new KeyParameter(key), iv);
            PaddedBufferedBlockCipher cipher
                    = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESFastEngine()), new PKCS7Padding());
            cipher.init(false, cipherParameters);

            byte[] buffer = new byte[cipher.getOutputSize(data.length)];

            int pos = cipher.processBytes(data, 0, data.length, buffer, 0);
            pos += cipher.doFinal(buffer, pos);

            return Arrays.copyOf(buffer, pos);

        } catch (DataLengthException | IllegalStateException | InvalidCipherTextException ex) {
            throw new IllegalArgumentException("decrypt failed", ex);
        }
    }
}
