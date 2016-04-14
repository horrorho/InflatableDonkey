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
import java.util.function.Function;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;

/**
 * Voodoo.
 *
 * @author Ahseya
 */
@Immutable
public final class Voodoo {

    public static List<Voodoo> create(ChunkServer.FileGroups fileGroups) {
        // TODO error information.
        return fileGroups.getFileGroupsList()
                .stream()
                .map(Voodoo::new)
                .collect(Collectors.toList());
    }

    private final Map<ByteString, Map<ChunkServer.StorageHostChunkList, Integer>> fileSignatureToStorageHostChunkMap;
    private final Map<ByteString, List<ChunkServer.ChunkReference>> fileSignatureToChunkReferenceList;
    private final Map<ChunkServer.StorageHostChunkList, Map<Integer, ByteString>> storageHostChunkListToFileSignatures;

    public Voodoo(ChunkServer.FileChecksumStorageHostChunkLists fileGroup) {
        Map<ChunkServer.StorageHostChunkList, List<ChunkServer.ChunkReference>> storageHostChunkListToChunkReferenceList
                = StorageHostChunkLists.storageHostChunkListToChunkReferenceList(
                        fileGroup.getStorageHostChunkListList());

        fileSignatureToChunkReferenceList
                = FileChecksumChunkReferences.fileSignatureToChunkReferenceList(
                        fileGroup.getFileChecksumChunkReferencesList());

        Map<ChunkServer.ChunkReference, ByteString> chunkReferenceToFileSignature
                = MapAssistant.invertMapList(fileSignatureToChunkReferenceList);

        Map<ChunkServer.ChunkReference, ChunkServer.StorageHostChunkList> chunkReferenceToStorageHostChunkList
                = MapAssistant.invertMapList(storageHostChunkListToChunkReferenceList);

        storageHostChunkListToFileSignatures = FileGroups.storageHostChunkListToFileSignatures(
                storageHostChunkListToChunkReferenceList, chunkReferenceToFileSignature);

        fileSignatureToStorageHostChunkMap = FileGroups.fileSignatureToStorageHostChunks(
                fileSignatureToChunkReferenceList,
                chunkReferenceToStorageHostChunkList);
    }

    public List<ChunkServer.ChunkReference> fileSignatureToChunkReferenceList(ByteString fileSignature) {
        return fileSignatureToChunkReferenceList.get(fileSignature);
    }

    public Map<ChunkServer.StorageHostChunkList, Integer> storageHostChunkLists(ByteString fileSignature) {
        return fileSignatureToStorageHostChunkMap.get(fileSignature);
    }

    public <T> Map<Integer, T> storageHostChunkListFileSignatures(
            ChunkServer.StorageHostChunkList storageHostChunkList, Function<ByteString, Optional<T>> function) {

        return storageHostChunkListToFileSignatures.getOrDefault(storageHostChunkList, Collections.emptyMap())
                .entrySet()
                .stream()
                .map(e -> new SimpleImmutableEntry<>(e.getKey(), function.apply(e.getValue())))
                .filter(e -> e.getValue().isPresent())
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    }

    public Map<Integer, ByteString>
            storageHostChunkListFileSignatures(ChunkServer.StorageHostChunkList storageHostChunkList) {
        return storageHostChunkListFileSignatures(storageHostChunkList, Optional::of);
    }

    @Override
    public String toString() {
        return "Voodoo{"
                + "fileSignatureToStorageHostChunkLists=" + fileSignatureToStorageHostChunkMap
                + ", storageHostChunkListToFileSignatures=" + storageHostChunkListToFileSignatures
                + '}';
    }
}
// TODO rename to something more meaningful.
