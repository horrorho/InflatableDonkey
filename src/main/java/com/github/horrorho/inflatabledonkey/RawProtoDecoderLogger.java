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
package com.github.horrorho.inflatabledonkey;

import com.github.horrorho.inflatabledonkey.io.IOFunction;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import net.jcip.annotations.Immutable;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * protoc --decode_raw logger
 *
 * @author Ahseya
 */
@Immutable
public class RawProtoDecoderLogger implements IOFunction<InputStream, List<CloudKit.ResponseOperation>> {

    private static final Logger logger = LoggerFactory.getLogger(RawProtoDecoderLogger.class);

    private final RawProtoDecoder rawProtoDecoder;

    public RawProtoDecoderLogger(RawProtoDecoder rawProtoDecoder) {
        this.rawProtoDecoder = rawProtoDecoder;
    }

    @Override
    public List<CloudKit.ResponseOperation> apply(InputStream input) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            IOUtils.copy(input, baos);

            if (rawProtoDecoder != null) {
                List<String> rawProtos = rawProtoDecoder.decodeProtos(new ByteArrayInputStream(baos.toByteArray()));
                logger.debug("-- main() - raw decode: {}", rawProtos);

            } else {
                logger.debug("-- main() - raw decode: no protoc decoder specified");
            }

            InputStream copy = new ByteArrayInputStream(baos.toByteArray());
            List<CloudKit.ResponseOperation> responseOperations = new ArrayList<>();
            CloudKit.ResponseOperation responseOperation;
            while ((responseOperation = CloudKit.ResponseOperation.parseDelimitedFrom(copy)) != null) {
                responseOperations.add(responseOperation);
            }

            return responseOperations;

        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
