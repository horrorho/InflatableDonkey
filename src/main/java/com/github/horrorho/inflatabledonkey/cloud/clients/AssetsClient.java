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
import com.github.horrorho.inflatabledonkey.data.backup.Assets;
import com.github.horrorho.inflatabledonkey.data.backup.AssetsFactory;
import com.github.horrorho.inflatabledonkey.data.backup.Manifest;
import com.github.horrorho.inflatabledonkey.pcs.zone.PZFactory;
import com.github.horrorho.inflatabledonkey.pcs.zone.ProtectionZone;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
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

        List<String> manifestIDs = manifests.stream()
                .map(AssetsClient::manifestIDs)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<CloudKit.RecordRetrieveResponse> responses
                = kitty.recordRetrieveRequest(
                        httpClient,
                        "_defaultZone",
                        manifestIDs);
        logger.info("-- assets() - responses: {}", responses.size());

        // Manifests with multiple counts only return protection info for the first block, as we are passing blocks in
        // order we can reference the previous protection zone.
        // TODO tighten up.
        AtomicReference<ProtectionZone> previous = new AtomicReference<>(zone);

        return responses.stream()
                .filter(CloudKit.RecordRetrieveResponse::hasRecord)
                .map(CloudKit.RecordRetrieveResponse::getRecord)
                .map(r -> assets(r, zone, previous))
                .collect(Collectors.toList());
    }

    static List<String> manifestIDs(Manifest manifest) {
        return IntStream.range(0, manifest.count())
                .mapToObj(i -> manifest.id() + ":" + i)
                .collect(Collectors.toList());
    }

    static Assets assets(CloudKit.Record record, ProtectionZone zone, AtomicReference<ProtectionZone> previous) {
        return PZFactory.instance().create(zone, record.getProtectionInfo())
                .map(z -> {
                    previous.set(z);
                    return AssetsFactory.from(record, z::decrypt);
                })
                .orElseGet(() -> AssetsFactory.from(record, previous.get()::decrypt));
    }
}
