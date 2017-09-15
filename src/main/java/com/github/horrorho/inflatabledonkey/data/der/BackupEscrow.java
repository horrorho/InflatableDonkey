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
import javax.annotation.concurrent.Immutable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.util.encoders.Hex;

/**
 * BackupEscrow.
 *
 * @author Ahseya
 */
@Immutable
public final class BackupEscrow extends ASN1Object {

    // TODO locate template.
    /*
        NO Template, possibly incomplete/ optional/ missing fields:
    
        appl [4] 
         SEQUENCE
          OCTET STRING  byte[] wrappedKey
          OCTET STRING  byte[] data
          OCTET STRING  byte[] x
          INTEGER       int    y
          OCTET STRING  byte[] masterKeyPublic
     */
    public static final int APPLICATION_TAG = 4;

    private final byte[] wrappedKey;
    private final byte[] data;
    private final byte[] x;
    private final int y;
    private final byte[] masterKeyPublic;

    public BackupEscrow(byte[] wrappedKey, byte[] data, byte[] x, int y, byte[] masterKeyPublic) {
        this.wrappedKey = Arrays.copyOf(wrappedKey, wrappedKey.length);
        this.data = Arrays.copyOf(data, data.length);
        this.x = Arrays.copyOf(x, x.length);
        this.y = y;
        this.masterKeyPublic = Arrays.copyOf(masterKeyPublic, masterKeyPublic.length);
    }

    public BackupEscrow(ASN1Primitive primitive) {
        ASN1Primitive app = DER.asApplicationSpecific(APPLICATION_TAG, primitive);
        DERIterator i = DER.asSequence(app);

        wrappedKey = DER.as(DEROctetString.class, i)
                .getOctets();

        data = DER.as(DEROctetString.class, i)
                .getOctets();

        x = DER.as(DEROctetString.class, i)
                .getOctets();

        y = DER.as(ASN1Integer.class, i)
                .getValue()
                .intValue();

        masterKeyPublic = DER.as(DEROctetString.class, i)
                .getOctets();
    }

    public byte[] wrappedKey() {
        return Arrays.copyOf(wrappedKey, wrappedKey.length);
    }

    public byte[] data() {
        return Arrays.copyOf(data, data.length);
    }

    public byte[] x() {
        return Arrays.copyOf(x, x.length);
    }

    public int y() {
        return y;
    }

    public byte[] masterKeyPublic() {
        return Arrays.copyOf(masterKeyPublic, masterKeyPublic.length);
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector vector = DER.vector(
                new DEROctetString(wrappedKey()),
                new DEROctetString(data()),
                new DEROctetString(x()),
                new ASN1Integer(y),
                new DEROctetString(masterKeyPublic()));

        DERSequence sequence = new DERSequence(vector);
        return DER.toApplicationSpecific(APPLICATION_TAG, sequence);
    }

    @Override
    public String toString() {
        return "BackupEscrow{"
                + "wrappedKey=" + Hex.toHexString(wrappedKey)
                + ", data=" + Hex.toHexString(data)
                + ", x=" + Hex.toHexString(x)
                + ", y=" + y
                + ", masterKeyPublic=" + Hex.toHexString(masterKeyPublic)
                + '}';
    }
}
