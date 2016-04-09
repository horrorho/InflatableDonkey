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
import java.util.Map;
import java.util.Optional;
import net.jcip.annotations.Immutable;

/**
 * Snapshots.
 *
 * @author Ahseya
 */
@Immutable
public final class Snapshots extends AbstractRecord {

    public static final String DOMAIN_HMAC = "domainHMAC";
    public static final String KEYBAG_UUID = "currentKeybagUUID";

    private final Collection<Snapshot> snapshots;

    public Snapshots(Collection<Snapshot> snapshots, Map<String, String> attributes) {
        super(attributes);
        this.snapshots = new ArrayList<>(snapshots);
    }

    public Snapshots(Collection<Snapshot> snapshots, Collection<CloudKit.RecordField> recordFields) {
        super(recordFields);
        this.snapshots = new ArrayList<>(snapshots);
    }

    public List<Snapshot> snapshots() {
        return new ArrayList<>(snapshots);
    }

    public Optional<String> domainHMAC() {
        return attribute(DOMAIN_HMAC);
    }

    public Optional<String> currentKeybagUUID() {
        return attribute(KEYBAG_UUID);
    }

    @Override
    public String toString() {
        return "Snapshots{"
                + super.toString()
                + ", snapshots=" + snapshots
                + '}';
    }
}
