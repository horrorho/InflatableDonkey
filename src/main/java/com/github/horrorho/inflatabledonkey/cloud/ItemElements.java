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

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import net.jcip.annotations.NotThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 * @param <T> item type
 * @param <U> element type
 */
@NotThreadSafe
public final class ItemElements<T, U> {

    @NotThreadSafe
    static final class Tally<T> {

        private final List<T> itemList;
        private final Set<T> remainingItems;

        Tally(List<T> itemList, Collection<T> remainingItems) {
            this.itemList = new ArrayList<>(itemList);
            this.remainingItems = new HashSet<>(remainingItems);
        }

        Tally(List<T> items) {
            this(items, items);
        }

        public List<T> items() {
            return new ArrayList<>(itemList);
        }

        Optional<List<T>> put(T item) {
            remainingItems.remove(item);
            return remainingItems.isEmpty()
                    ? Optional.of(new ArrayList<>(itemList))
                    : Optional.empty();
        }
    }

    static <T, U> Map<U, Set<T>> elementToItems(Map<T, List<U>> itemToElements) {
        return itemToElements
                .entrySet()
                .stream()
                .flatMap(e -> e.getValue().stream().map(v -> new SimpleImmutableEntry<>(v, e.getKey())))
                .collect(groupingBy(Map.Entry::getKey, mapping(Map.Entry::getValue, toSet())));
    }

    static <T, U> Map<T, Tally<U>> itemToTally(Map<T, List<U>> itemToElements) {
        return itemToElements
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, e -> new Tally<>(e.getValue())));
    }

    private static final Logger logger = LoggerFactory.getLogger(ItemElements.class);

    private final Map<U, Set<T>> elementToItems;
    private final Map<T, Tally<U>> itemToTally;

    ItemElements(Map<U, Set<T>> elementToItems, Map<T, Tally<U>> itemToTally) {
        this.elementToItems = new HashMap<>(elementToItems);
        this.itemToTally = new HashMap<>(itemToTally);
    }

    public ItemElements(Map<T, List<U>> itemToElements) {
        this(elementToItems(itemToElements), itemToTally(itemToElements));
    }

    public Set<T> items() {
        return new HashSet<>(itemToTally.keySet());
    }

    public Set<U> elements() {
        return new HashSet<>(elementToItems.keySet());
    }

    public Map<T, List<U>> putElements(Collection<U> elements) {
        return elements.stream()
                .map(this::putElement)
                .collect(HashMap::new, Map::putAll, Map::putAll);
    }

    public Map<T, List<U>> putElement(U element) {
        return Optional.ofNullable(elementToItems.remove(element))
                .<Map<T, List<U>>>map(ts -> ts
                        .stream()
                        .map(k -> putElement(k, element))
                        .collect(HashMap::new, Map::putAll, Map::putAll))
                .orElseGet(Collections::emptyMap);
    }

    Map<T, List<U>> putElement(T item, U element) {
        return Optional.ofNullable(itemToTally.get(item))
                .flatMap(t -> t.put(element))
                .<Map<T, List<U>>>map(ts -> {
                    itemToTally.remove(item);
                    HashMap<T, List<U>> map = new HashMap<>();
                    map.put(item, ts);
                    return map;
                })
                .orElseGet(Collections::emptyMap);
    }

    public Set<T> voidElements(Collection<U> elements) {
        return elements.stream()
                .map(this::voidElement)
                .collect(HashSet::new, Set::addAll, Set::addAll);
    }

    public Set<T> voidElement(U element) {
        Set<T> items = elementToItems.getOrDefault(element, Collections.emptySet());
        items.forEach(this::voidItem);
        return items;
    }

    void voidItem(T item) {
        Optional.ofNullable(itemToTally.remove(item))
                .map(Tally::items)
                .ifPresent(us -> us
                        .stream()
                        .map(elementToItems::get)
                        .filter(Objects::nonNull)
                        .forEach(s -> s.remove(item)));
    }

    @Override
    public String toString() {
        return "ItemElements{" + "elementToItems=" + elementToItems + ", itemToTally=" + itemToTally + '}';
    }
}
