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

import com.github.horrorho.inflatabledonkey.args.filter.AssetFilter;
import com.github.horrorho.inflatabledonkey.args.filter.AssetsFilter;
import com.github.horrorho.inflatabledonkey.args.Property;
import com.github.horrorho.inflatabledonkey.args.PropertyLoader;
import com.github.horrorho.inflatabledonkey.args.filter.ArgsSelector;
import com.github.horrorho.inflatabledonkey.args.filter.UserSelector;
import com.github.horrorho.inflatabledonkey.chunk.engine.ChunkClient;
import com.github.horrorho.inflatabledonkey.chunk.store.ChunkDigest;
import com.github.horrorho.inflatabledonkey.chunk.store.ChunkDigests;
import com.github.horrorho.inflatabledonkey.chunk.store.disk.DiskChunkStore;
import com.github.horrorho.inflatabledonkey.cloud.AuthorizeAssetsClient;
import com.github.horrorho.inflatabledonkey.cloud.BatchSetIterator;
import com.github.horrorho.inflatabledonkey.cloud.Donkey;
import com.github.horrorho.inflatabledonkey.cloud.accounts.Account;
import com.github.horrorho.inflatabledonkey.cloud.accounts.Accounts;
import com.github.horrorho.inflatabledonkey.cloud.auth.Auth;
import com.github.horrorho.inflatabledonkey.cloud.auth.Authenticator;
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.data.backup.Assets;
import com.github.horrorho.inflatabledonkey.data.backup.Device;
import com.github.horrorho.inflatabledonkey.data.backup.Snapshot;
import com.github.horrorho.inflatabledonkey.file.FileAssembler;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.function.Predicate;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
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
        System.out.println("    --mode CBC  OR  --mode XTS");
        // SystemDefault HttpClient.
        // TODO close
