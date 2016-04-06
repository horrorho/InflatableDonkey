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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.jcip.annotations.NotThreadSafe;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERTaggedObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DERIterator.
 *
 * @author Ahseya
 */
@NotThreadSafe
class DERIterator implements ASN1Encodable {

    private static final Logger logger = LoggerFactory.getLogger(DERIterator.class);

    private final Map<Integer, ASN1Primitive> derTaggedObjects = new HashMap<>();
    private final List<ASN1Primitive> list = new ArrayList<>();
    private Iterator<ASN1Primitive> iterator;
    private ASN1Primitive peek;

    DERIterator(Enumeration enumeration) {
        while (enumeration.hasMoreElements()) {
            ASN1Primitive primitive = (ASN1Primitive) enumeration.nextElement();

            if (primitive instanceof DERTaggedObject) {
                DERTaggedObject tagged = (DERTaggedObject) primitive;
                derTaggedObjects.put(tagged.getTagNo(), tagged.getObject());

            } else {
                list.add(primitive);
            }
        }

        iterator = list.iterator();
    }

    boolean hasNext() {
        return peek != null || iterator.hasNext();
    }

    ASN1Primitive next() {
        if (peek != null) {
            ASN1Primitive ref = peek;
            peek = null;
            return ref;
        }

        if (!iterator.hasNext()) {
            logger.error("-- next() - next call without next element");
        }

        return iterator.hasNext()
                ? iterator.next()
                : null;
    }

    Optional<ASN1Primitive> optional() {
        if (peek != null) {
            ASN1Primitive ref = peek;
            peek = null;
            return Optional.of(ref);
        }

        return iterator.hasNext()
                ? Optional.of(iterator.next())
                : Optional.empty();
    }

    ASN1Primitive peek() {
        if (peek == null) {
            peek = next();
        }

        return peek;
    }

    <T extends ASN1Primitive> Optional<T> nextIf(Class<T> is) {
        if (hasNext() == false) {
            return Optional.empty();
        }

        ASN1Primitive primitive = DER.asPrimitive(peek());
        if (is.isAssignableFrom(primitive.getClass())) {
            next();
            return Optional.ofNullable(is.cast(primitive));

        } else {
            return Optional.empty();
        }
    }

    Map<Integer, ASN1Primitive> derTaggedObjects() {
        return new HashMap<>(derTaggedObjects);
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return next();
    }
}
