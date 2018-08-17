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
import com.github.horrorho.inflatabledonkey.io.IOBiFunction;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.FileGroups;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit;
import com.github.horrorho.inflatabledonkey.requests.AuthorizeGetRequestFactory;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import javax.annotation.concurrent.Immutable;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @author Ahseya
 */
@Immutable
public final class AuthorizeAssetsClient implements IOBiFunction<HttpClient, Collection<Asset>, List<FileGroups>> {

    public static AuthorizeAssetsClient backupd() {
        return BACKUPD;
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthorizeAssetsClient.class);

    private static final long FALLBACK_DURATION_MS = 55 * 60 * 1000;    // conservative 55 minutes (default: 60 minutes)

    private static final ResponseHandler<FileGroups> RESPONSE_HANDLER
            = new AuthorizeAssetsResponseHandler(FALLBACK_DURATION_MS);
    private static final AuthorizeAssetsClient BACKUPD = new AuthorizeAssetsClient("com.apple.backup.ios");

    private final ResponseHandler<FileGroups> responseHandler;
    private final String container;
    private final String zone;

    public AuthorizeAssetsClient(ResponseHandler<FileGroups> responseHandler, String container, String zone) {
        this.responseHandler = Objects.requireNonNull(responseHandler);
        this.container = Objects.requireNonNull(container);
        this.zone = Objects.requireNonNull(zone);
    }

    public AuthorizeAssetsClient(String container, String zone) {
        this(RESPONSE_HANDLER, container, zone);
    }

    public AuthorizeAssetsClient(String container) {
        this(container, "_defaultZone");
    }

    @Override
    public List<FileGroups> apply(HttpClient httpClient, Collection<Asset> assets) throws IOException {
        logger.trace("<< apply() - assets: {}", assets.size());
        try {
            List<CloudKit.Asset> ckAssets = ckAssets(assets);
            // Only expecting one contentBaseUrl. 
            // Can probably simplify code to handle only one.
            List<FileGroups> fileGroups = ckAssets.stream()
                    .collect(Collectors.groupingBy(CloudKit.Asset::getContentBaseURL))
                    .entrySet()
                    .stream()
                    .map(e -> fileGroups(httpClient, e.getKey(), e.getValue()))
                    .collect(toList());
            logger.trace(">> apply() - fileGroups: {}", fileGroups.size());
            return fileGroups;

        } catch (UncheckedIOException ex) {
            throw ex.getCause();
        }
    }

    List<CloudKit.Asset> ckAssets(Collection<Asset> assets) {
        return assets.stream()
                .map(Asset::asset)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    FileGroups fileGroups(HttpClient httpClient, String contentBaseUrl, Collection<CloudKit.Asset> ckAssets)
            throws UncheckedIOException {
        logger.debug("-- fileGroups() - contentBaseUrl: {} ckAssets: {}", contentBaseUrl, ckAssets.size());
        String dsPrsID = dsPrsID(ckAssets);
        CloudKit.FileTokens fileTokens = FileTokensFactory.from(ckAssets);
        return fileGroups(httpClient, dsPrsID, contentBaseUrl, fileTokens);
    }

    FileGroups fileGroups(HttpClient httpClient, String dsPrsID, String contentBaseUrl, CloudKit.FileTokens fileTokens)
            throws UncheckedIOException {
        try {
            HttpUriRequest request = AuthorizeGetRequestFactory.instance()
                    .newRequest(dsPrsID, contentBaseUrl, container, zone, fileTokens);
            return httpClient.execute(request, responseHandler);

        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    String dsPrsID(Collection<CloudKit.Asset> ckAssets) {
        return ckAssets
                .stream()
                .map(CloudKit.Asset::getOwner)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("missing dsPrsID"));
    }
}
