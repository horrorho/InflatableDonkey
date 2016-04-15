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

import com.github.horrorho.inflatabledonkey.chunk.store.ChunkBuilder;
import com.github.horrorho.inflatabledonkey.chunk.Chunk;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;
import java.util.Objects;
import net.jcip.annotations.NotThreadSafe;
import net.jcip.annotations.ThreadSafe;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DiskChunk.
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
    public InputStream inputStream() throws UncheckedIOException {
        try {
            return Files.newInputStream(file, READ);

        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    public long copyTo(OutputStream output) throws UncheckedIOException {
        try (InputStream input = Files.newInputStream(file, READ)) {
            long bytes = IOUtils.copyLarge(input, output);

            logger.debug("-- copyTo() - written (bytes): {}", bytes);
            return bytes;

        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + java.util.Arrays.hashCode(this.checksum);
        hash = 53 * hash + Objects.hashCode(this.file);
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
        if (!Objects.equals(this.file, other.file)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DiskChunk{" + "file=" + file + '}';
    }

    @NotThreadSafe
    static class Builder implements ChunkBuilder {

        private final byte[] checksum;
        private final Path file;
        private OutputStream outputStream;

        Builder(byte[] checksum, Path file) {
            this.checksum = Arrays.copyOf(checksum, checksum.length);
            this.file = Objects.requireNonNull(file, "file");
        }

        @Override
        public OutputStream outputStream() throws IOException {
            if (outputStream != null) {
                throw new IllegalStateException("output stream already open");
            }

            Path parent = file.getParent();
            if (parent != null) {
                // TODO more specific error for FileAlreadyExistsException in case of file blocking directory creation.
                // Eg. a file name '0' in the path will block a directory '0' being created.
                Files.createDirectories(parent);
            }
            outputStream = Files.newOutputStream(file, CREATE, WRITE, TRUNCATE_EXISTING);

            return outputStream;
        }

        @Override
        public Chunk build() throws IOException {
            if (outputStream == null) {
                throw new IllegalStateException("no output stream");
            }
            outputStream.close();
            return new DiskChunk(checksum, file);
        }
    }
}
