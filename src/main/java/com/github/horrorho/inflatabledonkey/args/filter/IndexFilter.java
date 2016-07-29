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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;

/**
 * Index filter. Filters List elements with reference to their indices. Negative selection values indicate positions
 * relative to the last element, with the last element being -1. If no selections are specified, the filter is disabled
 * and all elements pass through un-filtered.
 *
 * @author Ahseya
 */
@Immutable
public final class IndexFilter<T> implements UnaryOperator<List<T>> {

    private final List<Integer> selection;

    public IndexFilter(Collection<Integer> selection) {
        this.selection = new ArrayList<>(selection);
    }

    @Override
    public List<T> apply(List<T> list) {
        return selection.isEmpty()
                ? list
                : filter(list);
    }

    List<T> filter(List<T> list) {
        return selection.stream()
                .map(i -> i < 0 ? list.size() - i : i)
                .filter(i -> i >= 0 && i < list.size())
                .map(list::get)
                .collect(Collectors.toList());
    }
}
