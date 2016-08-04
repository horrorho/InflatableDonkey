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
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import net.jcip.annotations.Immutable;
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
public final class AuthorizeAssetsClient
        implements IOBiFunction<HttpClient, Collection<Asset>, List<AuthorizedAsset<Asset>>> {

    public static AuthorizeAssetsClient backupd() {
        return BACKUPD;
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthorizeAssetsClient.class);

    private static final AuthorizeAssetsClient BACKUPD = new AuthorizeAssetsClient("com.apple.backup.ios");
    private static final long TIMESTAMP_TOLERANCE = 15 * 60 * 1000;

    private static final ResponseHandler<FileGroups> RESPONSE_HANDLER
            = new AuthorizeAssetsResponseHandler(TIMESTAMP_TOLERANCE);

    private final AuthorizedAssetFactory authorizedAssetFactory;
    private final String container;
    private final String zone;

    public AuthorizeAssetsClient(AuthorizedAssetFactory authorizedAssetFactory, String container, String zone) {
        this.authorizedAssetFactory = Objects.requireNonNull(authorizedAssetFactory);
        this.container = Objects.requireNonNull(container);
        this.zone = Objects.requireNonNull(zone);
    }

    public AuthorizeAssetsClient(String container, String zone) {
        this(AuthorizedAssetFactory.defaultInstance(), container, "_defaultZone");
    }

    public AuthorizeAssetsClient(String container) {
        this(container, "_defaultZone");
    }

    @Override
    public List<AuthorizedAsset<Asset>> apply(HttpClient httpClient, Collection<Asset> assets) throws IOException {
        try {
            Map<ByteString, Asset> downloadables = downloadables(assets);
            List<CloudKit.Asset> ckAssets = ckAssets(downloadables.values());

            // Only expecting one contentBaseUrl. 
            // Can probably simplify code to handle only one.
            return ckAssets.stream()
                    .collect(Collectors.groupingBy(CloudKit.Asset::getContentBaseURL))
                    .entrySet()
                    .stream()
                    .map(e -> fileGroups(httpClient, e.getKey(), e.getValue()))
                    .map(u -> authorizedAssetFactory.apply(u, downloadables))
                    .flatMap(Collection::stream)
                    .collect(toList());

        } catch (UncheckedIOException ex) {
            throw ex.getCause();
        }
    }

    Map<ByteString, Asset> downloadables(Collection<Asset> assets) {
        // Filter out non-downloadble assets.
        return assets.stream()
                .filter(u -> u.fileSignature().isPresent())
                .filter(u -> u.size().map(s -> s > 0).orElse(false))
                .filter(u -> u.keyEncryptionKey().isPresent())
                .filter(u -> u.domain().isPresent())
                .filter(u -> u.relativePath().isPresent())
                .collect(toMap(
                        u -> ByteString.copyFrom(u.fileSignature().get()),
                        Function.identity(),
                        (u, v) -> {
                            logger.warn("-- downloadables() - file signature collision: {} {}", u, v);
                            return u;
                        }));
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

        String dsPrsID = dsPrsID(ckAssets);
        CloudKit.FileTokens fileTokens = FileTokensFactory.from(ckAssets);
        return fileGroups(httpClient, dsPrsID, contentBaseUrl, fileTokens);
    }

    FileGroups fileGroups(HttpClient httpClient, String dsPrsID, String contentBaseUrl, CloudKit.FileTokens fileTokens)
            throws UncheckedIOException {
        try {
            HttpUriRequest request = AuthorizeGetRequestFactory.instance()
                    .newRequest(dsPrsID, contentBaseUrl, container, zone, fileTokens);
            return httpClient.execute(request, RESPONSE_HANDLER);

        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    String dsPrsID(Collection<CloudKit.Asset> ckAssets) {
        // Probably over-cautious approach, we could inject our account dsPrsID.
        // We expect only one dsPrsID value which corresponds to the account dsPrsID.
        Set<String> dsPrsIDs = ckAssets
                .stream()
                .map(CloudKit.Asset::getDsPrsID)
                .collect(toSet());
        if (dsPrsIDs.size() != 1) {
            logger.warn("-- dsPrsID() - unexpected dsPrsID count: {}", dsPrsIDs);
        }
        return dsPrsIDs.stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("missing dsPrsID"));
    }
}
