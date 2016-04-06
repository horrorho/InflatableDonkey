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
import org.bouncycastle.crypto.agreement.srp.SRP6Util;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.bouncycastle.util.BigIntegers;

/**
 * SRPCore.
 *
 * @author Ahseya
 */
@Immutable
public final class SRPCore {

    public static BigInteger calculateA(BigInteger N, BigInteger g, BigInteger a) {
        return g.modPow(a, N);
    }

    public static BigInteger calculatek(Digest digest, BigInteger N, BigInteger g) {
        return SRP6Util.calculateK(digest, N, g);
    }

    public static BigInteger calculateK(Digest digest, BigInteger N, BigInteger S) {
        return SRP6Util.calculateKey(digest, N, S);
    }

    public static BigInteger calculateM1(
            Digest digest,
            BigInteger N,
            BigInteger g,
            BigInteger A,
            BigInteger B,
            BigInteger K,
            byte[] salt, // s
            byte[] identity) {

        // M1 = H(H(N) XOR H(g) | H(I) | s | A | B | K) 
        int length = primeLength(N);

        // hI = H(I)
        byte[] hI = hash(digest, identity);

        // tmp = H(N) XOR H(g)
        byte[] hNxhG = ByteUtils.xor(hash(digest, padded(N, length)), hash(digest, padded(g, length)));

        update(digest, hNxhG);
        update(digest, hI);
        update(digest, salt);
        update(digest, padded(A, length));
        update(digest, padded(B, length));
        update(digest, padded(K, digest.getDigestSize()));

        byte[] output = new byte[digest.getDigestSize()];
        digest.doFinal(output, 0);

        return new BigInteger(1, output);
    }

    public static BigInteger calculateM2(Digest digest, BigInteger N, BigInteger A, BigInteger M1, BigInteger K) {
        int length = primeLength(N);

        update(digest, padded(A, length));
        update(digest, padded(M1, digest.getDigestSize()));
        update(digest, padded(K, digest.getDigestSize()));

        byte[] output = new byte[digest.getDigestSize()];
        digest.doFinal(output, 0);

        return new BigInteger(1, output);
    }

    public static BigInteger calculateS(
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

    public static BigInteger calculateu(Digest digest, BigInteger N, BigInteger A, BigInteger B) {
        return SRP6Util.calculateU(digest, N, A, B);
    }

    public static BigInteger calculatex(Digest digest, BigInteger N, byte[] salt, byte[] identity, byte[] password) {
        return SRP6Util.calculateX(digest, N, salt, identity, password);
    }

    public static boolean isZero(BigInteger N, BigInteger n) {
        return n.mod(N).equals(BigInteger.ZERO);
    }

    public static byte[] hash(Digest digest, byte[] bytes) {
        byte[] output = new byte[digest.getDigestSize()];
        digest.update(bytes, 0, bytes.length);
        digest.doFinal(output, 0);
        return output;
    }

    public static void update(Digest digest, byte[] bytes) {
        digest.update(bytes, 0, bytes.length);
    }

    public static int primeLength(BigInteger prime) {
        return (prime.bitLength() + 7) / 8;
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
