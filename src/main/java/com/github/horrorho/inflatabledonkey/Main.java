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
import com.github.horrorho.inflatabledonkey.args.PropertyItemType;
import com.github.horrorho.inflatabledonkey.args.PropertyLoader;
import com.github.horrorho.inflatabledonkey.args.filter.ArgsSelector;
import com.github.horrorho.inflatabledonkey.args.filter.AssetFilter;
import com.github.horrorho.inflatabledonkey.args.filter.AssetsFilter;
import com.github.horrorho.inflatabledonkey.args.filter.SnapshotFilter;
import com.github.horrorho.inflatabledonkey.args.filter.UserSelector;
import com.github.horrorho.inflatabledonkey.cache.FileCache;
import com.github.horrorho.inflatabledonkey.cache.InflatableData;
import com.github.horrorho.inflatabledonkey.chunk.engine.ChunkClient;
import com.github.horrorho.inflatabledonkey.chunk.store.ChunkDigest;
import com.github.horrorho.inflatabledonkey.chunk.store.ChunkDigests;
import com.github.horrorho.inflatabledonkey.chunk.store.disk.DiskChunkStore;
import com.github.horrorho.inflatabledonkey.cloud.Donkey;
import com.github.horrorho.inflatabledonkey.cloud.accounts.Account;
import com.github.horrorho.inflatabledonkey.cloud.accounts.Accounts;
import com.github.horrorho.inflatabledonkey.cloud.auth.Auth;
import com.github.horrorho.inflatabledonkey.cloud.auth.Authenticator;
import com.github.horrorho.inflatabledonkey.cloud.escrow.EscrowedKeys;
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.data.backup.Assets;
import com.github.horrorho.inflatabledonkey.data.backup.Device;
import com.github.horrorho.inflatabledonkey.data.backup.Snapshot;
import com.github.horrorho.inflatabledonkey.data.der.DERUtils;
import com.github.horrorho.inflatabledonkey.data.der.KeySet;
import com.github.horrorho.inflatabledonkey.pcs.service.ServiceKeySet;
import com.github.horrorho.inflatabledonkey.pcs.service.ServiceKeySetBuilder;
import com.github.horrorho.inflatabledonkey.util.BatchSetIterator;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.function.Predicate;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
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
        } catch (IllegalStateException ex) {
            System.out.println("Configuration error: " + ex.getMessage());
            System.exit(-2);
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
                .setUserAgent("CloudKit/480 (13A405)")
                .useSystemProperties()
                .build();

        // TODO manage
        int threads = Property.ENGINE_THREADS.asInteger().orElse(1);
        ForkJoinPool forkJoinPool = new ForkJoinPool(threads);

        // Hack to improve the speed of multiple small asset retrieval by providing a larger auxillary threadpool for
        // them.
        int auxThreads = Property.ENGINE_FRAGMENTATION_POOL_MULTIPLIER.asInteger().orElse(0) * threads;
        Optional<ForkJoinPool> forkJoinPoolAux = auxThreads == 0
                ? Optional.empty()
                : Optional.of(new ForkJoinPool(auxThreads));
        Integer fragmentationThreshold = Property.ENGINE_FRAGMENTATION_THRESHOLD.asInteger().orElse(64);
        logger.info("-- main() - ForkJoinPool parallelism: {}", forkJoinPool.getParallelism());
        logger.info("-- main() - ForkJoinPool aux: {}", forkJoinPoolAux.map(ForkJoinPool::getParallelism));
        logger.info("-- main() - ForkJoinPool fragmentation threshold: {}", fragmentationThreshold);

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

        // Output folders.
        Path outputFolder = Paths.get(Property.OUTPUT_FOLDER.value().orElse("backups")).normalize()
                .resolve(account.accountInfo().appleId());
        Path assetOutputFolder = outputFolder;
        Path chunkOutputFolder = outputFolder.resolve("cache"); // TOFIX from Property normalize()
        Path cachedData = chunkOutputFolder.resolve("data.enc");
        Path tempOutputFolder = outputFolder.resolve("temp"); // TOFIX from Property normalize()
        logger.info("-- main() - output folder backups: {}", assetOutputFolder.toAbsolutePath());
        logger.info("-- main() - output folder chunk cache: {}", chunkOutputFolder.toAbsolutePath());
        logger.info("-- main() - cached data: {}", cachedData.toAbsolutePath());
        System.out.println("\nOutput folder: " + assetOutputFolder.toAbsolutePath());

        // Cached data if available
        byte[] bs = Base64.getDecoder().decode(auth.mmeAuthToken());
        byte[] cachedPassword = Arrays.copyOfRange(bs, bs.length - 16, bs.length);
        FileCache cache = FileCache.defaultInstance();
        InflatableData data = cache.load(cachedData, cachedPassword)
                .flatMap(InflatableData::from)
                .orElseGet(() -> {
                    logger.info("-- main() - no cached data available");
                    return InflatableData.generate();
                });

        // Escrowed keys
        ServiceKeySet escrowServiceKeySet = null;
        if (data.hasEscrowedKeys()) {
            escrowServiceKeySet = DERUtils.parse(data.escrowedKeys(), KeySet::new)
                    .flatMap(ServiceKeySetBuilder::build)
                    .orElseGet(() -> {
                        logger.info("-- main() - failed to decrypt cached escrowed keys");
                        return null;
                    });
        }
        try {
            if (escrowServiceKeySet == null) {
                byte[] escrowedKeys = EscrowedKeys.data(httpClient, account);
                data.setEscrowedKeys(escrowedKeys);
                escrowServiceKeySet = DERUtils.parse(data.escrowedKeys(), KeySet::new)
                        .flatMap(ServiceKeySetBuilder::build)
                        .orElseThrow(() -> new IllegalStateException("failed to decrypt escrowed keys"));
                cache.store(cachedData, cachedPassword, data.encoded());
            }
        } catch (IllegalArgumentException ex) {
            logger.warn("-- main() - exception : {}", ex);
            System.out.println(ex.getLocalizedMessage());
            System.exit(-1);
        }

        logger.info("-- main() - device -> hardware id: {} uuid: {}", data.deviceHardWareId(), data.deviceUuid());

        // Backup
        BackupAssistant assistant
                = BackupAssistant.create(httpClient, forkJoinPool, account, escrowServiceKeySet, data.deviceUuid(), data.deviceHardWareId());

        // TODO automatic decrypt mode
        Property.DP_MODE.value().ifPresent(u -> logger.info("-- main() - decrypt mode override: {}", u));

        // Download tools.
        DiskChunkStore chunkStore = new DiskChunkStore(ChunkDigest::new, ChunkDigests::test, chunkOutputFolder, tempOutputFolder);
        KeyBagManager keyBagManager = assistant.newKeyBagManager();

        ChunkClient chunkClient = ChunkClient.defaults();

        Donkey donkey = new Donkey(chunkClient, chunkStore, fragmentationThreshold);
        int batchThreshold = Property.ENGINE_BATCH_THRESHOLD.asInteger().orElse(1048576);

        Function<Set<Asset>, List<Set<Asset>>> batchFunction
                = u -> BatchSetIterator.batchedSetList(u, a -> a.size().map(Long::intValue).orElse(0), batchThreshold);
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

        // Print snapshots option.
        if (Property.PRINT_SNAPSHOTS.asBoolean().orElse(false)) {
            print(deviceSnapshots);
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

        // Print domain list option.
        if (Property.PRINT_DOMAIN_LIST.asBoolean().orElse(false)) {
            backup.printDomainList(httpClient, filtered);
            return;
        }

        Predicate<Assets> assetsFilter = new AssetsFilter(Property.FILTER_ASSET_DOMAIN.asList());

        // For simplicity we just pass in item types as relative paths, although it could give us superfluous matches.
        List<String> filterItemTypes = Property.FILTER_ASSET_ITEM_TYPE.asList().orElse(Collections.emptyList())
                .stream()
                .map(PropertyItemType::valueOf)
                .map(PropertyItemType::extensions)
                .flatMap(Collection::stream)
                .collect(toList());
        List<String> filterRelativePathList = Property.FILTER_ASSET_RELATIVE_PATH.asList().orElse(Collections.emptyList());
        List<String> filterRelativePaths = Stream.of(filterItemTypes, filterRelativePathList)
                .flatMap(Collection::stream)
                .collect(toList());
        logger.info("-- main() - asset filter relative paths: {}", filterRelativePaths);

        List<String> filterExtensions = Property.FILTER_ASSET_EXTENSION.asList().orElse(Collections.emptyList());
        logger.info("-- main() - asset filter extensions: {}", filterExtensions);

        Predicate<Asset> assetFilter = new AssetFilter(
                Property.FILTER_ASSET_BIRTH_MAX.asLong(),
                Property.FILTER_ASSET_BIRTH_MIN.asLong(),
                filterExtensions,
                filterRelativePaths,
                Property.FILTER_ASSET_SIZE_MAX.asInteger(),
                Property.FILTER_ASSET_SIZE_MIN.asInteger(),
                Property.FILTER_ASSET_STATUS_CHANGED_MAX.asLong(),
                Property.FILTER_ASSET_STATUS_CHANGED_MIN.asLong());

        Optional<Long> snapshotDateMin = Stream.of(Property.FILTER_ASSET_BIRTH_MIN, Property.FILTER_ASSET_STATUS_CHANGED_MIN)
                .map(Property::asLong)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
        logger.info("-- main() - snapshot date min: {}", snapshotDateMin);

        // Can probably optimize further with snapshot date max to skip type 1 backups.
        Predicate<Snapshot> snapshotFilter = new SnapshotFilter(snapshotDateMin);

        backup.download(httpClient, filtered, snapshotFilter, assetsFilter, assetFilter);
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
// TODO reconstruct empty files/ empty directories
