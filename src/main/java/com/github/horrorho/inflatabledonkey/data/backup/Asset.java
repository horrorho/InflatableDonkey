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

import com.dd.plist.NSData;
import com.dd.plist.NSDate;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;
import com.dd.plist.NSObject;
import com.dd.plist.NSString;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit;
import com.github.horrorho.inflatabledonkey.util.NSDictionaries;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class Asset extends AbstractRecord {

    private final AssetID assetID;
    private final Optional<Integer> protectionClass;
    private final Optional<Integer> size;
    private final Optional<Integer> fileType;
    private final Optional<Instant> downloadTokenExpiration;
    private final Optional<String> dsPrsID;
    private final Optional<String> contentBaseURL;
    private final Optional<byte[]> fileChecksum;
    private final Optional<byte[]> fileSignature;
    private final Optional<byte[]> keyEncryptionKey;
    private final Optional<NSDictionary> encryptedAttributes;
    private final Optional<CloudKit.Asset> asset;

    Asset(
            CloudKit.Record record,
            AssetID assetID,
            Optional<Integer> protectionClass,
            Optional<Integer> size,
            Optional<Integer> fileType,
            Optional<Instant> downloadTokenExpiration,
            Optional<String> dsPrsID,
            Optional<String> contentBaseURL,
            Optional<byte[]> fileChecksum,
            Optional<byte[]> fileSignature,
            Optional<byte[]> keyEncryptionKey,
            Optional<byte[]> encryptedAttributes,
            Optional<CloudKit.Asset> asset) {
        super(record);
        this.assetID = Objects.requireNonNull(assetID, "assetID");
        this.protectionClass = Objects.requireNonNull(protectionClass, "protectionClass");
        this.size = Objects.requireNonNull(size, "size");
        this.fileType = Objects.requireNonNull(fileType, "fileType");
        this.downloadTokenExpiration = Objects.requireNonNull(downloadTokenExpiration, "downloadTokenExpiration");
        this.dsPrsID = Objects.requireNonNull(dsPrsID, "dsPrsID");
        this.contentBaseURL = Objects.requireNonNull(contentBaseURL, "contentBaseURL");
        this.fileChecksum = Objects.requireNonNull(fileChecksum, "fileChecksum");
        this.fileSignature = Objects.requireNonNull(fileSignature, "fileSignature");
        this.keyEncryptionKey = Objects.requireNonNull(keyEncryptionKey, "keyEncryptionKey");
        this.encryptedAttributes = encryptedAttributes.flatMap(NSDictionaries::parse);
        this.asset = Objects.requireNonNull(asset, "asset");
    }

    public AssetID assetID() {
        return assetID;
    }

    public Optional<Integer> protectionClass() {
        return protectionClass;
    }

    public Optional<Integer> size() {
        return size;
    }

    public Optional<Integer> fileType() {
        return fileType;
    }

    public Optional<Instant> downloadTokenExpiration() {
        return downloadTokenExpiration;
    }

    public Optional<String> dsPrsID() {
        return dsPrsID;
    }

    public Optional<String> contentBaseURL() {
        return contentBaseURL;
    }

    public Optional<byte[]> fileChecksum() {
        return fileChecksum.map(bs -> Arrays.copyOf(bs, bs.length));
    }

    public Optional<byte[]> fileSignature() {
        return fileSignature.map(bs -> Arrays.copyOf(bs, bs.length));
    }

    public Optional<byte[]> keyEncryptionKey() {
        return keyEncryptionKey.map(bs -> Arrays.copyOf(bs, bs.length));
    }

    <T extends NSObject> Optional<T> encryptedAttribute(String key, Class<T> to) {
        return encryptedAttributes.flatMap(e -> NSDictionaries.as(e, key, to));
    }

    public Optional<String> domain() {
        return encryptedAttribute("domain", NSString.class).map(NSString::getContent);
    }

    public Optional<String> relativePath() {
        return encryptedAttribute("relativePath", NSString.class).map(NSString::getContent);
    }

    public Optional<Instant> modified() {
        return encryptedAttribute("domain", NSDate.class).map(NSDate::getDate)
                .map(Date::toInstant);
    }

    public Optional<Instant> birth() {
        return encryptedAttribute("birth", NSDate.class).map(NSDate::getDate)
                .map(Date::toInstant);
    }

    public Optional<Instant> statusChanged() {
        return encryptedAttribute("statusChanged", NSDate.class).map(NSDate::getDate)
                .map(Date::toInstant);
    }

    public Optional<Integer> userID() {
        return encryptedAttribute("userID", NSNumber.class).map(NSNumber::intValue);
    }

    public Optional<Integer> groupID() {
        return encryptedAttribute("groupID", NSNumber.class).map(NSNumber::intValue);
    }

    public Optional<Integer> mode() {
        return encryptedAttribute("mode", NSNumber.class).map(NSNumber::intValue);
    }

    public Optional<byte[]> encryptionKey() {
        return encryptedAttribute("encryptionKey", NSData.class).map(NSData::bytes);
    }

    public Optional<Integer> attributeSize() {
        return encryptedAttribute("size", NSNumber.class).map(NSNumber::intValue);
    }

    public Optional<CloudKit.Asset> asset() {
        return asset;
    }

    @Override
    public String toString() {
        return "Asset{"
                + "assetID=" + assetID
                + "protectionClass=" + protectionClass
                + ", size=" + size
                + ", fileType=" + fileType
                + ", downloadTokenExpiration=" + downloadTokenExpiration
                + ", contentBaseURL=" + contentBaseURL
                + ", fileChecksum=" + fileChecksum.map(Hex::toHexString)
                + ", fileSignature=" + fileSignature.map(Hex::toHexString)
                + ", keyEncryptionKey=" + keyEncryptionKey.map(Hex::toHexString)
                + ", encryptedAttributes=" + encryptedAttributes.map(NSObject::toXMLPropertyList)
                + ", asset=" + asset
                + '}';
    }
}
