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

import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.ChunkReference;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.StorageHostChunkList;
import com.google.protobuf.ByteString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ties in asset chunk references to storage host containers.
 *
 * @author Ahseya
 */
@Immutable
public final class Voodoo {

    private static final Logger logger = LoggerFactory.getLogger(Voodoo.class);

    static Map<ByteString, List<ChunkReference>> validate(Map<Integer, StorageHostChunkList> indexToContainer,
            Map<ByteString, List<ChunkReference>> fileSignatureToChunkReferences) {
        return fileSignatureToChunkReferences.entrySet()
                .stream()
                .filter(e -> e
                        .getValue()
                        .stream()
                        .allMatch(u -> {
                            int containerIndex = (int) u.getContainerIndex();
                            if (!indexToContainer.containsKey(containerIndex)) {
                                logger.warn("-- validate() - bad container index: {} file signature: {}",
                                        containerIndex, e.getKey());
                                return false;
                            }
                            int chunkIndex = (int) u.getChunkIndex();
                            if (chunkIndex >= indexToContainer.get(containerIndex).getChunkInfoList().size()) {
                                logger.warn("-- validate() - bad chunk index: {} file signature: {}",
                                        chunkIndex, e.getKey());
                                return false;
                            }
                            return true;
                        }))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private final Map<Integer, StorageHostChunkList> indexToContainer;
    private final Map<ByteString, List<ChunkReference>> fileSignatureToChunkReferences;

    public Voodoo(Map<Integer, StorageHostChunkList> indexToContainer,
            Map<ByteString, List<ChunkReference>> fileSignatureToChunkReferences) {
        this.indexToContainer = new HashMap<>(indexToContainer);
        this.fileSignatureToChunkReferences = validate(indexToContainer, fileSignatureToChunkReferences);
    }

    public Map<Integer, StorageHostChunkList> indexToContainer() {
        return new HashMap<>(indexToContainer);
    }

    public Optional<Map<Integer, StorageHostChunkList>> indexToContainer(ByteString fileSignature) {
        if (!has(fileSignature)) {
            return Optional.empty();
        }
        Map<Integer, StorageHostChunkList> shcls = fileSignatureToChunkReferences.get(fileSignature)
                .stream()
                .map(u -> (int) u.getContainerIndex())
                .distinct()
                .collect(toMap(Function.identity(), indexToContainer::get));
        return Optional.of(shcls);
    }

    public List<StorageHostChunkList> containers() {
        return new ArrayList<>(indexToContainer.values());
    }

    public Set<ByteString> fileSignatures() {
        return new HashSet<>(fileSignatureToChunkReferences.keySet());
    }

    public boolean has(ByteString fileSignature) {
        return fileSignatureToChunkReferences.containsKey(fileSignature);
    }

    public Map<ByteString, List<ChunkReference>> fileSignatureToChunkReferences() {
        return new HashMap<>(fileSignatureToChunkReferences);
    }

    public Optional<List<ChunkReference>> chunkReferences(ByteString fileSignature) {
        return Optional.ofNullable(fileSignatureToChunkReferences.get(fileSignature));
    }

    public Map<ByteString, List<ByteString>> fileSignatureToChunkChecksumList() {
        return fileSignatureToChunkReferences
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, e -> checksums(e.getValue())));
    }

    private List<ByteString> checksums(List<ChunkReference> chunkReferences) {
        return chunkReferences
                .stream()
                .map(this::checksum)
                .collect(toList());
    }

    private ByteString checksum(ChunkReference chunkReference) {
        // Boundaries are checked/ filtered in constructor.
        return indexToContainer
                .get((int) chunkReference.getContainerIndex())
                .getChunkInfo((int) chunkReference.getChunkIndex())
                .getChunkChecksum();
    }

    public Optional<List<ByteString>> chunkChecksumList(ByteString fileSignature) {
        return chunkReferences(fileSignature)
                .map(u -> u.stream()
                        .map(v -> indexToContainer // Boundaries checked/ filtered in constructor.
                                .get((int) v.getContainerIndex())
                                .getChunkInfo((int) v.getChunkIndex())
                                .getChunkChecksum())
                        .collect(toList()));
    }

    public Optional<StorageHostChunkList> container(int containerIndex) {
        return Optional.ofNullable(indexToContainer.get(containerIndex));
    }

    public Optional<List<StorageHostChunkList>> containers(ByteString fileSignature) {
        return chunkReferences(fileSignature)
                .map(u -> u.stream()
                        .map(v -> indexToContainer // Boundaries checked/ filtered in constructor.
                                .get((int) v.getContainerIndex()))
                        .collect(toList()));
    }

    @Override
    public String toString() {
        return "Voodoo{"
                + "indexToContainer=" + indexToContainer
                + ", fileSignatureToChunkReferences=" + fileSignatureToChunkReferences
                + '}';
    }
}
