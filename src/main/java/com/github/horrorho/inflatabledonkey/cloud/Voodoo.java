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

import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.ChunkReference;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.StorageHostChunkList;
import com.google.protobuf.ByteString;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
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

    private final Map<Integer, StorageHostChunkList> indexToSHCL;
    private final Map<ByteString, List<ChunkReference>> fileSignatureToChunkReferences;

    public Voodoo(Map<Integer, StorageHostChunkList> indexToSHCL,
            Map<ByteString, List<ChunkReference>> fileSignatureToChunkReferences) {
        this.indexToSHCL = new HashMap<>(indexToSHCL);
        this.fileSignatureToChunkReferences = new HashMap<>(fileSignatureToChunkReferences);

        // Sanity check.
        fileSignatureToChunkReferences.forEach((k, v) -> v
                .stream()
                .forEach(u -> {
                    int containerIndex = (int) u.getContainerIndex();
                    if (!indexToSHCL.containsKey((int) u.getContainerIndex())) {
                        logger.warn("** Voodoo() - bad container index: {} file signature: {}", containerIndex, k);
                    }
                    List<ChunkServer.ChunkInfo> chunkInfoList = indexToSHCL.get(containerIndex).getChunkInfoList();
                    int chunkIndex = (int) u.getChunkIndex();
                    if (chunkIndex >= chunkInfoList.size()) {
                        logger.warn("** Voodoo() - bad chunk index: {} file signature: {}", chunkIndex, k);
                    }
                }));
    }

    public Map<Integer, StorageHostChunkList> indexToSHCL() {
        return new HashMap<>(indexToSHCL);
    }

    public Map<ByteString, List<ChunkReference>> fileSignatureToChunkReferences() {
        return new HashMap<>(fileSignatureToChunkReferences);
    }

    public Set<ByteString> fileSignatures() {
        return new HashSet<>(fileSignatureToChunkReferences.keySet());
    }

    public boolean contains(ByteString fileSignature) {
        return fileSignatureToChunkReferences.containsKey(fileSignature);
    }

    public Optional<List<ChunkReference>> chunkReferences(ByteString fileSignature) {
        return Optional.ofNullable(fileSignatureToChunkReferences.get(fileSignature));
    }

    public Optional<Map<Integer, StorageHostChunkList>> shcls(ByteString fileSignature) {
        if (!contains(fileSignature)) {
            return Optional.empty();
        }
        Map<Integer, StorageHostChunkList> shcls = fileSignatureToChunkReferences.get(fileSignature)
                .stream()
                .map(u -> (int) u.getContainerIndex())
                .distinct()
                .collect(toMap(Function.identity(), indexToSHCL::get));
        return Optional.of(shcls);
    }

    public Optional<StorageHostChunkList> shcl(int containerIndex) {
        return Optional.ofNullable(indexToSHCL.get(containerIndex));
    }

    @Override
    public String toString() {
        return "Voodoo{"
                + "indexToSHCL=" + indexToSHCL
                + ", fileSignatureToChunkReferences=" + fileSignatureToChunkReferences
                + '}';
    }
}
