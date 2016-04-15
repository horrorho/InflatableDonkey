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
package com.github.horrorho.inflatabledonkey.chunk.engine.standard;

import com.github.horrorho.inflatabledonkey.chunk.Chunk;
import com.github.horrorho.inflatabledonkey.chunk.engine.ChunkEngine;
import com.github.horrorho.inflatabledonkey.chunk.engine.ChunkListDecrypters;
import com.github.horrorho.inflatabledonkey.chunk.engine.ChunkClient;
import com.github.horrorho.inflatabledonkey.chunk.store.ChunkStore;
import com.github.horrorho.inflatabledonkey.protocol.ChunkServer;
import com.google.protobuf.ByteString;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.jcip.annotations.ThreadSafe;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * StandardChunkEngine.
 *
 * @author Ahseya
 */
@ThreadSafe
public class StandardChunkEngine implements ChunkEngine {

    private static final Logger logger = LoggerFactory.getLogger(StandardChunkEngine.class);

    private final ChunkStore chunkStore;

    public StandardChunkEngine(ChunkStore chunkStore) {
        this.chunkStore = Objects.requireNonNull(chunkStore, "chunkStore");
    }

    @Override
    public List<Optional<Chunk>> fetch(
            HttpClient httpClient,
            ChunkServer.StorageHostChunkList chunkList,
            Map<Integer, byte[]> keyEncryptionKeys) {

        List<Optional<Chunk>> chunkStoreChunks = chunkStoreChunks(chunkList.getChunkInfoList());

        if (!chunkStoreChunks.contains(Optional.<Chunk>empty())) {
            // ChunkStore has all our chunks, no need to fetch.
            return chunkStoreChunks;
        }

        // Retrieve chunks from cloud servers.
        List<Optional<Chunk>> fetchChunks = fetchChunks(httpClient, chunkList, keyEncryptionKeys);

       
        // If possible, fill in any missing chunks with those from the ChunkStore.
        return substitute(fetchChunks, chunkStoreChunks);
    }

    List<Optional<Chunk>> fetchChunks(
            HttpClient httpClient,
            ChunkServer.StorageHostChunkList chunkList,
            Map<Integer, byte[]> keyEncryptionKeys) {

        byte[] chunkData = fetchChunkData(httpClient, chunkList);
        return ChunkListDecrypters.decrypt(chunkList.getChunkInfoList(), chunkStore, chunkData, keyEncryptionKeys);
    }

    byte[] fetchChunkData(HttpClient httpClient, ChunkServer.StorageHostChunkList chunkList) {
        return ChunkClient.fetch(httpClient, chunkList)
                .orElse(new byte[]{});
    }

    List<Optional<Chunk>> chunkStoreChunks(List<ChunkServer.ChunkInfo> list) {
        List<byte[]> checksumList = checksumList(list);
        return chunkStore.chunks(checksumList);
    }

    List<byte[]> checksumList(List<ChunkServer.ChunkInfo> list) {
        return list.stream()
                .filter(chunkInfo -> {
                    if (!chunkInfo.hasChunkChecksum()) {
                        logger.warn("-- checksumList() - no checksum: {}", chunkInfo);
                        return false;
                    }
                    return true;
                })
                .map(ChunkServer.ChunkInfo::getChunkChecksum)
                .map(ByteString::toByteArray)
                .collect(Collectors.toList());
    }

    <T> List<Optional<T>> substitute(List<Optional<T>> list, List<Optional<T>> substitutes) {
        return IntStream.range(0, list.size())
                .mapToObj(i -> {
                    Optional<T> optional = list.get(i);
                    if (optional.isPresent()) {
                        return optional;
                    }
                    if (i < substitutes.size()) {
                        return substitutes.get(i);
                    }
                    return Optional.<T>empty();
                })
                .collect(Collectors.toList());
    }
}