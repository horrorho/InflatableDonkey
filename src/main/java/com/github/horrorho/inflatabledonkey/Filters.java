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
package com.github.horrorho.inflatabledonkey;

import com.github.horrorho.inflatabledonkey.args.Property;
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.data.backup.Assets;
import com.github.horrorho.inflatabledonkey.data.backup.Device;
import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import java.util.function.IntPredicate;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

/**
 * Filters.
 *
 * @author Ahseya
 */
@Immutable
public final class Filters {

    public static Predicate<Asset> assetFilter(Collection<String> extensions) {
        if (extensions.isEmpty()) {
            return asset -> true;
        }

        Predicate<String> filter = stringFilter(extensions, String::endsWith);
        return asset -> asset
                .relativePath()
                .map(filter::test)
                .orElse(false);
    }

    public static Predicate<Assets> assetsFilter(Collection<String> subStrings) {
        if (subStrings.isEmpty()) {
            return assets -> true;
        }

        Predicate<String> filter = stringFilter(subStrings, String::contains);
        return assets -> assets
                .domain()
                .map(filter::test)
                .orElse(false);
    }

    public static Predicate<Device> deviceFilter(Collection<String> subStrings) {
        if (subStrings.isEmpty()) {
            return device -> true;
        }

        Predicate<String> filter = stringFilter(subStrings, String::contains);
        return device -> filter.test(device.name());
    }

    static Predicate<String> stringFilter(Collection<String> strings, BiPredicate<String, String> function) {
        List<String> lowerCase = toLowerCase(strings);
        return string -> lowerCase
                .stream()
                .anyMatch(s -> function.test(string.toLowerCase(Property.locale()), s));
    }

    static List<String> toLowerCase(Collection<String> strings) {
        return strings.stream()
                .map(s -> s.toLowerCase(Property.locale()))
                .collect(Collectors.toList());
    }

    public static <T> UnaryOperator<List<T>> listFilter(Collection<Integer> indices) {
        if (indices.isEmpty()) {
            return UnaryOperator.identity();
        }
        return list -> {
            IntPredicate predicate = indexFilter(list.size(), indices);
            return IntStream.range(0, list.size())
                    .filter(predicate)
                    .mapToObj(list::get)
                    .collect(Collectors.toList());
        };
    }

    static IntPredicate indexFilter(int maxIndex, Collection<Integer> indices) {
        return index -> filterIndex(index, maxIndex, indices);
    }

    static boolean filterIndex(int index, int maxIndex, Collection<Integer> indices) {
        return indices.contains(index) || indices.contains(index - maxIndex);
    }
}
