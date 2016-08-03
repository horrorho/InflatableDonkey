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
package com.github.horrorho.inflatabledonkey.cloud;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.ToIntFunction;
import net.jcip.annotations.NotThreadSafe;

/**
 * Simple iterator that attempts to batch sized items together, based on a minimum size threshold.
 *
 * @author Ahseya
 */
@NotThreadSafe
public class BatchSetIterator<T> implements Iterator<Set<T>> {

    public static <T> List<Set<T>> batchedSetList(Set<T> batch, ToIntFunction<T> size, int threshold) {
        BatchSetIterator<T> it = new BatchSetIterator<>(batch, size, threshold);
        List<Set<T>> list = new ArrayList<>();
        while (it.hasNext()) {
            list.add(it.next());
        }
        return list;
    }

    private final Iterator<T> iterator;
    private final Set<T> batch;
    private final ToIntFunction<T> size;
    private final int threshold;
    private int batchSize;

    BatchSetIterator(Iterator<T> iterator, Set<T> batch, ToIntFunction<T> size, int threshold) {
        this.iterator = Objects.requireNonNull(iterator);
        this.batch = Objects.requireNonNull(batch);
        this.size = Objects.requireNonNull(size);
        this.threshold = threshold;
    }

    public BatchSetIterator(Collection<T> items, ToIntFunction<T> size, int threshold) {
        this(new HashSet<>(items).iterator(), new HashSet<>(), size, threshold);
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext() || !batch.isEmpty();
    }

    @Override
    public Set<T> next() {
        while (iterator.hasNext()) {
            T item = iterator.next();
            int assetSize = size.applyAsInt(item);
            if (assetSize > threshold) {
                HashSet<T> set = new HashSet<>();
                set.add(item);
                return set;
            }
            batch.add(item);
            if ((batchSize += assetSize) >= threshold) {
                break;
            }
        }

        if (batch.isEmpty()) {
            throw new NoSuchElementException();
        }

        HashSet<T> set = new HashSet<>();
        set.addAll(batch);
        batch.clear();
        batchSize = 0;
        return set;
    }
}
