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
package com.github.horrorho.inflatabledonkey.cloud.clients;

import com.github.horrorho.inflatabledonkey.cloudkitty.CloudKitty;
import com.github.horrorho.inflatabledonkey.cloudkitty.operations.RecordRetrieveRequestOperations;
import com.github.horrorho.inflatabledonkey.data.backup.Assets;
import com.github.horrorho.inflatabledonkey.data.backup.AssetsFactory;
import com.github.horrorho.inflatabledonkey.data.backup.Manifest;
import com.github.horrorho.inflatabledonkey.data.backup.ManifestID;
import com.github.horrorho.inflatabledonkey.data.backup.ManifestIDIndex;
import com.github.horrorho.inflatabledonkey.pcs.zone.PZFactory;
import com.github.horrorho.inflatabledonkey.pcs.zone.ProtectionZone;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit;
import java.io.IOException;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AssetsClient.
 *
 * @author Ahseya
 */
@Immutable
public final class AssetsClient {

    private static final Logger logger = LoggerFactory.getLogger(AssetsClient.class);

    public static List<Assets>
            assets(HttpClient httpClient, CloudKitty kitty, ProtectionZone zone, Collection<Manifest> manifests)
            throws IOException {

        if (manifests.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> manifestIDs = manifestIDs(manifests);

        List<CloudKit.RecordRetrieveResponse> responses
                = RecordRetrieveRequestOperations.get(kitty, httpClient, "_defaultZone", manifestIDs);

        return assetsList(responses, zone);
    }

    static List<String> manifestIDs(Collection<Manifest> manifests) {
        return manifests
                .stream()
                .map(AssetsClient::manifestIDs)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    static List<String> manifestIDs(Manifest manifest) {
        return IntStream.range(0, manifest.count())
                .mapToObj(i -> new ManifestIDIndex(manifest.id(), i))
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    static List<Assets> assetsList(List<CloudKit.RecordRetrieveResponse> responses, ProtectionZone zone) {
        return groupByManifestID(responses)
                .values()
                .stream()
                .map(u -> assets(u, zone))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    static Optional<Assets> assets(List<CloudKit.Record> records, ProtectionZone zone) {
        return zone(records, zone)
                .flatMap(u -> AssetsFactory.from(records, u));
    }

    static Optional<ProtectionZone> zone(Collection<CloudKit.Record> records, ProtectionZone zone) {
        List<ProtectionZone> zones = records
                .stream()
                .filter(CloudKit.Record::hasProtectionInfo)
                .map(CloudKit.Record::getProtectionInfo)
                .map(u -> PZFactory.instance().create(zone, u))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        if (zones.size() != 1) {
            logger.warn("-- zone() - unexpected protection info count: {}", zones.size());
        }
        return zones.isEmpty()
                ? Optional.empty()
                : Optional.of(zones.get(0));
    }

    static Map<ManifestID, List<CloudKit.Record>>
            groupByManifestID(Collection<CloudKit.RecordRetrieveResponse> responses) {
        return responses
                .stream()
                .map(CloudKit.RecordRetrieveResponse::getRecord)
                .map(AssetsClient::manifestIDRecord)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
    }

    static Optional<Map.Entry<ManifestID, CloudKit.Record>> manifestIDRecord(CloudKit.Record record) {
        String name = record.getRecordIdentifier().getValue().getName();
        Optional<Map.Entry<ManifestID, CloudKit.Record>> entry = ManifestIDIndex.from(name)
                .map(u -> new SimpleImmutableEntry<>(u.id(), record));
        if (!entry.isPresent()) {
            logger.warn("-- manifestIDRecord() - no manifest id found: {}", name);
        }
        return entry;
    }
}
