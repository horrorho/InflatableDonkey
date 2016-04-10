/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.data.backup;

import com.github.horrorho.inflatabledonkey.pcs.xzone.XZone;
import com.github.horrorho.inflatabledonkey.pcs.xzone.XZones;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FactoryAssistant .
 *
 * @author Ahseya
 */
@Immutable
public final class FactoryAssistant {

    private static final Logger logger = LoggerFactory.getLogger(FactoryAssistant.class);

    public static Map<String, String> attributes(Collection<CloudKit.RecordField> recordFields) {
        return recordFields.stream()
                .filter(f -> f.getValue().hasStringValue())
                .collect(Collectors.toMap(
                        f -> f.getIdentifier().getName(),
                        f -> f.getValue().getStringValue(),
                        (a, b) -> {
                            logger.warn("-- attributes() - duplicate items: {} {}", a, b);
                            return a;
                        }));
    }

    public static BiFunction<byte[], String, Optional<byte[]>> decryptor(Optional<XZone> zone) {
        return (bs, label) -> zone.map(z -> z.decrypt(bs, label));
    }

    public static BiFunction<byte[], String, Optional<byte[]>> decryptor(XZones zones, CloudKit.Record record) {
        return decryptor(addToZones(zones, record));
    }

    public static Optional<XZone> addToZones(XZones zones, CloudKit.Record record) {
        if (record.hasProtectionInfo()) {
            CloudKit.ProtectionInfo protectionInfo = record.getProtectionInfo();

            if (protectionInfo.hasProtectionInfoTag() && protectionInfo.hasProtectionInfo()) {
                return zones.put(
                        protectionInfo.getProtectionInfoTag(),
                        protectionInfo.getProtectionInfo().toByteArray());
            }
        }

        return Optional.empty();
    }
}
