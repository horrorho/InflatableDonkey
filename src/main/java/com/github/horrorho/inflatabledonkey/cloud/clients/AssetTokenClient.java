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
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.data.backup.AssetFactory;
import com.github.horrorho.inflatabledonkey.data.backup.Assets;
import com.github.horrorho.inflatabledonkey.pcs.zone.PZFactory;
import com.github.horrorho.inflatabledonkey.pcs.zone.ProtectionZone;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit;
import java.io.UncheckedIOException;
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
            assets(HttpClient httpClient, CloudKitty kitty, ProtectionZone zone, Collection<Assets> assets)
            throws UncheckedIOException {
        List<String> nonEmptyAssets = assets.stream()
                .map(Assets::nonEmpty)
                .flatMap(Collection::stream)
                .map(Object::toString)
                .collect(Collectors.toList());
        logger.debug("-- assets() - non-empty asset list size: {}", nonEmptyAssets.size());

        return RecordRetrieveRequestOperations.get(kitty, httpClient, "_defaultZone", nonEmptyAssets)
                .stream()
                .filter(CloudKit.RecordRetrieveResponse::hasRecord)
                .map(CloudKit.RecordRetrieveResponse::getRecord)
                .map(r -> asset(r, zone))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    static Optional<Asset> asset(CloudKit.Record record, ProtectionZone zone) {
        logger.debug("-- asset() - record: {} zone: {}", record, zone);
        return PZFactory.instance()
                .create(zone, record.getProtectionInfo())
                .flatMap(u -> AssetFactory.from(record, u));
    }
}
