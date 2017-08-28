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
package com.github.horrorho.inflatabledonkey.chunk.store;

import com.github.horrorho.inflatabledonkey.chunk.Chunk;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static java.util.stream.Collectors.toSet;
import net.jcip.annotations.ThreadSafe;

/**
 * ChunkStore.
 *
 * @author Ahseya
 */
@ThreadSafe
public interface ChunkStore {

    boolean contains(byte[] checksum);

    Optional<Chunk> chunk(byte[] checksum);

    /**
     * Returns an OutputStream to write chunk data into. On closing this stream, the checksum referenced data is
     * verified and committed to the store, assuming that a competing thread has not already done so. If the chunk data
     * fails to match the specified checksum an IOException is thrown.
     *
     * @param checksum
     * @return OutputStream if the referenced chunk is not already in the store
     * @throws IOException
     */
    Optional<OutputStream> outputStream(byte[] checksum) throws IOException;

    /**
     * Deletes the checksum referenced chunk data.
     *
     * @param checksum
     * @return true if the specified chunk data was present and deleted
     * @throws IOException
     */
    boolean delete(byte[] checksum) throws IOException;

    default Set<Chunk> anyChunks(Collection<byte[]> checksums) {
        return checksums.stream()
                .map(this::chunk)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toSet());
    }

    default Optional<List<Chunk>> allChunks(Collection<byte[]> checksums) {
        List<Chunk> chunks = new ArrayList<>();
        for (byte[] checksum : checksums) {
            Optional<Chunk> chunk = chunk(checksum);
            if (chunk.isPresent()) {
                chunks.add(chunk.get());
            } else {
                return Optional.empty();
            }
        }
        return Optional.of(chunks);
    }
}
