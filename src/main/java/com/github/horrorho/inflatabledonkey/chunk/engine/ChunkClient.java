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

import com.github.horrorho.inflatabledonkey.chunk.store.ChunkStore;
import com.github.horrorho.inflatabledonkey.io.IOFunction;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.HostInfo;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.StorageHostChunkList;
import com.github.horrorho.inflatabledonkey.requests.ChunkListRequestFactory;
import com.github.horrorho.inflatabledonkey.responsehandler.InputStreamResponseHandler;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import static java.util.stream.Collectors.toList;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fetches and decrypts chunk data from the server.
 *
 * @author Ahseya
 */
@Immutable
public final class ChunkClient {

    public static ChunkClient defaults() {
        return DEFAULTS;
    }

    private static final Logger logger = LoggerFactory.getLogger(ChunkClient.class);

    // TODO inject
    private static final long DEFAULT_EXPIRY_TIMESTAMP_GRACE = -5 * 60 * 1000;  // Negative 5 min grace period.

    private static final ChunkClient DEFAULTS
            = new ChunkClient(ChunkListRequestFactory.instance(), DEFAULT_EXPIRY_TIMESTAMP_GRACE);
    private static final ChunkListDecrypter DECRYPTER = ChunkListDecrypter.instance();

    private final Function<HostInfo, HttpUriRequest> requestFactory;
    private final long expiryTimestampGrace;

    public ChunkClient(Function<HostInfo, HttpUriRequest> requestFactory, long expiryTimestampGrace) {
        this.requestFactory = Objects.requireNonNull(requestFactory);
        this.expiryTimestampGrace = expiryTimestampGrace;
    }

    /**
     *
     * @param client
     * @param container
     * @param store
     * @throws IOException
     * @throws IllegalArgumentException on non 0x01 chunk keys
     */
    public void apply(HttpClient client, StorageHostChunkList container, ChunkStore store) throws IOException {
        List<byte[]> checksums = checksums(container);
        if (store.allChunks(checksums).isPresent()) {
            logger.debug("-- apply() - all chunks are already present in the store");
            return;
        }
        if (container.getHostInfo().getExpiry() + expiryTimestampGrace < System.currentTimeMillis()) {
            // TOFIX more specific exception
            throw new IllegalStateException("container has expired");
        }
        fetch(client, store, container);
    }

    List<byte[]> checksums(StorageHostChunkList container) {
        return container.getChunkInfoList()
                .stream()
                .map(chunkInfo -> chunkInfo.getChunkChecksum().toByteArray())
                .collect(toList());
    }

    void fetch(HttpClient client, ChunkStore store, StorageHostChunkList container) throws IOException {
        IOFunction<InputStream, Void> decrypt
                = is -> {
                    DECRYPTER.apply(container, is, store);
                    return null;
                };

        InputStreamResponseHandler<Void> handler = new InputStreamResponseHandler<>(decrypt);
        HttpUriRequest request = requestFactory.apply(container.getHostInfo());
        client.execute(request, handler);
    }
}
