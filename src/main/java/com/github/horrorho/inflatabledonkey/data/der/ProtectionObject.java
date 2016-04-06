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

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import net.jcip.annotations.Immutable;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class ProtectionObject extends ASN1Object {

    /*
        Template:

        SEQUENCE  (3)
         cont [0] OPTIONAL
          SET  (1)          xSet
           OCTET_STRING     x
         cont [1] OPTIONAL
          NOS masterKey   ?? check
         cont [2] OPTIONAL
          SET  (1)
           NOS masterKey  ?? check
     */
    private static final int CONT0 = 0;
    private static final int CONT1 = 1;
    private static final int CONT2 = 2;

    private final Optional<Set<byte[]>> xSet;
    private final Optional<NOS> masterKey;
    private final Optional<Set<NOS>> masterKeySet;

    public ProtectionObject(
            Optional<Set<byte[]>> xSet,
            Optional<NOS> masterKey,
            Optional<Set<NOS>> masterKeySet) {

        this.xSet = Objects.requireNonNull(xSet, "xSet")
                .filter(set -> !set.isEmpty());
        this.masterKey = Objects.requireNonNull(masterKey, "masterKey");
        this.masterKeySet = Objects.requireNonNull(masterKeySet, "masterKeySet")
                .filter(set -> !set.isEmpty());
    }

    public ProtectionObject(ASN1Primitive primitive) {
        DERIterator i = DER.asSequence(primitive);

        Map<Integer, ASN1Primitive> tagged = i.derTaggedObjects();

        Function<ASN1Encodable, byte[]> toOctets
                = DER.as(DEROctetString.class).andThen(ASN1OctetString::getOctets);

        xSet = Optional.ofNullable(tagged.get(CONT0))
                .map(DER.asSet(toOctets));

        masterKey = Optional.ofNullable(tagged.get(CONT1))
                .map(NOS::new);

        masterKeySet = Optional.ofNullable(tagged.get(CONT2))
                .map(DER.asSet(NOS::new));

    }

    public Optional<Set<byte[]>> getxSet() {
        throw new UnsupportedOperationException("TODO");
    }

    public Optional<NOS> getMasterKey() {
        return masterKey;
    }

    public Optional<Set<NOS>> getMasterKeySet() {
        return masterKeySet.map(HashSet::new);
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public String toString() {
        return "ProtectionObject{" + "xSet=" + xSet + ", masterKey=" + masterKey + ", masterKeySet=" + masterKeySet + '}';
    }
}
