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
package com.github.horrorho.inflatabledonkey.keybag;

import java.nio.ByteBuffer;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;

/**
 * TagLengthValue.
 *
 * @author ahseya
 */
@Immutable
public final class TagLengthValue {

    public static List<TagLengthValue> parse(byte[] data) {
        return parse(ByteBuffer.wrap(data));
    }

    public static List<TagLengthValue> parse(ByteBuffer buffer) {

        List<TagLengthValue> tagValues = new ArrayList<>();
        byte[] tagBytes = new byte[4];

        while (buffer.position() + 8 <= buffer.limit()) {
            buffer.get(tagBytes);
            String tag = new String(tagBytes, UTF_8);

            // Signed 32 bit length. Limited to 2 GB.
            int length = buffer.getInt();
            if (length < 0 || length > buffer.remaining()) {
                throw new IllegalArgumentException("bad tag data length: " + length);
            }

            byte[] value = new byte[length];
            buffer.get(value);

            TagLengthValue tagValue = new TagLengthValue(tag, value);
            tagValues.add(tagValue);
        }
        return tagValues;
    }

    private final String tag;
    private final byte[] value;

    private TagLengthValue(String tag, byte[] value) {
        this.tag = Objects.requireNonNull(tag, "tag");
        this.value = Objects.requireNonNull(value, "value");
    }

    public String tag() {
        return tag;
    }

    public int length() {
        return value.length;
    }

    public byte[] value() {
        return Arrays.copyOf(value, value.length);
    }

    @Override
    public String toString() {
        return "TagLengthValue{"
                + "tag=" + tag
                + ", value=0x" + Hex.toHexString(value)
                + '}';
    }
}
