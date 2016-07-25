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
import com.github.horrorho.inflatabledonkey.responsehandler.InputStreamResponseHandler;
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
public final class AuthorizeAssets {

    public static AuthorizeAssets backupd() {
        return BACKUPD;
    }

    private static final AuthorizeAssets BACKUPD = new AuthorizeAssets("com.apple.backup.ios");

    private static final Logger logger = LoggerFactory.getLogger(AuthorizeAssets.class);

    private static final ResponseHandler<ChunkServer.FileGroups> RESPONSE_HANDLER
            = new InputStreamResponseHandler<>(ChunkServer.FileGroups.PARSER::parseFrom);

    private final String container;
    private final String zone;

    public AuthorizeAssets(String container, String zone) {
        this.container = Objects.requireNonNull(container, "container");
        this.zone = Objects.requireNonNull(zone, "zone");
    }

    public AuthorizeAssets(String container) {
        this(container, "_defaultZone");
    }

    public AuthorizedAssets authorize(HttpClient httpClient, Collection<Asset> assets) throws UncheckedIOException {
        Map<ByteString, Asset> fileSignatureToAssets = fileSignatureToAssets(assets);
        List<CloudKit.Asset> ckAssets = ckAssets(fileSignatureToAssets.values());
        if (ckAssets.isEmpty()) {
            return AuthorizedAssets.empty();
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

        String dsPrsID = ckAssets.get(0).getDsPrsID();
        String contentBaseUrl = ckAssets.get(0).getContentBaseURL();
        CloudKit.FileTokens fileTokens = FileTokensFactory.from(ckAssets);

        ChunkServer.FileGroups fileGroups = authorizeGet(httpClient, dsPrsID, contentBaseUrl, fileTokens);

        return new AuthorizedAssets(fileGroups, fileSignatureToAssets);
    }

    Map<ByteString, Asset> fileSignatureToAssets(Collection<Asset> assets) {
        return assets.stream()
                .filter(u -> u.fileSignature().isPresent())
                .filter(u -> u.size().map(s -> s > 0).orElse(false))
                .filter(u -> u.encryptionKey().isPresent())
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

    ChunkServer.FileGroups
            authorizeGet(HttpClient httpClient, String dsPrsID, String contentBaseUrl, CloudKit.FileTokens fileTokens)
            throws UncheckedIOException {
        return AuthorizeGetRequestFactory.instance()
                .newRequest(dsPrsID, contentBaseUrl, container, zone, fileTokens)
                .map(u -> execute(httpClient, u, RESPONSE_HANDLER))
                .orElseGet(() -> {
                    logger.warn("-- authorizeGet() - no file groups");
                    return ChunkServer.FileGroups.newBuilder().build();
                });
    }

    <T> T execute(HttpClient httpClient, HttpUriRequest request, ResponseHandler<T> handler) {
        try {
            return httpClient.execute(request, handler);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
