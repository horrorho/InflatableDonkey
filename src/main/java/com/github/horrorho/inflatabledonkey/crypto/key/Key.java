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
package com.github.horrorho.inflatabledonkey.crypto.key;

import com.github.horrorho.inflatabledonkey.crypto.eckey.ECKey;
import com.github.horrorho.inflatabledonkey.data.der.PublicKeyInfo;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Key. Decorates ECKey types.
 *
 * @author Ahseya
 * @param <T> key data type
 */
@Immutable
public final class Key<T extends ECKey> {

    private static final Logger logger = LoggerFactory.getLogger(Key.class);

    private final KeyID keyID;
    private final T keyData;
    private final byte[] publicExportData;
    private final Optional<PublicKeyInfo> publicKeyInfo;
    private final boolean isCompact;
    private final boolean isTrusted;

    Key(
            KeyID keyID,
            T keyData,
            byte[] publicExportData,
            Optional<PublicKeyInfo> publicKeyInfo,
            boolean isCompact,
            boolean isTrusted) {

        this.keyID = Objects.requireNonNull(keyID, "keyID");
        this.keyData = Objects.requireNonNull(keyData, "keyData");
        this.publicExportData = Arrays.copyOf(publicExportData, publicExportData.length);
        this.isCompact = isCompact;
        this.publicKeyInfo = Objects.requireNonNull(publicKeyInfo, "publicKeyInfo");
        this.isTrusted = isTrusted;
    }

    Key(KeyID keyID, T keyData, byte[] publicExportData, Optional<PublicKeyInfo> publicKeyInfo, boolean isCompact) {
        this(keyID, keyData, publicExportData, publicKeyInfo, isCompact, false);
    }

    public KeyID keyID() {
        return keyID;
    }

    public T keyData() {
        return keyData;
    }

    public byte[] publicExportData() {
        return Arrays.copyOf(publicExportData, publicExportData.length);
    }

    public boolean isCompact() {
        return isCompact;
    }

    public boolean isTrusted() {
        return isTrusted;
    }

    public Optional<PublicKeyInfo> publicKeyInfo() {
        return publicKeyInfo;
    }

    public Optional<Integer> service() {
        return publicKeyInfo.map(PublicKeyInfo::service);
    }

    public Key<T> selfVerify() {
        return verify(this);
    }

    public Key<T> verify(Key<? extends ECKey> masterKey) {
        return SignatureAssistant.verify(this, masterKey)
                ? new Key<>(keyID, keyData, publicExportData, publicKeyInfo, isCompact, true)
                : this;
    }

    @Override
    public String toString() {
        return "Key{"
                + "keyID=" + keyID
                + ", keyData=" + keyData
                + ", publicExportData=" + Hex.toHexString(publicExportData)
                + ", publicKeyInfo=" + publicKeyInfo
                + ", isCompact=" + isCompact
                + ", isTrusted=" + isTrusted
                + '}';
    }
}
