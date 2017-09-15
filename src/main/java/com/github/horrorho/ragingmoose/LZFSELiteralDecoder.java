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

import java.io.IOException;
import java.nio.ByteBuffer;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import java.nio.channels.ReadableByteChannel;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.WillNotClose;
import javax.annotation.concurrent.NotThreadSafe;

/**
 *
 * @author Ayesha
 */
@NotThreadSafe
@ParametersAreNonnullByDefault
class LZFSELiteralDecoder {

    private final TANS<TANS.Entry> tans;
    private final TANS.State state0;
    private final TANS.State state1;
    private final TANS.State state2;
    private final TANS.State state3;

    @Nullable
    private ByteBuffer bb;

    private int nLiteralPayloadBytes;
    private int nLiterals;
    private int literalBits;

    LZFSELiteralDecoder(int nStates) throws LZFSEDecoderException {
        this.tans = new TANS<>(nStates, TANS.Entry::new, TANS.Entry[]::new);
        this.state0 = new TANS.State();
        this.state1 = new TANS.State();
        this.state2 = new TANS.State();
        this.state3 = new TANS.State();
    }

    @Nonnull
    LZFSELiteralDecoder load(short[] weights) throws LZFSEDecoderException {
        tans.init(weights);
        return this;
    }

    @Nonnull
    LZFSELiteralDecoder state(int state0, int state1, int state2, int state3) {
        this.state0.value(state0);
        this.state1.value(state1);
        this.state2.value(state2);
        this.state3.value(state3);
        return this;
    }

    @Nonnull
    LZFSELiteralDecoder nLiteralPayloadBytes(int nLiteralPayloadBytes) {
        this.nLiteralPayloadBytes = nLiteralPayloadBytes;
        return this;
    }

    @Nonnull
    LZFSELiteralDecoder nLiterals(int nLiterals) {
        this.nLiterals = nLiterals;
        return this;
    }

    @Nonnull
    LZFSELiteralDecoder literalBits(int literalBits) {
        this.literalBits = literalBits;
        return this;
    }

    @Nonnull
    LZFSELiteralDecoder decodeInto(@WillNotClose ReadableByteChannel ch, byte[] literals)
            throws IOException, LZFSEDecoderException {
        initBuffer();
        IO.readFully(ch, bb);
        BitInStream in = new BitInStream(bb)
                .init(literalBits);

        for (int i = 0; i < nLiterals; i += 4) {
            in.fill();
            literals[i + 0] = tans.transition(state0, in).symbol();
            literals[i + 1] = tans.transition(state1, in).symbol();
            literals[i + 2] = tans.transition(state2, in).symbol();
            literals[i + 3] = tans.transition(state3, in).symbol();
        }
        return this;
    }

    @Nonnull
    void initBuffer() {
        int capacity = 8 + nLiteralPayloadBytes;
        if (bb == null || bb.capacity() < capacity) {
            bb = ByteBuffer.allocate(capacity).order(LITTLE_ENDIAN);
        } else {
            bb.limit(capacity);
        }
        bb.position(8);
    }

    @Override
    public String toString() {
        return "LZFSELiteralDecoder{"
                + "tans=" + tans
                + ", state0=" + state0
                + ", state1=" + state1
                + ", state2=" + state2
                + ", state3=" + state3
                + ", nLiteralPayloadBytes=" + nLiteralPayloadBytes
                + ", nLiterals=" + nLiterals
                + ", literalBits=" + literalBits
                + '}';
    }
}
