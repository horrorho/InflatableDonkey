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

import java.util.Objects;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.Arrays;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class FileKey {

    private static final int DATA_UNIT_SIZE = 0x1000; // TODO derive via metadata

    private final byte[] key;
    private final EncryptionKeyBlob blob;

    public FileKey(byte[] key, EncryptionKeyBlob blob) {
        this.key = Arrays.copyOf(key, key.length);
        this.blob = Objects.requireNonNull(blob, "blob");

    }

    public byte[] key() {
        return Arrays.copyOf(key, key.length);
    }

    public boolean isXTS() {
        return blob.isXTS();
    }

    public int dataUnitSize() {
        return DATA_UNIT_SIZE;
    }

    String diagnostic() {
        return Integer.toHexString(blob.u1()) + " "
                + Integer.toHexString(blob.u2()) + " "
                + Integer.toHexString(blob.u3());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + java.util.Arrays.hashCode(this.key);
        hash = 41 * hash + Objects.hashCode(this.blob);
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
        final FileKey other = (FileKey) obj;
        if (!java.util.Arrays.equals(this.key, other.key)) {
            return false;
        }
        if (!Objects.equals(this.blob, other.blob)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FileKey{" + "key=" + key + ", blob=" + blob + '}';
    }
}
