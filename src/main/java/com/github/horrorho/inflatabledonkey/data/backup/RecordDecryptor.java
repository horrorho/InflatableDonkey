/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.data.backup;

import com.github.horrorho.inflatabledonkey.pcs.xzone.XZones;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import net.jcip.annotations.Immutable;

/**
 * RecordDecryptor.
 *
 * @author Ahseya
 */
@Immutable
public final class RecordDecryptor {

    public static RecordDecryptor from(XZones zones, CloudKit.Record record) {
        BiFunction<byte[], String, Optional<byte[]>> decryptor = FactoryAssistant.decryptor(zones, record);

        return new RecordDecryptor(record, decryptor);
    }

    private final CloudKit.Record record;
    private final BiFunction<byte[], String, Optional<byte[]>> decryptor;

    public RecordDecryptor(CloudKit.Record record, BiFunction<byte[], String, Optional<byte[]>> immutableDecryptor) {
        this.record = Objects.requireNonNull(record, "record");
        this.decryptor = Objects.requireNonNull(immutableDecryptor);
    }

    public CloudKit.Record record() {
        return record;
    }

    public BiFunction<byte[], String, Optional<byte[]>> decryptor() {
        return decryptor;
    }

    @Override
    public String toString() {
        return "RecordDecryptor{" + "record=" + record + ", decryptor=" + decryptor + '}';
    }
}
