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
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@ThreadSafe
public class AssetPool {

    static Set<Asset> validate(Set<Asset> assets) {
        return assets.stream()
                .filter(u -> {
                    if (u.fileSignature().isPresent()) {
                        return true;
                    }
                    logger.warn("-- validate() - missing file signature: {}", u);
                    return false;
                })
                .collect(toSet());
    }

    private static final Logger logger = LoggerFactory.getLogger(AssetPool.class);

    private final Object lock;
    private final AuthorizeAssetsClient authorizeAssets;
    private final VoodooChunkEncryptionKeyMapper keyMapper;
    private Set<Asset> assets;
    private ItemElements<Asset, ByteString> assetChunks = null;

    public AssetPool(Object lock, AuthorizeAssetsClient authorizeAssets, VoodooChunkEncryptionKeyMapper keyMapper,
            Set<Asset> assets) {
        this.lock = Objects.requireNonNull(lock);
        this.authorizeAssets = Objects.requireNonNull(authorizeAssets);
        this.keyMapper = Objects.requireNonNull(keyMapper);
        this.assets = validate(assets);
    }

    public AssetPool(AuthorizeAssetsClient authorizeAssets, VoodooChunkEncryptionKeyMapper keyMapper, Set<Asset> assets) {
        this(new Object(), authorizeAssets, keyMapper, assets);
    }

    public AssetPool(Set<Asset> assets) {
        this(AuthorizeAssetsClient.backupd(), VoodooChunkEncryptionKeyMapper.defaultInstance(), assets);
    }

    public Collection<StorageHostChunkList> authorize(HttpClient httpClient) throws IOException {
        synchronized (lock) {
            logger.trace("<< authorize()");
            Set<Asset> a = assets == null
                    ? assetChunks.items()
                    : assets;
            Map<ByteString, Asset> fileSignatureToAsset
                    = a.stream().collect(toMap(u -> ByteString.copyFrom(u.fileSignature().get()), Function.identity()));
            Collection<StorageHostChunkList> containers = authorize(httpClient, fileSignatureToAsset);
            logger.trace(">> authorize() - containers: {}", containers.size());
            return containers;
        }
    }

    @GuardedBy("lock")
    Collection<StorageHostChunkList>
            authorize(HttpClient httpClient, Map<ByteString, Asset> fileSignatureToAsset)
            throws IOException {

        List<Voodoo> voodooList = voodoos(httpClient, fileSignatureToAsset);
        assetChunks = assetChunks(voodooList, fileSignatureToAsset);
        assets = null;
        return voodooList.stream()
                .map(Voodoo::containers)
                .collect(ArrayList::new, List::addAll, List::addAll);
    }

    List<Voodoo> voodoos(HttpClient httpClient, Map<ByteString, Asset> fileSignatureToAsset) throws IOException {
        Function<ByteString, Optional<byte[]>> keyEncryptionKey
                = u -> Optional.ofNullable(fileSignatureToAsset.get(u)).flatMap(Asset::keyEncryptionKey);

        return authorizeAssets.apply(httpClient, fileSignatureToAsset.values())
                .stream()
                .map(VoodooFactory::from)
                .flatMap(Collection::stream)
                .map(u -> keyMapper.apply(u, keyEncryptionKey))
                .collect(toList());
    }

    ItemElements<Asset, ByteString>
            assetChunks(List<Voodoo> voodoos, Map<ByteString, Asset> fileSignatureToAsset) {
        Map<Asset, List<ByteString>> map = voodoos.stream()
                .map(Voodoo::fileSignatureToChunkChecksumList)
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .filter(e -> {
                    if (fileSignatureToAsset.containsKey(e.getKey())) {
                        return true;
                    }
                    logger.warn("-- assetChunks() - unreferenced signature: {}", e.getKey());
                    return false;
                })
                .map(e -> new SimpleImmutableEntry<>(fileSignatureToAsset.get(e.getKey()), e.getValue()))
                .collect(toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (u, v) -> {
                            if (!u.equals(v)) {
                                logger.warn("-- assetChunks() - collision: {} {}", u, v);
                            }
                            return u;
                        }));
        return new ItemElements<>(map);
    }

    public Map<Asset, Optional<List<ByteString>>>
            put(StorageHostChunkList container, Collection<ByteString> chunkChecksums) {
        synchronized (lock) {
            if (assetChunks == null) {
                throw new IllegalStateException("not authorized");
            }
            Set<ByteString> missing = container.getChunkInfoList()
                    .stream()
                    .map(ChunkInfo::getChunkChecksum)
                    .collect(toSet());
            missing.removeAll(chunkChecksums);
            logger.debug("-- debug() - missing: {}", missing);

            Map<Asset, Optional<List<ByteString>>> map = putElements(chunkChecksums);
            map.putAll(voidElements(missing));
            return map;
        }
    }

    @GuardedBy("lock")
    Map<Asset, Optional<List<ByteString>>> putElements(Collection<ByteString> chunkChecksums) {
        logger.trace("<< putElements() - chunk checksums: {}", chunkChecksums);
        Map<Asset, Optional<List<ByteString>>> map = assetChunks.putElements(chunkChecksums)
                .entrySet()
                .stream()
                .map(e -> new SimpleImmutableEntry<>(e.getKey(), Optional.of(e.getValue())))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        logger.trace(">> putElements() - assets: {}", map.size());
        return map;
    }

    @GuardedBy("lock")
    Map<Asset, Optional<List<ByteString>>> voidElements(Collection<ByteString> chunkChecksums) {
        logger.trace("<< voidElements() - chunk checksums: {}", chunkChecksums);
        Map<Asset, Optional<List<ByteString>>> map = assetChunks.voidElements(chunkChecksums)
                .stream()
                .collect(toMap(Function.identity(), u -> Optional.<List<ByteString>>empty()));
        logger.trace(">> voidElements() - assets: {}", map.size());
        return map;
    }

    public boolean isEmpty() {
        synchronized (lock) {
            return assets == null
                    ? assetChunks.isEmpty()
                    : assets.isEmpty();
        }
    }
}
