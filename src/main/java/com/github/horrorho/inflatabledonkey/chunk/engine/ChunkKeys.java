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
 *
 * @author Ahseya
 */
@Immutable
public final class ChunkKeys {

    // 447E254DA96F1A75C8FB:10007C60
    private static final Logger logger = LoggerFactory.getLogger(ChunkKeys.class);

    private ChunkKeys() {
    }

    public static Optional<byte[]> unwrap(byte[] key, byte[] kek) {
        logger.trace("-- unwrap() - key: 0x{} kek: 0x{}", Hex.toHexString(key), Hex.toHexString(kek));
        if (key.length != 0x19) {
            logger.warn("-- unwrap() - key too short: 0x{}", Hex.toHexString(key));
        }

        int keyType = key[0];
        if (keyType != 2) {
            logger.warn("-- unwrap() - unsupported key type: {}", keyType);
        }

        byte[] wrappedKey = Arrays.copyOfRange(key, 0x01, 0x19);
        Optional<byte[]> unwrappedKey = RFC3394Wrap.unwrapAES(kek, wrappedKey);

        logger.trace("-- unwrap() - unwrapped key: 0x{}", unwrappedKey.map(Hex::toHexString).orElse("NULL"));
        return unwrappedKey;
    }
}
