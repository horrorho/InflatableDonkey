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
import java.util.List;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.bouncycastle.util.Arrays;

/**
 * Unspecified format.
 *
 * @author Ahseya
 */
@Immutable
public final class BlobA4 {

    private static final Logger logger = LoggerFactory.getLogger(BlobA4.class);

    private static final int TYPE = 0x000000A4;

    private final int x;
    private final byte[] tag;
    private final byte[] uid;
    private final byte[] salt;
    private final byte[] ephemeralKey;

    BlobA4(int x, byte[] tag, byte[] uid, byte[] salt, byte[] ephemeralKey) {
        if (tag.length != 0x10) {
            throw new IllegalArgumentException("bad tag 0x" + Hex.toHexString(tag));
        }

        this.x = x;
        this.tag = Arrays.copyOf(tag, 0x10);
        this.uid = Arrays.copyOf(uid, uid.length);
        this.salt = Arrays.copyOf(salt, salt.length);
        this.ephemeralKey = Arrays.copyOf(ephemeralKey, ephemeralKey.length);
    }

    public BlobA4(byte[] tag, byte[] uid, byte[] salt, byte[] ephemeralKey) {
        this(0, tag, uid, salt, ephemeralKey);
    }

    public BlobA4(ByteBuffer blob) {
        int length = blob.getInt();
        if (length != blob.limit()) {
            logger.warn("** BlobA4() - bad length: 0x{}", Integer.toHexString(length));
        }

        int type = blob.getInt();
        if (type != TYPE) {
            logger.warn("** BlobA4() - unexpected type: 0x{}", Integer.toHexString(type));
        }

        x = blob.getInt();
        if (x != 0) {
            // Unsure of x, possibly offset. Only ever noted 0 value.
            logger.warn("** BlobA4() - non-zero x: 0x{}", Integer.toHexString(x));
        }

        tag = new byte[0x10];
        blob.get(tag);

        List<byte[]> list = BlobLists.importList(blob);
        if (list.size() < 3) {
            throw new IllegalArgumentException("too few blob fields: " + list.size());
        }

        uid = list.get(0);
        salt = list.get(1);
        ephemeralKey = list.get(2);
    }

    public byte[] tag() {
        return Arrays.copyOf(tag, tag.length);
    }

    public byte[] uid() {
        return Arrays.copyOf(uid, uid.length);
    }

    public byte[] salt() {
        return Arrays.copyOf(salt, salt.length);
    }

    public byte[] ephemeralKey() {
        return Arrays.copyOf(ephemeralKey, ephemeralKey.length);
    }

    @Override
    public String toString() {
        return "BlobA4{"
                + "x=" + Integer.toHexString(x)
                + ", tag=0x" + Hex.toHexString(tag)
                + ", uid=0x" + Hex.toHexString(uid)
                + ", salt=0x" + Hex.toHexString(salt)
                + ", ephemeralKey=0x" + Hex.toHexString(ephemeralKey)
                + '}';
    }
}
