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
package com.github.horrorho.inflatabledonkey.data.backup;

import com.dd.plist.NSDictionary;
import com.github.horrorho.inflatabledonkey.pcs.zone.ProtectionZone;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit;
import com.github.horrorho.inflatabledonkey.util.PLists;
import com.google.protobuf.ByteString;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AssetFactory.
 *
 * @author Ahseya
 */
@Immutable
public final class AssetFactory {

    private static final Logger logger = LoggerFactory.getLogger(AssetFactory.class);

    private static final String CONTENTS = "contents";
    private static final String ENCRYPTED_ATTRIBUTES = "encryptedAttributes";
    private static final String FILE_TYPE = "fileType";
    private static final String PROTECTION_CLASS = "protectionClass";

    private static final long DEFAULT_EXPIRATION_SECONDS = 60 * 60; // 1 hour.
    private static final long GRACE_TIME_SECONDS = 15 * 60; // 15 minutes.

    public static Asset from(CloudKit.Record record, ProtectionZone zone) {
        List<CloudKit.RecordField> records = record.getRecordFieldList();

        Optional<Integer> protectionClass = protectionClass(records);
        Optional<Integer> fileType = fileType(records);
        Optional<NSDictionary> encryptedAttributes = encryptedAttributes(records)
                .flatMap(u -> zone.decrypt(u, ENCRYPTED_ATTRIBUTES))
                .map(PLists::parseDictionary); // TOFIX cover the illegal argument exception.
        Optional<CloudKit.Asset> asset = asset(records);
        Optional<byte[]> keyEncryptionKey = asset.filter(CloudKit.Asset::hasData)
                .map(u -> u.getData().getValue().toByteArray())
                .flatMap(zone::unwrapKey);
        Optional<byte[]> fileChecksum = asset.filter(CloudKit.Asset::hasFileChecksum)
                .map(u -> u.getFileChecksum().toByteArray());
        Optional<byte[]> fileSignature = asset.filter(CloudKit.Asset::hasFileSignature)
                .map(u -> u.getFileSignature().toByteArray());
        Optional<String> contentBaseURL = asset.filter(CloudKit.Asset::hasContentBaseURL)
                .map(CloudKit.Asset::getContentBaseURL);
        Optional<String> dsPrsID = asset.filter(CloudKit.Asset::hasDsPrsID)
                .map(CloudKit.Asset::getDsPrsID);
        Optional<Integer> fileSize = asset.filter(CloudKit.Asset::hasSize)
                .map(CloudKit.Asset::getSize)
                .map(Long::intValue);
        Optional<Instant> downloadTokenExpiration = asset.filter(CloudKit.Asset::hasDownloadTokenExpiration)
                .map(CloudKit.Asset::getDownloadTokenExpiration)
                .map(Instant::ofEpochSecond);

        // TODO Adjust for bad system clocks.
//        long currentTimeSeconds = System.currentTimeMillis() / 1000;
//        Instant downloadTokenExpiration = tokenExpiration < (currentTimeSeconds + GRACE_TIME_SECONDS)
//                ? Instant.ofEpochSecond(currentTimeSeconds + DEFAULT_EXPIRATION_SECONDS)
//                : Instant.ofEpochSecond(tokenExpiration);
        Asset newAsset = new Asset(
                protectionClass,
                fileSize,
                fileType,
                downloadTokenExpiration,
                dsPrsID,
                contentBaseURL,
                fileChecksum,
                fileSignature,
                keyEncryptionKey,
                encryptedAttributes,
                asset);
        logger.debug("-- from() - asset: {}", newAsset);

        return newAsset;
    }

    static Optional<Integer> protectionClass(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(value -> value.getIdentifier().getName().equals(PROTECTION_CLASS))
                .map(u -> u.getValue().getSignedValue())
                .map(Long::intValue)
                .findFirst();
    }

    static Optional<Integer> fileType(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(value -> value.getIdentifier().getName().equals(FILE_TYPE))
                .map(u -> u.getValue().getSignedValue())
                .map(Long::intValue)
                .findFirst();
    }

    static Optional<CloudKit.Asset> asset(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(value -> value.getIdentifier().getName().equals(CONTENTS))
                .map(u -> u.getValue().getAssetValue())
                .findFirst();
    }

    static Optional<byte[]> encryptedAttributes(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(u -> u.getIdentifier().getName().equals(ENCRYPTED_ATTRIBUTES))
                .map(u -> u.getValue().getBytesValue())
                .map(ByteString::toByteArray)
                .findFirst();
    }
}
