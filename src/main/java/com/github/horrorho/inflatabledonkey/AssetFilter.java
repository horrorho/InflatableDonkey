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

import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class AssetFilter implements Predicate<Asset> {

    private static final Logger logger = LoggerFactory.getLogger(AssetFilter.class);

    private final Optional<Long> birthMax;
    private final Optional<Long> birthMin;
    private final Optional<List<String>> extension;
    private final Optional<List<String>> relativePath;
    private final Optional<Integer> sizeMax;
    private final Optional<Integer> sizeMin;
    private final Optional<Long> statusChangedMax;
    private final Optional<Long> statusChangedMin;

    public AssetFilter(
            Optional<Long> birthMax,
            Optional<Long> birthMin,
            Optional<? extends Collection<String>> extension,
            Optional<? extends Collection<String>> relativePath,
            Optional<Integer> sizeMax,
            Optional<Integer> sizeMin,
            Optional<Long> statusChangedMax,
            Optional<Long> statusChangedMin) {

        this.birthMax = Objects.requireNonNull(birthMax, "birthMax");
        this.birthMin = Objects.requireNonNull(birthMin, "birthMin");
        this.statusChangedMax = Objects.requireNonNull(statusChangedMax, "statusChangedMax");
        this.statusChangedMin = Objects.requireNonNull(statusChangedMin, "statusChangedMin");
        this.sizeMax = Objects.requireNonNull(sizeMax, "sizeMax");
        this.sizeMin = Objects.requireNonNull(sizeMin, "sizeMin");
        this.extension = extension.map(ArrayList::new);
        this.relativePath = relativePath.map(ArrayList::new);
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
                .filter(t -> sizeMax.map(u -> t < u * 1024).orElse(true))
                .map(t -> sizeMin.map(u -> t > u * 1024).orElse(true))
                .orElseGet(() -> {
                    logger.debug("-- filterAttributeSize() - no attributeSize: {}", asset);
                    return true;
                });
    }

    boolean filterBirth(Asset asset) {
        return asset.birth().map(Instant::getEpochSecond)
                .filter(t -> birthMax.map(u -> t < u).orElse(true))
                .map(t -> birthMin.map(u -> t > u).orElse(true))
                .orElseGet(() -> {
                    logger.debug("-- filterBirth() - no filterBirth: {}", asset);
                    return true;
                });
    }

    boolean filterRelativePath(Asset asset) {
        return asset.relativePath()
                .map(t -> t.toLowerCase(Locale.US))
                .filter(t -> relativePath.map(us -> us.stream().anyMatch(u -> t.contains(u))).orElse(true))
                .map(t -> extension.map(us -> us.stream().anyMatch(u -> t.endsWith(u))).orElse(true))
                .orElseGet(() -> {
                    logger.debug("-- filterRelativePath() - no relativePath: {}", asset);
                    return false;
                });
    }

    boolean filterStatusChanged(Asset asset) {
        return asset.statusChanged()
                .map(Instant::getEpochSecond)
                .filter(t -> statusChangedMax.map(u -> t < u).orElse(true))
                .map(t -> statusChangedMin.map(u -> t > u).orElse(true))
                .orElseGet(() -> {
                    logger.debug("-- filterStatusChanged() - no statusChanged: {}", asset);
                    return true;
                });
    }

    @Override
    public String toString() {
        return "AssetFilter{"
                + "statusChangedMax=" + statusChangedMax
                + ", statusChangedMin=" + statusChangedMin
                + ", sizeMax=" + sizeMax
                + ", sizeMin=" + sizeMin
                + ", extension=" + extension
                + ", relativePath=" + relativePath
                + '}';
    }
}
