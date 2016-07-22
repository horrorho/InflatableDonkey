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

    public static Optional<AssetID> from(String id) {
        Optional<AssetID> assetID = parse(id);
        assetID.filter(u -> !u.toString().equals(id))
                .ifPresent(u -> {
                    logger.warn("-- from() - mismatch in: {} out: {}", id, u.toString());
                });
        return assetID;
    }

    static Optional<AssetID> parse(String id) {
        if (!id.startsWith("F:")) {
            logger.warn("-- parse() - unexpected format: {}", id);
        }
        String[] split = id.split(":");
        switch (split.length) {
            case 5:
                return Optional.of(file(split));
            case 4:
                return Optional.of(directory(split));
            default:
                logger.warn("-- parse() - unexpected format: {}", id);
                return Optional.empty();
        }
    }

    static AssetID file(String[] split) {
        // Format: F:<uuid>:<base64 hash>:<size>:<tag>
        // size = size in bytes
        return new AssetID(split[1], split[2], split[3], split[4], size(split[3]));
    }

    static AssetID directory(String[] split) {
        // Format: F:<uuid>:<base64 hash>:D
        return new AssetID(split[1], split[2], split[3], "", 0);
    }

    static int size(String info) {
        try {
            return Integer.parseInt(info);
        } catch (NumberFormatException ex) {
            logger.warn("-- from() - NumberFormatException: {}", ex.getMessage());
            return 0;
        }
    }

    private final String uuid;
    private final String hash;  // Base64
    private final String info;
    private final String tag;
    private final int size;

    public AssetID(String uuid, String hash, String info, String tag, int size) {
        this.uuid = Objects.requireNonNull(uuid, "uuid");
        this.hash = Objects.requireNonNull(hash, "hash");
        this.info = Objects.requireNonNull(info, "info");
        this.tag = Objects.requireNonNull(tag, "tag");
        this.size = size;
    }

    public String uuid() {
        return uuid;
    }

    public String info() {
        return info;
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
        hash = 59 * hash + Objects.hashCode(this.uuid);
        hash = 59 * hash + Objects.hashCode(this.hash);
        hash = 59 * hash + Objects.hashCode(this.info);
        hash = 59 * hash + Objects.hashCode(this.tag);
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
        if (!Objects.equals(this.uuid, other.uuid)) {
            return false;
        }
        if (!Objects.equals(this.hash, other.hash)) {
            return false;
        }
        if (!Objects.equals(this.info, other.info)) {
            return false;
        }
        if (!Objects.equals(this.tag, other.tag)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "F:" + uuid + ":" + hash + ":" + info + (tag.isEmpty() ? "" : ":" + tag);
    }
}
