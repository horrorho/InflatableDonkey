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
package com.github.horrorho.inflatabledonkey.chunk.engine;

import java.util.Arrays;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ChunkChecksums.
 *
 * @author Ahseya
 */
@Immutable
public final class ChunkChecksums {

    private static final Logger logger = LoggerFactory.getLogger(ChunkChecksums.class);

    private ChunkChecksums() {
    }

    public static Optional<Boolean> match(byte[] checksumData, byte[] chunkData) {
        if (checksumData.length == 0) {
            logger.warn("-- match() - no checksum data");
            return Optional.empty();
        }

        int checksumType = checksumData[0] & 0x7F;
        if (checksumType != 1) {
            logger.warn("-- match() - checksum type not supported: {}", checksumType);
            return Optional.empty();
        }

        byte[] checksum = Arrays.copyOfRange(checksumData, 1, checksumData.length);
        byte[] chunkDataChecksum = checksumType1(chunkData);
        
        boolean matches = Arrays.equals(checksum, chunkDataChecksum);
        if (!matches) {
            logger.debug("-- match() - failed checksum match: {} to: {}",
                    Hex.toHexString(chunkDataChecksum), Hex.toHexString(checksum));
        }

        return Optional.of(matches);
    }

    public static byte[] checksumType1(byte[] data) {
        Digest digest = new SHA256Digest();
        byte[] hash = new byte[digest.getDigestSize()];

        digest.reset();
        digest.update(data, 0, data.length);
        digest.doFinal(hash, 0);
        digest.update(hash, 0, hash.length);
        digest.doFinal(hash, 0);

        byte[] checksum = Arrays.copyOf(hash, 20);
        logger.debug("-- checksum() - checksum: 0x{}", Hex.toHexString(checksum));

        return checksum;
    }
}
