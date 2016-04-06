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
package com.github.horrorho.inflatabledonkey.crypto.rfc6637;

/**
 * RFC6637 constants via Bouncy Castle PGP.
 *
 * @see <a href="http://www.bouncycastle.org">http://www.bouncycastle.org</a>
 * @author Ahseya
 */
public interface RFC6637Constants {

    // Symmetric-Key Algorithms
    static final int NULL = 0;
    static final int IDEA = 1;
    static final int TRIPLE_DES = 2;
    static final int CAST5 = 3;
    static final int BLOWFISH = 4;
    static final int SAFER = 5;
    static final int DES = 6;
    static final int AES_128 = 7;
    static final int AES_192 = 8;
    static final int AES_256 = 9;
    static final int TWOFISH = 10;
    static final int CAMELLIA_128 = 11;
    static final int CAMELLIA_192 = 12;
    static final int CAMELLIA_256 = 13;

    // Hash Algorithms
    static final int MD5 = 1;
    static final int SHA1 = 2;
    static final int RIPEMD160 = 3;
    static final int DOUBLE_SHA = 4;
    static final int MD2 = 5;
    static final int TIGER_192 = 6;
    static final int HAVAL_5_160 = 7;
    static final int SHA256 = 8;
    static final int SHA384 = 9;
    static final int SHA512 = 10;
    static final int SHA224 = 11;

    //Public-Key Algorithms
    static final int RSA_GENERAL = 1;
    static final int RSA_ENCRYPT = 2;
    static final int RSA_SIGN = 3;
    static final int ELGAMAL_ENCRYPT = 16;
    static final int DSA = 17;
    static final int ECDH = 18;
    static final int ECDSA = 19;
    static final int ELGAMAL_GENERAL = 20;
    static final int DIFFIE_HELLMAN = 21;
}
