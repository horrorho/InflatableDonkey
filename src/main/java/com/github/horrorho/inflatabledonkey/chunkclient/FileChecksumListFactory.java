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
package com.github.horrorho.inflatabledonkey.chunkclient;

import com.github.horrorho.inflatabledonkey.protocol.ChunkServer;
import com.google.protobuf.ByteString;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
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
 * FileChecksumListFactory.
 *
 * @author Ahseya
 */
@Immutable
public final class FileChecksumListFactory {

    private static final Logger logger = LoggerFactory.getLogger(FileChecksumListFactory.class);

    public static Map<ByteString, List<ByteString>>
            fileChecksumLists(ChunkServer.FileGroups fileGroups) {

        List<ChunkServer.FileChunkError> fileChunkErrorList = fileGroups.getFileChunkErrorList();
        if (!fileChunkErrorList.isEmpty()) {
            logger.warn("-- fileChecksumLists() - file chunk errors: {}", fileChunkErrorList);
        }

        List<ChunkServer.FileError> fileErrorList = fileGroups.getFileErrorList();
        if (!fileErrorList.isEmpty()) {
            logger.warn("-- fileChecksumLists() - file  errors: {}", fileChunkErrorList);
        }

        // Map: file checksum to an ordered list of constituent chunk checksums.
        return fileGroups.getFileGroupsList()
                .stream()
                .map(FileChecksumListFactory::fileChecksumLists)
                .collect(HashMap::new, HashMap::putAll, HashMap::putAll);
    }

    public static Map<ByteString, List<ByteString>>
            fileChecksumLists(ChunkServer.FileChecksumStorageHostChunkLists fileGroup) {

        Map<Integer, Map<Integer, ByteString>> containers = containers(fileGroup.getStorageHostChunkListList());

        return fileChecksumLists(containers, fileGroup.getFileChecksumChunkReferencesList());
    }

    static Map<ByteString, List<ByteString>> fileChecksumLists(
            Map<Integer, Map<Integer, ByteString>> containers,
            List<ChunkServer.FileChecksumChunkReferences> chunkReferencesList) {

        return chunkReferencesList.stream()
                .map(chunkReferences -> fileChecksumList(containers, chunkReferences))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    static Optional<Map.Entry<ByteString, List<ByteString>>> fileChecksumList(
            Map<Integer, Map<Integer, ByteString>> containers,
            ChunkServer.FileChecksumChunkReferences chunkReferences) {

        return checksumList(containers, chunkReferences)
                .map(checksumList -> new SimpleImmutableEntry(chunkReferences.getFileChecksum(), checksumList));
    }

    static Optional<List<ByteString>> checksumList(
            Map<Integer, Map<Integer, ByteString>> containers,
            ChunkServer.FileChecksumChunkReferences chunkReferences) {

        List<ChunkServer.ChunkReference> chunkReferencesList = chunkReferences.getChunkReferencesList();

        List<Optional<ByteString>> optionalChecksumList = chunkReferencesList.stream()
                .map(chunkReference -> chunkChecksum(containers, chunkReference))
                .collect(Collectors.toList());

        boolean allpresent = optionalChecksumList.stream()
                .allMatch(Optional::isPresent);

        if (!allpresent) {
            logger.warn("-- fileChecksumList() - missing chunk checksum/s");
            return Optional.empty();
        }

        List<ByteString> fileChecksumList = optionalChecksumList.stream()
                .map(Optional::get)
                .collect(Collectors.toList());

        return Optional.of(fileChecksumList);
    }

    static Optional<ByteString> chunkChecksum(
            Map<Integer, Map<Integer, ByteString>> containers,
            ChunkServer.ChunkReference chunkReference) {

        int containerIndex = (int) chunkReference.getContainerIndex();
        int chunkIndex = (int) chunkReference.getChunkIndex();

        if (!containers.containsKey(containerIndex)) {
            logger.warn("-- chunkChecksum() - container not found: {}", chunkReference);
            return Optional.empty();
        }

        Map<Integer, ByteString> container = containers.get(containerIndex);
        if (!container.containsKey(chunkIndex)) {
            logger.warn("-- chunkChecksum() - chunk not found: {}", chunkReference);
            return Optional.empty();
        }

        return Optional.of(container.get(chunkIndex));
    }

    static Map<Integer, Map<Integer, ByteString>> containers(List<ChunkServer.StorageHostChunkList> chunkLists) {
        // Map of container index to container.
        return IntStream.range(0, chunkLists.size())
                .mapToObj(Integer::valueOf)
                .collect(Collectors.toMap(
                        Function.identity(),
                        i -> container(chunkLists.get(i))));
    }

    static Map<Integer, ByteString> container(ChunkServer.StorageHostChunkList chunkList) {
        // List of chunk checksums.
        List<ByteString> chunkChecksumList = chunkList.getChunkInfoList()
                .stream()
                .map(ChunkServer.ChunkInfo::getChunkChecksum)
                .collect(Collectors.toList());

        // Map of chunk checksum index to chunk checksum.
        return IntStream.range(0, chunkChecksumList.size())
                .mapToObj(Integer::valueOf)
                .collect(Collectors.toMap(
                        Function.identity(),
                        chunkChecksumList::get));
    }
}
