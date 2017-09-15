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

import java.nio.ByteBuffer;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.function.Supplier;
import javax.annotation.concurrent.Immutable;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.generators.KDFCounterBytesGenerator;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KDFCounterParameters;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NIST SP800-108 key derivation.
 *
 * @author Ahseya
 */
@Immutable
public final class NISTKDF {

    private static final Logger logger = LoggerFactory.getLogger(NISTKDF.class);

    private static final int R = 32;            // counter length in bits 

    private NISTKDF() {
    }

    public static byte[]
            ctrHMac(byte[] keyDerivationKey, String label, Supplier<Digest> digestSupplier, int keyLengthBytes) {

        return ctrHMac(keyDerivationKey, label.getBytes(UTF_8), digestSupplier, keyLengthBytes);
    }

    // KDF in Counter Mode with an HMAC PRF
    // TODO document exceptions 
    public static byte[]
            ctrHMac(byte[] keyDerivationKey, byte[] label, Supplier<Digest> digestSupplier, int keyLengthBytes) {

        logger.trace("<< ctrHMac() - keyDerivationKey: 0x{} label: {} digestSupplier: {} length: {}",
                Hex.toHexString(keyDerivationKey), Hex.toHexString(label), digestSupplier, keyLengthBytes);

        byte[] derivedKey = new byte[keyLengthBytes];

        // fixedInputData = label || 0x00 || dkLen in bits as 4 bytes big endian
        ByteBuffer buffer = ByteBuffer.allocate(label.length + 5);
        buffer.put(label);
        buffer.put((byte) 0);
        buffer.putInt(keyLengthBytes * 8);
        byte[] fixedInputData = buffer.array();
        logger.debug("-- ctrHMac() - fixed input data: 0x{}", Hex.toHexString(fixedInputData));

        HMac hMac = new HMac(digestSupplier.get());
        KDFCounterBytesGenerator generator = new KDFCounterBytesGenerator(hMac);
        generator.init(new KDFCounterParameters(keyDerivationKey, fixedInputData, R));
        generator.generateBytes(derivedKey, 0, derivedKey.length);

        logger.trace(">> ctrHMac() - derivedKey: 0x{}", Hex.toHexString(derivedKey));
        return derivedKey;
    }
}
// TODO test
