/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.data.backup;

import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import com.google.protobuf.ByteString;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;

/**
 * AssetsFactory.
 *
 * @author Ahseya
 */
@Immutable
public final class AssetsFactory {

    private static final String DOMAIN = "domain";
    private static final String FILES = "files";

//    public static Assets from(ZoneRecord recordDecryptor) {
//        return fromLegacy(recordDecryptor.record(), recordDecryptor.zone());
//    }

    public static Assets from(CloudKit.Record record, BiFunction<byte[], String, Optional<byte[]>> decrypt) {
        List<CloudKit.RecordField> records = record.getRecordFieldList();
        List<String> files = files(records);

        Optional<String> domain = domain(records)
                .flatMap(bs -> decrypt.apply(bs, DOMAIN))
                .map(String::new);

        return new Assets(domain, files);
    }

//    public static Assets fromLegacy(CloudKit.Record record, Optional<XZone> zone) {
//        List<CloudKit.RecordField> records = record.getRecordFieldList();
//
//        List<String> files = files(records);
//
//        Optional<String> domain = domain(records)
//                .flatMap(bs -> zone.map(z -> z.decrypt(bs, DOMAIN)))
//                .map(String::new);
//
//        return new Assets(domain, files);
//    }

    static Optional<byte[]> domain(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(c -> c.getIdentifier().getName().equals(DOMAIN))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getBytesValue)
                .map(ByteString::toByteArray)
                .findFirst();
    }

    static List<String> files(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(c -> c.getIdentifier().getName().equals(FILES))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getRecordFieldValueList)
                .flatMap(Collection::stream)
                .map(CloudKit.RecordFieldValue::getReferenceValue)
                .map(CloudKit.RecordReference::getRecordIdentifier)
                .map(CloudKit.RecordIdentifier::getValue)
                .map(CloudKit.Identifier::getName)
                .collect(Collectors.toList());
    }
}
