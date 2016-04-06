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
package com.github.horrorho.inflatabledonkey.der;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.util.encoders.Hex;

/**
 * Elliptic private key with optional public info.
 *
 * @author Ahseya
 */
@Immutable
public final class PrivateKey extends ASN1Object {

    /*
        Template:
    
        SEQUENCE (2)		 
         OCTET_STRING       byte[] privateKey
         appl [1] optional  PublicKeyInfo publicKeyInfo
     */
    private final byte[] privateKey;
    private final Optional<PublicKeyInfo> publicKeyInfo;

    public PrivateKey(byte[] privateKey, Optional<PublicKeyInfo> publicKeyInfo) {
        this.privateKey = Arrays.copyOf(privateKey, privateKey.length);
        this.publicKeyInfo = Objects.requireNonNull(publicKeyInfo, "publicKeyInfo");
    }

    public PrivateKey(ASN1Primitive primitive) {
        DERIterator i = DER.asSequence(primitive);

        privateKey = DER.as(DEROctetString.class, i)
                .getOctets();

        publicKeyInfo = i.optional()
                .map(PublicKeyInfo::new);
    }

    public byte[] privateKey() {
        return Arrays.copyOf(privateKey, privateKey.length);
    }

    public Optional<PublicKeyInfo> publicKeyInfo() {
        return publicKeyInfo;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {

        ASN1EncodableVector vector = DER.vector(
                new DEROctetString(privateKey()),
                publicKeyInfo.orElse(null));

        return new DERSequence(vector);
    }

    @Override
    public String toString() {
        return "PrivateKey{"
                + "privateKey=0x" + Hex.toHexString(privateKey)
                + ", publicKeyInfo=" + publicKeyInfo
                + '}';
    }
}
