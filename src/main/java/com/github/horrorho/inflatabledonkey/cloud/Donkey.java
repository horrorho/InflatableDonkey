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
import com.github.horrorho.inflatabledonkey.exception.UncheckedInterruptedException;
import com.github.horrorho.inflatabledonkey.file.FileAssembler;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.ChunkInfo;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.StorageHostChunkList;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import static java.util.stream.Collectors.toList;
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

    private final ChunkClient chunkClient;
    private final ChunkStore store;
    private final int fragmentationThreshold;

    public Donkey(ChunkClient chunkClient, ChunkStore store, int fragmentationThreshold) {
        this.chunkClient = Objects.requireNonNull(chunkClient);
        this.store = Objects.requireNonNull(store);
        this.fragmentationThreshold = fragmentationThreshold;
    }

    public void apply(HttpClient httpClient, Optional<ForkJoinPool> aux, Set<Asset> assets, FileAssembler consumer) {
        logger.trace("<< apply() - assets: {}", assets.size());
        if (assets.isEmpty()) {
            return;
        }

        if (logger.isDebugEnabled()) {
            int bytes = assets.stream()
                    .mapToInt(u -> u.size().map(Long::intValue).orElse(0))
                    .sum();
            logger.debug("-- apply() - assets total: {} size (bytes): {}", assets.size(), bytes);
        }

        AssetPool pool = new AssetPool(assets);

        while (true) {
            try {
                if (assets.size() > fragmentationThreshold && aux.isPresent()) {
                    processConcurrent(httpClient, aux.get(), pool, consumer);
                } else {
                    process(httpClient, pool, consumer);
                }
                break;
            } catch (IllegalStateException ex) {
                // Our StorageHostChunkLists have expired.
                // TOFIX potentially unsafe. Use a more specific exception.
                logger.debug("-- apply() - IllegalStateException: ", ex);
            } catch (IllegalArgumentException | IOException ex) {
                logger.warn("-- apply() - {} {}", ex.getClass().getCanonicalName(), ex.getMessage());
                break;
            }
        }
        logger.trace(">> apply() - pool empty: {}", pool.isEmpty());
    }

    void process(HttpClient httpClient, AssetPool pool, FileAssembler consumer) throws IOException {
        logger.trace("<< process()");
        pool.authorize(httpClient).forEach(u -> processContainer(httpClient, u, pool, consumer));
        logger.trace(">> process()");
    }

    void processConcurrent(HttpClient httpClient, ForkJoinPool fjp, AssetPool pool, FileAssembler consumer)
            throws IOException {
        logger.trace("<< processConcurrent()");
        try {
            Collection<StorageHostChunkList> containers = pool.authorize(httpClient);
            fjp.submit(() -> containers.parallelStream().forEach(u -> processContainer(httpClient, u, pool, consumer)))
                    .get();

        } catch (InterruptedException ex) {
            throw new UncheckedInterruptedException(ex);
        } catch (ExecutionException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw new RuntimeException(cause);
        }
        logger.trace("<< processConcurrent()");
    }

    void processContainer(HttpClient httpClient, StorageHostChunkList container, AssetPool pool, FileAssembler consumer) {
        pool.put(container, fetchContainer(httpClient, container))
                .forEach((k, v) -> consumer.accept(k, chunks(v)));
    }

    Set<ByteString> fetchContainer(HttpClient httpClient, StorageHostChunkList container) {
        ChunkServer.HostInfo hostInfo = container.getHostInfo();
        logger.trace("<< fetchContainer() - uri: {}", hostInfo.getHostname() + "/" + hostInfo.getUri());
        if (logger.isDebugEnabled()) {
            int size = container.getChunkInfoList()
                    .stream()
                    .mapToInt(ChunkInfo::getChunkLength)
                    .sum();
            logger.debug("-- fetchContainer() - chunks: {} size: {}", container.getChunkInfoCount(), size);
        }

        try {
            Set<ByteString> chunkChecksums = chunkClient.apply(httpClient, store, container)
                    .stream()
                    .map(Chunk::checksum)
                    .map(ByteString::copyFrom)
                    .collect(toSet());
            logger.trace(">> fetchContainer() - chunk checksums: {}", chunkChecksums);
            return chunkChecksums;

        } catch (IOException ex) {
            logger.warn("-- fetchContainer() - {} {}", ex.getClass().getCanonicalName(), ex.getMessage());
            return Collections.emptySet();
        }
    }

    Optional<List<Chunk>> chunks(Optional<List<ByteString>> chunkChecksumList) {
        try {
            return chunkChecksumList
                    .map(us -> us
                    .stream()
                    .map(ByteString::toByteArray)
                    .map(store::chunk)
                    .map(Optional::get)
                    .collect(toList()));

        } catch (NoSuchElementException ex) {
            logger.warn("-- chunks() - NoSuchElementException: {}", ex.getMessage());
            return Optional.empty();
        }
    }
}
