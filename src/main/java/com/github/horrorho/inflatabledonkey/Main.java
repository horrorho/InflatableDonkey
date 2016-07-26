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
import com.github.horrorho.inflatabledonkey.chunk.store.disk.DiskChunkStore;
import com.github.horrorho.inflatabledonkey.cloud.AssetDownloader;
import com.github.horrorho.inflatabledonkey.cloud.AuthorizeAssets;
import com.github.horrorho.inflatabledonkey.cloud.accounts.Account;
import com.github.horrorho.inflatabledonkey.cloud.accounts.Accounts;
import com.github.horrorho.inflatabledonkey.cloud.auth.Auth;
import com.github.horrorho.inflatabledonkey.cloud.auth.Authenticator;
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.data.backup.Assets;
import com.github.horrorho.inflatabledonkey.data.backup.Device;
import com.github.horrorho.inflatabledonkey.data.backup.Snapshot;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Predicate;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
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
        // MUST initialize Property with args before we use it, otherwise it will throw an IllegalStateException.
        try {
            if (!PropertyLoader.instance().test(args)) {
                return;
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("Argument error: " + ex.getMessage());
            System.out.println("Try '" + Property.APP_NAME.value().orElse("") + " --help' for more information.");
            System.exit(-1);
        }

        Arrays.asList(Property.values())
                .forEach(u -> logger.info("-- main() - {} = {}", u.name(), u.value()));

        // INFO
        System.out.println("NOTE! Experimental Data Protection class mode detection.");
        System.out.println("If you have file corruption issues please try setting the mode manually:");
        System.out.println("    --mode CBC");
        System.out.println("or");
        System.out.println("    --mode XTS");
        System.out.println("");

        // SystemDefault HttpClient.
        // TODO concurrent, close
//        CloseableHttpClient httpClient = HttpClients.custom()
//                .setUserAgent("CloudKit/479 (13A404)")
//                .setRedirectStrategy(new LaxRedirectStrategy())
//                .useSystemProperties()
//                .build();
        int maxConnections = Property.HTTP_CLIENT_CONNECTIONS_MAX.asInteger().orElse(32);
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(maxConnections);
        connManager.setDefaultMaxPerRoute(maxConnections);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setUserAgent("CloudKit/479 (13A404)")
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setConnectionManager(connManager)
                .useSystemProperties()
                .build();

        // TODO manage
        Optional<ForkJoinPool> forkJoinPool = Property.THREADS.asInteger().map(ForkJoinPool::new);
        logger.info("-- main() - ForkJoinPool parallelism: {}", forkJoinPool.map(ForkJoinPool::getParallelism));

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

        if (Property.ARGS_TOKEN.asBoolean().orElse(false)) {
            System.out.println("DsPrsID:mmeAuthToken " + auth.dsPrsID() + ":" + auth.mmeAuthToken());
            return;
        }

        // Account
        Account account = Accounts.account(httpClient, auth);

        // Backup
        BackupAssistant assistant = BackupAssistant.create(httpClient, account);

        // Output folders.
        Path outputFolder = Paths.get(Property.OUTPUT_FOLDER.value().orElse("backups"))
                .resolve(account.accountInfo().appleId());
        Path assetOutputFolder = outputFolder;
        Path chunkOutputFolder = outputFolder.resolve("cache"); // TOFIX from Property
        Path tempOutputFolder = outputFolder.resolve("temp"); // TOFIX from Property
        logger.info("-- main() - output folder backups: {}", assetOutputFolder.toAbsolutePath());
        logger.info("-- main() - output folder chunk cache: {}", chunkOutputFolder.toAbsolutePath());
        System.out.println("Output folder: " + assetOutputFolder.toAbsolutePath());

        // TODO automatic decrypt mode
        Property.DP_MODE.value().ifPresent(u -> logger.info("-- main() - decrypt mode override: {}", u));

        // Download tools.
        AuthorizeAssets authorizeAssets = AuthorizeAssets.backupd();
        DiskChunkStore chunkStore = new DiskChunkStore(chunkOutputFolder, tempOutputFolder);
        AssetDownloader assetDownloader = new AssetDownloader(chunkStore);
        KeyBagManager keyBagManager = assistant.newKeyBagManager();

        DownloadAssistant downloadAssistant
                = new DownloadAssistant(authorizeAssets, assetDownloader, keyBagManager, outputFolder);
        Backup backup = new Backup(assistant, downloadAssistant, forkJoinPool);

        // Retrieve snapshots.
        Map<Device, List<Snapshot>> deviceSnapshots = backup.snapshots(httpClient);

        if (deviceSnapshots.isEmpty()) {
            System.out.println("No devices/ snapshots.");
            return;
        }

        // Filter snapshots.
        Map<Device, ? extends Collection<Snapshot>> filtered
                = XFilter.apply(Property.FILTER_DEVICE.asList(), Property.FILTER_SNAPSHOT.asList(Integer::parseInt))
                .apply(deviceSnapshots);

        filtered.forEach((device, snapshots) -> {
            logger.info("-- main() - selected device: {}", device.info());
            snapshots.forEach(snapshot -> logger.info("-- main() - selected snapshot: {} backupType: {}",
                    snapshot.info(), snapshot.backupType()));
        });

        if (filtered.isEmpty()) {
            System.out.println("Nothing selected.");
            System.exit(0);
        }

        System.out.println("\nSelection:");
        filtered.entrySet()
                .stream()
                .map(u -> {
                    System.out.println("DEVICE - " + u.getKey().info());
                    return u.getValue();
                })
                .flatMap(Collection::stream)
                .map(Snapshot::info)
                .forEach(u -> System.out.println("  SNAPSHOT - " + u));

        // Print snapshots option.
        if (Property.PRINT_SNAPSHOTS.asBoolean().orElse(false)) {
            return;
        }

        // Print domain list option.
        if (Property.PRINT_DOMAIN_LIST.asBoolean().orElse(false)) {
            backup.printDomainList(httpClient, deviceSnapshots);
            return;
        }

        Predicate<Assets> assetsFilter = new AssetsFilter(Property.FILTER_ASSET_DOMAIN.asList());

        Predicate<Asset> assetFilter = new AssetFilter(
                Property.FILTER_ASSET_BIRTH_MAX.asLong(),
                Property.FILTER_ASSET_BIRTH_MIN.asLong(),
                Property.FILTER_ASSET_EXTENSION.asList(),
                Property.FILTER_ASSET_RELATIVE_PATH.asList(),
                Property.FILTER_ASSET_SIZE_MAX.asInteger(),
                Property.FILTER_ASSET_SIZE_MIN.asInteger(),
                Property.FILTER_ASSET_STATUS_CHANGED_MAX.asLong(),
                Property.FILTER_ASSET_STATUS_CHANGED_MIN.asLong());
        backup.download(httpClient, filtered, assetsFilter, assetFilter);
    }
}

// TODO 0xFF System protectionInfo DONE
// TODO file timestamps DONE
// TODO date filtering DONE
// TODO size filtering DONE
// TODO time expired tokens / badly adjusted system clocks.
// TODO handle D in files DONE
// TODO reconstruct empty files/ empty directories
// TODO file timestamp DONE
// TODO filtering DONE
// TODO concurrent downloads DONE
// TODO file asset cache
// TODO 5000 limit? FIXED
