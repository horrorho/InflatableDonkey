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
public final class ManifestIDIndex {

    private static final Logger logger = LoggerFactory.getLogger(ManifestIDIndex.class);

    public static Optional<ManifestIDIndex> from(String id) {
        Optional<ManifestIDIndex> manifestIDIndex = parse(id);
        manifestIDIndex.filter(u -> !u.toString().equals(id))
                .ifPresent(u -> {
                    logger.warn("-- from() - mismatch in: {} out: {}", id, u.toString());
                });
        return manifestIDIndex;
    }

    static Optional<ManifestIDIndex> parse(String idIndex) {
        // Format: M:<uuid>:<base64 hash>:<index>
        String[] split = idIndex.split(":");
        if (split.length != 4 || !split[0].endsWith("M")) {
            logger.warn("-- parse() - unexpected format: {}", idIndex);
        }
        if (split.length < 4) {
            return Optional.empty();
        }

        int index;
        try {
            index = Integer.parseInt(split[3]);
        } catch (NumberFormatException ex) {
            logger.warn("-- parse() - input: {} NumberFormatException: {}", idIndex, ex.getMessage());
            return Optional.empty();
        }
        return Optional.of(new ManifestIDIndex(split[1], split[2], index));
    }

    private final ManifestID id;
    private final int index;

    public ManifestIDIndex(ManifestID id, int index) {
        this.id = Objects.requireNonNull(id, "id");
        this.index = index;
    }

    public ManifestIDIndex(String uuid, String hash, int index) {
        this(new ManifestID(uuid, hash), index);
    }

    public ManifestID id() {
        return id;
    }

    public int index() {
        return index;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.id);
        hash = 43 * hash + this.index;
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
        final ManifestIDIndex other = (ManifestIDIndex) obj;
        if (this.index != other.index) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id + ":" + index;
    }
}
