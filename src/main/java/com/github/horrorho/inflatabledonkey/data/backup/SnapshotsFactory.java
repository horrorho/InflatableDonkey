/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.data.backup;

import static com.github.horrorho.inflatabledonkey.data.backup.ManifestsFactory.manifestCounts;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public final class SnapshotsFactory {

    private static final Logger logger = LoggerFactory.getLogger(SnapshotsFactory.class);

    public static Snapshots from(CloudKit.Record record) {

        List<Snapshot> snapshots = snapshots(record.getRecordFieldList());

        return new Snapshots(snapshots, record.getRecordFieldList());
    }

    static List<Snapshot> snapshots(List<CloudKit.RecordField> records) {
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
                .mapToObj(i -> new Snapshot(
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
