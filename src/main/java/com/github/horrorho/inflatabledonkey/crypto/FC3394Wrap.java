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
package com.github.horrorho.inflatabledonkey.crypto;

import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.engines.RFC3394WrapEngine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RFC3394Wrap AES.
 *
 * @author ahseya
 */
@Immutable
public final class FC3394Wrap {

    private static final Logger logger = LoggerFactory.getLogger(FC3394Wrap.class);

    private FC3394Wrap() {
    }

    public static Optional<byte[]> unwrapAES(byte[] keyEncryptionKey, byte[] wrappedKey) {
        try {
            RFC3394WrapEngine engine = new RFC3394WrapEngine(new AESFastEngine());
            engine.init(false, new KeyParameter(keyEncryptionKey));
            return Optional.of(engine.unwrap(wrappedKey, 0, wrappedKey.length));

        } catch (InvalidCipherTextException ex) {
            logger.debug("-- unwrap() - InvalidCipherTextException: {}", ex.getMessage());
            return Optional.empty();
        }
    }

    public static byte[] wrapAES(byte[] keyEncryptionKey, byte[] unwrappedKey) {
        RFC3394WrapEngine engine = new RFC3394WrapEngine(new AESFastEngine());
        engine.init(true, new KeyParameter(keyEncryptionKey));
        return engine.wrap(unwrappedKey, 0, unwrappedKey.length);
    }
}
