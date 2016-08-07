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

import com.dd.plist.NSDate;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;
import com.dd.plist.NSObject;
import com.dd.plist.NSString;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit;
import com.github.horrorho.inflatabledonkey.util.NSDictionaries;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snapshot.
 *
 * @author Ahseya
 */
@Immutable
public final class Snapshot extends AbstractRecord {

    private static final Logger logger = LoggerFactory.getLogger(Snapshot.class);

    private final SnapshotID snapshotID;
    private final Optional<NSDictionary> backupProperties;
    private final List<Manifest> manifests;

    Snapshot(
            CloudKit.Record record,
            SnapshotID snapshotID,
            Optional<byte[]> backupProperties,
            List<Manifest> manifests) {

        super(record);
        this.snapshotID = Objects.requireNonNull(snapshotID, "snapshotID");
        this.backupProperties = backupProperties.flatMap(NSDictionaries::parse);
        this.manifests = Objects.requireNonNull(manifests, "manifests");
    }

    public SnapshotID snapshotID() {
        return snapshotID;
    }

    <T extends NSObject> Optional<T> backupProperty(String key, Class<T> to) {
        return backupProperties.flatMap(u -> NSDictionaries.as(u, key, to));
    }

    public Optional<List<String>> appleIDs() {
        return backupProperty("AppleIDs", NSDictionary.class).map(NSDictionary::allKeys).map(Arrays::asList);
    }

    public Optional<Date> date() {
        return backupProperty("Date", NSDate.class).map(NSDate::getDate);
    }

    public Instant timestamp() {
        return date().map(Date::toInstant).orElse(modification());
    }

    public Optional<String> snapshotHMACKey() {
        return backupProperty("SnapshotHMACKey", NSString.class).map(NSString::getContent);
    }

    public Optional<String> systemDomainsVersion() {
        return backupProperty("SystemDomainsVersion", NSString.class).map(NSString::getContent);
    }

    public Optional<String> version() {
        return backupProperty("Version", NSString.class).map(NSString::getContent);
    }

    public Optional<Boolean> wasPasscodeSet() {
        return backupProperty("WasPasscodeSet", NSNumber.class).map(NSNumber::boolValue);
    }

    public List<Manifest> manifests() {
        return new ArrayList<>(manifests);
    }

    public long backupReason() {
        return recordFieldValue("backupReason")
                .map(CloudKit.RecordFieldValue::getSignedValue)
                .orElse(-1L);
    }

    public long backupType() {
        return recordFieldValue("backupType")
                .map(CloudKit.RecordFieldValue::getSignedValue)
                .orElse(-1L);
    }

    public long quotaUsed() {
        return recordFieldValue("quotaUsed")
                .map(CloudKit.RecordFieldValue::getSignedValue)
                .orElse(-1L);
    }

    public String deviceName() {
        return recordFieldValue("deviceName")
                .map(CloudKit.RecordFieldValue::getStringValue)
                .orElse("");
    }

    public String info() {
        long quotaUsed = quotaUsed();
        String quota = quotaUsed > 10737418240L
                ? String.format("%4s GB ", (quotaUsed() / 1073741824))
                : String.format("%4s MB ", (quotaUsed() / 1048576));
        Instant timestamp = date().map(Date::toInstant).orElse(modification());
        String version = version().map(u -> "iOS " + u).orElse("");
        return quota + deviceName() + " (" + version + ")  " + timestamp;
    }

    @Override
    public String toString() {
        return "Snapshot{"
                + "snapshotID=" + snapshotID
                + ", backupProperties=" + backupProperties.map(NSObject::toXMLPropertyList)
                + '}';
    }
}
