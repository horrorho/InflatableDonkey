/*
 * The MIT License
 *
 * Copyright 2017 Ayesha.
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
package com.github.horrorho.inflatabledonkey.cache;

import java.util.Objects;
import java.util.function.Supplier;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;

/**
 *
 * @author Ayesha
 */
@Immutable
final class StreamCryptorPBKDF2 implements StreamCryptor.KDF {

    private final Supplier<Digest> digests;
    private final int iterations;
    private final int keyLength;

    StreamCryptorPBKDF2(Supplier<Digest> digests, int iterations, int keyLength) {
        this.digests = Objects.requireNonNull(digests);
        this.iterations = iterations;
        this.keyLength = keyLength;
    }

    @Override
    public byte[] apply(byte[] password, byte[] salt) {
        PKCS5S2ParametersGenerator generator = new PKCS5S2ParametersGenerator(digests.get());
        generator.init(password, salt, iterations);
        return ((KeyParameter) generator.generateDerivedParameters(keyLength * 8)).getKey();
    }

    @Override
    public String toString() {
        return "StreamCryptorPBKDF2{"
                + "digests=" + digests
                + ", iterations=" + iterations
                + ", keyLength=" + keyLength
                + '}';
    }
}
