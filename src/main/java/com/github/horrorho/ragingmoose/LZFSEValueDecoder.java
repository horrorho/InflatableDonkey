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

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

/**
 *
 * @author Ayesha
 */
@NotThreadSafe
@ParametersAreNonnullByDefault
class LZFSEValueDecoder {

    @NotThreadSafe
    static class Entry extends TANS.Entry {

        private int vBits;
        private int vBase;

        @Nonnull
        Entry set(byte[] symbolVBits, int[] symbolVBase) {
            int s = symbol() & 0xFF;
            this.vBase = symbolVBase[s];
            this.vBits = symbolVBits[s];
            return this;
        }
    }

    private final TANS<Entry> tans;
    private final TANS.State state;

    LZFSEValueDecoder(int nStates) throws LZFSEDecoderException {
        this.tans = new TANS<>(nStates, Entry::new, Entry[]::new);
        this.state = new TANS.State();
    }

    @Nonnull
    LZFSEValueDecoder load(short[] weights, byte[] symbolVBits, int[] symbolVBase) throws LZFSEDecoderException {
        tans.init(weights)
                .foreach((u, v) -> v.set(symbolVBits, symbolVBase));
        return this;
    }

    @Nonnull
    LZFSEValueDecoder state(int state) {
        this.state.value(state);
        return this;
    }

    int decode(BitInStream in) throws LZFSEDecoderException {
        Entry e = tans.transition(state, in);
        return e.vBase + (int) in.read(e.vBits);
    }

    @Override
    public String toString() {
        return "LZFSEValueDecoder{" + "tans=" + tans + ", state=" + state + '}';
    }
}
