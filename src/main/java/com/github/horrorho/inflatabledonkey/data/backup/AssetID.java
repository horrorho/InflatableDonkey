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
public final class AssetID {

    private static final Logger logger = LoggerFactory.getLogger(AssetID.class);

    public static Optional<AssetID> from(String formatted) {
        // Format: F:<uuid>:<base64 hash>:<size>:<tag>
        String[] split = formatted.split(":");
        if (split.length != 5) {
            logger.warn("-- from() - unexpected format: {}", formatted);
        }
        try {
            return split.length < 5
                    ? Optional.empty()
                    : Optional.of(new AssetID(split[1], split[2], split[4], Integer.parseInt(split[3])));
        } catch (NumberFormatException ex) {
            logger.warn("-- from() - NumberFormatException: {}", ex.getMessage());
            return Optional.empty();
        }
    }

    private final String uuid;
    private final String hash;  // Base64
    private final String tag;
    private final int size;

    public AssetID(String uuid, String hash, String tag, int size) {
        this.uuid = Objects.requireNonNull(uuid, "uuid");
        this.hash = Objects.requireNonNull(hash, "hash");
        this.tag = Objects.requireNonNull(tag, "tag");
        this.size = size;
    }

    public String uuid() {
        return uuid;
    }

    public String hash() {
        return hash;
    }

    public String tag() {
        return tag;
    }

    public int size() {
        return size;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.uuid);
        hash = 41 * hash + Objects.hashCode(this.hash);
        hash = 41 * hash + Objects.hashCode(this.tag);
        hash = 41 * hash + this.size;
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
        final AssetID other = (AssetID) obj;
        if (this.size != other.size) {
            return false;
        }
        if (!Objects.equals(this.uuid, other.uuid)) {
            return false;
        }
        if (!Objects.equals(this.hash, other.hash)) {
            return false;
        }
        if (!Objects.equals(this.tag, other.tag)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "F:" + uuid + ":" + hash + ":" + size + ":" + tag;
    }
}
