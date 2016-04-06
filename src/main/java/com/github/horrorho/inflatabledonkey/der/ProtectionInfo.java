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
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.util.encoders.Hex;

/**
 * ProtectionInfo.
 *
 * @author Ahseya
 */
@Immutable
public final class ProtectionInfo extends ASN1Object {

    /*
        Template:

        appl [1]
         SEQUENCE  (7)
          SEQUENCE  (3)     EncryptedKeys encryptedKeys
          cont [0] OPTIONAL byte[] data
          cont [1] OPTIONAL TypeData signature
          OCTET_STRING      byte[] hmac
          cont [2] OPTIONAL byte[] tag
          cont [3] OPTIONAL undefined_1
          cont [4] OPTIONAL EXTERN 0x10068f1c

        undefined_1:
         SET  (1)
          SEQUENCE  (3)
           INTEGER
           INTEGER optional
           OCTET_STRING

        EXTERN 0x10068f1c:
         SEQUENCE  (1)
          SEQUENCE  (2)
           INTEGER
           OCTET_STRING
     */
    public static final int APPLICATION_TAG = 1;

    private static final int DATA = 0;
    private static final int SIGNATURE = 1;
    private static final int TAG = 2;
    private static final int CONT3 = 3;
    private static final int CONT4 = 4;

    private final EncryptedKeys encryptedKeys;
    private final Optional<byte[]> data;
    private final Optional<TypeData> signature;
    private final byte[] hmac;
    private final Optional<byte[]> tag;
    private final Optional<byte[]> cont3;
    private final Optional<byte[]> cont4;

    public ProtectionInfo(
            EncryptedKeys encryptedKeys,
            Optional<byte[]> data,
            Optional<TypeData> signature,
            byte[] hmac,
            Optional<byte[]> tag,
            Optional<byte[]> cont3,
            Optional<byte[]> cont4) {

        this.encryptedKeys = Objects.requireNonNull(encryptedKeys);
        this.data = Objects.requireNonNull(data, "data");
        this.signature = Objects.requireNonNull(signature, "signature");
        this.hmac = Arrays.copyOf(hmac, hmac.length);
        this.tag = Objects.requireNonNull(tag, "tag");
        this.cont3 = Objects.requireNonNull(cont3, "cont3");
        this.cont4 = Objects.requireNonNull(cont4, "cont4");
    }

    public ProtectionInfo(ASN1Primitive primitive) {
        ASN1Primitive app = DER.asApplicationSpecific(APPLICATION_TAG, primitive);
        DERIterator i = DER.asSequence(app);

        Map<Integer, ASN1Primitive> tagged = i.derTaggedObjects();

        encryptedKeys = new EncryptedKeys(i.next());

        hmac = DER.as(DEROctetString.class, i)
                .getOctets();

        data = Optional.ofNullable(tagged.get(DATA))
                .map(DER.as(DEROctetString.class))
                .map(ASN1OctetString::getOctets);

        signature = Optional.ofNullable(tagged.get(SIGNATURE))
                .map(TypeData::new);

        tag = Optional.ofNullable(tagged.get(TAG))
                .map(DER.as(DEROctetString.class))
                .map(ASN1OctetString::getOctets);

        cont3 = Optional.ofNullable(tagged.get(CONT3))
                .map(DER.as(DEROctetString.class))
                .map(ASN1OctetString::getOctets);

        cont4 = Optional.ofNullable(tagged.get(CONT4))
                .map(DER.as(DEROctetString.class))
                .map(ASN1OctetString::getOctets);
    }

    public EncryptedKeys encryptedKeys() {
        return encryptedKeys;
    }

    public Optional<byte[]> data() {
        return data.map(b -> Arrays.copyOf(b, b.length));
    }

    public Optional<TypeData> signature() {
        return signature;
    }

    public byte[] hmac() {
        return Arrays.copyOf(hmac, hmac.length);
    }

    public Optional<byte[]> tag() {
        return tag.map(b -> Arrays.copyOf(b, b.length));
    }

    public Optional<byte[]> cont3() {
        return cont3.map(b -> Arrays.copyOf(b, b.length));
    }

    public Optional<byte[]> cont4() {
        return cont4.map(b -> Arrays.copyOf(b, b.length));
    }

    @Override
    public ASN1Primitive toASN1Primitive() {

        DERTaggedObject dataEncodable = data
                .map(DEROctetString::new)
                .map(e -> new DERTaggedObject(DATA, e))
                .orElseGet(null);

        DERTaggedObject signatureEncodable = signature
                .map(e -> new DERTaggedObject(SIGNATURE, e))
                .orElseGet(null);

        DERTaggedObject tagEncodable = tag
                .map(DEROctetString::new)
                .map(e -> new DERTaggedObject(TAG, e))
                .orElseGet(null);

        DERTaggedObject cont3Encodable = cont3
                .map(DEROctetString::new)
                .map(e -> new DERTaggedObject(CONT3, e))
                .orElseGet(null);

        DERTaggedObject cont4Encodable = cont4
                .map(DEROctetString::new)
                .map(e -> new DERTaggedObject(CONT4, e))
                .orElseGet(null);

        ASN1EncodableVector vector = DER.vector(
                encryptedKeys,
                dataEncodable,
                signatureEncodable,
                new DEROctetString(hmac()),
                tagEncodable,
                cont3Encodable,
                cont4Encodable);

        DERSequence sequence = new DERSequence(vector);
        return DER.toApplicationSpecific(APPLICATION_TAG, sequence);
    }

    @Override
    public String toString() {
        return "ProtectionInfo{"
                + "encryptedKeys=" + encryptedKeys
                + ", data=" + data.map(Hex::toHexString)
                + ", signature=" + signature
                + ", hmac=" + Hex.toHexString(hmac)
                + ", tag=" + tag.map(Hex::toHexString)
                + ", cont3=" + cont3.map(Hex::toHexString)
                + ", cont4=" + cont4.map(Hex::toHexString)
                + '}';
    }
}
