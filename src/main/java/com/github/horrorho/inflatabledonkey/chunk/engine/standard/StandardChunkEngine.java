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
import com.github.horrorho.inflatabledonkey.cloud.voodoo.SHCLContainer;
import com.github.horrorho.inflatabledonkey.chunk.store.ChunkStore;
import com.github.horrorho.inflatabledonkey.cloud.voodoo.ChunkReferences;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer;
import com.google.protobuf.ByteString;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
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
    public Map<ChunkServer.ChunkReference, Chunk> fetch(
            HttpClient httpClient,
            SHCLContainer storageHostChunkListContainer,
            Function<ChunkServer.ChunkReference, Optional<byte[]>> getKeyEncryptionKey) {

        Optional<Map<ChunkServer.ChunkReference, Chunk>> chunkStoreChunks
                = chunkStoreChunks(storageHostChunkListContainer);

        if (chunkStoreChunks.isPresent()) {
            return chunkStoreChunks.get();
        }

        return fetchChunks(httpClient, storageHostChunkListContainer, getKeyEncryptionKey);
    }

    Map<ChunkServer.ChunkReference, Chunk> fetchChunks(
            HttpClient httpClient,
            SHCLContainer storageHostChunkListContainer,
            Function<ChunkServer.ChunkReference, Optional<byte[]>> getKeyEncryptionKey) {

        ChunkServer.StorageHostChunkList storageHostChunkList = storageHostChunkListContainer.storageHostChunkList();
        int container = storageHostChunkListContainer.container();

        byte[] chunkData = fetchChunkData(httpClient, storageHostChunkList);

        return ChunkListDecrypters.decrypt(
                container,
                storageHostChunkList.getChunkInfoList(),
                chunkStore,
                chunkData,
                getKeyEncryptionKey);
    }

    byte[] fetchChunkData(HttpClient httpClient, ChunkServer.StorageHostChunkList chunkList) {
        return ChunkClient.fetch(httpClient, chunkList)
                .orElseGet(() -> new byte[]{});
    }

    Optional<Map<ChunkServer.ChunkReference, Chunk>>
            chunkStoreChunks(SHCLContainer storageHostChunkListContainer) {

        ChunkServer.StorageHostChunkList storageHostChunkList = storageHostChunkListContainer.storageHostChunkList();
        int container = storageHostChunkListContainer.container();

        List<byte[]> checksumList = checksumList(storageHostChunkList.getChunkInfoList());
        List<Optional<Chunk>> chunkList = chunkStore.chunks(checksumList);

        return chunks(container, chunkList);
    }

    Optional<Map<ChunkServer.ChunkReference, Chunk>> chunks(int container, List<Optional<Chunk>> chunkList) {
        if (chunkList.contains(Optional.<Chunk>empty())) {
            return Optional.empty();
        }

        Map<ChunkServer.ChunkReference, Chunk> chunks = IntStream.range(0, chunkList.size())
                .filter(i -> chunkList.get(i).isPresent())
                .mapToObj(Integer::valueOf)
                .collect(Collectors.toMap(
                        i -> ChunkReferences.chunkReference(container, i),
                        i -> chunkList.get(i).get()));

        return Optional.of(chunks);
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

}
