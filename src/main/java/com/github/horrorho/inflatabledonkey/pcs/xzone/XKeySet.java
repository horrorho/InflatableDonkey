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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * XKeySet. Experimental proof of concept code.
 *
 * @author Ahseya
 */
@Immutable
public final class XKeySet {

    public static XKeySet empty() {
        return EMPTY;
    }

    private static final Logger logger = LoggerFactory.getLogger(XKeySet.class);

    private static final XKeySet EMPTY = new XKeySet(new HashMap<>());

    private final Map<KeyID, Key<ECPrivate>> keys;

    private XKeySet(Map<KeyID, Key<ECPrivate>> keys) {
        this.keys = keys;
    }

    public XKeySet(Collection<Key<ECPrivate>> keys) {
        this.keys = keys.stream()
                .collect(Collectors.toMap(
                        Key::keyID,
                        Function.identity(),
                        (a, b) -> {
                            logger.warn("** XKeySet() - duplicate key: {}", a);
                            return a;
                        }));
    }

    public Optional<Key<ECPrivate>> key(KeyID keyID) {
        return keys.containsKey(keyID)
                ? Optional.of(keys.get(keyID))
                : Optional.empty();
    }

    public Map<KeyID, Key<ECPrivate>> keys() {
        return new HashMap<>(keys);
    }

    public XKeySet with(Key<ECPrivate> key) {
        HashMap<KeyID, Key<ECPrivate>> copy = new HashMap<>(keys);
        copy.put(key.keyID(), key);
        return new XKeySet(copy);
    }

    public XKeySet withAll(Collection<Key<ECPrivate>> keys) {
        HashMap<KeyID, Key<ECPrivate>> copy = new HashMap<>(this.keys);
        keys.forEach(key -> copy.put(key.keyID(), key));
        return new XKeySet(copy);
    }

    public XKeySet withAll(XKeySet other) {
        return XKeySet.this.withAll(other.keys().values());
    }

    @Override
    public String toString() {
        return "XKeySet{" + "keys=" + keys + '}';
    }
}
