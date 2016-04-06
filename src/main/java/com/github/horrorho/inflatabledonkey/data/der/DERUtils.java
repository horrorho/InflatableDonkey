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

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DERUtils.
 *
 * @author Ahseya
 */
public final class DERUtils {

    private static final Logger logger = LoggerFactory.getLogger(DERUtils.class);

    public static <T> Optional<T> parse(byte[] data, Function<ASN1Primitive, T> function) {
        try (ASN1InputStream asN1InputStream = new ASN1InputStream(data)) {
            ASN1Primitive primitive = asN1InputStream.readObject();

            return parse(primitive, function);

        } catch (IOException ex) {
            logger.warn("-- parse() - IOException: {}", ex);
            return Optional.empty();
        }
    }

    public static <T> Optional<T> parse(ASN1Primitive primitive, Function<ASN1Primitive, T> function) {
        try {
            T t = function.apply(primitive);
            logger.debug("-- parse() - t: {}", t);

            return Optional.ofNullable(t);

        } catch (RuntimeException ex) {
            logger.warn("-- parse() - failed decode: {}", ex);
            return Optional.empty();
        }
    }
}
