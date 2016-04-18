/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.data.backup;

import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import net.jcip.annotations.Immutable;

/**
 * Snapshots.
 *
 * @author Ahseya
 */
@Immutable
public final class Device extends AbstractRecord {

    public static final String DOMAIN_HMAC = "domainHMAC";
    public static final String KEYBAG_UUID = "currentKeybagUUID";

    private final Collection<Snapshot> snapshots;

    public Device(Collection<Snapshot> snapshots, Collection<CloudKit.RecordField> recordFields) {
        super(recordFields);
        this.snapshots = new ArrayList<>(snapshots);
    }

    public List<Snapshot> snapshots() {
        return new ArrayList<>(snapshots);
    }

    public Optional<String> domainHMAC() {
        return recordFieldValue(DOMAIN_HMAC)
                .map(CloudKit.RecordFieldValue::getStringValue);
    }

    public Optional<String> currentKeybagUUID() {
        return recordFieldValue(KEYBAG_UUID)
                .map(CloudKit.RecordFieldValue::getStringValue);
    }

    @Override
    public String toString() {
        return "Snapshots{"
                + super.toString()
                + ", snapshots=" + snapshots
                + '}';
    }
}
