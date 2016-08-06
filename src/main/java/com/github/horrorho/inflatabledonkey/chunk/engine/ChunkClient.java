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

import com.github.horrorho.inflatabledonkey.chunk.Chunk;
import com.github.horrorho.inflatabledonkey.chunk.store.ChunkStore;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.ChunkInfo;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.HostInfo;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.StorageHostChunkList;
import com.github.horrorho.inflatabledonkey.requests.ChunkListRequestFactory;
import com.github.horrorho.inflatabledonkey.responsehandler.InputStreamResponseHandler;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fetches, orders and decrypts chunk data from the server.
 *
 * @author Ahseya
 */
@Immutable
public final class ChunkClient {

    public static ChunkClient defaultInstance() {
        return DEFAULT_INSTANCE;
    }

    private static final Logger logger = LoggerFactory.getLogger(ChunkClient.class);

    private static final long DEFAULT_EXPIRY_TIMESTAMP_GRACE = -5 * 60 * 1000;  // Negative 5 min grace period.
    private static final ChunkClient DEFAULT_INSTANCE
            = new ChunkClient(ChunkListRequestFactory.instance(), DEFAULT_EXPIRY_TIMESTAMP_GRACE);

    private final Function<HostInfo, HttpUriRequest> requestFactory;
    private final long expiryTimestampGrace;

    public ChunkClient(Function<HostInfo, HttpUriRequest> requestFactory, long expiryTimestampGrace) {
        this.requestFactory = Objects.requireNonNull(requestFactory);
        this.expiryTimestampGrace = expiryTimestampGrace;
    }

    public Set<Chunk> apply(HttpClient client, ChunkStore store, StorageHostChunkList container) throws IOException {
        Optional<Set<Chunk>> stored = storedChunks(store, container);
        if (stored.isPresent()) {
            return stored.get();
        }

        if (!containsType1Keys(container)) {
            // We cannot decrypt non-type 1 keys, little point in fetching data.
            logger.debug("-- apply() - no type 1 keys: ", container);
            return Collections.emptySet();
        }

        if (container.getHostInfo().getExpiry() + expiryTimestampGrace < System.currentTimeMillis()) {
            throw new IllegalStateException("container has expired");
        }

        return fetch(client, store, container);
    }

    Set<Chunk> fetch(HttpClient client, ChunkStore store, StorageHostChunkList container) throws IOException {
        ChunkListDecrypter decrypter = new ChunkListDecrypter(store, compact(container));
        InputStreamResponseHandler<Set<Chunk>> handler = new InputStreamResponseHandler<>(decrypter);
        HttpUriRequest request = requestFactory.apply(container.getHostInfo());
        return client.execute(request, handler);
    }

    boolean containsType1Keys(StorageHostChunkList container) {
        return container
                .getChunkInfoList()
                .stream()
                .map(u -> u.getChunkEncryptionKey().toByteArray())
                .map(ChunkKeys::keyType)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .anyMatch(u -> u == 1);
    }

    StorageHostChunkList compact(StorageHostChunkList container) {
        // ChunkInfos can reference the same chunk block offset, but with different wrapped 0x02 keys. These
        // keys should unwrap to the same 0x01 key with the block yielding the same output data.
        if (container.getChunkInfoCount() < 2) {
            return container;
        }

        List<ChunkInfo> list = container.getChunkInfoList();
        StorageHostChunkList.Builder builder = container.toBuilder().clearChunkInfo();

        ChunkInfo prior = list.get(0);
        for (int i = 1, n = list.size(); i < n; i++) {
            ChunkInfo chunkInfo = list.get(i);
            if (chunkInfo.getChunkOffset() == prior.getChunkOffset()) {
                prior = merge(prior, chunkInfo);
            } else {
                builder.addChunkInfo(prior);
                prior = chunkInfo;
            }
        }
        return builder.addChunkInfo(prior).build();
    }

    ChunkInfo merge(ChunkInfo a, ChunkInfo b) {
        int aType = ChunkKeys.keyType(a.getChunkEncryptionKey().toByteArray()).orElse(-1);
        int bType = ChunkKeys.keyType(b.getChunkEncryptionKey().toByteArray()).orElse(-1);
        if (aType == 0x01 && bType == 0x01 && !a.getChunkEncryptionKey().equals(b.getChunkEncryptionKey())) {
            logger.warn("-- merge() - incongruent chunks: {} {}", a, b);
        } else {
            logger.debug("-- merge() - merged: {} {}", a, b);
        }
        return aType == 0x01 ? a : b;
    }

    Optional<Set<Chunk>> storedChunks(ChunkStore store, StorageHostChunkList container) {
        Set<Chunk> chunks = new HashSet<>();
        List<ChunkInfo> list = container.getChunkInfoList();
        for (int i = 0, n = list.size(); i < n; i++) {
            Optional<Chunk> chunk = store.chunk(list.get(i).getChunkChecksum().toByteArray());
            if (!chunk.isPresent()) {
                logger.debug("-- storedChunks() - not all chunks present in store");
                return Optional.empty();
            }
            chunk.ifPresent(chunks::add);
        }
        logger.debug("-- storedChunks() - all chunks present in store");
        return Optional.of(chunks);
    }
}
