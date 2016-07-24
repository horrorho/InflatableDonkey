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

import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.*;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.jcip.annotations.Immutable;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class SHCLContainers {

    public static Map<SHCLContainer, List<ChunkReference>>
            shclContainerToChunkRefList(List<StorageHostChunkList> list) {

        return IntStream.range(0, list.size())
                .mapToObj(i -> shclContainerChunkRefList(list.get(i), i))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    static Map.Entry<SHCLContainer, List<ChunkReference>>
            shclContainerChunkRefList(StorageHostChunkList list, int container) {

        int chunkCount = list.getChunkInfoCount();
        List<ChunkReference> chunkReferenceList = IntStream.range(0, chunkCount)
                .mapToObj(i -> ChunkReferences.chunkReference(container, i))
                .collect(Collectors.toList());

        SHCLContainer shclContainer = new SHCLContainer(list, container);

        return new SimpleImmutableEntry<>(shclContainer, chunkReferenceList);
    }
}
