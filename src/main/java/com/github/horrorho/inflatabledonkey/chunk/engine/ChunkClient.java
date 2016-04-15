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
package com.github.horrorho.inflatabledonkey.chunk.engine;

import com.github.horrorho.inflatabledonkey.protocol.ChunkServer;
import com.github.horrorho.inflatabledonkey.requests.ChunkListRequestFactory;
import com.github.horrorho.inflatabledonkey.responsehandler.ByteArrayResponseHandler;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ChunkListsClient.
 *
 * @author Ahseya
 */
@Immutable
public final class ChunkClient {

    private static final Logger logger = LoggerFactory.getLogger(ChunkClient.class);

    private ChunkClient() {
    }

    public static Optional<byte[]> fetch(HttpClient httpClient, ChunkServer.StorageHostChunkList chunkList) {
        ChunkListRequestFactory chunkListRequestFactory = ChunkListRequestFactory.instance();
        return fetch(httpClient, chunkList, chunkListRequestFactory);
    }

    public static Optional<byte[]> fetch(
            HttpClient httpClient,
            ChunkServer.StorageHostChunkList chunkList,
            Function<ChunkServer.StorageHostChunkList, HttpUriRequest> chunkListRequestFactory) {

        ByteArrayResponseHandler responseHandler = ByteArrayResponseHandler.instance();
        return fetch(httpClient, chunkList, chunkListRequestFactory, responseHandler);
    }

    public static <T> Optional<T> fetch(
            HttpClient httpClient,
            ChunkServer.StorageHostChunkList chunkList,
            Function<ChunkServer.StorageHostChunkList, HttpUriRequest> chunkListRequestFactory,
            ResponseHandler<T> responseHandler) {

        try {
            HttpUriRequest request = chunkListRequestFactory.apply(chunkList);
            T data = httpClient.execute(request, responseHandler);
            return Optional.of(data);

        } catch (IOException ex) {
            logger.warn("-- fetch() - IOException: {}", ex);
            return Optional.empty();
        }
    }
}
