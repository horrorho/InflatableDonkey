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
package com.github.horrorho.inflatabledonkey;

import com.google.protobuf.CodedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
public final class RawProtoDecoder {

    private static final Logger logger = LoggerFactory.getLogger(RawProtoDecoder.class);

    private final String protocPath;

    public RawProtoDecoder(String protocPath) {
        this.protocPath = protocPath;
    }

    public RawProtoDecoder() {
        this("protoc");
    }

    public String decodeProto(InputStream protoBytes) throws IOException, InterruptedException {
        try {
            Process exec;

            try {
                exec = Runtime.getRuntime().exec(protocPath + " --decode_raw");

            } catch (IOException ex) {
                logger.warn("-- decodeProto() - protoc exec error: {}", ex.getMessage());
                return "error";
            }

            try (OutputStream execOut = exec.getOutputStream()) {
                IOUtils.copy(protoBytes, execOut);
            }

            boolean completed = exec.waitFor(15, TimeUnit.SECONDS);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(exec.getInputStream()))) {
                StringWriter decoded = new StringWriter();
                PrintWriter print = new PrintWriter(decoded);

                br.lines().forEach(print::println);

                return decoded.toString() + (completed ? "" : "\nIncomplete\n");
            }

        } finally {
            protoBytes.close();
        }
    }

    public List<String> decodeProtos(InputStream protoArrayBytes) throws IOException, InterruptedException {
        List<byte[]> split = split(protoArrayBytes);

        logger.debug("-- decodeProtos() - split: {}", split);

        List<String> decoded = new ArrayList<>();
        for (byte[] protoBytes : split) {
            logger.debug("-- decodeProtos() - protoBytes: {}", protoBytes.length);

            decoded.add(decodeProto(new ByteArrayInputStream(protoBytes)));
        }
        return decoded;
    }

    public List<byte[]> split(InputStream arrayBytes) throws IOException {
        try {
            CodedInputStream stream = CodedInputStream.newInstance(arrayBytes);
            List<byte[]> list = new ArrayList<>();

            while (!stream.isAtEnd()) {
                int size = stream.readRawVarint32();
                if (size == 0) {
                    break;
                }

                byte[] element = stream.readRawBytes(size);
                list.add(element);
            }
            return list;

        } finally {
            arrayBytes.close();
        }
    }
}
