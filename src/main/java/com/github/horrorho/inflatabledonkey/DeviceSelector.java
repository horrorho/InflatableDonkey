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
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.UnaryOperator;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import net.jcip.annotations.Immutable;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class DeviceSelector implements UnaryOperator<Map<Device, ? extends Collection<Snapshot>>> {

    private final Collection<String> selection;

    public DeviceSelector(Collection<String> selection) {
        this.selection = selection.stream()
                .map(u -> u.toLowerCase(Locale.US))
                .collect(toList());
    }

    @Override
    public Map<Device, ? extends Collection<Snapshot>>
            apply(Map<Device, ? extends Collection<Snapshot>> deviceSnapshots) {
        return selection.isEmpty()
                ? deviceSnapshots
                : doApply(deviceSnapshots);
    }

    public Map<Device, ? extends Collection<Snapshot>>
            doApply(Map<Device, ? extends Collection<Snapshot>> deviceSnapshots) {
        return deviceSnapshots.entrySet()
                .stream()
                .filter(this::match)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (u, v) -> u, LinkedHashMap::new));
    }

    boolean match(Map.Entry<Device, ? extends Collection<Snapshot>> entry) {
        String hash = entry.getKey()
                .deviceID()
                .hash()
                .toLowerCase(Locale.US);
        return selection.stream()
                .anyMatch(hash::contains);
    }
}
