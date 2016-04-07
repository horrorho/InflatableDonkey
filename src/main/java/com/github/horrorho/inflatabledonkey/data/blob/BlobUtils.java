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
package com.github.horrorho.inflatabledonkey.data.blob;

import java.nio.ByteBuffer;
import org.bouncycastle.util.encoders.Hex;

/**
 * Unspecified format.
 *
 * @author Ahseya
 */
public class BlobUtils {

    /**
     * Returns the index aligned to the next four byte boundary.
     *
     * @param index index
     * @return aligned index
     */
    public static int align(int index) {
        return index + 3 & 0xFFFFFFFC;
    }

    /**
     * Aligns the supplied ByteBuffer position to the next four byte boundary
     *
     * @param blob blob
     * @throws NullPointerException if buffer is null
     */
    public static void align(ByteBuffer blob) {
        blob.position(align(blob.position()));
    }

    public static void pad(ByteBuffer blob) {
        for (int i = blob.position(); i < align(blob.position()); i++) {
            blob.put((byte) 0x00);
        }
    }

    public static int type(ByteBuffer blob) {
        if (blob.limit() < 8) {
            return -2;
        }

        int type = blob.getInt() == blob.limit()
                ? blob.getInt()
                : -1;

        blob.rewind();
        return type;
    }
}
