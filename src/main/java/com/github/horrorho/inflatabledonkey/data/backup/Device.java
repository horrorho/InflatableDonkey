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

import com.github.horrorho.inflatabledonkey.args.Property;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Device.
 *
 * @author Ahseya
 */
@Immutable
public final class Device extends AbstractRecord {

    private static final Logger logger = LoggerFactory.getLogger(Device.class);

    public static final String DOMAIN_HMAC = "domainHMAC";
    public static final String KEYBAG_UUID = "currentKeybagUUID";

    private final Collection<SnapshotID> snapshots;

    public Device(Collection<SnapshotID> snapshots, CloudKit.Record record) {
        super(record);
        this.snapshots = new ArrayList<>(snapshots);
    }

    public List<SnapshotID> snapshots() {
        return new ArrayList<>(snapshots);
    }

    public Map<String, Instant> snapshotTimestampMap() {
        return snapshots.stream()
                .collect(Collectors.toMap(
                        SnapshotID::id,
                        SnapshotID::timestamp,
                        (a, b) -> {
                            logger.warn("-- snapshotTimestampMap() - collision: {} {}", a, b);
                            return a;
                        }
                ));
    }

    public Optional<String> domainHMAC() {
        return recordFieldValue(DOMAIN_HMAC)
                .map(CloudKit.RecordFieldValue::getStringValue);
    }

    public Optional<String> currentKeybagUUID() {
        return recordFieldValue(KEYBAG_UUID)
                .map(CloudKit.RecordFieldValue::getStringValue);
    }

    public String deviceClass() {
        return recordFieldValue("deviceClass")
                .map(CloudKit.RecordFieldValue::getStringValue)
                .orElse("");
    }

    public String hardwareModel() {
        return recordFieldValue("hardwareModel")
                .map(CloudKit.RecordFieldValue::getStringValue)
                .orElse("");
    }

    public String marketingName() {
        return recordFieldValue("marketingName")
                .map(CloudKit.RecordFieldValue::getStringValue)
                .orElse("");
    }

    public String productType() {
        return recordFieldValue("productType")
                .map(CloudKit.RecordFieldValue::getStringValue)
                .orElse("");
    }

    public String serialNumber() {
        return recordFieldValue("serialNumber")
                .map(CloudKit.RecordFieldValue::getStringValue)
                .orElse("");
    }

    public String uuid() {
        String[] split = name().split(":");
        if (split.length < 2) {
            logger.warn("-- main() - unsupported device name format: {}", name());
            return name();
        }
        return split[1].toUpperCase(Property.locale())
                .toUpperCase(Property.locale());
    }

    public String info() {
        return uuid() + " "
                + productType() + " "
                + hardwareModel();
    }

    @Override
    public String toString() {
        return "Device{" + super.toString() + '}';
    }
}
