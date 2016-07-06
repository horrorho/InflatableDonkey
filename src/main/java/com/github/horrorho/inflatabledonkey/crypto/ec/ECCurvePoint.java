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
package com.github.horrorho.inflatabledonkey.crypto.ec;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.asn1.x9.ECNamedCurveTable;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ECCurvePoint.
 *
 * @author Ahseya
 */
@Immutable  // Effectively immutable.
public final class ECCurvePoint {

    private static final Logger logger = LoggerFactory.getLogger(ECCurvePoint.class);

    public static Optional<ECCurvePoint> create(BigInteger x, BigInteger y, String curveName) {
        X9ECParameters x9ECParameters = ECAssistant.x9ECParameters(curveName);
        ECPoint Q = x9ECParameters.getCurve()
                .createPoint(x, y);

        if (!Q.isValid()) {
            logger.warn("-- create() - bad Q: {} curve: {}", Q, curveName);
            return Optional.empty();
        }

        ECCurvePoint point = new ECCurvePoint(Q, curveName, x9ECParameters);
        return Optional.of(point);
    }

    public static Optional<ECCurvePoint> create(BigInteger d, String curveName) {
        X9ECParameters x9ECParameters = ECAssistant.x9ECParameters(curveName);
        ECPoint Q = new FixedPointCombMultiplier().multiply(x9ECParameters.getG(), d).normalize();

        ECCurvePoint point = new ECCurvePoint(Q, curveName, x9ECParameters);
        return Optional.of(point);
    }

    private final Object lock;
    private final ECPoint Q;
    private final String curveName;
    private final X9ECParameters x9ECParameters;

    private ECCurvePoint(Object lock, ECPoint Q, String curveName, X9ECParameters x9ECParameters) {
        this.lock = Objects.requireNonNull(lock, "lock");
        this.Q = Objects.requireNonNull(Q.normalize(), "Q");
        this.curveName = Objects.requireNonNull(curveName, "curveName");
        this.x9ECParameters = Objects.requireNonNull(x9ECParameters, "x9ECParameters");
    }

    private ECCurvePoint(ECPoint Q, String curveName, X9ECParameters x9ECParameters) {
        this(new Object(), Q, curveName, x9ECParameters);
    }

    public byte[] agreement(BigInteger d) {
        // TODO thread safety of ECPoint unclear.
        synchronized (lock) {
            ECPoint P = Q.multiply(d).normalize();
            if (P.isInfinity()) {
                throw new IllegalStateException("invalid EDCH: infinity");
            }

            return P.getAffineXCoord().getEncoded();
        }
    }

    public ECPoint copyQ() {
        return x9ECParameters()
                .getCurve()
                .createPoint(x(), y());
    }

    public BigInteger x() {
        return Q.getXCoord().toBigInteger();
    }

    public BigInteger y() {
        return Q.getYCoord().toBigInteger();
    }

    public byte[] xEncoded() {
        return Q.getXCoord().getEncoded();
    }

    public byte[] yEncoded() {
        return Q.getYCoord().getEncoded();
    }

    public String curveName() {
        return curveName;
    }

    public X9ECParameters x9ECParameters() {
        return ECAssistant.x9ECParameters(curveName);
    }

    public int fieldBitLength() {
        return x9ECParameters.getCurve().getFieldSize();
    }

    public int fieldLength() {
        return ECAssistant.fieldLength(fieldBitLength());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.Q);
        hash = 79 * hash + Objects.hashCode(this.curveName);
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
        final ECCurvePoint other = (ECCurvePoint) obj;
        if (!Objects.equals(this.curveName, other.curveName)) {
            return false;
        }
        if (!Objects.equals(this.Q, other.Q)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ECCurvePoint{"
                + "lock=" + lock
                + ", Q=" + Q
                + ", curveName=" + curveName
                + ", x9ECParameters=" + x9ECParameters
                + '}';
    }
}
