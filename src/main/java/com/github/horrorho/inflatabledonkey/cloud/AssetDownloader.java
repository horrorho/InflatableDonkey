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
import com.github.horrorho.inflatabledonkey.cloud.voodoo.StorageHostChunkListContainer;
import com.github.horrorho.inflatabledonkey.cloud.voodoo.Voodoo;
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.protocol.ChunkServer;
import com.google.protobuf.ByteString;
import java.util.Collection;
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

    private final ChunkEngine chunkEngine;

    public AssetDownloader(ChunkEngine chunkEngine) {
        this.chunkEngine = Objects.requireNonNull(chunkEngine);
    }

    public void get(
            HttpClient httpClient,
            Collection<Asset> assets,
            ChunkServer.FileGroups fileGroups,
            BiConsumer<Asset, List<Chunk>> consumer) {

        fileGroups.getFileGroupsList()
                .forEach(fileGroup -> get(httpClient, assets, fileGroup, consumer));
    }

    public void get(
            HttpClient httpClient,
            Collection<Asset> assets,
            ChunkServer.FileChecksumStorageHostChunkLists fileGroup,
            BiConsumer<Asset, List<Chunk>> consumer) {

        FileSignatureAssets fileSignatureAssets = FileSignatureAssets.create(assets);
        Voodoo voodoo = new Voodoo(fileGroup);

        get(httpClient, fileSignatureAssets, voodoo, consumer);
    }

    void get(
            HttpClient httpClient,
            FileSignatureAssets fileSignatureAssets,
            Voodoo voodoo,
            BiConsumer<Asset, List<Chunk>> consumer) {

        Function<ChunkServer.ChunkReference, Optional<byte[]>> getKeyEncryptionKey
                = chunkReference -> voodoo.fileSignature(chunkReference)
                .flatMap(fileSignatureAssets::asset)
                .flatMap(Asset::keyEncryptionKey);

        fileSignatureAssets.assets()
                .forEach(asset -> get(httpClient, getKeyEncryptionKey, asset, voodoo, consumer));
    }

    void get(
            HttpClient httpClient,
            Function<ChunkServer.ChunkReference, Optional<byte[]>> getKeyEncryptionKey,
            Asset asset,
            Voodoo voodoo,
            BiConsumer<Asset, List<Chunk>> consumer) {

        Map<ChunkServer.ChunkReference, Chunk> chunkData
                = fetchChunkData(httpClient, getKeyEncryptionKey, asset, voodoo);

        assembleAssetChunkList(chunkData, asset, voodoo)
                .ifPresent(fileChunkList -> consumer.accept(asset, fileChunkList));
    }

    Map<ChunkServer.ChunkReference, Chunk> fetchChunkData(
            HttpClient httpClient,
            Function<ChunkServer.ChunkReference, Optional<byte[]>> getKeyEncryptionKey,
            Asset asset,
            Voodoo voodoo) {

        ByteString fileSignature = ByteString.copyFrom(asset.fileSignature().get());    // Filtered, get should be safe.
        Set<StorageHostChunkListContainer> storageHostChunkListContainer
                = voodoo.storageHostChunkListContainer(fileSignature);

        return chunkEngine.fetch(httpClient, storageHostChunkListContainer, getKeyEncryptionKey);
    }

    Optional<List<Chunk>>
            assembleAssetChunkList(Map<ChunkServer.ChunkReference, Chunk> chunkData, Asset asset, Voodoo voodoo) {

        ByteString fileSignature = ByteString.copyFrom(asset.fileSignature().get());    // Filtered, get should be safe.
        List<ChunkServer.ChunkReference> chunkReferenceList = voodoo.chunkReferenceList(fileSignature);
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
