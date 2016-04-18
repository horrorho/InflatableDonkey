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
import com.github.horrorho.inflatabledonkey.keybag.KeyBag;
import com.github.horrorho.inflatabledonkey.keybag.KeyBagFactory;
import com.github.horrorho.inflatabledonkey.pcs.xzone.XZones;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * KeyBagClient.
 *
 * @author Ahseya
 */
@Immutable
public final class KeyBagClient {

    private static final Logger logger = LoggerFactory.getLogger(KeyBagClient.class);

    public static KeyBag keyBag(
            HttpClient httpClient,
            CloudKitty requestOperations,
            XZones zones,
            String keyBagUUID) throws IOException {
        logger.debug("-- keyBag() - keybag UUID: {}", keyBagUUID);

        List<CloudKit.RecordRetrieveResponse> responseKeyBagList
                = requestOperations.recordRetrieveRequest(httpClient, "mbksync", "K:" + keyBagUUID);

        // protection info
        responseKeyBagList.stream()
                .map(CloudKit.RecordRetrieveResponse::getRecord)
                .filter(CloudKit.Record::hasProtectionInfo)
                .map(CloudKit.Record::getProtectionInfo)
                .forEach(p -> zones.put(p.getProtectionInfoTag(), p.getProtectionInfo().toByteArray()));

        Function<byte[], byte[]> decryptKeybagData
                = bs -> zones.lastZone().get().decrypt(bs, "keybagData");

        Optional<byte[]> optionalKeybagData = responseKeyBagList.stream()
                .map(CloudKit.RecordRetrieveResponse::getRecord)
                .map(CloudKit.Record::getRecordFieldList)
                .flatMap(Collection::stream)
                .filter(value -> value.getIdentifier().getName().equals("keybagData"))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getBytesValue)
                .map(ByteString::toByteArray)
                .map(decryptKeybagData)
                .findFirst();

        if (!optionalKeybagData.isPresent()) {
            logger.warn("-- keyBag() - failed to acquire key bag");
            System.exit(-1);
        }
        byte[] keybagData = optionalKeybagData.get();

        Function<byte[], byte[]> decryptSecret
                = bs -> zones.lastZone().get().decrypt(bs, "secret");

        Optional<byte[]> optionalSecret = responseKeyBagList.stream()
                .map(CloudKit.RecordRetrieveResponse::getRecord)
                .map(CloudKit.Record::getRecordFieldList)
                .flatMap(Collection::stream)
                .filter(value -> value.getIdentifier().getName().equals("secret"))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getBytesValue)
                .map(ByteString::toByteArray)
                .map(decryptSecret)
                .findFirst();

        if (!optionalSecret.isPresent()) {
            logger.warn("-- keyBag() - failed to acquire key bag pass code");
            System.exit(-1);
        }
        byte[] secret = optionalSecret.get();

        KeyBag keyBag = KeyBagFactory.create(keybagData, secret);
        logger.debug("-- keyBag() - key bag: {}", keyBag);

        return keyBag;
    }
}
