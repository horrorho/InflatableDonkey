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
package com.github.horrorho.inflatabledonkey.chunk.store;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;

/**
 *
 * @author Ahseya
 */
public class ChunkDigest implements Digest {

    private static final int SIZE = 21;

    private final Digest digest;

    public ChunkDigest() {
        this.digest = new SHA256Digest();
    }

    @Override
    public String getAlgorithmName() {
        return "ChunkDigest";
    }

    @Override
    public int getDigestSize() {
        return SIZE;
    }

    @Override
    public void update(byte in) {
        digest.update(in);
    }

    @Override
    public void update(byte[] in, int inOff, int len) {
        digest.update(in, inOff, len);
    }

    @Override
    public int doFinal(byte[] out, int outOff) {
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        digest.update(hash, 0, hash.length);
        digest.doFinal(hash, 0);
        out[outOff] = 0x01;
        System.arraycopy(hash, 0, out, outOff + 1, SIZE - 1);
        return SIZE;
    }

    @Override
    public void reset() {
        digest.reset();
    }
}
