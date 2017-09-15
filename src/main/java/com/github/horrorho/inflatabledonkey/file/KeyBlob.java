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
package com.github.horrorho.inflatabledonkey.file;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import javax.annotation.concurrent.Immutable;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unspecified file key blob data.
 *
 * @author Ahseya
 */
@Immutable
public final class KeyBlob {

    private static final Logger logger = LoggerFactory.getLogger(KeyBlob.class);

    public static Optional<byte[]> uuid(byte[] data) {
        return data.length < 0x10
                ? Optional.empty()
                : Optional.of(Arrays.copyOfRange(data, 0, 0x10));
    }

    public static Optional<KeyBlob> create(byte[] data) {
        logger.trace("<< create() - data: 0x{}", Hex.toHexString(data));
        if (data.length < 36) {
            logger.warn("-- create() - short input length: 0x{}", Hex.toHexString(data));
            return Optional.empty();
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);

        byte[] uuid = new byte[0x10];
        buffer.get(uuid);
        int u1 = buffer.getInt();
        int u2 = buffer.getInt();
        int protectionClass = buffer.getInt();
        int u3 = buffer.getInt();
        int length = buffer.getInt();
        if (length != 0x48) {
            logger.warn(">> create() - unsupported key length: 0x{}", Integer.toHexString(length));
            return Optional.empty();
        }

        if (buffer.remaining() < 0x48) {
            logger.warn(">> create() - short key data length: 0x{}", Integer.toHexString(buffer.remaining()));
            return Optional.empty();
        }

        byte[] publicKey = new byte[0x20];
        byte[] wrappedKey = new byte[0x28];
        buffer.get(publicKey);
        buffer.get(wrappedKey);

        // TODO iOS 10.3 beta has an additional 8 bytes after the key data.
        
        KeyBlob blob = new KeyBlob(uuid, publicKey, wrappedKey, protectionClass, u1, u2, u3);
        logger.trace(">> create() - blob: {}", blob);
        return Optional.of(blob);
    }

    private final byte[] uuid;
    private final byte[] publicKey;
    private final byte[] wrappedKey;
    private final int protectionClass;
    private final int u1;
    private final int u2;
    private final int u3;

    public KeyBlob(byte[] uuid, byte[] publicKey, byte[] wrappedKey, int protectionClass, int u1, int u2, int u3) {
        this.uuid = Arrays.copyOf(uuid, uuid.length);
        this.publicKey = Arrays.copyOf(publicKey, publicKey.length);
        this.wrappedKey = Arrays.copyOf(wrappedKey, wrappedKey.length);
        this.protectionClass = protectionClass;
        this.u1 = u1;
        this.u2 = u2;
        this.u3 = u3;
    }

    public byte[] uuid() {
        return Arrays.copyOf(uuid, uuid.length);
    }

    public String uuidBase64() {
        return Base64.getEncoder().encodeToString(uuid);
    }

    public byte[] publicKey() {
        return Arrays.copyOf(publicKey, publicKey.length);
    }

    public byte[] wrappedKey() {
        return Arrays.copyOf(wrappedKey, wrappedKey.length);
    }

    public int protectionClass() {
        return protectionClass;
    }

    public int u1() {
        return u1;
    }

    public int u2() {
        return u2;
    }

    public int u3() {
        return u3;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Arrays.hashCode(this.uuid);
        hash = 97 * hash + Arrays.hashCode(this.publicKey);
        hash = 97 * hash + Arrays.hashCode(this.wrappedKey);
        hash = 97 * hash + this.protectionClass;
        hash = 97 * hash + this.u1;
        hash = 97 * hash + this.u2;
        hash = 97 * hash + this.u3;
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
        final KeyBlob other = (KeyBlob) obj;
        if (this.protectionClass != other.protectionClass) {
            return false;
        }
        if (this.u1 != other.u1) {
            return false;
        }
        if (this.u2 != other.u2) {
            return false;
        }
        if (this.u3 != other.u3) {
            return false;
        }
        if (!Arrays.equals(this.uuid, other.uuid)) {
            return false;
        }
        if (!Arrays.equals(this.publicKey, other.publicKey)) {
            return false;
        }
        if (!Arrays.equals(this.wrappedKey, other.wrappedKey)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FileKeyBlob{"
                + "uuid=0x" + Hex.toHexString(uuid)
                + ", publicKey=0x" + Hex.toHexString(publicKey)
                + ", wrappedKey=0x" + Hex.toHexString(wrappedKey)
                + ", protectionClass=" + protectionClass
                + ", u1=0x" + Integer.toHexString(u1)
                + ", u2=0x" + Integer.toHexString(u2)
                + ", u3=0x" + Integer.toHexString(u3)
                + '}';
    }
}
