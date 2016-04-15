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

import com.github.horrorho.inflatabledonkey.args.Property;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.bouncycastle.util.encoders.Hex;

/**
 * DiskChunkFiles.
 *
 * @author Ahseya
 */
public class DiskChunkFiles {

    private static final int SUBSPLIT = Property.PATH_CHUNK_STORE_SUBSPLIT
            .intValue()
            .orElse(3);

    static Path filename(byte[] chunkChecksum) {
        return filename(chunkChecksum, SUBSPLIT);
    }

    static Path filename(byte[] chunkChecksum, int subSplit) {
        String filename = Hex.toHexString(chunkChecksum);

        return filename.length() < subSplit
                ? Paths.get(filename)
                : subSplit(filename, subSplit);
    }

    static Path subSplit(String filename, int subSplit) {
        Path path = Paths.get(".");
        for (int i = 0; i < subSplit; i++) {
            path = path.resolve(String.valueOf(filename.charAt(i)));
        }

        return path.resolve(filename.substring(subSplit))
                .normalize();
    }
}
