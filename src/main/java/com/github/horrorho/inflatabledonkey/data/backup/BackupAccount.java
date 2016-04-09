/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.data.backup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;

/**
 * BackupAccount.
 *
 * @author Ahseya
 */
@Immutable
public final class BackupAccount {

    private final Optional<byte[]> hmacKey;
    private final Collection<String> devices;

    public BackupAccount(Optional<byte[]> hmacKey, Collection<String> devices) {
        this.hmacKey = hmacKey.map(bs -> Arrays.copyOf(bs, bs.length));
        this.devices = new ArrayList<>(devices);
    }

    public Optional<byte[]> getHmacKey() {
        return hmacKey.map(bs -> Arrays.copyOf(bs, bs.length));
    }

    public List<String> devices() {
        return new ArrayList<>(devices);
    }

    @Override
    public String toString() {
        return "BackupAccount{"
                + super.toString()
                + ", hmacKey=" + hmacKey.map(Hex::toHexString)
                + ", devices=" + devices
                + '}';
    }
}
