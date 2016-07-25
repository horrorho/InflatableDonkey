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
import com.google.protobuf.ByteString;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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

    private final ChunkServer.FileGroups fileGroups;
    private final Map<ByteString, Asset> fileSignatureToAssets;

    public AuthorizedAssets(ChunkServer.FileGroups fileGroups, Map<ByteString, Asset> fileSignatureToAssets) {
        this.fileGroups = Objects.requireNonNull(fileGroups);
        this.fileSignatureToAssets = new HashMap<>(fileSignatureToAssets);
    }

    public Optional<Asset> asset(ByteString fileSignature) {
        return Optional.ofNullable(fileSignatureToAssets.get(fileSignature));
    }

    public Map<ByteString, Asset> fileSignatureToAssets() {
        return new HashMap<>(fileSignatureToAssets);
    }

    public ChunkServer.FileGroups fileGroups() {
        return fileGroups;
    }

    @Override
    public String toString() {
        return "AuthorizedAssets{"
                + "fileGroups=" + fileGroups
                + ", fileSignatureToAssets=" + fileSignatureToAssets
                + '}';
    }
}
