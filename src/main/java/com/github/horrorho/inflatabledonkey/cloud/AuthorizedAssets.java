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
import com.github.horrorho.inflatabledonkey.protocol.ChunkServer;
import com.google.protobuf.ByteString;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AuthorizedAssets.
 *
 * @author Ahseya
 */
@Immutable
public final class AuthorizedAssets {

    public static AuthorizedAssets empty() {
        return EMPTY;
    }

    private static final AuthorizedAssets EMPTY = new AuthorizedAssets(
            ChunkServer.FileGroups.newBuilder().build(),
            Collections.emptyMap());

    private static final Logger logger = LoggerFactory.getLogger(AuthorizedAssets.class);

    static Map<ByteString, List<Asset>> copy(Map<ByteString, List<Asset>> assets) {
        return assets.entrySet()
                .stream()
                .filter(e -> {
                    if (e.getValue().isEmpty()) {
                        logger.error("-- copy() - empty asset list: {}", e);
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new ArrayList<>(e.getValue())));
    }

    private final ChunkServer.FileGroups fileGroups;
    private final Map<ByteString, List<Asset>> assets;

    public AuthorizedAssets(
            ChunkServer.FileGroups fileGroups,
            Map<ByteString, List<Asset>> assets) {

        this.fileGroups = Objects.requireNonNull(fileGroups);
        this.assets = copy(assets);
    }

    public Optional<Asset> asset(ByteString fileSignature) {
        return Optional.ofNullable(assets.get(fileSignature))
                .map(l -> l.get(0));
    }

    public Optional<List<Asset>> assets(ByteString fileSignature) {
        return Optional.ofNullable(assets.get(fileSignature))
                .map(ArrayList::new);
    }

    public Collection<List<Asset>> assets() {
        return new ArrayList<>(assets.values());
    }

    public ChunkServer.FileGroups fileGroups() {
        return fileGroups;
    }

    @Override
    public String toString() {
        return "AuthorizedAssets{" + "fileGroups=" + fileGroups + ", assets=" + assets + '}';
    }
}
