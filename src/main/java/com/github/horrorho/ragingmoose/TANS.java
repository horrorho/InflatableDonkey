/*
 * The MIT License
 *
 * Copyright 2017 Ayesha.
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
package com.github.horrorho.ragingmoose;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * tANS - asymmetric numeral systems tabled variant
 *
 * @author Ayesha
 * @param <T>
 */
@NotThreadSafe
@ParametersAreNonnullByDefault
final class TANS<T extends TANS.Entry> {

    @NotThreadSafe
    static class Entry {

        private int nBits;
        private int nBase;
        private byte symbol;

        int nBits() {
            return nBits;
        }

        void nBits(int nBits) {
            this.nBits = nBits;
        }

        int nBase() {
            return nBase;
        }

        void nBase(int nBase) {
            this.nBase = nBase;
        }

        byte symbol() {
            return symbol;
        }

        void symbol(byte symbol) {
            this.symbol = symbol;
        }
    }

    @NotThreadSafe
    static class State {

        private int value;

        void value(int v) {
            this.value = v;
        }

        int value() {
            return value;
        }

        @Override
        public String toString() {
            return "State{" + "value=" + value + '}';
        }
    }

    @Nonnull
    static <T> T[] table(int n, Supplier<T> cons, IntFunction<T[]> generator) throws LZFSEDecoderException {
        if (n < 0) {
            throw new LZFSEDecoderException();
        }
        return Stream.generate(cons)
                .limit(n)
                .toArray(generator);
    }

    private final T[] table;
    private final int nZero;

    TANS(T[] table) {
        this.table = Objects.requireNonNull(table);
        this.nZero = Integer.numberOfLeadingZeros(table.length);
    }

    TANS(int n, Supplier<T> cons, IntFunction<T[]> generator) throws LZFSEDecoderException {
        this(TANS.<T>table(n, cons, generator));
    }

    @Nonnull
    T transition(State state, BitInStream in) {
        T e = table[state.value()];
        state.value(e.nBase() + (int) in.read(e.nBits()));
        return e;
    }

    void foreach(BiConsumer<Integer, T> consumer) {
        IntStream.range(0, table.length)
                .forEachOrdered(i -> consumer.accept(i, table[i]));
    }

    @Nonnull
    TANS<T> init(short[] weights) throws LZFSEDecoderException {
        if (weights.length > 256) {
            throw new LZFSEDecoderException();
        }
        try {
            for (int i = 0, t = 0; i < weights.length; i++) {
                t = fill((byte) i, weights[i], t);
            }
            return this;

        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new LZFSEDecoderException(ex);
        }
    }

    int fill(byte s, int w, int t) {
        int k = Integer.numberOfLeadingZeros(w) - nZero;
        int x = (table.length << 1 >>> k) - w;
        for (int i = 0; i < w; i++) {
            T e = table[t++];
            e.symbol(s);
            if (i < x) {
                e.nBits(k);
                e.nBase((w + i << k) - table.length);
            } else {
                e.nBits(k - 1);
                e.nBase(i - x << k - 1);
            }
        }
        return t;
    }

    @Override
    public String toString() {
        return "TANS{" + "table.length=" + table.length + '}';
    }
}
