/*
 * The MIT License
 *
 * Copyright 2017 Ayesha.
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
package com.github.horrorho.ragingmoose;

import static java.lang.Long.toHexString;
import java.nio.ByteBuffer;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * Low level bit in stream.
 *
 * @author Ayesha
 */
@NotThreadSafe
@ParametersAreNonnullByDefault
class BitInStream {
    // accumNBits 63 bit limit avoids unsupported 64 bit shifts/ branch.

    private final ByteBuffer in;
    private long accum;
    private int accumNBits;

    BitInStream(ByteBuffer in, long accum, int accumNBits) {
        this.in = Objects.requireNonNull(in);
        this.accum = accum;
        this.accumNBits = accumNBits;
    }

    BitInStream(ByteBuffer in) {
        this(in, 0, 0);
    }

    @Nonnull
    BitInStream init(int n) throws LZFSEDecoderException {
        try {
            if (n > 0) {
                throw new LZFSEDecoderException();
            } else if (n == 0) {
                in.position(in.position() - 7);
                accum = in.getLong(in.position() - 1);
                accum >>>= 8;
                accumNBits = 56;
            } else {
                in.position(in.position() - 8);
                accum = in.getLong(in.position());
                accumNBits = n + 64;
            }
//            check();
            return this;

        } catch (IllegalArgumentException ex) {
            throw new LZFSEDecoderException(ex);
        }
    }

    @Nonnull
    BitInStream fill() throws LZFSEDecoderException {
        try {
            if (accumNBits < 56) {
                int nBits = 63 - accumNBits;
                int nBytes = nBits >>> 3;
                int mBits = (nBits & 0x07) + 1;
                in.position(in.position() - nBytes);
                accum = in.getLong(in.position());
                accum <<= mBits;
                accum >>>= mBits;
                accumNBits += nBytes << 3;
            }
//            check();
            return this;

        } catch (IllegalArgumentException ex) {
            throw new LZFSEDecoderException(ex);
        }
    }

    long read(int n) {
        if (n > accumNBits) {
            throw new IllegalStateException();
        }
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        accumNBits -= n;
        long bits = accum >>> accumNBits;
        accum ^= bits << accumNBits;
        return bits;
    }

    void check() {
        if (accumNBits < 56) {
            throw new AssertionError("accumulator underflow: " + accumNBits);
        }
        if (accumNBits > 63) {
            throw new AssertionError("accumulator overflow: " + accumNBits);
        }
        if (accum >>> accumNBits != 0) {
            throw new AssertionError("accumulator corruption: 0x" + toHexString(accum) + " " + accumNBits);
        }
    }

    @Override
    public String toString() {
        return "BitStream{"
                + "in=" + in
                + ", accum=0x" + toHexString(accum)
                + ", accumNBits=" + accumNBits
                + '}';
    }
}
