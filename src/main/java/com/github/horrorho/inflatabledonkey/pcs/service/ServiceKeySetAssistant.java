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
package com.github.horrorho.inflatabledonkey.pcs.service;

import com.github.horrorho.inflatabledonkey.crypto.ec.key.ECPrivateKey;
import com.github.horrorho.inflatabledonkey.pcs.key.Key;
import com.github.horrorho.inflatabledonkey.pcs.key.KeyID;
import com.github.horrorho.inflatabledonkey.data.der.PrivateKey;
import com.github.horrorho.inflatabledonkey.data.der.PublicKeyInfo;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

/**
 * ServiceKeySetAssistant.
 *
 * @author Ahseya
 */
@Immutable
public final class ServiceKeySetAssistant {

    private static final Logger logger = LoggerFactory.getLogger(ServiceKeySetAssistant.class);

    public static Set<Key<ECPrivateKey>> importPrivateKeys(Collection<PrivateKey> keys,
            Function<PrivateKey, Optional<Key<ECPrivateKey>>> importPrivateKey) {
        // Logging via peeks here as opposed to introducing logging code downstream.
        Set<Key<ECPrivateKey>> importedKeys = keys.stream()
                .peek(u -> logger.debug("-- importPrivateKeys() > import: {}", u))
                .map(importPrivateKey)
                .peek(u -> {
                    if (u.isPresent()) {
                        logger.debug("-- importPrivateKeys() < import: {}", u.get());
                    } else {
                        logger.warn("-- importPrivateKeys() < import failed");
                    }
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toSet());
        if (importedKeys.size() != keys.size()) {
            logger.warn("-- importPrivateKeys() - duplicate keys/ unable to import all keys");
        }
        return importedKeys;
    }

    public static Set<Key<ECPrivateKey>> verifiedPrivateKeys(Collection<Key<ECPrivateKey>> keys) {
        Set<Key<ECPrivateKey>> masterKeys = verifiedMasterKeys(keys);
        Map<KeyID, Key<ECPrivateKey>> keyIDMasterKey = masterKeys
                .stream().collect(toMap(Key::keyID, Function.identity()));

        return keys.stream()
                .map(u -> masterKeys.contains(u)
                        ? u
                        : verifiedPrivateKey(u, keyIDMasterKey))
                .collect(toSet());
    }

    static Key<ECPrivateKey>
            verifiedPrivateKey(Key<ECPrivateKey> key, Map<KeyID, Key<ECPrivateKey>> keyIDMasterKey) {
        // Returns trusted key if possible, otherwise untrusted key.
        if (key.isTrusted()) {
            return key;
        }
        return key.masterKeyID()
                .filter(keyIDMasterKey::containsKey)
                .map(keyIDMasterKey::get)
                .flatMap(key::verified)
                .orElse(key);
    }

    static Set<Key<ECPrivateKey>> verifiedMasterKeys(Collection<Key<ECPrivateKey>> keys) {
        int masterKeyServiceNumber = Service.PCS_MASTERKEY.number();
        return keys
                .stream()
                .filter(u -> u
                        .service()
                        .map(v -> v == masterKeyServiceNumber)
                        .orElse(false))
                .map(Key::verifed)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toSet());
    }

    public static List<KeyID> untrustedKeys(Collection<Key<ECPrivateKey>> privateKeys) {
        return privateKeys.stream()
                .filter(privateKey -> !privateKey.isTrusted())
                .map(Key::keyID)
                .collect(toList());
    }

    public static Map<Integer, Set<Key<ECPrivateKey>>> serviceKeys(Collection<Key<ECPrivateKey>> keys) {
        return keys.stream()
                .collect(groupingBy(ServiceKeySetAssistant::service, toSet()));
    }

    static int service(Key<ECPrivateKey> key) {
        return key.publicKeyInfo()
                .map(u -> u.service())
                .orElseGet(() -> {
                    logger.debug("-- service() - missing key service: {}", key);
                    return Service.UNKNOWN.number();
                });
    }

    public static Map<Integer, Key<ECPrivateKey>>
            incongruentServiceKeys(Collection<Key<ECPrivateKey>> keys, Map<Integer, Set<KeyID>> serviceKeyIDs) {
        Map<KeyID, Key<ECPrivateKey>> map = keys.stream()
                .collect(toMap(Key::keyID, Function.identity()));
        return serviceKeyIDs.entrySet()
                .stream()
                .flatMap(e -> e
                        .getValue()
                        .stream()
                        .map(v -> new SimpleImmutableEntry<>(e.getKey(), v)))
                .filter(e -> map.containsKey(e.getValue()))
                .filter(e -> map
                        .get(e.getValue())
                        .publicKeyInfo()
                        .map(PublicKeyInfo::service)
                        .map(u -> !e.getKey().equals(u))
                        .orElse(false))
                .collect(toMap(Map.Entry::getKey, e -> map.get(e.getValue())));
    }
}
