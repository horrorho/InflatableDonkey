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
package com.github.horrorho.inflatabledonkey.cloud;

import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.ChunkReference;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.FileChecksumChunkReferences;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.FileChecksumStorageHostChunkLists;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.FileGroups;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.StorageHostChunkList;
import com.google.protobuf.ByteString;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class VoodooFactory {

    private static final Logger logger = LoggerFactory.getLogger(VoodooFactory.class);

    public static final List<Voodoo> from(AuthorizedAssets assets) {
        FileGroups fileGroups = assets.fileGroups();
        fileGroups.getFileErrorList()
                .forEach(u -> logger.warn("-- from() - server file error: {}", u));
        fileGroups.getFileChunkErrorList()
                .forEach(u -> logger.warn("-- from() - server file chunk error: {}", u));
        // TODO do we need to filter error assets out, or is already done server side?

        List<Voodoo> voodoos = fileGroups.getFileGroupsList()
                .stream()
                .map(u -> from(assets, u))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        if (voodoos.size() != assets.size()) {
            logger.warn("-- from() - missing assets in: {} out: {}", assets.size(), voodoos.size());
        }
        return voodoos;
    }

    static final List<Voodoo> from(AuthorizedAssets assets, FileChecksumStorageHostChunkLists fileGroup) {
        Map<Integer, StorageHostChunkList> containers = containers(fileGroup.getStorageHostChunkListList());
        List<FileChecksumChunkReferences> fileChecksumChunkReferencesList
                = fileGroup.getFileChecksumChunkReferencesList();
        return voodoos(assets, containers, fileChecksumChunkReferencesList);
    }

    static Map<Integer, StorageHostChunkList> containers(List<StorageHostChunkList> list) {
        return IntStream.range(0, list.size())
                .mapToObj(Integer::valueOf)
                .collect(Collectors.toMap(Function.identity(), list::get));
    }

    static List<Voodoo>
            voodoos(AuthorizedAssets assets, Map<Integer, StorageHostChunkList> containers,
                    List<FileChecksumChunkReferences> references) {
        return references
                .stream()
                .map(u -> voodoo(assets, containers, u))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    static Optional<Voodoo>
            voodoo(AuthorizedAssets assets, Map<Integer, StorageHostChunkList> containers,
                    FileChecksumChunkReferences references) {
        ByteString fileSignature = references.getFileSignature();
        return assets.asset(fileSignature)
                .map(u -> voodoo(u, containers, references))
                .orElseGet(() -> {
                    logger.warn("-- voodoo() - no Asset for file signature: {}", fileSignature);
                    return Optional.empty();
                });
    }

    static Optional<Voodoo>
            voodoo(Asset asset, Map<Integer, StorageHostChunkList> containers, FileChecksumChunkReferences references) {
        try {
            return Optional.of(new Voodoo(asset, chunkReferences(containers, references)));

        } catch (IllegalArgumentException | IllegalStateException ex) {
            logger.warn("-- voodoo() - failed: {}", ex.getMessage());
            return Optional.empty();
        }
    }

    static LinkedHashMap<ChunkReference, StorageHostChunkList>
            chunkReferences(Map<Integer, StorageHostChunkList> containers, FileChecksumChunkReferences references) {
        return references.getChunkReferencesList()
                .stream()
                .map(u -> chunkReferenceEntry(containers, u))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (u, v) -> {
                            throw new IllegalStateException("ChunkReference collision");
                        },
                        LinkedHashMap::new));
    }

    static Map.Entry<ChunkReference, StorageHostChunkList>
            chunkReferenceEntry(Map<Integer, StorageHostChunkList> containers, ChunkReference reference) {
        int containerIndex = (int) reference.getContainerIndex();
        int chunkIndex = (int) reference.getChunkIndex();

        StorageHostChunkList shcl = containers.get((int) reference.getContainerIndex());
        if (shcl == null) {
            throw new IllegalArgumentException("container index out of bounds: " + containerIndex);
        }
        if (shcl.getChunkInfoCount() < chunkIndex) {
            throw new IllegalArgumentException("chunk index out of bounds: " + chunkIndex);
        }
        return new SimpleImmutableEntry<>(reference, shcl);
    }
}
