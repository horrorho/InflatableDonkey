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
import com.github.horrorho.inflatabledonkey.util.ProcessManager;
import com.google.protobuf.CodedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    private static final Optional<String> PATH = Property.PROTOC_PATH.value();
    private static final long TIMEOUT = Property.PROTOC_TIMEOUT_MS.asLong().orElse(150000L);

    private static final Optional<ProtocRawDecoder> DEFAULTS
            = PATH.map(u -> new ProtocRawDecoder(u));

    private static final Logger logger = LoggerFactory.getLogger(ProtocRawDecoder.class);

    private final String protocPath;
    private final ProcessManager processManager;

    public ProtocRawDecoder(String protocPath, ProcessManager processManager) {
        this.protocPath = Objects.requireNonNull(protocPath, "protocPath");
        this.processManager = Objects.requireNonNull(processManager, "processManager");
    }

    public ProtocRawDecoder(String protocPath, long timeout) {
        this(protocPath, new ProcessManager(timeout));
    }

    public ProtocRawDecoder(String protocPath) {
        this(protocPath, TIMEOUT);
    }

    public List<String> decodeDelimited(InputStream in) {
        List<String> list = new ArrayList<>();
        try {
            CodedInputStream stream = CodedInputStream.newInstance(in);
            int size;
            while (!stream.isAtEnd() && (size = stream.readRawVarint32()) != 0) {
                if (Thread.currentThread().isInterrupted()) {
                    logger.warn("-- decodeDelimited() - interrupted");
                    break;
                }
                ByteArrayInputStream delimited = new ByteArrayInputStream(stream.readRawBytes(size));
                decode(delimited).map(list::add);
            }
        } catch (IOException ex) {
            logger.warn("-- decodeDelimited() - IOException: {}", ex.getMessage());

        } finally {
            IOUtils.closeQuietly(in);
        }
        return list;
    }

    public Optional<String> decode(InputStream in) {
        String command = protocPath + " --decode_raw";
        return processManager.apply(command, in)
                .map(String::new);
    }
}
