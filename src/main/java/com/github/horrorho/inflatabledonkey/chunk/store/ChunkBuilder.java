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

/**
 * ChunkBuilder.
 *
 * @author Ahseya
 */
public interface ChunkBuilder {

    /**
     * Returns an OutputStream for the chunk data to be written to.
     *
     * @return OutputStream
     * @throws IOException
     * @throws IllegalStateException if the OutputStream is already in use
     */
    OutputStream outputStream() throws IOException;

    /**
     * Builds the Chunk. The OutputStream is closed. The Chunk is placed into the managing ChunkStore.
     *
     * @return Chunk
     * @throws IOException
     * @throws IllegalStateException if no OutputStream
     */
    Chunk build() throws IOException;

    default Chunk build(byte[] data) throws IOException {
        outputStream().write(data);
        return build();
    }
}
