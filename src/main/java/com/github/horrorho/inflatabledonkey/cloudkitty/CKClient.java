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
import com.github.horrorho.inflatabledonkey.responsehandler.DelimitedProtobufHandler;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CloudKitty client.
 *
 * @author Ahseya
 */
@Immutable
public final class CKClient {

    static ResponseHandler<List<ResponseOperation>> responseHandler() {
        IOFunction<InputStream, ResponseOperation> parser
                = logger.isTraceEnabled()
                        ? new ProtobufParser<>(ResponseOperation::parseFrom)
                        : ResponseOperation::parseFrom;
        return new DelimitedProtobufHandler<>(parser);
    }

    private static final Logger logger = LoggerFactory.getLogger(CKClient.class);
    private static final int LIMIT = 400;

    private final ResponseHandler<List<ResponseOperation>> responseHandler;
    private final Function<String, RequestOperationHeader> requestOperationHeaders;
    private final BiFunction<UUID, byte[], HttpUriRequest> requestFactory;
    private final int limit;

    CKClient(ResponseHandler<List<ResponseOperation>> responseHandler,
            Function<String, RequestOperationHeader> requestOperationHeaders,
            BiFunction<UUID, byte[], HttpUriRequest> requestFactory,
            int limit) {

        this.responseHandler = Objects.requireNonNull(responseHandler, "responseHandler");
        this.requestOperationHeaders = Objects.requireNonNull(requestOperationHeaders, "requestOperationHeaders");
        this.requestFactory = Objects.requireNonNull(requestFactory, "requestFactory");
        this.limit = limit;
    }

    CKClient(Function<String, RequestOperationHeader> requestOperationHeaders,
            BiFunction<UUID, byte[], HttpUriRequest> requestFactory,
            int limit) {
        this(responseHandler(), requestOperationHeaders, requestFactory, limit);
    }

    CKClient(Function<String, RequestOperationHeader> requestOperationHeaders,
            BiFunction<UUID, byte[], HttpUriRequest> requestFactory) {
        this(requestOperationHeaders, requestFactory, LIMIT);
    }

    public List<ResponseOperation>
            get(HttpClient httpClient, String operation, List<RequestOperation> requests)
            throws UncheckedIOException {
        return get(httpClient, operation, requests, Function.identity());
    }

    public <T> List<T> get(HttpClient httpClient, String operation, List<RequestOperation> requests,
            Function<ResponseOperation, T> field) throws UncheckedIOException {
        return doGet(httpClient, requestOperationHeaders.apply(operation), requests, field);
    }

    <T> List<T> doGet(HttpClient httpClient, RequestOperationHeader header, List<RequestOperation> requests,
            Function<ResponseOperation, T> field) throws UncheckedIOException {
        return request(httpClient, header, requests)
                .stream()
                .flatMap(Collection::stream)
                .map(field)
                .collect(Collectors.toList());
    }

    List<List<ResponseOperation>>
            request(HttpClient httpClient, RequestOperationHeader header, List<RequestOperation> requests)
            throws UncheckedIOException {
        logger.trace("<< request() - httpClient: {} header: {} requests: {}", httpClient, header, requests);
        List<List<ResponseOperation>> responses = new ArrayList<>();
        for (Iterator<RequestOperation> it = requests.iterator(); it.hasNext();) {
            byte[] data = encodeToLimit(header, it);
            List<ResponseOperation> response = clientRequest(httpClient, data);
            responses.add(response);
        }
        logger.trace(">> request() - responses: {}", responses);
        return responses;
    }

    byte[] encodeToLimit(RequestOperationHeader header, Iterator<RequestOperation> it) throws UncheckedIOException {
        try {
            if (!it.hasNext()) {
                throw new IllegalStateException("empty iterator");
            }
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

    List<ResponseOperation> clientRequest(HttpClient httpClient, byte[] data) {
        try {
            HttpUriRequest uriRequest = requestFactory.apply(UUID.randomUUID(), data);
            return httpClient.execute(uriRequest, responseHandler);

        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
