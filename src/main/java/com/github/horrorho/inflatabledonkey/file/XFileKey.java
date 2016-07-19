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
package com.github.horrorho.inflatabledonkey.file;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.util.encoders.Hex;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class XFileKey {

    private final byte[] key;
    private final Supplier<BlockCipher> ciphers;
    private final byte[] flags;    // TODO can remove once we have figured the xts/ cbc switches

    public XFileKey(byte[] key, Supplier<BlockCipher> ciphers, byte[] flags) {
        this.key = Arrays.copyOf(key, key.length);
        this.ciphers = Objects.requireNonNull(ciphers, "ciphers");
        this.flags = Objects.requireNonNull(flags, "flags");
    }

    public XFileKey(byte[] key, Supplier<BlockCipher> ciphers) {
        this(key, ciphers, new byte[]{});
    }

    public byte[] key() {
        return Arrays.copyOf(key, key.length);
    }

    public Supplier<BlockCipher> ciphers() {
        return ciphers;
    }

    public byte[] flags() {
        return Arrays.copyOf(flags, flags.length);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Arrays.hashCode(this.key);
        hash = 79 * hash + Objects.hashCode(this.ciphers);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final XFileKey other = (XFileKey) obj;
        if (!Arrays.equals(this.key, other.key)) {
            return false;
        }
        if (!Objects.equals(this.ciphers, other.ciphers)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FileKeyCipher{"
                + "key=0x" + Hex.toHexString(key)
                + ", ciphers=" + ciphers
                + ", flags=0x" + Hex.toHexString(flags)
                + '}';
    }
}
