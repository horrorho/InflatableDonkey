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

import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.StreamBlockCipher;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ChunkDecrypters.
 *
 * @author Ahseya
 */
@Immutable
public final class ChunkDecrypters {

    private static final Logger logger = LoggerFactory.getLogger(ChunkDecrypters.class);

    private ChunkDecrypters() {
    }

    public static byte[] decrypt(byte[] key, byte[] data, int offset, int length) {
        StreamBlockCipher cipher = new CFBBlockCipher(new AESFastEngine(), 128);
        return decrypt(key, cipher, data, offset, length);
    }

    public static byte[] decrypt(byte[] key, StreamBlockCipher cipher, byte[] data, int offset, int length) {
        KeyParameter keyParameter = new KeyParameter(key);
        cipher.init(false, keyParameter);
        byte[] decrypted = new byte[length];
        cipher.processBytes(data, offset, length, decrypted, 0);
        return decrypted;
    }
}
// TODO optional for errors
// TODO OutputStream
// org.bouncycastle.crypto.DataLengthException
