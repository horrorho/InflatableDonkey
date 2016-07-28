/*
 * The MIT License
 *
 * Copyright 2015 Ahseya.
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

import com.github.horrorho.inflatabledonkey.crypto.RFC3394Wrap;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converts type 0x02 chunk encryption keys to type 0x01 chunk encryption keys.
 *
 * @author Ahseya
 */
@Immutable
public final class ChunkKeys implements ChunkEncryptionKeyConverter<byte[]> {

    public static ChunkKeys instance() {
        return INSTANCE;
    }
    private static final Logger logger = LoggerFactory.getLogger(ChunkKeys.class);

    private static final ChunkKeys INSTANCE = new ChunkKeys();

    private ChunkKeys() {
    }

    @Override
    public Optional<byte[]> apply(byte[] chunkEncryptionKey, byte[] keyEncryptionKey) {
        logger.trace("<< apply() - chunkEncryptionKey: 0x{} keyEncryptionKey: 0x{}",
                Hex.toHexString(chunkEncryptionKey), Hex.toHexString(keyEncryptionKey));
        Optional<byte[]> out = doApply(chunkEncryptionKey, keyEncryptionKey);
        logger.trace(">> apply() - chunkEncryptionKey: 0x{}", out.map(Hex::toHexString).orElse("NULL"));
        return out;
    }

    public Optional<byte[]> doApply(byte[] chunkEncryptionKey, byte[] keyEncryptionKey) {
        if (chunkEncryptionKey.length == 0x00) {
            logger.warn("-- doApply() - empty chunk key encryption key", Hex.toHexString(chunkEncryptionKey));
            return Optional.empty();
        }
        int keyType = chunkEncryptionKey[0];
        switch (keyType) {
            case 1:
                return type1(chunkEncryptionKey);
            case 2:
                return type2(chunkEncryptionKey, keyEncryptionKey);
            default:
                logger.warn("-- doApply() - unsupported key type: {}", keyType);
                return Optional.empty();
        }
    }

    Optional<byte[]> type1(byte[] chunkEncryptionKey) {
        if (chunkEncryptionKey.length != 0x11) {
            logger.warn("-- type1() - bad chunk encryption key length: 0x:{}", Hex.toHexString(chunkEncryptionKey));
            return Optional.empty();
        }
        return Optional.of(chunkEncryptionKey);
    }

    Optional<byte[]> type2(byte[] chunkEncryptionKey, byte[] keyEncryptionKey) {
        if (chunkEncryptionKey.length != 0x19) {
            logger.warn("-- type2() - bad chunk encryption key length: 0x:{}", Hex.toHexString(chunkEncryptionKey));
            return Optional.empty();
        }
        byte[] wrappedKey = Arrays.copyOfRange(chunkEncryptionKey, 0x01, 0x19);
        return RFC3394Wrap.unwrapAES(keyEncryptionKey, wrappedKey)
                .map(u -> {
                    byte[] k = new byte[0x11];
                    k[0] = 0x01;
                    System.arraycopy(u, 0, k, 1, u.length);
                    return k;
                });
    }
}
