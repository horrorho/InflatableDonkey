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

import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.ChunkInfo;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.StorageHostChunkList;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import net.jcip.annotations.NotThreadSafe;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.util.stream.Collectors.toMap;

/**
 *
 * @author Ahseya
 */
@NotThreadSafe
public class AssetPool {

    private static final Logger logger = LoggerFactory.getLogger(AssetPool.class);

    private final AuthorizeAssetsClient authorizeAssets;
    private final VoodooChunkEncryptionKeyMapper keyMapper;
    private Set<Asset> assets;
    private ItemElements<Asset, ByteString> assetChunks = null;

    public AssetPool(AuthorizeAssetsClient authorizeAssets, VoodooChunkEncryptionKeyMapper keyMapper,
            Set<Asset> assets) {
        this.authorizeAssets = Objects.requireNonNull(authorizeAssets);
        this.keyMapper = Objects.requireNonNull(keyMapper);
        this.assets = assets.stream()
                .filter(u -> u.fileSignature().isPresent())
                .collect(toSet());
    }

    public AssetPool(Set<Asset> assets) {
        this(AuthorizeAssetsClient.backupd(), VoodooChunkEncryptionKeyMapper.defaultInstance(), assets);
    }

    Collection<StorageHostChunkList> authorize(HttpClient httpClient) throws IOException {
        Set<Asset> a = assets == null
                ? assetChunks.items()
                : assets;
        Map<ByteString, Asset> fileSignatureToAsset
                = a.stream().collect(toMap(u -> ByteString.copyFrom(u.fileSignature().get()), Function.identity()));
        return authorize(httpClient, fileSignatureToAsset);
    }

    Collection<StorageHostChunkList>
            authorize(HttpClient httpClient, Map<ByteString, Asset> fileSignatureToAsset) throws IOException {
        Function<ByteString, Optional<byte[]>> keyEncryptionKey
                = u -> Optional.ofNullable(fileSignatureToAsset.get(u)).flatMap(Asset::keyEncryptionKey);

        List<Voodoo> voodooList = authorizeAssets.apply(httpClient, fileSignatureToAsset.values())
                .stream()
                .map(VoodooFactory::from)
                .flatMap(Collection::stream)
                .map(u -> keyMapper.apply(u, keyEncryptionKey))
                .collect(toList());

        Map<Asset, List<ByteString>> assetToChunkChecksumList = voodooList.stream()
                .map(Voodoo::fileSignatureToChunkChecksumList)
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .filter(e -> {
                    if (fileSignatureToAsset.containsKey(e.getKey())) {
                        return true;
                    }
                    logger.warn("-- authorize() - unreferenced signature: {}", e.getKey());
                    return false;
                })
                .map(e -> new SimpleImmutableEntry<>(fileSignatureToAsset.get(e.getKey()), e.getValue()))
                .collect(toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (u, v) -> {
                            if (!u.equals(v)) {
                                logger.warn("-- authorize() - collision: {} {}", u, v);
                            }
                            return u;
                        }));

        assetChunks = new ItemElements<>(assetToChunkChecksumList);
        assets = null;

        return voodooList.stream()
                .map(Voodoo::containers)
                .collect(ArrayList::new, List::addAll, List::addAll);
    }

    public Map<Asset, Optional<List<ByteString>>>
            put(StorageHostChunkList container, Collection<ByteString> chunkChecksums) {
        if (assetChunks == null) {
            throw new IllegalStateException("not authorized");
        }
        Set<ByteString> missing = container.getChunkInfoList()
                .stream()
                .map(ChunkInfo::getChunkChecksum)
                .collect(toSet());
        missing.removeAll(chunkChecksums);

        Map<Asset, Optional<List<ByteString>>> map = putElements(chunkChecksums);
        map.putAll(voidElements(missing));
        return map;
    }

    Map<Asset, Optional<List<ByteString>>> putElements(Collection<ByteString> chunkChecksums) {
        return assetChunks.putElements(chunkChecksums)
                .entrySet()
                .stream()
                .map(e -> new SimpleImmutableEntry<>(e.getKey(), Optional.of(e.getValue())))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    Map<Asset, Optional<List<ByteString>>> voidElements(Collection<ByteString> chunkChecksums) {
        return assetChunks.voidElements(chunkChecksums)
                .stream()
                .collect(toMap(Function.identity(), u -> Optional.empty()));
    }

    public boolean isEmpty() {
        return false;
    }
}
