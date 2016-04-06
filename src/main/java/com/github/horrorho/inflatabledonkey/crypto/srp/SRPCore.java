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
package com.github.horrorho.inflatabledonkey.crypto.srp;

import java.math.BigInteger;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.bouncycastle.util.BigIntegers;

/**
 * SRPCore.
 *
 * @author Ahseya
 */
@Immutable
public final class SRPCore {
    // https://en.wikipedia.org/wiki/Secure_Remote_Password_protocol
    // based on org.bouncycastle.crypto.agreement.srp.SRP6Util

    private static final byte[] COLON = new byte[]{(byte) ':'};

    public static byte[] generateEphemeralKeyA(BigInteger N, BigInteger g, BigInteger a) {
        BigInteger A = g.modPow(a, N);
        int length = length(N);
        return padded(A, length);
    }

    public static BigInteger generatek(Digest digest, BigInteger N, BigInteger g) {
        int length = length(N);
        byte[] hash = hash(digest, padded(N, length), padded(g, length));
        return new BigInteger(1, hash);
    }

    public static byte[] generateKey(Digest digest, BigInteger N, BigInteger S) {
        int length = length(N);
        return hash(digest, padded(S, length));
    }

    public static byte[] generateM1(
            Digest digest,
            BigInteger N,
            BigInteger g,
            byte[] ephemeralKeyA,
            byte[] ephemeralKeyB,
            byte[] key,
            byte[] salt, // s
            byte[] identity) {

        // M1 = H(H(N) XOR H(g) | H(I) | s | A | B | K) 
        int length = length(N);

        // hI = H(I)
        byte[] hI = SRPCore.hash(digest, identity);

        // tmp = H(N) XOR H(g)
        byte[] hNxhG = ByteUtils.xor(SRPCore.hash(digest, padded(N, length)), SRPCore.hash(digest, padded(g, length)));

        return hash(digest, hNxhG, hI, salt, ephemeralKeyA, ephemeralKeyB, key);
    }

    public static byte[] generateM2(Digest digest, BigInteger N, byte[] A, byte[] M1, byte[] K) {
        return hash(digest, A, M1, K);
    }

    public static BigInteger generateS(
            Digest digest,
            BigInteger N,
            BigInteger g,
            BigInteger a,
            BigInteger k,
            BigInteger u,
            BigInteger x,
            BigInteger B) {

        // S = (B - k*(g^x)) ^ (a + ux)
        BigInteger exp = u.multiply(x).add(a);
        BigInteger tmp = g.modPow(x, N).multiply(k).mod(N);
        return B.subtract(tmp).mod(N).modPow(exp, N);
    }

    public static BigInteger generateu(Digest digest, byte[] A, byte[] B) {
        return new BigInteger(1, hash(digest, A, B));
    }

    public static BigInteger generatex(Digest digest, BigInteger N, byte[] salt, byte[] identity, byte[] password) {
        // x = SHA1(s | SHA1(I | ":" | P))
        byte[] tmp = hash(digest, identity, COLON, password);
        byte[] hash = hash(digest, salt, tmp);

        return new BigInteger(1, hash);
    }

    public static boolean isZero(BigInteger N, BigInteger i) {
        return i.mod(N).equals(BigInteger.ZERO);
    }

    static byte[] hash(Digest digest, byte[] bytes) {
        return hash(digest, new byte[][]{bytes});
    }

    static byte[] hash(Digest digest, byte[]... bytes) {
        for (byte[] b : bytes) {
            digest.update(b, 0, b.length);
        }

        byte[] output = new byte[digest.getDigestSize()];
        digest.doFinal(output, 0);
        return output;
    }

    public static int length(BigInteger i) {
        return (i.bitLength() + 7) / 8;
    }

    public static byte[] padded(BigInteger n, int length) {
        // org.bouncycastle.crypto.agreement.srp.SRP6Util#getPadded() with overflow check
        byte[] byteArray = BigIntegers.asUnsignedByteArray(n);

        if (byteArray.length > length) {
            throw new IllegalArgumentException("BigInteger overflows specified length");
        }

        if (byteArray.length < length) {
            byte[] tmp = new byte[length];
            System.arraycopy(byteArray, 0, tmp, length - byteArray.length, byteArray.length);
            byteArray = tmp;
        }
        return byteArray;
    }
}
