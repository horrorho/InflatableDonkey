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

import com.github.horrorho.inflatabledonkey.chunk.Chunk;
import com.github.horrorho.inflatabledonkey.chunk.engine.ChunkClient;
import com.github.horrorho.inflatabledonkey.chunk.engine.ChunkEncryptionKeyConverter;
import com.github.horrorho.inflatabledonkey.chunk.store.ChunkStore;
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.ChunkReference;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.StorageHostChunkList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import net.jcip.annotations.ThreadSafe;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.horrorho.inflatabledonkey.chunk.engine.ChunkKeys;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.util.function.Consumer;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author Ahseya
 */
@ThreadSafe
public final class AssetDownloader {

    private static final Logger logger = LoggerFactory.getLogger(AssetDownloader.class);

    private final ChunkClient chunkClient;
    private final ChunkStore store;
    private final ChunkEncryptionKeyConverter<byte[]> converter;

    public AssetDownloader(ChunkClient chunkClient, ChunkStore store, ChunkEncryptionKeyConverter<byte[]> converter) {
        this.chunkClient = Objects.requireNonNull(chunkClient);
        this.store = Objects.requireNonNull(store);
        this.converter = Objects.requireNonNull(converter);
    }

    public AssetDownloader(ChunkStore store) {
        this(ChunkClient.defaultInstance(), store, ChunkKeys.instance());
    }

    public void
            accept(HttpClient httpClient, AuthorizedAssets authorizedAssets, BiConsumer<Asset, List<Chunk>> consumer) {
        // Map consumer to accepting file signatures.
        BiConsumer<ByteString, List<Chunk>> c = consumer(consumer, authorizedAssets.fileSignatureToAsset());

        // Unwrap and replace type 2 chunk encryption keys with type 1 keys.
        Map<ByteString, byte[]> fsToKek = authorizedAssets.fileSignatureToKeyEncryptionKey();
        List<Voodoo> voodooList = VoodooFactory.from(authorizedAssets.fileGroups())
                .stream()
                .map(u -> VoodooAssistant.convertChunkEncryptionKeys(u, converter, fsToKek))
                .collect(toList());

        // Remaining process is now similar to iOS 8.
        processGroups(httpClient, voodooList, c);
    }

    BiConsumer<ByteString, List<Chunk>>
            consumer(BiConsumer<Asset, List<Chunk>> consumer, Map<ByteString, Asset> fileSignatureToAsset) {
        return (u, v) -> {
            if (fileSignatureToAsset.containsKey(u)) {
                consumer.accept(fileSignatureToAsset.get(u), v);
            } else {
                logger.warn("-- consumer() - no asset for file signature: {}", u);
            }
        };
    }

    void processGroups(HttpClient httpClient, Collection<Voodoo> voodoo, BiConsumer<ByteString, List<Chunk>> consumer) {
        voodoo.forEach(u -> processGroup(httpClient, u, consumer));
    }

    void processGroup(HttpClient httpClient, Voodoo voodoo, BiConsumer<ByteString, List<Chunk>> consumer) {
        voodoo.fileSignatures()
                .stream()
                .forEach(u -> process(httpClient, voodoo, u, v -> consumer.accept(u, v)));
    }

    void process(HttpClient httpClient, Voodoo voodoo, ByteString fileSignature, Consumer< List<Chunk>> consumer) {
        voodoo.shcls(fileSignature)
                .ifPresent(u -> voodoo
                        .chunkReferences(fileSignature)
                        .flatMap(v -> fetch(httpClient, v, u))
                        .ifPresent(consumer));
    }

    public Optional<List<Chunk>>
            fetch(HttpClient httpClient, List<ChunkReference> chunks, Map<Integer, StorageHostChunkList> containers) {
        return fetch(httpClient, containers)
                .filter(u -> u.keySet().containsAll(chunks))
                .map(u -> chunks
                        .stream()
                        .map(u::get)
                        .collect(toList()));
    }

    Optional<Map<ChunkReference, Chunk>>
            fetch(HttpClient httpClient, Map<Integer, StorageHostChunkList> containers) {
        try {
            Map<ChunkReference, Chunk> map = new HashMap<>();
            for (Map.Entry<Integer, StorageHostChunkList> entry : containers.entrySet()) {
                StorageHostChunkList container = entry.getValue();
                int containerIndex = entry.getKey();
                Optional<Map<ChunkReference, Chunk>> chunks
                        = chunkClient.apply(httpClient, store, container, containerIndex);
                if (!chunks.isPresent()) {
                    return Optional.empty();
                }
                map.putAll(chunks.get());
            }
            return Optional.of(map);

        } catch (IOException ex) {
            logger.warn("-- fetch() - {} {}", ex.getClass().getCanonicalName(), ex.getMessage());
            logger.trace("-- fetch() - IOException: ", ex);
            return Optional.empty();
        }
    }
}
