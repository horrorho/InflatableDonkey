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
import com.github.horrorho.inflatabledonkey.data.backup.Manifests;
import com.github.horrorho.inflatabledonkey.data.backup.ManifestsFactory;
import com.github.horrorho.inflatabledonkey.pcs.xzone.XZones;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import java.io.IOException;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class ManifestsClient {

    private static final Logger logger = LoggerFactory.getLogger(ManifestsClient.class);

    public static Manifests get(HttpClient httpClient,
            CloudKitty requestOperations,
            XZones zones,
            String snapshot) throws IOException {

        Consumer<CloudKit.Record> zonesAddRecord
                = record -> {
                    if (record.hasProtectionInfo()) {
                        CloudKit.ProtectionInfo protectionInfo = record.getProtectionInfo();

                        if (protectionInfo.hasProtectionInfoTag() && protectionInfo.hasProtectionInfo()) {
                            zones.put(
                                    protectionInfo.getProtectionInfoTag(),
                                    protectionInfo.getProtectionInfo().toByteArray());
                        }
                    }
                };

        BiFunction<byte[], String, byte[]> zonesDecrypt
                = (bs, s) -> zones.lastZone()
                .orElseThrow(() -> new IllegalArgumentException("no zones present"))
                .decrypt(bs, s);

        List<CloudKit.RecordRetrieveResponse> responseManifestList
                = requestOperations.recordRetrieveRequest(
                        httpClient,
                        "mbksync",
                        snapshot);

        if (responseManifestList.isEmpty()) {
            logger.warn("-- main() - no response received");
        }

        if (responseManifestList.size() != 1) {
            // We only sent a single delimited protobuf, so we expect only a single reply.
            logger.warn("-- main() - unexpected list size: {}", responseManifestList.size());
        }

        CloudKit.RecordRetrieveResponse manifestResponse = responseManifestList.get(0);

        zonesAddRecord.accept(manifestResponse.getRecord());

        return ManifestsFactory.from(manifestResponse.getRecord(), zonesDecrypt);

    }
}
