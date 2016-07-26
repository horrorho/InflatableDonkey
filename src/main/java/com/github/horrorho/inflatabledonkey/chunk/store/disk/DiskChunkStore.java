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
package com.github.horrorho.inflatabledonkey.chunk.store.disk;

import com.github.horrorho.inflatabledonkey.chunk.Chunk;
import com.github.horrorho.inflatabledonkey.chunk.store.ChunkStore;
import com.github.horrorho.inflatabledonkey.io.DirectoryAssistant;
import com.github.horrorho.inflatabledonkey.io.IOConsumer;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import net.jcip.annotations.GuardedBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DiskChunkStore.
 *
 * @author Ahseya
 */
//@ThreadSafe
public final class DiskChunkStore implements ChunkStore {

    private static final Logger logger = LoggerFactory.getLogger(DiskChunkStore.class);
    private static final int RETRY = 3;

    private final Object lock;
    private final Path chunkFolder;
    private final Path tempFolder;

    public DiskChunkStore(Object lock, Path chunkFolder, Path tempFolder) {
        this.lock = Objects.requireNonNull(lock, "lock");
        this.chunkFolder = Objects.requireNonNull(chunkFolder, "chunkFolder");
        this.tempFolder = Objects.requireNonNull(tempFolder, "tempFolder");
    }

    public DiskChunkStore(Path chunkFolder, Path tempFolder) {
        this(new Object(), chunkFolder, tempFolder);
    }

    @Override
    public Optional<Chunk> chunk(byte[] checksum) {
        synchronized (lock) {
            Path file = path(checksum);
            // DiskChunk instances are lightweight, not cached.
            return Files.exists(file)
                    ? Optional.of(new DiskChunk(checksum, file))
                    : Optional.empty();
        }
    }

    @Override
    public Optional<OutputStream> write(byte[] checksum) throws IOException {
        synchronized (lock) {
            Path path = path(checksum);
            return Files.exists(path)
                    ? Optional.empty()
                    : write(path);
        }
    }

    @GuardedBy("lock")
    Optional<OutputStream> write(Path to) throws IOException {
        Path temp = tempFile(RETRY);
        if (!DirectoryAssistant.createParent(temp)) {
            return Optional.empty();
        }
        HookOutputStream outputStream = new HookOutputStream(Files.newOutputStream(temp), hook(temp, to));
        return Optional.of(outputStream);
    }

    IOConsumer<Boolean> hook(Path temp, Path to) {
        return b -> copy(b, temp, to);
    }

    void copy(boolean success, Path temp, Path to) throws IOException {
        synchronized (lock) {
            if (success == false) {
                logger.warn("-- copy() - failed to write to temporary file: {} chunk: {}", temp, to);
                return;
            }
            if (Files.exists(to)) {
                logger.debug("-- copy() - duplicate chunk ignored: {}", to);
                return;
            }
            if (!Files.exists(temp)) {
                logger.warn("-- copy() - temporary file missing: {}", temp);
                return;
            }
            if (!DirectoryAssistant.createParent(to)) {
                logger.warn("-- copy() - failed to create cache directory: {}", to);
            }
            Files.move(temp, to);
            logger.debug("-- copy() - chunk created: {}", to);
        }
    }

    @GuardedBy("lock")
    Path tempFile(int retry) throws IOException {
        if (retry == 0) {
            throw new IOException("failed to create temporary file");
        }
        String random = new BigInteger(64, ThreadLocalRandom.current()).toString(16).toUpperCase(Locale.US);
        String filename = random + ".tmp";
        Path path = tempFolder.resolve(filename);
        return Files.exists(path)
                ? tempFile(--retry)
                : path;
    }

    Path path(byte[] checksum) {
        Path filename = DiskChunkFiles.filename(checksum);
        return chunkFolder.resolve(filename);
    }

    @Override
    public String toString() {
        return "DiskChunkStore{" + "lock=" + lock + ", chunkFolder=" + chunkFolder + ", tempFolder=" + tempFolder + '}';
    }
}
// TODO checksum verification
