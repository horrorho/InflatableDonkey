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
public final class DeviceID {

    private static final Logger logger = LoggerFactory.getLogger(DeviceID.class);

    public static Optional<DeviceID> from(String id) {
        Optional<DeviceID> deviceID = parse(id);
        deviceID.filter(u -> !u.toString().equals(id))
                .ifPresent(u -> {
                    logger.warn("-- from() - mismatch in: {} out: {}", id, u.toString());
                });
        return deviceID;
    }

    static Optional<DeviceID> parse(String id) {
        // Format: D:<base64 hash>
        String[] split = id.split(":");
        if (split.length != 2 || !split[0].equals("D")) {
            logger.warn("-- parse() - unexpected format: {}", id);
        }
        return split.length < 2
                ? Optional.empty()
                : Optional.of(new DeviceID(split[1]));
    }
    private final String hash;

    public DeviceID(String hash) {
        this.hash = Objects.requireNonNull(hash, "hash");
    }

    public String hash() {
        return hash;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.hash);
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
        final DeviceID other = (DeviceID) obj;
        if (!Objects.equals(this.hash, other.hash)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "D:" + hash;
    }
}
