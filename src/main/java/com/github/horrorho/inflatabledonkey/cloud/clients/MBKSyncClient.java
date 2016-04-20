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
import com.github.horrorho.inflatabledonkey.crypto.eckey.ECPrivate;
import com.github.horrorho.inflatabledonkey.crypto.key.Key;
import com.github.horrorho.inflatabledonkey.pcs.xzone.ProtectionZone;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MBKSyncClient.
 *
 * @author Ahseya
 */
@Immutable
public final class MBKSyncClient {

    private static final Logger logger = LoggerFactory.getLogger(MBKSyncClient.class);

    public static Optional<ProtectionZone>
            mbksync(HttpClient httpClient, CloudKitty kitty, Collection<Key<ECPrivate>> keys) throws IOException {

        List<CloudKit.ZoneRetrieveResponse> responses
                = kitty.zoneRetrieveRequest(httpClient, "_defaultZone", "mbksync");
        logger.debug("-- baseZones() - responses: {}", responses);

        if (responses.size() != 2) {
            logger.warn("-- baseZones() - bad response list size: {}", responses);
            return Optional.empty();
        }

        ProtectionZone zone = ProtectionZone.createBaseZone(keys);

        List<CloudKit.ProtectionInfo> protectionInfoList = zone(responses, zone);
        for (CloudKit.ProtectionInfo protectionInfo : protectionInfoList) {
            zone = zone.newProtectionZone(protectionInfo)
                    .orElse(zone);
        }
        return Optional.of(zone);
    }

    static List<CloudKit.ProtectionInfo> zone(List<CloudKit.ZoneRetrieveResponse> response, ProtectionZone zone) {
        return response.stream()
                .map(CloudKit.ZoneRetrieveResponse::getZoneSummarysList)
                .flatMap(Collection::stream)
                .filter(CloudKit.ZoneRetrieveResponseZoneSummary::hasTargetZone)
                .map(CloudKit.ZoneRetrieveResponseZoneSummary::getTargetZone)
                .filter(CloudKit.Zone::hasProtectionInfo)
                .map(CloudKit.Zone::getProtectionInfo)
                .collect(Collectors.toList());
    }
}
