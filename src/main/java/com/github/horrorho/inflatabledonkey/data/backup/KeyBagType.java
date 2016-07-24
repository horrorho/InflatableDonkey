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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.jcip.annotations.Immutable;

/**
 * KeybagType.
 *
 * @author ahseya
 */
@Immutable
public enum KeyBagType {

    SYSTEM("System"),
    BACKUP("Backup"),
    ESCROW("Escrow"),
    OTA("OTA (icloud)");

    private static final Map<Integer, KeyBagType> intToType
            = Stream.of(KeyBagType.values()).collect(Collectors.toMap(KeyBagType::value, Function.identity()));

    public static KeyBagType from(int value) {
        KeyBagType type = intToType.get(value & 0x3FFFFFFF);
        if (type == null) {
            throw new IllegalArgumentException("unknown KeyBagType: " + value);
        }
        return type;
    }

    private final String toString;

    private KeyBagType(String description) {
        this.toString = description;
    }

    @Override
    public String toString() {
        return toString;
    }

    public int value() {
        return ordinal();
    }
}
