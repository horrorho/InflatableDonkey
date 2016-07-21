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
package com.github.horrorho.inflatabledonkey;

import com.github.horrorho.inflatabledonkey.cloud.clients.KeyBagClient;
import com.github.horrorho.inflatabledonkey.cloudkitty.CloudKittyLegacy;
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.file.KeyBlob;
import com.github.horrorho.inflatabledonkey.keybag.KeyBag;
import com.github.horrorho.inflatabledonkey.keybag.KeyBagType;
import com.github.horrorho.inflatabledonkey.pcs.zone.ProtectionZone;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import net.jcip.annotations.ThreadSafe;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * KeyBagsManager.
 *
 * @author Ahseya
 */
@ThreadSafe
public final class KeyBagManager {

    private static final Logger logger = LoggerFactory.getLogger(KeyBagManager.class);

    public static KeyBagManager create(CloudKittyLegacy kitty, ProtectionZone mbksync) {
        BiFunction<HttpClient, String, Optional<KeyBag>> keyBagFactory
                = (httpClient, uuid) -> keyBag(httpClient, kitty, mbksync, uuid);

        return new KeyBagManager(keyBagFactory);
    }

    static Optional<KeyBag> keyBag(HttpClient httpClient, CloudKittyLegacy kitty, ProtectionZone mbksync, String uuid) {
        try {
            return KeyBagClient.keyBag(httpClient, kitty, mbksync, uuid);

        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private static final KeyBag FAIL
            = new KeyBag(KeyBagType.BACKUP, new byte[]{}, Collections.emptyMap(), Collections.emptyMap());

    private final BiFunction<HttpClient, String, Optional<KeyBag>> keyBagClient;
    private final ConcurrentHashMap<String, KeyBag> keyBagMap;

    public KeyBagManager(BiFunction<HttpClient, String, Optional<KeyBag>> keyBagClient, Map<String, KeyBag> keyBagMap) {
        this.keyBagClient = Objects.requireNonNull(keyBagClient, "keyBagClient");
        this.keyBagMap = new ConcurrentHashMap<>(keyBagMap);
    }

    public KeyBagManager(BiFunction<HttpClient, String, Optional<KeyBag>> keyBagFactory) {
        this(keyBagFactory, Collections.emptyMap());
    }

    public KeyBagManager addKeyBags(KeyBag... keyBags) {
        for (KeyBag keyBag : keyBags) {
            keyBagMap.put(keyBag.uuidBase64(), keyBag);
        }
        return this;
    }

    public Collection<KeyBag> keyBags() {
        return keyBagMap.values()
                .stream()
                .filter(keyBag -> keyBag != FAIL)
                .collect(Collectors.toList());
    }

    public Optional<KeyBag> keyBag(byte[] uuid) {
        String encoded = Base64.getEncoder().encodeToString(uuid);
        return keyBag(encoded);
    }

    public Optional<KeyBag> keyBag(String uuid) {
        KeyBag keyBag = keyBagMap.getOrDefault(uuid, FAIL);
        if (keyBag == FAIL) {
            logger.warn("-- keyBag() - no key bag for uuid: {}", uuid);
        }

        return keyBag == FAIL
                ? Optional.empty()
                : Optional.of(keyBag);
    }

    public KeyBagManager update(HttpClient httpClient, List<Asset> assets) {
        keyBagUUIDs(assets)
                .forEach(uuid -> keyBagMap.computeIfAbsent(uuid, u -> fetchKeyBag(httpClient, u)));
        return this;
    }

    KeyBag fetchKeyBag(HttpClient httpClient, String uuid) {
        return keyBagClient.apply(httpClient, uuid)
                .orElse(FAIL);
    }

    Set<String> keyBagUUIDs(Collection<Asset> assets) {
        return assets.stream()
                .map(Asset::encryptionKey)
                .map(key -> key.flatMap(KeyBlob::uuid))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(uuid -> Base64.getEncoder().encodeToString(uuid))
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "KeyBagManager{" + "keyBagClient=" + keyBagClient + ", keyBagMap=" + keyBagMap + '}';
    }
}
