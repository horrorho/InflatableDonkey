/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.data.backup;

import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * AbstractRecord.
 *
 * @author Ahseya
 */
public abstract class AbstractRecord {

    private final Map<String, String> attributes;

    public AbstractRecord(Map<String, String> attributes) {
        this.attributes = new HashMap<>(attributes);
    }

    public AbstractRecord(Collection<CloudKit.RecordField> recordFields) {
        this.attributes = RecordAssistant.attributes(recordFields);
    }

    public Map<String, String> attributes() {
        return new HashMap<>(attributes);
    }

    public Optional<String> attribute(String name) {
        return Optional.ofNullable(attributes.get(name));

    }

    @Override
    public String toString() {
        return "AbstractRecord{" + "attributes=" + attributes + '}';
    }
}
