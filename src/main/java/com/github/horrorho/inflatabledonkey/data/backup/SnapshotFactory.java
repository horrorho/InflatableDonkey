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

import com.github.horrorho.inflatabledonkey.pcs.zone.ProtectionZone;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit;
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
public final class SnapshotFactory {

    private static final String BACKUP_PROPERTIES = "backupProperties";
    private static final String MANIFEST_CHECKSUMS = "manifestChecksums";
    private static final String MANIFEST_COUNTS = "manifestCounts";
    private static final String MANIFEST_IDS = "manifestIDs";

    private static final Logger logger = LoggerFactory.getLogger(SnapshotFactory.class);

    public static Optional<Snapshot> from(CloudKit.Record record, ProtectionZone zone) {
        return SnapshotID.from(record.getRecordIdentifier().getValue().getName())
                .map(u -> from(u, record, zone));
    }

    static Snapshot from(SnapshotID snapshotID, CloudKit.Record record, ProtectionZone zone) {
        List<Manifest> manifests = manifests(record.getRecordFieldList());
        Optional<byte[]> backupProperties = backupProperties(record.getRecordFieldList())
                .flatMap(u -> zone.decrypt(u, BACKUP_PROPERTIES));

        Snapshot snapshot = new Snapshot(record, snapshotID, backupProperties, manifests);
        logger.info("-- from() - snapshot: {}", snapshot);
        return snapshot;
    }

    static List<Manifest> manifests(List<CloudKit.RecordField> records) {
        List<ManifestID> manifestIDs = manifestIDs(records);
        List<Integer> manifestCounts = manifestCounts(records);
        List<Integer> manifestChecksums = manifestChecksums(records);
        logger.debug("-- manifests() - manifestsIDs: {}", manifestIDs.size());
        logger.debug("-- manifests() - counts: {}", manifestCounts.size());
        logger.debug("-- manifests() - checksums: {}", manifestChecksums.size());
        if (manifestCounts.size() != manifestChecksums.size() || manifestChecksums.size() != manifestIDs.size()) {
            logger.warn("-- manifests() - mismatched manifest data");
        }

        int limit = Math.min(manifestCounts.size(), Math.min(manifestChecksums.size(), manifestIDs.size()));
        return IntStream.range(0, limit)
                .mapToObj(i -> new Manifest(
                        manifestIDs.get(i),
                        manifestCounts.get(i),
                        manifestChecksums.get(i)))
                .collect(Collectors.toList());
    }

    static List<Integer> manifestCounts(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(u -> u
                        .getIdentifier()
                        .getName()
                        .equals(MANIFEST_COUNTS))
                .map(u -> u
                        .getValue()
                        .getRecordFieldValueList())
                .flatMap(Collection::stream)
                .map(CloudKit.RecordFieldValue::getSignedValue)
                .map(Long::intValue)
                .collect(Collectors.toList());
    }

    static List<Integer> manifestChecksums(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(u -> u
                        .getIdentifier()
                        .getName()
                        .equals(MANIFEST_CHECKSUMS))
                .map(u -> u
                        .getValue()
                        .getRecordFieldValueList())
                .flatMap(Collection::stream)
                .map(CloudKit.RecordFieldValue::getSignedValue)
                .map(Long::intValue)
                .collect(Collectors.toList());
    }

    static List<ManifestID> manifestIDs(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(u -> u
                        .getIdentifier()
                        .getName()
                        .equals(MANIFEST_IDS))
                .map(u -> u
                        .getValue()
                        .getRecordFieldValueList())
                .flatMap(Collection::stream)
                .map(CloudKit.RecordFieldValue::getStringValue)
                .map(ManifestID::from)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    static Optional<byte[]> backupProperties(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(u -> u
                        .getIdentifier()
                        .getName()
                        .equals(BACKUP_PROPERTIES))
                .map(u -> u
                        .getValue()
                        .getBytesValue()
                        .toByteArray())
                .findFirst();
    }
}
