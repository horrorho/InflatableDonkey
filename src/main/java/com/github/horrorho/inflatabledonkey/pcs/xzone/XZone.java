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
package com.github.horrorho.inflatabledonkey.pcs.xzone;

import com.github.horrorho.inflatabledonkey.crypto.GCMDataA;
import com.github.horrorho.inflatabledonkey.crypto.eckey.ECPrivate;
import com.github.horrorho.inflatabledonkey.crypto.key.Key;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;

/**
 * XZone. Experimental proof of concept code.
 *
 * @author Ahseya
 */
@Immutable
public final class XZone {

    private final String protectionTag;
    private final byte[] kdk;
    private final byte[] dk;    // dk for GCM decryption
    private final Collection<Key<ECPrivate>> keys;

    public XZone(String protectionTag, byte[] kdk, byte[] dk, Collection<Key<ECPrivate>> keys) {
        this.protectionTag = Objects.requireNonNull(protectionTag, "protectionTag");
        this.kdk = Arrays.copyOf(kdk, kdk.length);
        this.dk = Arrays.copyOf(dk, dk.length);
        this.keys = new ArrayList<>(keys);
    }

    public XZone(String protectionTag, byte[] kdk, byte[] dk) {
        this.protectionTag = Objects.requireNonNull(protectionTag, "protectionTag");
        this.kdk = Arrays.copyOf(kdk, kdk.length);
        this.dk = Arrays.copyOf(dk, dk.length);
        this.keys = new ArrayList<>();
    }

    public String protectionTag() {
        return protectionTag;
    }

    // Do we need this?
    public byte[] kdk() {
        return Arrays.copyOf(kdk, kdk.length);
    }

    // Do we need this?
    public byte[] dk() {
        return Arrays.copyOf(dk, dk.length);
    }

    public byte[] decrypt(byte[] data, String identifier) {
        return decrypt(data, identifier.getBytes(StandardCharsets.UTF_8));
    }

    public byte[] decrypt(byte[] data, byte[] identifier) {
        return GCMDataA.decrypt(dk, data, Optional.of(identifier));
    }

    public Optional<byte[]> fpDecrypt(byte[] wrappedKey) {
        return XFPKeyUnwrap.unwrap(kdk, wrappedKey);
    }

    public List<Key<ECPrivate>> keys() {
        return new ArrayList<>(keys);
    }

    @Override
    public String toString() {
        return "XZone{"
                + "protectionTag=" + protectionTag
                + ", kdk=" + Hex.toHexString(kdk)
                + ", dk=" + Hex.toHexString(dk)
                + ", keys=" + keys
                + '}';
    }
}
