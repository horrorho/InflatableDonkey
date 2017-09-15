/* 
 * The MIT License
 *
 * Copyright 2015 Ahseya.
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
package com.github.horrorho.inflatabledonkey.data.backup;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.concurrent.Immutable;
import org.bouncycastle.util.Arrays;

/**
 *
 * @author ahseya
 */
@Immutable
public final class KeyBag {

    private final KeyBagID keyBagID;
    private final KeyBagType type;
    private final Map<Integer, byte[]> publicKeys;
    private final Map<Integer, byte[]> privateKeys;

    public KeyBag(
            KeyBagID keyBagID,
            KeyBagType type,
            Map<Integer, byte[]> publicKeys,
            Map<Integer, byte[]> privateKeys) {

        this.keyBagID = Objects.requireNonNull(keyBagID);
        this.type = Objects.requireNonNull(type, "type");
        this.publicKeys = publicKeys.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        bs -> Arrays.copyOf(bs.getValue(), bs.getValue().length)));
        this.privateKeys = privateKeys.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        u -> Arrays.copyOf(u.getValue(), u.getValue().length)));
    }

    public Optional<byte[]> publicKey(int protectionClass) {
        return Optional.ofNullable(publicKeys.get(protectionClass))
                .map(k -> Arrays.copyOf(k, k.length));
    }

    public Optional<byte[]> privateKey(int protectionClass) {
        return Optional.ofNullable(privateKeys.get(protectionClass))
                .map(k -> Arrays.copyOf(k, k.length));
    }

    public KeyBagType type() {
        return type;
    }

    public KeyBagID keyBagID() {
        return keyBagID;
    }

    @Override
    public String toString() {
        return "KeyBag{"
                + "keyBagID=" + keyBagID
                + ", type=" + type
                + ", publicKeys=" + publicKeys
                + ", privateKeys=" + privateKeys
                + '}';
    }
}
