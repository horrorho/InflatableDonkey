/*
 * The MIT License
 *
 * Copyright 2015 Ahseya.
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
package com.github.horrorho.inflatabledonkey.pcs.xfile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import net.jcip.annotations.NotThreadSafe;

/**
 * BlockStreamDecrypter.
 *
 * @author Ahseya
 */
@NotThreadSafe
public final class BlockStreamDecrypter {

    public static BlockStreamDecrypter create(byte[] key) {
        return create(key, DEFAULT_BLOCK_LENGTH);
    }

    public static BlockStreamDecrypter create(byte[] key, int blockLength) {
        BlockDecrypter blockDecrypter = BlockDecrypter.create(key);
        return new BlockStreamDecrypter(blockDecrypter, blockLength);
    }

    private static final int DEFAULT_BLOCK_LENGTH = 0x1000;

    private final BlockDecrypter blockDecrypter;
    private final int blockLength;

    public BlockStreamDecrypter(BlockDecrypter blockDecrypter, int blockLength) {

        this.blockDecrypter = Objects.requireNonNull(blockDecrypter, "blockDecrypter");
        this.blockLength = blockLength;
    }

    public void decrypt(InputStream input, OutputStream output) throws IOException {
        byte[] in = new byte[blockLength];
        byte[] out = new byte[blockLength];

        int block = 0;
        int length;
        while ((length = read(input, in)) != -1) {
            blockDecrypter.decrypt(block, in, length, out);
            output.write(out, 0, length);
            block++;
        }
    }

    int read(InputStream input, byte[] buffer) throws IOException {
        // Read complete blocks where possible, as read on streams may return partial data which leads to incomplete 
        // block filling and broken decryption.
        int offset = 0;
        do {
            int read = input.read(buffer, offset, buffer.length - offset);

            if (read == -1) {
                return offset == 0 ? -1 : offset;
            }

            offset += read;

        } while (offset < buffer.length);
        return offset;
    }
}
