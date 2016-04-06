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
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.NotThreadSafe;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.agreement.srp.SRP6Util;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SRPClient. SRP-6a client.
 *
 * @author Ahseya
 */
@NotThreadSafe
public class SRPClient {
    // https://en.wikipedia.org/wiki/Secure_Remote_Password_protocol

    protected static final Logger logger = LoggerFactory.getLogger(SRPClient.class);

    protected final SecureRandom random;
    protected final Digest digest;
    protected final BigInteger N;
    protected final BigInteger g;
    protected BigInteger a;
    protected byte[] ephemeralKeyA;
    protected byte[] key;
    protected byte[] M1;

    public SRPClient(SecureRandom random, Digest digest, BigInteger N, BigInteger g) {
        this.random = Objects.requireNonNull(random, "random");
        this.digest = Objects.requireNonNull(digest, "digest");
        this.N = Objects.requireNonNull(N, "N");
        this.g = Objects.requireNonNull(g, "g");

        logger.debug(" **SRP() - N: 0x{}", N.toString(16));
        logger.debug(" **SRP() - g: 0x{}", g.toString(16));
    }

    public byte[] generateClientCredentials() {
        return generateClientCredentials(SRP6Util.generatePrivateValue(digest, N, g, random));
    }

    // TODO to package private
    public byte[] generateClientCredentials(BigInteger a) {
        // Package private test injection point.
        this.a = a;
        logger.debug("-- generateClientCredentials() - a: 0x{}", a.toString(16));

        ephemeralKeyA = SRPCore.generateEphemeralKeyA(N, g, a);
        logger.debug("-- generateClientCredentials() - ephemeral key A: 0x{}", Hex.toHexString(ephemeralKeyA));

        return Arrays.copyOf(ephemeralKeyA, ephemeralKeyA.length);
    }

    public Optional<byte[]>
            calculateClientEvidenceMessage(byte[] salt, byte[] identity, byte[] password, byte[] serverB) {

        if (this.ephemeralKeyA == null) {
            throw new IllegalStateException("Client credentials not yet generated");
        }

        BigInteger B = new BigInteger(1, serverB);

        if (SRPCore.isZero(N, serverB)) {
            return Optional.empty();
        }

        BigInteger u = SRPCore.calculateu(digest, ephemeralKeyA, serverB);
        logger.debug("-- calculateClientEvidenceMessage() - u: 0x{}", u.toString(16));

        BigInteger x = SRPCore.generatex(digest, N, salt, identity, password);
        logger.debug("-- calculateClientEvidenceMessage() - x: 0x{}", x.toString(16));

        BigInteger k = SRPCore.calculatek(digest, N, g);
        logger.debug("-- calculateClientEvidenceMessage() - k: 0x{}", k.toString(16));

        BigInteger S = SRPCore.calculateS(digest, N, g, a, k, u, x, B);
        logger.debug("-- calculateClientEvidenceMessage() - S: 0x{}", S.toString(16));

        key = SRPCore.generateKey(digest, N, S);
        logger.debug("-- calculateClientEvidenceMessage() - key: 0x{}", Hex.toHexString(key));

        M1 = SRPCore.calculateM1(digest, N, g, ephemeralKeyA, serverB, key, salt, identity);
        logger.debug("-- calculateClientEvidenceMessage() - M1: 0x{}", Hex.toHexString(M1));

        return Optional.of(M1);
    }

    public Optional<byte[]> verifyServerEvidenceMessage(byte[] serverM2) {
        byte[] computedM2 = SRPCore.calculateM2(digest, N, ephemeralKeyA, M1, key);
        logger.debug("-- verifyServerEvidenceMessage() - computed M2: {}", Hex.toHexString(computedM2));

        if (Arrays.equals(computedM2, serverM2)) {
            logger.debug("-- verifyServerEvidenceMessage() - server M2 verification passed");
            return Optional.of(Arrays.copyOf(key, key.length));
        }

        logger.warn("-- verifyServerEvidenceMessage() - server M2 verification failed");
        return Optional.empty();
    }
}
