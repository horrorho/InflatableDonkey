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
import java.security.SecureRandom;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;

/**
 * SRPFactory.
 *
 * @author Ahseya
 */
@Immutable
public final class SRPFactory {

    public static SRPClient rfc5054(SecureRandom random) {
        BigInteger N = new BigInteger(DEFAULT_PRIME_HEX, 16);
        Digest digest = new SHA256Digest();
        return new SRPClient(random, digest, N, BigInteger.valueOf(2));
    }

    // https://tools.ietf.org/html/rfc5054 2048-bit Group
    private static final String DEFAULT_PRIME_HEX
            = "AC6BDB41324A9A9BF166DE5E1389582FAF72B6651987EE07FC319294"
            + "3DB56050A37329CBB4A099ED8193E0757767A13DD52312AB4B03310D"
            + "CD7F48A9DA04FD50E8083969EDB767B0CF6095179A163AB3661A05FB"
            + "D5FAAAE82918A9962F0B93B855F97993EC975EEAA80D740ADBF4FF74"
            + "7359D041D5C33EA71D281E446B14773BCA97B43A23FB801676BD207A"
            + "436C6481F1D2B9078717461A5B9D32E688F87748544523B524B0D57D"
            + "5EA77A2775D2ECFA032CFBDBF52FB3786160279004E57AE6AF874E73"
            + "03CE53299CCC041C7BC308D82A5698F3A8D0C38271AE35F8E9DBFBB6"
            + "94B5C803D89F7AE435DE236D525F54759B65E372FCD68EF20FA7111F"
            + "9E4AFF73";
}
