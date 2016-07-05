/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
                .toUpperCase();
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
