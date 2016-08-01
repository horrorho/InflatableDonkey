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
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit;
import com.github.horrorho.inflatabledonkey.requests.AuthorizeGetRequestFactory;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
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
@Immutable
public final class AuthorizeAssets implements BiFunction<HttpClient, Collection<Asset>, Optional<AuthorizedAssets>> {

    public static AuthorizeAssets backupd() {
        return BACKUPD;
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthorizeAssets.class);

    private static final AuthorizeAssets BACKUPD = new AuthorizeAssets("com.apple.backup.ios");

    private static final long TIMESTAMP_TOLERANCE = 15 * 60 * 1000;

    private static final ResponseHandler<ChunkServer.FileGroups> RESPONSE_HANDLER
            = new AuthorizeAssetsResponseHandler(TIMESTAMP_TOLERANCE);

    private final String container;
    private final String zone;

    public AuthorizeAssets(String container, String zone) {
        this.container = Objects.requireNonNull(container, "container");
        this.zone = Objects.requireNonNull(zone, "zone");
    }

    public AuthorizeAssets(String container) {
        this(container, "_defaultZone");
    }

    @Override
    public Optional<AuthorizedAssets> apply(HttpClient httpClient, Collection<Asset> assets) {
        Map<ByteString, Asset> fileSignatureToAssets = fileSignatureToAssets(assets);
        List<CloudKit.Asset> ckAssets = ckAssets(fileSignatureToAssets.values());
        if (ckAssets.isEmpty()) {
            return Optional.of(AuthorizedAssets.empty());
        }

        // Only expecting one dsPrsID/ contentBaseUrl.
        Set<String> dsPrsIDs = ckAssets.stream()
                .collect(Collectors.groupingBy(u -> u.getDsPrsID()))
                .keySet();
        if (dsPrsIDs.size() != 1) {
            logger.warn("-- authorize() - unexpected dsPrsID count: {}", dsPrsIDs);
        }

        Set<String> contentBaseUrls = ckAssets.stream()
                .collect(Collectors.groupingBy(u -> u.getContentBaseURL()))
                .keySet();
        if (contentBaseUrls.size() != 1) {
            logger.warn("-- authorize() - unexpected contentBaseUrl count: {}", contentBaseUrls);
        }
        if (ckAssets.isEmpty()) {
            logger.warn("-- authorize() - no ckAssets");
            return Optional.of(AuthorizedAssets.empty());
        }
        if (dsPrsIDs.isEmpty()) {
            logger.warn("-- authorize() - no dsPrsID");
            return Optional.of(AuthorizedAssets.empty());
        }

        String dsPrsID = ckAssets.get(0).getDsPrsID();
        String contentBaseUrl = ckAssets.get(0).getContentBaseURL();
        CloudKit.FileTokens fileTokens = FileTokensFactory.from(ckAssets);

        return fileGroups(httpClient, dsPrsID, contentBaseUrl, fileTokens)
                .map(u -> new AuthorizedAssets(u, fileSignatureToAssets));
    }

    Map<ByteString, Asset> fileSignatureToAssets(Collection<Asset> assets) {
        // Filter out empty files/ files that cannot be re-assembled.
        return assets.stream()
                .filter(u -> u.fileSignature().isPresent())
                .filter(u -> u.size().map(s -> s > 0).orElse(false))
                .filter(u -> u.keyEncryptionKey().isPresent())
                .filter(u -> u.domain().isPresent())
                .filter(u -> u.relativePath().isPresent())
                .collect(Collectors.toMap(
                        u -> ByteString.copyFrom(u.fileSignature().get()),
                        Function.identity(),
                        (u, v) -> {
                            logger.warn("-- fileSignatureToAssets() - file signature collision: {} {}", u, v);
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

    Optional<ChunkServer.FileGroups>
            fileGroups(HttpClient httpClient, String dsPrsID, String contentBaseUrl, CloudKit.FileTokens fileTokens) {

        return AuthorizeGetRequestFactory.instance().newRequest(dsPrsID, contentBaseUrl, container, zone, fileTokens)
                .flatMap(u -> execute(httpClient, u));
    }

    Optional<ChunkServer.FileGroups> execute(HttpClient httpClient, HttpUriRequest request) {
        try {
            return Optional.ofNullable(httpClient.execute(request, RESPONSE_HANDLER));
        } catch (IOException ex) {
            logger.warn("-- execute() - {} {}", ex.getClass().getCanonicalName(), ex.getMessage());
            return Optional.empty();
        }
    }
}
