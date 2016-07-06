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
package com.github.horrorho.inflatabledonkey.crypto.ec.key.imports;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * KeyImport. Elliptic curve key import.
 * <p>
 * Implementations must be immutable.
 *
 * @author Ahseya
 * @param <T> key type
 */
public interface ECKeyImport<T> {

    Optional<T> importKey(String curveName, byte[] data);

    /**
     * Returns the elliptic curve field length corresponding to the supplied export data length, otherwise -1.
     *
     * @param dataLength
     * @return the elliptic curve field length corresponding to the supplied export data length, otherwise -1.
     */
    int fieldLength(int dataLength);

    default Function<byte[], Optional<T>>
            importKey(IntFunction<Optional<String>> fieldLengthToCurveName) {

        return exportedData -> fieldLengthToCurveName.apply(fieldLength(exportedData.length))
                .flatMap(curveName -> importKey(curveName, exportedData));
    }
}
