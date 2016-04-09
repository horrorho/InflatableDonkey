/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.data.backup;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;

/**
 * BackupAccount.
 *
 * @author Ahseya
 */
@Immutable
public final class BackupAccount implements ProtectedRecord {

    private final Instant creation;
    private final Instant modification;
    private final byte[] hmacKey;
    private final String protectionInfoTag;
    private final byte[] protectionInfo;
    private final Collection<String> devices;

    public BackupAccount(
            Instant creation,
            Instant modification,
            byte[] hmacKey,
            Collection<String> devices,
            String protectionInfoTag,
            byte[] protectionInfo) {

        this.creation = Objects.requireNonNull(creation, "creation");
        this.modification = Objects.requireNonNull(modification, "modification");
        this.hmacKey = Arrays.copyOf(hmacKey, hmacKey.length);
        this.devices = new ArrayList<>(devices);
        this.protectionInfoTag = Objects.requireNonNull(protectionInfoTag, "protectionInfoTag");
        this.protectionInfo = Arrays.copyOf(protectionInfo, protectionInfo.length);
    }

    public Instant creation() {
        return creation;
    }

    public Instant modification() {
        return modification;
    }

    public byte[] hmacKey() {
        return Arrays.copyOf(hmacKey, hmacKey.length);
    }

    @Override
    public String protectionInfoTag() {
        return protectionInfoTag;
    }

    @Override
    public byte[] protectionInfo() {
        return Arrays.copyOf(protectionInfo, protectionInfo.length);
    }

    public List<String> devices() {
        return new ArrayList<>(devices);
    }

    @Override
    public String toString() {
        return "BackupAccount{"
                + "creation=" + creation
                + ", modification=" + modification
                + ", hmacKey=0x" + Hex.toHexString(hmacKey)
                + ", protectionInfoTag=" + protectionInfoTag
                + ", protectionInfo=0x" + Hex.toHexString(protectionInfo)
                + ", devices=" + devices
                + '}';
    }
}
