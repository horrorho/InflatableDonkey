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
package com.github.horrorho.inflatabledonkey.chunk.store;

import java.util.Arrays;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ChunkChecksums. Checksum = (byte) type | (bytes) hash
 *
 * @author Ahseya
 */
@Immutable
public final class ChunkChecksums {

    private static final Logger logger = LoggerFactory.getLogger(ChunkChecksums.class);

    private ChunkChecksums() {
    }

    public static Optional<byte[]> checksum(int type, byte[] data) {
        if (type != 1) {
            logger.warn("-- checksum() - checksum type not supported: {}", type);
            return Optional.empty();
        }

        byte[] hashType1 = hashType1(data);
        byte[] checksum = ByteUtils.concatenate(new byte[]{0x01}, hashType1);
        return Optional.of(checksum);
    }

    public static Optional<Boolean> matchToData(byte[] checksum, byte[] data) {
        return checksumType(checksum)
                .flatMap(type -> checksum(type, data))
                .map(cs -> match(cs, checksum));
    }

    public static boolean match(byte[] one, byte[] two) {
        if (!checksumType(one).equals(checksumType(two))) {
            logger.warn("-- match() - type mismatch: {} {}", Hex.toHexString(one), Hex.toHexString(two));
            return false;
        }

        Optional<byte[]> hashOne = checksumHash(one);
        if (!hashOne.isPresent()) {
            logger.warn("-- match() - no hash for checksum one: {}", Hex.toHexString(one));
            return false;
        }
        
        Optional<byte[]> hashTwo = checksumHash(two);
        if (!hashTwo.isPresent()) {
            logger.warn("-- match() - no hash for checksum two: {}", Hex.toHexString(one));
            return false;
        }

        boolean matches = Arrays.equals(hashOne.get(), hashTwo.get());
        if (!matches) {
            logger.debug("-- match() - failed hash match, one {} two: {}",
                    Hex.toHexString(one), Hex.toHexString(two));
        }
        return matches;
    }

    public static Optional<Integer> checksumType(byte[] checksum) {
        if (checksum.length == 0) {
            logger.warn("-- checksumType() - no checksum data");
            return Optional.empty();
        }

        int type = checksum[0] & 0x7F;
        return Optional.of(type);
    }

    public static Optional<byte[]> checksumHash(byte[] checksum) {
        if (checksum.length == 0) {
            logger.warn("-- checksumHash() - no checksum hash");
            return Optional.empty();
        }
        byte[] hash = Arrays.copyOfRange(checksum, 1, checksum.length);
        return Optional.of(hash);
    }

    public static byte[] hashType1(byte[] data) {
        Digest digest = new SHA256Digest();
        byte[] hash = new byte[digest.getDigestSize()];

        digest.reset();
        digest.update(data, 0, data.length);
        digest.doFinal(hash, 0);
        digest.update(hash, 0, hash.length);
        digest.doFinal(hash, 0);

        byte[] checksum = Arrays.copyOf(hash, 20);
        logger.debug("-- hashType1() - hash: 0x{}", Hex.toHexString(checksum));

        return checksum;
    }
}
