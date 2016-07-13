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

import com.github.horrorho.inflatabledonkey.crypto.GCMDataA;
import com.github.horrorho.inflatabledonkey.crypto.ec.key.ECPrivateKey;
import com.github.horrorho.inflatabledonkey.pcs.key.Key;
import com.github.horrorho.inflatabledonkey.pcs.key.KeyID;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ProtectionZone.
 *
 * @author Ahseya
 */
@Immutable
public final class ProtectionZone {

    private static final Logger logger = LoggerFactory.getLogger(ProtectionZone.class);

    private final Function<byte[], Optional<byte[]>> unwrapKey;
    private final Optional<byte[]> masterKey;
    private final Optional<byte[]> decryptKey;
    private final LinkedHashMap<KeyID, Key<ECPrivateKey>> keys;
    private final String protectionTag;

    ProtectionZone(
            PZKeyUnwrap unwrapKey,
            Optional<byte[]> masterKey,
            Optional<byte[]> decryptKey,
            LinkedHashMap<KeyID, Key<ECPrivateKey>> keys,
            String protectionTag) {

        this.masterKey = Objects.requireNonNull(masterKey, "masterKey");
        this.decryptKey = Objects.requireNonNull(decryptKey, "decryptKey");
        this.keys = new LinkedHashMap<>(keys);
        this.protectionTag = Objects.requireNonNull(protectionTag, "protectionTag");

        // TOFIX partial application probably stops equals() from working
        this.unwrapKey = masterKey.isPresent()
                ? wk -> unwrapKey.apply(masterKey.get(), wk)
                : wk -> Optional.empty();
    }

    public String protectionTag() {
        return protectionTag;
    }

    Optional<byte[]> kdk() {
        return masterKey.map(bs -> Arrays.copyOf(bs, bs.length));
    }

    Optional<byte[]> dk() {
        return decryptKey.map(bs -> Arrays.copyOf(bs, bs.length));
    }

    public Optional<byte[]> decrypt(byte[] data, String identifier) {
        return decrypt(data, identifier.getBytes(StandardCharsets.UTF_8));
    }

    public Optional<byte[]> decrypt(byte[] data, byte[] identifier) {
        return decryptKey.map(key -> GCMDataA.decrypt(key, data, Optional.of(identifier)));
    }

    public Optional<byte[]> unwrapKey(byte[] wrappedKey) {
        return unwrapKey.apply(wrappedKey);
    }

    public LinkedHashMap<KeyID, Key<ECPrivateKey>> keys() {
        return new LinkedHashMap<>(keys);
    }

    public List<Key<ECPrivateKey>> keyList() {
        return new ArrayList<>(keys.values());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.unwrapKey);
        hash = 13 * hash + Objects.hashCode(this.masterKey);
        hash = 13 * hash + Objects.hashCode(this.decryptKey);
        hash = 13 * hash + Objects.hashCode(this.keys);
        hash = 13 * hash + Objects.hashCode(this.protectionTag);
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
        final ProtectionZone other = (ProtectionZone) obj;
        if (!Objects.equals(this.protectionTag, other.protectionTag)) {
            return false;
        }
        if (!Objects.equals(this.unwrapKey, other.unwrapKey)) {
            return false;
        }
        if (!Objects.equals(this.masterKey, other.masterKey)) {
            return false;
        }
        if (!Objects.equals(this.decryptKey, other.decryptKey)) {
            return false;
        }
        if (!Objects.equals(this.keys, other.keys)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProtectionZone{"
                + "unwrapKey=" + unwrapKey
                + ", masterKey=" + masterKey
                + ", decryptKey=" + decryptKey
                + ", keys=" + keys
                + ", protectionTag=" + protectionTag
                + '}';
    }
}
// Optimise key handling
