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

import static com.github.horrorho.inflatabledonkey.data.blob.BlobUtils.align;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;

/**
 * Unspecified format.
 *
 * @author Ahseya
 */
@Immutable
public final class BlobList {

    private final List<byte[]> items;

    public BlobList(ByteBuffer buffer) {
        List<Integer> indices = new ArrayList<>();

        int index;
        while ((index = buffer.getInt()) + buffer.position() != buffer.limit()) {
            if (index < 0 || index > buffer.limit()) {

                System.out.println(Integer.toHexString(index));
                throw new IllegalArgumentException("bad blob index: " + index);
            }
            indices.add(index);
        }

        int itemsOffset = buffer.position();
        items = indices.stream()
                .map(i -> {
                    if (i + itemsOffset != buffer.position()) {
                        throw new IllegalArgumentException("bad blob data structure");
                    }

                    int itemLength = buffer.getInt();
                    byte[] item = new byte[itemLength];
                    buffer.get(item);
                    align(buffer);

                    return item;
                }).collect(Collectors.toList());
    }

    public byte[] get(int index) {
        byte[] item = items.get(index);
        return Arrays.copyOf(item, item.length);
    }

    public int size() {
        return items.size();
    }

    @Override
    public String toString() {
        return "BlobData{" + "items=" + items + '}';
    }
}