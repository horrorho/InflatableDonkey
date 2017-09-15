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
package com.github.horrorho.inflatabledonkey.responsehandler;

import com.github.horrorho.inflatabledonkey.io.IOFunction;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.Message;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.concurrent.Immutable;
import org.apache.http.HttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Delimited Protobuf response handler.
 *
 * @author Ahseya
 * @param <T>
 */
@Immutable
public class DelimitedProtobufHandler<T extends Message> extends DonkeyResponseHandler<List<T>> {

    private static final Logger logger = LoggerFactory.getLogger(DelimitedProtobufHandler.class);

    private final IOFunction<InputStream, T> parse;

    public DelimitedProtobufHandler(IOFunction<InputStream, T> parse) {
        this.parse = Objects.requireNonNull(parse, "parse");
    }

    @Override
    public List<T> handleEntity(HttpEntity entity) throws IOException {
        try (InputStream in = entity.getContent()) {
            return apply(in);
        }
    }

    List<T> apply(InputStream in) throws IOException {
        List<T> list = new ArrayList<>();
        CodedInputStream stream = CodedInputStream.newInstance(in);
        int size;
        while (!stream.isAtEnd() && (size = stream.readRawVarint32()) != 0) {
            ByteArrayInputStream delimited = new ByteArrayInputStream(stream.readRawBytes(size));
            list.add(parse.apply(delimited));
        }
        return list;
    }
}
