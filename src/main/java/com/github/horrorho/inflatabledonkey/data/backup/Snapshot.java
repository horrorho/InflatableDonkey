/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.data.backup;

import java.time.Instant;
import java.util.Objects;
import net.jcip.annotations.Immutable;

/**
 * Manifest.
 *
 * @author Ahseya
 */
@Immutable
public final class Snapshot {

    private final Instant timestamp;
    private final String id;

    public Snapshot(Instant timestamp, String id) {
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp");
        this.id = Objects.requireNonNull(id, "id");
    }

    public Instant timestamp() {
        return timestamp;
    }

    public String id() {
        return id;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.timestamp);
        hash = 37 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Snapshot other = (Snapshot) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.timestamp, other.timestamp)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Snapshot{" + "timestamp=" + timestamp + ", id=" + id + '}';
    }
}
