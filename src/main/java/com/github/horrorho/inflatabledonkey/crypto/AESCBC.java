/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.crypto;

import java.util.Arrays;
import net.jcip.annotations.Immutable;
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
