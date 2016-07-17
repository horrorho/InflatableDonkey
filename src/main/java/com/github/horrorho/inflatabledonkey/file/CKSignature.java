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
package com.github.horrorho.inflatabledonkey.file;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.Digest;

/**
 * MMCSSignatureGenerator/ CKFileSignatureGenerator.
 *
 * @author Ahseya
 */
@Immutable
public enum CKSignature {
    ONE(CKDigestOne::new);

    public static Optional<CKSignature> type(byte[] signature) {
        if (signature.length == 0) {
            return Optional.empty();
        }

        switch (signature[0] & 0x7F) {
            case 0x01:
            case 0x02:
            case 0x0B:
                return signature.length == 21
                        ? Optional.of(ONE)
                        : Optional.empty();

            default:
                return Optional.empty();
        }
    }

    private final Supplier<Digest> supplier;

    private CKSignature(Supplier<Digest> supplier) {
        this.supplier = Objects.requireNonNull(supplier, "supplier");
    }

    public Digest newDigest() {
        return supplier.get();
    }
}
