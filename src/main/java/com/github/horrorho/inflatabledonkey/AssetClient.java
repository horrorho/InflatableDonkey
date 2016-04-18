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
package com.github.horrorho.inflatabledonkey;

import com.github.horrorho.inflatabledonkey.cloudkitty.CloudKitty;
import com.github.horrorho.inflatabledonkey.data.backup.Assets;
import com.github.horrorho.inflatabledonkey.data.backup.AssetsFactory;
import com.github.horrorho.inflatabledonkey.data.backup.Manifest;
import com.github.horrorho.inflatabledonkey.data.backup.Manifests;
import com.github.horrorho.inflatabledonkey.data.backup.ZoneRecord;
import com.github.horrorho.inflatabledonkey.pcs.xzone.XZones;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class AssetClient {

    public static List<Assets> get(
            HttpClient httpClient,
            CloudKitty requestOperations,
            XZones zones,
            Manifests manifests) throws IOException {

        Function<Manifest, List<String>> manifestIDs
                = m
                -> IntStream.range(0, m.count())
                .mapToObj(i -> m.id() + ":" + i)
                .collect(Collectors.toList());

        List<String> manifestRecords = manifests.manifests()
                .stream()
                .map(manifestIDs)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<CloudKit.RecordRetrieveResponse> responseAssetList
                = requestOperations.recordRetrieveRequest(
                        httpClient,
                        "_defaultZone",
                        manifestRecords);

        return responseAssetList.stream()
                .filter(CloudKit.RecordRetrieveResponse::hasRecord)
                .map(CloudKit.RecordRetrieveResponse::getRecord)
                .map(r -> ZoneRecord.from(zones, r))
                .map(AssetsFactory::from)
                .collect(Collectors.toList());
    }
}
