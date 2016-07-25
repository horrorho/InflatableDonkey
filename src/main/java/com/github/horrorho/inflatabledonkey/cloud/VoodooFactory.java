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

import com.github.horrorho.inflatabledonkey.chunk.engine.SHCLContainer;
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.ChunkReference;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.FileChecksumChunkReferences;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.FileChecksumStorageHostChunkLists;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.FileGroups;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.StorageHostChunkList;
import com.google.protobuf.ByteString;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

        return fileGroups.getFileGroupsList()
                .stream()
                .map(u -> from(assets, u))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    static final List<Voodoo> from(AuthorizedAssets assets, FileChecksumStorageHostChunkLists fileGroup) {
        List<SHCLContainer> containers = shclContainers(fileGroup.getStorageHostChunkListList());
        List<FileChecksumChunkReferences> fileChecksumChunkReferencesList
                = fileGroup.getFileChecksumChunkReferencesList();
        return voodoos(assets, containers, fileChecksumChunkReferencesList);
    }

    static List<SHCLContainer> shclContainers(List<StorageHostChunkList> list) {
        return IntStream.range(0, list.size())
                .mapToObj(i -> new SHCLContainer(list.get(i), i))
                .collect(Collectors.toList());
    }

    static List<Voodoo>
            voodoos(AuthorizedAssets assets, List<SHCLContainer> containers, List<FileChecksumChunkReferences> references) {
        return references
                .stream()
                .map(u -> voodoo(assets, containers, u))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    static Optional<Voodoo>
            voodoo(AuthorizedAssets assets, List<SHCLContainer> containers, FileChecksumChunkReferences references) {

        ByteString fileSignature = references.getFileSignature();
        ByteString fileChecksum = references.getFileChecksum();

        Optional<Asset> asset = assets.asset(fileSignature);
        if (!asset.isPresent()) {
            logger.warn("-- voodoo() - no Asset for file signature: {}", fileSignature);
            return Optional.empty();
        }

        List<Optional<Map.Entry<ChunkReference, SHCLContainer>>> optionals
                = references.getChunkReferencesList()
                .stream()
                .map(u -> map(containers, u))
                .collect(Collectors.toList());

        if (optionals.contains(Optional.<Map.Entry<ChunkReference, SHCLContainer>>empty())) {
            return Optional.empty();
        }

        List<ChunkReference> chunkReferences = optionals.stream()
                .map(Optional::get)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<SHCLContainer> assetContainers = optionals.stream()
                .map(Optional::get)
                .map(Map.Entry::getValue)
                .distinct()
                .collect(Collectors.toList());

        Voodoo voodoo = new Voodoo(fileSignature, fileChecksum, asset.get(), assetContainers, chunkReferences);
        return Optional.of(voodoo);
    }

    static Optional<Map.Entry<ChunkReference, SHCLContainer>>
            map(List<SHCLContainer> containers, ChunkReference reference) {
        if (containers.size() < reference.getContainerIndex()) {
            logger.warn("-- map() - container index out of bounds: {}", reference.getContainerIndex());
            return Optional.empty();
        }
        SHCLContainer container = containers.get((int) reference.getContainerIndex());
        if (container.count() < reference.getChunkIndex()) {
            logger.warn("-- map() - chunk index out of bounds: {}", reference.getChunkIndex());
            return Optional.empty();
        }
        return Optional.of((new SimpleImmutableEntry<>(reference, container)));
    }
}
// TODO tidy
