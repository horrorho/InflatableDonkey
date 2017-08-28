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

import com.github.horrorho.inflatabledonkey.protobuf.CloudKit;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class Asset extends AbstractRecord {

    private static final Logger logger = LoggerFactory.getLogger(Asset.class);

    private final AssetID assetID;
    private final Optional<Integer> protectionClass;
    private final Optional<Long> size;
    private final Optional<Integer> fileType;
    private final Optional<Instant> downloadTokenExpiration;
    private final Optional<String> dsPrsID;
    private final Optional<String> contentBaseURL;
    private final Optional<byte[]> fileChecksum;
    private final Optional<byte[]> fileSignature;
    private final Optional<byte[]> keyEncryptionKey;
    private final Optional<AssetEncryptedAttributes> encryptedAttributes;
    private final Optional<CloudKit.Asset> asset;

    Asset(
            CloudKit.Record record,
            AssetID assetID,
            Optional<Integer> protectionClass,
            Optional<Long> size,
            Optional<Integer> fileType,
            Optional<Instant> downloadTokenExpiration,
            Optional<String> dsPrsID,
            Optional<String> contentBaseURL,
            Optional<byte[]> fileChecksum,
            Optional<byte[]> fileSignature,
            Optional<byte[]> keyEncryptionKey,
            Optional<AssetEncryptedAttributes> encryptedAttributes,
            Optional<CloudKit.Asset> asset) {
        super(record);
        this.assetID = Objects.requireNonNull(assetID);
        this.protectionClass = Objects.requireNonNull(protectionClass);
        this.size = Objects.requireNonNull(size);
        this.fileType = Objects.requireNonNull(fileType);
        this.downloadTokenExpiration = Objects.requireNonNull(downloadTokenExpiration);
        this.dsPrsID = Objects.requireNonNull(dsPrsID);
        this.contentBaseURL = Objects.requireNonNull(contentBaseURL);
        this.fileChecksum = Objects.requireNonNull(fileChecksum);
        this.fileSignature = Objects.requireNonNull(fileSignature);
        this.keyEncryptionKey = Objects.requireNonNull(keyEncryptionKey);
        this.encryptedAttributes = Objects.requireNonNull(encryptedAttributes);
        this.asset = Objects.requireNonNull(asset);
    }

    public AssetID assetID() {
        return assetID;
    }

    public Optional<Integer> protectionClass() {
        return protectionClass;
    }

    public Optional<Long> size() {
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

    public Optional<String> domain() {
        return encryptedAttributes.flatMap(AssetEncryptedAttributes::domain);
    }

    public Optional<String> relativePath() {
        return encryptedAttributes.flatMap(AssetEncryptedAttributes::relativePath);
    }

    public Optional<Instant> modified() {
        return encryptedAttributes.flatMap(AssetEncryptedAttributes::modified);
    }

    public Optional<Instant> birth() {
        return encryptedAttributes.flatMap(AssetEncryptedAttributes::birth);
    }

    public Optional<Instant> statusChanged() {
        return encryptedAttributes.flatMap(AssetEncryptedAttributes::statusChanged);
    }

    public Optional<Integer> userID() {
        return encryptedAttributes.flatMap(AssetEncryptedAttributes::userID);
    }

    public Optional<Integer> groupID() {
        return encryptedAttributes.flatMap(AssetEncryptedAttributes::groupID);
    }

    public Optional<Integer> mode() {
        return encryptedAttributes.flatMap(AssetEncryptedAttributes::mode);
    }

    public Optional<byte[]> encryptionKey() {
        return encryptedAttributes.flatMap(AssetEncryptedAttributes::encryptionKey);
    }

    public Optional<Long> attributeSize() {
        return encryptedAttributes.flatMap(AssetEncryptedAttributes::size);
    }

    public Optional<byte[]> attributeChecksum() {
        return encryptedAttributes.flatMap(AssetEncryptedAttributes::checksum);
    }

    public Optional<Long> sizeBeforeCopy() {
        return encryptedAttributes.flatMap(AssetEncryptedAttributes::sizeBeforeCopy);
    }

    public Optional<Integer> contentCompressionMethod() {
        return encryptedAttributes.flatMap(AssetEncryptedAttributes::contentCompressionMethod);
    }

    public Optional<Integer> contentEncodingMethod() {
        return encryptedAttributes.flatMap(AssetEncryptedAttributes::contentEncodingMethod);
    }

    public Optional<CloudKit.Asset> asset() {
        return asset;
    }

    @Override
    public String toString() {
        return "Asset{"
                + "assetID=" + assetID
                + ",protectionClass=" + protectionClass
                + ", size=" + size
                + ", fileType=" + fileType
                + ", downloadTokenExpiration=" + downloadTokenExpiration
                + ", contentBaseURL=" + contentBaseURL
                + ", fileChecksum=" + fileChecksum.map(Hex::toHexString)
                + ", fileSignature=" + fileSignature.map(Hex::toHexString)
                + ", keyEncryptionKey=" + keyEncryptionKey.map(Hex::toHexString)
                + ", encryptedAttributes=" + encryptedAttributes
                + ", asset=" + asset
                + '}';
    }
}
