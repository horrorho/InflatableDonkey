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

import com.github.horrorho.inflatabledonkey.cloudkitty.CloudKitty;
import com.github.horrorho.inflatabledonkey.keybag.KeyBag;
import com.github.horrorho.inflatabledonkey.keybag.KeyBagFactory;
import com.github.horrorho.inflatabledonkey.pcs.xzone.ProtectionZone;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * KeyBagZone.
 *
 * @author Ahseya
 */
@Immutable
// TODO move to the appropriate ProtectionZone.
public final class KeyBagZone {

    private static final Logger logger = LoggerFactory.getLogger(KeyBagZone.class);

    public static Optional<ProtectedItem<KeyBag>>
            keyBag(HttpClient httpClient, CloudKitty kitty, ProtectionZone zone, String keyBagUUID) throws IOException {
        logger.debug("-- keyBag() - keybag UUID: {}", keyBagUUID);

        List<CloudKit.RecordRetrieveResponse> responses
                = kitty.recordRetrieveRequest(httpClient, "mbksync", "K:" + keyBagUUID);

        if (responses.size() != 1) {
            logger.warn("-- create() - bad response list size: {}", responses);
            return Optional.empty();
        }
        CloudKit.RecordRetrieveResponse response = responses.get(0);

        List<CloudKit.ProtectionInfo> protectionInfoList
                = ZoneAssistant.recordRetrieveResponseProtectionInfo(responses, zone);
        ProtectionZone localZone = ZoneAssistant.roll(zone, protectionInfoList);

        return keyBag(response, localZone)
                .map(kb -> new ProtectedItem<>(localZone, kb));
    }

    static Optional<KeyBag> keyBag(CloudKit.RecordRetrieveResponse response, ProtectionZone zone) {
        Optional<byte[]> keyBagData = field(response.getRecord(), "keybagData", zone::decrypt);
        if (!keyBagData.isPresent()) {
            logger.warn("-- keyBag() - failed to acquire key bag");
            return Optional.empty();
        }

        Optional<byte[]> secret = field(response.getRecord(), "secret", zone::decrypt);
        if (!secret.isPresent()) {
            logger.warn("-- keyBag() - failed to acquire key bag pass code");
            return Optional.empty();
        }

        KeyBag keyBag = KeyBagFactory.create(keyBagData.get(), secret.get());
        return Optional.of(keyBag);
    }

    static Optional<byte[]> field(
            CloudKit.Record record,
            String label,
            BiFunction<byte[], String, Optional<byte[]>> decrypt) {

        return record.getRecordFieldList()
                .stream()
                .filter(value -> value.getIdentifier().getName().equals(label))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getBytesValue)
                .map(ByteString::toByteArray)
                .map(bs -> decrypt.apply(bs, label))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}
