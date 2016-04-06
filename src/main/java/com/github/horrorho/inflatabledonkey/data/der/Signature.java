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
import net.jcip.annotations.Immutable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.util.encoders.Hex;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class Signature extends ASN1Object {

    /*
        SEQUENCE (3)
         OCTET_STRING  signerKeyID
         INTEGER       type
         OCTET_STRING  signature
     */
    private final byte[] signerKeyID;
    private final int type;
    private final byte[] data;

    public Signature(byte[] signerKeyID, int type, byte[] data) {
        this.signerKeyID = Arrays.copyOf(signerKeyID, signerKeyID.length);
        this.type = type;
        this.data = Arrays.copyOf(data, data.length);
    }

    public Signature(ASN1Primitive primitive) {
        DERIterator i = DER.asSequence(primitive);

        signerKeyID = DER.as(DEROctetString.class, i)
                .getOctets();

        type = DER.as(ASN1Integer.class, i)
                .getValue()
                .intValue();

        data = DER.as(DEROctetString.class, i)
                .getOctets();
    }

    public byte[] signerKeyID() {
        return Arrays.copyOf(signerKeyID, signerKeyID.length);
    }

    public int type() {
        return type;
    }

    public byte[] data() {
        return Arrays.copyOf(data, data.length);
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector vector = DER.vector(
                new DEROctetString(signerKeyID()),
                new ASN1Integer(type),
                new DEROctetString(data()));

        return new DERSequence(vector);
    }

    @Override
    public String toString() {
        return "Signature{"
                + "signerKeyID=" + Hex.toHexString(signerKeyID)
                + ", type=" + type
                + ", data=" + Hex.toHexString(data)
                + '}';
    }
}
