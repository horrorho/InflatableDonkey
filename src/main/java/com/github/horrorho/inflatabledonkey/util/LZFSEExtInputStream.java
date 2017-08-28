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
package com.github.horrorho.inflatabledonkey.util;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ClosedChannelException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Consumer;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ayesha
 */
public class LZFSEExtInputStream extends InputStream implements Closeable {

    public static LZFSEExtInputStream create(Path cmd, InputStream is) throws IOException {
        Process process = Runtime.getRuntime().exec(cmd + " -decode");
        LZFSEExtInputStream instance = new LZFSEExtInputStream(process, null);
        Thread thread = new Thread(() -> pipe(is, process.getOutputStream(), instance::error));
        thread.start();
        return instance;
    }

    static void pipe(InputStream is, OutputStream os, Consumer<IOException> error) {
        logger.trace("<< pipe() - is: {} os: {} error: {}", is, os, error);
        try {
            IOUtils.copy(is, os);
        } catch (IOException ex) {
            error.accept(ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                error.accept(ex);
            }
            try {
                os.close();
            } catch (IOException ex) {
                error.accept(ex);
            }
            logger.trace(">> pipe()");
        }
    }

    static IOException errorMessage(Process process) {
        try (InputStream is = process.getErrorStream()) {
            String message = IOUtils.toString(is, StandardCharsets.UTF_8);
            return new IOException("lzfse: " + message);
        } catch (IOException ex) {
            return ex;
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(LZFSEExtInputStream.class);

    private final InputStream is;
    private Process process;
    private volatile IOException error;

    LZFSEExtInputStream(Process process, IOException error) {
        this.process = Objects.requireNonNull(process);
        this.is = new BufferedInputStream(process.getInputStream());
        this.error = error;
    }

    @Override
    public int read() throws IOException {
        chk();
        return is.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        chk();
        return is.read(b);
    }

    @Override
    public int read(byte b[], int off, int len) throws IOException {
        chk();
        return is.read(b, off, len);
    }

    void chk() throws IOException {
        if (process == null) {
            throw new ClosedChannelException();
        }
        if (error != null) {
            throw error;
        }
    }

    void error(IOException ex) {
        if (error == null) {
            error = ex;
        } else {
            error.addSuppressed(ex);
        }
    }

    @Override
    public void close() throws IOException {
        if (process == null) {
            return;
        }
        try {
            if (process.isAlive()) {
                process.destroyForcibly();
                logger.debug("-- close() - process destroyed");
            } else {
                int code = process.exitValue();
                if (code == 0) {
                    logger.debug("-- close() - process completed -> code: 0");
                } else {
                    error(errorMessage(process));
                }
            }
            if (error != null) {
                throw error;
            }
        } finally {
            process = null;
            is.close();
        }
    }
}
