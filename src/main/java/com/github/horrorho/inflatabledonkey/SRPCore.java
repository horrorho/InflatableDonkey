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
package com.github.horrorho.inflatabledonkey;

import java.math.BigInteger;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.agreement.srp.SRP6Util;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.bouncycastle.util.BigIntegers;

/**
 *
 *
 * @author Ahseya
 */
@Immutable
public final class SRPCore {

    public static BigInteger A(BigInteger N, BigInteger g, BigInteger a) {
        return g.modPow(a, N);
    }

    public static BigInteger k(Digest digest, BigInteger N, BigInteger g) {
        return SRP6Util.calculateK(digest, N, g);
    }

    public static BigInteger K(Digest digest, BigInteger N, BigInteger S) {
        return SRP6Util.calculateKey(digest, N, S);
    }

    public static BigInteger M1(
            Digest digest,
            BigInteger N,
            BigInteger g,
            BigInteger A,
            BigInteger B,
            BigInteger K,
            byte[] salt,
            byte[] identity) {

        // M1 = H(H(N) ^ H(g), H(ID), SALT, A, B, K)
        int length = (N.bitLength() + 7) / 8;

        byte[] hashIdentity = hash(digest, identity);

        byte[] xor = ByteUtils.xor(hash(digest, bytes(N, length)), hash(digest, bytes(g, length)));

        byte[] output = new byte[digest.getDigestSize()];
        update(digest, xor);
        update(digest, hashIdentity);
        update(digest, salt);
        update(digest, bytes(A, length));
        update(digest, bytes(B, length));
        update(digest, bytes(K, digest.getDigestSize()));
        digest.doFinal(output, 0);

        return new BigInteger(1, output);
    }

    public static BigInteger S(
            Digest digest,
            BigInteger N,
            BigInteger g,
            BigInteger a,
            BigInteger k,
            BigInteger u,
            BigInteger x,
            BigInteger B) {

        BigInteger exp = u.multiply(x).add(a);
        BigInteger tmp = g.modPow(x, N).multiply(k).mod(N);
        return B.subtract(tmp).mod(N).modPow(exp, N);
    }

    public static BigInteger u(Digest digest, BigInteger N, BigInteger A, BigInteger B) {
        return SRP6Util.calculateU(digest, N, A, B);
    }

    public static boolean isZero(BigInteger N, BigInteger n) {
        return n.mod(N).equals(BigInteger.ZERO);
    }

    public static BigInteger x(Digest digest, BigInteger N, byte[] salt, byte[] identity, byte[] password) {
        return SRP6Util.calculateX(digest, N, salt, identity, password);
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

    public static byte[] bytes(BigInteger n, int length) {
        byte[] bs = BigIntegers.asUnsignedByteArray(n);
        if (bs.length < length) {
            byte[] tmp = new byte[length];
            System.arraycopy(bs, 0, tmp, length - bs.length, bs.length);
            bs = tmp;
        }
        return bs;
    }
}
