/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.data.backup;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSString;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import com.github.horrorho.inflatabledonkey.util.PLists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manifests.
 *
 * @author Ahseya
 */
@Immutable
public final class Manifests extends AbstractRecord {

    private static final Logger logger = LoggerFactory.getLogger(Manifests.class);

    private final Optional<byte[]> backupProperties;
    private final List<Manifest> manifests;

    public Manifests(Optional<byte[]> backupProperties, List<Manifest> manifests, Map<String, String> attributes) {
        super(attributes);
        this.backupProperties = Objects.requireNonNull(backupProperties, "backupProperties");
        this.manifests = Objects.requireNonNull(manifests, "manifests");
    }

    public Manifests(
            Optional<byte[]> backupProperties,
            List<Manifest> manifests,
            Collection<CloudKit.RecordField> recordFields) {

        super(recordFields);
        this.backupProperties = Objects.requireNonNull(backupProperties, "backupProperties");
        this.manifests = Objects.requireNonNull(manifests, "manifests");
    }

    public Optional<NSDictionary> backupProperties() {
        return backupProperties.map(bs -> PLists.<NSDictionary>parseLegacy(bs));
    }

    public List<Manifest> manifests() {
        return new ArrayList<>(manifests);
    }

    @Override
    public String toString() {
        return "Manifests{"
                + super.toString()
                + ", backupProperties=" + backupProperties
                + ", manifests=" + manifests
                + '}';
    }
}
