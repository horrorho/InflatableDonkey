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
package com.github.horrorho.inflatabledonkey.cloud.protos;

import com.github.horrorho.inflatabledonkey.protocol.ChunkServer;
import com.google.protobuf.ByteString;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;

/**
 * Protos.
 *
 * @author Ahseya
 */
@Immutable
public final class Protos {

    private final Map<ByteString, Set<ChunkServer.StorageHostChunkList>> fileSignatureToStorageHostChunkLists;
    private final Map<ChunkServer.StorageHostChunkList, Map<Integer, ByteString>> storageHostChunkListToFileSignatures;

    public Protos(ChunkServer.FileChecksumStorageHostChunkLists fileGroup) {
        Map<ChunkServer.StorageHostChunkList, List<ChunkServer.ChunkReference>> storageHostChunkListToChunkReferenceList
                = StorageHostChunkLists.storageHostChunkListToChunkReferenceList(
                        fileGroup.getStorageHostChunkListList());

        Map<ByteString, List<ChunkServer.ChunkReference>> fileSignatureToChunkReferenceList
                = FileChecksumChunkReferences.fileSignatureToChunkReferenceList(
                        fileGroup.getFileChecksumChunkReferencesList());

        Map<ChunkServer.ChunkReference, ByteString> chunkReferenceToFileSignature
                = MapAssistant.invertMapList(fileSignatureToChunkReferenceList);

        Map<ChunkServer.ChunkReference, ChunkServer.StorageHostChunkList> chunkReferenceToStorageHostChunkList
                = MapAssistant.invertMapList(storageHostChunkListToChunkReferenceList);

        storageHostChunkListToFileSignatures = FileGroups.storageHostChunkListToFileSignatures(
                storageHostChunkListToChunkReferenceList, chunkReferenceToFileSignature);

        fileSignatureToStorageHostChunkLists = FileGroups.fileSignatureToStorageHostChunkLists(
                fileSignatureToChunkReferenceList,
                chunkReferenceToStorageHostChunkList);
    }

    public Set<ChunkServer.StorageHostChunkList> storageHostChunkLists(ByteString fileSignature) {
        return fileSignatureToStorageHostChunkLists.get(fileSignature);
    }

    public <T> Map<Integer, T> storageHostChunkListFileSignatures(Function<ByteString, Optional<T>> function) {
        return storageHostChunkListToFileSignatures.getOrDefault(this, Collections.emptyMap())
                .entrySet()
                .stream()
                .map(e -> new SimpleImmutableEntry<>(e.getKey(), function.apply(e.getValue())))
                .filter(e -> e.getValue().isPresent())
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    }

    public Map<Integer, ByteString> storageHostChunkListFileSignatures() {
        return storageHostChunkListFileSignatures(Optional::of);
    }

    @Override
    public String toString() {
        return "FileGroupData{" + 
                "fileSignatureToStorageHostChunkLists=" + fileSignatureToStorageHostChunkLists +
                ", storageHostChunkListToFileSignatures=" + storageHostChunkListToFileSignatures + 
                '}';
    }
}
// TODO rename to something more meaningful.