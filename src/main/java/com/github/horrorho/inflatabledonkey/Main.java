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
import com.github.horrorho.inflatabledonkey.io.IOFunction;
import com.github.horrorho.inflatabledonkey.protocol.ChunkServer;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import com.github.horrorho.inflatabledonkey.protocol.ProtoBufArray;
import com.github.horrorho.inflatabledonkey.requests.AccountSettingsRequestFactory;
import com.github.horrorho.inflatabledonkey.requests.AuthorizeGetRequestFactory;
import com.github.horrorho.inflatabledonkey.requests.CkAppInitBackupRequestFactory;
import com.github.horrorho.inflatabledonkey.requests.Headers;
import com.github.horrorho.inflatabledonkey.requests.MappedHeaders;
import com.github.horrorho.inflatabledonkey.responsehandler.InputStreamResponseHandler;
import com.github.horrorho.inflatabledonkey.responsehandler.JsonResponseHandler;
import com.github.horrorho.inflatabledonkey.responsehandler.PropertyListResponseHandler;
import com.github.horrorho.inflatabledonkey.util.PLists;
import com.google.protobuf.ByteString;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * iOS9 backup retrieval proof of concept - work in progress. Development tool intended to demonstrate a functional
 * chain of client/ server responses required for backup retrieval, as opposed to being a fully functional backup tool
 * in itself.
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

        int deviceIndex = 0;
        int snapshotIndex = 0;
        int manifestIndex = 0;
        String protocPath = null;

        try {
            List<String> operands = argsParser.apply(args, arguments);

            if (arguments.containsKey(Property.ARGS_HELP)) {
                Help.instance().accept(optionsFactory.get().keySet());
                System.exit(0);
            }

            arguments.putAll(authenticationMapper.apply(operands));

            Function<Property, String> getProperty = property
                    -> arguments.containsKey(property)
                            ? arguments.get(property)
                            : property.defaultValue();

            Function<Property, Integer> intArgument = property
                    -> Integer.parseInt(getProperty.apply(property));

            deviceIndex = intArgument.apply(Property.SELECT_DEVICE_INDEX);
            snapshotIndex = intArgument.apply(Property.SELECT_SNAPSHOT_INDEX);
            manifestIndex = intArgument.apply(Property.SELECT_MANIFEST_INDEX);

            protocPath = arguments.containsKey(Property.PROTOC_PATH)
                    ? arguments.get(Property.PROTOC_PATH) == null
                            ? Property.PROTOC_PATH.defaultValue()
                            : arguments.get(Property.PROTOC_PATH)
                    : null;
        } catch (IllegalArgumentException ex) {
            System.out.println("Argument error: " + ex.getMessage());
            System.out.println("Try '" + Property.APP_NAME.defaultValue() + " --help' for more information.");
            System.exit(0);
        }

        // Constants        
        // TODO can we retrieve this value?
        String deviceID = new BigInteger(256, ThreadLocalRandom.current()).toString(16).toUpperCase(Locale.US);

        String container = "com.apple.backup.ios";
        String bundle = "com.apple.backupd";

        Headers coreHeaders = new MappedHeaders(
                new BasicHeader(Headers.userAgent, "CloudKit/479 (13A404)"),
                new BasicHeader(Headers.xMmeClientInfo, "<iPhone5,3> <iPhone OS;9.0.1;13A404> <com.apple.cloudkit.CloudKitDaemon/479 (com.apple.cloudd/479)>"),
                new BasicHeader(Headers.xCloudKitProtocolVersion, "client=1;comments=1;device=1;presence=1;records=1;sharing=1;subscriptions=1;users=1;mescal=1;"),
                new BasicHeader(Headers.xAppleMmcsProtoVersion, "4.0")
        );

        // Raw protoc decode
        RawProtoDecoder rawProtoDecoder = protocPath == null
                ? null
                : new RawProtoDecoder(protocPath);

        InputStreamResponseHandler<List<CloudKit.ResponseOperation>> ckResponseHandler
                = new InputStreamResponseHandler<>(new RawProtoDecoderLogger(rawProtoDecoder));

        // Default HttpClient.
        HttpClient httpClient = HttpClients.createDefault();

        /*        
         STEP 1. Authenticate via appleId/ password or dsPrsID:mmeAuthToken.
        
         No different to iOS8.
         */
        logger.info("-- main() - STEP 1. Authenticate via appleId/ password or dsPrsID:mmeAuthToken.");
        Auth auth = arguments.containsKey(Property.AUTHENTICATION_TOKEN)
                ? new Auth(arguments.get(Property.AUTHENTICATION_TOKEN))
                : new Authenticator(coreHeaders).authenticate(
                        httpClient,
                        arguments.get(Property.AUTHENTICATION_APPLEID),
                        arguments.get(Property.AUTHENTICATION_PASSWORD));

        logger.debug("-- main() - auth: {}", auth);
        logger.info("-- main() - dsPrsID:mmeAuthToken: {}:{}", auth.dsPrsID(), auth.mmeAuthToken());

        if (arguments.containsKey(Property.ARGS_TOKEN)) {
            System.out.println("DsPrsID:mmeAuthToken " + auth.dsPrsID() + ":" + auth.mmeAuthToken());
            System.exit(0);
        }

        /*
         STEP 2. Account settings.
                
         New url/ headers otherwise comparable to iOS8.     
         https://setup.icloud.com/setup/get_account_settings

         Returns an account settings plist, that along with numerous other items includes a cloudKitToken.        
         */
        logger.info("-- main() - STEP 2. Account settings.");
        HttpUriRequest accountSettingsRequest = new AccountSettingsRequestFactory(coreHeaders)
                .apply(auth.dsPrsID(), auth.mmeAuthToken());

        NSDictionary settings
                = httpClient.execute(accountSettingsRequest, PropertyListResponseHandler.nsDictionaryResponseHandler());
        logger.debug("-- main() - account settings: {}", settings.toASCIIPropertyList());

        AccountInfo accountInfo = new AccountInfo(PLists.get(settings, "appleAccountInfo"));
        Tokens tokens = new Tokens(PLists.get(settings, "tokens"));

        /* 
         STEP 3. CloudKit Application Initialization.
         
         Url/ headers are specific to the particular bundle/ container required 
         (in our case bundle = com.apple.backupd  container = com.apple.backup.ios)
         cloudKitToken also required
         https://setup.icloud.com/setup/ck/v1/ckAppInit?container=$CONTAINER
        
         Returns a JSON response outlining various urls and a cloudKitUserId.        
         */
        logger.info("-- main() - STEP 3. CloudKit Application Initialization.");
        HttpUriRequest ckAppInitRequest
                = CkAppInitBackupRequestFactory.create()
                .newRequest(accountInfo.dsPrsID(), tokens.mmeAuthToken(), tokens.cloudKitToken());

        JsonResponseHandler<CKInit> jsonResponseHandler = new JsonResponseHandler<>(CKInit.class);

        CKInit ckInit = httpClient.execute(ckAppInitRequest, jsonResponseHandler);
        logger.debug("-- main() - ckInit: {}", ckInit);

        CloudKit.Identifier ckUserId = CloudKit.Identifier.newBuilder()
                .setName(ckInit.cloudKitUserId())
                .setType(7)
                .build();

        /*
         CloudKitty. Meow.
         */
        CloudKitty cloudKitty
                = new CloudKitty(
                        ckInit,
                        tokens.cloudKitToken(),
                        deviceID,
                        coreHeaders,
                        ckResponseHandler);

        /* 
         STEP 4. Record zones.
        
         Url ckDatabase from ckInit + /record/retrieve
         ~ pXX-ckdatabase.icloud.com:443//api/client/record/retrieve
         Headers similar to step 3
        
         Message type 201 request (cloud_kit.proto)      
         This proto is encoded with Apple's protobuf array encoding (see ProtoBufArray class).
         Possibility of passing multiple requests not yet explored.
        
         Returns record zone data which needs further analysis.
        
         */
        logger.info("-- main() - STEP 4. Record zones.");
        List<CloudKit.ZoneRetrieveResponse> zoneRetrieveRequest
                = cloudKitty.zoneRetrieveRequest(httpClient, container, bundle, "mbksync");

        /* 
         STEP 5. Backup list.
        
         Url/ headers as step 4.
         Message type 211 request, protobuf array encoded.

         Returns device data/ backups.        
         */
        logger.info("-- main() - STEP 5. Backup list.");
        List<CloudKit.RecordRetrieveResponse> responseBackupList
                = cloudKitty.recordRetrieveRequest(httpClient, container, bundle, "mbksync", "BackupAccount");

        List<String> devices = responseBackupList.stream()
                .map(CloudKit.RecordRetrieveResponse::getRecord)
                .map(CloudKit.Record::getRecordFieldList)
                .flatMap(Collection::stream)
                .filter(value -> value.getIdentifier().getName().equals("devices"))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getRecordFieldValueList)
                .flatMap(Collection::stream)
                .map(CloudKit.RecordFieldValue::getReferenceValue)
                .map(CloudKit.RecordReference::getRecordIdentifier)
                .map(CloudKit.RecordIdentifier::getValue)
                .map(CloudKit.Identifier::getName)
                .collect(Collectors.toList());
        logger.info("-- main() - devices: {}", devices);

        if (devices.isEmpty()) {
            logger.info("-- main() - no devices");
            System.exit(0);
        }

        /* 
         STEP 6. Snapshot list (+ Keybag)
        
         Url/ headers as step 5.
         Message type 211 with the required backup uuid, protobuf array encoded.

         Returns device/ snapshots/ keybag information.
         Timestamps are hex encoded double offsets to 01 Jan 2001 00:00:00 GMT (Cocoa/ Webkit reference date).
        
         */
        logger.info("-- main() - STEP 6. Snapshot list.");

        if (deviceIndex >= devices.size()) {
            logger.warn("-- main() - No such device: {}, available devices: {}", deviceIndex, devices);
            System.exit(0);
        }

        List<CloudKit.RecordRetrieveResponse> responseSnapshotList
                = cloudKitty.recordRetrieveRequest(httpClient, container, bundle, "mbksync", devices.get(deviceIndex));

        List<CloudKit.RecordField> recordFieldsC = responseSnapshotList.stream()
                .map(CloudKit.RecordRetrieveResponse::getRecord)
                .map(CloudKit.Record::getRecordFieldList)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<String> snapshots = recordFieldsC.stream()
                .filter(value -> value.getIdentifier().getName().equals("snapshots"))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getRecordFieldValueList)
                .flatMap(Collection::stream)
                .map(CloudKit.RecordFieldValue::getReferenceValue)
                .map(CloudKit.RecordReference::getRecordIdentifier)
                .map(CloudKit.RecordIdentifier::getValue)
                .map(CloudKit.Identifier::getName)
                .collect(Collectors.toList());
        logger.info("-- main() - snapshots: {}", snapshots);

        String keybagUUID = recordFieldsC.stream()
                .filter(value -> value.getIdentifier().getName().equals("currentKeybagUUID"))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getStringValue)
                .findFirst()
                .orElse(null);
        logger.info("-- main() - keybagUUID: {}", keybagUUID);

        if (snapshots.isEmpty()) {
            logger.info("-- main() - no snapshots for device: {}", devices.get(deviceIndex));
            System.exit(0);
        }

        /* 
         STEP 7. Manifest list.
        
         Url/ headers as step 6.
         Message type 211 with the required snapshot uuid, protobuf array encoded.

         Returns system/ backup properties (bytes ? format ?? proto), quota information and manifest details.
        
         */
        logger.info("-- main() - STEP 7. Manifest list.");

        if (snapshotIndex >= snapshots.size()) {
            logger.warn("-- main() - No such snapshot: {}, available snapshots: {}", snapshotIndex, snapshots);
            System.exit(0);
        }

        List<CloudKit.RecordRetrieveResponse> responseManifestList
                = cloudKitty.recordRetrieveRequest(
                        httpClient,
                        container,
                        bundle,
                        "mbksync",
                        snapshots.get(snapshotIndex));

        List<String> manifests = responseManifestList.stream()
                .map(CloudKit.RecordRetrieveResponse::getRecord)
                .map(CloudKit.Record::getRecordFieldList)
                .flatMap(Collection::stream)
                .filter(value -> value.getIdentifier().getName().equals("manifestIDs"))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getRecordFieldValueList)
                .flatMap(Collection::stream)
                .map(CloudKit.RecordFieldValue::getStringValue)
                .collect(Collectors.toList());
        logger.info("-- main() - manifests: {}", manifests);

        if (manifests.isEmpty()) {
            logger.info("-- main() - no manifests for snapshot: {}", snapshots.get(snapshotIndex));
            System.exit(0);
        }

        /* 
         STEP 8. Retrieve list of files.
    
         Url/ headers as step 7.
         Message type 211 with the required manifest, protobuf array encoded.

         Returns system/ backup properties (bytes ? format ?? proto), quota information and manifest details.
        
         Returns a rather somewhat familiar looking set of results but with encoded bytes.
         */
        logger.info("-- main() - STEP 8. Retrieve list of assets");

        if (manifestIndex >= manifests.size()) {
            logger.warn("-- main() - No such manifest: {}, available manifests: {}", manifestIndex, manifests);
            System.exit(0);
        }

        List<CloudKit.RecordRetrieveResponse> responseAssetList
                = cloudKitty.recordRetrieveRequest(
                        httpClient,
                        container,
                        bundle,
                        "_defaultZone",
                        (manifests.get(manifestIndex) + ":0"));

        List<String> files = responseAssetList.stream()
                .map(CloudKit.RecordRetrieveResponse::getRecord)
                .map(CloudKit.Record::getRecordFieldList)
                .flatMap(Collection::stream)
                .filter(c -> c.getIdentifier().getName().equals("files"))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getRecordFieldValueList)
                .flatMap(Collection::stream)
                .map(CloudKit.RecordFieldValue::getReferenceValue)
                .map(CloudKit.RecordReference::getRecordIdentifier)
                .map(CloudKit.RecordIdentifier::getValue)
                .map(CloudKit.Identifier::getName)
                .collect(Collectors.toList());
        logger.debug("-- main() - assets: {}", files);

        /* 
         STEP 9. Retrieve asset tokens.
    
         Url/ headers as step 8.
         Message type 211 with the required file, protobuf array encoded.
         */
        logger.info("-- main() - STEP 9. Retrieve list of asset tokens");

        // F:UUID:token:length:x
        // Find first file that isn't 0 length.
        // Not ideal, as all files might be zero length in this manifest, but allows us a flat sequence of steps.
        String file = files.stream()
                .filter(a -> {
                    // TOFIX fragile
                    String[] split = a.split(":");
                    return !split[3].equals("0");
                })
                .findFirst()
                .orElse(null);

        if (file == null) {
            logger.warn("-- main() - Manifest only contains empty files: {}, try another one.", manifests.get(manifestIndex));
            System.exit(0);
        }

        List<CloudKit.RecordRetrieveResponse> assetTokens
                = cloudKitty.recordRetrieveRequest(
                        httpClient,
                        container,
                        bundle,
                        "_defaultZone",
                        file);


        /* 
         STEP 10. AuthorizeGet.
        
         Process somewhat different to iOS8.
        
         New headers/ mmcs auth token. See AuthorizeGetRequestFactory for details.

         Returns a ChunkServer.FileGroup protobuf which is largely identical to iOS8.        
         */
        logger.info("-- main() - STEP 10. AuthorizeGet");

        List<CloudKit.Asset> contents = assetTokens.stream()
                .map(CloudKit.RecordRetrieveResponse::getRecord)
                .map(CloudKit.Record::getRecordFieldList)
                .flatMap(Collection::stream)
                .filter(c -> c.getIdentifier().getName().equals("contents"))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getAssetValue)
                .collect(Collectors.toList());

        // TODO confusing, rename to file attributes or similar?
        CloudKit.Asset asset = contents.get(0);
        logger.debug("-- main() - file attributes: {}", asset);

        /*
        TODO
        
        encryptedAttributes need deciphering.
        I suspect this contains the metadata required to manage/ decrypt our files c.f. ICloud.MBSFile.
        https://www.apple.com/business/docs/iOS_Security_Guide.pdf refers to a Cloudkit Service key.
        This is available after authentication but may well be wrapped. 
        We need to track down the appropriate api call.
        Hopefully this key will decrypt the zone data in step 4 to reveal zone-wide keys.
        
        So:
        
        cloudkit service key > zone wide key > file key
        
        */
        List<ByteString> encryptedAttributes = assetTokens.stream()
                .map(CloudKit.RecordRetrieveResponse::getRecord)
                .map(CloudKit.Record::getRecordFieldList)
                .flatMap(Collection::stream)
                .filter(c -> c.getIdentifier().getName().equals("encryptedAttributes"))
                .map(CloudKit.RecordField::getValue)
                .map(CloudKit.RecordFieldValue::getBytesValue)
                .collect(Collectors.toList());

        encryptedAttributes.forEach(
                x -> logger.debug("-- main() - encryptedAttributes: {}", Hex.toHexString(x.toByteArray())));

        // FileTokens. Expanded from iOS8.
        CloudKit.FileTokens fileTokens = FileTokensFactory.instance().apply(Arrays.asList(asset));

        // TODO check mmcsurl and com.apple.Dataclass.Content url match. But is there a reason they shouldn't?
        HttpUriRequest authorizeGet = new AuthorizeGetRequestFactory(coreHeaders)
                .newRequest(auth.dsPrsID(), asset.getContentBaseURL(), container, "_defaultZone", fileTokens);
        logger.debug("-- main() - authorizeGet request: {}", authorizeGet);

        ResponseHandler<ChunkServer.FileGroups> fileGroupsResponseHandler
                = new InputStreamResponseHandler<>(ChunkServer.FileGroups.PARSER::parseFrom);

        ChunkServer.FileGroups fileGroups = httpClient.execute(authorizeGet, fileGroupsResponseHandler);
        logger.debug("-- main() - fileGroups: {}", fileGroups);

        /* 
         STEP 11. ChunkServer.FileGroups.
        
         TODO. 
         */
        /*
         STEP 12. Assemble assets/ files.
         */
        logger.info("-- main() - STEP 12. Assemble assets/ files.");
        List<CloudKit.QueryRetrieveRequestResponse> keybagResponse
                = cloudKitty.queryRetrieveRequest(
                        httpClient,
                        container,
                        bundle,
                        "mbksync",
                        "K:" + keybagUUID);

        // TODO Possibility of multiple keybags. iOS8 backups could have multiple keybags.
    }

    static void dump(byte[] data, String path) throws IOException {
        // Dump out binary data to file.
        try (OutputStream outputStream = Files.newOutputStream(Paths.get(path))) {
            outputStream.write(data);
        }
    }
}
