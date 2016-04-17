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

import com.github.horrorho.inflatabledonkey.crypto.eckey.ECPrivate;
import com.github.horrorho.inflatabledonkey.crypto.key.Key;
import com.github.horrorho.inflatabledonkey.crypto.key.KeyID;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Xzones.
 *
 * @author Ahseya
 */
@ThreadSafe
public final class XZones {

    public static XZones create() {
        return new XZones();
    }

    private static final Logger logger = LoggerFactory.getLogger(XZones.class);

    private final ConcurrentMap<KeyID, Key<ECPrivate>> keys;
    private final ConcurrentMap<String, XZone> zones;
    private AtomicReference<String> lastProtectionTag;

    private XZones(
            ConcurrentMap<KeyID, Key<ECPrivate>> keys,
            ConcurrentMap<String, XZone> zones,
            AtomicReference<String> lastProtectionTag) {

        this.keys = Objects.requireNonNull(keys, "keys");
        this.zones = Objects.requireNonNull(zones, "zones");
        this.lastProtectionTag = Objects.requireNonNull(lastProtectionTag, "lastProtectionTag");
    }

    public XZones() {
        this(new ConcurrentHashMap<>(), new ConcurrentHashMap<>(), new AtomicReference());
    }

    public Collection<Key<ECPrivate>> keys() {
        return new ArrayList<>(keys.values());
    }

    public Optional<Key<ECPrivate>> key(KeyID keyID) {
        return Optional.ofNullable(keys.get(keyID));
    }

    public Map<String, XZone> zones() {
        return new HashMap<>(zones);
    }

    public Optional<XZone> zone(String protectionTag) {
        return Optional.ofNullable(zones.get(protectionTag));
    }

    public Optional<String> lastProtectionTag() {
        // NB use with care in a multithreaded environment.
        return Optional.ofNullable(lastProtectionTag.get());
    }

    public Optional<XZone> lastZone() {
        return lastProtectionTag().map(zones::get);
    }

    public Optional<XZone> put(String protectionTag, byte[] protectionInfo) {
        return XZoneFactory.instance()
                .create(protectionInfo, protectionTag, this::key)
                .map(this::put);
    }

    public XZone put(XZone zone) {
        if (zones.containsKey(zone.protectionTag())) {
            logger.debug("-- put() - overwritten zone: {}", zone.protectionTag());
        }

        put(zone.keys());
        zones.put(zone.protectionTag(), zone);
        lastProtectionTag.set(zone.protectionTag());
        return zone;
    }

    public XZones put(Collection<Key<ECPrivate>> keys) {
        keys.stream()
                .filter(k -> !this.keys.containsKey(k.keyID()))
                .peek(k -> logger.debug("-- put() - added key id: {}", k.keyID()))
                .forEach(k -> this.keys.put(k.keyID(), k));
        return this;
    }

    @Override
    public String toString() {
        return "XZones{" + "keys=" + keys + ", zones=" + zones + ", currentProtectionTag=" + lastProtectionTag + '}';
    }
}
