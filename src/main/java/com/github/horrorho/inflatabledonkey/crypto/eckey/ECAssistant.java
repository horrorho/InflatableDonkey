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
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.bouncycastle.asn1.x9.ECNamedCurveTable;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.bouncycastle.util.BigIntegers;

/**
 * Elliptic curve utility methods.
 *
 * @author Ahseya
 */
@Immutable
public final class ECAssistant {

    static ECPoint getQ(BigInteger x, BigInteger y, ECCurve curve) {
        return curve.createPoint(x, y);
    }

    static Optional<ECPoint> getQ(Optional<BigInteger> x, Optional<BigInteger> y, ECCurve curve) {
        return x.isPresent() && y.isPresent()
                ? Optional.of(curve.createPoint(x.get(), y.get()))
                : Optional.empty();
    }

    static ECPoint getQd(BigInteger d, X9ECParameters x9ECParameters) {
        return new FixedPointCombMultiplier().multiply(x9ECParameters.getG(), d).normalize();
    }

    static ECPoint getQdQxy(
            Optional<BigInteger> x,
            Optional<BigInteger> y,
            BigInteger d,
            X9ECParameters x9ECParameters) {

        ECPoint dG = getQd(d, x9ECParameters);

        boolean correlate = getQ(x, y, x9ECParameters.getCurve())
                .map(Qxy -> Qxy.equals(dG))
                .orElse(true);

        if (!correlate) {
            throw new IllegalArgumentException("Q doesn't correlated to d");
        }

        return dG;
    }

    public static IntFunction<Optional<String>>
            fieldLengthToCurveName(Collection<String> curveNames) {

        Map<Integer, String> dataLengthToCurveName = curveNames.stream()
                .collect(Collectors.toMap(ECAssistant::fieldLength, Function.identity()));

        return dataLength -> Optional.ofNullable(dataLengthToCurveName.get(dataLength));
    }

    public static int fieldLength(String curveName) {
        return fieldLength(ECAssistant.x9ECParameters(curveName));
    }

    public static int fieldLength(X9ECParameters x9ECParameters) {
        return fieldLength(x9ECParameters.getCurve());
    }

    public static int fieldLength(ECCurve curve) {
        return fieldLength(curve.getFieldSize());
    }

    public static int fieldLength(int fieldBitLength) {
        return (fieldBitLength + 7) / 8;
    }

    public static byte[] encodedField(int length, BigInteger i) {
        return BigIntegers.asUnsignedByteArray(length, i);
    }

    public static X9ECParameters x9ECParameters(String curveName) {
        X9ECParameters x9ECParameters = ECNamedCurveTable.getByName(curveName);
        if (x9ECParameters == null) {
            throw new IllegalArgumentException("ec curve not found: " + curveName);
        }
        return x9ECParameters;
    }

    public static ECDomainParameters ecDomainParametersFrom(X9ECParameters x9ECParameters) {
        return new ECDomainParameters(
                x9ECParameters.getCurve(),
                x9ECParameters.getG(),
                x9ECParameters.getN(),
                x9ECParameters.getH(),
                x9ECParameters.getSeed());
    }
}
