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

import com.github.horrorho.inflatabledonkey.cloud.AssetDownloader;
import com.github.horrorho.inflatabledonkey.cloud.AuthorizeAssets;
import com.github.horrorho.inflatabledonkey.cloud.AuthorizedAssets;
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.file.FileAssembler;
import com.github.horrorho.inflatabledonkey.file.XFileKeyFactory;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
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

    void download(HttpClient httpClient, List<Asset> assets, Path relativePath) throws UncheckedIOException {
        Path outputFolder = folder.resolve(relativePath);

        keyBagManager.update(httpClient, assets);

        XFileKeyFactory fileKeys = new XFileKeyFactory(keyBagManager::keyBag);
        FileAssembler fileAssembler = new FileAssembler(fileKeys, outputFolder);

        AuthorizedAssets authorizedAssets = authorizeAssets.authorize(httpClient, assets);

        assetDownloader.accept(httpClient, authorizedAssets, fileAssembler);
    }
}
// TODO time expiration tokens
