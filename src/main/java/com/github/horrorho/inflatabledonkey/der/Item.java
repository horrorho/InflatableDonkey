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
 * Item.
 * <p>
 * Item ::= SEQUENCE { version INTEGER, data OCTET STRING }
 *
 * @author Ahseya
 */
@Immutable
public final class Item extends ASN1Object {

    private final int version;
    private final byte[] data;

    public Item(int version, byte[] data) {
        this.version = version;
        this.data = Arrays.copyOf(data, data.length);
    }

    public Item(ASN1Primitive primitive) {
        DERIterator i = DER.asSequence(primitive);

        version = DER.as(ASN1Integer.class, i)
                .getValue()
                .intValue();

        data = DER.as(DEROctetString.class, i)
                .getOctets();
    }

    public int version() {
        return version;
    }

    public byte[] octets() {
        return Arrays.copyOf(data, data.length);
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector vector = DER.vector(
                new ASN1Integer(version),
                new DEROctetString(octets()));

        return new DERSequence(vector);
    }

    @Override
    public String toString() {
        return "Item{"
                + "version=" + version
                + ", data=" + Hex.toHexString(data)
                + '}';
    }
}
