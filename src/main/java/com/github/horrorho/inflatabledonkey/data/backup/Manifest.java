/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.data.backup;

import java.util.Objects;
import net.jcip.annotations.Immutable;

/**
 * Manifest.
 *
 * @author Ahseya
 */
@Immutable
public final class Manifest {

    private final int count;
    private final int checksum;
    private final String id;

    public Manifest(int count, int checksum, String id) {
        this.count = count;
        this.checksum = checksum;
        this.id = Objects.requireNonNull(id, "id");
    }

    public int count() {
        return count;
    }

    public int checksum() {
        return checksum;
    }

    public String id() {
        return id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.count;
        hash = 23 * hash + this.checksum;
        hash = 23 * hash + Objects.hashCode(this.id);
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
        final Manifest other = (Manifest) obj;
        if (this.count != other.count) {
            return false;
        }
        if (this.checksum != other.checksum) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Manifest{" + "count=" + count + ", checksum=" + checksum + ", id=" + id + '}';
    }
}
