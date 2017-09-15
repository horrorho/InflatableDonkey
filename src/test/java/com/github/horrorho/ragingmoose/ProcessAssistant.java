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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ClosedChannelException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.WillClose;
import javax.annotation.WillNotClose;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

/**
 *
 * @author Ayesha
 */
@Immutable
@ParametersAreNonnullByDefault
public class ProcessAssistant {

    @NotThreadSafe
    static class ProcessInputStream extends InputStream {

        static void pipe(@WillNotClose InputStream is, @WillClose OutputStream os, byte[] buffer, Consumer<IOException> error) {
            try {
                ProcessAssistant.copy(is, os, buffer);
            } catch (IOException ex) {
                error.accept(ex);
            } finally {
                try {
                    os.close();
                } catch (IOException ex) {
                    error.accept(ex);
                }
            }
        }

        @Nonnull
        static String string(@WillNotClose InputStream is, byte[] buffer) throws IOException {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ProcessAssistant.copy(is, baos, buffer);
                return baos.toString("UTF-8");
            }
        }

        @Nonnull
        static IOException processError(Process process) {
            try (InputStream is = process.getErrorStream()) {
                return new IOException(string(is, new byte[256]));

            } catch (IOException ex) {
                return ex;
            }
        }

        private final InputStream is;
        @Nullable
        private Process process;
        @Nullable
        private volatile IOException error;

        private ProcessInputStream(Process process) {
            this.process = process;
            this.is = process.getInputStream();
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

        @Override
        public int available() throws IOException {
            chk();
            return is.available();
        }

        void chk() throws IOException {
            if (process == null) {
                throw new ClosedChannelException();
            }
            if (error != null) {
                IOException _error = error;
                error = null;
                throw _error;
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
                } else {
                    if (process.exitValue() != 0) {
                        error(processError(process));
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

    @Nonnull
    public static InputStream newPipedInputStream(ProcessBuilder pb, @WillNotClose InputStream is) throws IOException {
        Process p = pb.start();
        ProcessInputStream pis = new ProcessInputStream(p);
        new Thread(() -> ProcessInputStream.pipe(is, p.getOutputStream(), new byte[16384], pis::error))
                .start();
        return pis;
    }

    @Nonnull
    public static InputStream newInputStream(ProcessBuilder pb) throws IOException {
        Process p = pb.start();
        ProcessInputStream pis = new ProcessInputStream(p);
        return pis;
    }

    public static long copy(@WillNotClose InputStream is, @WillNotClose OutputStream os, byte[] buffer) throws IOException {
        long n = 0;
        int read;
        while ((read = is.read(buffer)) != -1) {
            os.write(buffer, 0, read);
            n += read;
        }
        return n;
    }

    @Nonnull
    public static Optional<String> firstInPath(String... cmd) {
        return Arrays.stream(cmd)
                .filter(ProcessAssistant::isInPath)
                .findFirst();
    }

    @Nonnull
    public static boolean isInPath(String cmd) {
        return Pattern.compile(Pattern.quote(File.pathSeparator))
                .splitAsStream(System.getenv("PATH"))
                .map(u -> Paths.get(u, cmd))
                .anyMatch(Files::exists);
    }

    private ProcessAssistant() {
    }
}
