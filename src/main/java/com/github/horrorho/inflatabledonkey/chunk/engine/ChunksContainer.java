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

import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.jcip.annotations.Immutable;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class ChunksContainer {

    private final ChunkServer.StorageHostChunkList shcl;
    private final ChunkKeyEncryptionKey kek;
    private final int index;

    public ChunksContainer(ChunkServer.StorageHostChunkList shcl, ChunkKeyEncryptionKey kek, int index) {
        this.shcl = Objects.requireNonNull(shcl);
        this.kek = Objects.requireNonNull(kek);
        this.index = index;
    }

    public ChunkServer.StorageHostChunkList storageHostChunkList() {
        return shcl;
    }

    public int index() {
        return index;
    }

    public int count() {
        return shcl.getChunkInfoCount();
    }

    public Optional<byte[]> keyEncryptionKey(int index) {
        return kek.apply(index);
    }

    public LinkedHashMap<ChunkServer.ChunkReference, ChunkServer.ChunkInfo> chunkInfos() {
        List<ChunkServer.ChunkInfo> list = shcl.getChunkInfoList();
        return IntStream.range(0, list.size())
                .mapToObj(Integer::valueOf)
                .collect(Collectors.toMap(
                        i -> chunkReference(index, i),
                        list::get,
                        (u, t) -> u,
                        LinkedHashMap::new));
    }

    ChunkServer.ChunkReference chunkReference(int containerIndex, int chunkIndex) {
        return ChunkServer.ChunkReference.newBuilder()
                .setContainerIndex(containerIndex)
                .setChunkIndex(chunkIndex)
                .build();
    }
}
