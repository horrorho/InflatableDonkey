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

import com.github.horrorho.inflatabledonkey.util.BiMapSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import net.jcip.annotations.NotThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 * @param <T> asset type
 * @param <U> chunk type
 */
@NotThreadSafe
public final class ItemElements<T, U> {

    static <T, U> Map<T, List<U>> copy(Map<T, List<U>> map) {
        return map.entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, e -> new ArrayList<>(e.getValue())));
    }

    private static final Logger logger = LoggerFactory.getLogger(ItemElements.class);

    private final Map<T, List<U>> itemToElements;
    private final BiMapSet<T, U> biMap;

    private ItemElements(Map<T, List<U>> itemToElements, BiMapSet<T, U> biMap) {
        this.itemToElements = itemToElements;
        this.biMap = biMap;
    }

    public ItemElements(Map<T, List<U>> itemToElements) {
        this(copy(itemToElements), BiMapSet.from(itemToElements));
    }

    public Set<T> items() {
        return biMap.keySet();
    }

    public Set<U> elements() {
        return biMap.valueSet();
    }

    public Map<T, List<U>> putElements(Collection<U> elements) {
        return elements.stream()
                .map(this::putElement)
                .collect(HashMap::new, Map::putAll, Map::putAll);
    }

    public Map<T, List<U>> putElement(U element) {
        return biMap.removeValue(element)
                .stream()
                .peek(u -> {
                    if (!itemToElements.containsKey(u)) {
                        logger.error("-- putElement() - bad state, missing element: {}", u);
                    }
                })
                .collect(toMap(Function.identity(), itemToElements::remove));
    }

    public Set<T> voidElements(Collection<U> elements) {
        return elements.stream()
                .map(this::voidElement)
                .collect(HashSet::new, Set::addAll, Set::addAll);
    }

    public Set<T> voidElement(U element) {
        // Unrecoverable items.
        return biMap.keys(element)
                .stream()
                .peek(biMap::removeKey)
                .peek(t -> {
                    if (itemToElements.remove(t) == null) {
                        logger.error("-- voidElement() - bad state, missing element: {}", t);
                    }
                })
                .collect(toSet());
    }

    public boolean isEmpty() {
        if (itemToElements.isEmpty() != biMap.isEmpty()) {
            logger.error("-- isEmpty() - bad state, itemToElements: {} biMap: {}",
                    itemToElements.isEmpty(), biMap.isEmpty());
        }
        return itemToElements.isEmpty();
    }

    @Override
    public String toString() {
        return "ItemElements{" + "itemToElements=" + itemToElements + ", biMap=" + biMap + '}';
    }
}
