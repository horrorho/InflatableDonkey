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
import com.github.horrorho.inflatabledonkey.exception.BadDataException;
import com.github.horrorho.inflatabledonkey.io.IOFunction;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import net.jcip.annotations.ThreadSafe;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BoundedInputStream;
import org.apache.commons.io.output.NullOutputStream;
import org.bouncycastle.crypto.io.CipherInputStream;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@ThreadSafe
public final class ChunkListDecrypter implements IOFunction<InputStream, Map<ChunkServer.ChunkReference, Chunk>> {

    private static final Logger logger = LoggerFactory.getLogger(ChunkListDecrypter.class);

    private final BiFunction<byte[], InputStream, CipherInputStream> cipherInputStreams;
    private final BiFunction<byte[], byte[], Optional<byte[]>> keyUnwrap;
    private final ChunkStore store;
    private final SHCLContainer container;
    private final byte[] keyEncryptionKey;

    public ChunkListDecrypter(
            BiFunction< byte[], InputStream, CipherInputStream> cipherInputStreams,
            BiFunction<byte[], byte[], Optional<byte[]>> keyUnwrap,
            ChunkStore store,
            SHCLContainer container,
            byte[] keyEncryptionKey) {

        this.cipherInputStreams = Objects.requireNonNull(cipherInputStreams, "cipherInputStreams");
        this.keyUnwrap = Objects.requireNonNull(keyUnwrap, "keyUnwrap");
        this.store = Objects.requireNonNull(store, "store");
        this.container = Objects.requireNonNull(container, "container");
        this.keyEncryptionKey = Objects.requireNonNull(keyEncryptionKey, "keyEncryptionKey");
    }

    @Override
    public Map<ChunkServer.ChunkReference, Chunk> apply(InputStream inputStream) throws BadDataException, IOException {
        logger.trace("<< apply() - InputStream: {}", inputStream);
        Map<ChunkServer.ChunkReference, Chunk> chunks = new HashMap<>();
        for (Map.Entry<ChunkServer.ChunkReference, ChunkServer.ChunkInfo> entry : container.chunkInfos().entrySet()) {
            Chunk chunk = process(inputStream, entry.getValue());
            chunks.put(entry.getKey(), chunk);
        }
        logger.trace(">> apply() - chunks: {}", chunks);
        return chunks;
    }

    Chunk process(InputStream inputStream, ChunkServer.ChunkInfo chunkInfo) throws BadDataException, IOException {
        byte[] chunkKey = chunkInfo.getChunkEncryptionKey().toByteArray();
        byte[] unwrappedKey = keyUnwrap.apply(chunkKey, keyEncryptionKey)
                .orElseThrow(() -> new BadDataException("failed to unwrap key"));
        return ChunkListDecrypter.this.process(inputStream, chunkInfo, unwrappedKey);
    }

    Chunk process(InputStream inputStream, ChunkServer.ChunkInfo chunkInfo, byte[] key) throws IOException {
        logger.trace("<< process() - chunkInfo: {} key: {}", chunkInfo, Hex.toHexString(key));
        BoundedInputStream bis = new BoundedInputStream(inputStream, chunkInfo.getChunkLength());
        bis.setPropagateClose(false);
        CipherInputStream cis = cipherInputStreams.apply(key, inputStream);

        byte[] checksum = chunkInfo.getChunkChecksum().toByteArray();
        try (OutputStream os = store.write(checksum).orElseGet(NullOutputStream::new)) {
            IOUtils.copy(cis, os);
        }

        Chunk chunk = store.chunk(checksum)
                .orElseThrow(() -> new BadDataException("failed to store chunk"));
        logger.trace(">> process() - chunk: {}", chunk);
        return chunk;
    }
}
