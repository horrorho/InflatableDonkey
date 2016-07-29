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
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import net.jcip.annotations.NotThreadSafe;

/**
 *
 * @author Ahseya
 */
@NotThreadSafe
public class IOSupplierSequenceStream extends InputStream {

    private final Iterator<IOSupplier<InputStream>> it;
    private InputStream is = null;

    public IOSupplierSequenceStream(Iterator<IOSupplier<InputStream>> streams) throws IOException {
        this.it = Objects.requireNonNull(streams);
        next();
    }

    public IOSupplierSequenceStream(Collection<IOSupplier<InputStream>> streams) throws IOException {
        this(streams.iterator());
    }

    final void next() throws IOException {
        if (is != null) {
            is.close();
        }
        if (it.hasNext()) {
            is = it.next().get();
            Objects.requireNonNull(is);
        } else {
            is = null;
        }
    }

    @Override
    public void close() throws IOException {
        if (is != null) {
            is.close();
        }
    }

    @Override
    public int available() throws IOException {
        return is == null ? 0 : is.available();
    }

    @Override
    public int read() throws IOException {
        while (is != null) {
            int c = is.read();
            if (c != -1) {
                return c;
            }
            next();
        }
        return -1;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        Objects.requireNonNull(b);
        if (is == null) {
            return -1;
        }
        if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return 0;
        }
        do {
            int n = is.read(b, off, len);
            if (n > 0) {
                return n;
            }
            next();
        } while (is != null);
        return -1;
    }
}
