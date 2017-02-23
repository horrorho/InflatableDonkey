/*
 * The MIT License
 *
 * Copyright 2017 Ahseya.
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
import com.dd.plist.NSString;
import com.github.horrorho.inflatabledonkey.io.IOFunction;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit;
import com.github.horrorho.inflatabledonkey.protobuf.util.ProtobufParser;
import com.github.horrorho.inflatabledonkey.util.NSDictionaries;
import com.google.protobuf.UnknownFieldSet;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class AssetEncryptedAttributesFactory {

    private static final Logger logger = LoggerFactory.getLogger(AssetEncryptedAttributesFactory.class);

    private static final byte[] BPLIST = new byte[]{0x62, 0x70, 0x6c, 0x69, 0x73, 0x74}; // binary property list header

    private static final IOFunction<InputStream, CloudKit.EncryptedAttributes> PARSER
            = ProtobufParser.of(CloudKit.EncryptedAttributes::parseFrom);

    public static Optional<AssetEncryptedAttributes> from(byte[] data) {
        if (data.length == 0) {
            logger.warn("-- from() - empty data packet");
            return Optional.empty();
        }

        // Test/ parse BPList.
        byte[] header = Arrays.copyOf(data, BPLIST.length);
        if (Arrays.equals(header, BPLIST)) {
            return fromBPList(data);
        }

        // Try parse Protobuf.
        Optional<AssetEncryptedAttributes> attributes = AssetEncryptedAttributesFactory.fromProtobuf(data);
        if (attributes.isPresent()) {
            return attributes;
        }

        logger.warn("-- from() - unsupported data: 0x{}", Hex.toHexString(data));
        return Optional.empty();
    }

    public static Optional<AssetEncryptedAttributes> fromProtobuf(byte[] data) {
        return parseProtobuf(data)
                .map(AssetEncryptedAttributesFactory::fromProtobuf);
    }

    static Optional<CloudKit.EncryptedAttributes> parseProtobuf(byte[] data) {
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            CloudKit.EncryptedAttributes attributes = PARSER.apply(is);
            return Optional.of(attributes);

        } catch (IOException ex) {
            logger.warn("-- parseProtobuf() - {}: {}", ex.getClass(), ex.getMessage());
            return Optional.empty();
        }
    }

    public static AssetEncryptedAttributes fromProtobuf(CloudKit.EncryptedAttributes data) {
        Optional<String> domain = data.hasDomain()
                ? Optional.of(data.getDomain())
                : Optional.empty();
        Optional<String> relativePath = data.hasRelativePath()
                ? Optional.of(data.getRelativePath())
                : Optional.empty();
        Optional<Instant> modified = data.hasModified()
                ? Optional.of(data.getModified()).map(WKTimestamp::toInstant)
                : Optional.empty();
        Optional<Instant> birth = data.hasBirth()
                ? Optional.of(data.getBirth()).map(WKTimestamp::toInstant)
                : Optional.empty();
        Optional<Instant> statusChanged = data.hasStatusChanged()
                ? Optional.of(data.getStatusChanged()).map(WKTimestamp::toInstant)
                : Optional.empty();
        Optional<Integer> userID = data.hasUserID()
                ? Optional.of(data.getUserID())
                : Optional.empty();
        Optional<Integer> groupID = data.hasGroupID()
                ? Optional.of(data.getGroupID())
                : Optional.empty();
        Optional<Integer> mode = data.hasMode()
                ? Optional.of(data.getMode())
                : Optional.empty();
        Optional<Long> size = data.hasSize()
                ? Optional.of(data.getSize())
                : Optional.empty();
        Optional<byte[]> encryptionKey = data.hasEncryptionKey()
                ? Optional.of(data.getEncryptionKey().toByteArray())
                : Optional.empty();
        Optional<byte[]> checksum = data.hasChecksum()
                ? Optional.of(data.getChecksum().toByteArray())
                : Optional.empty();

        // Experimental info notifications as not all fields are known.
        if (logger.isInfoEnabled()) {
            UnknownFieldSet unknownFields = data.getUnknownFields();
            if (!unknownFields.asMap().isEmpty()) {
                logger.info("-- fromProtobuf() - unknown fields: {} message: {}", unknownFields, data);
            }
        }

        AssetEncryptedAttributes attributes = new AssetEncryptedAttributes(
                domain,
                relativePath,
                modified,
                birth,
                statusChanged,
                userID,
                groupID,
                mode,
                size,
                encryptionKey,
                checksum);
        logger.debug("-- fromProtobuf() - encrypted attributes: {}", attributes);
        return attributes;
    }

    public static Optional<AssetEncryptedAttributes> fromBPList(byte[] data) {
        return NSDictionaries.parse(data)
                .map(AssetEncryptedAttributesFactory::fromDictionary);
    }

    public static AssetEncryptedAttributes fromDictionary(NSDictionary data) {
        Optional<String> domain = NSDictionaries.as(data, "domain", NSString.class)
                .map(NSString::getContent);
        Optional<String> relativePath = NSDictionaries.as(data, "relativePath", NSString.class)
                .map(NSString::getContent);
        Optional<Instant> modified = NSDictionaries.as(data, "modified", NSDate.class)
                .map(NSDate::getDate)
                .map(Date::toInstant);
        Optional<Instant> birth = NSDictionaries.as(data, "birth", NSDate.class)
                .map(NSDate::getDate)
                .map(Date::toInstant);
        Optional<Instant> statusChanged = NSDictionaries.as(data, "statusChanged", NSDate.class)
                .map(NSDate::getDate)
                .map(Date::toInstant);
        Optional<Integer> userID = NSDictionaries.as(data, "userID", NSNumber.class)
                .map(NSNumber::intValue);
        Optional<Integer> groupID = NSDictionaries.as(data, "groupID", NSNumber.class)
                .map(NSNumber::intValue);
        Optional<Integer> mode = NSDictionaries.as(data, "mode", NSNumber.class)
                .map(NSNumber::intValue);
        Optional<Long> size = NSDictionaries.as(data, "size", NSNumber.class)
                .map(NSNumber::longValue);
        Optional<byte[]> encryptionKey = NSDictionaries.as(data, "encryptionKey", NSData.class)
                .map(NSData::bytes);
        Optional<byte[]> checksum = Optional.empty();   // not present

        AssetEncryptedAttributes attributes = new AssetEncryptedAttributes(
                domain,
                relativePath,
                modified,
                birth,
                statusChanged,
                userID,
                groupID,
                mode,
                size,
                encryptionKey,
                checksum);
        logger.debug("-- fromDictionary() - encrypted attributes: {}", attributes);
        return attributes;
    }
}
