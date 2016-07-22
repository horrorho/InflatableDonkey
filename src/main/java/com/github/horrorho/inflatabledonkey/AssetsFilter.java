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

import com.github.horrorho.inflatabledonkey.data.backup.Assets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
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
public final class AssetsFilter implements Predicate<Assets> {

    private static final Logger logger = LoggerFactory.getLogger(AssetsFilter.class);

    private final Optional<List<String>> domain;

    public AssetsFilter(Optional<? extends Collection<String>> domain) {
        this.domain = domain.map(ArrayList::new);
    }

    @Override
    public boolean test(Assets asset) {
        return Optional.of(asset.domain()) // TOFIX if Asset#domain() isn't altered to Optional
                .map(t -> t.toLowerCase(Locale.US))
                .map(t -> domain.map(us -> us.stream().anyMatch(u -> t.contains(u))).orElse(true))
                .orElseGet(() -> {
                    logger.debug("-- filterDomain() - no domain: {}", asset);
                    return false;
                });

    }

    @Override
    public String toString() {
        return "AssetsFilter{" + "domain=" + domain + '}';
    }
}
