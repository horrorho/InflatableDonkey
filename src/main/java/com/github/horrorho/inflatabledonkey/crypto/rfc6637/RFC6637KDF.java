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
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;
import net.jcip.annotations.Immutable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;

/**
 * RFC6637 KDF.
 *
 * @author Ahseya
 */
@Immutable
public final class RFC6637KDF {

    private static final byte[] ANONYMOUS_SENDER = Hex.decode("416E6F6E796D6F75732053656E64657220202020");

    private final Supplier<Digest> digestFactory;
    private final byte[] formattedOid;
    private final byte publicKeyAlgID;
    private final byte symAlgID;
    private final byte kdfHashID;

    public RFC6637KDF(
            Supplier<Digest> digestFactory,
            byte[] formattedOid,
            byte publicKeyAlgID,
            byte symAlgID,
            byte kdfHashID) {

        this.digestFactory = Objects.requireNonNull(digestFactory);
        this.formattedOid = Arrays.copyOf(formattedOid, formattedOid.length);
        this.publicKeyAlgID = publicKeyAlgID;
        this.symAlgID = symAlgID;
        this.kdfHashID = kdfHashID;
    }

    public RFC6637KDF(
            Supplier<Digest> digestFactory,
            ASN1ObjectIdentifier oid,
            byte publicKeyAlgID,
            byte symAlgID,
            byte kdfHashID) {
        this(digestFactory, formatOid(oid), publicKeyAlgID, symAlgID, kdfHashID);
    }

    public byte[] apply(ECPoint S, byte[] fingerprint) throws IOException {
        // RFC Sections 7, 8
        byte[] ZB = S.getAffineXCoord().getEncoded();

        Digest digest = digestFactory.get();

        digest.update((byte) 0x00);                                     // 00
        digest.update((byte) 0x00);                                     // 00
        digest.update((byte) 0x00);                                     // 00 
        digest.update((byte) 0x01);                                     // 01 
        digest.update(ZB, 0, ZB.length);                                // ZB

        // Params
        digest.update(formattedOid, 0, formattedOid.length);            // curve_OID_len || curve_OID 
        digest.update(publicKeyAlgID);                                  // public_key_alg_ID
        digest.update((byte) 0x03);                                     // 03
        digest.update((byte) 0x01);                                     // 01
        digest.update(kdfHashID);                                       // KDF_hash_ID
        digest.update(symAlgID);                                        // KEK_alg_ID for AESKeyWrap
        digest.update(ANONYMOUS_SENDER, 0, ANONYMOUS_SENDER.length);    // "Anonymous Sender    "
        digest.update(fingerprint, 0, fingerprint.length);              // recipient_fingerprint

        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return hash;
    }

    public static byte[] formatOid(ASN1ObjectIdentifier oid) {
        try {
            byte[] encodedOid = oid.getEncoded();
            byte[] formattedOid = new byte[encodedOid.length - 1];
            System.arraycopy(encodedOid, 1, formattedOid, 0, formattedOid.length);
            return formattedOid;

        } catch (IOException ex) {
            throw new IllegalArgumentException("format OID failed", ex);
        }
    }
}
