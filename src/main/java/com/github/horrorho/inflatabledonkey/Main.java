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

import com.dd.plist.NSDictionary;
import com.github.horrorho.inflatabledonkey.args.ArgsParser;
import com.github.horrorho.inflatabledonkey.args.AuthenticationMapper;
import com.github.horrorho.inflatabledonkey.args.Help;
import com.github.horrorho.inflatabledonkey.args.OptionsFactory;
import com.github.horrorho.inflatabledonkey.args.Property;
import com.github.horrorho.inflatabledonkey.data.AccountInfo;
import com.github.horrorho.inflatabledonkey.data.Auth;
import com.github.horrorho.inflatabledonkey.data.Authenticator;
import com.github.horrorho.inflatabledonkey.data.CKInit;
import com.github.horrorho.inflatabledonkey.data.Tokens;
import com.github.horrorho.inflatabledonkey.protocol.CK;
import com.github.horrorho.inflatabledonkey.protocol.ProtoBufArray;
import com.github.horrorho.inflatabledonkey.requests.AccountSettingsRequestFactory;
import com.github.horrorho.inflatabledonkey.requests.CkAppInitBackupRequestFactory;
import com.github.horrorho.inflatabledonkey.requests.Headers;
import com.github.horrorho.inflatabledonkey.requests.M201RequestFactory;
import com.github.horrorho.inflatabledonkey.requests.M211RequestFactory;
import com.github.horrorho.inflatabledonkey.requests.MappedHeaders;
import com.github.horrorho.inflatabledonkey.requests.PostMessageFactory;
import com.github.horrorho.inflatabledonkey.responsehandler.InputStreamResponseHandler;
import com.github.horrorho.inflatabledonkey.responsehandler.JsonResponseHandler;
import com.github.horrorho.inflatabledonkey.responsehandler.PropertyListResponseHandler;
import com.github.horrorho.inflatabledonkey.util.PLists;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * iOS9 backup retrieval proof of concept - work in progress. Development tool intended to demonstrate a functional
 * chain of client/ server responses required for backup retrieval, as opposed to being a fully functional backup tool
 * in itself.
 * <p>
 * Postulated steps:
 * <p>
 * 1. Authentication. Functional.
 * <p>
 * 2. Account settings. Functional.
 * <p>
 * 3. CloudKit Application Initialization. Functional.
 * <p>
 * 4. Record zones. Functional.
 * <p>
 * 5. Backup list. Functional.
 * <p>
 * 6. Snapshot list. Functional.
 * <p>
 * 7. Manifest list. Functional.
 * <p>
 * 8. Retrieve assets. Unknown.
 * <p>
 * 9. Asset tokens. Unknown.
 * <p>
 * 10.AuthorizeGet. Known if unchanged from iOS8.
 * <p>
 * 11. ChunkServer.FileGroups retrieval. Known if unchanged from iOS8.
 * <p>
 * 12. Assemble assets/ files. Known if unchanged from iOS8.
 * <p>
 * 13. Decrypt files. Known if unchanged from iOS8.
 * <p>
 *
 * Thank you to:
 * <p>
 * <a href="https://github.com/ItsASmallWorld">ItsASmallWorld</a> for deciphering key client/ server interactions and
 * assisting with Protobuf definitions.
 *
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
        // Command line arguments.
        AuthenticationMapper authenticationMapper = AuthenticationMapper.instance();
        OptionsFactory optionsFactory = OptionsFactory.create();
        ArgsParser<Property> argsParser = new ArgsParser<>(optionsFactory);
        Map<Property, String> arguments = new HashMap<>();

        try {
            List<String> operands = argsParser.apply(args, arguments);

            if (arguments.containsKey(Property.ARGS_HELP)) {
                Help.instance().accept(optionsFactory.get().keySet());
                System.exit(0);
            }

            arguments.putAll(authenticationMapper.apply(operands));

        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
            System.out.println("Try '" + Property.APP_NAME.defaultValue() + " --help' for more information.");
            System.exit(0);
        }

        logger.debug("-- main() - arguments: {}", arguments);

        // Session constants        
        String deviceID = new BigInteger(256, ThreadLocalRandom.current()).toString(16).toUpperCase(Locale.US);
        //deviceID = "51378363AF15278F992AEB76026C539217D49B2F678DB559F33B53528FAD291C";
        String container = "com.apple.backup.ios";
        String bundle = "com.apple.backupd";
        String ckdFetchRecordZonesOperation = "CKDFetchRecordZonesOperation";

        Headers coreHeaders = new MappedHeaders(
                new BasicHeader(Headers.userAgent, "CloudKit/479 (13A404)"),
                new BasicHeader(Headers.xMmeClientInfo, "<iPhone5,3> <iPhone OS;9.0.1;13A404> <com.apple.cloudkit.CloudKitDaemon/479 (com.apple.cloudd/479)>"),
                new BasicHeader(Headers.xCloudKitProtocolVersion, "client=1;comments=1;device=1;presence=1;records=1;sharing=1;subscriptions=1;users=1;mescal=1;")
        );

        CK.Item mbksync = CK.Item.newBuilder()
                .setValue("mbksync")
                .setType(6)
                .build();

        CK.Item backupAccount = CK.Item.newBuilder()
                .setValue("BackupAccount")
                .setType(1)
                .build();

        CK.Item seven = CK.Item.newBuilder()
                .setValue(UUID.randomUUID().toString()) // Unknown session static UUID.
                .setType(2)
                .build();

        CK.Info info = CK.Info.newBuilder()
                .setContainer(container)
                .setBundle(bundle)
                .setF7(seven)
                .setOs("9.0.1")
                .setApp("com.apple.cloudkit.CloudKitDaemon")
                .setAppVersion("479")
                .setVersion("4.0")
                .setF19(1)
                .setDeviceName("My iPhone")
                .setDeviceID(deviceID)
                .setF23(1)
                .setF25(1)
                .build();

        // Protobuf array response handler.
        InputStreamResponseHandler<List<CK.Response>> ckResponseHandler
                = new InputStreamResponseHandler<>(inputStream -> ProtoBufArray.decode(inputStream, CK.Response.PARSER));

        // Default HttpClient.
        HttpClient httpClient = HttpClients.createDefault();

        // 1. Authenticate via appleId/ password or dsPrsID:mmeAuthToken.
        Auth auth = arguments.containsKey(Property.AUTHENTICATION_TOKEN)
                ? new Auth(arguments.get(Property.AUTHENTICATION_TOKEN))
                : new Authenticator(coreHeaders).authenticate(
                        httpClient,
                        arguments.get(Property.AUTHENTICATION_APPLEID),
                        arguments.get(Property.AUTHENTICATION_PASSWORD));

        logger.debug("-- main() - auth: {}", auth);
        logger.info("-- main() - dsPrsID:mmeAuthToken: {}:{}", auth.dsPrsID(), auth.mmeAuthToken());

        // 2. Account settings.
        HttpUriRequest accountSettingsRequest = new AccountSettingsRequestFactory(coreHeaders)
                .apply(auth.dsPrsID(), auth.mmeAuthToken());

        NSDictionary settings
                = httpClient.execute(accountSettingsRequest, PropertyListResponseHandler.nsDictionaryResponseHandler());

        logger.debug("-- main() - account settings: {}", settings.toASCIIPropertyList());

        AccountInfo accountInfo = new AccountInfo(PLists.get(settings, "appleAccountInfo"));
        Tokens tokens = new Tokens(PLists.get(settings, "tokens"));

        // 3. CloudKit Application Initialization.
        HttpUriRequest ckAppInitRequest
                = CkAppInitBackupRequestFactory.create()
                .newRequest(accountInfo.dsPrsID(), tokens.mmeAuthToken(), tokens.cloudKitToken());

        JsonResponseHandler<CKInit> jsonResponseHandler = new JsonResponseHandler<>(CKInit.class);

        CKInit ckInit = httpClient.execute(ckAppInitRequest, jsonResponseHandler);
        logger.debug("-- main() - ckInit: {}", ckInit);

        CK.Item ckUserId = CK.Item.newBuilder()
                .setValue(ckInit.cloudKitUserId())
                .setType(7)
                .build();

        // 4. Record zones.
        CK.Request requestA = M201RequestFactory.instance()
                .newRequest(
                        container,
                        bundle,
                        ckdFetchRecordZonesOperation,
                        UUID.randomUUID().toString(),
                        mbksync,
                        ckUserId,
                        info);
        logger.debug("-- main() - record zones request: {}", requestA);

        HttpUriRequest postA = PostMessageFactory.defaultInstance().newRequest(
                ckInit.production().url() + "/record/retrieve",
                container,
                bundle,
                ckInit.cloudKitUserId(),
                tokens.cloudKitToken(),
                UUID.randomUUID().toString(),
                Arrays.asList(requestA),
                coreHeaders);

        List<CK.Response> responseA = httpClient.execute(postA, ckResponseHandler);
        logger.debug("-- main() - record zones response: {}", responseA);

        // 5. Backups. 
        CK.Request requestB = M211RequestFactory.instance()
                .newRequest(
                        container,
                        bundle,
                        ckdFetchRecordZonesOperation,
                        UUID.randomUUID().toString(),
                        backupAccount,
                        mbksync,
                        ckUserId,
                        info);
        logger.debug("-- main() - backups request: {}", requestB);

        HttpUriRequest postB = PostMessageFactory.defaultInstance().newRequest(
                ckInit.production().url() + "/record/retrieve",
                container,
                bundle,
                ckInit.cloudKitUserId(),
                tokens.cloudKitToken(),
                UUID.randomUUID().toString(),
                Arrays.asList(requestB),
                coreHeaders);

        List<CK.Response> responseB = httpClient.execute(postB, ckResponseHandler);
        logger.debug("-- main() - backup response: {}", responseB);

        List<String> devices = responseB.stream()
                .map(CK.Response::getM211Response)
                .map(CK.M211Response::getBody)
                .map(CK.M211ResponseBody::getContainerList)
                .flatMap(Collection::stream)
                .filter(value -> value.getTag().getValue().equals("devices"))
                .flatMap(value -> value.getData().getDataList().stream())
                .map(x -> x.getIdItemOp().getItemOp().getItem().getValue())
                .collect(Collectors.toList());
        logger.info("-- main() - devices: {}", devices);

        if (devices.isEmpty()) {
            logger.info("-- main() - no devices");
            System.exit(0);
        }

        // 6. Snapshots.
        CK.Item device = CK.Item.newBuilder()
                .setValue(devices.get(0)) // First device only.
                .setType(1)
                .build();

        CK.Request requestC = M211RequestFactory.instance()
                .newRequest(
                        container,
                        bundle,
                        ckdFetchRecordZonesOperation,
                        UUID.randomUUID().toString(),
                        device,
                        mbksync,
                        ckUserId,
                        info);
        logger.debug("-- main() - snapshots request: {}", requestC);

        HttpUriRequest postC = PostMessageFactory.defaultInstance().newRequest(
                ckInit.production().url() + "/record/retrieve",
                container,
                bundle,
                ckInit.cloudKitUserId(),
                tokens.cloudKitToken(),
                UUID.randomUUID().toString(),
                Arrays.asList(requestC),
                coreHeaders);

        List<CK.Response> responseC = httpClient.execute(postC, ckResponseHandler);
        logger.debug("-- main() - snapshot response: {}", responseC);

        List<String> snapshots = responseC.stream()
                .map(CK.Response::getM211Response)
                .map(CK.M211Response::getBody)
                .map(CK.M211ResponseBody::getContainerList)
                .flatMap(Collection::stream)
                .filter(value -> value.getTag().getValue().equals("snapshots"))
                .flatMap(value -> value.getData().getDataList().stream())
                .map(x -> x.getIdItemOp().getItemOp().getItem().getValue())
                .collect(Collectors.toList());
        logger.info("-- main() - snapshots: {}", snapshots);

        if (snapshots.isEmpty()) {
            logger.info("-- main() - no snapshots for device: {}", devices.get(0));
            System.exit(0);
        }

        // 7. Manifests.   
        CK.Item snapshot = CK.Item.newBuilder()
                .setValue(snapshots.get(0)) // First snapshot only.
                .setType(1)
                .build();

        CK.Request requestD = M211RequestFactory.instance()
                .newRequest(
                        container,
                        bundle,
                        ckdFetchRecordZonesOperation,
                        UUID.randomUUID().toString(),
                        snapshot,
                        mbksync,
                        ckUserId,
                        info);
        logger.debug("-- main() - manifests request: {}", requestD);

        HttpUriRequest postD = PostMessageFactory.defaultInstance().newRequest(
                ckInit.production().url() + "/record/retrieve",
                container,
                bundle,
                ckInit.cloudKitUserId(),
                tokens.cloudKitToken(),
                UUID.randomUUID().toString(),
                Arrays.asList(requestD),
                coreHeaders);

        List<CK.Response> responseD = httpClient.execute(postD, ckResponseHandler);
        logger.debug("-- main() - manifests response: {}", responseD);
    }
}
// TODO info limits have not been set
