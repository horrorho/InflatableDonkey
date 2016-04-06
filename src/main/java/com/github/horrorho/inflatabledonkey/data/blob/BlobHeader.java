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
import net.jcip.annotations.Immutable;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class BlobHeader {

    private final int length;
    private final int type;

    public BlobHeader(ByteBuffer blob) {
        blob.rewind();

        length = blob.getInt();
        if (length != blob.limit()) {
            throw new IllegalArgumentException("bad length: expected " + length + " bytes, got " + blob.limit() + " bytes");
        }

        type = blob.getInt();
    }

    public int length() {
        return length;
    }

    public int type() {
        return type;
    }

    @Override
    public String toString() {
        return "BlobHeader{" + "length=" + length + ", type=" + type + '}';
    }
}
