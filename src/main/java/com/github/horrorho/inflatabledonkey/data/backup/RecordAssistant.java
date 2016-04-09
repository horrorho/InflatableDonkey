/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.data.backup;

import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ProtectionRecordAssistant.
 *
 * @author Ahseya
 */
@Immutable
public final class RecordAssistant {

    private static final Logger logger = LoggerFactory.getLogger(RecordAssistant.class);

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
}
