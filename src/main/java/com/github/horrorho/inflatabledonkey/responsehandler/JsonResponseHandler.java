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
package com.github.horrorho.inflatabledonkey.responsehandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.horrorho.inflatabledonkey.exception.BadDataException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import javax.annotation.concurrent.Immutable;
import org.apache.http.HttpEntity;

/**
 * Json ResponseHandler.
 *
 * @author Ahseya
 * @param <T> type
 */
@Immutable
public final class JsonResponseHandler<T> extends DonkeyResponseHandler<T> {

    // ObjectMapper thread safe if we don't modify its configuration.
    // http://stackoverflow.com/questions/3907929/should-i-declare-jacksons-objectmapper-as-a-static-field
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final ObjectMapper objectMapper;
    private final Class<T> to;

    JsonResponseHandler(ObjectMapper objectMapper, Class<T> to) {
        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.to = Objects.requireNonNull(to);
    }

    public JsonResponseHandler(Class<T> to) {
        this(OBJECT_MAPPER, to);
    }

    @Override
    public T handleEntity(HttpEntity entity) throws IOException {
        try (InputStream inputStream = entity.getContent()) {
            return objectMapper.readValue(inputStream, to);

        } catch (JsonProcessingException ex) {
            throw new BadDataException("failed to parse json", ex);
        }
    }
}
