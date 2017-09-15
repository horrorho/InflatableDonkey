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
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import java.nio.channels.ReadableByteChannel;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.WillNotClose;
import javax.annotation.concurrent.NotThreadSafe;

/**
 *
 * @author Ayesha
 */
@NotThreadSafe
@ParametersAreNonnullByDefault
class LZFSEBlockHeader implements LZFSEConstants {

    static void initV1Tables(ByteBuffer bb, short[]... tables) throws LZFSEDecoderException {
        try {
            for (int i = 0, j = 0, k = 0, n = count(tables); i < n; i++) {
                tables[j][k] = bb.getShort();
                if (++k == tables[j].length) {
                    j++;
                    k = 0;
                }
            }
        } catch (BufferUnderflowException ex) {
            throw new LZFSEDecoderException(ex);
        }
    }

    static void initV2Tables(ByteBuffer bb, short[]... tables) throws LZFSEDecoderException {
        int accum = 0;
        int accumNBits = 0;
        for (int i = 0, j = 0, k = 0, n = count(tables); i < n; i++) {
            while (bb.hasRemaining() && accumNBits + 8 <= 32) {
                accum |= (bb.get() & 0xFF) << accumNBits;
                accumNBits += 8;
            }

            int nbits = nBits(accum);
            if (nbits > accumNBits) {
                throw new LZFSEDecoderException();
            }

            tables[j][k] = (short) value(accum, nbits);
            if (++k == tables[j].length) {
                j++;
                k = 0;
            }

            accum >>>= nbits;
            accumNBits -= nbits;
        }
        if (accumNBits >= 8 || bb.hasRemaining()) {
            throw new LZFSEDecoderException();
        }
    }

    static int nBits(int bits) {
        return FREQ_NBITS_TABLE[bits & 0x1F];
    }

    private static final byte[] FREQ_NBITS_TABLE = new byte[]{
        2, 3, 2, 5, 2, 3, 2, 8, 2, 3, 2, 5, 2, 3, 2, 14,
        2, 3, 2, 5, 2, 3, 2, 8, 2, 3, 2, 5, 2, 3, 2, 14
    };

    static int value(int bits, int nBits) {
        if (nBits == 8) {
            return 8 + (bits >>> 4 & 0x000F);
        }
        if (nBits == 14) {
            return 24 + (bits >>> 4 & 0x03FF);
        }
        return FREQ_VALUE_TABLE[bits & 0x1F];
    }

    private static final byte[] FREQ_VALUE_TABLE = new byte[]{
        0, 2, 1, 4, 0, 3, 1, -1, 0, 2, 1, 5, 0, 3, 1, -1,
        0, 2, 1, 6, 0, 3, 1, -1, 0, 2, 1, 7, 0, 3, 1, -1
    };

    static int count(short[]... tables) {
        int n = 0;
        for (short[] table : tables) {
            n += table.length;
        }
        return n;
    }

    static void clear(short[]... tables) {
        for (short[] table : tables) {
            clear(table);
        }
    }

    static void clear(short[] table) {
        for (int i = 0; i < table.length; i++) {
            table[i] = 0;
        }
    }

    static int n(long v, long offset, long nBits) {
        return (int) (v >>> offset & (1L << nBits) - 1L);
    }

    private static final int V1_SIZE = 48 + ENCODE_SYMBOLS * 2;

    private static final int V2_SIZE = 28;

    private final ByteBuffer bb = ByteBuffer.allocate(V1_SIZE).order(LITTLE_ENDIAN);

    private final short[] literalFreq = new short[ENCODE_LITERAL_SYMBOLS];
    private final short[] lFreq = new short[ENCODE_L_SYMBOLS];
    private final short[] mFreq = new short[ENCODE_M_SYMBOLS];
    private final short[] dFreq = new short[ENCODE_D_SYMBOLS];

    private int nRawBytes;
    private int nPayloadBytes;
    private int nLiterals;
    private int nMatches;
    private int nLiteralPayloadBytes;
    private int nLmdPayloadBytes;

    private int literalBits;
    private int lmdBits;

    private int lState;
    private int mState;
    private int dState;
    private int literalState0;
    private int literalState1;
    private int literalState2;
    private int literalState3;

    @Nonnull
    LZFSEBlockHeader loadV1(@WillNotClose ReadableByteChannel ch) throws IOException, LZFSEDecoderException {
        bb.rewind().limit(V1_SIZE);
        IO.readFully(ch, bb).flip();

        nRawBytes(bb.getInt());
        nPayloadBytes(bb.getInt());
        nLiterals(bb.getInt());
        nMatches(bb.getInt());
        nLiteralPayloadBytes(bb.getInt());
        nLmdPayloadBytes(bb.getInt());

        literalBits = bb.getInt();
        literalState0 = bb.getShort();
        literalState1 = bb.getShort();
        literalState2 = bb.getShort();
        literalState3 = bb.getShort();

        lmdBits = bb.getInt();
        lState = bb.getShort();
        mState = bb.getShort();
        dState = bb.getShort();

        initV1Tables(bb, lFreq, mFreq, dFreq, literalFreq);

        return this;
    }

