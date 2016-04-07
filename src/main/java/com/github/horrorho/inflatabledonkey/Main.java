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

import com.github.horrorho.inflatabledonkey.cloudkitty.CloudKitty;
import com.github.horrorho.inflatabledonkey.crypto.srp.SRPFactory;
import com.github.horrorho.inflatabledonkey.crypto.srp.SRPClient;
import com.github.horrorho.inflatabledonkey.crypto.srp.data.SRPInitResponse;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.dd.plist.NSString;
import com.dd.plist.PropertyListFormatException;
import com.dd.plist.PropertyListParser;
import com.github.horrorho.inflatabledonkey.args.ArgsParser;
import com.github.horrorho.inflatabledonkey.args.AuthenticationMapper;
import com.github.horrorho.inflatabledonkey.args.Help;
import com.github.horrorho.inflatabledonkey.args.OptionsFactory;
import com.github.horrorho.inflatabledonkey.args.Property;
import com.github.horrorho.inflatabledonkey.chunkclient.ChunkClient;
import com.github.horrorho.inflatabledonkey.crypto.srp.EscrowTest;
import com.github.horrorho.inflatabledonkey.crypto.srp.data.SRPGetRecords;
import com.github.horrorho.inflatabledonkey.crypto.srp.data.SRPGetRecordsMetadata;
import com.github.horrorho.inflatabledonkey.data.AccountInfo;
import com.github.horrorho.inflatabledonkey.data.Auth;
import com.github.horrorho.inflatabledonkey.data.Authenticator;
import com.github.horrorho.inflatabledonkey.data.CKInit;
import com.github.horrorho.inflatabledonkey.data.MobileMe;
import com.github.horrorho.inflatabledonkey.data.Tokens;
import com.github.horrorho.inflatabledonkey.data.blob.BlobA6;
import com.github.horrorho.inflatabledonkey.pcs.service.ServiceKeySet;
import com.github.horrorho.inflatabledonkey.pcs.xzone.XKeySet;
import com.github.horrorho.inflatabledonkey.pcs.xzone.XZones;
import com.github.horrorho.inflatabledonkey.protocol.ChunkServer;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit.Zone;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit.ZoneRetrieveResponse;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit.ZoneRetrieveResponseZoneSummary;
import com.github.horrorho.inflatabledonkey.requests.AccountSettingsRequestFactory;
import com.github.horrorho.inflatabledonkey.requests.AuthorizeGetRequestFactory;
import com.github.horrorho.inflatabledonkey.requests.CkAppInitBackupRequestFactory;
import com.github.horrorho.inflatabledonkey.requests.EscrowProxyApi;
import com.github.horrorho.inflatabledonkey.requests.Headers;
import com.github.horrorho.inflatabledonkey.requests.MappedHeaders;
import com.github.horrorho.inflatabledonkey.responsehandler.InputStreamResponseHandler;
import com.github.horrorho.inflatabledonkey.responsehandler.JsonResponseHandler;
import com.github.horrorho.inflatabledonkey.responsehandler.PropertyListResponseHandler;
import com.github.horrorho.inflatabledonkey.util.PLists;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

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
    public static void main(String[] args) throws IOException, PropertyListFormatException, ParseException, ParserConfigurationException, SAXException {
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
        String deviceIdentifier = UUID.randomUUID().toString();
        String deviceHardwareID = new BigInteger(256, ThreadLocalRandom.current()).toString(16).toUpperCase(Locale.US);

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

        // SystemDefault HttpClient.
        CloseableHttpClient httpClient = HttpClients.custom()
                .setUserAgent("CloudKit/479 (13A404)")
                .useSystemProperties()
                .build();
        /*        
         Authenticate via appleId/ password or dsPrsID:mmeAuthToken.
        
         No different to iOS8.
         */
        logger.info("-- main() - *** Authenticate via appleId/ password or dsPrsID:mmeAuthToken ***");
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
         Account settings.
                
         New url/ headers otherwise comparable to iOS8.     
         https://setup.icloud.com/setup/get_account_settings

         Returns an account settings plist, that along with numerous other items includes a cloudKitToken.        
         */
        logger.info("-- main() - *** Account settings ***");
        HttpUriRequest accountSettingsRequest = new AccountSettingsRequestFactory(coreHeaders)
                .apply(auth.dsPrsID(), auth.mmeAuthToken());

        NSDictionary settings
                = httpClient.execute(accountSettingsRequest, PropertyListResponseHandler.nsDictionaryResponseHandler());
        logger.debug("-- main() - account settings: {}", settings.toASCIIPropertyList());

        AccountInfo accountInfo = new AccountInfo(PLists.<NSDictionary>get(settings, "appleAccountInfo"));
        Tokens tokens = new Tokens(PLists.get(settings, "tokens"));
        MobileMe mobileMe = new MobileMe(PLists.<NSDictionary>get(settings, "com.apple.mobileme"));


        /* 
         CloudKit Application Initialization.
         
         Url/ headers are specific to the particular bundle/ container required 
         (in our case bundle = com.apple.backupd  container = com.apple.backup.ios)
         cloudKitToken also required
         https://setup.icloud.com/setup/ck/v1/ckAppInit?container=$CONTAINER
        
         Returns a JSON response outlining various urls and a cloudKitUserId.        
         */
        logger.info("-- main() - *** CloudKit Application Initialization ***");
        HttpUriRequest ckAppInitRequest
                = CkAppInitBackupRequestFactory.create()
                .newRequest(accountInfo.dsPrsID(), tokens.mmeAuthToken(), tokens.cloudKitToken());

        JsonResponseHandler<CKInit> jsonResponseHandler = new JsonResponseHandler<>(CKInit.class);

        CKInit ckInit = httpClient.execute(ckAppInitRequest, jsonResponseHandler);
        logger.debug("-- main() - ckInit: {}", ckInit);

        /* 
         EscrowService SRP-6a exchange.
        
         Coding critical as repeated errors will lock the account.                 
         https://en.wikipedia.org/wiki/Secure_Remote_Password_protocol
        
         escrow user id = dsid
         escrow password = dsid                
         */
        logger.info("-- main() - *** EscrowService SRP exchange ***");

        Optional<String> optionalEscrowProxyUrl = mobileMe.get("com.apple.Dataclass.KeychainSync", "escrowProxyUrl");
        if (!optionalEscrowProxyUrl.isPresent()) {
            logger.error("-- main() - no escrowProxyUrl");
            System.exit(-1);
        }

        String escrowProxyUrl = optionalEscrowProxyUrl.get();
        EscrowProxyApi escrowProxy = new EscrowProxyApi(accountInfo.dsPrsID(), escrowProxyUrl, coreHeaders);

        /* 
         EscrowService SRP-6a exchanges: GETRECORDS
        
         We'll do the last step first, so we can abort if only a few attempts remain rather than risk further depleting
         them with experimental code. 
         */
        logger.info("-- main() - *** EscrowService SRP exchange: GETRECORDS ***");

        NSDictionary records = escrowProxy.getRecords(httpClient, tokens.mmeAuthToken());
        logger.debug("-- main() - getRecords response: {}", records.toASCIIPropertyList());

        SRPGetRecords srpGetRecords = new SRPGetRecords(records);

        srpGetRecords.metadata()
                .values()
                .stream()
                .forEach(v -> {
                    byte[] metadata = Base64.getDecoder().decode(v.metadata());
                    try {
                        NSObject nsObject = PropertyListParser.parse(metadata);
                        if (nsObject instanceof NSDictionary) {
                            logger.debug("-- main() - label: {} dictionary: {}",
                                    v.label(), ((NSDictionary) nsObject).toXMLPropertyList());
                        }
                    } catch (IOException | PropertyListFormatException | ParseException | ParserConfigurationException | SAXException ex) {
                        logger.warn("-- main() - failed to parse property list: {}", v.metadata());
                    }
                });

        int remainingAttempts = srpGetRecords.remainingAttempts();
        logger.debug("-- main() - remaining attempts: {}", remainingAttempts);
        if (remainingAttempts < 5) {
            logger.warn("-- main() - remaining attempt threshold, aborting");
            System.exit(-1);
        }

        /* 
         EscrowService SRP-6a exchanges: SRP_INIT
        
         Randomly generated ephemeral key A presented to escrow server along with id (mmeAuthToken).
         Server returns amongst other things a salt and an ephemeral key B.
         */
        logger.info("-- main() - *** EscrowService SRP exchange: SRP_INIT ***");

        SRPClient srp = SRPFactory.rfc5054(new SecureRandom());
        byte[] ephemeralKeyA = srp.generateClientCredentials();
        NSDictionary srpInit = escrowProxy.srpInit(httpClient, tokens.mmeAuthToken(), ephemeralKeyA);

        SRPInitResponse srpInitResponse = new SRPInitResponse(srpInit);

        logger.debug("-- main() - SRP_INIT dictionary: {}", srpInit.toASCIIPropertyList());
        logger.debug("-- main() - SRP_INIT data: {}", srpInitResponse);

        /* 
         EscrowService SRP-6a exchanges: RECOVER
        
         CRITICAL step. Generate M1 verification message. Failure here will deplete attempts (we have 10 attempts max).
         Server will abort on an invalid M1 or present us with, amongst other things, M2 which we can verify (or not).
         */
        logger.info("-- main() - *** EscrowService SRP exchange: RECOVER ***");

        byte[] dsid = srpInitResponse.dsid().getBytes(StandardCharsets.UTF_8);
        logger.debug("-- main() - escrow service id: {}", Hex.encodeHexString(dsid));
        logger.debug("-- main() - escrow service password: {}", Hex.encodeHexString(dsid));

        Optional<byte[]> optionalM1
                = srp.calculateClientEvidenceMessage(srpInitResponse.salt(), dsid, dsid, srpInitResponse.ephemeralKeyB());

        if (!optionalM1.isPresent()) {
            logger.error("-- main() - bad ephemeral key from server: 0x{}",
                    Hex.encodeHex(srpInitResponse.ephemeralKeyB()));
            System.exit(-1);
        }
        byte[] m1 = optionalM1.get();
        logger.debug("-- main() - m1: 0x{}", Hex.encodeHexString(m1));

        NSDictionary recover
                = escrowProxy.recover(httpClient, tokens.mmeAuthToken(), m1, srpInitResponse.uid(), srpInitResponse.tag());
        logger.debug("-- main() - srpInit response: {}", recover.toASCIIPropertyList());

        /* 
         EscrowService SRP-6a exchanges: session key
        
         Verify the server M2 message (or not). Either way we have a session key and an encrypted key set.
         */
        logger.info("-- main() - *** EscrowService SRP exchange: session key ***");

        byte[] recoverRespBlob = Base64.getDecoder().decode(PLists.<NSString>get(recover, "respBlob").getContent());
        BlobA6 blobA6 = new BlobA6(ByteBuffer.wrap(recoverRespBlob));

        Optional<byte[]> optionalKey = srp.verifyServerEvidenceMessage(blobA6.m2());
        if (!optionalKey.isPresent()) {
            logger.debug("-- main() - failed to verify m2");
        }

        byte[] key = optionalKey.get();
        logger.debug("-- main() - session key: {}", Hex.encodeHexString(key));

        /*
         EscrowService decrypt M2 key set.
         
         BlobA6 contains amongst other things, salt, iv and encrypted key set data.
         */
        Optional<ServiceKeySet> optionalServiceKeySet = EscrowTest.decryptRecoveryResponseBlob(blobA6, key);

        if (!optionalServiceKeySet.isPresent()) {
            logger.warn("-- main() - failed to recover key set");
            System.exit(-1);
        }
        ServiceKeySet keySet = optionalServiceKeySet.get();


        /* 
         EscrowService SRP-6a exchanges: decrypt escrowed keys
        
         Verify the server M2 message (or not). Either way we have a session key and an encrypted key set.
         */
        logger.info("-- main() - *** EscrowService SRP exchange: decrypt escrowed keys ***");

        Optional<byte[]> pcsRecordMetadata = srpGetRecords.metadata("com.apple.protectedcloudstorage.record")
                .map(SRPGetRecordsMetadata::metadata)
                .map(s -> Base64.getDecoder().decode(s));

        if (!pcsRecordMetadata.isPresent()) {
            logger.warn("-- main() - unable to locate 'com.apple.protectedcloudstorage.record' metadata");
            System.exit(-1);
        }

        Optional<ServiceKeySet> optionalEscrowServiceKeySet
                = EscrowTest.decryptGetRecordsResponseMetadata(pcsRecordMetadata.get(), keySet::key);
        if (!optionalEscrowServiceKeySet.isPresent()) {
            logger.warn("-- main() - failed to retrieve escrow key set");
            System.exit(-1);
        }
        ServiceKeySet escrowServiceKeySet = optionalEscrowServiceKeySet.get();

        /* 
         Protection zone setup.

         XZones is our simplified and experimental protection zones handler.
         */
        logger.info("-- main() - *** Protection zone setup ***");

        final XZones zones = XZones.create();
        zones.put(escrowServiceKeySet.keys());

        /*
         CloudKitty, simple client-server CloudKit calls. Meow.
         */
        CloudKitty cloudKitty
                = new CloudKitty(
                        ckInit,
                        tokens.cloudKitToken(),
                        deviceHardwareID,
                        deviceIdentifier,
                        coreHeaders,
                        ckResponseHandler);

        /* 
         Record zones: _defaultZone
        
         Url ckDatabase from ckInit + /record/retrieve
         ~ pXX-ckdatabase.icloud.com:443//api/client/record/retrieve
        
         Message type 201 request (cloud_kit.proto)    
        
         Return us an encrypted key which we pass to XZones.
         */
        logger.info("-- main() - *** Record zones: _defaultZone ***");
        List<CloudKit.ZoneRetrieveResponse> recordZoneDefaultResponse
                = cloudKitty.zoneRetrieveRequest(httpClient, container, bundle, "_defaultZone");

        recordZoneDefaultResponse
                .stream()
                .map(ZoneRetrieveResponse::getZoneSummarysList)
                .flatMap(Collection::stream)
                .filter(ZoneRetrieveResponseZoneSummary::hasTargetZone)
                .map(ZoneRetrieveResponseZoneSummary::getTargetZone)
                .filter(Zone::hasProtectionInfo)
                .map(Zone::getProtectionInfo)
                .forEach(p -> zones.put(p.getProtectionInfoTag(), p.getProtectionInfo().toByteArray()));

        /* 
         Record zones: mbksync
        
         Url ckDatabase from ckInit + /record/retrieve
         ~ pXX-ckdatabase.icloud.com:443//api/client/record/retrieve
        
         Message type 201 request (cloud_kit.proto)      

         Return us an encrypted key which we pass to XZones.
        
         We should really combine _defaultZone and mbksync requests into a single request using delimited protobuf
         requests.         
         */
        logger.info("-- main() - *** Record zones: mbksync ***");
        List<CloudKit.ZoneRetrieveResponse> zoneRetrieveRequest
                = cloudKitty.zoneRetrieveRequest(httpClient, container, bundle, "mbksync");

        zoneRetrieveRequest
                .stream()
                .map(ZoneRetrieveResponse::getZoneSummarysList)
                .flatMap(Collection::stream)
                .filter(ZoneRetrieveResponseZoneSummary::hasTargetZone)
                .map(ZoneRetrieveResponseZoneSummary::getTargetZone)
                .filter(Zone::hasProtectionInfo)
                .map(Zone::getProtectionInfo)
                .forEach(p -> zones.put(p.getProtectionInfoTag(), p.getProtectionInfo().toByteArray()));

        /* 
         Backup list.
        
         Url/ headers as step 4.
         Message type 211 request, protobuf array encoded.

         Returns device data/ backups.        
         */
        logger.info("-- main() - *** Backup list ***");
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
         Snapshot list (+ Keybag)
        
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

        // Device journal information. Output not used.
        logger.info("-- main() - device journal information");

        List<CloudKit.RecordRetrieveResponse> deviceJournal
                = cloudKitty.recordRetrieveRequest(
                        httpClient,
                        container,
                        bundle,
                        "mbksync",
                        token(devices.get(deviceIndex), 1) + ":Journal");

        if (snapshots.isEmpty()) {
            logger.info("-- main() - no snapshots for device: {}", devices.get(deviceIndex));
            System.exit(0);
        }
        /* 
         Manifest list.
        
         Url/ headers as step 6.
         Message type 211 with the required snapshot uuid, protobuf array encoded.

         Returns system/ backup properties (bytes ? format ?? proto), quota information and manifest details.
        
         */
        logger.info("-- main() - *** Manifest list ***");

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
         Retrieve list of files.
    
         Url/ headers as step 7.
         Message type 211 with the required manifest, protobuf array encoded.

         Returns system/ backup properties (bytes ? format ?? proto), quota information and manifest details.
        
         Returns a rather somewhat familiar looking set of results but with encoded bytes.
         */
        logger.info("-- main() - *** Retrieve list of assets ***");

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
         Retrieve asset tokens.
    
         Url/ headers as step 8.
         Message type 211 with the required file, protobuf array encoded.
         */
        logger.info("-- main() - *** Retrieve list of asset tokens ***");

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
         AuthorizeGet.
        
         Process somewhat different to iOS8.
        
         New headers/ mmcs auth token. See AuthorizeGetRequestFactory for details.

         Returns a ChunkServer.FileGroup protobuf which is largely identical to iOS8.        
         */
        logger.info("-- main() - *** AuthorizeGet ***");

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
         TODO -> SOLVED! :)
        
         encryptedAttributes need deciphering.
         I suspect this contains the metadata required to manage/ decrypt our files c.f. ICloud.MBSFile.
         https://www.apple.com/business/docs/iOS_Security_Guide.pdf refers to a Cloudkit Service key.
         This is available after authentication but may well be wrapped. 
         We need to track down the appropriate api call.
         Hopefully this key will decrypt the zone data in step 4 to reveal zone-wide keys.
         AES is the likely cipher. 
        
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
                x -> logger.debug("-- main() - encryptedAttributes: {}", Hex.encodeHex(x.toByteArray())));

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
         ChunkServer.FileGroups.

         At present, we are missing file attributes. 
         The chunk decryption key mechanic has also changed.
         We can download data, but we are unable to decrypt the chunks.
         */
        logger.info("-- main() - *** ChunkServer.FileGroups ***");
        ChunkClient chunkClient = new ChunkClient(coreHeaders);

        BiConsumer<ByteString, List<byte[]>> dataConsumer
                = (fileChecksum, data) -> write(Hex.encodeHex(fileChecksum.toByteArray()) + ".bin", data);

        //chunkClient.fileGroups(httpClient, fileGroups, dataConsumer);
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
        // TODO File attribute code/ encryptedAttributes.
        // TODO For now just write out the 
    }

    static String token(String delimited, int index) {
        String[] split = delimited.split(":");
        return (index < split.length) ? split[index] : "";
    }

    static void write(String path, List<byte[]> data) {
        // Dump out binary data to file.
        try (OutputStream outputStream = Files.newOutputStream(Paths.get(path))) {
            for (byte[] d : data) {
                outputStream.write(d);
            }
            logger.info("-- write() - file written: {}", path);

        } catch (IOException ex) {
            logger.warn("-- write() - exception: {}", ex);
        }
    }

}
