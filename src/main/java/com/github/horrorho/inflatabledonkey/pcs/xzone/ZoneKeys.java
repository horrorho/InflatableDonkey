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
import com.github.horrorho.inflatabledonkey.crypto.key.KeyID;
import com.github.horrorho.inflatabledonkey.data.der.ProtectionInfo;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZoneKeys. Manages CloudKit ProtectionInfo keys.
 *
 * @author Ahseya
 */
@Immutable
public final class ZoneKeys {

    private static final Logger logger = LoggerFactory.getLogger(ZoneKeys.class);

    public static ZoneKeys base(Collection<Key<ECPrivate>> keys) {
        Map<KeyID, Key<ECPrivate>> map = keys.stream()
                .collect(Collectors.toMap(
                        Key::keyID,
                        Function.identity(),
                        (a, b) -> {
                            logger.warn("-- create() - collision: {} {}", a, b);
                            return a;
                        }));

        return new ZoneKeys("", Optional.empty(), Optional.empty(), map);
    }

    private final String protectionTag;
    private final Optional<byte[]> keyDerivationKey;
    private final Optional<byte[]> derivedKey;    // dk for GCM decryption
    private final Map<KeyID, Key<ECPrivate>> keys;

    public ZoneKeys(
            String protectionTag,
            Optional<byte[]> keyDerivationKey,
            Optional<byte[]> derivedKey,
            Map<KeyID, Key<ECPrivate>> keys) {

        this.protectionTag = Objects.requireNonNull(protectionTag, "protectionTag");
        this.keyDerivationKey = Objects.requireNonNull(keyDerivationKey, "keyDerivationKey");
        this.derivedKey = Objects.requireNonNull(derivedKey, "derivedKey");
        this.keys = new HashMap<>(keys);
    }

    public String protectionTag() {
        return protectionTag;
    }

    Optional<byte[]> kdk() {
        return keyDerivationKey.map(bs -> Arrays.copyOf(bs, bs.length));
    }

    Optional<byte[]> dk() {
        return derivedKey.map(bs -> Arrays.copyOf(bs, bs.length));
    }

    public Optional<byte[]> decrypt(byte[] data, String identifier) {
        return decrypt(data, identifier.getBytes(StandardCharsets.UTF_8));
    }

    public Optional<byte[]> decrypt(byte[] data, byte[] identifier) {
        return derivedKey.map(key -> GCMDataA.decrypt(key, data, Optional.of(identifier)));
    }

    public Optional<byte[]> fpDecrypt(byte[] wrappedKey) {
        return keyDerivationKey.flatMap(kdk -> XFPKeyUnwrap.unwrap(kdk, wrappedKey));
    }

    public Map<KeyID, Key<ECPrivate>> keys() {
        return new HashMap<>(keys);
    }

    public Optional<ZoneKeys> newZoneKeys(CloudKit.ProtectionInfo protectionInfo) {
        return protectionInfo.hasProtectionInfo() && protectionInfo.hasProtectionInfoTag()
                ? newZoneKeys(protectionInfo.getProtectionInfoTag(), protectionInfo.getProtectionInfo().toByteArray())
                : Optional.empty();
    }

    Optional<ZoneKeys> newZoneKeys(String protectionInfoTag, byte[] protectionInfo) {
        return ZoneKeysAssistant.importProtectionInfo(protectionInfo)
                .map(pi -> newZoneKeys(protectionInfoTag, pi));
    }

    ZoneKeys newZoneKeys(String protectionInfoTag, ProtectionInfo protectionInfo) {
        Optional<byte[]> kdk = ZoneKeysAssistant.keyDerivationKey(protectionInfo, keys);

        Optional<byte[]> dk = kdk.map(ZoneKeysAssistant::derivedKey);

        Map<KeyID, Key<ECPrivate>> masterKeys
                = dk.map(key -> ZoneKeysAssistant.masterKeys(protectionInfo, key))
                .orElse(Collections.emptyMap());
        masterKeys.putAll(keys);

        return new ZoneKeys(protectionInfoTag, kdk, dk, masterKeys);
    }
}
