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
import com.google.protobuf.ByteString;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snapshots.
 *
 * @author Ahseya
 */
@Immutable
public class Snapshots {

    private static final String BACKUP_PROPERTIES = "backupProperties";

    private static final Logger logger = LoggerFactory.getLogger(Snapshots.class);

    public static Snapshot from(CloudKit.Record record, BiFunction<byte[], String, byte[]> decrypt) {
        List<Manifest> manifests = manifests(record.getRecordFieldList());

        Optional<byte[]> optionalBackupProperties = backupProperties(record.getRecordFieldList())
                .map(k -> decrypt.apply(k, BACKUP_PROPERTIES));

        Snapshot snapshot = new Snapshot(optionalBackupProperties, manifests, record);
        logger.debug("-- from() - snapshot: {}", snapshot);
        return snapshot;
    }

    static List<Manifest> manifests(List<CloudKit.RecordField> records) {
        List<Integer> manifestCounts = manifestCounts(records);
        List<Integer> manifestChecksums = manifestChecksums(records);
        List<ManifestID> manifestIDs = manifestIDs(records);

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
                .map(CloudKit.RecordFieldValue::getSignedValue)
                .map(Long::intValue)
                .collect(Collectors.toList());
    }

    static List<Integer> manifestChecksums(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(value -> value.getIdentifier().getName().equals("manifestChecksums"))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getRecordFieldValueList)
                .flatMap(Collection::stream)
                .map(CloudKit.RecordFieldValue::getSignedValue)
                .map(Long::intValue)
                .collect(Collectors.toList());
    }

    static List<ManifestID> manifestIDs(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(value -> value.getIdentifier().getName().equals("manifestIDs"))
                .map(u -> u.getValue().getRecordFieldValueList())
                .flatMap(Collection::stream)
                .map(CloudKit.RecordFieldValue::getStringValue)
                .map(ManifestID::from)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    static Optional<byte[]> backupProperties(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(value -> value.getIdentifier().getName().equals(BACKUP_PROPERTIES))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getBytesValue)
                .map(ByteString::toByteArray)
                .findFirst();
    }
}
