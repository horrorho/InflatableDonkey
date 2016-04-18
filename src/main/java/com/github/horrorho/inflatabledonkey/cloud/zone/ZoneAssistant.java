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
package com.github.horrorho.inflatabledonkey.cloud.zone;

import com.github.horrorho.inflatabledonkey.pcs.xzone.ProtectionZone;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;

/**
 * ZoneAssistant.
 *
 * @author Ahseya
 */
@Immutable
public final class ZoneAssistant {

    @Deprecated
    public static List<CloudKit.ProtectionInfo>
            zoneRetrieveResponseProtectionInfo(List<CloudKit.ZoneRetrieveResponse> response, ProtectionZone zoneKeys) {

        return response.stream()
                .map(CloudKit.ZoneRetrieveResponse::getZoneSummarysList)
                .flatMap(Collection::stream)
                .filter(CloudKit.ZoneRetrieveResponseZoneSummary::hasTargetZone)
                .map(CloudKit.ZoneRetrieveResponseZoneSummary::getTargetZone)
                .filter(CloudKit.Zone::hasProtectionInfo)
                .map(CloudKit.Zone::getProtectionInfo)
                .collect(Collectors.toList());
    }

    @Deprecated
    public static List<CloudKit.ProtectionInfo>
            recordRetrieveResponseProtectionInfo(List<CloudKit.RecordRetrieveResponse> response, ProtectionZone zoneKeys) {

        return response.stream()
                .map(CloudKit.RecordRetrieveResponse::getRecord)
                .filter(CloudKit.Record::hasProtectionInfo)
                .map(CloudKit.Record::getProtectionInfo)
                .collect(Collectors.toList());
    }

    @Deprecated
    public static Optional<CloudKit.ProtectionInfo>
            recordRetrieveResponseProtectionInfo(CloudKit.RecordRetrieveResponse response) {

        return Optional.ofNullable(response)
                .map(CloudKit.RecordRetrieveResponse::getRecord)
                .filter(CloudKit.Record::hasProtectionInfo)
                .map(CloudKit.Record::getProtectionInfo);
    }

    @Deprecated
    static ProtectionZone roll(ProtectionZone zoneKeys, List<CloudKit.ProtectionInfo> protectionInfoList) {
        for (CloudKit.ProtectionInfo protectionInfo : protectionInfoList) {
            zoneKeys = zoneKeys.newProtectionZone(protectionInfo)
                    .orElse(zoneKeys);
        }
        return zoneKeys;
    }
}
