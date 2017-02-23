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
import com.google.protobuf.Message;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import net.jcip.annotations.Immutable;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Protobuf parser with protoc --raw_decode logging.
 *
 * @author Ahseya
 * @param <T>
 */
@Immutable
public class ProtobufParser<T extends Message> implements IOFunction<InputStream, T> {

    public static <T extends Message> IOFunction<InputStream, T> of(IOFunction<InputStream, T> parse) {
        if (logger.isTraceEnabled()) {
            return ProtocRawDecoder.defaults()
                    .map(decoder -> (IOFunction<InputStream, T>) new ProtobufParser(parse, decoder))
                    .orElse(parse);
        } else {
            return parse;
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(ProtobufParser.class);

    private final ProtocRawDecoder decoder;
    private final IOFunction<InputStream, T> parse;

    public ProtobufParser(IOFunction<InputStream, T> parse, ProtocRawDecoder decoder) {
        this.parse = Objects.requireNonNull(parse);
        this.decoder = Objects.requireNonNull(decoder);
    }

    @Override
    public T apply(InputStream input) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(input, baos);

        String decode = decoder.decode(new ByteArrayInputStream(baos.toByteArray())).orElse("error");
        logger.trace("-- apply() - raw decode: {}", decode);

        ByteArrayInputStream copy = new ByteArrayInputStream(baos.toByteArray());
        return parse.apply(copy);
    }
}
