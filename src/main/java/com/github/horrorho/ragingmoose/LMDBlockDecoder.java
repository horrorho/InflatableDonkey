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

import java.io.IOException;
import java.util.Objects;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

/**
 *
 * @author Ayesha
 */
@NotThreadSafe
@ParametersAreNonnullByDefault
abstract class LMDBlockDecoder implements BlockDecoder {

    private final MatchBuffer mb;

    LMDBlockDecoder(MatchBuffer mb) {
        this.mb = Objects.requireNonNull(mb);
    }

    int l;
    int m;
    int d;

    @Override
    public int read() throws IOException {
        try {
            do {
                // Literal
                if (l > 0) {
                    l--;
                    byte b = literal();
                    mb.write(b);
                    return b & 0xFF;
                }
                // Match
                if (m > 0) {
                    m--;
                    return mb.match(d) & 0xFF;
                }
            } while (lmd());

            return -1;

        } catch (IllegalArgumentException ex) {
            throw new LZFSEDecoderException(ex);
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        try {
            int to = off + len;
            int o = off;
            do {
                // Literals
                int ls = Math.min(to - o, l);
                for (int n = o + ls; o < n; o++) {
                    byte _b = literal();
                    mb.write(_b);
                    b[o] = _b;
                }
                l -= ls;
                // Matches
                int ms = Math.min(to - o, m);
                for (int n = o + ms; o < n; o++) {
                    b[o] = mb.match(d);
                }
                m -= ms;
            } while (to - o > 0 && lmd());

            return o - off;

        } catch (IllegalArgumentException ex) {
            throw new LZFSEDecoderException(ex);
        }
    }

    abstract byte literal() throws IOException;

    abstract boolean lmd() throws IOException;

    void l(int l) {
        this.l = l;
    }

    void m(int m) {
        this.m = m;
    }

    void d(int d) {
        if (d != 0) {
            this.d = d;
        }
    }
}
