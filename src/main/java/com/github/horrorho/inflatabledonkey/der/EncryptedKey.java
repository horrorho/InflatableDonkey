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

import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;

/**
 * Encrypted key.
 *
 * @author Ahseya
 */
@Immutable
public final class EncryptedKey extends ASN1Object {

    /*
        Template:
    
        SEQUENCE  (3)
         SEQUENCE  (3)      NOS masterKey
         OCTET_STRING       byte[] wrappedKey
         INTEGER optional   int flags
     */
    private final NOS masterKey;    // TODO rename, is public point of master key
    private final byte[] wrappedKey;
    private final Optional<Integer> flags;

    public EncryptedKey(NOS masterKey, byte[] wrappedKey, Optional<Integer> flags) {
        this.masterKey = Objects.requireNonNull(masterKey, "masterKey");
        this.wrappedKey = Arrays.copyOf(wrappedKey, wrappedKey.length);
        this.flags = Objects.requireNonNull(flags, "flags");
    }

    public EncryptedKey(ASN1Primitive primitive) {
        DERIterator i = DER.asSequence(primitive);

        masterKey = new NOS(i.next());

        wrappedKey = DER.as(DEROctetString.class, i)
                .getOctets();

        flags = i.optional()
                .map(DER.as(ASN1Integer.class))
                .map(ASN1Integer::getValue)
                .map(BigInteger::intValue);
    }

    public Optional<Integer> flags() {
        return flags;
    }

    public NOS masterKey() {
        return masterKey;
    }

    public byte[] wrappedKey() {
        return Arrays.copyOf(wrappedKey, wrappedKey.length);
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1Integer asn1IntegerFlags = flags.map(ASN1Integer::new)
                .orElse(null);

        ASN1EncodableVector vector = DER.vector(
                masterKey,
                new DEROctetString(wrappedKey()),
                asn1IntegerFlags);

        return new DERSequence(vector);
    }

    @Override
    public String toString() {
        return "EncryptedKey{"
                + "masterKey=" + masterKey
                + ", wrappedKey=0x" + Hex.toHexString(wrappedKey)
                + ", flags=" + flags
                + '}';
    }
}
