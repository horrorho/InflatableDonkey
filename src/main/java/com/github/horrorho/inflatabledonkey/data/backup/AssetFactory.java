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
import com.github.horrorho.inflatabledonkey.pcs.xzone.XZone;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import com.github.horrorho.inflatabledonkey.util.PLists;
import com.google.protobuf.ByteString;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.encoders.Hex;
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

    private static final long DEFAULT_EXPIRATION_SECONDS = 60 * 60; // 1 hour.
    private static final long GRACE_TIME_SECONDS = 15 * 60; // 15 minutes.

    public static Asset from(ZoneRecord zoneRecord) {
        return from(zoneRecord.record(), zoneRecord.zone());
    }

    public static Asset from(CloudKit.Record record, Optional<XZone> zone) {
        List<CloudKit.RecordField> records = record.getRecordFieldList();

        int protectionClass = protectionClass(records);
        int fileType = fileType(records);

        Optional<NSDictionary> encryptedAttributes = encryptedAttributes(records)
                .flatMap(bs -> zone.map(z -> z.decrypt(bs, ENCRYPTED_ATTRIBUTES)))
                .map(bs -> PLists.<NSDictionary>parse(bs)); // TODO cover the illegal argument exception.

        Optional<CloudKit.Asset> asset = asset(records);

        Optional<byte[]> data
                = asset.flatMap(as -> as.hasData()
                        ? Optional.of(as.getData().getValue().toByteArray())
                        : Optional.empty());

        Optional<byte[]> keyEncryptionKey = decryptData(data, zone);

        // TODO rework to getters in Asset.
        Optional<byte[]> fileChecksum
                = asset.flatMap(as -> as.hasFileChecksum()
                        ? Optional.of(as.getFileChecksum().toByteArray())
                        : Optional.empty());

        Optional<byte[]> fileSignature
                = asset.flatMap(as -> as.hasFileSignature()
                        ? Optional.of(as.getFileSignature().toByteArray())
                        : Optional.empty());

        Optional<String> contentBaseURL
                = asset.flatMap(as -> as.hasContentBaseURL()
                        ? Optional.of(as.getContentBaseURL())
                        : Optional.empty());

        int fileSize = asset.map(as -> as.getSize()).orElse(0L).intValue();
        long tokenExpiration = asset.map(as -> as.getDownloadTokenExpiration()).orElse(0L);

        long currentTimeSeconds = System.currentTimeMillis() / 1000;

        // Adjust for bad system clocks.
        Instant downloadTokenExpiration = tokenExpiration < (currentTimeSeconds + GRACE_TIME_SECONDS)
                ? Instant.ofEpochSecond(currentTimeSeconds + DEFAULT_EXPIRATION_SECONDS)
                : Instant.ofEpochSecond(tokenExpiration);

        return new Asset(
                protectionClass,
                fileSize,
                fileType,
                downloadTokenExpiration,
                contentBaseURL,
                fileChecksum,
                fileSignature,
                keyEncryptionKey,
                encryptedAttributes,
                asset);
    }

    static Optional<byte[]> decryptData(Optional<byte[]> data, Optional<XZone> zone) {
        if (!data.isPresent()) {
            logger.debug("-- decryptData() - no data");
            return Optional.empty();
        }

        if (!zone.isPresent()) {
            logger.debug("-- decryptData() - no zone");
            return Optional.empty();
        }

        Optional<byte[]> key = zone.get().fpDecrypt(data.get());
        logger.debug("-- decryptData() - data 0x{} > key: 0x{}",
                data.map(Hex::toHexString).orElse("NULL"), key.map(Hex::toHexString).orElse("NULL"));
        return key;
    }

    static Optional<byte[]> optional(byte[] bytes) {
        return bytes.length == 0
                ? Optional.empty()
                : Optional.of(bytes);
    }

    static Integer protectionClass(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(value -> value.getIdentifier().getName().equals("protectionClass"))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getUint32)
                .findFirst()
                .orElse(-1);
    }

    static Integer fileType(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(value -> value.getIdentifier().getName().equals("fileType"))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getUint32)
                .findFirst()
                .orElse(-1);
    }

    static Optional<CloudKit.Asset> asset(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(value -> value.getIdentifier().getName().equals(CONTENTS))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getAssetValue)
                .findFirst();
    }

    public static Optional<byte[]> encryptedAttributes(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(value -> value.getIdentifier().getName().equals(ENCRYPTED_ATTRIBUTES))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getBytesValue)
                .map(ByteString::toByteArray)
                .findFirst();
    }
}
