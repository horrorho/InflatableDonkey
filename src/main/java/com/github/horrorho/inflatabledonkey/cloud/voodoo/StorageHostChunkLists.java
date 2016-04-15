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
package com.github.horrorho.inflatabledonkey.cloud.voodoo;

import com.github.horrorho.inflatabledonkey.protocol.ChunkServer;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.jcip.annotations.Immutable;

/**
 * StorageHostChunkLists.
 *
 * @author Ahseya
 */
@Immutable
public final class StorageHostChunkLists {

    public static Map<ChunkServer.StorageHostChunkList, List<ChunkServer.ChunkReference>>
            storageHostChunkListToChunkReferenceList(List<ChunkServer.StorageHostChunkList> storageHostChunkListList) {

        return IntStream.range(0, storageHostChunkListList.size())
                .mapToObj(i -> storageHostChunkListChunkReferenceList(storageHostChunkListList.get(i), i))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    static Map.Entry<ChunkServer.StorageHostChunkList, List<ChunkServer.ChunkReference>>
            storageHostChunkListChunkReferenceList(ChunkServer.StorageHostChunkList storageHostChunkList, int container) {

        int chunkCount = storageHostChunkList.getChunkInfoCount();

        List<ChunkServer.ChunkReference> chunkReferenceList = IntStream.range(0, chunkCount)
                .mapToObj(i -> ChunkReferences.chunkReference(container, i))
                .collect(Collectors.toList());

        return new AbstractMap.SimpleImmutableEntry<>(storageHostChunkList, chunkReferenceList);
    }
}
