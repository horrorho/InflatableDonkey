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
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import com.github.horrorho.inflatabledonkey.chunk.engine.ChunkKeyEncryptionKeys;

/**
 * Asset with it's ChunkServer.StorageHostChunkList/ ChunkServer.Reference data.
 *
 * @author Ahseya
 */
@Immutable
public final class Voodoo {

    public static ChunkKeyEncryptionKeys
            keyEncryptionKeys(Collection<Voodoo> voodoos) {
        Map<ChunkServer.StorageHostChunkList, Map<Integer, byte[]>> keks = voodoos.stream()
                .map(Voodoo::keyEncryptionKeys)
                .collect(HashMap::new, Map::putAll, Map::putAll);
        return container -> Optional.ofNullable(keks.get(container))
                .map(u -> index -> Optional.ofNullable(u.get(index)));
    }

    private final Asset asset;
    private final LinkedHashMap<ChunkServer.ChunkReference, ChunkServer.StorageHostChunkList> chunkReferences;

    public Voodoo(
            Asset asset,
            LinkedHashMap<ChunkServer.ChunkReference, ChunkServer.StorageHostChunkList> chunkReferences) {

        this.asset = Objects.requireNonNull(asset);
        this.chunkReferences = new LinkedHashMap<>(chunkReferences);
        asset.fileSignature()
                .map(ByteString::copyFrom)
                .orElseThrow(() -> new IllegalArgumentException("asset missing file signature"));
    }

    public Asset asset() {
        return asset;
    }

    public Map<ChunkServer.StorageHostChunkList, Map<Integer, byte[]>> keyEncryptionKeys() {
        return asset.keyEncryptionKey()
                .map(this::keyEncryptionKeys)
                .orElseGet(Collections::emptyMap);
    }

    Map<ChunkServer.StorageHostChunkList, Map<Integer, byte[]>> keyEncryptionKeys(byte[] kek) {
        return chunkReferences
                .entrySet()
                .stream()
                .map(e -> new SimpleImmutableEntry<>(e.getValue(), (int) e.getKey().getChunkIndex()))
                .collect(Collectors.groupingBy(e -> e.getKey(),
                        Collectors.toMap(Map.Entry::getValue, e -> kek, (u, v) -> u)));
    }

    public Map<Integer, ChunkServer.StorageHostChunkList> containers() {
        return chunkReferences.entrySet()
                .stream()
                .collect(Collectors.toMap(e -> (int) e.getKey().getContainerIndex(), Map.Entry::getValue));
    }

    public List<ChunkServer.ChunkReference> chunkReferences() {
        return new ArrayList<>(chunkReferences.keySet());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.asset);
        hash = 41 * hash + Objects.hashCode(this.chunkReferences);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Voodoo other = (Voodoo) obj;
        if (!Objects.equals(this.asset, other.asset)) {
            return false;
        }
        if (!Objects.equals(this.chunkReferences, other.chunkReferences)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Voodoo{"
                + ", asset=" + asset
                + ", chunkReferences=" + chunkReferences
                + '}';
    }
}
