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
import com.github.horrorho.inflatabledonkey.chunk.store.ChunkBuilder;
import com.github.horrorho.inflatabledonkey.chunk.store.ChunkStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.ThreadSafe;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DiskChunkStore.
 *
 * @author Ahseya
 */
@ThreadSafe
public final class DiskChunkStore implements ChunkStore {

    private static final Logger logger = LoggerFactory.getLogger(DiskChunkStore.class);

    private final Path chunkFolder;

    public DiskChunkStore(Path chunkFolder) {
        this.chunkFolder = Objects.requireNonNull(chunkFolder, "chunkFolder");
    }

    Path file(byte[] checksum) {
        Path filename = DiskChunkFiles.filename(checksum);
        return chunkFolder.resolve(filename);
    }

    @Override
    public boolean hasChunk(byte[] checksum) {
        boolean exists = Files.exists(file(checksum));
        logger.debug("-- hasChunk() - hasChunk: {} exists: {}", Hex.toHexString(checksum), exists);
        return exists;
    }

    @Override
    public Optional<Chunk> chunk(byte[] checksum) {
        Path file = file(checksum);

        // DiskChunk instances are lightweight, not cached.
        return Files.exists(file)
                ? Optional.of(new DiskChunk(checksum, file))
                : Optional.empty();
    }

    @Override
    public ChunkBuilder chunkBuilder(byte[] checksum) {
        Path file = file(checksum);
        if (Files.exists(file)) {
            logger.warn("chunk overwritten: {}", Hex.toHexString(checksum));
        }

        return new DiskChunk.Builder(checksum, file);
    }

    @Override
    public String toString() {
        return "DiskChunkStore{" + "chunkFolder=" + chunkFolder + '}';
    }
}
// TODO collisions
