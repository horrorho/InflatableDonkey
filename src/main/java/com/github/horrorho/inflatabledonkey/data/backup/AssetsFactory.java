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
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import javax.annotation.concurrent.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.github.horrorho.inflatabledonkey.data.backup.AssetsFactory.from;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class AssetsFactory {

    private static final Logger logger = LoggerFactory.getLogger(AssetsFactory.class);

    private static final String DOMAIN = "domain";
    private static final String FILES = "files";

    public static Optional<Assets> from(List<CloudKit.Record> records, ProtectionZone protectionZone) {
        // records = ALL records that belong to a particular manifest.
        return records.isEmpty()
                ? Optional.empty()
                : manifestID(records).flatMap(id -> from(id, records, protectionZone));
    }

    static Optional<Assets> from(ManifestID id, List<CloudKit.Record> records, ProtectionZone protectionZone) {
        return domain(records, protectionZone::decrypt)
                .map(u -> from(id, u, records));
    }

    static Assets from(ManifestID id, String domain, List<CloudKit.Record> records) {
        List<AssetID> files = files(records);
        Assets assets = new Assets(id, domain, files);
        logger.debug("-- from() - manifestID: {} domain: {} files: {}",
                assets.manifestID(), assets.domain(), assets.count());
        return assets;
    }

    static Optional<ManifestID> manifestID(List<CloudKit.Record> records) {
        Set<ManifestID> ids = records.stream()
                .map(u -> u.getRecordIdentifier().getValue().getName())
                .map(ManifestIDIndex::from)
                .filter(Optional::isPresent)
                .map(u -> u.get().id())
                .collect(Collectors.toSet());
        if (ids.size() != 1) {
            logger.warn("-- manifestID() - multiple manifest ids found: {}", ids);
        }
        return ids.size() == 1
                ? Optional.of(ids.iterator().next())
                : Optional.empty();
    }

    static Optional<String>
            domain(List<CloudKit.Record> records, BiFunction<byte[], String, Optional<byte[]>> decrypt) {
        Optional<String> domain = records
                .stream()
                .map(CloudKit.Record::getRecordFieldList)
                .map(AssetsFactory::domain)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(u -> decrypt.apply(u, DOMAIN))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(String::new)
                .findFirst();
        if (!domain.isPresent()) {
            logger.warn("-- domain() - no domain found");
        }
        return domain;
    }

    static Optional<byte[]>
            domain(List<CloudKit.RecordField> records) {
        return records
                .stream()
                .filter(u -> u.getIdentifier().getName().equals(DOMAIN))
                .map(u -> u.getValue().getBytesValue().toByteArray())
                .findFirst();
    }

    static List<AssetID> files(List<CloudKit.Record> records) {
        return records
                .stream()
                .map(CloudKit.Record::getRecordFieldList)
                .flatMap(Collection::stream)
                .filter(u -> u.getIdentifier().getName().equals(FILES))
                .map(u -> u.getValue().getRecordFieldValueList())
                .flatMap(Collection::stream)
                .map(u -> u.getReferenceValue().getRecordIdentifier().getValue().getName())
                .map(AssetID::from)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
