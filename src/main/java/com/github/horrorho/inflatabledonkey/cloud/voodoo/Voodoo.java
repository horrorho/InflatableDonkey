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
import com.google.protobuf.ByteString;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FileGroup assistant. Undefined behaviour for duplicate file signatures.
 *
 * @author Ahseya
 */
@Immutable
public final class Voodoo {

    static <K, V> Map<V, K> invert(Map<K, List<V>> map) {
        return map.entrySet()
                .stream()
                .flatMap(e -> e.getValue().stream()
                        .map(v -> new AbstractMap.SimpleImmutableEntry<>(v, e.getKey())))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (u, v) -> {
                            logger.warn("-- invert() - duplicates: {} {}", u, v);
                            return u;
                        }));
    }

    private static final Logger logger = LoggerFactory.getLogger(Voodoo.class);

    private final Map<ChunkReference, SHCLContainer> chunkRefToSHCLContainer;
    private final Map<ByteString, List<ChunkReference>> fileSigToChunkRefList;
    private final Map<ChunkReference, ByteString> chunkRefToFileSig;

    public Voodoo(FileChecksumStorageHostChunkLists fileGroup) {
        List<StorageHostChunkList> shclList = fileGroup.getStorageHostChunkListList();

        Map<SHCLContainer, List<ChunkReference>> shclContainerToChunkRefList
                = SHCLContainers.shclContainerToChunkRefList(shclList);

        chunkRefToSHCLContainer = invert(shclContainerToChunkRefList);

        fileSigToChunkRefList = FileChecksumChunkReferences
                .fileSignatureToChunkReferenceList(fileGroup.getFileChecksumChunkReferencesList());

        chunkRefToFileSig = invert(fileSigToChunkRefList);
    }

    /**
     * Returns ChunkReferences in the correct construction order for the specified file signature.
     *
     * @param fileSignature
     * @return list of ChunkReferences or empty if the signature is unreferenced
     */
    public Optional<List<ChunkReference>> chunkReferenceList(ByteString fileSignature) {
        return Optional.ofNullable(fileSigToChunkRefList.get(fileSignature));
    }

    /**
     * Returns the file signature associated with the specified ChunkReference.
     *
     * @param chunkReference
     * @return the associated file signature or empty
     */
    public Optional<ByteString> fileSignature(ChunkReference chunkReference) {
        return chunkRefToFileSig.containsKey(chunkReference)
                ? Optional.of(chunkRefToFileSig.get(chunkReference))
                : Optional.empty();
    }

    /**
     * Returns the SHCLContainers required for the specified file signature.
     *
     * @param fileSignature
     * @return the SHCLContainers
     */
    public Set<SHCLContainer> storageHostChunkListContainers(ByteString fileSignature) {
        List<ChunkReference> chunkReferenceList = fileSigToChunkRefList.get(fileSignature);
        if (chunkReferenceList == null) {
            return Collections.emptySet();
        }
        return chunkReferenceList.stream()
                .map(chunkRefToSHCLContainer::get)
                .collect(Collectors.toSet());
    }
}
