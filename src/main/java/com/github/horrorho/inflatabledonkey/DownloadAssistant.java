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
package com.github.horrorho.inflatabledonkey;

import com.github.horrorho.inflatabledonkey.chunk.Chunk;
import com.github.horrorho.inflatabledonkey.cloud.AssetDownloader;
import com.github.horrorho.inflatabledonkey.cloud.AuthorizeAssets;
import com.github.horrorho.inflatabledonkey.cloud.AuthorizedAssets;
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.file.FileAssembler;
import com.github.horrorho.inflatabledonkey.file.XFileKeyFactory;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import net.jcip.annotations.ThreadSafe;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DownloadAssistant.
 *
 * @author Ahseya
 */
@ThreadSafe
public final class DownloadAssistant {

    private static final Logger logger = LoggerFactory.getLogger(DownloadAssistant.class);

    private final AuthorizeAssets authorizeAssets;
    private final AssetDownloader assetDownloader;
    private final KeyBagManager keyBagManager;
    private final Path folder;

    public DownloadAssistant(
            AuthorizeAssets authorizeAssets,
            AssetDownloader assetDownloader,
            KeyBagManager keyBagManager,
            Path folder) {

        this.authorizeAssets = Objects.requireNonNull(authorizeAssets, "authorizeAssets");
        this.assetDownloader = Objects.requireNonNull(assetDownloader, "assetDownloader");
        this.keyBagManager = Objects.requireNonNull(keyBagManager, "keyBagManager");
        this.folder = Objects.requireNonNull(folder, "folder");
    }

    public void download(HttpClient httpClient, Set<Asset> assets, Path relativePath) {
        logger.trace("<< download() - assets: {}", assets.size());
        Path outputFolder = folder.resolve(relativePath);
        keyBagManager.update(httpClient, assets);
        XFileKeyFactory fileKeys = new XFileKeyFactory(keyBagManager::keyBag);
        FileAssembler fileAssembler = new FileAssembler(fileKeys, outputFolder);

        Set<Asset> remaining = new HashSet<>(assets);
        while (!remaining.isEmpty()) {
            Optional<AuthorizedAssets> authorized = authorizeAssets.apply(httpClient, remaining);
            if (!authorized.isPresent()) {
                logger.warn("-- download() - failed to authorize assets");
                return;
            }
            Set<Asset> attempted = doDownload(httpClient, authorized.get(), fileAssembler);
            remaining.removeAll(attempted);
        }
        logger.trace(">> download()");
    }

    Set<Asset> doDownload(HttpClient httpClient, AuthorizedAssets authorized, FileAssembler assembler) {
        logger.trace("<< doDownload() - authorized: {}", authorized.size());
        // Here we attempt each file once. We leave SHCL container retries to the http client retry handler.
        // Occasionally data servers report internal errors or are offline.
        Set<Asset> attempted = new HashSet<>();
        BiConsumer<Asset, Optional<List<Chunk>>> consumer
                = (asset, chunks) -> {
                    attempted.add(asset);
                    assembler.accept(asset, chunks);
                };

        try {
            assetDownloader.accept(httpClient, authorized, consumer);
        } catch (IllegalStateException ex) {
            // Consider creating a more specific exception for time expiration.
            logger.debug("-- doDownload() - IllegalStateException: ", ex);
        }
        logger.trace(">> doDownload() - attempted: {}", attempted.size());
        return attempted;
    }
}
