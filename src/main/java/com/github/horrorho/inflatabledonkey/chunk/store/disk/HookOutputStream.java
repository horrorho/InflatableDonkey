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
package com.github.horrorho.inflatabledonkey.chunk.store.disk;

import com.github.horrorho.inflatabledonkey.io.IOConsumer;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import net.jcip.annotations.NotThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OutputStream wrapper with a callback hook on successful close.
 *
 * @author Ahseya
 * @param <T> wrapped type
 */
@NotThreadSafe
public class HookOutputStream<T extends OutputStream> extends OutputStream {

    private static final Logger logger = LoggerFactory.getLogger(HookOutputStream.class);

    private final T outputStream;
    private final IOConsumer<T> callback;

    public HookOutputStream(T outputStream, IOConsumer<T> callback) {
        this.outputStream = Objects.requireNonNull(outputStream);
        this.callback = Objects.requireNonNull(callback);
    }

    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
    }

    @Override
    public void flush() throws IOException {
        outputStream.flush();
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        outputStream.write(b, off, len);
    }

    @Override
    public void write(byte[] b) throws IOException {
        outputStream.write(b);
    }

    @Override
    public void close() throws IOException {
        try {
            outputStream.close();
            callback.accept(outputStream);

        } catch (IOException ex) {
            logger.warn("-- close() - {} {}", ex.getClass().getCanonicalName(), ex.getMessage());
            throw ex;
        }
    }
}
