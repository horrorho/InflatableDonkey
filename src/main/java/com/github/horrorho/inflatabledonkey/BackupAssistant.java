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
package com.github.horrorho.inflatabledonkey;

import com.github.horrorho.inflatabledonkey.cloud.accounts.Account;
import com.github.horrorho.inflatabledonkey.cloud.clients.AssetTokenClient;
import com.github.horrorho.inflatabledonkey.cloud.clients.AssetsClient;
import com.github.horrorho.inflatabledonkey.cloud.clients.BackupAccountClient;
import com.github.horrorho.inflatabledonkey.cloud.clients.DeviceClient;
import com.github.horrorho.inflatabledonkey.cloud.clients.MBKSyncClient;
import com.github.horrorho.inflatabledonkey.cloud.clients.SnapshotClient;
import com.github.horrorho.inflatabledonkey.cloud.cloudkit.CKInit;
import com.github.horrorho.inflatabledonkey.cloud.cloudkit.CKInits;
import com.github.horrorho.inflatabledonkey.cloud.escrow.EscrowedKeys;
import com.github.horrorho.inflatabledonkey.cloudkitty.CloudKitties;
import com.github.horrorho.inflatabledonkey.cloudkitty.CloudKitty;
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.data.backup.Assets;
import com.github.horrorho.inflatabledonkey.data.backup.BackupAccount;
import com.github.horrorho.inflatabledonkey.data.backup.Device;
import com.github.horrorho.inflatabledonkey.data.backup.Manifest;
import com.github.horrorho.inflatabledonkey.data.backup.Snapshot;
import com.github.horrorho.inflatabledonkey.data.backup.SnapshotID;
import com.github.horrorho.inflatabledonkey.pcs.service.ServiceKeySet;
import com.github.horrorho.inflatabledonkey.pcs.zone.ProtectionZone;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Backup.
 *
 * @author Ahseya
 */
@Immutable
public final class BackupAssistant {

    public static BackupAssistant create(HttpClient httpClient, Account account) throws IOException {
        CKInit ckInit = CKInits.ckInitBackupd(httpClient, account);
        CloudKitty kitty = CloudKitties.backupd(ckInit, account);
        ServiceKeySet escrowServiceKeySet = EscrowedKeys.keys(httpClient, account);
        ProtectionZone mbksync = MBKSyncClient.mbksync(httpClient, kitty, escrowServiceKeySet.keys()).get();

        return new BackupAssistant(kitty, mbksync);
    }

    private static final Logger logger = LoggerFactory.getLogger(BackupAssistant.class);

    private final CloudKitty kitty;
    private final ProtectionZone mbksync;

    public BackupAssistant(CloudKitty kitty, ProtectionZone mbksync, KeyBagManager keyBagManager) {
        this.kitty = Objects.requireNonNull(kitty, "kitty");
        this.mbksync = Objects.requireNonNull(mbksync, "mbksync");
    }

    public BackupAssistant(CloudKitty kitty, ProtectionZone mbksync) {
        this(kitty, mbksync, KeyBagManager.create(kitty, mbksync));
    }

    public Optional<BackupAccount> backupAccount(HttpClient httpClient) throws IOException {
        return BackupAccountClient.backupAccount(httpClient, kitty, mbksync);
    }

    public List<Device> devices(HttpClient httpClient, Collection<String> devices) throws IOException {
        return DeviceClient.device(httpClient, kitty, devices);
    }

    public List<Snapshot> snapshots(HttpClient httpClient, Collection<SnapshotID> snapshotIDs) throws IOException {
        return SnapshotClient.snapshots(httpClient, kitty, mbksync, snapshotIDs);
    }

    public Map<Device, List<Snapshot>> snapshotsForDevices(HttpClient httpClient, Collection<Device> devices)
            throws IOException {

        Map<String, Device> snapshotDevice = devices.stream()
                .map(d -> d
                        .snapshots()
                        .stream()
                        .collect(Collectors.toMap(
                                SnapshotID::id,
                                s -> d,
                                (a, b) -> {
                                    logger.warn("-- snapshotsForDevices() - collision: {} {}", a, b);
                                    return a;
                                })))
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<SnapshotID> snapshotIDs = devices.stream()
                .map(Device::snapshots)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return snapshots(httpClient, snapshotIDs)
                .stream()
                .collect(Collectors.groupingBy(s -> snapshotDevice.get(s.name())));
    }

    public List<Assets> assetsList(HttpClient httpClient, Snapshot snapshot) throws IOException {
        List<Manifest> manifests = snapshot.manifests()
                .stream()
                .filter(x -> x.count() != 0)
                .collect(Collectors.toList());

        return AssetsClient.assets(httpClient, kitty, mbksync, manifests);
    }

    public List<Asset> assets(HttpClient httpClient, Collection<Assets> assets) throws IOException {
        return AssetTokenClient.assets(httpClient, kitty, mbksync, assets);
    }

    public KeyBagManager newKeyBagManager() {
        return KeyBagManager.create(kitty, mbksync);
    }
}
