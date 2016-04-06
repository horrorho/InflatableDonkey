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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.util.encoders.Hex;

/**
 * SEC1 EC private key.
 *
 * @author Ahseya
 */
@Immutable
public final class SECPrivateKey extends ASN1Object {

    private static final int PARAMETERS = 0;
    private static final int PUBLIC_KEY = 0;
    /*  SEC 1
                ECPrivateKey ::= SEQUENCE {
                    version         INTEGER { ecPrivkeyVer1(1) } (ecPrivkeyVer1), 
                    privateKey      OCTET STRING, 
                    parameters  [0] Parameters OPTIONAL, 
                    publicKey   [1] BIT STRING OPTIONAL }
     */
    private final int version;
    private final byte[] privateKey;
    private final Optional<byte[]> parameters;
    private final Optional<byte[]> publicKey;

    public SECPrivateKey(int version, byte[] privateKey, Optional<byte[]> parameters, Optional<byte[]> publicKey) {
        this.version = Objects.requireNonNull(version, "version");
        this.privateKey = Objects.requireNonNull(privateKey, "privateKey");
        this.parameters = Objects.requireNonNull(parameters, "parameters");
        this.publicKey = Objects.requireNonNull(publicKey, "publicKey");
    }

    public SECPrivateKey(ASN1Primitive primitive) {
        DERIterator i = DER.asSequence(primitive);
        Map<Integer, ASN1Primitive> tagged = i.derTaggedObjects();

        version = DER.as(ASN1Integer.class, i)
                .getValue()
                .intValue();

        privateKey = DER.as(DEROctetString.class, i)
                .getOctets();

        parameters = Optional.ofNullable(tagged.get(PARAMETERS))
                .map(DER.as(DEROctetString.class))
                .map(ASN1OctetString::getOctets);

        publicKey = Optional.ofNullable(tagged.get(PUBLIC_KEY))
                .map(DER.as(DERBitString.class))
                .map(DERBitString::getBytes);
    }

    public int version() {
        return version;
    }

    public byte[] privateKey() {
        return Arrays.copyOf(privateKey, privateKey.length);
    }

    public Optional<byte[]> parameters() {
        return parameters.map(a -> Arrays.copyOf(a, a.length));
    }

    public Optional<byte[]> publicKey() {
        return publicKey.map(a -> Arrays.copyOf(a, a.length));
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        DERTaggedObject parametersEncodable = parameters()
                .map(DEROctetString::new)
                .map(e -> new DERTaggedObject(PARAMETERS, e))
                .orElseGet(null);

        DERTaggedObject publicKeyEncodable = publicKey()
                .map(DERBitString::new)
                .map(e -> new DERTaggedObject(PUBLIC_KEY, e))
                .orElseGet(null);

        ASN1EncodableVector vector = DER.vector(
                new ASN1Integer(version),
                new DEROctetString(privateKey),
                parametersEncodable,
                publicKeyEncodable);

        return new DERSequence(vector);
    }

    @Override
    public String toString() {
        return "SECPrivateKey{"
                + "version=" + version
                + ", privateKey=" + Hex.toHexString(privateKey)
                + ", parameters=" + parameters.map(Hex::toHexString).orElse(null)
                + ", publicKey=" + publicKey.map(Hex::toHexString).orElse(null)
                + '}';
    }
}
