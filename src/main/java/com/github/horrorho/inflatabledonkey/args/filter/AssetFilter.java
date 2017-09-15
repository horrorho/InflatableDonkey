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
package com.github.horrorho.inflatabledonkey.args.filter;

import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import java.time.Instant;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toSet;
import java.util.stream.Stream;
import javax.annotation.concurrent.Immutable;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class AssetFilter implements Predicate<Asset> {

    static Set<String> stringFilters(Collection<String> collection) {
        Set<String> set = collection.stream().map(v -> v.toLowerCase(Locale.US)).collect(Collectors.toSet());
        return set.isEmpty()
                ? matchAll
                : set;
    }

    private static final Set<String> matchAll = Stream.of("").collect(toSet());

    private final Optional<Long> birthMax;
    private final Optional<Long> birthMin;
    private final Set<String> extensions;
    private final Set<String> relativePaths;
    private final Optional<Integer> sizeMax;
    private final Optional<Integer> sizeMin;
    private final Optional<Long> statusChangedMax;
    private final Optional<Long> statusChangedMin;

    public AssetFilter(
            Optional<Long> birthMax,
            Optional<Long> birthMin,
            Collection<String> extensions,
            Collection<String> relativePaths,
            Optional<Integer> sizeMax,
            Optional<Integer> sizeMin,
            Optional<Long> statusChangedMax,
            Optional<Long> statusChangedMin) {
        this.birthMax = Objects.requireNonNull(birthMax);
        this.birthMin = Objects.requireNonNull(birthMin);
        this.statusChangedMax = Objects.requireNonNull(statusChangedMax);
        this.statusChangedMin = Objects.requireNonNull(statusChangedMin);
        this.sizeMax = Objects.requireNonNull(sizeMax);
        this.sizeMin = Objects.requireNonNull(sizeMin);
        this.extensions = stringFilters(extensions);
        this.relativePaths = stringFilters(relativePaths);
    }

    @Override
    public boolean test(Asset asset) {
        return filterAttributeSize(asset)
                && filterBirth(asset)
                && filterRelativePath(asset)
                && filterStatusChanged(asset);
    }

    boolean filterAttributeSize(Asset asset) {
        return asset.attributeSize()
                .map(u -> sizeMax.map(v -> u <= v * 1024).orElse(true)
                        && sizeMin.map(v -> u >= v * 1024).orElse(true))
                .orElse(true);
    }

    boolean filterBirth(Asset asset) {
        return asset.birth().map(Instant::getEpochSecond)
                .map(u -> birthMax.map(v -> u <= v).orElse(true)
                        && birthMin.map(v -> u >= v).orElse(true))
                .orElse(true);
    }

    boolean filterRelativePath(Asset asset) {
        return asset.relativePath()
                .map(u -> u.toLowerCase(Locale.US))
                .map(u -> relativePaths.stream().anyMatch(u::contains) && extensions.stream().anyMatch(u::endsWith))
                .orElse(true);
    }

    boolean filterStatusChanged(Asset asset) {
        return asset.statusChanged()
                .map(Instant::getEpochSecond)
                .map(u -> statusChangedMax.map(v -> u <= v).orElse(true)
                        && statusChangedMin.map(v -> u >= v).orElse(true))
                .orElse(true);
    }

    @Override
    public String toString() {
        return "AssetFilter{"
                + "statusChangedMax=" + statusChangedMax
                + ", statusChangedMin=" + statusChangedMin
                + ", sizeMax=" + sizeMax
                + ", sizeMin=" + sizeMin
                + ", extension=" + extensions
                + ", relativePath=" + relativePaths
                + '}';
    }
}
