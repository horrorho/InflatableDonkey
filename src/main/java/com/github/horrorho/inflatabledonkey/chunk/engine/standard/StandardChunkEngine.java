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
import com.github.horrorho.inflatabledonkey.chunk.engine.ChunkClient;
import com.github.horrorho.inflatabledonkey.chunk.engine.SHCLContainer;
import com.github.horrorho.inflatabledonkey.chunk.store.ChunkStore;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer;
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
 *
 * @author Ahseya
 */
@ThreadSafe
public class StandardChunkEngine implements ChunkEngine {

    private static final Logger logger = LoggerFactory.getLogger(StandardChunkEngine.class);

    private final ChunkStore chunkStore;
    private final ChunkClient chunkClient;

    public StandardChunkEngine(ChunkStore chunkStore, ChunkClient chunkClient) {
        this.chunkStore = Objects.requireNonNull(chunkStore);
        this.chunkClient = Objects.requireNonNull(chunkClient);
    }

    public StandardChunkEngine(ChunkStore chunkStore) {
        this(chunkStore, ChunkClient.defaultInstance());
    }

    @Override
    public Optional<Map<ChunkServer.ChunkReference, Chunk>>
            fetch(HttpClient client, SHCLContainer container, byte[] keyEncryptionKey) {

        return fromStore(container)
                .map(Optional::of)
                .orElseGet(() -> chunkClient.apply(client, container, keyEncryptionKey, chunkStore));

    }

    Optional<Map<ChunkServer.ChunkReference, Chunk>> fromStore(SHCLContainer container) {
        return chunkStore.chunks(container.chunkChecksums())
                .map(u -> toMap(container.index(), u));
    }

    Map<ChunkServer.ChunkReference, Chunk> toMap(int index, List<Chunk> chunks) {
        return IntStream.range(0, chunks.size())
                .mapToObj(Integer::valueOf)
                .collect(Collectors.toMap(i -> chunkReference(index, i), chunks::get));
    }

    ChunkServer.ChunkReference chunkReference(int containerIndex, int chunkIndex) {
        return ChunkServer.ChunkReference.newBuilder()
                .setContainerIndex(containerIndex)
                .setChunkIndex(chunkIndex)
                .build();
    }
}
