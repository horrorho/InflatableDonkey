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

import com.github.horrorho.inflatabledonkey.cloudkitty.CloudKittyLegacy;
import com.github.horrorho.inflatabledonkey.data.backup.Snapshot;
import com.github.horrorho.inflatabledonkey.data.backup.Snapshots;
import com.github.horrorho.inflatabledonkey.data.backup.SnapshotID;
import com.github.horrorho.inflatabledonkey.pcs.zone.PZFactory;
import com.github.horrorho.inflatabledonkey.pcs.zone.ProtectionZone;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SnapshotClient.
 *
 * @author Ahseya
 */
@Immutable
public final class SnapshotClient {

    private static final Logger logger = LoggerFactory.getLogger(SnapshotClient.class);

    public static List<Snapshot>
            snapshots(HttpClient httpClient, CloudKittyLegacy kitty, ProtectionZone zone, Collection<SnapshotID> snapshotIDs)
            throws IOException {

        if (snapshotIDs.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> snapshots = snapshotIDs.stream()
                .map(SnapshotID::id)
                .collect(Collectors.toList());

        List<CloudKit.RecordRetrieveResponse> responses
                = kitty.recordRetrieveRequest(httpClient, "mbksync", snapshots);
        logger.debug("-- manifests() - responses: {}", responses);

        return responses.stream()
                .filter(CloudKit.RecordRetrieveResponse::hasRecord)
                .map(CloudKit.RecordRetrieveResponse::getRecord)
                .map(r -> manifests(r, zone))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    static Optional<Snapshot> manifests(CloudKit.Record record, ProtectionZone zone) {
        return PZFactory.instance().create(zone, record.getProtectionInfo())
                .map(z -> Snapshots.from(record, (bs, id) -> z.decrypt(bs, id).get())); // TODO rework Manifests
    }
}
