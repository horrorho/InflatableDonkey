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
package com.github.horrorho.inflatabledonkey.x;

import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.NotThreadSafe;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class FilterAssets {

    public static FilterAssets.Builder builder() {
        return new FilterAssets.Builder();
    }

    public static Predicate<Asset> isDownloadable() {
        return DOWNLOADABLE;
    }

    private static final Predicate<Asset> DOWNLOADABLE = builder()
            .test(Asset::attributeSize, size -> size > 0)
            .has(Asset::keyEncryptionKey)
            .has(Asset::domain)
            .has(Asset::relativePath)
            .build();

    @NotThreadSafe
    public static final class Builder {

        static Predicate<Asset> allMatch(List<Predicate<Asset>> tests) {
            return a -> tests.stream().allMatch(p -> p.test(a));
        }

        private List<Predicate<Asset>> tests;

        Builder(List<Predicate<Asset>> tests) {
            this.tests = Objects.requireNonNull(tests, "tests");
        }

        Builder() {
            this(new ArrayList<>());
        }

        public <T> Builder has(Function<Asset, Optional<T>> function) {
            return test(a -> function.apply(a).isPresent());
        }

        public Builder test(Predicate<Asset> predicate) {
            tests.add(predicate);
            return this;
        }

        public <T> Builder test(Function<Asset, Optional<T>> function, Predicate<T> predicate) {
            return test(a -> function.apply(a).map(predicate::test).orElse(false));
        }

        public <T> Builder test(Function<Asset, Optional<T>> function, BiPredicate<T, T> predicate, T value) {
            return test(function, t -> predicate.test(t, value));
        }

        public <T> Builder
                any(Function<Asset, Optional<T>> function, BiPredicate<T, T> predicate, Collection<T> values) {
            return test(function, t -> values.stream().anyMatch(u -> predicate.test(t, u)));
        }

        public Predicate<Asset> build() {
            return tests.isEmpty()
                    ? a -> true
                    : allMatch(new ArrayList<>(tests));
        }
    }
}
