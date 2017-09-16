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

import com.github.horrorho.inflatabledonkey.chunk.store.ChunkStore;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.ChunkInfo;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.StorageHostChunkList;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import javax.annotation.concurrent.Immutable;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BoundedInputStream;
import org.apache.commons.io.input.CountingInputStream;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.io.CipherInputStream;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decrypts storage host chunk list streams. Limited to type 0x01 chunk decryption and streams under 2 Gb in length.
 *
 * @author Ahseya
 */
@Immutable
public final class ChunkListDecrypter {

    public static ChunkListDecrypter instance() {
        return INSTANCE;
    }

    private static final Logger logger = LoggerFactory.getLogger(ChunkListDecrypter.class);

    private static final ChunkListDecrypter INSTANCE = new ChunkListDecrypter();

    private static final Comparator<ChunkInfo> CHUNK_OFFSET_COMPARATOR
            = Comparator.comparing(ChunkInfo::getChunkOffset);

    private ChunkListDecrypter() {
    }

    /**
     *
     * @param container
     * @param inputStream closed on exit
     * @param store
     * @throws IOException
     * @throws ArithmeticException on input streams over 2 Gb.
     * @throws IllegalArgumentException on non 0x01 chunk keys
     */
    public void apply(StorageHostChunkList container, InputStream inputStream, ChunkStore store) throws IOException {
        logger.trace("<< apply() - input: {}", inputStream);
        // Ensure our chunk offsets are sequentially ordered.
        List<ChunkInfo> list = container.getChunkInfoList()
                .stream()
                .sorted(CHUNK_OFFSET_COMPARATOR)
                .collect(toList());

        try (CountingInputStream countingInputStream = new CountingInputStream(inputStream)) {
            streamChunks(list, countingInputStream, store);
        } catch (UncheckedIOException ex) {
            throw ex.getCause();
        }

        if (logger.isDebugEnabled()) {
            // Sanity check. Has a minor IO cost with a disk based chunk store.
            String missingChunks = list.stream()
                    .map(ci -> ci.getChunkChecksum().toByteArray())
                    .filter(c -> !store.contains(c))
                    .map(c -> "0x" + Hex.toHexString(c))
                    .collect(joining(" "));
            if (missingChunks.isEmpty()) {
                logger.debug("-- apply() - all chunks have been stored");
            } else {
                logger.warn("-- apply() - missing chunks: {}", missingChunks);
            }
        }
        logger.trace(">> apply()");
    }

    void streamChunks(List<ChunkInfo> chunkInfos, CountingInputStream inputStream, ChunkStore store) {
        logger.debug("-- streamChunks() - chunk count: {}", chunkInfos.size());
        chunkInfos.stream()
                .peek(ci -> logger.debug("-- streamChunks() - chunk info: {}", ci))
                .filter(u -> isChunkMissing(u, store))
                .forEach(u -> streamChunk(inputStream, inputStream.getCount(), u, store));
    }

    boolean isChunkMissing(ChunkInfo chunkInfo, ChunkStore store) {
        byte[] checksum = chunkInfo.getChunkChecksum().toByteArray();
        if (store.contains(checksum)) {
            logger.debug("-- isChunkMissing() - chunk already present in store: 0x{}", Hex.toHexString(checksum));
            return false;
        }
        return true;
    }

    void streamChunk(InputStream inputStream, int position, ChunkInfo chunkInfo, ChunkStore store) {
        byte[] checksum = chunkInfo.getChunkChecksum().toByteArray();
        int chunkOffset = chunkInfo.getChunkOffset();
        int chunkLength = chunkInfo.getChunkLength();
        byte[] key = key(chunkInfo);

        logger.debug("-- streamChunk() - streaming chunk: 0x{}", Hex.toHexString(checksum));
        positionedStream(inputStream, position, chunkOffset, chunkLength)
                .map(s -> boundedInputStream(s, chunkLength))
                .map(s -> cipherInputStream(s, key, checksum))
                .ifPresent(s -> copy(s, checksum, store));
    }

    Optional<InputStream> positionedStream(InputStream inputStream, int position, int chunkOffset, int chunkLength) {
        // Align stream offset with chunk offset, although we cannot back track nor should we need to.
        try {
            if (chunkOffset < position) {
                logger.warn("-- positionedStream() - bad stream position: {} chunk offset: {}", position, chunkOffset);
                return Optional.empty();
            }
            if (chunkOffset > position) {
                int bytes = chunkOffset - position;
                logger.debug("-- positionedStream() - skipping: {}", bytes);
                IOUtils.skipFully(inputStream, bytes);
            }
            return Optional.of(inputStream);

        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    BoundedInputStream boundedInputStream(InputStream inputStream, int length) {
        BoundedInputStream bis = new BoundedInputStream(inputStream, length); // No close() required/ not propagated.
        bis.setPropagateClose(false);
        return bis;
    }

    CipherInputStream cipherInputStream(InputStream inputStream, byte[] key, byte[] checksum) {
        CFBBlockCipher cipher = new CFBBlockCipher(new AESFastEngine(), 128);
        KeyParameter keyParameter = new KeyParameter(key);
        cipher.init(false, keyParameter);
        return new CipherInputStream(inputStream, cipher);
    }

    byte[] key(ChunkInfo chunkInfo) {
        byte[] chunkEncryptionKey = chunkInfo.getChunkEncryptionKey().toByteArray();
        if (chunkEncryptionKey.length != 0x11 || chunkEncryptionKey[0] != 0x01) {
            throw new IllegalArgumentException("unsupported key type: 0x" + Hex.toHexString(chunkEncryptionKey));
        }
        return Arrays.copyOfRange(chunkEncryptionKey, 1, chunkEncryptionKey.length);
    }

    void copy(InputStream is, byte[] checksum, ChunkStore store) {
        try {
            Optional<OutputStream> os = store.outputStream(checksum);
            if (os.isPresent()) {
                logger.debug("-- copy() - copying chunk into store: 0x{}", Hex.toHexString(checksum));
                doCopy(is, os.get());
            } else {
                logger.debug("-- copy() - store now already contains chunk: 0x{}", Hex.toHexString(checksum));
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    void doCopy(InputStream is, OutputStream os) throws IOException {
        try {
            IOUtils.copy(is, os);
        } finally {
            os.close(); // May throw an error/ bad checksum.
        }
    }
}
