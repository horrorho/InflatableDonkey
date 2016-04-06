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

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class PublicKeyInfo extends ASN1Object {

    /*
        Template:
    
        appl [1] 
         SEQUENCE (6)
          INTEGER           int service
          INTEGER           int type
          OCTET_STRING      byte[] key
          cont [0] OPTIONAL  SignatureInfo signatureInfo
          cont [1] OPTIONAL  Signature signature;
          cont [2] OPTIONAL  ObjectSignature extendedSignature
     */
    public static final int APPLICATION_TAG = 1;

    private static final int SIGNATURE_INFO = 0;
    private static final int SIGNATURE = 1;
    private static final int EXTENDED_SIGNATURE = 2;

    private static final Logger logger = LoggerFactory.getLogger(PublicKeyInfo.class);

    private final int service;
    private final int type;
    private final byte[] key;
    private final Optional<SignatureInfo> signatureInfo;
    private final Optional<Signature> signature;
    private final Optional<ObjectSignature> extendedSignature;

    public PublicKeyInfo(
            int service,
            int type,
            byte[] key,
            Optional<SignatureInfo> signatureInfo,
            Optional<Signature> signature,
            Optional<ObjectSignature> extendedSignature) {

        this.service = service;
        this.type = type;
        this.key = Objects.requireNonNull(key, "key");
        this.signatureInfo = Objects.requireNonNull(signatureInfo, "signatureInfo");
        this.signature = Objects.requireNonNull(signature);
        this.extendedSignature = Objects.requireNonNull(extendedSignature, "extendedSignature");
    }

    public PublicKeyInfo(ASN1Primitive primitive) {
        ASN1Primitive app = DER.asApplicationSpecific(APPLICATION_TAG, primitive);

        DERIterator i = DER.asSequence(app);
        Map<Integer, ASN1Primitive> tagged = i.derTaggedObjects();

        service = DER.as(ASN1Integer.class, i)
                .getValue()
                .intValue();

        type = DER.as(ASN1Integer.class, i)
                .getValue()
                .intValue();

        key = DER.as(DEROctetString.class, i)
                .getOctets();

        signatureInfo = Optional.ofNullable(tagged.get(SIGNATURE_INFO))
                .map(SignatureInfo::new);

        signature = Optional.ofNullable(tagged.get(SIGNATURE))
                .map(Signature::new);

        extendedSignature = Optional.ofNullable(tagged.get(EXTENDED_SIGNATURE))
                .map(ObjectSignature::new);
    }

    public int service() {
        return service;
    }

    public int type() {
        return type;
    }

    public byte[] key() {
        return Arrays.copyOf(key, key.length);
    }

    public Optional<SignatureInfo> buildAndTime() {
        return signatureInfo;
    }

    public Optional<Signature> signature() {
        return signature;
    }

    public Optional<ObjectSignature> extendedSignature() {
        return extendedSignature;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {

        DERTaggedObject signatureInfoEncodable
                = signatureInfo.map(e -> new DERTaggedObject(SIGNATURE_INFO, e))
                .orElse(null);

        DERTaggedObject signatureEncodable
                = signature.map(e -> new DERTaggedObject(SIGNATURE, e))
                .orElse(null);

        DERTaggedObject extendedSignatureEncodable
                = extendedSignature.map(e -> new DERTaggedObject(EXTENDED_SIGNATURE, e))
                .orElse(null);

        ASN1EncodableVector vector = DER.vector(
                new ASN1Integer(service),
                new ASN1Integer(type),
                new DEROctetString(key()),
                signatureInfoEncodable,
                signatureEncodable,
                extendedSignatureEncodable);

        DERSequence sequence = new DERSequence(vector);
        return DER.toApplicationSpecific(APPLICATION_TAG, sequence);
    }

    @Override
    public String toString() {
        return "PublicKeyInfo{"
                + "id=" + service
                + ", type=" + type
                + ", key=" + Hex.encodeHexString(key)
                + ", buildAndTime=" + signatureInfo
                + ", signature=" + signature
                + ", extendedSignature=" + extendedSignature
                + '}';
    }
}
