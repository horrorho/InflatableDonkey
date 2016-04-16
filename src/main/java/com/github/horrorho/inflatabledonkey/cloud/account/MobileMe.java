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
package com.github.horrorho.inflatabledonkey.cloud.account;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSString;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;

/**
 * MobileMe.
 *
 * @author Ahseya
 */
@Immutable
public final class MobileMe {

    static Map<String, Map<String, String>> dictionaries(NSDictionary mobileme) {
        return mobileme.entrySet()
                .stream()
                .filter(entry -> entry.getValue() instanceof NSDictionary)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> dictionary((NSDictionary) entry.getValue())));
    }

    static Map<String, String> dictionary(NSDictionary dictionary) {
        return dictionary.entrySet()
                .stream()
                .filter(entry -> entry.getValue() instanceof NSString)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> ((NSString) entry.getValue()).getContent()));
    }

    private final Map<String, Map<String, String>> dictionaries;

    MobileMe(Map<String, Map<String, String>> dictionaries) {
        this.dictionaries = Objects.requireNonNull(dictionaries, "dictionaries");
    }

    public MobileMe(NSDictionary mobileme) {
        this(dictionaries(mobileme));
    }

    public Optional<String> get(String domain, String key) {
        return Optional.ofNullable(dictionaries.get(domain))
                .map(map -> map.get(key));
    }

    @Override
    public String toString() {
        return "MobileMe{" + "domains=" + dictionaries + ", values=" + dictionaries + '}';
    }
}
