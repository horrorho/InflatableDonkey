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
package com.github.horrorho.inflatabledonkey.chunk.engine;

import com.github.horrorho.inflatabledonkey.chunk.Chunk;
import com.github.horrorho.inflatabledonkey.chunk.store.ChunkStore;
import com.github.horrorho.inflatabledonkey.cloud.voodoo.ChunkReferences;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ChunkListDecryptors.
 *
 * @author Ahseya
 */
@Immutable
public final class ChunkListDecrypters {

    private static final Logger logger = LoggerFactory.getLogger(ChunkListDecrypters.class);

    public static Map<ChunkServer.ChunkReference, Chunk> decrypt(
            int container,
            List<ChunkServer.ChunkInfo> chunkInfoList,
            ChunkStore chunkStore,
            byte[] data,
            Function<ChunkServer.ChunkReference, Optional<byte[]>> getKeyEncryptionKey) {

        Map<ChunkServer.ChunkReference, Chunk> chunks = new HashMap<>();

        for (int i = 0; i < chunkInfoList.size(); i++) {
            ChunkServer.ChunkReference chunkReference = ChunkReferences.chunkReference(container, i);
            ChunkServer.ChunkInfo chunkInfo = chunkInfoList.get(i);

            Optional<byte[]> keyEncryptionKey = getKeyEncryptionKey.apply(chunkReference);
            Optional<Chunk> chunk = decrypt(chunkInfo, chunkStore, data, keyEncryptionKey);

            if (chunk.isPresent()) {
                chunks.put(chunkReference, chunk.get());
            }
        }
        return chunks;
    }

    static Optional<Chunk> decrypt(
            ChunkServer.ChunkInfo chunkInfo,
            ChunkStore chunkStore,
            byte[] data,
            Optional<byte[]> kek) {

        return ChunkEncryptionKeys.unwrapKey(kek, chunkInfo.getChunkEncryptionKey().toByteArray())
                .flatMap(k -> decrypt(chunkInfo, chunkStore, data, k));
    }

    static Optional<Chunk> decrypt(ChunkServer.ChunkInfo chunkInfo, ChunkStore chunkStore, byte[] data, byte[] key) {
        int offset = chunkInfo.getChunkOffset();
        int length = chunkInfo.getChunkLength();

        if (offset + length > data.length) {
            logger.warn("-- decrypt() - input data too short");
        }

        if (offset < 0 || length <= 0) {
            logger.warn("-- decrypt() - cannot decrypt offset: 0x{} length: 0x{}",
                    Integer.toHexString(offset), Integer.toHexString(length));
        }

        Optional<byte[]> optionalChunkData = ChunkDecrypters.decrypt(key, data, offset, length);
        if (!optionalChunkData.isPresent()) {
            logger.warn("-- decrypt() - decrypt failed");
            return Optional.empty();
        }
        byte[] chunkData = optionalChunkData.get();

        return validate(chunkInfo, chunkData)
                .flatMap(checksum -> copy(chunkStore, checksum, chunkData));
    }

    static Optional<Chunk> copy(ChunkStore chunkStore, byte[] checksum, byte[] data) {
        try (OutputStream outputStream = chunkStore.write(checksum).orElse(null)) {
            if (outputStream != null) {
                outputStream.write(data);
            }
        } catch (IOException ex) {
            logger.warn("-- copy() - failed to copy chunk data: {} ex: {}", Hex.toHexString(checksum), ex);
        }
        return chunkStore.chunk(checksum);
    }

    static Optional<byte[]> validate(ChunkServer.ChunkInfo chunkInfo, byte[] data) {
        if (!chunkInfo.hasChunkChecksum()) {
            logger.warn("-- validate() - no checksum data for chunk");
            // Shouldn't happen, but we'll assume the data is valid and create a type 1 checksum for it.
            return ChunkChecksums.checksum(1, data);
        }

        byte[] checksum = chunkInfo.getChunkChecksum().toByteArray();

        // If the checksum type is unknown/ match couldn't be made we'll assume the data checksums correctly.
        if (ChunkChecksums.matchToData(checksum, data).orElse(true)) {
            return Optional.of(checksum);
        }
        return Optional.empty();
    }
}
