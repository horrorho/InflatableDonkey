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
import net.jcip.annotations.Immutable;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.math.ec.ECPoint;

@Immutable
public final class DefaultECPublicKey implements ECPublic {

    private final ECPoint Q;    // TODO appears to be thread safe in the context of our operations, to test.
    private final String curveName;
    private final X9ECParameters x9ECParameters;

    DefaultECPublicKey(ECPoint Q, String curveName, X9ECParameters x9ECParameters) {
        this.Q = Objects.requireNonNull(Q, "Q");
        this.curveName = Objects.requireNonNull(curveName, "curveName");
        this.x9ECParameters = Objects.requireNonNull(x9ECParameters, "x9ECParameters");
    }

    public DefaultECPublicKey(BigInteger x, BigInteger y, String curveName, X9ECParameters x9ECParameters) {
        this(
                ECAssistant.getQ(x, y, x9ECParameters.getCurve()),
                curveName,
                x9ECParameters);
    }

    @Override
    public BigInteger x() {
        return Q.getXCoord().toBigInteger();
    }

    @Override
    public BigInteger y() {
        return Q.getYCoord().toBigInteger();
    }

    @Override
    public boolean verifySignature(byte[] message, BigInteger r, BigInteger s) {
        ECDomainParameters ecDomainParameters = ECAssistant.ecDomainParametersFrom(x9ECParameters);
        ECPublicKeyParameters ecPublicKeyParameters = new ECPublicKeyParameters(Q, ecDomainParameters);

        ECDSASigner signer = new ECDSASigner();
        signer.init(false, ecPublicKeyParameters);

        return signer.verifySignature(message, r, s);
    }

    @Override
    public String curveName() {
        return curveName;
    }

    @Override
    public int fieldBitLength() {
        return x9ECParameters.getCurve().getFieldSize();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.Q);
        hash = 53 * hash + Objects.hashCode(this.curveName);
        hash = 53 * hash + Objects.hashCode(this.x9ECParameters);
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
        final DefaultECPublicKey other = (DefaultECPublicKey) obj;
        if (!Objects.equals(this.curveName, other.curveName)) {
            return false;
        }
        if (!Objects.equals(this.Q, other.Q)) {
            return false;
        }
        return Objects.equals(this.x9ECParameters, other.x9ECParameters);
    }

    @Override
    public String toString() {
        return "DefaultECPublicKey{"
                + "Q=" + Q
                + ", curveName=" + curveName
                + ", x9ECParameters=" + x9ECParameters
                + '}';
    }
}
