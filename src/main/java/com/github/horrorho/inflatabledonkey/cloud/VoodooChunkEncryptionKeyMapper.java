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
import com.github.horrorho.inflatabledonkey.chunk.engine.ChunkKeys;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.ChunkInfo;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.ChunkReference;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.StorageHostChunkList;
import com.google.protobuf.ByteString;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;
import javax.annotation.concurrent.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Collectors.groupingBy;

/**
 * Maps Voodoo ChunkServer.ChunkInfo chunk encryption keys.
 *
 * @author Ahseya
 */
@Immutable
public final class VoodooChunkEncryptionKeyMapper
        implements BiFunction<Voodoo, Function<ByteString, Optional<byte[]>>, Voodoo> {

    public static VoodooChunkEncryptionKeyMapper defaultInstance() {
        return DEFAULT_INSTANCE;
    }

    private static final Logger logger = LoggerFactory.getLogger(VoodooChunkEncryptionKeyMapper.class);

    private static final VoodooChunkEncryptionKeyMapper DEFAULT_INSTANCE
            = new VoodooChunkEncryptionKeyMapper(ChunkKeys.instance());

    private final ChunkEncryptionKeyMapper<byte[]> chunkEncryptionKeyMapper;

    public VoodooChunkEncryptionKeyMapper(ChunkEncryptionKeyMapper<byte[]> chunkEncryptionKeyMapper) {
        this.chunkEncryptionKeyMapper = Objects.requireNonNull(chunkEncryptionKeyMapper);
    }

    @Override
    public Voodoo apply(Voodoo voodoo, Function<ByteString, Optional<byte[]>> keyEncryptionKey) {
        Map<ByteString, List<ChunkReference>> chunkReferences = voodoo.fileSignatureToChunkReferences();
        Map<Integer, StorageHostChunkList> containers = voodoo.indexToContainer();

        chunkReferences
                .forEach((k, v) -> keyEncryptionKey.apply(k).ifPresent(kek -> unwrap(containers, v, kek)));

        return new Voodoo(containers, chunkReferences);
    }

    void unwrap(Map<Integer, StorageHostChunkList> containers, List<ChunkReference> references, byte[] kek) {
        Function<byte[], Optional<byte[]>> unwrap = key -> chunkEncryptionKeyMapper.apply(key, kek);
        resolveChunkReferences(containers, references)
                .forEach((index, chunks) -> containers.compute(index, (u, v) -> unwrapKeys(unwrap, v, chunks)));
    }

    StorageHostChunkList
            unwrapKeys(Function<byte[], Optional<byte[]>> unwrap, StorageHostChunkList container, Set<Integer> chunks) {
        StorageHostChunkList.Builder builder = container.toBuilder();
        if (logger.isDebugEnabled()) {
            List<Integer> bad = chunks.stream()
                    .filter(i -> i >= builder.getChunkInfoCount())
                    .collect(toList());
            if (!bad.isEmpty()) {
                logger.warn("-- unwrapKeys() - bad indices: {} shcl: {}", bad, container);
            }
        }
        IntStream.range(0, builder.getChunkInfoCount())
                .filter(chunks::contains)
                .forEach(i -> builder.setChunkInfo(i, unwrapKey(unwrap, builder.getChunkInfo(i))));
        return builder.build();
    }

    ChunkInfo unwrapKey(Function<byte[], Optional<byte[]>> unwrap, ChunkInfo chunkInfo) {
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

    Map<Integer, Set<Integer>>
            resolveChunkReferences(Map<Integer, StorageHostChunkList> containers, List<ChunkReference> references) {
        return references
                .stream()
                .map(cr -> {
                    int containerIndex = (int) cr.getContainerIndex();
                    if (!containers.containsKey(containerIndex)) {
                        logger.warn("-- containerChunkList() - bad container index: {}", containerIndex);
                        return null;
                    }
                    int chunkIndex = (int) cr.getChunkIndex();
                    if (containers.get(containerIndex).getChunkInfoCount() < chunkIndex) {
                        logger.warn("-- containerChunkList() - bad chunk index: {}", chunkIndex);
                        return null;
                    }
                    return new SimpleImmutableEntry<>(containerIndex, chunkIndex);
                })
                .filter(Objects::nonNull)
                .collect(groupingBy(Map.Entry::getKey, mapping(Map.Entry::getValue, toSet())));
    }
}
// TODO concurrent