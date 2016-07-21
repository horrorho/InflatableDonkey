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
import com.github.horrorho.inflatabledonkey.util.PLists;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;

/**
 * Asset.
 *
 * @author Ahseya
 */
@Immutable
public final class Asset {

    private final int protectionClass;
    private final int size;
    private final int fileType;
    private final Instant downloadTokenExpiration;
    private final String dsPrsID;
    private final Optional<String> contentBaseURL;
    private final Optional<byte[]> fileChecksum;
    private final Optional<byte[]> fileSignature;
    private final Optional<byte[]> keyEncryptionKey;
    private final Optional<NSDictionary> encryptedAttributes;
    private final Optional<CloudKit.Asset> asset;

    public Asset(
            int protectionClass,
            int size,
            int fileType,
            Instant downloadTokenExpiration,
            String dsPrsID,
            Optional<String> contentBaseURL,
            Optional<byte[]> fileChecksum,
            Optional<byte[]> fileSignature,
            Optional<byte[]> keyEncryptionKey,
            Optional<NSDictionary> encryptedAttributes,
            Optional<CloudKit.Asset> asset) {

        this.protectionClass = protectionClass;
        this.size = size;
        this.fileType = fileType;
        this.downloadTokenExpiration = downloadTokenExpiration;
        this.dsPrsID = Objects.requireNonNull(dsPrsID, "dsPrsID");
        this.contentBaseURL = Objects.requireNonNull(contentBaseURL, "contentBaseURL");
        this.fileChecksum = Objects.requireNonNull(fileChecksum, "fileChecksum");
        this.fileSignature = Objects.requireNonNull(fileSignature, "fileSignature");
        this.keyEncryptionKey = Objects.requireNonNull(keyEncryptionKey, "keyEncryptionKey");
        this.encryptedAttributes = Objects.requireNonNull(encryptedAttributes, "encryptedAttributes");
        this.asset = Objects.requireNonNull(asset, "asset");
    }

    public int protectionClass() {
        return protectionClass;
    }

    public int size() {
        return size;
    }

    public int fileType() {
        return fileType;
    }

    public Instant downloadTokenExpiration() {
        return downloadTokenExpiration;
    }

    public String dsPrsID() {
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

    <T extends NSObject, U> Optional<U> encryptedAttribute(String key, Class<T> to, Function<T, U> then) {
        return encryptedAttributes.flatMap(e -> PLists.optionalAs(e, key, to)).map(then);
    }

    public Optional<String> domain() {
        return encryptedAttribute("domain", NSString.class, NSString::getContent);
    }

    public Optional<String> relativePath() {
        return encryptedAttribute("relativePath", NSString.class, NSString::getContent);
    }

    public Optional<Instant> modified() {
        return encryptedAttribute("domain", NSDate.class, NSDate::getDate)
                .map(Date::toInstant);
    }

    public Optional<Instant> birth() {
        return encryptedAttribute("birth", NSDate.class, NSDate::getDate)
                .map(Date::toInstant);
    }

    public Optional<Instant> statusChanged() {
        return encryptedAttribute("statusChanged", NSDate.class, NSDate::getDate)
                .map(Date::toInstant);
    }

    public Optional<Integer> userID() {
        return encryptedAttribute("userID", NSNumber.class, NSNumber::intValue);
    }

    public Optional<Integer> groupID() {
        return encryptedAttribute("groupID", NSNumber.class, NSNumber::intValue);
    }

    public Optional<Integer> mode() {
        return encryptedAttribute("mode", NSNumber.class, NSNumber::intValue);
    }

    public Optional<byte[]> encryptionKey() {
        return encryptedAttribute("encryptionKey", NSData.class, NSData::bytes);
    }

    public Optional<Integer> attributeSize() {
        return encryptedAttribute("size", NSNumber.class, NSNumber::intValue);
    }

    public Optional<CloudKit.Asset> asset() {
        return asset;
    }

    @Override
    public String toString() {
        return "Asset{"
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
// TODO simplify
// Optional fields