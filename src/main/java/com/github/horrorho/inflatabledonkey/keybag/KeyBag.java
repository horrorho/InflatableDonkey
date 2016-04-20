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
package com.github.horrorho.inflatabledonkey.keybag;

import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;

/**
 * Keybag
 *
 * @author ahseya
 */
@Immutable
public final class KeyBag {

    private final KeyBagType type;
    private final byte[] uuid;
    private final String uuidBase64;
    private final Map<Integer, byte[]> publicKeys;
    private final Map<Integer, byte[]> privateKeys;

    public KeyBag(
            KeyBagType type,
            byte[] uuid,
            Map<Integer, byte[]> publicKeys,
            Map<Integer, byte[]> privateKeys) {

        this.type = Objects.requireNonNull(type, "type");
        this.uuid = Objects.requireNonNull(uuid, "uuid");
        this.uuidBase64 = Base64.getEncoder().encodeToString(uuid);

        this.publicKeys = publicKeys.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        bs -> Arrays.copyOf(bs.getValue(), bs.getValue().length)));

        this.privateKeys = privateKeys.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        bs -> Arrays.copyOf(bs.getValue(), bs.getValue().length)));
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

    public byte[] uuid() {
        return Arrays.copyOf(uuid, uuid.length);
    }

    public String uuidBase64() {
        return uuidBase64;
    }

    @Override
    public String toString() {
        return "KeyBag{"
                + "type=" + type
                + ", uuid=0x" + Hex.toHexString(uuid)
                + ", publicKeys=" + publicKeys
                + ", privateKeys=" + privateKeys
                + '}';
    }
}
