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
import com.github.horrorho.inflatabledonkey.cloudkitty.CloudKitty;
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.file.KeyBlob;
import com.github.horrorho.inflatabledonkey.data.backup.KeyBag;
import com.github.horrorho.inflatabledonkey.data.backup.KeyBagID;
import com.github.horrorho.inflatabledonkey.data.backup.KeyBagType;
import com.github.horrorho.inflatabledonkey.pcs.zone.ProtectionZone;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
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

    public static KeyBagManager defaults(CloudKitty kitty, ProtectionZone mbksync) {
        BiFunction<HttpClient, KeyBagID, Optional<KeyBag>> keyBagClient
                = (httpClient, keyBagID) -> keyBagClient(httpClient, kitty, mbksync, keyBagID);
        return new KeyBagManager(keyBagClient);
    }

    static Optional<KeyBag>
            keyBagClient(HttpClient httpClient, CloudKitty kitty, ProtectionZone mbksync, KeyBagID keyBagID) {
        try {
            return Optional.of(KeyBagClient.apply(httpClient, kitty, mbksync, keyBagID));

        } catch (IOException ex) {
            logger.warn("-- keyBagClient() - IOException: {} {}",
                    ex.getClass().getCanonicalName(), ex.getMessage());
            return Optional.empty();
        }
    }

    private static final KeyBag FAIL
            = new KeyBag(new KeyBagID(new byte[]{0}), KeyBagType.BACKUP, Collections.emptyMap(), Collections.emptyMap());

    private final BiFunction<HttpClient, KeyBagID, Optional<KeyBag>> keyBagClient;
    private final ConcurrentHashMap<KeyBagID, KeyBag> keyBagMap;

    public KeyBagManager(BiFunction<HttpClient, KeyBagID, Optional<KeyBag>> keyBagClient, Map<KeyBagID, KeyBag> keyBagMap) {
        this.keyBagClient = Objects.requireNonNull(keyBagClient, "keyBagClient");
        this.keyBagMap = new ConcurrentHashMap<>(keyBagMap);
    }

    public KeyBagManager(BiFunction<HttpClient, KeyBagID, Optional<KeyBag>> keyBagClient) {
        this(keyBagClient, Collections.emptyMap());
    }

    public Optional<KeyBag> keyBag(KeyBagID keyBagID) {
        return Optional.ofNullable(keyBagMap.get(keyBagID));
    }

    public KeyBagManager update(HttpClient httpClient, Collection<Asset> assets) {
        // Although we lock the map for a number of seconds to fetch a new key bag, it's a rare event.
        keyBagUUIDs(assets)
                .forEach(uuid -> keyBagMap.computeIfAbsent(uuid, u -> fetchKeyBag(httpClient, u)));
        return this;
    }

    Set<KeyBagID> keyBagUUIDs(Collection<Asset> assets) {
        return assets.stream()
                .map(Asset::encryptionKey)
                .map(u -> u.flatMap(KeyBlob::uuid))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(KeyBagID::new)
                .collect(Collectors.toSet());
    }

    KeyBag fetchKeyBag(HttpClient httpClient, KeyBagID keyBagID) {
        // FAIL used to limit recurrent fetches on unavailable key bags.
        return keyBagClient.apply(httpClient, keyBagID)
                .orElseGet(() -> {
                    logger.warn(" --fetchKeyBag() - failed to fetch key bag: {}", keyBagID);
                    return FAIL;
                });
    }

    @Override
    public String toString() {
        return "KeyBagManager{" + "keyBagClient=" + keyBagClient + ", keyBagMap=" + keyBagMap + '}';
    }
}
