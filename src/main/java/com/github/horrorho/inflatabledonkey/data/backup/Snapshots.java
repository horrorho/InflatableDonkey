/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.data.backup;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.Immutable;

/**
 * Snapshots.
 *
 * @author Ahseya
 */
@Immutable
public final class Snapshots implements TimeStatistics {

    private final Instant creation;
    private final Instant modification;
    private final Map<String, Instant> snapshots;
    private final Map<String, String> attributes;
    private final String domainHMAC;
    private final String currentKeybagUUID;

    public Snapshots(
            Instant creation,
            Instant modification,
            Map<String, Instant> snapshots,
            String domainHMAC,
            String currentKeybagUUID,
            Map<String, String> attributes) {

        this.creation = Objects.requireNonNull(creation, "creation");
        this.modification = Objects.requireNonNull(modification, "modification");
        this.snapshots = new HashMap<>(snapshots);
        this.attributes = new HashMap<>(attributes);
        this.domainHMAC = Objects.requireNonNull(domainHMAC, "domainHMAC");
        this.currentKeybagUUID = Objects.requireNonNull(currentKeybagUUID, "currentKeybagUUID");
    }

    @Override
    public Instant creation() {
        return creation;
    }

    @Override
    public Instant modification() {
        return modification;
    }

    public Map<String, Instant> snapshots() {
        return new HashMap<>(snapshots);
    }

    public Map<String, String> attributes() {
        return new HashMap<>(attributes);
    }

    public Optional<String> attribute(String key) {
        return Optional.of(attributes.get(key));
    }

    public String attributeOrEmpty(String key) {
        return attributes.getOrDefault(key, "");
    }

    public String domainHMAC() {
        return domainHMAC;
    }

    public String currentKeybagUUID() {
        return currentKeybagUUID;
    }

    @Override
    public String toString() {
        return "Snapshots{"
                + "creation=" + creation
                + ", modification=" + modification
                + ", snapshots=" + snapshots
                + ", attributes=" + attributes
                + ", domainHMAC=" + domainHMAC
                + ", currentKeybagUUID=" + currentKeybagUUID
                + '}';
    }
}
