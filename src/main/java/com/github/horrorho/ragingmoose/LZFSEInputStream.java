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
import java.io.InputStream;
import static java.lang.Integer.toHexString;
import java.nio.ByteBuffer;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

/**
 *
 * @author Ayesha
 */
@NotThreadSafe
@ParametersAreNonnullByDefault
public class LZFSEInputStream extends InputStream implements LZFSEConstants {

    private final ByteBuffer word = ByteBuffer.allocate(4).order(LITTLE_ENDIAN);
    private final ReadableByteChannel ch;

    private boolean eos = false;

    @Nullable
    private MatchBuffer mb;
    @Nullable
    private LZFSEBlockHeader lzfseBlockHeader;
    @Nullable
    private LZVNBlockHeader lzvnBlockHeader;
    @Nullable
    private RawBlockHeader rawBlockHeader;
    @Nullable
    private LZFSEBlockDecoder lzfseBlockDecoder;
    @Nullable
    private LZVNBlockDecoder lzvnBlockDecoder;
    @Nullable
    private RawBlockDecoder rawBlockDecoder;

    @Nullable
    private BlockDecoder decoder;

    public LZFSEInputStream(InputStream is) {
        this(Channels.newChannel(is));
    }

    public LZFSEInputStream(ReadableByteChannel ch) {
        this.ch = Objects.requireNonNull(ch);
    }

    @Override
    public int available() {
        return eos ? 0 : 1;
    }

    @Override
    public int read() throws IOException {
        try {
            while (!eos) {
                if (decoder == null) {
                    next();
                } else {
                    int b = decoder.read();
                    if (b == -1) {
                        decoder = null;
                    } else {
                        return b;
                    }
                }
            }
            return -1;
            
        } catch (RuntimeException ex) {
            throw new LZFSEDecoderException("internal error", ex);
        }
    }

    @Override
    public int read(byte b[], int off, int len) throws IOException {
        if (off < 0 || len < 0 || len + off > b.length) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return 0;
        }
        try {
            while (!eos) {
                if (decoder == null) {
                    next();
                } else {
                    int n = decoder.read(b, off, len);
                    if (n == 0) {
                        decoder = null;
                    }
                    return n;
                }
            }
            return -1;
            
        } catch (RuntimeException ex) {
            throw new LZFSEDecoderException("internal error", ex);
        }
    }

    void next() throws IOException {
        int magic = magic();
        switch (magic) {
            case COMPRESSEDV2_BLOCK_MAGIC:
                v2Block();
                break;
            case COMPRESSEDV1_BLOCK_MAGIC:
                v1Block();
                break;
            case COMPRESSEDLZVN_BLOCK_MAGIC:
                vnBlock();
                break;
            case UNCOMPRESSED_BLOCK_MAGIC:
                raw();
                break;
            case ENDOFSTREAM_BLOCK_MAGIC:
                eosBlock();
                break;
            default:
                throw new LZFSEDecoderException("bad block: 0x" + toHexString(magic));
        }
    }

    void v1Block() throws IOException, LZFSEDecoderException {
        lzfseBlockHeader()
                .loadV1(ch);
        decoder = lzfseBlockDecoder()
                .init(lzfseBlockHeader, ch);
    }

    void v2Block() throws IOException, LZFSEDecoderException {
        lzfseBlockHeader()
                .loadV2(ch);
        decoder = lzfseBlockDecoder()
                .init(lzfseBlockHeader, ch);
    }

    void vnBlock() throws IOException {
        lzvnBlockHeader()
                .load(ch);
        decoder = lzvnBlockDecoder()
                .init(lzvnBlockHeader, ch);
    }

    void raw() throws IOException {
        rawBlockHeader()
                .load(ch);
        decoder = rawBlockDecoder()
                .init(rawBlockHeader, ch);
    }

    void eosBlock() {
        eos = true;
        decoder = null;
    }

    @Nonnull
    LZFSEBlockHeader lzfseBlockHeader() {
        if (lzfseBlockHeader == null) {
            lzfseBlockHeader = new LZFSEBlockHeader();
        }
        return lzfseBlockHeader;
    }

    @Nonnull
    LZFSEBlockDecoder lzfseBlockDecoder() throws LZFSEDecoderException {
        if (lzfseBlockDecoder == null) {
            lzfseBlockDecoder = new LZFSEBlockDecoder(matchBuffer());
        }
        return lzfseBlockDecoder;
    }

    @Nonnull
    LZVNBlockHeader lzvnBlockHeader() {
        if (lzvnBlockHeader == null) {
            lzvnBlockHeader = new LZVNBlockHeader();
        }
        return lzvnBlockHeader;
    }

    @Nonnull
    LZVNBlockDecoder lzvnBlockDecoder() {
        if (lzvnBlockDecoder == null) {
            lzvnBlockDecoder = new LZVNBlockDecoder(matchBuffer());
        }
        return lzvnBlockDecoder;
    }

    @Nonnull
    RawBlockHeader rawBlockHeader() {
        if (rawBlockHeader == null) {
            rawBlockHeader = new RawBlockHeader();
        }
        return rawBlockHeader;
    }

    @Nonnull
    RawBlockDecoder rawBlockDecoder() {
        if (rawBlockDecoder == null) {
            rawBlockDecoder = new RawBlockDecoder();
        }
        return rawBlockDecoder;
    }

    @Nonnull
    MatchBuffer matchBuffer() {
        if (mb == null) {
            mb = new MatchBuffer(MATCH_BUFFER_SIZE);
        }
        return mb;
    }

    int magic() throws IOException {
        word.rewind();
        IO.readFully(ch, word).rewind();
        return word.getInt();
    }
}