//        CloseableHttpClient httpClient = HttpClients.custom()
//                .setUserAgent("CloudKit/479 (13A404)")
//                .setRedirectStrategy(new LaxRedirectStrategy())
//                .useSystemProperties()
//                .build();
        int maxConnections = Property.HTTP_CLIENT_CONNECTIONS_MAX_TOTAL.asInteger().orElse(256);
        int maxConnectionsPerRoute = Property.HTTP_CLIENT_CONNECTIONS_MAX_PER_ROUTE.asInteger().orElse(32);
        int timeoutMS = Property.HTTP_CLIENT_TIMEOUT_MS.asInteger().orElse(60000);

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setDefaultMaxPerRoute(maxConnectionsPerRoute);
        connManager.setMaxTotal(maxConnections);
        connManager.setValidateAfterInactivity(timeoutMS);

        DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(3, true);

        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(timeoutMS)
                .setConnectTimeout(timeoutMS)
                .setSocketTimeout(timeoutMS)
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(config)
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setRetryHandler(retryHandler)
                .setUserAgent("CloudKit/479 (13A404)")
                .useSystemProperties()
                .build();

        // TODO manage
        ForkJoinPool forkJoinPool = Property.THREADS.asInteger().map(ForkJoinPool::new)
                .orElseGet(() -> new ForkJoinPool(1));
        ForkJoinPool forkJoinPoolAux = new ForkJoinPool(12);

        logger.info("-- main() - ForkJoinPool parallelism: {}", forkJoinPool.getParallelism());

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
        BackupAssistant assistant = BackupAssistant.create(httpClient, forkJoinPool, account);

        // Output folders.
        Path outputFolder = Paths.get(Property.OUTPUT_FOLDER.value().orElse("backups")).normalize()
                .resolve(account.accountInfo().appleId());
        Path assetOutputFolder = outputFolder;
        Path chunkOutputFolder = outputFolder.resolve("cache"); // TOFIX from Property normalize()
        Path tempOutputFolder = outputFolder.resolve("temp"); // TOFIX from Property normalize()
        logger.info("-- main() - output folder backups: {}", assetOutputFolder.toAbsolutePath());
        logger.info("-- main() - output folder chunk cache: {}", chunkOutputFolder.toAbsolutePath());
        System.out.println("\nOutput folder: " + assetOutputFolder.toAbsolutePath());

        // TODO automatic decrypt mode
        Property.DP_MODE.value().ifPresent(u -> logger.info("-- main() - decrypt mode override: {}", u));

        // Download tools.
        DiskChunkStore chunkStore = new DiskChunkStore(ChunkDigest::new, ChunkDigests::test, chunkOutputFolder, tempOutputFolder);
        KeyBagManager keyBagManager = assistant.newKeyBagManager();

        AuthorizeAssetsClient authorizeAssetsClient = AuthorizeAssetsClient.backupd();
        ChunkClient chunkClient = ChunkClient.defaultInstance();

        Donkey donkey = new Donkey(chunkClient, chunkStore);
        int batchThreshold = Property.ENGINE_BATCH_THRESHOLD.asInteger().orElse(1048576);
        Function<Set<Asset>, List<Set<Asset>>> batchFunction
                = u -> BatchSetIterator.batchedSetList(u, a -> a.size().orElse(0), batchThreshold);
        DownloadAssistant downloadAssistant
                = new DownloadAssistant(batchFunction, keyBagManager, forkJoinPool, forkJoinPoolAux, donkey, outputFolder);
        Backup backup = new Backup(assistant, downloadAssistant);

        // Retrieve snapshots.
        Map<Device, List<Snapshot>> deviceSnapshots = backup.snapshots(httpClient);

        System.out.println("");
        if (deviceSnapshots.isEmpty()) {
            System.out.println("No iOS 9+ backups found.");
            return;
        }

        // Filter snapshots.
        Optional<List<String>> filterDevices = Property.FILTER_DEVICE.asList();
        Optional<List<Integer>> filterSnapshots = Property.FILTER_SNAPSHOT.asList(Integer::parseInt);
        final Map<Device, List<Snapshot>> filtered;
        if (filterDevices.isPresent() || filterSnapshots.isPresent()) {
            System.out.println("Available:");
            print(deviceSnapshots);

            String cliFilters = "\nFilter -> ";
            String cliFilterDevices = Property.FILTER_DEVICE.value().orElse("");
            if (cliFilterDevices.isEmpty()) {
                cliFilterDevices = "ALL";
            }
            cliFilters += " devices: " + cliFilterDevices;

            cliFilters += Property.FILTER_SNAPSHOT.value().map(u -> " snapshots: " + u).orElse("");
            System.out.println(cliFilters);
            filtered = new ArgsSelector(filterDevices.orElseGet(Collections::emptyList), filterSnapshots.orElseGet(Collections::emptyList))
                    .apply(deviceSnapshots);
        } else {
            filtered = UserSelector.instance()
                    .apply(deviceSnapshots);
        }

        filtered.forEach((device, snapshots) -> {
            logger.info("-- main() - selected device: {}", device.info());
            snapshots.forEach(snapshot -> logger.info("-- main() - selected snapshot: {} backupType: {}",
                    snapshot.info(), snapshot.backupType()));
        });

        if (filtered.isEmpty()) {
            System.out.println("\nNothing selected.");
            return;
        }

        System.out.println("\nSelected:");
        print(filtered);
        System.out.println("");

        // Print snapshots option.
        if (Property.PRINT_SNAPSHOTS.asBoolean().orElse(false)) {
            return;
        }

        // Print domain list option.
        if (Property.PRINT_DOMAIN_LIST.asBoolean().orElse(false)) {
            backup.printDomainList(httpClient, filtered);
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

    static void print(Map<Device, List<Snapshot>> deviceSnapshot) {
        deviceSnapshot.entrySet()
                .stream()
                .map(u -> {
                    System.out.println("DEVICE: " + u.getKey().info());
                    return u.getValue();
                })
                .flatMap(Collection::stream)
                .forEach(u -> System.out.println("  SNAPSHOT: " + u.info()));
    }
}

// TODO filter AssetID size rather than wait to download Asset and then filter.
// TODO 0xFF System protectionInfo DONE
// TODO file timestamps DONE
// TODO date filtering DONE
// TODO size filtering DONE
// TODO time expired tokens / badly adjusted system clocks. DONE
// TODO handle D in files DONE
// TODO reconstruct empty files/ empty directories
// TODO file timestamp DONE
// TODO filtering DONE
// TODO concurrent downloads DONE
// TODO file asset cache
// TODO 5000 limit? FIXED
