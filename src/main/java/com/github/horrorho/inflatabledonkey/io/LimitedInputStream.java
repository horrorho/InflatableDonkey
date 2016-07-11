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
package com.github.horrorho.inflatabledonkey.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import net.jcip.annotations.NotThreadSafe;

/**
 * LimitInputStream.
 *
 * @author Ahseya
 */
@NotThreadSafe
public class LimitedInputStream extends InputStream {

    private final InputStream input;
    private final long limit;
    private long index;

    LimitedInputStream(InputStream input, long limit, long index) {
        this.input = Objects.requireNonNull(input, "input");
        this.limit = limit;
        this.index = index;
    }

    public LimitedInputStream(InputStream input, long limit) {
        this(input, limit, 0);
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public synchronized void reset() throws IOException {
        throw new UnsupportedOperationException("reset() unsupported");
    }

    @Override
    public synchronized void mark(int readlimit) {
        throw new UnsupportedOperationException("mark() unsupported");
    }

    @Override
    public void close() throws IOException {
        input.close();
    }

    @Override
    public int available() throws IOException {
        if (index >= limit) {
            return - 1;
        }

        int available = input.available();
        return index + available > limit
                ? (int) (limit - index)
                : available;
    }

    @Override
    public long skip(long n) throws IOException {
        if (n < 0) {
            return 0;
        }

        if (index + n > limit) {
            n = limit - index;
        }

        index += n;
        return input.skip(n);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (index >= limit) {
            return - 1;
        }

        if (index + len > limit) {
            len = (int) (limit - index);
        }

        index += len;
        return input.read(b, off, len);
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read() throws IOException {
        if (index >= limit) {
            return -1;
        }
        index++;
        return input.read();
    }
}
