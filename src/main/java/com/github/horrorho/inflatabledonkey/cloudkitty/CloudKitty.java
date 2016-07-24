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
package com.github.horrorho.inflatabledonkey.cloudkitty;

import com.github.horrorho.inflatabledonkey.io.IOFunction;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit.*;
import com.github.horrorho.inflatabledonkey.protobuf.util.ProtobufParser;
import com.github.horrorho.inflatabledonkey.requests.ProtoBufsRequestFactory;
import com.github.horrorho.inflatabledonkey.responsehandler.DelimitedProtobufHandler;
import com.github.horrorho.inflatabledonkey.util.ListUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Super basic CloudKit client.
 *
 * @author Ahseya
 */
@Immutable
public final class CloudKitty {

    static ResponseHandler<List<ResponseOperation>> responseHandler() {
        IOFunction<InputStream, ResponseOperation> parser
                = logger.isTraceEnabled()
                        ? new ProtobufParser<>(ResponseOperation::parseFrom)
                        : ResponseOperation::parseFrom;
        return new DelimitedProtobufHandler<>(parser);
    }

    private static final Logger logger = LoggerFactory.getLogger(CloudKitty.class);
    private static final int LIMIT = 400;

    private final ResponseHandler<List<ResponseOperation>> responseHandler;
    private final Function<String, RequestOperationHeader> requestOperationHeaders;
    private final ProtoBufsRequestFactory requestFactory;
    private final int limit;

    CloudKitty(
            ResponseHandler<List<ResponseOperation>> responseHandler,
            Function<String, RequestOperationHeader> requestOperationHeaders,
            ProtoBufsRequestFactory requestFactory,
            int limit) {
        this.responseHandler = Objects.requireNonNull(responseHandler, "responseHandler");
        this.requestOperationHeaders = Objects.requireNonNull(requestOperationHeaders, "requestOperationHeaders");
        this.requestFactory = Objects.requireNonNull(requestFactory, "requestFactory");
        this.limit = limit;
    }

    CloudKitty(
            Function<String, RequestOperationHeader> requestOperationHeaders,
            ProtoBufsRequestFactory requestFactory,
            int limit) {
        this(responseHandler(), requestOperationHeaders, requestFactory, limit);
    }

    CloudKitty(
            Function<String, RequestOperationHeader> requestOperationHeaders,
            ProtoBufsRequestFactory requestFactory) {
        this(requestOperationHeaders, requestFactory, LIMIT);
    }

    public Optional<List<ResponseOperation>>
            get(HttpClient httpClient, String operation, List<RequestOperation> requests)
            throws UncheckedIOException {
        return get(httpClient, operation, requests, Function.identity());
    }

    public <T> Optional<List<T>> get(HttpClient httpClient, String operation, List<RequestOperation> requests,
            Function<ResponseOperation, T> field) throws UncheckedIOException {
        List<T> responses = doGet(httpClient, requestOperationHeaders.apply(operation), requests, field);
        if (responses.size() != requests.size()) {
            // TODO consider retry
            logger.warn("-- get() - mismatch request: {} response: {}", requests.size(), responses.size());
            return Optional.empty();
        }
        return Optional.of(responses);
    }

    <T> List<T> doGet(HttpClient httpClient, RequestOperationHeader header, List<RequestOperation> requests,
            Function<ResponseOperation, T> field) throws UncheckedIOException {
        List<T> responses = ListUtils.split(requests, LIMIT)
                .stream()
                .map(u -> request(httpClient, header, u))
                .flatMap(Collection::stream)
                .map(field)
                .collect(Collectors.toList());
        if (responses.size() != requests.size()) {
            logger.warn("-- doGet() - size mismatch requests: {} responses: {}", requests.size(), responses.size());
        }
        return responses;
    }

    List<ResponseOperation>
            request(HttpClient httpClient, RequestOperationHeader header, List<RequestOperation> requests)
            throws UncheckedIOException {
        logger.trace("<< request() - httpClient: {} header: {} requests: {}", httpClient, header, requests);

        assert (!requests.isEmpty());
        byte[] data = encode(header, requests.iterator());
        List<ResponseOperation> responses = client(httpClient, data);

        logger.trace(">> request() - responses: {}", responses);
        return responses;
    }

    byte[] encode(RequestOperationHeader header, Iterator<RequestOperation> it) throws UncheckedIOException {
        try {
            assert (it.hasNext());
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            CKProto.requestOperationWithHeader(it.next(), header).writeDelimitedTo(os);
            for (int i = 1; it.hasNext() && i < limit; i++) {
                it.next().writeDelimitedTo(os);
            }
            return os.toByteArray();

        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    List<ResponseOperation> client(HttpClient httpClient, byte[] data) {
        try {
            HttpUriRequest uriRequest = requestFactory.apply(UUID.randomUUID(), data);
            return httpClient.execute(uriRequest, responseHandler);

        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public String cloudKitUserId() {
        return requestFactory.cloudKitUserId();
    }
}
