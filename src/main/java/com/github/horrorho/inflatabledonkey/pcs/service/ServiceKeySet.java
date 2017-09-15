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
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import javax.annotation.concurrent.Immutable;

/**
 * ServiceKeySet.
 *
 * @author Ahseya
 */
@Immutable
public final class ServiceKeySet {

    static <K, V> Map<K, Set<V>> copy(Map<K, Set<V>> map) {
        return map.entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, u -> new HashSet<>(u.getValue())));
    }

    private final Map<Integer, Set<Key<ECPrivateKey>>> serviceKeys;
    private final String name;
    private final String ksID;
    private final boolean isCompact;

    ServiceKeySet(
            Map<Integer, Set<Key<ECPrivateKey>>> serviceKeys,
            String name,
            String ksID,
            boolean isCompact) {
        this.serviceKeys = copy(serviceKeys);
        this.name = Objects.requireNonNull(name);
        this.ksID = Objects.requireNonNull(ksID);
        this.isCompact = isCompact;
    }

    public Set<Key<ECPrivateKey>> key(Service service) {
        return key(service.number());
    }

    public Set<Key<ECPrivateKey>> key(int service) {
        return serviceKeys.containsKey(service)
                ? new HashSet<>(serviceKeys.get(service))
                : new HashSet<>();
    }

    public Optional<Key<ECPrivateKey>> key(KeyID keyID) {
        return serviceKeys.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(key -> key.keyID().equals(keyID))
                .findFirst();
    }

    public Map<Integer, Set<Key<ECPrivateKey>>> serviceKeys() {
        return copy(serviceKeys);
    }

    public Collection<Key<ECPrivateKey>> keys() {
        return serviceKeys.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(toSet());
    }

    public String name() {
        return name;
    }

    public String ksID() {
        return ksID;
    }

    public boolean isCompact() {
        return isCompact;
    }

    @Override
    public String toString() {
        return "ServiceKeys{"
                + "keys=" + serviceKeys
                + ", name=" + name
                + ", ksID=" + ksID
                + ", isCompact=" + isCompact
                + '}';
    }
}
