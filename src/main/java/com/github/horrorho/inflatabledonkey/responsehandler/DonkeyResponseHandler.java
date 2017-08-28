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

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 * @param <T>
 */
public abstract class DonkeyResponseHandler<T> implements ResponseHandler<T> {

    private static final Logger logger = LoggerFactory.getLogger(DonkeyResponseHandler.class);

    // Modified org.apache.http.impl.client.AbstractResponseHandler#handleResponse 
    // Appends the entity body, if present, to the HttpResponseException message if thrown.
    // TOFIX should return Optional as nullable result.
    @Override
    public T handleResponse(final HttpResponse response) throws HttpResponseException, IOException {
        StatusLine statusLine = response.getStatusLine();
        HttpEntity entity = response.getEntity();

        if (statusLine.getStatusCode() >= 300) {
            String message = statusLine.getReasonPhrase();
            if (entity != null) {
                message += ": " + EntityUtils.toString(entity);
            }
            throw new HttpResponseException(statusLine.getStatusCode(), message);
        }

        if (entity == null) {
            return null;
        }

        long timestampSystem = System.currentTimeMillis();
        logger.debug("-- handleResponse() - timestamp system: {}", timestampSystem);
        Optional<Long> timestampOffset = timestamp(response).map(t -> t - timestampSystem);
        logger.debug("-- handleResponse() - timestamp offset: {}", timestampOffset);
        return handleEntityTimestampOffset(entity, timestampOffset);
    }

    public T handleEntityTimestampOffset(HttpEntity entity, Optional<Long> timestampOffset) throws IOException {
        return handleEntity(entity);
    }

    public abstract T handleEntity(HttpEntity entity) throws IOException;

    Optional<Long> timestamp(HttpResponse response) {
        Optional<Long> timestamp = Arrays.asList(response.getAllHeaders())
                .stream()
                .filter(u -> u.getName().equalsIgnoreCase(HttpHeaders.DATE))
                .map(Header::getValue)
                .map(DateUtils::parseDate)
                .filter(Objects::nonNull)
                .map(u -> u.toInstant().toEpochMilli())
                .findFirst();
        logger.debug("-- timestamp() - timestamp: {}", timestamp);
        return timestamp;
    }
}
