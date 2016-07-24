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
package com.github.horrorho.inflatabledonkey;

import com.github.horrorho.inflatabledonkey.data.backup.Device;
import com.github.horrorho.inflatabledonkey.data.backup.Snapshot;
import com.github.horrorho.inflatabledonkey.util.Selector;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.NotThreadSafe;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class UserSelector implements UnaryOperator<Map<Device, ? extends Collection<Snapshot>>> {

    public static final UserSelector instance() {
        return INSTANCE;
    }

    private static final UserSelector INSTANCE = new UserSelector();

    private UserSelector() {
    }

    @Override
    public Map<Device, ? extends Collection<Snapshot>> apply(Map<Device, ? extends Collection<Snapshot>> deviceSnapshots) {
        if (deviceSnapshots.isEmpty()) {
            return Collections.emptyMap();
        }

        System.out.println();
        Map<String, Map<Device, Collection<Snapshot>>> choices = new Builder().add(deviceSnapshots).build();

        System.out.println("\nInput selection (multiple values accepted, leave blank to select all, q to quit):");
        return Selector.builder(choices).build()
                .get()
                .map(this::combine)
                .map(m -> m.isEmpty() ? deviceSnapshots : m)
                .orElse(Collections.emptyMap());
    }

    Map<Device, ? extends Collection<Snapshot>> combine(Set<Map<Device, Collection<Snapshot>>> set) {
        return set.stream()
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (u, v) -> Stream.of(u, v)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                        LinkedHashMap::new));
    }

    @NotThreadSafe
    static class Builder {

        private static final List<String> DEVICE_TOKENS
                = IntStream.range('A', 'Z' + 1).mapToObj(i -> (char) i).map(Object::toString).collect(Collectors.toList());

        private final Iterator<String> deviceTokens;
        private final Map<String, Map<Device, Collection<Snapshot>>> choices = new HashMap<>();
        private int snapshotIndex = 1;

        Builder(List<String> deviceTokens) {
            this.deviceTokens = deviceTokens.iterator();
        }

        Builder() {
            this(DEVICE_TOKENS);
        }

        Builder add(Map<Device, ? extends Collection<Snapshot>> deviceSnapshots) {
            deviceSnapshots.entrySet()
                    .stream()
                    .forEach(entry -> {
                        device(entry.getKey(), entry.getValue());
                        snapshots(entry.getKey(), entry.getValue());
                    });
            return this;
        }

        Builder snapshots(Device device, Collection<Snapshot> snapshots) {
            if (snapshots.isEmpty()) {
                System.out.println("\tNo snapshots.");
                return this;
            }
            snapshots.forEach(s -> snapshot(device, s));
            return this;
        }

        Builder snapshot(Device device, Snapshot snapshot) {
            String token = Integer.toString(snapshotIndex++);
            System.out.println(pad(token, 2) + ":\t" + snapshot.info());
            putChoice(token, device, Arrays.asList(snapshot));
            return this;
        }

        Builder device(Device device, Collection<Snapshot> snapshots) {
            if (!deviceTokens.hasNext()) {
                return this;
            }
            String token = deviceTokens.next();
            System.out.println(pad(token, 2) + ": DEVICE - " + device.info());
            putChoice(token, device, snapshots);
            return this;
        }

        void putChoice(String token, Device device, Collection<Snapshot> snapshots) {
            Map<Device, Collection<Snapshot>> map = new HashMap<>();
            map.put(device, snapshots);
            choices.put(token, map);
        }

        String pad(String token, int pad) {
            return String.format("%" + pad + "s", token);
        }

        Map<String, Map<Device, Collection<Snapshot>>> build() {
            return choices;
        }
    }
}
