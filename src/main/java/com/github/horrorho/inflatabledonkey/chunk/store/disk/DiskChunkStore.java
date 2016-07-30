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

import com.github.horrorho.inflatabledonkey.io.HookOutputStream;
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
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.io.DigestOutputStream;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.io.TeeOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread safe disk based chunk store.
 *
 * @author Ahseya
 */
@ThreadSafe
public final class DiskChunkStore implements ChunkStore {

    private static final Logger logger = LoggerFactory.getLogger(DiskChunkStore.class);

    private static final int TEMP_FILE_RETRY = 3;   // ~ 2^190 collision risk with 4 threads
    private static final String TEMP_SUFFIX = ".tmp";

    private final Object lock;
    private final Supplier<Digest> digests;
    private final BiPredicate<byte[], byte[]> testDigest;
    private final Path chunkFolder;
    private final Path tempFolder;

    public DiskChunkStore(Object lock, Supplier<Digest> digests, BiPredicate<byte[], byte[]> testDigest,
            Path chunkFolder, Path tempFolder) throws IOException {
        if (!DirectoryAssistant.create(chunkFolder)) {
            throw new IOException("DiskChunkStore failed to create chunk folder: "
                    + chunkFolder.normalize().toAbsolutePath());
        }
        if (!DirectoryAssistant.create(tempFolder)) {
            throw new IOException("DiskChunkStore failed to create temp folder: "
                    + tempFolder.normalize().toAbsolutePath());
        }
        this.lock = Objects.requireNonNull(lock);
        this.digests = Objects.requireNonNull(digests);
        this.testDigest = Objects.requireNonNull(testDigest);
        this.chunkFolder = chunkFolder.normalize().toAbsolutePath();
        this.tempFolder = tempFolder.normalize().toAbsolutePath();
    }

    public DiskChunkStore(Supplier<Digest> digests, BiPredicate<byte[], byte[]> testDigest, Path chunkFolder,
            Path tempFolder) throws IOException {
        this(new Object(), digests, testDigest, chunkFolder, tempFolder);
    }

    @Override
    public boolean contains(byte[] checksum) {
        synchronized (lock) {
            Path file = path(checksum);
            return Files.exists(file);
        }
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
    public Optional<OutputStream> outputStream(byte[] checksum) throws IOException {
        synchronized (lock) {
            Path to = path(checksum);
            return Files.exists(to)
                    ? Optional.empty()
                    : getOutputStream(checksum, to);
        }
    }

    @GuardedBy("lock")
    Optional<OutputStream> getOutputStream(byte[] checksum, Path to) throws IOException {
        Path temp = tempFile(TEMP_FILE_RETRY);
        // Possibly superfluous. Is user likely to delete the temp folder whilst a backup is in progress?
        if (!DirectoryAssistant.create(tempFolder)) {
            logger.warn("-- getOutputStream() - failed to create temp folder: {}", tempFolder);
            return Optional.empty();
        }

        OutputStream os = Files.newOutputStream(temp);
        DigestOutputStream dos = new DigestOutputStream(digests.get());
        TeeOutputStream tos = new TeeOutputStream(os, dos);

        HookOutputStream<OutputStream> hos = new HookOutputStream<>(tos, callback(checksum, dos, temp, to));
        return Optional.of(hos);
    }

    IOConsumer<OutputStream> callback(byte[] checksum, DigestOutputStream dos, Path temp, Path to) {
        return os -> copy(checksum, dos, os, temp, to);
    }

    void copy(byte[] checksum, DigestOutputStream dos, OutputStream os, Path temp, Path to) throws IOException {
        synchronized (lock) {
            byte[] digest = dos.getDigest();
            if (testDigest.test(digest, checksum)) {
                logger.debug("-- copy() - positive checksum match: {}", Hex.toHexString(digest));
            } else {
                Files.deleteIfExists(temp);
                throw new IOException("DiskChunkStore copy, bad digest: " + Hex.toHexString(digest));
            }

            if (Files.exists(to)) {
                logger.debug("-- copy() - duplicate chunk ignored: {}", to);
                return;
            }
            if (!Files.exists(temp)) {
                throw new IOException("DiskChunkStore copy, temporary file missing: " + temp);
            }
            if (!DirectoryAssistant.createParent(to)) {
                throw new IOException("DiskChunkStore copy, failed to create cache directory: " + to);
            }

            try {
                Files.move(temp, to);
            } catch (IOException ex) {
                logger.warn("-- copy() - IOException: {}", ex);
                throw new IOException("DiskChunkStore copy, failed", ex);
            }
            logger.debug("-- copy() - chunk created: {}", to);
        }
    }

    @GuardedBy("lock")
    Path tempFile(int retry) throws IOException {
        if (retry == 0) {
            throw new IOException("failed to create temporary file");
        }
        String random = new BigInteger(64, ThreadLocalRandom.current()).toString(16).toLowerCase(Locale.US);
        String filename = random + TEMP_SUFFIX;
        Path path = tempFolder.resolve(filename);
        return Files.exists(path)
                ? tempFile(--retry)
                : path;
    }

    @Override
    public boolean delete(byte[] checksum) throws IOException {
        synchronized (lock) {
            Path to = path(checksum);
            return Files.exists(to)
                    ? doDelete(to)
                    : false;
        }
    }

    @GuardedBy("lock")
    public boolean doDelete(Path to) throws IOException {
        logger.trace("-- doDelete() - to: {}", to);
        boolean deleted = Files.deleteIfExists(to);
        if (deleted) {
            logger.debug("-- doDelete() - deleted: {}", to);
            DirectoryAssistant.deleteEmptyBranch(chunkFolder, to.getParent());
        }
        logger.trace("-- doDelete() - deleted: {}", deleted);
        return deleted;
    }

    Path path(byte[] checksum) {
        Path filename = DiskChunkFiles.filename(checksum);
        return chunkFolder.resolve(filename);
    }

    @Override
    public String toString() {
        return "DiskChunkStore{"
                + "lock=" + lock
                + ", digests=" + digests
                + ", chunkFolder=" + chunkFolder
                + ", tempFolder=" + tempFolder
                + '}';
    }
}
// TODO consider interruptable ReentrantLock methods.

