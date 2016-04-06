/* 
 * The MIT License
 *
 * Copyright 2015 Ahseya.
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
package com.github.horrorho.inflatabledonkey.crypto.rfc6637;

import java.io.IOException;
import java.util.function.Supplier;
import net.jcip.annotations.ThreadSafe;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x9.ECNamedCurveTable;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.engines.RFC3394WrapEngine;

/**
 * Default curves.
 *
 * @author Ahseya
 */
@ThreadSafe
public class RFC6637Factory {
    
    public static synchronized RFC6637 secp521r1() {
        if (SECP521R1 == null) {
            SECP521R1 = create(
                    "secp521r1",
                    SHA512Digest::new,
                    () -> new RFC3394WrapEngine(new AESFastEngine()),
                    RFC6637Constants.ECDH,
                    RFC6637Constants.AES_256,
                    0x20,
                    RFC6637Constants.SHA512);
        }

        return SECP521R1;
    }

    public static synchronized RFC6637 secp256r1() {
        if (SECP256R1 == null) {
            SECP256R1 = create(
                    "secp256r1",
                    SHA256Digest::new,
                    () -> new RFC3394WrapEngine(new AESFastEngine()),
                    RFC6637Constants.ECDH,
                    RFC6637Constants.AES_128,
                    0x10,
                    RFC6637Constants.SHA256);
        }

        return SECP256R1;
    }

    private static RFC6637 create(
            String curveName,
            Supplier<Digest> digestFactory,
            Supplier<Wrapper> wrapperFactory,
            int publicKeyAlgID,
            int symAlgID,
            int symAlgIDLength,
            int kdfHashID) {

        try {
            ASN1ObjectIdentifier oid = ECNamedCurveTable.getOID(curveName);

            RFC6637KDF kdf = new RFC6637KDF(
                    digestFactory,
                    oid,
                    (byte) publicKeyAlgID,
                    (byte) symAlgID,
                    (byte) kdfHashID);

            return new RFC6637(wrapperFactory, curveName, symAlgIDLength, kdf);

        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private static RFC6637 SECP256R1;
    private static RFC6637 SECP521R1;
}
