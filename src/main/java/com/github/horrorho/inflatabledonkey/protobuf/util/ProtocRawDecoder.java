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
package com.github.horrorho.inflatabledonkey.protobuf.util;

import com.github.horrorho.inflatabledonkey.args.Property;
import com.google.protobuf.CodedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Protoc --decode_raw wrapper.
 *
 * @author Ahseya
 */
@Immutable
public final class ProtocRawDecoder {

    public static final Optional<ProtocRawDecoder> defaults() {
        return DEFAULTS;
    }
    private static final String PATH = Property.PROTOC_PATH.value().orElse(null);
    private static final long TIMEOUT = Property.PROTOC_TIMEOUT_MS.asLong().orElse(150000L);

    private static final Optional<ProtocRawDecoder> DEFAULTS
            = PATH != null
                    ? Optional.of(new ProtocRawDecoder())
                    : Optional.empty();

    private static final Logger logger = LoggerFactory.getLogger(ProtocRawDecoder.class);

    private final String protocPath;
    private final long timeoutMS;

    public ProtocRawDecoder(String protocPath, long timeoutMS) {
        this.protocPath = Objects.requireNonNull(protocPath, "protocPath");
        this.timeoutMS = timeoutMS;
    }

    public ProtocRawDecoder(String protocPath) {
        this(protocPath, TIMEOUT);
    }

    ProtocRawDecoder() {
        this(PATH);
    }

    public List<String> decodeDelimited(InputStream in) {
        List<String> list = new ArrayList<>();
        try {
            CodedInputStream stream = CodedInputStream.newInstance(in);
            int size;
            while (!stream.isAtEnd() && (size = stream.readRawVarint32()) != 0) {
                if (Thread.currentThread().isInterrupted()) {
                    logger.warn("-- decodeProtos() - interrupted");
                    break;
                }
                ByteArrayInputStream delimited = new ByteArrayInputStream(stream.readRawBytes(size));
                decode(delimited).map(list::add);
            }
        } catch (IOException ex) {
            logger.warn("-- decodeProtos() - IOException: {}", ex.getMessage());

        } finally {
            close(in);
        }
        return list;
    }

    public Optional<String> decode(InputStream in) {
        try {
            Process protoc = Runtime.getRuntime().exec(protocPath + " --decode_raw");
            return decode(in, protoc);

        } catch (IOException ex) {
            logger.warn("-- decodeProto() - IOException: {}", ex.getMessage());

        } catch (InterruptedException ex) {
            logger.warn("-- decodeProto() - InterruptedException: {}", ex.getMessage());
            Thread.currentThread().interrupt();

        } finally {
            close(in);
        }
        return Optional.empty();
    }

    Optional<String> decode(InputStream in, Process protoc) throws IOException, InterruptedException {
        try (InputStream decoderIn = protoc.getInputStream();
                OutputStream decoderOut = protoc.getOutputStream()) {

            AtomicReference<String> decoded = new AtomicReference<>(new String());

            Thread copy = new Thread(() -> copy(in, decoderOut));
            Thread read = new Thread(() -> read(decoderIn, decoded));
            copy.start();
            read.start();

            if (!protoc.waitFor(timeoutMS, TimeUnit.MILLISECONDS)) {
                logger.warn("--decode() - timed out");
                // read/ copy threads will exit on exceptions as their streams are closed.
                return Optional.empty();
            }
            read.join(timeoutMS);

            return Optional.of(decoded.get());

        } finally {
            if (logger.isWarnEnabled()) {
                try (BufferedReader br
                        = new BufferedReader(new InputStreamReader(protoc.getErrorStream(), StandardCharsets.UTF_8))) {

                    String error = br.lines().collect(Collectors.joining("\n"));
                    if (!error.isEmpty()) {
                        logger.warn("-- decode() - error: {}", error);
                    }
                }
            }
        }
    }

    void copy(InputStream in, OutputStream out) {
        try {
            IOUtils.copy(in, out);

        } catch (Throwable ex) {
            logger.warn("-- copy() - Throwable: {}", ex.getMessage());
        } finally {
            // close protoc's output stream at which point protoc will shortly terminate
            close(out);
        }
    }

    void read(InputStream in, AtomicReference<String> out) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            br.lines().forEach(pw::println);
            out.set(sw.toString());

        } catch (Throwable ex) {
            logger.warn("-- read() - Throwable: {}", ex.getMessage());
        }
    }

    void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException ex) {
            logger.warn("-- close() - IOException: {}", ex.getMessage());
        }
    }
}
