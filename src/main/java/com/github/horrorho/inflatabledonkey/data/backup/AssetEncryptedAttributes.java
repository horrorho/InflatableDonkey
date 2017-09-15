/*
 * The MIT License
 *
 * Copyright 2017 Ahseya.
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

import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.concurrent.Immutable;
import org.bouncycastle.util.encoders.Hex;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class AssetEncryptedAttributes {

    private final Optional<String> domain;
    private final Optional<String> relativePath;
    private final Optional<Instant> modified;
    private final Optional<Instant> birth;
    private final Optional<Instant> statusChanged;
    private final Optional<Integer> userID;
    private final Optional<Integer> groupID;
    private final Optional<Integer> mode;
    private final Optional<Long> size;
    private final Optional<byte[]> encryptionKey;
    private final Optional<byte[]> checksum;        // SHA-256
    private final Optional<Long> sizeBeforeCopy;
    private final Optional<Integer> contentEncodingMethod;
    private final Optional<Integer> contentCompressionMethod;

    public AssetEncryptedAttributes(
            Optional<String> domain,
            Optional<String> relativePath,
            Optional<Instant> modified,
            Optional<Instant> birth,
            Optional<Instant> statusChanged,
            Optional<Integer> userID,
            Optional<Integer> groupID,
            Optional<Integer> mode,
            Optional<Long> size,
            Optional<byte[]> encryptionKey,
            Optional<byte[]> checksum,
            Optional<Long> sizeBeforeCopy,
            Optional<Integer> contentEncodingMethod,
            Optional<Integer> contentCompressionMethod) {
        this.domain = Objects.requireNonNull(domain);
        this.relativePath = Objects.requireNonNull(relativePath);
        this.modified = Objects.requireNonNull(modified);
        this.birth = Objects.requireNonNull(birth);
        this.statusChanged = Objects.requireNonNull(statusChanged);
        this.userID = Objects.requireNonNull(userID);
        this.groupID = Objects.requireNonNull(groupID);
        this.mode = Objects.requireNonNull(mode);
        this.size = Objects.requireNonNull(size);
        this.encryptionKey = encryptionKey.map(bs -> Arrays.copyOf(bs, bs.length));
        this.checksum = checksum.map(bs -> Arrays.copyOf(bs, bs.length));
        this.sizeBeforeCopy = Objects.requireNonNull(sizeBeforeCopy);
        this.contentEncodingMethod = Objects.requireNonNull(contentEncodingMethod);
        this.contentCompressionMethod = Objects.requireNonNull(contentCompressionMethod);
    }

    public Optional<String> domain() {
        return domain;
    }

    public Optional<String> relativePath() {
        return relativePath;
    }

    public Optional<Instant> modified() {
        return modified;
    }

    public Optional<Instant> birth() {
        return birth;
    }

    public Optional<Instant> statusChanged() {
        return statusChanged;
    }

    public Optional<Integer> userID() {
        return userID;
    }

    public Optional<Integer> groupID() {
        return groupID;
    }

    public Optional<Integer> mode() {
        return mode;
    }

    public Optional<Long> size() {
        return size;
    }

    public Optional<byte[]> encryptionKey() {
        return encryptionKey.map(bs -> Arrays.copyOf(bs, bs.length));
    }

    public Optional<byte[]> checksum() {
        return checksum.map(bs -> Arrays.copyOf(bs, bs.length));
    }

    public Optional<Long> sizeBeforeCopy() {
        return sizeBeforeCopy;
    }

    public Optional<Integer> contentEncodingMethod() {
        return contentEncodingMethod;
    }

    public Optional<Integer> contentCompressionMethod() {
        return contentCompressionMethod;
    }

    @Override
    public String toString() {
        return "AssetEncryptedAttributes{"
                + "domain=" + domain
                + ", relativePath=" + relativePath
                + ", modified=" + modified
                + ", birth=" + birth
                + ", statusChanged=" + statusChanged
                + ", userID=" + userID
                + ", groupID=" + groupID
                + ", mode=" + mode
                + ", size=" + size
                + ", encryptionKey=0x" + encryptionKey.map((Hex::toHexString))
                + ", checksum=0x" + checksum.map((Hex::toHexString))
                + ", sizeBeforeCopy=" + sizeBeforeCopy
                + ", contentEncodingMethod=" + contentEncodingMethod
                + ", contentCompressionMethod=" + contentCompressionMethod
                + '}';
    }
}
