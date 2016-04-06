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
package com.github.horrorho.inflatabledonkey.data.der;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class ObjectSignature extends ASN1Object {
//TODO rename

    /*
        Template:
    
        SEQUENCE (2)
         cont [0] OPTIONAL  Object
          SEQUENCE (1)
           SEQUENCE (2)
            INTEGER
            OCTET_STRING
         cont [1] OPTIONAL  Signature
          SEQUENCE (3)
           OCTET_STRING
           INTEGER
           OCTET_STRING
     */
    private static final int SIGNATURE_INFO = 0;
    private static final int SIGNATURE = 1;

    private final Optional<SignatureInfo> signatureInfo;
    private final Optional<Signature> signature;

    public ObjectSignature(Optional<SignatureInfo> signatureInfo, Optional<Signature> signature) {
        this.signatureInfo = Objects.requireNonNull(signatureInfo, "signatureInfo");
        this.signature = Objects.requireNonNull(signature, "signature");
    }

    public ObjectSignature(ASN1Primitive primitive) {
        DERIterator i = DER.asSequence(primitive);
        Map<Integer, ASN1Primitive> tagged = i.derTaggedObjects();

        signatureInfo = Optional.ofNullable(tagged.get(SIGNATURE_INFO))
                .map(SignatureInfo::new);

        signature = Optional.ofNullable(tagged.get(SIGNATURE))
                .map(Signature::new);
    }

    public Optional<SignatureInfo> signatureInfo() {
        return signatureInfo;
    }

    public Optional<Signature> signature() {
        return signature;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {

        DERTaggedObject signatureInfoEncodable = signatureInfo
                .map(e -> new DERTaggedObject(SIGNATURE_INFO, e))
                .orElseGet(null);

        DERTaggedObject signatureEncodable = signature
                .map(e -> new DERTaggedObject(SIGNATURE, e))
                .orElseGet(null);

        ASN1EncodableVector vector = DER.vector(signatureInfoEncodable, signatureEncodable);

        return new DERSequence(vector);
    }

    @Override
    public String toString() {
        return "DeviceTimestampSignature{"
                + "timestamp=" + signatureInfo
                + ", signature=" + signature
                + '}';
    }
}
