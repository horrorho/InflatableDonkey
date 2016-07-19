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
package com.github.horrorho.inflatabledonkey.protobuf.util;

import com.github.horrorho.inflatabledonkey.io.IOFunction;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.Message;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Delimited Protobuf parser with protoc --raw_decode logging.
 *
 * @author Ahseya
 * @param <T>
 */
@Immutable
public class DelimitedProtobufParser<T extends Message> implements IOFunction<InputStream, List<T>> {

    private final ProtobufParser<T> handler;

    public DelimitedProtobufParser(ProtobufParser<T> handler) {
        this.handler = Objects.requireNonNull(handler, "handler");
    }

    public DelimitedProtobufParser(IOFunction<InputStream, T> parse) {
        this(new ProtobufParser(parse));
    }

    @Override
    public List<T> apply(InputStream input) throws IOException {
        List<T> list = new ArrayList<>();
        CodedInputStream stream = CodedInputStream.newInstance(input);
        int size;
        while (!stream.isAtEnd() && (size = stream.readRawVarint32()) != 0) {
            ByteArrayInputStream delimited = new ByteArrayInputStream(stream.readRawBytes(size));
            list.add(handler.apply(delimited));
        }
        return list;
    }
}
