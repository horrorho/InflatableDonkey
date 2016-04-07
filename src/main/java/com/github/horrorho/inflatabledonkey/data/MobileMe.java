/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.data;

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
