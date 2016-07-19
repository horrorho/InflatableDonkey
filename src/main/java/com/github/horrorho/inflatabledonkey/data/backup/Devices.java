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
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SnapshotsFactory.
 *
 * @author Ahseya
 */
@Immutable
public final class Devices {

    private static final Logger logger = LoggerFactory.getLogger(Devices.class);

    public static Device from(CloudKit.Record record) {

        List<SnapshotID> snapshots = snapshots(record.getRecordFieldList());

        return new Device(snapshots, record);
    }

    static List<SnapshotID> snapshots(List<CloudKit.RecordField> records) {
        List<String> snapshotRecords = snapshotRecords(records);
        List<Double> snapshotCommittedDates = snapshotCommittedDates(records);

        logger.debug("-- snapshots() - records: {}", snapshotRecords.size());
        logger.debug("-- snapshots() - dates: {}", snapshotCommittedDates.size());

        if (snapshotRecords.size() != snapshotCommittedDates.size()) {
            logger.warn("-- snapshots()() - mismatched snapshot data");
        }

        int limit = Stream.<List<?>>of(snapshotRecords, snapshotCommittedDates)
                .mapToInt(List::size)
                .min()
                .orElse(0);

        return IntStream.range(0, limit)
                .mapToObj(i -> new SnapshotID(
                        WKTimestamp.toInstant(snapshotCommittedDates.get(i)),
                        snapshotRecords.get(i)))
                .collect(Collectors.toList());
    }

    static List<String> snapshotRecords(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(value -> value.getIdentifier().getName().equals("snapshots"))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getRecordFieldValueList)
                .flatMap(Collection::stream)
                .map(CloudKit.RecordFieldValue::getReferenceValue)
                .map(CloudKit.RecordReference::getRecordIdentifier)
                .map(CloudKit.RecordIdentifier::getValue)
                .map(CloudKit.Identifier::getName)
                .collect(Collectors.toList());
    }

    static List<Double> snapshotCommittedDates(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(value -> value.getIdentifier().getName().equals("snapshotCommittedDates"))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getRecordFieldValueList)
                .flatMap(Collection::stream)
                .map(CloudKit.RecordFieldValue::getDateValue)
                .map(CloudKit.Date::getTime)
                .collect(Collectors.toList());
    }
}
