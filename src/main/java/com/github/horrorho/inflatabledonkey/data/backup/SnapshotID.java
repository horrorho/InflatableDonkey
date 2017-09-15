/*
 * The MIT License
 *
 * Copyright 2016 Ahseya.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.horrorho.inflatabledonkey.data.backup;

import java.util.Objects;
import java.util.Optional;
import javax.annotation.concurrent.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class SnapshotID {

    private static final Logger logger = LoggerFactory.getLogger(SnapshotID.class);

    public static Optional<SnapshotID> from(String id) {
        Optional<SnapshotID> snapshotID = parse(id);
        snapshotID.filter(u -> !u.toString().equals(id))
                .ifPresent(u -> {
                    logger.warn("-- from() - mismatch in: {} out: {}", id, u.toString());
                });
        return snapshotID;
    }

    static Optional<SnapshotID> parse(String id) {
        // Format: S:<uuid>
        String[] split = id.split(":");
        if (split.length != 2 || !split[0].equals("S")) {
            logger.warn("-- parse() - unexpected format: {}", id);
        }
        return split.length < 2
                ? Optional.empty()
                : Optional.of(new SnapshotID(split[1]));
    }

    private final String uuid;

    public SnapshotID(String hash) {
        this.uuid = Objects.requireNonNull(hash, "hash");
    }

    public String uuid() {
        return uuid;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.uuid);
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
        final SnapshotID other = (SnapshotID) obj;
        if (!Objects.equals(this.uuid, other.uuid)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "S:" + uuid;
    }
}
