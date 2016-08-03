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

import com.github.horrorho.inflatabledonkey.exception.UncheckedInterruptedException;
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
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;
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
    private final ForkJoinPool forkJoinPool;
    private final int limit;

    CloudKitty(
            ResponseHandler<List<ResponseOperation>> responseHandler,
            Function<String, RequestOperationHeader> requestOperationHeaders,
            ProtoBufsRequestFactory requestFactory,
            ForkJoinPool forkJoinPool,
            int limit) {
        this.responseHandler = Objects.requireNonNull(responseHandler);
        this.requestOperationHeaders = Objects.requireNonNull(requestOperationHeaders);
        this.requestFactory = Objects.requireNonNull(requestFactory);
        this.forkJoinPool = Objects.requireNonNull(forkJoinPool);
        this.limit = limit;
    }

    CloudKitty(
            Function<String, RequestOperationHeader> requestOperationHeaders,
            ProtoBufsRequestFactory requestFactory,
            ForkJoinPool forkJoinPool,
            int limit) {
        this(responseHandler(), requestOperationHeaders, requestFactory, forkJoinPool, limit);
    }

    CloudKitty(
            Function<String, RequestOperationHeader> requestOperationHeaders,
            ProtoBufsRequestFactory requestFactory,
            ForkJoinPool forkJoinPool) {
        this(requestOperationHeaders, requestFactory, forkJoinPool, LIMIT);
    }

    public List<ResponseOperation> get(HttpClient httpClient, String operation, List<RequestOperation> requests)
            throws IOException {

        return get(httpClient, operation, requests, Function.identity());
    }

    public <T> List<T> get(HttpClient httpClient, String operation, List<RequestOperation> requests,
            Function<ResponseOperation, T> field) throws IOException {

        return execute(httpClient, requestOperationHeaders.apply(operation), requests, field);
    }

    <T> List<T> execute(HttpClient httpClient, RequestOperationHeader header, List<RequestOperation> requests,
            Function<ResponseOperation, T> field) throws IOException {
        try {
            logger.debug("-- execute() - requests: {}", requests.size());
            List<List<RequestOperation>> split = ListUtils.split(requests, limit);
            logger.debug("-- execute() - split: {}", split.size());

            List<SimpleImmutableEntry<Integer, List<RequestOperation>>> list = IntStream.range(0, split.size())
                    .mapToObj(i -> new SimpleImmutableEntry<>(i, split.get(i)))
                    .collect(toList());

            List<SimpleImmutableEntry<Integer, List<ResponseOperation>>> get
                    = forkJoinPool.submit(() -> list.parallelStream()
                            .map(u -> new SimpleImmutableEntry<>(u.getKey(), request(httpClient, header, u.getValue())))
                            .collect(toList()))
                    .get();

            // Order responses to match requests.
            List<T> reordered = get.stream()
                    .sorted(Comparator.comparing(Map.Entry::getKey))
                    .map(Map.Entry::getValue)
                    .flatMap(Collection::stream)
                    .map(field)
                    .collect(Collectors.toList());

            if (reordered.size() != requests.size()) {
                logger.warn("-- execute() - requests: {} reordered: {}", requests.size(), reordered.size());
                throw new IOException("CloudKitty execute, bad response");
            }
            return reordered;

        } catch (UncheckedIOException ex) {
            throw ex.getCause();
        } catch (InterruptedException ex) {
            throw new UncheckedInterruptedException(ex);
        } catch (ExecutionException ex) {
            throw new RuntimeException(ex);
        }
    }

    List<ResponseOperation>
            request(HttpClient httpClient, RequestOperationHeader header, List<RequestOperation> requests)
            throws UncheckedIOException {
        logger.trace("<< request() - httpClient: {} header: {} requests: {}", httpClient, header, requests);
        logger.debug("-- request() - requests: {}", requests.size());

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
