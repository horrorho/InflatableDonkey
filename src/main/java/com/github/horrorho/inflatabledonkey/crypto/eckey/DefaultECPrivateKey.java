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
package com.github.horrorho.inflatabledonkey.crypto.eckey;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.asn1.x9.X9ECParameters;

/**
 * DefaultECPublicKey.
 *
 * @author cain
 */
@Immutable
public final class DefaultECPrivateKey implements ECPrivate {

    private final DefaultECPublicKey publicKey;
    private final BigInteger d;

    DefaultECPrivateKey(DefaultECPublicKey publicKey, BigInteger d) {
        this.publicKey = Objects.requireNonNull(publicKey, "publicKey");
        this.d = d;
    }

    public DefaultECPrivateKey(BigInteger d, String curveName, X9ECParameters x9ECParameters) {
        this(new DefaultECPublicKey(ECAssistant.getQd(d, x9ECParameters), curveName, x9ECParameters), d);
    }

    public DefaultECPrivateKey(
            Optional<BigInteger> x,
            Optional<BigInteger> y,
            BigInteger d,
            String curveName,
            X9ECParameters x9ECParameters) {

        this(new DefaultECPublicKey(ECAssistant.getQdQxy(x, y, d, x9ECParameters), curveName, x9ECParameters), d);
    }

    @Override
    public BigInteger d() {
        return d;
    }

    @Override
    public ECPublic publicKey() {
        return publicKey;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.publicKey);
        hash = 59 * hash + Objects.hashCode(this.d);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DefaultECPrivateKey other = (DefaultECPrivateKey) obj;
        if (!Objects.equals(this.publicKey, other.publicKey)) {
            return false;
        }
        return Objects.equals(this.d, other.d);
    }

    @Override
    public String toString() {
        return "DefaultECPrivateKey{"
                + "publicKey=" + publicKey
                + ", d=" + d.toString(16)
                + '}';
    }
}
