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
package com.github.horrorho.inflatabledonkey.pcs.zone;

import com.github.horrorho.inflatabledonkey.crypto.NISTKDF;
import java.util.Objects;
import java.util.function.Supplier;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;

/**
 * PZKeyDerivationFunction.
 *
 * @author Ahseya
 */
@Immutable
public final class PZKeyDerivationFunction {

    public static PZKeyDerivationFunction instance() {
        return INSTANCE;
    }

    private static final PZKeyDerivationFunction INSTANCE
            = new PZKeyDerivationFunction(SHA256Digest::new, Hex.decode("656E6372797074696F6E206B6579206B6579206D"), 0x10);

    private final Supplier<Digest> digestSupplier;
    private final byte[] label;
    private final int keyLength;

    public PZKeyDerivationFunction(Supplier<Digest> digestSupplier, byte[] label, int keyLength) {
        this.digestSupplier = Objects.requireNonNull(digestSupplier, "digestSupplier");
        this.label = Arrays.copyOf(label, label.length);
        this.keyLength = keyLength;
    }

    byte[] apply(byte[] keyDerivationKey) {
        return NISTKDF.ctrHMac(keyDerivationKey, label, digestSupplier, keyLength);
    }
}
