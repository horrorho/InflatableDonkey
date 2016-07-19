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

import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer;
import com.google.protobuf.ByteString;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;

/**
 * Voodoo.
 *
 * @author Ahseya
 */
@Immutable
public final class Voodoo {

    private final Map<ChunkServer.ChunkReference, StorageHostChunkListContainer> chunkReferenceToStorageHostChunkListContainer;
    private final Map<ByteString, List<ChunkServer.ChunkReference>> fileSignatureToChunkReferenceList;
    private final Map<ChunkServer.ChunkReference, ByteString> chunkReferenceToFileSignature;

    public Voodoo(ChunkServer.FileChecksumStorageHostChunkLists fileGroup) {
        List<ChunkServer.StorageHostChunkList> storageHostChunkListList = fileGroup.getStorageHostChunkListList();

        Map<StorageHostChunkListContainer, List<ChunkServer.ChunkReference>> storageHostChunkListContainerToChunkReferenceList
                = StorageHostChunkListContainers.storageHostChunkListContainerToChunkReferenceList(storageHostChunkListList);

        chunkReferenceToStorageHostChunkListContainer
                = MapAssistant.invertMapList(storageHostChunkListContainerToChunkReferenceList);

        fileSignatureToChunkReferenceList
                = FileChecksumChunkReferences.fileSignatureToChunkReferenceList(
                        fileGroup.getFileChecksumChunkReferencesList());

        chunkReferenceToFileSignature = MapAssistant.invertMapList(fileSignatureToChunkReferenceList);
    }

    public List<ChunkServer.ChunkReference> chunkReferenceList(ByteString fileSignature) {
        return fileSignatureToChunkReferenceList.get(fileSignature);
    }

    public Optional<ByteString> fileSignature(ChunkServer.ChunkReference chunkReference) {
        return chunkReferenceToFileSignature.containsKey(chunkReference)
                ? Optional.of(chunkReferenceToFileSignature.get(chunkReference))
                : Optional.empty();
    }

    public Set<StorageHostChunkListContainer> storageHostChunkListContainer(ByteString fileSignature) {
        List<ChunkServer.ChunkReference> chunkReferenceList = fileSignatureToChunkReferenceList.get(fileSignature);
        if (chunkReferenceList == null) {
            return Collections.emptySet();
        }

        return chunkReferenceList.stream()
                .map(chunkReferenceToStorageHostChunkListContainer::get)
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "Voodoo{"
                + "chunkReferenceToStorageHostChunkListContainer=" + chunkReferenceToStorageHostChunkListContainer
                + ", fileSignatureToChunkReferenceList=" + fileSignatureToChunkReferenceList
                + ", chunkReferenceToFileSignature=" + chunkReferenceToFileSignature
                + '}';
    }
}