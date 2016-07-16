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
package com.github.horrorho.inflatabledonkey.crypto.xtsaes;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.LongFunction;
import net.jcip.annotations.NotThreadSafe;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Pack;

/**
 *
 * @author Ahseya
 */
@NotThreadSafe
class XTSTweak {

    static byte[] defaultTweakFunction(long tweakValue) {
        byte[] bs = Pack.longToLittleEndian(tweakValue);
        bs = Arrays.copyOfRange(bs, 0, BLOCK_SIZE);
        return bs;
    }

    private static final long FDBK = 0x87;
    private static final long MSB = 0x8000000000000000L;
    private static final int BLOCK_SIZE = 16;

    private final BlockCipher cipher;
    private final LongFunction<byte[]> tweakFunction;
    private final byte[] tweak;

    XTSTweak(BlockCipher cipher, LongFunction<byte[]> tweakFunction, byte[] tweak) {
        this.cipher = Objects.requireNonNull(cipher, "cipher");
        this.tweakFunction = Objects.requireNonNull(tweakFunction, "tweakFunction");
        this.tweak = Objects.requireNonNull(tweak, "tweak");

        if (cipher.getBlockSize() != BLOCK_SIZE) {
            throw new IllegalArgumentException("bad block size: " + cipher.getBlockSize());
        }
    }

    XTSTweak(BlockCipher cipher, LongFunction<byte[]> tweakFunction) {
        this(cipher, tweakFunction, new byte[cipher.getBlockSize()]);
    }

    XTSTweak(LongFunction<byte[]> tweakFunction) {
        this(new AESFastEngine(), tweakFunction);
    }

    XTSTweak() {
        this(XTSTweak::defaultTweakFunction);
    }

    XTSTweak init(KeyParameter key) throws IllegalArgumentException {
        cipher.init(true, key);
        return this;
    }

    XTSTweak reset(long tweakValue) throws DataLengthException, IllegalStateException {
        return reset(tweakFunction.apply(tweakValue));
    }

    XTSTweak reset(byte[] tweakBytes) throws DataLengthException, IllegalStateException {
        cipher.processBlock(tweakBytes, 0, tweak, 0);
        return this;
    }

    byte[] value() {
        return Arrays.copyOf(tweak, tweak.length);
    }

    XTSTweak next() {
        long lo = Pack.littleEndianToLong(tweak, 0);
        long hi = Pack.littleEndianToLong(tweak, 8);

        long fdbk = (hi & MSB) == 0
                ? 0L
                : FDBK;

        hi = (hi << 1) | (lo >>> 63);
        lo = (lo << 1) ^ fdbk;

        Pack.longToLittleEndian(lo, tweak, 0);
        Pack.longToLittleEndian(hi, tweak, 8);
        return this;
    }
}
