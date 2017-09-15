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
import com.github.horrorho.inflatabledonkey.cloudkitty.operations.ZoneRetrieveRequestOperations;
import com.github.horrorho.inflatabledonkey.crypto.ec.key.ECPrivateKey;
import com.github.horrorho.inflatabledonkey.pcs.key.Key;
import com.github.horrorho.inflatabledonkey.pcs.zone.PZFactory;
import com.github.horrorho.inflatabledonkey.pcs.zone.ProtectionZone;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.concurrent.Immutable;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class MBKSyncClient {

    private static final Logger logger = LoggerFactory.getLogger(MBKSyncClient.class);

    public static ProtectionZone
            apply(HttpClient httpClient, CloudKitty kitty, Collection<Key<ECPrivateKey>> keys)
            throws IOException {

        List<CloudKit.ZoneRetrieveResponse> responses
                = ZoneRetrieveRequestOperations.get(kitty, httpClient, "_defaultZone", "mbksync");
        return zone(responses, keys);
    }

    static ProtectionZone
            zone(List<CloudKit.ZoneRetrieveResponse> responses, Collection<Key<ECPrivateKey>> keys) {
        logger.trace("-- zone() - responses: {}", responses);
        ProtectionZone zone = PZFactory.instance().create(keys);
        List<CloudKit.ProtectionInfo> protectionInfoList = protectionInfo(responses, zone);
        for (CloudKit.ProtectionInfo protectionInfo : protectionInfoList) {
            zone = PZFactory.instance().create(zone, protectionInfo)
                    .orElse(zone);
        }
        List<CloudKit.ProtectionInfo> xs = recordProtectionInfo(responses, zone);
        for (CloudKit.ProtectionInfo x : xs) {
            zone = PZFactory.instance().create(zone, x)
                    .orElse(zone);
        }
        return zone;
    }

    static List<CloudKit.ProtectionInfo>
            protectionInfo(List<CloudKit.ZoneRetrieveResponse> response, ProtectionZone zone) {
        return response
                .stream()
                .map(CloudKit.ZoneRetrieveResponse::getZoneSummarysList)
                .flatMap(Collection::stream)
                .map(CloudKit.ZoneRetrieveResponseZoneSummary::getTargetZone)
                .filter(CloudKit.Zone::hasProtectionInfo)
                .map(CloudKit.Zone::getProtectionInfo)
                .collect(Collectors.toList());
    }

    static List<CloudKit.ProtectionInfo>
            recordProtectionInfo(List<CloudKit.ZoneRetrieveResponse> response, ProtectionZone zone) {
        return response
                .stream()
                .map(CloudKit.ZoneRetrieveResponse::getZoneSummarysList)
                .flatMap(Collection::stream)
                .map(CloudKit.ZoneRetrieveResponseZoneSummary::getTargetZone)
                .filter(CloudKit.Zone::hasRecordProtectionInfo)
                .map(CloudKit.Zone::getRecordProtectionInfo)
                .collect(Collectors.toList());
    }
}
// TOFIX rework to pull out zones individually
