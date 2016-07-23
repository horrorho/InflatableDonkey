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
package com.github.horrorho.inflatabledonkey.data.backup;

import com.github.horrorho.inflatabledonkey.protobuf.CloudKit;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class DeviceFactory {

    private static final Logger logger = LoggerFactory.getLogger(DeviceFactory.class);

    public static Optional<Device> from(CloudKit.Record record) {
        return DeviceID.from(record.getRecordIdentifier().getValue().getName())
                .map(u -> from(u, record));
    }

    static Device from(DeviceID deviceID, CloudKit.Record record) {
        return new Device(record, deviceID, snapshots(record.getRecordFieldList()));
    }

    static List<SnapshotIDTimestamp> snapshots(List<CloudKit.RecordField> records) {
        List<SnapshotID> snapshotRecords = snapshotRecords(records);
        List<Instant> snapshotCommittedDates = snapshotCommittedDates(records);
        logger.debug("-- snapshots() - records: {}", snapshotRecords.size());
        logger.debug("-- snapshots() - dates: {}", snapshotCommittedDates.size());
        if (snapshotRecords.size() != snapshotCommittedDates.size()) {
            logger.warn("-- snapshots()() - mismatched snapshot data");
        }

        int limit = Math.min(snapshotRecords.size(), snapshotCommittedDates.size());
        return IntStream.range(0, limit)
                .mapToObj(i -> new SnapshotIDTimestamp(snapshotRecords.get(i), snapshotCommittedDates.get(i)))
                .collect(Collectors.toList());
    }

    static List<SnapshotID> snapshotRecords(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(u -> u
                        .getIdentifier()
                        .getName()
                        .equals("snapshots"))
                .map(u -> u
                        .getValue()
                        .getRecordFieldValueList())
                .flatMap(Collection::stream)
                .map(u -> u
                        .getReferenceValue()
                        .getRecordIdentifier()
                        .getValue()
                        .getName())
                .map(SnapshotID::from)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    static List<Instant> snapshotCommittedDates(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(u -> u
                        .getIdentifier()
                        .getName()
                        .equals("snapshotCommittedDates"))
                .map(u -> u
                        .getValue()
                        .getRecordFieldValueList())
                .flatMap(Collection::stream)
                .map(u -> u
                        .getDateValue()
                        .getTime())
                .map(WKTimestamp::toInstant)
                .collect(Collectors.toList());
    }
}
