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
class LZFSEBlockDecoder extends LMDBlockDecoder implements LZFSEConstants {

    private static final byte[] L_EXTRA_BITS = {
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 5, 8
    };

    private static final int[] L_BASE_VALUE = {
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 20, 28, 60
    };

    private static final byte[] M_EXTRA_BITS = {
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 5, 8, 11
    };

    private static final int[] M_BASE_VALUE = {
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 24, 56, 312
    };

    private static final byte[] D_EXTRA_BITS = {
        0,  0,  0,  0,  1,  1,  1,  1,  2,  2,  2,  2,  3,  3,  3,  3,
        4,  4,  4,  4,  5,  5,  5,  5,  6,  6,  6,  6,  7,  7,  7,  7,
        8,  8,  8,  8,  9,  9,  9,  9,  10, 10, 10, 10, 11, 11, 11, 11,
        12, 12, 12, 12, 13, 13, 13, 13, 14, 14, 14, 14, 15, 15, 15, 15
    };

    private static final int[] D_BASE_VALUE = {
        0,      1,      2,      3,     4,     6,     8,     10,    12,    16,
        20,     24,     28,     36,    44,    52,    60,    76,    92,    108,
        124,    156,    188,    220,   252,   316,   380,   444,   508,   636,
        764,    892,    1020,   1276,  1532,  1788,  2044,  2556,  3068,  3580,
        4092,   5116,   6140,   7164,  8188,  10236, 12284, 14332, 16380, 20476,
        24572,  28668,  32764,  40956, 49148, 57340, 65532, 81916, 98300, 114684,
        131068, 163836, 196604, 229372
    };

    private final LZFSEValueDecoder lValueDecoder; 
    private final LZFSEValueDecoder mValueDecoder;  
    private final LZFSEValueDecoder dValueDecoder; 

    private final LZFSELiteralDecoder literalDecoder;

    private final byte[] literals = new byte[LITERALS_PER_BLOCK + 64];
    private int pos;

    @Nullable
    private ByteBuffer bb;
    @Nullable
    private BitInStream in;

    private int rawBytes;
    private int symbols;

    LZFSEBlockDecoder(MatchBuffer mb) throws LZFSEDecoderException {
        super(mb);
        this.lValueDecoder = new LZFSEValueDecoder(ENCODE_L_STATES);
        this.mValueDecoder = new LZFSEValueDecoder(ENCODE_M_STATES);
        this.dValueDecoder = new LZFSEValueDecoder(ENCODE_D_STATES);
        this.literalDecoder = new LZFSELiteralDecoder(ENCODE_LITERAL_STATES);
    }

    @Nonnull
    LZFSEBlockDecoder init(LZFSEBlockHeader bh, @WillNotClose ReadableByteChannel ch) throws LZFSEDecoderException, IOException {
        lValueDecoder.load(bh.lFreq(), L_EXTRA_BITS, L_BASE_VALUE)
                .state(bh.lState());
        mValueDecoder.load(bh.mFreq(), M_EXTRA_BITS, M_BASE_VALUE)
                .state(bh.mState());
        dValueDecoder.load(bh.dFreq(), D_EXTRA_BITS, D_BASE_VALUE)
                .state(bh.dState());
        literalDecoder.load(bh.literalFreq())
                .state(bh.literalState0(), bh.literalState1(), bh.literalState2(), bh.literalState3())
                .nLiteralPayloadBytes(bh.nLiteralPayloadBytes())
                .nLiterals(bh.nLiterals())
                .literalBits(bh.literalBits())
                .decodeInto(ch, literals);

        initBuffer(bh.nLmdPayloadBytes());
        IO.readFully(ch, bb);
        in = new BitInStream(bb)
                .init(bh.lmdBits());

        rawBytes = bh.nRawBytes();
        symbols = bh.nMatches();

        pos = 0;

        return this;
    }

    int rawBytes() {
        return rawBytes;
    }

    @Override
    byte literal() throws IOException {
        return literals[pos++];
    }

    @Override
    boolean lmd() throws LZFSEDecoderException {
        if (symbols > 0) {
            symbols--;
            in.fill();
            l(lValueDecoder.decode(in));
            m(mValueDecoder.decode(in));
            d(dValueDecoder.decode(in));
            return true;

        } else {
            return false;
        }
    }

    void initBuffer(int nLmdPayloadBytes) {
        int capacity = 32 + nLmdPayloadBytes;
        if (bb == null || bb.capacity() < capacity) {
            bb = ByteBuffer.allocate(capacity).order(LITTLE_ENDIAN);
        } else {
            bb.limit(capacity);
        }
        bb.position(32);
    }

    @Override
    public String toString() {
        return "LZFSEBlockDecoder{"
                + "lValueDecoder=" + lValueDecoder
                + ", mValueDecoder=" + mValueDecoder
                + ", dValueDecoder=" + dValueDecoder
                + ", literalDecoder=" + literalDecoder
                + ", literals=.length" + literals.length
                + ", bb=" + bb
                + ", in=" + in
                + '}';
    }
}
