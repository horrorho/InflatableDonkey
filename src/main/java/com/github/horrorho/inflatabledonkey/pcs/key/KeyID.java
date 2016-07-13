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
package com.github.horrorho.inflatabledonkey.pcs.key;

import java.util.Arrays;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.encoders.Hex; 

/**
 * KeyID.
 *
 * @author Ahseya
 */
@Immutable
public final class KeyID {

    public static KeyID importKeyID(byte[] data) {
        return new KeyID(Arrays.copyOf(data, data.length));
    }

    public static KeyID of(byte[] publicExportData) {
        byte[] id = id(publicExportData);
        return new KeyID(id);
    }

    static byte[] id(byte[] data) {
        // SHA256 truncated to 20 bytes. 
        Digest digest = new SHA256Digest();
        byte[] out = new byte[digest.getDigestSize()];

        digest.update(data, 0, data.length);
        digest.doFinal(out, 0);

        return Arrays.copyOf(out, 20);
    }

    private final byte[] id;

    private KeyID(byte[] id) {
        this.id = id;
    }

    public byte[] bytes() {
        return Arrays.copyOf(id, id.length);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Arrays.hashCode(this.id);
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

        final KeyID other = (KeyID) obj;
        return Arrays.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "KeyID{" + "0x" + Hex.toHexString(id) + '}';
    }
}
