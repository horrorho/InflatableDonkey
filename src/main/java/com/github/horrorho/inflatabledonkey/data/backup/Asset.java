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

import com.dd.plist.NSDictionary;
import com.github.horrorho.inflatabledonkey.util.PLists;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;

/**
 * Asset.
 *
 * @author Ahseya
 */
@Immutable
public final class Asset {

    private final int protectionClass;
    private final int size;
    private final int fileType;
    private final Instant downloadTokenExpiration;
    private final Optional<String> contentBaseURL;
    private final Optional<byte[]> fileChecksum;
    private final Optional<byte[]> fileSignature;
    private final Optional<byte[]> keyEncryptionKey;
    private final Optional<byte[]> encryptedAttributes;

    public Asset(
            int protectionClass,
            int size,
            int fileType,
            Instant downloadTokenExpiration,
            Optional<String> contentBaseURL,
            Optional<byte[]> fileChecksum,
            Optional<byte[]> fileSignature,
            Optional<byte[]> keyEncryptionKey,
            Optional<byte[]> encryptedAttributes) {

        this.protectionClass = protectionClass;
        this.size = size;
        this.fileType = fileType;
        this.downloadTokenExpiration = downloadTokenExpiration;
        this.contentBaseURL = Objects.requireNonNull(contentBaseURL, "contentBaseURL");
        this.fileChecksum = Objects.requireNonNull(fileChecksum, "fileChecksum");
        this.fileSignature = Objects.requireNonNull(fileSignature, "fileSignature");
        this.keyEncryptionKey = Objects.requireNonNull(keyEncryptionKey);
        this.encryptedAttributes = Objects.requireNonNull(encryptedAttributes, "encryptedAttributes");
    }

    public int getProtectionClass() {
        return protectionClass;
    }

    public int getSize() {
        return size;
    }

    public int getFileType() {
        return fileType;
    }

    public Instant getDownloadTokenExpiration() {
        return downloadTokenExpiration;
    }

    public Optional<String> getContentBaseURL() {
        return contentBaseURL;
    }

    public Optional<byte[]> getFileChecksum() {
        return fileChecksum.map(bs -> Arrays.copyOf(bs, bs.length));
    }

    public Optional<byte[]> getFileSignature() {
        return fileSignature.map(bs -> Arrays.copyOf(bs, bs.length));
    }

    public Optional<byte[]> getKeyEncryptionKey() {
        return keyEncryptionKey.map(bs -> Arrays.copyOf(bs, bs.length));
    }

    public Optional<NSDictionary> getEncryptedAttributes() {
        return encryptedAttributes.map(bs -> PLists.<NSDictionary>parse(bs));
    }

    @Override
    public String toString() {
        return "Asset{"
                + "protectionClass=" + protectionClass
                + ", size=" + size
                + ", fileType=" + fileType
                + ", downloadTokenExpiration=" + downloadTokenExpiration
                + ", contentBaseURL=" + contentBaseURL
                + ", fileChecksum=" + fileChecksum.map(Hex::toHexString)
                + ", fileSignature=" + fileSignature.map(Hex::toHexString)
                + ", keyEncryptionKey=" + keyEncryptionKey.map(Hex::toHexString)
                + ", encryptedAttributes=" + encryptedAttributes.map(Hex::toHexString)
                + '}';
    }
}
