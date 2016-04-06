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
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.NotThreadSafe;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.agreement.srp.SRP6Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SRPClient. SRP-6a client.
 *
 * @author Ahseya
 */
@NotThreadSafe
public class SRPClient {

    protected static final Logger logger = LoggerFactory.getLogger(SRPClient.class);

    protected final SecureRandom random;
    protected final Digest digest;
    protected final BigInteger N;
    protected final BigInteger g;
    protected BigInteger a;
    protected BigInteger A;
    protected BigInteger K;
    protected BigInteger M1;

    public SRPClient(SecureRandom random, Digest digest, BigInteger N, BigInteger g) {
        this.random = Objects.requireNonNull(random, "random");
        this.digest = Objects.requireNonNull(digest, "digest");
        this.N = Objects.requireNonNull(N, "N");
        this.g = Objects.requireNonNull(g, "g");

        logger.debug(" **SRP() - N: 0x{}", N.toString(16));
        logger.debug(" **SRP() - g: 0x{}", g.toString(16));
    }

    // TODO byte[] result
    public BigInteger generateClientCredentials() {
        return generateClientCredentials(SRP6Util.generatePrivateValue(digest, N, g, random));
    }

    // TODO to package private
    public BigInteger generateClientCredentials(BigInteger a) {
        // Package private test injection point.
        this.a = a;
        logger.debug("-- generateClientCredentials() - a: 0x{}", a.toString(16));

        A = SRPCore.calculateA(N, g, a);
        logger.debug("-- generateClientCredentials() - A: 0x{}", A.toString(16));

        return A;
    }

    // TODO optional result
    // TODO byte[] result
    public BigInteger
            calculateClientEvidenceMessage(byte[] salt, byte[] identity, byte[] password, BigInteger serverB) {

        if (this.A == null) {
            throw new IllegalStateException("Client credentials not yet generated");
        }

        if (SRPCore.isZero(N, serverB)) {
            return null;
        }

        BigInteger u = SRPCore.calculateu(digest, N, A, serverB);
        logger.debug("-- calculateClientEvidenceMessage() - u: 0x{}", u.toString(16));

        BigInteger x = SRPCore.calculatex(digest, N, salt, identity, password);
        logger.debug("-- calculateClientEvidenceMessage() - x: 0x{}", x.toString(16));

        BigInteger k = SRPCore.calculatek(digest, N, g);
        logger.debug("-- calculateClientEvidenceMessage() - k: 0x{}", k.toString(16));

        BigInteger S = SRPCore.calculateS(digest, N, g, a, k, u, x, serverB);
        logger.debug("-- calculateClientEvidenceMessage() - S: 0x{}", S.toString(16));

        K = SRPCore.calculateK(digest, N, S);
        logger.debug("-- calculateClientEvidenceMessage() - K: 0x{}", K.toString(16));

        M1 = SRPCore.calculateM1(digest, N, g, A, serverB, K, salt, identity);
        logger.debug("-- calculateClientEvidenceMessage() - M1: 0x{}", M1.toString(16));

        return M1;
    }

    public Optional<byte[]> verifyServerEvidenceMessage(BigInteger serverM2) {

        BigInteger computedM2 = SRPCore.calculateM2(digest, N, A, M1, K);
        logger.debug("-- verifyServerEvidenceMessage() - computed M2: {}", computedM2.toString(16));

        if (computedM2.equals(serverM2)) {
            logger.debug("-- verifyServerEvidenceMessage() - server M2 verification passed");
            byte[] key = SRPCore.padded(K, digest.getDigestSize());
            return Optional.of(key);
        }

        logger.warn("-- verifyServerEvidenceMessage() - server M2 verification failed");
        return Optional.empty();
    }
}
