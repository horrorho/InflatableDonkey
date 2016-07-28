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

import com.github.horrorho.inflatabledonkey.io.HookInputStream;
import com.github.horrorho.inflatabledonkey.chunk.Chunk;
import com.github.horrorho.inflatabledonkey.io.IOConsumer;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.READ;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.ThreadSafe;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@ThreadSafe
public final class DiskChunk implements Chunk {

    private static final Logger logger = LoggerFactory.getLogger(DiskChunk.class);

    private final byte[] checksum;
    private final Path file;

    DiskChunk(byte[] checksum, Path file) {
        this.file = Objects.requireNonNull(file, "file");
        this.checksum = Arrays.copyOf(checksum, checksum.length);
    }

    @Override
    public byte[] checksum() {
        return Arrays.copyOf(checksum, checksum.length);
    }

    @Override
    public Optional<InputStream> inputStream() throws IOException {
        return Files.exists(file)
                ? doInputStream()
                : Optional.empty();
    }

    Optional<InputStream> doInputStream() throws IOException {
        try {
            InputStream is = Files.newInputStream(file, READ);
            if (logger.isTraceEnabled()) {
                logger.trace("-- doInputStream() - open: {}", Hex.toHexString(checksum()));
                IOConsumer<InputStream> callback = u
                        -> logger.trace("-- doInputStream() - callback close: {}", Hex.toHexString(checksum()));
                is = new HookInputStream(is, callback);
            }
            return Optional.of(is);

        } catch (NoSuchFileException ex) {
            logger.warn("-- doInputStream() - file was just deleted: {}", ex);
            return Optional.empty();
        }
    }

//    Optional<InputStream> doInputStream() throws IOException {
//        try {
//            return Optional.of(Files.newInputStream(file, READ));
//        } catch (NoSuchFileException ex) {
//            logger.warn("-- doInputStream() - file was just deleted: {}", ex);
//            return Optional.empty();
//        }
//    }
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + java.util.Arrays.hashCode(this.checksum);
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
        final DiskChunk other = (DiskChunk) obj;
        if (!java.util.Arrays.equals(this.checksum, other.checksum)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DiskChunk{"
                + "checksum=" + Hex.toHexString(checksum)
                + ", file=" + file
                + '}';
    }
}
