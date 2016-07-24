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
import com.github.horrorho.inflatabledonkey.cloud.voodoo.SHCLContainer;
import com.github.horrorho.inflatabledonkey.cloud.voodoo.Voodoo;
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer;
import com.google.protobuf.ByteString;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.jcip.annotations.ThreadSafe;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AssetDownloader.
 *
 * @author Ahseya
 */
@ThreadSafe
public final class AssetDownloader {

    private static final Logger logger = LoggerFactory.getLogger(AssetDownloader.class);

    private final ChunkEngine engine;

    public AssetDownloader(ChunkEngine engine) {
        this.engine = Objects.requireNonNull(engine, "engine");
    }

    public void get(HttpClient httpClient, AuthorizedAssets assets, BiConsumer<Asset, List<Chunk>> consumer) {
        assets.fileGroups()
                .getFileGroupsList()
                .forEach(u -> get(httpClient, assets, u, consumer));
    }

    void get(HttpClient httpClient, AuthorizedAssets assets, ChunkServer.FileChecksumStorageHostChunkLists fileGroup,
            BiConsumer<Asset, List<Chunk>> consumer) {
        Voodoo voodoo = new Voodoo(fileGroup);
        get(httpClient, assets, voodoo, consumer);
    }

    void get(HttpClient httpClient, AuthorizedAssets assets, Voodoo voodoo, BiConsumer<Asset, List<Chunk>> consumer) {
        Function<ChunkServer.ChunkReference, Optional<byte[]>> kek = u -> keyEncryptionKey(voodoo, assets, u);
        assets.assets()
                .forEach(u -> get(httpClient, kek, u, voodoo, consumer));
    }

    Optional<byte[]>
            keyEncryptionKey(Voodoo voodoo, AuthorizedAssets assets, ChunkServer.ChunkReference chunkReference) {
        return voodoo.fileSignature(chunkReference)
                .flatMap(assets::asset)
                .flatMap(Asset::keyEncryptionKey);
    }

    void get(
            HttpClient httpClient,
            Function<ChunkServer.ChunkReference, Optional<byte[]>> kek,
            List<Asset> assetList,
            Voodoo voodoo,
            BiConsumer<Asset, List<Chunk>> consumer) {

        Asset primaryAsset = assetList.get(0);

        Map<ChunkServer.ChunkReference, Chunk> chunkData
                = fetchChunkData(httpClient, kek, primaryAsset, voodoo);

        assembleAssetChunkList(chunkData, primaryAsset, voodoo)
                .ifPresent(fileChunkList -> assetList.forEach(a -> consumer.accept(a, fileChunkList)));
    }

    Map<ChunkServer.ChunkReference, Chunk> fetchChunkData(
            HttpClient httpClient,
            Function<ChunkServer.ChunkReference, Optional<byte[]>> getKeyEncryptionKey,
            Asset asset,
            Voodoo voodoo) {

        ByteString fileSignature = ByteString.copyFrom(asset.fileSignature().get());    // Filtered, get should be safe.
        Set<SHCLContainer> storageHostChunkListContainer
                = voodoo.storageHostChunkListContainers(fileSignature);

        return engine.fetch(httpClient, storageHostChunkListContainer, getKeyEncryptionKey);
    }

    Optional<List<Chunk>>
            assembleAssetChunkList(Map<ChunkServer.ChunkReference, Chunk> chunkData, Asset asset, Voodoo voodoo) {

        ByteString fileSignature = ByteString.copyFrom(asset.fileSignature().get());    // Filtered, get should be safe.
        List<ChunkServer.ChunkReference> chunkReferenceList = voodoo.chunkReferenceList(fileSignature).get();
        if (!chunkData.keySet().containsAll(chunkReferenceList)) {
            logger.warn("-- assembleFileChunkList() - missing chunks: {}", asset);
            return Optional.empty();
        }

        List<Chunk> fileChunkList = chunkReferenceList.stream()
                .map(chunkData::get)
                .collect(Collectors.toList());

        return Optional.of(fileChunkList);
    }
}
