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
package com.github.horrorho.inflatabledonkey.args.filter;

import com.github.horrorho.inflatabledonkey.data.backup.Device;
import com.github.horrorho.inflatabledonkey.data.backup.Snapshot;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import static java.util.stream.Collectors.toMap;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class ArgsSelector implements UnaryOperator<Map<Device, List<Snapshot>>> {

    private final Set<String> uuids;
    private final IndexFilter<Snapshot> snapshotFilter;

    public ArgsSelector(Collection<String> deviceUUIDs, IndexFilter<Snapshot> snapshotFilter) {
        this.uuids = deviceUUIDs.isEmpty()
                ? new HashSet<>(Arrays.asList(""))
                : deviceUUIDs.stream().map(u -> u.toLowerCase(Locale.US)).collect(Collectors.toSet());
        this.snapshotFilter = Objects.requireNonNull(snapshotFilter);
    }

    public ArgsSelector(Collection<String> deviceUUIDs, Collection<Integer> snapshotIndices) {
        this(deviceUUIDs, new IndexFilter(snapshotIndices));
    }

    @Override
    public Map<Device, List<Snapshot>> apply(Map<Device, List<Snapshot>> deviceSnapshots) {
        return deviceSnapshots
                .entrySet()
                .stream()
                .map(e -> new SimpleImmutableEntry<>(e.getKey(), snapshots(e.getValue())))
                .filter(this::device)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    List<Snapshot> snapshots(Collection<Snapshot> snapshots) {
        return snapshotFilter.apply(new ArrayList<>(snapshots));
    }

    boolean device(Map.Entry<Device, ? extends Collection<Snapshot>> entry) {
        return entry.getValue().isEmpty()
                ? false
                : uuids.contains(entry.getKey().deviceID().hash().toLowerCase(Locale.US));
    }
}
