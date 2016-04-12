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

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ListAssembler.
 *
 * @author Ahseya
 * @param <K> key
 * @param <T> data
 * @param <R> result
 */
@ThreadSafe
public final class CompositeAssembler<K, T, R> implements Assembler<K, T, Collection<R>> {

    private static final Logger logger = LoggerFactory.getLogger(CompositeAssembler.class);

    private final ConcurrentMap<K, ? extends Set<? extends Assembler<K, T, R>>> map;

    public CompositeAssembler(Set<? extends Assembler<K, T, R>> assemblers) {
        map = assemblers.stream()
                .flatMap(a -> a.required()
                        .stream()
                        .map(k -> new SimpleImmutableEntry<>(k, a)))
                .collect(Collectors.groupingByConcurrent(
                        Map.Entry::getKey,
                        Collectors.mapping(SimpleImmutableEntry::getValue, Collectors.toSet())));
    }

    @Override
    public Optional<Collection<R>> apply(K key, T value) {
        Collection<? extends Assembler<K, T, R>> assemblers = map.remove(key);

        if (assemblers == null) {
            logger.warn("-- apply() - no assemblers for key: {}", key);
            return Optional.empty();
        }

        List<R> assembled = assemblers.stream()
                .map(collector -> collector.apply(key, value))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return assembled.isEmpty()
                ? Optional.empty()
                : Optional.of(assembled);
    }

    @Override
    public synchronized Set<K> required() {
        return new HashSet<>(map.keySet());
    }

    @Override
    public String toString() {
        return "CompositeAssembler{" + "map=" + map + '}';
    }
}
