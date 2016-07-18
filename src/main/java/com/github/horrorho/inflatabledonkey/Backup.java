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

import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.data.backup.Assets;
import com.github.horrorho.inflatabledonkey.data.backup.BackupAccount;
import com.github.horrorho.inflatabledonkey.data.backup.Device;
import com.github.horrorho.inflatabledonkey.data.backup.Snapshot;
import com.github.horrorho.inflatabledonkey.data.backup.SnapshotID;
import com.github.horrorho.inflatabledonkey.util.ListUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
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
public final class Backup {

    private static final Logger logger = LoggerFactory.getLogger(Backup.class);

    private final BackupAssistant backupAssistant;
    private final DownloadAssistant downloadAssistant;
//    private final Predicate<Optional<String>> domainFilter;
//    private final Predicate<Asset> assetFilter;

    public Backup(BackupAssistant backupAssistant, DownloadAssistant downloadAssistant) {
        this.backupAssistant = Objects.requireNonNull(backupAssistant, "backupAssistant");
        this.downloadAssistant = Objects.requireNonNull(downloadAssistant, "downloadAssistant");
    }

    public Map<Device, List<Snapshot>> snapshots(HttpClient httpClient) throws IOException {
        BackupAccount backupAccount = backupAssistant.backupAccount(httpClient);
        logger.debug("-- snapshots() - backup account: {}", backupAccount);

        List<Device> devices = backupAssistant.devices(httpClient, backupAccount.devices());
        logger.debug("-- snapshots() - device count: {}", devices.size());

        Map<Device, List<Snapshot>> snapshots = backupAssistant.snapshotsForDevices(httpClient, devices);
        logger.debug("-- snapshots() - snapshot count: {}", snapshots.values().size());

        return snapshots;
    }

    public void download(
            HttpClient httpClient,
            Map<Device, ? extends Collection<Snapshot>> snapshots,
            Predicate<Assets> assetsFilter,
            Predicate<Asset> assetFilter
    ) throws IOException {

        // TODO rework once we have UncheckedIOExceptions
        for (Map.Entry<Device, ? extends Collection<Snapshot>> deviceSnapshot : snapshots.entrySet()) {
            Device device = deviceSnapshot.getKey();

            for (Snapshot snapshot : deviceSnapshot.getValue()) {
                download(httpClient, device, snapshot, assetsFilter, assetFilter);
            }
        }
    }

    public void download(
            HttpClient httpClient,
            Device device,
            Snapshot snapshot,
            Predicate<Assets> assetsFilter,
            Predicate<Asset> assetFilter
    ) throws IOException {

        // Asset list.
        List<Assets> assetsList = backupAssistant.assetsList(httpClient, snapshot);
        logger.info("-- download() - assets count: {}", assetsList.size());

        // Domain filter
        List<String> files = Assets.files(assetsList, assetsFilter);
        logger.info("-- download() - domain filtered file count: {}", files.size());

        // Output folders.
        Path relativePath = deviceSnapshotDateSubPath(device, snapshot);
        logger.info("-- download() - snapshot relative path: {}", relativePath);

        // Filename extension filter.
        // Batch process files in groups of 100.
        // TODO group files into batches based on file size.
        List<List<String>> batches = ListUtils.partition(files, 100);

        for (List<String> batch : batches) {
            List<Asset> assets = backupAssistant.assets(httpClient, batch)
                    .stream()
                    .filter(assetFilter::test)
                    .collect(Collectors.toList());
            logger.info("-- download() - filtered asset count: {}", assets.size());
            downloadAssistant.download(httpClient, assets, relativePath);
        }
    }

    public Path deviceSnapshotDateSubPath(Device device, Snapshot snapshot) {
        Map<String, Instant> snapshotTimestamp = device.snapshots()
                .stream()
                .collect(Collectors.toMap(
                        SnapshotID::id,
                        SnapshotID::timestamp,
                        (a, b) -> {
                            logger.warn("-- deviceSnapshotDateSubPath() - collsion: {} {}", a, b);
                            return a;
                        }));

        if (!snapshotTimestamp.containsKey(snapshot.name())) {
            logger.warn("-- deviceSnapshotDateSubPath() - snapshot not found in device: {} {}", snapshot.name(), snapshotTimestamp);
        }

        Instant timestamp = snapshotTimestamp.containsKey(snapshot.name())
                ? snapshotTimestamp.get(snapshot.name())
                : snapshot.modification();
        LocalDateTime ldt = LocalDateTime.ofInstant(timestamp, ZoneId.of("UTC"));
        String date = DateTimeFormatter.BASIC_ISO_DATE.format(ldt);

        return Paths.get(device.uuid()).resolve(date);
    }

    public void printDomainList(HttpClient httpClient, Map<Device, ? extends Collection<Snapshot>> snapshots)
            throws IOException {
        // TODO rework once we have UncheckedIOExceptions
        for (Map.Entry<Device, ? extends Collection<Snapshot>> deviceSnapshot : snapshots.entrySet()) {
            Device device = deviceSnapshot.getKey();
            for (Snapshot snapshot : deviceSnapshot.getValue()) {
                printDomainList(httpClient, device, snapshot);
            }
        }
    }

    public void printDomainList(HttpClient httpClient, Device device, Snapshot snapshot)
            throws IOException {
        // Asset list.
        List<Assets> assetsList = backupAssistant.assetsList(httpClient, snapshot);
        logger.info("-- printDomainList() - assets count: {}", assetsList.size());

        // Output domains --domains option
        System.out.println("Device: " + device.info());
        System.out.println("Snapshot: " + snapshot.info());
        System.out.println("Domains / file count:");

        assetsList.stream()
                .map(a -> a.domain() + " / " + a.files().size())
                .sorted()
                .forEach(System.out::println);
    }
}
