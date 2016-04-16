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

import com.github.horrorho.inflatabledonkey.FileTokensFactory;
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.protocol.ChunkServer;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import com.github.horrorho.inflatabledonkey.requests.AuthorizeGetRequestFactory;
import com.github.horrorho.inflatabledonkey.responsehandler.InputStreamResponseHandler;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AuthorizeAssets.
 *
 * @author Ahseya
 */
public final class AuthorizeAssets {

    private static final ResponseHandler<ChunkServer.FileGroups> RESPONSE_HANDLER
            = new InputStreamResponseHandler<>(ChunkServer.FileGroups.PARSER::parseFrom);

    private static final Logger logger = LoggerFactory.getLogger(AuthorizeAssets.class);

    private final String container;
    private final String zone;

    public AuthorizeAssets(String container, String zone) {
        this.container = Objects.requireNonNull(container, "container");
        this.zone = Objects.requireNonNull(zone, "zone");
    }

    public AuthorizeAssets(String container) {
        this(container, "_defaultZone");
    }

    public AuthorizedAssets authorize(HttpClient httpClient, Collection<Asset> assets) throws IOException {
        Map<ByteString, List<Asset>> downloadables = downloadables(assets);

        // Duplicate file signatures indicate duplicate files.
        // Primary asset is downloaded, others are copied.
        // Voodoo will is undefined for duplicate file signatures.
        List<Asset> primaryAssets = primaryAsset(downloadables);

        List<CloudKit.Asset> ckAssets = ckAssets(primaryAssets);

        if (ckAssets.isEmpty()) {
            return AuthorizedAssets.empty();
        }

        // Should be the same for all assets. Assumption not tested.
        String dsPrsID = ckAssets.get(0).getDsPrsID();
        String contentBaseUrl = ckAssets.get(0).getContentBaseURL();

        CloudKit.FileTokens fileTokens = FileTokensFactory.from(ckAssets);

        ChunkServer.FileGroups fileGroups = authorizeGet(httpClient, dsPrsID, contentBaseUrl, fileTokens);
        return new AuthorizedAssets(fileGroups, downloadables);
    }

    ChunkServer.FileGroups authorizeGet(
            HttpClient httpClient,
            String dsPrsID,
            String contentBaseUrl,
            CloudKit.FileTokens fileTokens) throws IOException {

        Optional<HttpUriRequest> fileGroups = AuthorizeGetRequestFactory.instance()
                .newRequest(dsPrsID, contentBaseUrl, container, zone, fileTokens);

        if (!fileGroups.isPresent()) {
            logger.warn("-- authorizeGet() - no file groups");
            return ChunkServer.FileGroups.newBuilder().build();
        }

        return httpClient.execute(fileGroups.get(), RESPONSE_HANDLER);
    }

    List<CloudKit.Asset> ckAssets(Collection<Asset> assets) {
        return assets.stream()
                .map(Asset::asset)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    List<Asset> primaryAsset(Map<ByteString, List<Asset>> fileSignaturetoAssetList) {
        return fileSignaturetoAssetList.entrySet()
                .stream().filter(e -> {
                    if (e.getValue().isEmpty()) {
                        logger.error("-- primaryAsset() - empty asset list: {}", e);
                        return false;
                    }
                    return true;
                })
                .map(e -> e.getValue()
                        .get(0))
                .collect(Collectors.toList());
    }

    Map<ByteString, List<Asset>> downloadables(Collection<Asset> assets) {
        return assets.stream()
                .filter(this::isDownloadable)
                .collect(Collectors.groupingBy(a -> ByteString.copyFrom(a.fileSignature().get())));
    }

    boolean isDownloadable(Asset asset) {
        // TODO verify then simplify/ remove logging statements
        if (asset.size() == 0) {
            logger.debug("-- isDownloadable() - empty asset: {}", asset);
            return false;
        }
        if (!asset.fileSignature().isPresent()) {
            logger.debug("-- isDownloadable() - no file signature: {}", asset);
            return false;
        }
        if (!asset.keyEncryptionKey().isPresent()) {
            logger.debug("-- isDownloadable() - no key decryption key: {}", asset);
            return false;
        }
        if (!asset.domain().isPresent()) {
            logger.debug("-- isDownloadable() - domain: {}", asset);
            return false;
        }
        if (!asset.relativePath().isPresent()) {
            logger.debug("-- isDownloadable() - no relative path: {}", asset);
            return false;
        }
        logger.debug("-- isDownloadable() - true: {}", asset);
        return true;
    }
}
