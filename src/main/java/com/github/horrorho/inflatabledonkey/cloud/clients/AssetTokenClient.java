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
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.data.backup.AssetFactory;
import com.github.horrorho.inflatabledonkey.data.backup.Assets;
import com.github.horrorho.inflatabledonkey.pcs.xzone.ProtectionZone;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
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
 * AssetTokenClient.
 *
 * @author Ahseya
 */
@Immutable
public final class AssetTokenClient {

    private static final Logger logger = LoggerFactory.getLogger(AssetTokenClient.class);

    public static List<Asset>
            assets(HttpClient httpClient, CloudKitty kitty, ProtectionZone zone, Collection<Assets> assetsList)
            throws IOException {

        List<String> fileList = assetsList.stream()
                .map(Assets::nonEmptyFiles)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        logger.debug("-- assets() - non-empty file list size: {}", fileList.size());

        if (fileList.isEmpty()) {
            return new ArrayList<>();
        }

        List<CloudKit.RecordRetrieveResponse> responses
                = kitty.recordRetrieveRequest(
                        httpClient,
                        "_defaultZone",
                        fileList);

        return responses.stream()
                .filter(CloudKit.RecordRetrieveResponse::hasRecord)
                .map(CloudKit.RecordRetrieveResponse::getRecord)
                .map(r -> asset(r, zone))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    static Optional<Asset> asset(CloudKit.Record record, ProtectionZone zone) {
        return zone.newProtectionZone(record.getProtectionInfo())
                .map(z -> AssetFactory.from(record, z::decrypt, z::fpDecrypt));
    }
}
