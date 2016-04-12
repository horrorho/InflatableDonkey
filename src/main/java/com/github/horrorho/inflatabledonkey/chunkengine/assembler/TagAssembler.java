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

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import net.jcip.annotations.ThreadSafe;

/**
 * TagAssembler.
 *
 * @author Ahseya
 * @param <K> key
 * @param <L> tag
 * @param <T> value
 * @param <R> assembled
 */
@ThreadSafe
public final class TagAssembler<K, L, T, R> implements Assembler<K, T, TaggedAssembly<L, R>> {

    private final L tag;
    private final Assembler<K, T, R> assembler;

    public TagAssembler(L tag, Assembler<K, T, R> assembler) {
        this.tag = Objects.requireNonNull(tag, "tag");
        this.assembler = Objects.requireNonNull(assembler, "assembler");
    }

    @Override
    public Optional<TaggedAssembly<L, R>> apply(K key, T value) {
        return assembler.apply(key, value)
                .map(a -> new TaggedAssembly<>(tag, a));
    }

    @Override
    public Set<K> required() {
        return assembler.required();
    }

    @Override
    public String toString() {
        return "TagAssembler{" + "tag=" + tag + ", assembler=" + assembler + '}';
    }
}
