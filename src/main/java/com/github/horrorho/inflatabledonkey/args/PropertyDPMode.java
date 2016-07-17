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
package com.github.horrorho.inflatabledonkey.args;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;

/**
 * Data Protection mode.
 *
 * @author Ahseya
 */
@Immutable
public enum PropertyDPMode {
    CBC,
    XTS,
    OFF;

    public static Optional<PropertyDPMode> parse(String mode) {
        return Optional.ofNullable(map.get(mode.toUpperCase(Property.locale())));
    }

    public static String options() {
        return Arrays.asList(PropertyDPMode.values())
                .stream()
                .map(PropertyDPMode::name)
                .map(s -> s.toLowerCase(Property.locale()))
                .collect(Collectors.joining(" "));
    }

    private static final Map<String, PropertyDPMode> map = Arrays.asList(PropertyDPMode.values())
            .stream()
            .collect(Collectors.toMap(PropertyDPMode::name, Function.identity()));
}
