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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.concurrent.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class Device extends AbstractRecord {

    private static final Logger logger = LoggerFactory.getLogger(Device.class);

    public static final String DOMAIN_HMAC = "domainHMAC";
    public static final String KEYBAG_UUID = "currentKeybagUUID";

    private final DeviceID deviceID;
    private final List<SnapshotIDTimestamp> snapshotIDTimestamps;

    Device(CloudKit.Record record, DeviceID deviceID, Collection<SnapshotIDTimestamp> snapshotIDTimestamps) {
        super(record);
        this.deviceID = Objects.requireNonNull(deviceID, "deviceID");
        this.snapshotIDTimestamps = new ArrayList<>(snapshotIDTimestamps);
    }

    public DeviceID deviceID() {
        return deviceID;
    }

    public List<SnapshotIDTimestamp> snapshotIDTimestamps() {
        return new ArrayList<>(snapshotIDTimestamps);
    }

    public List<SnapshotID> snapshotIDs() {
        return snapshotIDTimestamps
                .stream()
                .map(SnapshotIDTimestamp::snapshotID)
                .collect(Collectors.toList());
    }

    public Optional<String> domainHMAC() {
        return recordFieldValue(DOMAIN_HMAC)
                .map(CloudKit.Record.Field.Value::getStringValue);
    }

    public Optional<String> currentKeybagUUID() {
        return recordFieldValue(KEYBAG_UUID)
                .map(CloudKit.Record.Field.Value::getStringValue);
    }

    public String deviceClass() {
        return recordFieldValue("deviceClass")
                .map(CloudKit.Record.Field.Value::getStringValue)
                .orElse("");
    }

    public String hardwareModel() {
        return recordFieldValue("hardwareModel")
                .map(CloudKit.Record.Field.Value::getStringValue)
                .orElse("");
    }

    public String marketingName() {
        return recordFieldValue("marketingName")
                .map(CloudKit.Record.Field.Value::getStringValue)
                .orElse("");
    }

    public String productType() {
        return recordFieldValue("productType")
                .map(CloudKit.Record.Field.Value::getStringValue)
                .orElse("");
    }

    public String serialNumber() {
        return recordFieldValue("serialNumber")
                .map(CloudKit.Record.Field.Value::getStringValue)
                .orElse("");
    }

    public String info() {
        return deviceID.hash().toUpperCase(Locale.US) + " " + productType() + " " + hardwareModel();
    }

    @Override
    public String toString() {
        return "Device{" + "deviceID=" + deviceID + ", snapshotIDTimestamps=" + snapshotIDTimestamps + '}';
    }
}
