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

import com.github.horrorho.inflatabledonkey.chunk.engine.ChunkKeys;
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.FileGroups;
import com.google.protobuf.ByteString;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class AuthorizedAssetFactory
        implements BiFunction<FileGroups, Map<ByteString, Asset>, List<AuthorizedAsset<Asset>>> {

    public static AuthorizedAssetFactory defaultInstance() {
        return DEFAULT_INSTANCE;
    }
    private static final Logger logger = LoggerFactory.getLogger(AuthorizedAssetFactory.class);

    private static final AuthorizedAssetFactory DEFAULT_INSTANCE
            = new AuthorizedAssetFactory((t, u) -> VoodooChunkEncryptionKeyMapper.map(t, ChunkKeys.instance(), u));

    private final BiFunction<Voodoo, Map<ByteString, byte[]>, Voodoo> voodooKeyMapper;

    public AuthorizedAssetFactory(BiFunction<Voodoo, Map<ByteString, byte[]>, Voodoo> voodooKeyMapper) {
        this.voodooKeyMapper = Objects.requireNonNull(voodooKeyMapper);
    }

    @Override
    public List<AuthorizedAsset<Asset>> apply(FileGroups fileGroups, Map<ByteString, Asset> fileSignatureToAsset) {
        Map<ByteString, byte[]> keys = fileSignatureToKeyEncryptionKey(fileSignatureToAsset);
        return VoodooFactory.from(fileGroups)
                .stream()
                .map(u -> voodooKeyMapper.apply(u, keys))
                .map(this::authorizedAssets)
                .flatMap(Collection::stream)
                .map(u -> mapAuthorizedAsset(fileSignatureToAsset, u))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    List<AuthorizedAsset<ByteString>> authorizedAssets(Voodoo voodoo) {
        try {
            return voodoo.fileSignatures()
                    .stream()
                    .map(u -> new AuthorizedAsset<>(u, voodoo.containers(u).get(), voodoo.chunkChecksumList(u).get()))
                    .collect(Collectors.toList());
        } catch (NoSuchElementException ex) {
            // Shouldn't happen.
            logger.error("-- authorizedAssets() - NoSuchElementException: ", ex);
            return Collections.emptyList();
        }
    }

    Optional<AuthorizedAsset<Asset>>
            mapAuthorizedAsset(Map<ByteString, Asset> fileSignatureToAsset, AuthorizedAsset<ByteString> authorizedAsset) {
        return Optional.ofNullable(fileSignatureToAsset.get(authorizedAsset.asset()))
                .map(u -> new AuthorizedAsset<Asset>(u, authorizedAsset.containers(), authorizedAsset.chunkChecksumList()));
    }

    Map<ByteString, byte[]> fileSignatureToKeyEncryptionKey(Map<ByteString, Asset> fileSignatureToAsset) {
        return fileSignatureToAsset
                .entrySet()
                .stream()
                .filter(e -> e.getValue().keyEncryptionKey().isPresent())
                .map(e -> new SimpleImmutableEntry<>(e.getKey(), e.getValue().keyEncryptionKey().get()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