    @Nonnull
    LZFSEBlockHeader loadV2(@WillNotClose ReadableByteChannel in) throws IOException, LZFSEDecoderException {
        bb.rewind().limit(V2_SIZE);
        IO.readFully(in, bb).flip();

        nRawBytes(bb.getInt());

        long v0 = bb.getLong();
        long v1 = bb.getLong();
        long v2 = bb.getLong();

        int headerSize = n(v2, 00, 32);
        int nCompressedPayload = headerSize - V2_SIZE - 4;

        nLiterals(n(v0, 00, 20));
        nLiteralPayloadBytes(n(v0, 20, 20));
        literalBits = n(v0, 60, 03) - 7;
        literalState0 = n(v1, 00, 10);
        literalState1 = n(v1, 10, 10);
        literalState2 = n(v1, 20, 10);
        literalState3 = n(v1, 30, 10);

        nMatches(n(v0, 40, 20));
        nLmdPayloadBytes(n(v1, 40, 20));
        lmdBits = n(v1, 60, 03) - 7;
        lState = n(v2, 32, 10);
        mState = n(v2, 42, 10);
        dState = n(v2, 52, 10);

        nPayloadBytes(nLiteralPayloadBytes + nLmdPayloadBytes);

        if (nCompressedPayload == 0) {
            clear(lFreq, mFreq, dFreq, literalFreq);

        } else if (nCompressedPayload > bb.capacity()) {
            throw new LZFSEDecoderException();

        } else {
            bb.rewind().limit(nCompressedPayload);
            IO.readFully(in, bb).flip();

            initV2Tables(bb, lFreq, mFreq, dFreq, literalFreq);
        }
        return this;
    }

    @Nonnull
    short[] literalFreq() {
        return literalFreq;
    }

    @Nonnull
    short[] lFreq() {
        return lFreq;
    }

    @Nonnull
    short[] mFreq() {
        return mFreq;
    }

    @Nonnull
    short[] dFreq() {
        return dFreq;
    }

    int nRawBytes() {
        return nRawBytes;
    }

    private void nRawBytes(int v) throws LZFSEDecoderException {
        if (v < 0) {
            throw new LZFSEDecoderException();
        }
        this.nRawBytes = v;
    }

    int nPayloadBytes() {
        return nPayloadBytes;
    }

    private void nPayloadBytes(int v) throws LZFSEDecoderException {
        if (v < 0) {
            throw new LZFSEDecoderException();
        }
        this.nPayloadBytes = v;
    }

    int nLiterals() {
        return nLiterals;
    }

    private void nLiterals(int v) throws LZFSEDecoderException {
        if (v < 0) {
            throw new LZFSEDecoderException();
        }
        this.nLiterals = v;
    }

    int nMatches() {
        return nMatches;
    }

    private void nMatches(int v) throws LZFSEDecoderException {
        if (v < 0) {
            throw new LZFSEDecoderException();
        }
        this.nMatches = v;
    }

    int nLiteralPayloadBytes() {
        return nLiteralPayloadBytes;
    }

    private void nLiteralPayloadBytes(int v) throws LZFSEDecoderException {
        if (v < 0) {
            throw new LZFSEDecoderException();
        }
        this.nLiteralPayloadBytes = v;
    }

    int nLmdPayloadBytes() {
        return nLmdPayloadBytes;
    }

    private void nLmdPayloadBytes(int v) throws LZFSEDecoderException {
        if (v < 0) {
            throw new LZFSEDecoderException();
        }
        this.nLmdPayloadBytes = v;
    }

    int literalBits() {
        return literalBits;
    }

    int lmdBits() {
        return lmdBits;
    }

    int lState() {
        return lState;
    }

    int mState() {
        return mState;
    }

    int dState() {
        return dState;
    }

    int literalState0() {
        return literalState0;
    }

    int literalState1() {
        return literalState1;
    }

    int literalState2() {
        return literalState2;
    }

    int literalState3() {
        return literalState3;
    }

    @Override
    public String toString() {
        return "LZFSEBlockHeader{"
                + "literalFreq=" + literalFreq.length
                + ", lFreq=" + lFreq.length
                + ", mFreq=" + mFreq.length
                + ", dFreq=" + dFreq.length
                + ", nRawBytes=" + nRawBytes
                + ", nPayloadBytes=" + nPayloadBytes
                + ", nLiterals=" + nLiterals
                + ", nMatches=" + nMatches
                + ", nLiteralPayloadBytes=" + nLiteralPayloadBytes
                + ", nLmdPayloadBytes=" + nLmdPayloadBytes
                + ", literalBits=" + literalBits
                + ", lmdBits=" + lmdBits
                + ", lState=" + lState
                + ", mState=" + mState
                + ", dState=" + dState
                + ", literalState0=" + literalState0
                + ", literalState1=" + literalState1
                + ", literalState2=" + literalState2
                + ", literalState3=" + literalState3
                + '}';
    }
}
