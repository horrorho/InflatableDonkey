/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.data.backup;

import com.github.horrorho.inflatabledonkey.pcs.xzone.XZone;
import com.github.horrorho.inflatabledonkey.pcs.xzone.XZones;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.Immutable;

/**
 * ZoneRecord.
 *
 * @author Ahseya
 */
@Immutable
public final class ZoneRecord {

    public static ZoneRecord from(XZones zones, CloudKit.Record record) {
        Optional<XZone> zone = FactoryAssistant.addToZones(zones, record);

        return new ZoneRecord(zone, record);
    }

    private final Optional<XZone> zone;
    private final CloudKit.Record record;

    public ZoneRecord(Optional<XZone> zone, CloudKit.Record record) {
        this.zone = Objects.requireNonNull(zone, "zone");
        this.record = Objects.requireNonNull(record, "record");
    }

    public Optional<XZone> zone() {
        return zone;
    }

    public CloudKit.Record record() {
        return record;
    }

    @Override
    public String toString() {
        return "ZoneRecord{" + "zone=" + zone + ", record=" + record + '}';
    }
}
