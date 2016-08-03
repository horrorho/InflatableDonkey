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

import com.github.horrorho.inflatabledonkey.chunk.engine.ChunkEncryptionKeyMapper;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.ChunkInfo;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.ChunkReference;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.StorageHostChunkList;
import com.google.protobuf.ByteString;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.util.stream.Collectors.groupingBy;

/**
 * Maps Voodoo ChunkServer.ChunkInfo chunk encryption keys.
 *
 * @author Ahseya
 */
@Immutable
public final class VoodooChunkEncryptionKeyMapper {

    @Immutable
    @FunctionalInterface
    private interface KeyMapper extends Function<byte[], Optional<byte[]>> {
    }

    public static Voodoo map(Voodoo voodoo, ChunkEncryptionKeyMapper<byte[]> chunkEncryptionKeyMapper,
            Map<ByteString, byte[]> fileSignatureToKeyEncryptionKey) {

        Map<Integer, StorageHostChunkList> indexToSHCL = voodoo.indexToContainer();
        fileSignatureToKeyEncryptionKey
                .entrySet()
                .stream()
                .filter(u -> voodoo.has(u.getKey()))
                .<Map.Entry<ByteString, KeyMapper>>map(
                        e -> new AbstractMap.SimpleImmutableEntry<>(
                                e.getKey(), cek -> chunkEncryptionKeyMapper.apply(cek, e.getValue())))
                .forEach(u -> containerChunkList(voodoo.chunkReferences(u.getKey()).get(), indexToSHCL)
                        .forEach((i, j) -> indexToSHCL.compute(i, (x, shcl) -> unwrapKeys(shcl, u.getValue(), j))));
        return new Voodoo(indexToSHCL, voodoo.fileSignatureToChunkReferences());
    }

    static StorageHostChunkList unwrapKeys(StorageHostChunkList shcl, KeyMapper unwrap, Collection<Integer> chunks) {
        return unwrapKeys(shcl, unwrap, new HashSet<>(chunks));
    }

    static StorageHostChunkList unwrapKeys(StorageHostChunkList shcl, KeyMapper unwrap, Set<Integer> chunks) {
        StorageHostChunkList.Builder builder = shcl.toBuilder();
        if (logger.isDebugEnabled()) {
            List<Integer> bad = chunks.stream()
                    .filter(i -> i >= builder.getChunkInfoCount())
                    .collect(toList());
            if (!bad.isEmpty()) {
                logger.warn("-- unwrapKeys() - bad indices: {} shcl: {}", bad, shcl);
            }
        }
        IntStream.range(0, builder.getChunkInfoCount())
                .filter(chunks::contains)
                .forEach(i -> builder.setChunkInfo(i, unwrapKey(unwrap, builder.getChunkInfo(i))));
        return builder.build();
    }

    static ChunkInfo unwrapKey(KeyMapper unwrap, ChunkInfo chunkInfo) {
        return unwrap.apply(chunkInfo.getChunkEncryptionKey().toByteArray())
                .map(ByteString::copyFrom)
                .map(u -> chunkInfo
                        .toBuilder()
                        .setChunkEncryptionKey(u)
                        .build())
                .orElseGet(() -> {
                    logger.warn("-- unwrapKey() - chunk encryption key unwrap failed: {}", chunkInfo.getChunkChecksum());
                    return chunkInfo.toBuilder().clearChunkEncryptionKey().build();
                });
    }

    static Map<Integer, List<Integer>>
            containerChunkList(List<ChunkReference> chunkReferences, Map<Integer, StorageHostChunkList> indexToSHCL) {
        return chunkReferences
                .stream()
                .filter(cr -> {
                    int containerIndex = (int) cr.getContainerIndex();
                    if (!indexToSHCL.containsKey(containerIndex)) {
                        logger.warn("-- containerChunkList() - bad container index: {}", containerIndex);
                        return false;
                    }
                    int chunkIndex = (int) cr.getChunkIndex();
                    if (indexToSHCL.get(containerIndex).getChunkInfoCount() < chunkIndex) {
                        logger.warn("-- containerChunkList() - bad chunk index: {}", chunkIndex);
                        return false;
                    }
                    return true;
                })
                .collect(groupingBy(u -> (int) u.getContainerIndex(), mapping(v -> (int) v.getChunkIndex(), toList())));
    }

    private static final Logger logger = LoggerFactory.getLogger(VoodooChunkEncryptionKeyMapper.class);
}
// TODO convert to Object
