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
import com.github.horrorho.inflatabledonkey.chunk.engine.ChunkEngine;
import com.github.horrorho.inflatabledonkey.chunk.engine.SHCLContainer;
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer;
import java.util.Collection;
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

    private final ChunkEngine engine;

    public AssetDownloader(ChunkEngine engine) {
        this.engine = Objects.requireNonNull(engine);
    }

    public void accept(HttpClient httpClient, AuthorizedAssets authorizedAssets, BiConsumer<Asset, List<Chunk>> consumer) {
        accept(httpClient, VoodooFactory.from(authorizedAssets), consumer);
    }

    public void accept(HttpClient httpClient, Collection<Voodoo> voodoos, BiConsumer<Asset, List<Chunk>> consumer) {
        voodoos.forEach(v -> accept(httpClient, v, consumer));
    }

    public void accept(HttpClient httpClient, Voodoo voodoo, BiConsumer<Asset, List<Chunk>> consumer) {
        doAccept(httpClient, voodoo.containers(), voodoo.asset())
                .flatMap(u -> assemble(u, voodoo.chunkReferences()))
                .ifPresent(u -> consumer.accept(voodoo.asset(), u));
    }

    Optional<Map<ChunkServer.ChunkReference, Chunk>>
            doAccept(HttpClient httpClient, List<SHCLContainer> containers, Asset asset) {
        return asset.keyEncryptionKey()
                .filter(u -> !containers.isEmpty())
                .map(u -> fetch(httpClient, containers, u))
                .orElseGet(() -> {
                    logger.warn("-- doAccept() - failed to fetch asset: {}", asset.assetID());
                    return Optional.empty();
                });
    }

    Optional<Map<ChunkServer.ChunkReference, Chunk>>
            fetch(HttpClient httpClient, List<SHCLContainer> containers, byte[] kek) {
        Map<SHCLContainer, byte[]> containersKeyEncryptionKeys
                = containers.stream().collect(Collectors.toMap(Function.identity(), u -> kek));
        return engine.fetch(httpClient, containersKeyEncryptionKeys);
    }

    Optional<List<Chunk>>
            assemble(Map<ChunkServer.ChunkReference, Chunk> map, List<ChunkServer.ChunkReference> references) {
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
