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
package com.github.horrorho.inflatabledonkey.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pipes data in/ out from the specified command process.
 *
 * @author Ahseya
 */
@Immutable
public final class ProcessManager implements BiFunction<String, InputStream, Optional<byte[]>> {

    private static final Logger logger = LoggerFactory.getLogger(ProcessManager.class);

    private final long timeoutMS;

    public ProcessManager(long timeoutMS) {
        this.timeoutMS = timeoutMS;
    }

    @Override
    public Optional<byte[]> apply(String command, InputStream in) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            return pipe(process, in);

        } catch (IOException ex) {
            logger.warn("-- apply() - IOException: {}", ex.getMessage());

        } catch (InterruptedException ex) {
            logger.warn("-- apply() - InterruptedException: {}", ex.getMessage());
            Thread.currentThread().interrupt();

        } finally {
            IOUtils.closeQuietly(in);
        }
        return Optional.empty();
    }

    Optional<byte[]> pipe(Process process, InputStream in) throws IOException, InterruptedException {
        try (InputStream decoderIn = process.getInputStream();
                OutputStream decoderOut = process.getOutputStream()) {

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Thread copy = new Thread(() -> copy(in, decoderOut));
            Thread read = new Thread(() -> copy(decoderIn, out));
            copy.start();
            read.start();

            if (!process.waitFor(timeoutMS, TimeUnit.MILLISECONDS)) {
                logger.warn("-- pipe() - timed out");
                // copy threads will shortly exit on exceptions as their streams are closed.
                return Optional.empty();
            }
            read.join(timeoutMS);

            return Optional.of(out.toByteArray());

        } finally {
            if (logger.isWarnEnabled()) {
                error(process);
            }
        }
    }

    void error(Process process) throws IOException {
        try (BufferedReader br
                = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))) {

            String error = br.lines().collect(Collectors.joining("\n"));
            if (!error.isEmpty()) {
                logger.warn("-- error() - error: {}", error);
            }
        }
    }

    void copy(InputStream in, OutputStream out) {
        try {
            IOUtils.copy(in, out);

        } catch (Throwable ex) {
            logger.warn("-- copy() - Throwable: {}", ex.getMessage());

        } finally {
            IOUtils.closeQuietly(out);
        }
    }
}
