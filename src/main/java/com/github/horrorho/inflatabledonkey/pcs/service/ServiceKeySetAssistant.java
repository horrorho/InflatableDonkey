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

import com.github.horrorho.inflatabledonkey.crypto.eckey.ECPrivate;
import com.github.horrorho.inflatabledonkey.crypto.key.Key;
import com.github.horrorho.inflatabledonkey.crypto.key.KeyID;
import com.github.horrorho.inflatabledonkey.data.der.PrivateKey;
import com.github.horrorho.inflatabledonkey.data.der.PublicKeyInfo;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ServiceKeySetAssistant.
 *
 * @author Ahseya
 */
@Immutable
public final class ServiceKeySetAssistant {

    private static final Logger logger = LoggerFactory.getLogger(ServiceKeySetAssistant.class);

    public static Map<KeyID, Key<ECPrivate>> importPrivateKeys(
            Collection<PrivateKey> keys,
            Function<PrivateKey, Optional<Key<ECPrivate>>> importFunction) {

        // Logging via peeks here as opposed to introducing logging code downstream.
        Map<KeyID, Key<ECPrivate>> importedKeys = keys.stream()
                .peek(pk -> {
                    logger.debug("-- importPrivateKeys() > import: {}", pk);
                })
                .map(importFunction)
                .peek(privateKey -> {
                    if (privateKey.isPresent()) {
                        logger.debug("-- importPrivateKeys() < import: {}", privateKey.get());

                    } else {
                        logger.warn("-- importPrivateKeys() < import failed");
                    }
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(
                        Key::keyID,
                        Function.identity(),
                        (a, b) -> {
                            logger.warn("-- importPrivateKeys() - duplicate key ID: {}", a.keyID());
                            return a;
                        }));

        if (importedKeys.size() != keys.size()) {
            logger.warn("-- importPrivateKeys() - duplicate keys/ unable to import all keys");
        }

        return importedKeys;
    }

    public static Map<KeyID, Key<ECPrivate>> verifyKeys(Collection<Key<ECPrivate>> privateKeys, Key<ECPrivate> masterKey) {
        if (!masterKey.isTrusted()) {
            logger.warn("-- verifyKeys() - master key is not trusted: {}", masterKey.keyID());
            return new HashMap<>();

        } else {
            logger.debug("-- verifyKeys() - master key id: {}", masterKey.keyID());
        }

        return privateKeys.stream()
                .map(key -> key.keyID().equals(masterKey.keyID()) ? masterKey : key.verify(masterKey))
                .filter(Key::isTrusted)
                .collect(Collectors.toMap(Key::keyID, Function.identity()));
    }

    public static List<KeyID> untrustedKeys(Collection<Key<ECPrivate>> privateKeys) {
        return privateKeys.stream()
                .filter(privateKey -> !privateKey.isTrusted())
                .map(Key::keyID)
                .collect(Collectors.toList());
    }

    public static Optional<Key<ECPrivate>> keyForService(Collection<Key<ECPrivate>> privateKeys, int service) {
        return privateKeys.stream()
                .filter(privateKey -> privateKey.service()
                        .map(s -> s == service)
                        .orElse(false))
                .findFirst();
    }

    public static List<Key<ECPrivate>>
            unreferencedKeys(Map<Integer, KeyID> serviceKeyIDs, Map<KeyID, Key<ECPrivate>> privateKeys) {

        HashSet<KeyID> keyIDs = new HashSet<>(serviceKeyIDs.values());
        Predicate<Map.Entry<KeyID, Key<ECPrivate>>> isOrphaned = entry -> !keyIDs.contains(entry.getKey());

        return privateKeys.entrySet()
                .stream()
                .filter(isOrphaned)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public static Map<Integer, Key<ECPrivate>>
            serviceKeys(Map<Integer, KeyID> serviceKeyIDs, Map<KeyID, Key<ECPrivate>> privateKeys) {

        Predicate<Map.Entry<Integer, KeyID>> isPresent = entry -> {
            if (privateKeys.containsKey(entry.getValue())) {
                return true;

            } else {
                logger.warn("-- serviceKeys() - missing key service: {}, key id: {}", entry.getKey(), entry.getValue());
                return false;
            }
        };

        return serviceKeyIDs.entrySet()
                .stream()
                .filter(isPresent)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> privateKeys.get(entry.getValue())));
    }

    public static Map<Integer, Key<ECPrivate>> incongruentKeys(Map<Integer, Key<ECPrivate>> serviceKeys) {
        Predicate<Map.Entry<Integer, Key<ECPrivate>>> isIncongruent = entry
                -> entry.getValue().publicKeyInfo()
                .map(PublicKeyInfo::service)
                .map(s -> !entry.getKey().equals(s))
                .orElse(false);

        return serviceKeys.entrySet()
                .stream()
                .filter(isIncongruent)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
