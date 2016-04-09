/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.data.backup;

import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

        Map<String, String> attributes = attributes(record);
        Map<Instant, String> snapshots = snapshots(record.getRecordFieldList());

        String domainHMAC = attributes.remove("domainHMAC");
        String currentKeybagUUID = attributes.remove("currentKeybagUUID");

        Instant creation = WKTimestamp.toInstant(
                record.getTimeStatistics().getCreation().getTime());
        Instant modification = WKTimestamp.toInstant(
                record.getTimeStatistics().getModification().getTime());

        return new Snapshots(
                creation,
                modification,
                snapshots,
                domainHMAC == null ? "" : domainHMAC,
                currentKeybagUUID == null ? "" : currentKeybagUUID,
                attributes);

    }

    static Map<String, String> attributes(CloudKit.Record record) {
        return record.getRecordFieldList()
                .stream()
                .filter(f -> f.getValue().hasStringValue())
                .collect(Collectors.toMap(
                        f -> f.getIdentifier().getName(),
                        f -> f.getValue().getStringValue(),
                        (a, b) -> {
                            logger.warn("-- attributes() - duplicate items: {} {}", a, b);
                            return a;
                        }));
    }

    static Map<Instant, String> snapshots(List<CloudKit.RecordField> records) {
        List<String> snapshotRecords = snapshotRecords(records);
        List<Double> snapshotCommittedDates = snapshotCommittedDates(records);

        if (snapshotRecords.size() != snapshotCommittedDates.size()) {
            logger.warn("-- snapshots() - mismatched snapshot records/ committed dates sizes: {} {}",
                    snapshotRecords, snapshotCommittedDates);
        }

        Map<Instant, String> snapshots = new HashMap<>();
        for (int i = 0; i < snapshotRecords.size(); i++) {
            double committedDate = i < snapshotCommittedDates.size()
                    ? snapshotCommittedDates.get(i)
                    : 0;

            Instant timestamp = WKTimestamp.toInstant(committedDate);
            snapshots.put(timestamp, snapshotRecords.get(i));
        }

        return snapshots;
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
