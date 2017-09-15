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

import java.util.function.Supplier;
import javax.annotation.concurrent.Immutable;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.generators.HKDFBytesGenerator;
import org.bouncycastle.crypto.params.HKDFParameters;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RFC5869: HMAC-based Extract-and-Expand Key Derivation Function (HKDF).
 *
 * @author Ahseya
 */
@Immutable
public final class RFC5869KDF {

    private static final Logger logger = LoggerFactory.getLogger(RFC5869KDF.class);

    private RFC5869KDF() {
    }

    public static final byte[]
            apply(byte[] ikm, byte[] salt, byte[] info, Supplier<Digest> digestSupplier, int keyLengthBytes) {
                
        logger.trace("<< apply() - ikm: 0x{} salt: 0x{} info: 0x{} digestSupplier: {} keyLengthBytes: {}",
                Hex.toHexString(ikm), Hex.toHexString(salt), Hex.toHexString(info), digestSupplier, keyLengthBytes);

        Digest hash = digestSupplier.get();
        byte[] okm = new byte[keyLengthBytes];

        HKDFParameters params = new HKDFParameters(ikm, salt, info);
        HKDFBytesGenerator hkdf = new HKDFBytesGenerator(hash);
        hkdf.init(params);
        hkdf.generateBytes(okm, 0, keyLengthBytes);

        logger.trace(">> apply() - output keying material: 0x{}", Hex.toHexString(okm));
        return okm;
    }
}
