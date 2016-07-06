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
package com.github.horrorho.inflatabledonkey.crypto.ec.key;

import com.github.horrorho.inflatabledonkey.crypto.ec.ECCurvePoint;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.BigIntegers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DefaultECPublicKey.
 *
 * @author cain
 */
@Immutable
public final class ECPrivateKey implements ECKey {

    private static final Logger logger = LoggerFactory.getLogger(ECPrivateKey.class);

    public static Optional<ECPrivateKey>
            create(Optional<BigInteger> x, Optional<BigInteger> y, BigInteger d, String curveName) {

        return ECCurvePoint.create(d, curveName)
                .filter(Q -> correlate(x, y, Q))
                .map(ECPublicKey::create)
                .map(pub -> new ECPrivateKey(pub, d));
    }

    static boolean correlate(Optional<BigInteger> x, Optional<BigInteger> y, ECCurvePoint Q) {
        boolean correlates = (x.map(c -> c.equals(Q.x())).orElse(true)
                && y.map(c -> c.equals(Q.y())).orElse(true));

        if (!correlates) {
            logger.warn("-- correlate() - bad point, x: 0x{} y: 0x{} Q: {}",
                    x.map(b -> b.toString(16)), y.map(b -> b.toString(16)), Q);
        }

        return correlates;
    }

    private final ECPublicKey publicKey;
    private final BigInteger d;

    private ECPrivateKey(ECPublicKey publicKey, BigInteger d) {
        this.publicKey = Objects.requireNonNull(publicKey, "publicKey");
        this.d = d;
    }

    public byte[] agreement(ECPublicKey publicKey) {
        return publicKey.point().agreement(d);
    }
    
    public BigInteger d() {
        return d;
    }

    public byte[] dEncoded() {
        return BigIntegers.asUnsignedByteArray(publicKey.point().fieldLength(), d);
    }

    public ECPublicKey publicKey() {
        return publicKey;
    }

    @Override
    public ECCurvePoint point() {
        return publicKey.point();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.publicKey);
        hash = 89 * hash + Objects.hashCode(this.d);
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
        final ECPrivateKey other = (ECPrivateKey) obj;
        if (!Objects.equals(this.publicKey, other.publicKey)) {
            return false;
        }
        if (!Objects.equals(this.d, other.d)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ECPrivateKey{" + "publicKey=" + publicKey + ", d=" + d + '}';
    }
}
