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
import net.jcip.annotations.Immutable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.util.encoders.Hex;

/**
 * SignatureInfo.
 *
 * @author Ahseya
 */
@Immutable
public final class SignatureInfo extends ASN1Object {

    // TODO locate template/ re-examine as possibly nested types
    /*        
        SEQUENCE (1)		
         SEQUENCE (2)
          INTEGER       version		
          OCTET_STRING  info
     */
    private final int version;
    private final byte[] info;

    public SignatureInfo(int version, byte[] info) {
        this.version = version;
        this.info = Arrays.copyOf(info, info.length);
    }

    public SignatureInfo(ASN1Primitive primitive) {
        DERIterator i = DER.asSequence(primitive);

        DERIterator j = DER.asSequence(i);

        version = DER.as(ASN1Integer.class, j)
                .getValue()
                .intValue();

        info = DER.as(DEROctetString.class, j)
                .getOctets();
    }

    public int version() {
        return version;
    }

    public byte[] info() {
        return Arrays.copyOf(info, info.length);
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector inner = DER.vector(
                new ASN1Integer(version),
                new DEROctetString(info()));

        ASN1EncodableVector outer = DER.vector(
                new DERSequence(inner));

        return new DERSequence(outer);
    }

    @Override
    public String toString() {
        return "SignatureInfo{"
                + "version=" + version
                + ", info=" + Hex.toHexString(info)
                + '}';
    }
}
