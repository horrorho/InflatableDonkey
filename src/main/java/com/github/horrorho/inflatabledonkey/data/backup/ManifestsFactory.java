/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.data.backup;

import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import com.google.protobuf.ByteString;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ManifestsFactory.
 *
 * @author Ahseya
 */
@Immutable
public class ManifestsFactory {

    private static final Logger logger = LoggerFactory.getLogger(ManifestsFactory.class);

    public static Manifests from(CloudKit.Record record) {

        Map<String, String> attributes = attributes(record);
        List<Manifest> manifests = manifests(record.getRecordFieldList());
        Optional<byte[]> optionalBackupProperties = backupProperties(record.getRecordFieldList());

        Instant creation = WKTimestamp.toInstant(
                record.getTimeStatistics().getCreation().getTime());
        Instant modification = WKTimestamp.toInstant(
                record.getTimeStatistics().getModification().getTime());

        String protectionInfoTag = record.getProtectionInfo().getProtectionInfoTag();
        byte[] protectionInfo = record.getProtectionInfo().getProtectionInfo().toByteArray();

        return new Manifests(
                creation,
                modification,
                optionalBackupProperties,
                attributes,
                manifests,
                protectionInfoTag,
                protectionInfo);
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

    static List<Manifest> manifests(List<CloudKit.RecordField> records) {
        List<Integer> manifestCounts = manifestCounts(records);
        List<Integer> manifestChecksums = manifestChecksums(records);
        List<String> manifestIDs = manifestIDs(records);

        logger.debug("-- manifests() - counts: {}", manifestCounts.size());
        logger.debug("-- manifests() - checksums: {}", manifestChecksums.size());
        logger.debug("-- manifests() - manifestsIDs: {}", manifestIDs.size());

        if (manifestCounts.size() != manifestChecksums.size() || manifestChecksums.size() != manifestIDs.size()) {
            logger.warn("-- manifests() - mismatched manifest data");
        }

        int limit = Stream.<List<?>>of(manifestCounts, manifestChecksums, manifestIDs)
                .mapToInt(List::size)
                .min()
                .orElse(0);

        return IntStream.range(0, limit)
                .mapToObj(i -> new Manifest(
                        manifestCounts.get(i),
                        manifestChecksums.get(i),
                        manifestIDs.get(i)))
                .collect(Collectors.toList());
    }

    static List<Integer> manifestCounts(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(value -> value.getIdentifier().getName().equals("manifestCounts"))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getRecordFieldValueList)
                .flatMap(Collection::stream)
                .map(CloudKit.RecordFieldValue::getUint32)
                .collect(Collectors.toList());
    }

    static List<Integer> manifestChecksums(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(value -> value.getIdentifier().getName().equals("manifestChecksums"))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getRecordFieldValueList)
                .flatMap(Collection::stream)
                .map(CloudKit.RecordFieldValue::getUint32)
                .collect(Collectors.toList());
    }

    static List<String> manifestIDs(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(value -> value.getIdentifier().getName().equals("manifestIDs"))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getRecordFieldValueList)
                .flatMap(Collection::stream)
                .map(CloudKit.RecordFieldValue::getStringValue)
                .collect(Collectors.toList());
    }

    static Optional<byte[]> backupProperties(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(value -> value.getIdentifier().getName().equals("backupProperties"))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getBytesValue)
                .map(ByteString::toByteArray)
                .findFirst();
    }
}
