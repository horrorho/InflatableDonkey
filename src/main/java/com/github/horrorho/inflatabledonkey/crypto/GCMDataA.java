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

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unspecified AES GCM data type.
 * <p>
 * Format = header || nonce || tag || data
 *
 * @author Ahseya
 */
@Immutable
public final class GCMDataA {

    private static final Logger logger = LoggerFactory.getLogger(GCMDataA.class);

    private GCMDataA() {
    }

    public static byte[] decrypt(byte[] key, byte[] data) {
        return decrypt(key, data, Optional.empty());
    }

    /**
     * Returns the decrypted data.
     *
     * @param key key
     * @param data encrypted data
     * @param optional optional AADBytes (post header)
     * @return decrypted data
     * @throws IllegalArgumentException on decryption exceptions
     * @throws NullPointerException on null arguments
     */
    public static byte[] decrypt(byte[] key, byte[] data, Optional<byte[]> optional) {
        try {
            // Network byte orderered data.
            ByteBuffer buffer = ByteBuffer.wrap(data);
            buffer.order(ByteOrder.BIG_ENDIAN);

            int version = Byte.toUnsignedInt(buffer.get());
            logger.debug("-- decrypt() - version: {}", version);
            buffer.rewind();

            switch (version) {
                case 0:
                    // Not verified
                    return doDecrypt(buffer, key, 1, 8, 8, optional);

                case 1:
                    // Not verified
                    return doDecrypt(buffer, key, 1, 12, 12, optional);

                case 2:
                    // Not verified
                    return doDecrypt(buffer, key, 3, 12, 12, optional);

                case 3:
                    if (buffer.limit() < 4) {
                        throw new IllegalArgumentException("cipher text too short for header");
                    }

                    int headerLength = 4 + Byte.toUnsignedInt(buffer.get(3));
                    return doDecrypt(buffer, key, headerLength, 12, 12, optional);

                default:
                    throw new IllegalArgumentException("unsupported version: " + version);
            }
        } catch (BufferUnderflowException ex) {
            throw new IllegalArgumentException("decryption exception", ex);
        }
    }

    static byte[] doDecrypt(
            ByteBuffer buffer,
            byte[] key,
            int headerLength,
            int nonceLength,
            int tagLength,
            Optional<byte[]> optional) {

        logger.trace("<< doDecrypt() - data: {} key: {} headerLength: {} nonce length: {} tag length: {}",
                Hex.toHexString(buffer.array()), Hex.toHexString(key), headerLength, nonceLength, tagLength);

        int cipherTextLength = buffer.limit() - headerLength - nonceLength - tagLength;
        if (cipherTextLength < 0) {
            throw new IllegalArgumentException("cipher text too short");
        }

        byte[] header = new byte[headerLength];
        buffer.get(header);
        logger.debug("-- doDecrypt() - header: 0x{}", Hex.toHexString(header));

        byte[] nonce = new byte[nonceLength];
        buffer.get(nonce);
        logger.debug("-- doDecrypt() - nonce: 0x{}", Hex.toHexString(nonce));

        byte[] tag = new byte[tagLength];
        buffer.get(tag);
        logger.debug("-- doDecrypt() - tag: 0x{}", Hex.toHexString(tag));

        byte[] encrypted = new byte[cipherTextLength];
        buffer.get(encrypted);
        logger.debug("-- doDecrypt() - encrypted data: 0x{}", Hex.toHexString(encrypted));

        if (optional.isPresent()) {
            logger.debug("-- doDecrypt() - optional: 0x{}", Hex.toHexString(optional.get()));
        }

        byte[] decrypted = GCMAES.decrypt(key, nonce, header, encrypted, tag, optional);

        logger.trace(">> doDecrypt() - decrypted: 0x{}", Hex.toHexString(decrypted));
        return decrypted;
    }
}
