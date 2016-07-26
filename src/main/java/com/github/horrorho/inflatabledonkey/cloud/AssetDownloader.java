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
import com.github.horrorho.inflatabledonkey.chunk.Chunk;
import com.github.horrorho.inflatabledonkey.chunk.engine.ChunkClient;
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
import java.util.function.Function;
import java.util.stream.Collectors;
import net.jcip.annotations.ThreadSafe;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@ThreadSafe
public final class AssetDownloader {

    private static final Logger logger = LoggerFactory.getLogger(AssetDownloader.class);

    private final ChunkClient chunkClient;
    private final ChunkStore store;

    public AssetDownloader(ChunkClient chunkClient, ChunkStore store) {
        this.chunkClient = Objects.requireNonNull(chunkClient);
        this.store = Objects.requireNonNull(store);
    }

    public AssetDownloader(ChunkStore store) {
        this(ChunkClient.defaultInstance(), store);
    }

    public void accept(HttpClient httpClient, AuthorizedAssets authorizedAssets, BiConsumer<Asset, List<Chunk>> consumer) {
        accept(httpClient, VoodooFactory.from(authorizedAssets), consumer);
    }

    public void accept(HttpClient httpClient, Collection<Voodoo> voodoos, BiConsumer<Asset, List<Chunk>> consumer) {
        KeyEncryptionKeys keks = Voodoo.keyEncryptionKeys(voodoos);
        voodoos.forEach(v -> accept(httpClient, v, keks, consumer));
    }

    public boolean
            accept(HttpClient httpClient, Voodoo voodoo, KeyEncryptionKeys keks, BiConsumer<Asset, List<Chunk>> consumer) {
        return fetch(httpClient, keks, voodoo.containers(), voodoo.asset())
                .flatMap(u -> assemble(u, voodoo.chunkReferences()))
                .map(u -> {
                    consumer.accept(voodoo.asset(), u);
                    return true;
                })
                .orElseGet(() -> {
                    Asset asset = voodoo.asset();
                    String path = asset.domain().orElse("NULL") + asset.relativePath().orElse("NULL");
                    logger.warn("-- accept() - failed to download asset: {}", path);
                    return false;
                });
    }

    Optional<Map<ChunkReference, Chunk>>
            fetch(HttpClient httpClient, KeyEncryptionKeys keks, Map<Integer, StorageHostChunkList> containers,
                    Asset asset) {
        Map<ChunkReference, Chunk> map = new HashMap<>();
        for (Map.Entry<Integer, StorageHostChunkList> entry : containers.entrySet()) {
            Optional<Map<ChunkReference, Chunk>> chunks = keks.apply(entry.getValue())
                    .flatMap(kek -> fetch(httpClient, kek, entry.getValue(), entry.getKey()));
            if (!chunks.isPresent()) {
                return Optional.empty();
            }
            map.putAll(chunks.get());
        }
        return Optional.of(map);
    }

    Optional<Map<ChunkReference, Chunk>>
            fetch(HttpClient httpClient, Function<Integer, Optional<byte[]>> kek, StorageHostChunkList container, int index) {
        SHCLContainer shclContainer = new SHCLContainer(container, kek, index);
        return chunkClient.apply(httpClient, shclContainer, store);
    }

    Optional<List<Chunk>>
            assemble(Map<ChunkReference, Chunk> map, List<ChunkReference> references) {
        if (map.keySet().containsAll(references)) {
            logger.warn("-- assemble() - missing chunks");
            return Optional.empty();
        }

        List<Chunk> chunkList = references.stream()
                .map(map::get)
                .collect(Collectors.toList());
        return Optional.of(chunkList);
    }
}
