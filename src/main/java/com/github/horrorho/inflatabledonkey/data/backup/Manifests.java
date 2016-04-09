/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.data.backup;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manifests.
 *
 * @author Ahseya
 */
@Immutable
public final class Manifests implements ProtectedRecord, TimeStatistics {

    private static final Logger logger = LoggerFactory.getLogger(Manifests.class);

    private final Instant creation;
    private final Instant modification;
    private final Optional<byte[]> backupProperties;
    private final Map<String, String> attributes;
    private final List<Manifest> manifests;
    private final String protectionInfoTag;
    private final byte[] protectionInfo;

    public Manifests(
            Instant creation,
            Instant modification,
            Optional<byte[]> backupProperties,
            Map<String, String> attributes,
            List<Manifest> manifests,
            String protectionInfoTag,
            byte[] protectionInfo) {

        this.creation = Objects.requireNonNull(creation, "creation");
        this.modification = Objects.requireNonNull(modification, "modification");
        this.backupProperties = backupProperties.map(bs -> Arrays.copyOf(bs, bs.length));
        this.attributes = new HashMap<>(attributes);
        this.manifests = new ArrayList<>(manifests);
        this.protectionInfoTag = Objects.requireNonNull(protectionInfoTag, "protectionInfoTag");
        this.protectionInfo = Arrays.copyOf(protectionInfo, protectionInfo.length);
    }

    @Override
    public Instant creation() {
        return creation;
    }

    @Override
    public Instant modification() {
        return modification;
    }

    public Map<String, String> attributes() {
        return new HashMap<>(attributes);
    }

    public List<Manifest> manifests() {
        return new ArrayList<>(manifests);
    }

    @Override
    public String protectionInfoTag() {
        return protectionInfoTag;
    }

    @Override
    public byte[] protectionInfo() {
        return Arrays.copyOf(protectionInfo, protectionInfo.length);
    }

    public Optional<byte[]> backupProperties() {
        return backupProperties.map(bs -> Arrays.copyOf(bs, bs.length));
    }

    @Override
    public String toString() {
        return "Manifests{"
                + "creation=" + creation
                + ", modification=" + modification
                + ", backupProperties=" + backupProperties.map(Hex::toHexString)
                + ", attributes=" + attributes
                + ", manifests=" + manifests
                + ", protectionInfoTag=" + protectionInfoTag
                + ", protectionInfo=" + Hex.toHexString(protectionInfo)
                + '}';
    }
}
