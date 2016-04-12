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
package com.github.horrorho.inflatabledonkey.chunkengine;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;

/**
 * ChunkChecksum.
 *
 * @author Ahseya
 */
@Immutable
public final class ChunkChecksum implements Function<byte[], byte[]> {

    public static ChunkChecksum instance() {
        return INSTANCE;
    }

    private static final ChunkChecksum INSTANCE = new ChunkChecksum(SHA256Digest::new);

    private final Supplier<Digest> digestSupplier;

    public ChunkChecksum(Supplier<Digest> digestSupplier) {
        this.digestSupplier = Objects.requireNonNull(digestSupplier, "digestSupplier");
    }

    @Override
    public byte[] apply(byte[] data) {
        Digest digest = digestSupplier.get();

        byte[] hash = new byte[digest.getDigestSize()];

        digest.reset();
        digest.update(data, 0, data.length);
        digest.doFinal(hash, 0);
        digest.update(hash, 0, hash.length);
        digest.doFinal(hash, 0);

        return Arrays.copyOf(hash, 20);
    }
}
