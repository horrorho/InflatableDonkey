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
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class ManifestID {

    private static final Logger logger = LoggerFactory.getLogger(ManifestID.class);

    public static Optional<ManifestID> from(String id) {
        Optional<ManifestID> manifestID = parse(id);
        manifestID.filter(u -> !u.toString().equals(id))
                .ifPresent(u -> {
                    logger.warn("-- from() - mismatch in: {} out: {}", id, u.toString());
                });
        return manifestID;
    }

    static Optional<ManifestID> parse(String id) {
        // Format: M:<uuid>:<base64 hash>
        // hash = HMAC-SHA1 <domain name> using Snapshot/ backupProperties/ SnapshotHMACKey
        String[] split = id.split(":");
        if (split.length != 3 || !split[0].endsWith("M")) {
            logger.warn("-- parse() - unexpected format: {}", id);
        }
        return split.length < 3
                ? Optional.empty()
                : Optional.of(new ManifestID(split[1], split[2]));
    }

    private final String uuid;
    private final String hash;  // Base64

    public ManifestID(String uuid, String hash) {
        this.uuid = Objects.requireNonNull(uuid, "uuid");
        this.hash = Objects.requireNonNull(hash, "hash");
    }

    public String uuid() {
        return uuid;
    }

    public String hash() {
        return hash;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.uuid);
        hash = 41 * hash + Objects.hashCode(this.hash);
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
        final ManifestID other = (ManifestID) obj;
        if (!Objects.equals(this.uuid, other.uuid)) {
            return false;
        }
        if (!Objects.equals(this.hash, other.hash)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "M:" + uuid + ":" + hash;
    }
}
