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
import static java.nio.charset.StandardCharsets.UTF_8;
import net.jcip.annotations.Immutable;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unspecified format.
 *
 * @author Ahseya
 */
@Immutable
public final class BlobA0 extends BlobBase {

    private static final Logger logger = LoggerFactory.getLogger(BlobA0.class);

    private final int x;
    private final int iterations;
    private final int y;
    private final BlobList list;
    private final String label;
    private final String timestamp;

    public BlobA0(ByteBuffer blob) {
        super(blob);

        if (type() != 0x000000A0) {
            // Presumed type 0x000000A0, consider throwing exception.
            logger.warn("** BlobB() - unexpected type: 0x{}", Integer.toHexString(type()));
        }

        x = blob.getInt();
        iterations = blob.getInt();
        y = blob.getInt();

        list = new BlobList(blob);
        if (list.size() < 5) {
            throw new IllegalArgumentException("too few blob fields: " + list.size());
        }

        label = new String(list.get(4), UTF_8);

        timestamp = list.size() > 5
                ? new String(list.get(5), UTF_8)
                : "";
    }

    public int iterations() {
        return iterations;
    }

    public byte[] dsid() {
        return list.get(0);
    }

    public byte[] salt() {
        return list.get(1);
    }

    public byte[] key() {
        return list.get(2);
    }

    public byte[] data() {
        return list.get(3);
    }

    public String label() {
        return label;
    }

    public String timestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "BlobA0{"
                + "type=0x" + Integer.toHexString(type())
                + ",length=0x" + Integer.toHexString(length())
                + ", x=" + x
                + ", iterations=" + iterations
                + ", y=" + y
                + ", dsid=0x" + Hex.encodeHexString(dsid())
                + ", salt=0x" + Hex.encodeHexString(salt())
                + ", key=0x" + Hex.encodeHexString(key())
                + ", data=0x" + Hex.encodeHexString(data())
                + ", label=" + label
                + ", timestamp=" + timestamp
                + '}';
    }
}
