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
import javax.annotation.concurrent.Immutable;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.BigIntegers;

/**
 * Elliptic curve compact point decompression.
 *
 * @see
 * <a href="https://www.ietf.org/archive/id/draft-jivsov-ecc-compact-05.txt">https://www.ietf.org/archive/id/draft-jivsov-ecc-compact-05.txt</a>
 * @author Ahseya
 */
@Immutable
public final class ECPointsCompact {

    public static BigInteger y(ECCurve curve, BigInteger x) {
        // Andrey Jivsov https://www.ietf.org/archive/id/draft-jivsov-ecc-compact-05.txt.
        ECFieldElement X = curve.fromBigInteger(x);
        ECFieldElement rhs = X.square().add(curve.getA()).multiply(X).add(curve.getB());

        // y' = sqrt( C(x) ), where y'>0
        ECFieldElement yTilde = rhs.sqrt();

        if (yTilde == null) {
            throw new IllegalArgumentException("invalid point compression");
        }

        // y = min(y',p-y')
        BigInteger yT = yTilde.toBigInteger();
        BigInteger yTn = yTilde.negate().toBigInteger();
        BigInteger y = yT.compareTo(yTn) == -1 ? yT : yTn;

        return y;
    }

    @Deprecated
    public static ECPoint decodeFPPoint(ECCurve curve, byte[] data) {
        // Patched org.bouncycastle.math.ec.ECCurve#decodePoint code.
        int expectedLength = (curve.getFieldSize() + 7) / 8;
        if (expectedLength != data.length) {
            throw new IllegalArgumentException("incorrect data length for compact encoding");
        }

        BigInteger X = BigIntegers.fromUnsignedByteArray(data, 0, expectedLength);
        ECPoint p = decompressFPPoint(curve, X);

        if (!satisfiesCofactor(curve, p)) {
            throw new IllegalArgumentException("invalid point");
        }

        return p;
    }

    @Deprecated
    public static ECPoint decompressFPPoint(ECCurve curve, BigInteger X) {
        // See Andrey Jivsov https://www.ietf.org/archive/id/draft-jivsov-ecc-compact-05.txt.
        ECFieldElement x = curve.fromBigInteger(X);
        ECFieldElement rhs = x.square().add(curve.getA()).multiply(x).add(curve.getB());

        // y' = sqrt( C(x) ), where y'>0
        ECFieldElement yTilde = rhs.sqrt();

        if (yTilde == null) {
            throw new IllegalArgumentException("invalid point compression");
        }

        // y = min(y',p-y')
        BigInteger yT = yTilde.toBigInteger();
        BigInteger yTn = yTilde.negate().toBigInteger();
        BigInteger y = yT.compareTo(yTn) == -1 ? yT : yTn;

        // Q=(x,y) is the canonical representation of the point
        ECPoint Q = curve.createPoint(X, y);

        return Q;
    }

    @Deprecated
    public static boolean satisfiesCofactor(ECCurve curve, ECPoint point) {
        // Patched org.bouncycastle.math.ec.ECPoint#satisfiesCofactor protected code.
        BigInteger h = curve.getCofactor();
        return h == null || h.equals(ECConstants.ONE) || !ECAlgorithms.referenceMultiply(point, h).isInfinity();
    }
}
