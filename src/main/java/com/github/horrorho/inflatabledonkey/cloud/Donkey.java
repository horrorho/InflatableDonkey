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
import com.github.horrorho.inflatabledonkey.chunk.store.ChunkStore;
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.file.FileAssembler;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.StorageHostChunkList;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toSet;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class Donkey {

    private static final Logger logger = LoggerFactory.getLogger(Donkey.class);

    private final AuthorizeAssetsClient authorizeAssetsClient;
    private final ChunkClient chunkClient;
    private final ChunkStore store;
    private final FileAssembler consumer;

    public Donkey(AuthorizeAssetsClient authorizeAssetsClient, ChunkClient chunkClient, ChunkStore store,
            FileAssembler consumer) {

        this.authorizeAssetsClient = Objects.requireNonNull(authorizeAssetsClient);
        this.chunkClient = Objects.requireNonNull(chunkClient);
        this.store = Objects.requireNonNull(store);
        this.consumer = Objects.requireNonNull(consumer);
    }

    public void apply(HttpClient httpClient, Collection<Asset> assets) {
        logger.trace("<< apply() - assets: {}", assets.size());
        if (assets.isEmpty()) {
            return;
        }

        List<AuthorizedAsset<Asset>> authorizedAssets = authorizeAssetsClient.apply(httpClient, assets);
        Set<Chunk> chunks = fetchAssets(httpClient, authorizedAssets);
        consumeAssets(authorizedAssets, chunks);

        // TODO IllegalStateException
        logger.trace(">> apply()");
    }

    Set<Chunk> fetchAssets(HttpClient httpClient, Collection<AuthorizedAsset<Asset>> authorizedAssets) {
        return authorizedAssets.stream()
                .map(AuthorizedAsset::containers)
                .flatMap(Collection::stream)
                .map(u -> fetchContainer(httpClient, u))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(Collection::stream)
                .collect(toSet());
    }

    Optional<Set<Chunk>> fetchContainer(HttpClient httpClient, StorageHostChunkList container) {
        logger.trace("<< fetchContainer() - container host info: {}", container.getHostInfo());
        try {
            Set<Chunk> chunks = chunkClient.apply(httpClient, store, container);
            logger.trace(">> fetchContainer() - chunks: {}", chunks);
            return Optional.of(chunks);

        } catch (IOException ex) {
            logger.warn(">> fetchContainer() - {} {}", ex.getClass().getCanonicalName(), ex.getMessage());
            return Optional.empty();
        }
    }

    void consumeAssets(Collection<AuthorizedAsset<Asset>> authorizedAssets, Set<Chunk> chunks) {
        Map<ByteString, Chunk> map = chunkMap(chunks);
        authorizedAssets.forEach(u -> consumeAsset(u, map));
    }

    void consumeAsset(AuthorizedAsset<Asset> authorizedAsset, Map<ByteString, Chunk> map) {
        if (map.keySet().containsAll(authorizedAsset.chunkChecksumList())) {
            List<Chunk> chunkList = authorizedAsset.chunkChecksumList()
                    .stream()
                    .map(map::get)
                    .collect(Collectors.toList());
            logger.debug("-- consumeAsset() - all chunks present: {}", authorizedAsset.asset().decription());
            consumer.accept(authorizedAsset.asset(), Optional.of(chunkList));

        } else {
            logger.debug("-- consumeAsset() - not all chunks present {}", authorizedAsset.asset().decription());
            consumer.accept(authorizedAsset.asset(), Optional.empty());
        }
    }

    Map<ByteString, Chunk> chunkMap(Set<Chunk> chunks) {
        return chunks.stream().collect(Collectors.toMap(u -> ByteString.copyFrom(u.checksum()), Function.identity()));
    }
}
