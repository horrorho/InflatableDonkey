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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FileGroups.
 *
 * @author Ahseya
 */
@Immutable
public final class FileGroups {

    private static final Logger logger = LoggerFactory.getLogger(FileGroups.class);

    public static Map<ChunkServer.StorageHostChunkList, Map<Integer, ByteString>> storageHostChunkListToFileSignatures(
            Map<ChunkServer.StorageHostChunkList, List<ChunkServer.ChunkReference>> storageHostChunkListToChunkReferenceList,
            Map<ChunkServer.ChunkReference, ByteString> chunkReferenceToFileSignature) {

        return storageHostChunkListToChunkReferenceList.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> chunkIndexToFileSignature(e.getValue(), chunkReferenceToFileSignature)));
    }

    static Map<Integer, ByteString> chunkIndexToFileSignature(
            List<ChunkServer.ChunkReference> chunkReferenceList,
            Map<ChunkServer.ChunkReference, ByteString> chunkReferenceToFileSignature) {

        return chunkReferenceList.stream()
                .filter(chunkReference -> {
                    if (chunkReferenceToFileSignature.containsKey(chunkReference)) {
                        return true;
                    }
                    logger.warn("-- chunkIndexToFileSignature() - unreferenced chunk: {}", chunkReference);
                    return false;
                })
                .collect(Collectors.toMap(
                        chunkReference -> (int) chunkReference.getChunkIndex(),
                        chunkReferenceToFileSignature::get));
    }

    public static Map<ByteString, Map<ChunkServer.StorageHostChunkList, Integer>> fileSignatureToStorageHostChunks(
            Map<ByteString, List<ChunkServer.ChunkReference>> fileSignatureToChunkReferenceList,
            Map<ChunkServer.ChunkReference, ChunkServer.StorageHostChunkList> chunkReferenceToStorageHostChunkList) {

        return fileSignatureToChunkReferenceList.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> storageHostChunks(e.getValue(), chunkReferenceToStorageHostChunkList)));
    }

    static Map<ChunkServer.StorageHostChunkList, Integer> storageHostChunks(
            List<ChunkServer.ChunkReference> chunkReferenceList,
            Map<ChunkServer.ChunkReference, ChunkServer.StorageHostChunkList> chunkReferenceToStorageHostChunkList) {

        return chunkReferenceList.stream()
                .filter(chunkReference -> {
                    if (chunkReferenceToStorageHostChunkList.containsKey(chunkReference)) {
                        return true;
                    }
                    logger.warn("-- storageHostChunks() - unreferenced chunk: {}", chunkReference);
                    return false;
                })
                .collect(Collectors.toMap(
                        chunkReferenceToStorageHostChunkList::get,
                        chunkReference -> (int) chunkReference.getContainerIndex(),
                        (a, b) -> {
                            if (!Objects.equals(a, b)) {
                                logger.warn("-- storageHostChunks() - mismatched containers: {} {}", a, b);
                            }
                            return a;
                        }));
    }
}
