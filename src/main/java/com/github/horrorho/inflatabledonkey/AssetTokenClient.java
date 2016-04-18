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
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.data.backup.AssetFactory;
import com.github.horrorho.inflatabledonkey.data.backup.Assets;
import com.github.horrorho.inflatabledonkey.data.backup.ZoneRecord;
import com.github.horrorho.inflatabledonkey.pcs.xzone.XZones;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
public final class AssetTokenClient {

    private static final Logger logger = LoggerFactory.getLogger(AssetTokenClient.class);

    public static List<Asset> get(
            HttpClient httpClient,
            CloudKitty requestOperations,
            XZones zones,
            List<Assets> assetsList) throws IOException {

        assetsList.stream()
                .filter(a -> a.domain().isPresent())
                .forEach(a -> logger.debug("-- main() - domain: {} files:{}", a.domain().get(), a.nonEmptyFiles().size()));

        List<String> fileList = assetsList.stream()
                .filter(a -> a.domain().isPresent())
                .filter(a -> a.domain().get().contains("MediaDomain"))
                .map(Assets::nonEmptyFiles)
                .flatMap(Collection::stream)
                //                .skip(1000)
                .limit(400) // TODO manage MAX 400
                .collect(Collectors.toList());

        logger.debug("-- main() - non-empty file list size: {}", fileList.size());

        List<CloudKit.RecordRetrieveResponse> assetTokens
                = requestOperations.recordRetrieveRequest(
                        httpClient,
                        "_defaultZone",
                        fileList);

        return assetTokens.stream()
                .filter(CloudKit.RecordRetrieveResponse::hasRecord)
                .map(CloudKit.RecordRetrieveResponse::getRecord)
                .map(r -> ZoneRecord.from(zones, r))
                .map(AssetFactory::fromLegacy)
                .limit(10) // TODO LIMIT
                .collect(Collectors.toList());
    }
}
