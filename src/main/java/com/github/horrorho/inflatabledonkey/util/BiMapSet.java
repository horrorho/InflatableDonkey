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
package com.github.horrorho.inflatabledonkey.util;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;
import net.jcip.annotations.NotThreadSafe;

/**
 * BiMapSet.
 * <p>
 * Lightweight bi-map of sets with common references. Remove only. No put methods. Null values not permitted. Thread
 * safe.
 *
 * @author Ahseya
 * @param <K> key type
 * @param <V> value type
 */
@NotThreadSafe
public class BiMapSet<K, V> {

    public static <K, V> BiMapSet<K, V> from(Map<K, ? extends Collection<V>> map) {
        map.forEach((k, v) -> {
            if (k == null || v == null || v.contains(null)) {
                throw new NullPointerException("BiMapSet, null values not permitted");
            }
        });

        Map<K, Set<V>> kToVSet = map.entrySet()
                .stream()
                .flatMap(e -> e.getValue()
                        .stream()
                        .map(v -> new SimpleImmutableEntry<>(e.getKey(), v)))
                .collect(groupingBy(Map.Entry::getKey, mapping(Map.Entry::getValue, toSet())));

        Map<V, Set<K>> vToKSet = map.entrySet()
                .stream()
                .flatMap(e -> e.getValue()
                        .stream()
                        .map(v -> new SimpleImmutableEntry<>(v, e.getKey())))
                .collect(groupingBy(Map.Entry::getKey, mapping(Map.Entry::getValue, toSet())));

        return new BiMapSet(kToVSet, vToKSet);
    }

    final Map<K, Set<V>> kToVSet;
    final Map<V, Set<K>> vToKSet;

    BiMapSet(Map kToVSet, Map vToKSet) {
        this.kToVSet = kToVSet;
        this.vToKSet = vToKSet;
    }

    public Set<K> keySet() {
        return kToVSet.keySet();
    }

    public Set<V> valueSet() {
        return vToKSet.keySet();
    }

    public Set<K> keys(V value) {
        return set(value, vToKSet);
    }

    public Set<V> values(K key) {
        return set(key, kToVSet);
    }

    public Set<V> removeKey(K key) {
        return remove(key, vToKSet, kToVSet);
    }

    public Set<K> removeValue(V value) {
        return remove(value, kToVSet, vToKSet);
    }

    public boolean isEmpty() {
        return vToKSet.isEmpty() && kToVSet.isEmpty();
    }

    <T, U> Set<U> set(T t, Map<T, Set<U>> tToUSet) {
        Set<U> set = t == null
                ? Collections.emptySet()
                : tToUSet.get(t);

        return set == null
                ? new HashSet<>()
                : new HashSet<>(set);
    }

    <T, U> Set<U> remove(T t, Map<U, Set<T>> uToTSet, Map<T, Set<U>> tToUSet) {
        Set<U> us = tToUSet.remove(t);
        if (us == null) {
            return Collections.emptySet();
        }
        return us.stream()
                .filter(u -> {
                    Set<T> ts = uToTSet.get(u);
                    ts.remove(t);
                    if (ts.isEmpty()) {
                        uToTSet.remove(u);
                        return true;
                    } else {
                        return false;
                    }
                })
                .collect(toSet());
    }
}
