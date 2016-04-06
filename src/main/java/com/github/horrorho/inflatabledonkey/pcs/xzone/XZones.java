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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class XZones {

    public static XZones empty() {
        return EMPTY;
    }

    private static final Logger logger = LoggerFactory.getLogger(XZones.class);

    private static final XZones EMPTY = new XZones(XKeySet.empty(), new HashMap<>(), Optional.empty());

    private final XKeySet keySet;
    private final Map<String, XZone> zones;
    private final Optional<String> protectionTag;

    private XZones(XKeySet keySet, Map<String, XZone> zones, Optional<String> protectionTag) {
        this.keySet = Objects.requireNonNull(keySet, "keySet");
        this.zones = Objects.requireNonNull(zones, "zones");
        this.protectionTag = Objects.requireNonNull(protectionTag, "protectionTag");
    }

    public XKeySet keySet() {
        return keySet;
    }

    public Map<String, XZone> zones() {
        return new HashMap<>(zones);
    }

    public Optional<XZone> zone(String protectionTag) {
        return Optional.ofNullable(zones.get(protectionTag));
    }

    public Optional<XZone> zone() {
        return protectionTag.map(zones::get);
    }

    public Optional<XZones> with(byte[] protectionInfo, String protectionTag) {
        Optional<XZone> optionalXZone = XZoneFactory.instance().create(protectionInfo, protectionTag, keySet());
        if (!optionalXZone.isPresent()) {
            logger.warn("-- with() - failed to create zone");
            return Optional.empty();
        }

        logger.debug("-- with() - zones key count: {}", keySet.keys().size());
        keySet.keys().keySet().forEach(k -> logger.debug("-- with() - key id: {}", k));

        return Optional.of(XZones.this.with(optionalXZone.get()));
    }

    public XZones with(XZone xZone) {
        if (zones.containsKey(xZone.protectionTag())) {
            logger.debug("-- with() - overwritten zone: {}", xZone.protectionTag());
        }

        XKeySet xKeySetNew = keySet.withAll(xZone.keys());
        HashMap<String, XZone> xZonesNew = new HashMap<>(zones);
        xZonesNew.put(xZone.protectionTag(), xZone);

        return new XZones(xKeySetNew, xZonesNew, Optional.of(xZone.protectionTag()));
    }

    public XZones with(XKeySet xKeySet) {
        XKeySet newXKeySet = this.keySet.withAll(xKeySet);

        return new XZones(newXKeySet, zones, protectionTag);
    }

    @Override
    public String toString() {
        return "XZones{" + "keySet=" + keySet + ", zones=" + zones + ", protectionTag=" + protectionTag + '}';
    }
}
