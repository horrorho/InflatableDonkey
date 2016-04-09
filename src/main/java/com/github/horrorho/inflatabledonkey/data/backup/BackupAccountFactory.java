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
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BackupAccountFactory.
 *
 * @author Ahseya
 */
@Immutable
public final class BackupAccountFactory {

    private static final Logger logger = LoggerFactory.getLogger(BackupAccountFactory.class);

    public static BackupAccount from(CloudKit.Record record) {

        List<CloudKit.RecordField> records = record.getRecordFieldList();

        byte[] hmacKey = hmacKey(records);
        Collection<String> devices = devices(records);

        Instant creation = WKTimestamp.toInstant(
                record.getTimeStatistics().getCreation().getTime());
        Instant modification = WKTimestamp.toInstant(
                record.getTimeStatistics().getModification().getTime());

        String protectionInfoTag = record.getProtectionInfo().getProtectionInfoTag();
        byte[] protectionInfo = record.getProtectionInfo().getProtectionInfo().toByteArray();

        return new BackupAccount(
                creation,
                modification,
                hmacKey,
                devices,
                protectionInfoTag,
                protectionInfo);
    }

    public static byte[] hmacKey(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(value -> value.getIdentifier().getName().equals("HMACKey"))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getBytesValue)
                .findFirst()
                .orElseGet(() -> {
                    logger.warn("-- hmacKey() - not found");
                    return ByteString.EMPTY;
                })
                .toByteArray();
    }

    public static Collection<String> devices(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(value -> value.getIdentifier().getName().equals("devices"))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getRecordFieldValueList)
                .flatMap(Collection::stream)
                .map(CloudKit.RecordFieldValue::getReferenceValue)
                .map(CloudKit.RecordReference::getRecordIdentifier)
                .map(CloudKit.RecordIdentifier::getValue)
                .map(CloudKit.Identifier::getName)
                .collect(Collectors.toList());
    }
}
