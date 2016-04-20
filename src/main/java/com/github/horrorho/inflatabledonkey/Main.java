/* 
 * The MIT License
 *
 * Copyright 2015 Ahseya.
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

import com.github.horrorho.inflatabledonkey.args.Property;
import com.github.horrorho.inflatabledonkey.args.PropertyLoader;
import com.github.horrorho.inflatabledonkey.chunk.engine.standard.StandardChunkEngine;
import com.github.horrorho.inflatabledonkey.chunk.store.disk.DiskChunkStore;
import com.github.horrorho.inflatabledonkey.cloud.AssetDownloader;
import com.github.horrorho.inflatabledonkey.cloud.AuthorizeAssets;
import com.github.horrorho.inflatabledonkey.cloud.accounts.Account;
import com.github.horrorho.inflatabledonkey.cloud.accounts.Accounts;
import com.github.horrorho.inflatabledonkey.cloud.auth.Auth;
import com.github.horrorho.inflatabledonkey.cloud.auth.Authenticator;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * InflatableDonkey.
 *
 * @author Ahseya
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * @param args the command line arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        try {
            if (!PropertyLoader.instance().test(args)) {
                return;
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("Argument error: " + ex.getMessage());
            System.out.println("Try '" + Property.APP_NAME.value() + " --help' for more information.");
            System.exit(-1);
        }

        // SystemDefault HttpClient.
        // TODO concurrent
        CloseableHttpClient httpClient = HttpClients.custom()
                .setUserAgent("CloudKit/479 (13A404)")
                .useSystemProperties()
                .build();

        // Auth
        // TODO rework when we have UncheckedIOException for Authenticator
        Auth auth = Property.AUTHENTICATION_TOKEN.value()
                .map(Auth::new)
                .orElse(null);

        if (auth == null) {
            auth = Authenticator.authenticate(
                    httpClient,
                    Property.AUTHENTICATION_APPLEID.value().get(),
                    Property.AUTHENTICATION_PASSWORD.value().get());
        }
        logger.debug("-- main() - auth: {}", auth);
        logger.info("-- main() - dsPrsID:mmeAuthToken: {}:{}", auth.dsPrsID(), auth.mmeAuthToken());

        if (Property.ARGS_TOKEN.booleanValue().orElse(false)) {
            System.out.println("DsPrsID:mmeAuthToken " + auth.dsPrsID() + ":" + auth.mmeAuthToken());
            return;
        }

        logger.info("-- main() - Apple ID: {}", Property.AUTHENTICATION_APPLEID.value());
        logger.info("-- main() - password: {}", Property.AUTHENTICATION_PASSWORD.value());
        logger.info("-- main() - token: {}", Property.AUTHENTICATION_TOKEN.value());

        // Account
        Account account = Accounts.account(httpClient, auth);

        // Backup
        BackupAssistant backup = BackupAssistant.create(httpClient, account);

        // BackupAccount
        BackupAccount backupAccount = backup.backupAccount(httpClient);
        logger.debug("-- main() - backup account: {}", backupAccount);

        // Devices
        List<Device> devices = backup.devices(httpClient, backupAccount.devices());
        logger.debug("-- main() - device count: {}", devices.size());

        // Snapshots
        List<SnapshotID> snapshotIDs = devices.stream()
                .map(Device::snapshots)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        logger.info("-- main() - total snapshot count: {}", snapshotIDs.size());

        Map<String, Snapshot> snapshots = backup.snapshot(httpClient, snapshotIDs)
                .stream()
                .collect(Collectors.toMap(
                        s -> s.record()
                        .getRecordIdentifier()
                        .getValue()
                        .getName(),
                        Function.identity()));

        // Selection
        int deviceIndex = Property.SELECT_DEVICE_INDEX.intValue().get();
        int snapshotIndex = Property.SELECT_SNAPSHOT_INDEX.intValue().get();
        logger.info("-- main() - arg device index: {}", deviceIndex);
        logger.info("-- main() - arg snapshot index: {}", snapshotIndex);

        for (int i = 0; i < devices.size(); i++) {
            Device device = devices.get(i);
            List<SnapshotID> deviceSnapshotIDs = device.snapshots();

            System.out.println(i + " " + device.info());

            for (int j = 0; j < deviceSnapshotIDs.size(); j++) {
                SnapshotID sid = deviceSnapshotIDs.get(j);
                System.out.println("\t" + j + snapshots.get(sid.id()).info() + "   " + sid.timestamp());
            }
        }
        if (Property.PRINT_SNAPSHOTS.booleanValue().orElse(false)) {
            return;
        }

        if (deviceIndex >= devices.size()) {
            System.out.println("No such device: " + deviceIndex);
        }
        Device device = devices.get(deviceIndex);
        System.out.println("Selected device: " + deviceIndex + ", " + device.info());

        if (snapshotIndex >= devices.get(deviceIndex).snapshots().size()) {
            System.out.println("No such snapshot for selected device: " + snapshotIndex);
        }

        String selected = devices.get(deviceIndex).snapshots().get(snapshotIndex).id();
        Snapshot snapshot = snapshots.get(selected);
        System.out.println("Selected snapshot: " + snapshotIndex + ", " + snapshot.info());

        // Asset list.
        List<Assets> assetsList = backup.assetsList(httpClient, snapshot);
        logger.info("-- main() - assets count: {}", assetsList.size());

        // Output domains --domains option
        if (Property.PRINT_DOMAIN_LIST.booleanValue().orElse(false)) {
            System.out.println("Domains / file count:");
            assetsList.stream()
                    .filter(a -> a.domain().isPresent())
                    .map(a -> a.domain().get() + " / " + a.files().size())
                    .sorted()
                    .forEach(System.out::println);
            return;
            // TODO check Assets without domain information.
        }

        // Domains filter --domain option
        List<String> domainSubstringFilter = Property.FILTER_DOMAIN.list().orElse(Collections.emptyList())
                .stream()
                .map(s -> s.toLowerCase(Locale.US))
                .collect(Collectors.toList());
        logger.info("-- main() - arg domain filters: {}", domainSubstringFilter);

        Predicate<Optional<String>> domainFilter = domain -> domain
                .map(d -> d.toLowerCase(Locale.US))
                .map(d -> domainSubstringFilter
                        .stream()
                        .anyMatch(s -> d.contains(s)))
                .orElse(false);
        List<String> files = Assets.files(assetsList, domainFilter);
        logger.info("-- main() - domain filtered file count: {}", files.size());

        // Output folders.
        Path outputFolder = Paths.get(Property.OUTPUT_FOLDER.value().orElse("output"));
        Path assetOutputFolder = outputFolder.resolve("assets"); // TODO assets value injection
        Path chunkOutputFolder = outputFolder.resolve("chunks"); // TODO chunks value injection
        logger.info("-- main() - output folder chunks: {}", chunkOutputFolder);
        logger.info("-- main() - output folder assets: {}", assetOutputFolder);

        // Download tools.
        AuthorizeAssets authorizeAssets = AuthorizeAssets.backupd();
        DiskChunkStore chunkStore = new DiskChunkStore(chunkOutputFolder);
        StandardChunkEngine chunkEngine = new StandardChunkEngine(chunkStore);
        AssetDownloader assetDownloader = new AssetDownloader(chunkEngine);
        KeyBagManager keyBagManager = backup.newKeyBagManager();

        // Mystery Moo. 
        Moo moo = new Moo(authorizeAssets, assetDownloader, keyBagManager);

        // Filename extension filter.
        List<String> extensionFilterList = Property.FILTER_EXTENSION.list().orElse(Collections.emptyList())
                .stream()
                .map(s -> s.toLowerCase(Locale.US))
                .collect(Collectors.toList());
        logger.info("-- main() - arg filename extension filters: {}", extensionFilterList);
        Predicate<Asset> assetFilter = asset -> asset
                .relativePath()
                .map(d -> d.toLowerCase(Locale.US))
                .map(d -> extensionFilterList
                        .stream()
                        .anyMatch(s -> d.endsWith(s)))
                .orElse(false);

        // Batch process files in groups of 100.
        // TODO group files into batches based on file size.
        List<List<String>> batches = ListUtils.partition(files, 100);

        for (List<String> batch : batches) {
            List<Asset> assets = backup.assets(httpClient, batch)
                    .stream()
                    .filter(assetFilter::test)
                    .collect(Collectors.toList());
            logger.info("-- main() - filtered asset count: {}", assets.size());
            moo.download(httpClient, assets, assetOutputFolder);
        }
    }
}
// TODO multiple domain/ extension filtering arguments
// TODO complete device/ snapshot backup if device/ snapshot argument is in not present
// TODO file timestamps
// TODO date filtering
// TODO size filtering
// TODO time expired tokens / badly adjusted system clocks.
// TODO handle D in files
// TODO reconstruct empty files/ empty directories
// TODO file timestamp
// TODO filtering
// TODO concurrent downloads
// TODO file asset cache
