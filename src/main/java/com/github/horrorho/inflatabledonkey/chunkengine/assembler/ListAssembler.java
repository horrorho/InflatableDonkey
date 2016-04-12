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
package com.github.horrorho.inflatabledonkey.chunkengine.assembler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ListAssembler.
 *
 * @author Ahseya
 * @param <K> key
 * @param <T> value
 */
@ThreadSafe
public final class ListAssembler<K, T> implements Assembler<K, T, List<T>> {

    static <K> Map<K, Integer> map(List<K> list) {
        return IntStream.range(0, list.size())
                .mapToObj(Integer::valueOf)
                .collect(Collectors.toMap(
                        list::get,
                        Function.identity()));
    }

    static <K> List<K> list(int size) {
        return Stream.of((K) null)
                .limit(size)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static final Logger logger = LoggerFactory.getLogger(ListAssembler.class);

    private final Map<K, Integer> keyIndexMap;
    private final List<T> list;

    // TODO tidy
    private ListAssembler(Map<K, Integer> keyIndexMap, List<T> chunks) {
        this.keyIndexMap = Objects.requireNonNull(keyIndexMap, "keyIndexMap");
        this.list = Objects.requireNonNull(chunks, "list");

        if (keyIndexMap.isEmpty()) {
            throw new IllegalArgumentException("empty key index map");
        }

        if (chunks.size() != keyIndexMap.size()) {
            throw new IllegalArgumentException("list size doesn't match key index map size");
        }
    }

    public ListAssembler(List<K> list) {
        this(map(list), list(list.size()));
    }

    @Override
    public synchronized Optional<List<T>> apply(K key, T value) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(value, "value");

        Integer index = keyIndexMap.remove(key);
        if (index == null) {
            logger.warn("-- apply() - not expecting key: {}", key);
            return Optional.empty();
        }
        list.set(index, value);

        if (!keyIndexMap.isEmpty()) {
            return Optional.empty();
        }

        if (list.contains(null)) {
            throw new IllegalStateException("null values");
        }

        ArrayList<T> copy = new ArrayList<>(list);
        list.clear();
        return Optional.of(copy);
    }

    @Override
    public synchronized Set<K> required() {
        return new HashSet<>(keyIndexMap.keySet());
    }

    @Override
    public String toString() {
        return "ListAssembler{" + "keyIndexMap=" + keyIndexMap + ", list=" + list + '}';
    }
}
