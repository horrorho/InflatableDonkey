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

    public ChunkListDecrypter(
            BiFunction< byte[], InputStream, CipherInputStream> cipherInputStreams,
            BiFunction<byte[], byte[], Optional<byte[]>> keyUnwrap,
            ChunkStore store,
            SHCLContainer container) {

        this.cipherInputStreams = Objects.requireNonNull(cipherInputStreams);
        this.keyUnwrap = Objects.requireNonNull(keyUnwrap);
        this.store = Objects.requireNonNull(store);
        this.container = Objects.requireNonNull(container);
    }

    @Override
    public Map<ChunkServer.ChunkReference, Chunk> apply(InputStream inputStream) throws IOException {
        logger.trace("<< apply() - InputStream: {}", inputStream);
        Map<ChunkServer.ChunkReference, Chunk> chunks = new HashMap<>();
        int i = 0;
        for (Map.Entry<ChunkServer.ChunkReference, ChunkServer.ChunkInfo> entry : container.chunkInfos().entrySet()) {
            chunk(inputStream, entry.getValue(), i++)
                    .map(c -> chunks.put(entry.getKey(), c));
        }
        logger.trace(">> apply() - chunks: {}", chunks);
        return chunks;
    }

    Optional<Chunk> chunk(InputStream inputStream, ChunkServer.ChunkInfo chunkInfo, int index) throws IOException {
        logger.trace("<< chunk() - chunkInfo: {} index: {}", chunkInfo, index);

        BoundedInputStream bis = new BoundedInputStream(inputStream, chunkInfo.getChunkLength());
        bis.setPropagateClose(false);
        Optional<Chunk> chunk = chunk(bis, chunkInfo, index);
        consume(bis);

        logger.trace(">> chunk() - chunk: {}", chunk);
        return chunk;
    }

    Optional<Chunk> chunk(BoundedInputStream bis, ChunkServer.ChunkInfo chunkInfo, int index) throws IOException {
        byte[] checksum = chunkInfo.getChunkChecksum().toByteArray();
        Optional<Chunk> chunk = store.chunk(checksum);
        if (chunk.isPresent()) {
            logger.debug("-- chunk() - chunk present in store: 0x:{}", Hex.toHexString(checksum));
            return chunk;
        }
        logger.debug("-- chunk() - chunk not present in store: 0x:{}", Hex.toHexString(checksum));
        byte[] chunkEncryptionKey = chunkInfo.getChunkEncryptionKey().toByteArray();
        return decrypt(bis, chunkEncryptionKey, checksum, index);
    }

    Optional<Chunk>
            decrypt(BoundedInputStream bis, byte[] chunkEncryptionKey, byte[] checksum, int index) throws IOException {
        Optional<byte[]> key = unwrapKey(chunkEncryptionKey, index);
        if (key.isPresent()) {
            byte[] k = key.get();
            logger.debug("-- decrypt() - key: 0x{} chunk: 0x{}", Hex.toHexString(k), Hex.toHexString(checksum));
            store(cipherInputStreams.apply(k, bis), checksum);
        } else {
            logger.warn("-- decrypt() - key unwrap failed chunk: 0x{}", Hex.toHexString(checksum));
        }
        return store.chunk(checksum);
    }

    Optional<byte[]> unwrapKey(byte[] chunkEncryptionKey, int index) {
        return container.keyEncryptionKey(index)
                .flatMap(kek -> keyUnwrap.apply(chunkEncryptionKey, kek));
    }

    void store(CipherInputStream is, byte[] checksum) throws IOException {
        Optional<OutputStream> os = store.outputStream(checksum);
        if (os.isPresent()) {
            logger.debug("-- store() - copying chunk into store: 0x{}", Hex.toHexString(checksum));
            copy(is, os.get());
        } else {
            logger.debug("-- store() - store now already contains chunk: 0x{}", Hex.toHexString(checksum));
        }
    }

    void consume(InputStream is) throws IOException {
        copy(is, new NullOutputStream());
    }

    void copy(InputStream is, OutputStream os) throws IOException {
        try {
            IOUtils.copy(is, os);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }
}
