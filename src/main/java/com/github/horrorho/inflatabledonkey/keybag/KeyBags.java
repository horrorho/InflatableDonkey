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
package com.github.horrorho.inflatabledonkey.keybag;

import com.google.protobuf.ByteString;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;

/**
 * KeyBags.
 *
 * @author Ahseya
 */
@Immutable
public final class KeyBags {

    static Map<ByteString, KeyBag> keyBags(Collection<KeyBag> keyBags) {
        return keyBags.stream()
                .collect(Collectors.toMap(
                        keyBag -> ByteString.copyFrom(keyBag.uuid()),
                        Function.identity()));
    }

    private final Map<ByteString, KeyBag> keyBags;

    KeyBags(Map<ByteString, KeyBag> keyBags) {
        this.keyBags = Objects.requireNonNull(keyBags, "keyBags");
    }

    public KeyBags(Collection<KeyBag> keybags) {
        this(keyBags(keybags));
    }

    public KeyBags(KeyBag... keybags) {
        this(Arrays.asList(keybags));
    }

    public Optional<KeyBag> uuid(byte[] uuid) {
        ByteString key = ByteString.copyFrom(uuid);
        return Optional.ofNullable(keyBags.get(key));
    }

    public Collection<KeyBag> keyBags() {
        return keyBags.values();
    }

    @Override
    public String toString() {
        return "KeyBags{" + "keyBags=" + keyBags + '}';
    }
}
