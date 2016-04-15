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
package com.github.horrorho.inflatabledonkey.pcs.xfile;

import com.github.horrorho.inflatabledonkey.args.Property;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import net.jcip.annotations.NotThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FileDecrypter.
 *
 * @author Ahseya
 */
@NotThreadSafe
public class FileDecrypterInputStream extends InputStream {

    private static final Logger logger = LoggerFactory.getLogger(FileDecrypterInputStream.class);

    private static final int BLOCK_LENGTH = Property.DECRYPTION_BLOCK_LENGTH.intValue().orElse(0x1000);

    private final InputStream input;
    private final BlockDecrypter decrypter;
    private final byte[] in;
    private final byte[] out;
    private int pos = 0;
    private int limit = 0;
    private int block = 0;

    public FileDecrypterInputStream(
            InputStream input,
            BlockDecrypter blockDecrypter,
            int blockLength) {

        this.input = Objects.requireNonNull(input, "input");
        this.decrypter = Objects.requireNonNull(blockDecrypter, "blockDecrypter");
        this.in = new byte[blockLength];
        this.out = new byte[blockLength];
    }

    public FileDecrypterInputStream(InputStream input, BlockDecrypter blockDecrypter) {
        this(input, blockDecrypter, BLOCK_LENGTH);
    }

    @Override
    public int read() throws IOException {
        byte[] b = new byte[1];
        read(b);
        return b[0];
    }

    @Override
    public int read(byte b[], int off, int len) throws IOException {
        int read = 0;
        while (len > 0) {
            if (pos >= limit) {
                fill();
            }
            if (limit == -1) {
                return -1;
            }

            int remaining = limit - pos;
            int write = len > remaining ? remaining : len;

            System.arraycopy(out, pos, b, off + read, write);

            len -= write;
            read += write;
        }
        return read;
    }

    void fill() throws IOException {
        limit = readBuffer();
        if (limit == -1) {
            return;
        }

        pos = 0;
        int processed = decrypter.decrypt(block, in, 0, limit, out, pos);
        if (processed != limit) {
            logger.warn("-- fill() - non block-length aligned input");
        }

        block++;
    }

    int readBuffer() throws IOException {
        if (limit == -1) {
            return -1;
        }
        // Read complete blocks where possible, as read on streams may return partial data which leads to incomplete 
        // block filling and broken decryption.
        int offset = 0;
        do {
            int read = input.read(in, offset, in.length - offset);
            if (read == -1) {
                return offset == 0 ? -1 : offset;
            }
            offset += read;
        } while (offset < in.length);
        return offset;
    }

    @Override
    public void close() throws IOException {
        input.close();
    }
}
// TODO test
