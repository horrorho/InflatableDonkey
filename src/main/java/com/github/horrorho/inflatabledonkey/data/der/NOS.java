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

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;
import javax.annotation.concurrent.Immutable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.util.encoders.Hex;

/**
 * NOS.
 * <p>
 *
 * @author Ahseya
 */
@Immutable
public final class NOS extends ASN1Object {

    // Possibly SPKey
    /*
        Template:
    
        SEQUENCE  (3)
         INTEGER            int x
         INTEGER optional   int y
         OCTET_STRING       byte[] key 
     */
    private final int x;
    private final Optional<Integer> y;    // optional
    private final byte[] key;

    public NOS(int x, Optional<Integer> y, byte[] key) {
        this.x = x;
        this.y = y;
        this.key = Arrays.copyOf(key, key.length);
    }

    public NOS(ASN1Primitive primitive) {
        DERIterator i = DER.asSequence(primitive);

        x = DER.as(ASN1Integer.class, i)
                .getValue()
                .intValue();

        y = i.nextIf(ASN1Integer.class)
                .map(ASN1Integer::getValue)
                .map(BigInteger::intValue);

        key = DER.as(DEROctetString.class, i)
                .getOctets();
    }

    public int x() {
        return x;
    }

    public Optional<Integer> y() {
        return y;
    }

    public byte[] key() {
        return Arrays.copyOf(key, key.length);
    }

    @Override
    public ASN1Primitive toASN1Primitive() {

        ASN1EncodableVector vector = DER.vector(
                new ASN1Integer(x),
                y.map(ASN1Integer::new).orElse(null),
                new DEROctetString(key()));

        return new DERSequence(vector);
    }

    @Override
    public String toString() {
        return "MasterKey{"
                + "x=" + x
                + ", y=" + y
                + ", key=0x" + Hex.toHexString(key)
                + '}';
    }
}
