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
import javax.annotation.concurrent.Immutable;
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

    private final PZKeyUnwrap unwrapKeys;
    private final List<byte[]> masterKeys;
    private final List<byte[]> decryptKeys;
    private final LinkedHashMap<KeyID, Key<ECPrivateKey>> keys;
    private final String protectionTag;

    ProtectionZone(
            PZKeyUnwrap unwrapKeys,
            List<byte[]> masterKeys,
            List<byte[]> decryptKey,
            LinkedHashMap<KeyID, Key<ECPrivateKey>> keys,
            String protectionTag) {
        this.unwrapKeys = Objects.requireNonNull(unwrapKeys);
        this.masterKeys = Objects.requireNonNull(masterKeys);
        this.decryptKeys = Objects.requireNonNull(decryptKey);
        this.keys = new LinkedHashMap<>(keys);
        this.protectionTag = Objects.requireNonNull(protectionTag);
    }

    public String protectionTag() {
        return protectionTag;
    }

    public Optional<byte[]> decrypt(byte[] data, String identifier) {
        return decrypt(data, identifier.getBytes(StandardCharsets.UTF_8));
    }

    public Optional<byte[]> decrypt(byte[] data, byte[] identifier) {
        return decryptKeys.stream()
                .map(u -> {
                    try {
                        return GCMDataA.decrypt(u, data, Optional.of(identifier));
                    } catch (IllegalArgumentException | IllegalStateException ex) {
                        logger.debug("-- decrypt() - exception: {}", ex.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .findFirst();
    }

    public Optional<byte[]> unwrapKey(byte[] wrappedKey) {
        return masterKeys.stream()
                .map(u -> unwrapKeys.apply(u, wrappedKey))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
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
        hash = 13 * hash + Objects.hashCode(this.unwrapKeys);
        hash = 13 * hash + Objects.hashCode(this.masterKeys);
        hash = 13 * hash + Objects.hashCode(this.decryptKeys);
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
        if (!Objects.equals(this.unwrapKeys, other.unwrapKeys)) {
            return false;
        }
        if (!Objects.equals(this.masterKeys, other.masterKeys)) {
            return false;
        }
        if (!Objects.equals(this.decryptKeys, other.decryptKeys)) {
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
                + "unwrapKey=" + unwrapKeys
                + ", masterKey=" + masterKeys
                + ", decryptKey=" + decryptKeys
                + ", keys=" + keys
                + ", protectionTag=" + protectionTag
                + '}';
    }
}
// Optimise key handling
