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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.concurrent.Immutable;
import static com.github.horrorho.inflatabledonkey.data.blob.BlobUtils.align;

/**
 * Unspecified format.
 *
 * @author Ahseya
 */
@Immutable
public final class BlobLists {

    public static List<byte[]> importList(ByteBuffer buffer) {
        List<Integer> indices = new ArrayList<>();

        int index;
        while ((index = buffer.getInt()) + buffer.position() != buffer.limit()) {
            if (index < 0 || index > buffer.limit()) {
                throw new IllegalArgumentException("bad blob index: " + index);
            }
            indices.add(index);
        }

        int itemsOffset = buffer.position();
        return indices.stream()
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

    public static void exportList(ByteBuffer buffer, List<byte[]> list) {
        int offset = 0;
        for (byte[] bytes : list) {
            int length = BlobUtils.align(bytes.length) + 4;
            buffer.putInt(offset);
            offset += length;
        }
        buffer.putInt(offset);

        list.stream()
                .forEach(bytes -> {
                    buffer.putInt(bytes.length);
                    buffer.put(bytes);
                    BlobUtils.pad(buffer);
                });
    }

    public static int exportListSize(List<byte[]> list) {
        int sum = list.stream()
                .mapToInt(bytes -> bytes.length)
                .map(BlobUtils::align)
                .sum();

        return sum + list.size() * 8 + 4;
    }

    @Deprecated
    private final List<byte[]> items;

    @Deprecated
    public BlobLists(ByteBuffer buffer) {
        List<Integer> indices = new ArrayList<>();

        int index;
        while ((index = buffer.getInt()) + buffer.position() != buffer.limit()) {
            if (index < 0 || index > buffer.limit()) {
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

    @Deprecated
    public byte[] get(int index) {
        byte[] item = items.get(index);
        return Arrays.copyOf(item, item.length);
    }

    @Deprecated
    public int size() {
        return items.size();
    }

    @Deprecated
    @Override
    public String toString() {
        return "BlobData{" + "items=" + items + '}';
    }
}
