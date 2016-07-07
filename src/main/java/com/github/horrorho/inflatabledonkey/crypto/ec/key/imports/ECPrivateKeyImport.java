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
package com.github.horrorho.inflatabledonkey.crypto.ec.key.imports;

import com.github.horrorho.inflatabledonkey.crypto.ec.key.ECKeyFactories;
import com.github.horrorho.inflatabledonkey.crypto.ec.key.ECPrivateKey;
import com.github.horrorho.inflatabledonkey.crypto.ec.ECAssistant;
import java.math.BigInteger;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.BigIntegers;
import java.util.Optional;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ECPrivateKeyImport. Format: data = d
 *
 * @author Ahseya
 */
@Immutable
public final class ECPrivateKeyImport implements ECKeyImport<ECPrivateKey> {

    private static final Logger logger = LoggerFactory.getLogger(ECPrivateKeyImport.class);

    public static ECPrivateKeyImport instance() {
        return INSTANCE;
    }

    private static final ECPrivateKeyImport INSTANCE
            = new ECPrivateKeyImport();

    private ECPrivateKeyImport() {
    }

    @Override
    public Optional<ECPrivateKey> importKey(String curveName, byte[] data) {
        int fieldLength = ECAssistant.fieldLength(curveName);
        if (fieldLength(data.length) != fieldLength) {
            logger.warn("-- importKey() - bad data length: {} curve: {} data:0x{}",
                    data.length, curveName, Hex.toHexString(data));
        }

        BigInteger d = BigIntegers.fromUnsignedByteArray(data, 0, fieldLength);

        return ECKeyFactories.privateKeyFactory()
                .createECPrivateKey(d, curveName);
    }

    @Override
    public int fieldLength(int dataLength) {
        return dataLength > 0
                ? dataLength
                : -1;
    }
}
