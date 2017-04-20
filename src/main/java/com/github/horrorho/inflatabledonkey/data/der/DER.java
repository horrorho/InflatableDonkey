/* 
 * The MIT License
 *
 * Copyright 2015 Ahseya.
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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERApplicationSpecific;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERSet;

/**
 * DER. Utility class with casting/ conversion methods.
 *
 * @author Ahseya
 */
@Immutable
final class DER {

    static ASN1Primitive asPrimitive(ASN1Encodable encodable) {
        return encodable instanceof ASN1Primitive
                ? (ASN1Primitive) encodable
                : encodable.toASN1Primitive();
    }

    static <T extends ASN1Primitive> Optional<T> asOptional(Class<T> to, ASN1Encodable encodable) {
        if (encodable == null) {
            return Optional.empty();
        }

        ASN1Primitive primitive = asPrimitive(encodable);
        if (to.isAssignableFrom(primitive.getClass())) {
            return Optional.ofNullable(to.cast(primitive));

        } else {
            return Optional.empty();
        }
    }

    static <T extends ASN1Primitive> T as(Class<T> to, ASN1Encodable encodable) {
        return DER.asOptional(to, encodable).orElseThrow(()
                -> new IllegalArgumentException(
                        "bad class, expected " + to + " got " + encodable.getClass()));
    }

    static <T extends ASN1Primitive> Function<ASN1Encodable, T> as(Class<T> to) {
        return encodable -> as(to, encodable);
    }

    static DERIterator asSequence(ASN1Encodable encodable) {
        return new DERIterator(as(ASN1Sequence.class, asPrimitive(encodable)).getObjects());
    }

    static Set<ASN1Primitive> asPrimitiveSet(ASN1Encodable encodable) {
        Enumeration enumeration = as(ASN1Set.class, encodable).getObjects();
        Set<ASN1Primitive> set = new HashSet<>();

        while (enumeration.hasMoreElements()) {
            ASN1Encodable element = (ASN1Encodable) enumeration.nextElement();
            set.add(asPrimitive(element));
        }

        return set;
    }

    static <T> List<T> asSet(ASN1Encodable encodable, Function<? super ASN1Primitive, T> function) {
        return asPrimitiveSet(encodable)
                .stream()
                .map(function)
                .collect(Collectors.toList());
    }

    static <T> Function<ASN1Encodable, List<T>> asSet(Function<? super ASN1Primitive, T> function) {
        return encodable -> asSet(encodable, function);
    }

    static ASN1Primitive asApplicationSpecific(int tag, ASN1Encodable encodable) {
        try {
            DERApplicationSpecific specific = as(DERApplicationSpecific.class, encodable);

            if (specific.getApplicationTag() == tag) {
                return specific.getObject();

            } else {
                throw new IllegalArgumentException(
                        "tag mismatch, expected " + tag + " got " + specific.getApplicationTag());
            }

        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    static ASN1EncodableVector vector(Collection<? extends ASN1Encodable> collection) {
        return collection.stream()
                .filter(Objects::nonNull)
                .collect(ASN1EncodableVector::new, ASN1EncodableVector::add, ASN1EncodableVector::addAll);
    }

    static ASN1EncodableVector vector(ASN1Encodable... encodables) {
        return vector(Arrays.asList(encodables));
    }

    static DERApplicationSpecific toApplicationSpecific(int tag, ASN1Encodable encodable) {
        try {
            return new DERApplicationSpecific(tag, encodable);

        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    static DERSet toSet(Collection<? extends ASN1Encodable> collection) {
        return new DERSet(vector(collection));
    }

    static DERSequence toSequence(List<? extends ASN1Encodable> collection) {
        return new DERSequence(vector(collection));
    }
}
