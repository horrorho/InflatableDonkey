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

import com.github.horrorho.inflatabledonkey.args.ArgsParsers;
import com.github.horrorho.inflatabledonkey.args.Help;
import com.github.horrorho.inflatabledonkey.args.OptionsFactory;
import com.github.horrorho.inflatabledonkey.args.Property;
import com.github.horrorho.inflatabledonkey.chunk.engine.standard.StandardChunkEngine;
import com.github.horrorho.inflatabledonkey.chunk.store.disk.DiskChunkStore;
import com.github.horrorho.inflatabledonkey.cloud.AssetDownloader;
import com.github.horrorho.inflatabledonkey.cloud.AssetWriter;
import com.github.horrorho.inflatabledonkey.cloud.AuthorizeAssets;
import com.github.horrorho.inflatabledonkey.cloud.AuthorizedAssets;
import com.github.horrorho.inflatabledonkey.cloud.accounts.Account;
import com.github.horrorho.inflatabledonkey.cloudkitty.CloudKitty;
import com.github.horrorho.inflatabledonkey.cloud.accounts.Accounts;
import com.github.horrorho.inflatabledonkey.cloud.auth.Auth;
import com.github.horrorho.inflatabledonkey.cloud.auth.Authenticator;
import com.github.horrorho.inflatabledonkey.cloud.cloudkit.CKInit;
import com.github.horrorho.inflatabledonkey.cloud.accounts.Token;
import com.github.horrorho.inflatabledonkey.cloud.accounts.Tokens;
import com.github.horrorho.inflatabledonkey.cloud.cloudkit.CKInits;
import com.github.horrorho.inflatabledonkey.cloud.escrow.EscrowedKeys;
import com.github.horrorho.inflatabledonkey.cloud.clients.AssetTokenClient;
import com.github.horrorho.inflatabledonkey.cloud.clients.BackupAccountClient;
import com.github.horrorho.inflatabledonkey.cloud.clients.BaseZonesClient;
import com.github.horrorho.inflatabledonkey.cloud.clients.DeviceClient;
import com.github.horrorho.inflatabledonkey.cloud.clients.ManifestsClient;
import com.github.horrorho.inflatabledonkey.cloud.clients.KeyBagClient;
import com.github.horrorho.inflatabledonkey.cloud.clients.AssetsClient;
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.data.backup.Assets;
import com.github.horrorho.inflatabledonkey.data.backup.BackupAccount;
import com.github.horrorho.inflatabledonkey.data.backup.Device;
import com.github.horrorho.inflatabledonkey.data.backup.Manifest;
import com.github.horrorho.inflatabledonkey.data.backup.Manifests;
import com.github.horrorho.inflatabledonkey.keybag.KeyBag;
import com.github.horrorho.inflatabledonkey.pcs.service.ServiceKeySet;
import com.github.horrorho.inflatabledonkey.pcs.xzone.ProtectionZone;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
        Function<String[], Map<Property, String>> argsParser = ArgsParsers.instance();
        Map<Property, String> arguments = new HashMap<>();

        try {
            arguments = argsParser.apply(args);

        } catch (IllegalArgumentException ex) {
            System.out.println("Argument error: " + ex.getMessage());
            System.out.println("Try '" + Property.APP_NAME.defaultValue() + " --help' for more information.");
            System.exit(-1);
        }

        if (arguments.containsKey(Property.ARGS_HELP)) {
            Help.instance().accept(OptionsFactory.options().keySet());
            System.exit(0);
        }

        // SystemDefault HttpClient.
        // TODO concurrent
        CloseableHttpClient httpClient = HttpClients.custom()
                .setUserAgent("CloudKit/479 (13A404)")
                .useSystemProperties()
                .build();

        Auth auth = arguments.containsKey(Property.AUTHENTICATION_TOKEN)
                ? new Auth(arguments.get(Property.AUTHENTICATION_TOKEN))
                : Authenticator.authenticate(
                        httpClient,
                        arguments.get(Property.AUTHENTICATION_APPLEID),
                        arguments.get(Property.AUTHENTICATION_PASSWORD));

        logger.debug("-- main() - auth: {}", auth);
        logger.info("-- main() - dsPrsID:mmeAuthToken: {}:{}", auth.dsPrsID(), auth.mmeAuthToken());

        if (arguments.containsKey(Property.ARGS_TOKEN)) {
            System.out.println("DsPrsID:mmeAuthToken " + auth.dsPrsID() + ":" + auth.mmeAuthToken());
            System.exit(0);
        }
        Account account = Accounts.account(httpClient, auth);
        Tokens tokens = account.tokens();

        CKInit ckInit = CKInits.ckInitBackupd(httpClient, account);
        CloudKitty kitty = CloudKitty.backupd(ckInit, tokens.get(Token.CLOUDKITTOKEN));

        ServiceKeySet escrowServiceKeySet = EscrowedKeys.keys(httpClient, account);

        ProtectionZone zoneKeys = BaseZonesClient.baseZones(httpClient, kitty, escrowServiceKeySet.keys()).get();

        BackupAccount backupAccount = BackupAccountClient.backupAccount(httpClient, kitty, zoneKeys).get();

        List<Device> devices = DeviceClient.device(httpClient, kitty, backupAccount.devices());
        logger.info("-- main() - devices: {}", devices);
        List<Manifests> manifestsList = ManifestsClient.manifestsForDevices(httpClient, kitty, zoneKeys, devices);

        logger.info("-- main() - manifests downloaded: {}", manifestsList);

        Manifest manifest = manifestsList
                .stream()
                .map(Manifests::manifests)
                .flatMap(Collection::stream)
                .filter(x -> x.count() != 0)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("no non-empty manifests"));

        List<Assets> assetsList = AssetsClient.assets(httpClient, kitty, zoneKeys, Arrays.asList(manifest));
        logger.debug("-- main() - assets: {}", assetsList);

        List<Asset> assets = AssetTokenClient.assets(httpClient, kitty, zoneKeys, assetsList);
        logger.debug("-- main() - assets: {}", assets);

        String keybagUUID = devices.get(0).currentKeybagUUID().get();
        KeyBag keyBag = KeyBagClient.keyBag(httpClient, kitty, zoneKeys, keybagUUID).get();
        logger.debug("-- main() - key bag: {}", keyBag);

        AuthorizeAssets authorizeAssets = AuthorizeAssets.backupd();

        AuthorizedAssets authorizedAssets = authorizeAssets.authorize(httpClient, assets);

        DiskChunkStore chunkStore = new DiskChunkStore(Paths.get("chunks"));
        StandardChunkEngine chunkEngine = new StandardChunkEngine(chunkStore);

        AssetWriter cloudWriter = new AssetWriter(Paths.get("testfolder"), keyBag);

        AssetDownloader moo = new AssetDownloader(chunkEngine);
        moo.get(httpClient, authorizedAssets, cloudWriter);
    }
}
// TODO time expired tokens
