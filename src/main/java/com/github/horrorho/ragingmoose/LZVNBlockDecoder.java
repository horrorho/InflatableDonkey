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
final class LZVNBlockDecoder extends LMDBlockDecoder {

    private interface Op {

        boolean call(int opc) throws LZFSEDecoderException;
    }

    private final Op[] tbl = new Op[]{
        this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::eos,  this::lrgD,
        this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::nop,  this::lrgD,
        this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::nop,  this::lrgD,
        this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::udef, this::lrgD,
        this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::udef, this::lrgD,
        this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::udef, this::lrgD,
        this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::udef, this::lrgD,
        this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::udef, this::lrgD,
        this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::preD, this::lrgD,
        this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::preD, this::lrgD,
        this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::preD, this::lrgD,
        this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::preD, this::lrgD,
        this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::preD, this::lrgD,
        this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::preD, this::lrgD,
        this::udef, this::udef, this::udef, this::udef, this::udef, this::udef, this::udef, this::udef,
        this::udef, this::udef, this::udef, this::udef, this::udef, this::udef, this::udef, this::udef,
        this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::preD, this::lrgD,
        this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::preD, this::lrgD,
        this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::preD, this::lrgD,
        this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::preD, this::lrgD,
        this::medD, this::medD, this::medD, this::medD, this::medD, this::medD, this::medD, this::medD,
        this::medD, this::medD, this::medD, this::medD, this::medD, this::medD, this::medD, this::medD,
        this::medD, this::medD, this::medD, this::medD, this::medD, this::medD, this::medD, this::medD,
        this::medD, this::medD, this::medD, this::medD, this::medD, this::medD, this::medD, this::medD,
        this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::preD, this::lrgD,
        this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::smlD, this::preD, this::lrgD,
        this::udef, this::udef, this::udef, this::udef, this::udef, this::udef, this::udef, this::udef,
        this::udef, this::udef, this::udef, this::udef, this::udef, this::udef, this::udef, this::udef,
        this::lrgL, this::smlL, this::smlL, this::smlL, this::smlL, this::smlL, this::smlL, this::smlL,
        this::smlL, this::smlL, this::smlL, this::smlL, this::smlL, this::smlL, this::smlL, this::smlL,
        this::lrgM, this::smlM, this::smlM, this::smlM, this::smlM, this::smlM, this::smlM, this::smlM,
        this::smlM, this::smlM, this::smlM, this::smlM, this::smlM, this::smlM, this::smlM, this::smlM};

    @Nullable
    private ByteBuffer bb;
    private boolean neos = true;

    LZVNBlockDecoder(MatchBuffer mb) {
        super(mb);
    }

    LZVNBlockDecoder init(LZVNBlockHeader header, @WillNotClose ReadableByteChannel ch) throws IOException {
        initBuffer(header.nPayloadBytes());
        IO.readFully(ch, bb).rewind();

        l = 0;
        m = 0;
        d = -1;

        return this;
    }

    void initBuffer(int capacity) {
        if (bb == null || bb.capacity() < capacity) {
            bb = ByteBuffer.allocate(capacity).order(LITTLE_ENDIAN);
        } else {
            bb.limit(capacity);
        }
        bb.position(0);
    }

    @Override
    boolean lmd() throws IOException, LZFSEDecoderException {
        if (neos) {
            try {
                int opc = bb.get() & 0xFF;
                neos = tbl[opc].call(opc);

            } catch (BufferUnderflowException ex) {
                throw new LZFSEDecoderException(ex);
            }
        }
        return neos;
    }

    @Override
    byte literal() throws IOException {
        try {
            return bb.get();

        } catch (BufferUnderflowException ex) {
            throw new LZFSEDecoderException(ex);
        }
    }

    boolean smlL(int opc) {
        // 1110LLLL LITERAL
        l(opc & 0x0F);
        return true;
    }

    boolean lrgL(int opc) {
        // 11100000 LLLLLLLL LITERAL
        l((bb.get() & 0xFF) + 16);
        return true;
    }

    boolean smlM(int opc) {
        // 1111MMMM
        m(opc & 0xF);
        return true;
    }

    boolean lrgM(int opc) {
        // 11110000 MMMMMMMM
        m((bb.get() & 0xFF) + 16);
        return true;
    }

    boolean preD(int opc) {
        // LLMMM110
        l(opc >>> 6 & 0x03);
        m((opc >>> 3 & 0x07) + 3);
        return true;
    }

    boolean smlD(int opc) {
        // LLMMMDDD DDDDDDDD LITERAL
        l(opc >>> 6 & 0x03);
        m((opc >>> 3 & 0x07) + 3);
        d((opc & 0x07) << 8 | (bb.get() & 0xFF));
        return true;
    }

    boolean medD(int opc) {
        // 101LLMMM DDDDDDMM DDDDDDDD LITERAL
        int s = bb.getShort();
        l(opc >>> 3 & 0x03);
        m(((opc & 0x7) << 2 | (s & 0x03)) + 3);
        d(s >>> 2);
        return true;
    }

    boolean lrgD(int opc) {
        // LLMMM111 DDDDDDDD DDDDDDDD LITERAL 
        l(opc >>> 6 & 0x03);
        m((opc >>> 3 & 0x07) + 3);
        d(bb.getShort() & 0xFFFF);
        return true;
    }

    boolean eos(int opc) {
        return false;
    }

    boolean nop(int opc) {
        return true;
    }

    boolean udef(int opc) throws LZFSEDecoderException {
        throw new LZFSEDecoderException();
    }
}
