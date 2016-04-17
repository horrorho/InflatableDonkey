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
package com.github.horrorho.inflatabledonkey.cloud.srp;

import com.dd.plist.NSData;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.dd.plist.NSString;
import com.dd.plist.PropertyListFormatException;
import com.dd.plist.PropertyListParser;
import com.github.horrorho.inflatabledonkey.args.Property;
import com.github.horrorho.inflatabledonkey.crypto.srp.EscrowTest;
import com.github.horrorho.inflatabledonkey.crypto.srp.SRPFactory;
import com.github.horrorho.inflatabledonkey.crypto.srp.data.SRPGetRecords;
import com.github.horrorho.inflatabledonkey.crypto.srp.data.SRPGetRecordsMetadata;
import com.github.horrorho.inflatabledonkey.crypto.srp.data.SRPInitResponse;
import com.github.horrorho.inflatabledonkey.cloud.accounts.AccountInfo;
import com.github.horrorho.inflatabledonkey.cloud.accounts.MobileMe;
import com.github.horrorho.inflatabledonkey.cloud.accounts.Token;
import com.github.horrorho.inflatabledonkey.cloud.accounts.Tokens;
import com.github.horrorho.inflatabledonkey.crypto.AESCBC;
import com.github.horrorho.inflatabledonkey.crypto.GCMDataB;
import com.github.horrorho.inflatabledonkey.crypto.PBKDF2;
import com.github.horrorho.inflatabledonkey.crypto.eckey.ECAssistant;
import com.github.horrorho.inflatabledonkey.crypto.eckey.ECPrivate;
import com.github.horrorho.inflatabledonkey.crypto.eckey.ECPublic;
import com.github.horrorho.inflatabledonkey.crypto.eckey.ECurves;
import com.github.horrorho.inflatabledonkey.crypto.key.Key;
import com.github.horrorho.inflatabledonkey.crypto.key.KeyID;
import com.github.horrorho.inflatabledonkey.crypto.srp.SRPClient;
import com.github.horrorho.inflatabledonkey.data.blob.BlobA0;
import com.github.horrorho.inflatabledonkey.data.blob.BlobA6;
import com.github.horrorho.inflatabledonkey.data.der.BackupEscrow;
import com.github.horrorho.inflatabledonkey.data.der.DERUtils;
import com.github.horrorho.inflatabledonkey.data.der.KeySet;
import com.github.horrorho.inflatabledonkey.data.der.PublicKeyInfo;
import com.github.horrorho.inflatabledonkey.pcs.service.ServiceKeySet;
import com.github.horrorho.inflatabledonkey.pcs.service.ServiceKeySetBuilder;
import com.github.horrorho.inflatabledonkey.pcs.xzone.KeyImport;
import com.github.horrorho.inflatabledonkey.pcs.xzone.XUnwrapData;
import com.github.horrorho.inflatabledonkey.requests.EscrowProxyRequestFactory;
import com.github.horrorho.inflatabledonkey.responsehandler.PropertyListResponseHandler;
import com.github.horrorho.inflatabledonkey.util.PLists;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import javax.xml.parsers.ParserConfigurationException;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * SRPx. EscrowService SRP-6a exchange.
 *
 * @author Ahseya
 */
@Immutable
public final class SRPx {

    private static final Logger logger = LoggerFactory.getLogger(SRPx.class);

    private static final PropertyListResponseHandler<NSDictionary> RESPONSE_HANDLER
            = PropertyListResponseHandler.nsDictionaryResponseHandler();

    private static final int MIN_REMAINING_ATTEMPTS = Property.SRP_MIN_REMAINING_ATTEMPTS.intValue().orElse(3);

    public static ServiceKeySet srp(HttpClient httpClient, MobileMe mobileMe, Tokens tokens, AccountInfo accountInfo) throws IOException, PropertyListFormatException, ParseException, ParserConfigurationException, SAXException {
        String escrowProxyUrl = mobileMe.get("com.apple.Dataclass.KeychainSync", "escrowProxyUrl");
        EscrowProxyRequestFactory escrowProxy
                = new EscrowProxyRequestFactory(accountInfo.dsPrsID(), tokens.get(Token.MMEAUTHTOKEN), escrowProxyUrl);

//        return srp(httpClient, escrowProxy);
        return test(httpClient, escrowProxy);
    }

    static ServiceKeySet test(HttpClient httpClient, EscrowProxyRequestFactory escrowProxy) throws IOException {
        SRPGetRecords records = getRecords(httpClient, escrowProxy);
        // We'll do the last step first, so we can abort if only a few attempts remain rather than risk further 
        // depleting them.
        int remainingAttempts = records.remainingAttempts();
        logger.debug("-- getRecords() - remaining attempts: {}", remainingAttempts);
        if (remainingAttempts < MIN_REMAINING_ATTEMPTS) {
            throw new IllegalStateException("srp minimum remaining attempts threshold exceeded: " + remainingAttempts);
        };

        SRPClient srp = SRPFactory.rfc5054(new SecureRandom());
        SRPInitResponse srpInit = srpInit(httpClient, escrowProxy, srp);

        NSDictionary recover = recover(httpClient, escrowProxy, srp, srpInit);
        BlobA6 blob = blob(recover);

        byte[] key = verify(srp, blob);

        ServiceKeySet decryptM2 = decryptM2(blob, key);

        ServiceKeySet b = b(records, decryptM2);

        return b;
    }

    static SRPGetRecords getRecords(HttpClient httpClient, EscrowProxyRequestFactory factory) throws IOException {
        /* 
        EscrowService SRP-6a exchanges: GETRECORDS
         */
        HttpUriRequest recordsRequest = factory.getRecords();
        NSDictionary records = httpClient.execute(recordsRequest, RESPONSE_HANDLER);
        logger.debug("-- getRecords() - GETRECORDS response: {}", records.toASCIIPropertyList());

        return new SRPGetRecords(records);
    }

    static SRPInitResponse
            srpInit(HttpClient httpClient, EscrowProxyRequestFactory factory, SRPClient srp) throws IOException {
        /* 
         EscrowService SRP-6a exchanges: SRP_INIT
        
         Randomly generated ephemeral key A presented to escrow server along with id (mmeAuthToken).
         Server returns amongst other things a salt and an ephemeral key B.
         */
        byte[] ephemeralKeyA = srp.generateClientCredentials();

        HttpUriRequest srpInitRequest = factory.srpInit(ephemeralKeyA);
        NSDictionary srpInit = httpClient.execute(srpInitRequest, RESPONSE_HANDLER);
        logger.debug("-- srpInit() - SRP_INIT response: {}", srpInit.toASCIIPropertyList());

        return new SRPInitResponse(srpInit);
    }

    static NSDictionary recover(
            HttpClient httpClient,
            EscrowProxyRequestFactory factory,
            SRPClient srp,
            SRPInitResponse response
    ) throws IOException {
        /* 
         EscrowService SRP-6a exchanges: RECOVER
        
         CRITICAL step. Generate M1 verification message. Failure here will deplete attempts (we have 10 attempts max).
         Server will abort on an invalid M1 or present us with, amongst other things, M2 which we can verify (or not).
         */
        byte[] dsid = response.dsid().getBytes(StandardCharsets.UTF_8);

        byte[] m1 = srp.calculateClientEvidenceMessage(response.salt(), dsid, dsid, response.ephemeralKeyB())
                .orElseThrow(() -> new IllegalStateException("bad SRP ephemeral key received from server"));
        logger.debug("-- recover() - m1: 0x{}", Hex.toHexString(m1));

        HttpUriRequest recoverRequest = factory.recover(m1, response.uid(), response.tag());
        NSDictionary recover = httpClient.execute(recoverRequest, RESPONSE_HANDLER);
        logger.debug("-- recover() - srpInit response: {}", recover.toASCIIPropertyList());

        return recover;
    }

    static byte[] verify(SRPClient srp, BlobA6 blob) {
        byte[] key = srp.verifyServerEvidenceMessage(blob.m2())
                .orElseThrow(() -> new IllegalStateException("failed to verify SRP m2"));
        logger.debug("-- verify() - session key: {}", Hex.toHexString(key));
        return key;
    }

    static BlobA6 blob(NSDictionary recover) {
        byte[] recoverRespBlob = Base64.getDecoder()
                .decode(PLists.getAs(recover, "respBlob", NSString.class).getContent());
        return new BlobA6(ByteBuffer.wrap(recoverRespBlob));
    }

    static ServiceKeySet decryptM2(BlobA6 blob, byte[] key) {
        /*
            EscrowService decrypt M2 key set.
            
            BlobA6 contains amongst other things, salt, iv and encrypted key set data.
         */
        byte[] backupBagPassword = decryptRecoveryResponseBlob(blob, key);

        Optional<ServiceKeySet> optionalServiceKeySet = DERUtils.parse(backupBagPassword, KeySet::new)
                .flatMap(ServiceKeySetBuilder::build);

        if (!optionalServiceKeySet.isPresent()) {
            throw new IllegalStateException("SRP failed to recover key set");
        }
        ServiceKeySet keySet = optionalServiceKeySet.get();
        return keySet;
    }

    static ServiceKeySet b(SRPGetRecords records, ServiceKeySet keySet) {
        /* 
         EscrowService SRP-6a exchanges: decrypt escrowed keys
        
         Verify the server M2 message (or not). Either way we have a session key and an encrypted key set.
         */
        Optional<byte[]> pcsRecordMetadata = records.metadata("com.apple.protectedcloudstorage.record")
                .map(SRPGetRecordsMetadata::metadata)
                .map(s -> Base64.getDecoder().decode(s));

        if (!pcsRecordMetadata.isPresent()) {
            logger.warn("-- main() - unable to locate 'com.apple.protectedcloudstorage.record' metadata");
            System.exit(-1);
        }

        Optional<ServiceKeySet> optionalEscrowServiceKeySet
                = decryptGetRecordsResponseMetadata(pcsRecordMetadata.get(), keySet::key);
        if (!optionalEscrowServiceKeySet.isPresent()) {
            logger.warn("-- main() - failed to retrieve escrow key set");
            System.exit(-1);
        }
        ServiceKeySet escrowServiceKeySet = optionalEscrowServiceKeySet.get();
        return escrowServiceKeySet;
    }

    public static byte[] decryptRecoveryResponseBlob(BlobA6 blob, byte[] key) {
        logger.debug("-- decryptRecoverResponseBlob() - response blob: {}", blob);

        byte[] pcsData = AESCBC.decryptAESCBC(key, blob.iv(), blob.data());
        logger.debug("-- decryptRecoverResponseBlob() - pcs data: 0x{}", Hex.toHexString(pcsData));

        BlobA0 pcsBlob = new BlobA0(ByteBuffer.wrap(pcsData));
        logger.debug("-- decryptRecoverResponseBlob() - pcs blob: {}", pcsBlob);

        byte[] derivedKey
                = PBKDF2.generate(new SHA256Digest(), pcsBlob.dsid(), pcsBlob.salt(), pcsBlob.iterations(), 16 * 8);
        logger.debug("-- decryptRecoverResponseBlob() - derived key: 0x{}", Hex.toHexString(derivedKey));

        byte[] saltIV = Arrays.copyOf(pcsBlob.salt(), 16);
        logger.debug("-- decryptRecoverResponseBlob() - salt/ iv: 0x{}", Hex.toHexString(saltIV));

        byte[] dictionaryData = AESCBC.decryptAESCBC(derivedKey, saltIV, pcsBlob.data());
        logger.debug("-- decryptRecoverResponseBlob() - dictionary data: 0x{}", Hex.toHexString(dictionaryData));

        NSDictionary dictionary = PLists.parseDictionary(dictionaryData);
        logger.debug("-- decryptRecoverResponseBlob() - dictionary: {}", dictionary.toXMLPropertyList());

        byte[] backupBagPassword = PLists.getAs(dictionary, "BackupBagPassword", NSData.class).bytes();
        logger.debug("-- decryptRecoverResponseBlob() - BackupBagPassword: 0x{}", Hex.toHexString(backupBagPassword));

        Optional<NSData> backupKeybagDigest = PLists.optionalAs(dictionary, "BackupKeybagDigest", NSData.class);
        if (!backupKeybagDigest.isPresent()) {
            logger.debug("-- decryptRecoverResponseBlob() - no backup keybag digest");
            return backupBagPassword;
        }
        byte[] checksum = backupKeybagDigest.get().bytes();
        logger.debug("-- decryptRecoverResponseBlob() - BackupKeybagDigest: 0x{}", Hex.toHexString(checksum));

        SHA1Digest digest = new SHA1Digest();
        byte[] out = new byte[digest.getDigestSize()];
        digest.update(backupBagPassword, 0, backupBagPassword.length);
        digest.doFinal(out, 0);
        logger.debug("-- decryptRecoverResponseBlob() - calculated digest: 0x{}", Hex.toHexString(out));
        logger.debug("-- decryptRecoverResponseBlob() - digests match: {}", Arrays.equals(checksum, out));
        return backupBagPassword;
    }

    // getRecords response > metadatalist > 'com.apple.protectedcloudstorage.record' > metadata    
    public static Optional<ServiceKeySet> decryptGetRecordsResponseMetadata(
            byte[] metadata, Function<KeyID, Optional<Key<ECPrivate>>> getMasterKey) {

        NSDictionary dictionary = PLists.parseDictionary(metadata);
        logger.debug("-- decryptGetRecordsResponseMetadata() - dictionary: {}", dictionary.toXMLPropertyList());

        byte[] backupKeybagDigest = PLists.getAs(dictionary, "BackupKeybagDigest", NSData.class).bytes();
        logger.debug("-- decryptGetRecordsResponseMetadata() - BackupKeybagDigest: 0x{}", Hex.toHexString(backupKeybagDigest));

        Optional<NSString> timestamp = PLists.optionalAs(dictionary, "com.apple.securebackup.timestamp", NSString.class);
        logger.debug("-- decryptGetRecordsResponseMetadata() - com.apple.securebackup.timestamp: {}",
                timestamp.map(NSString::getContent));

        NSDictionary clientMetadata = PLists.getAs(dictionary, "ClientMetadata", NSDictionary.class);

        NSDictionary secureBackupiCloudDataProtection
                = PLists.getAs(clientMetadata, "SecureBackupiCloudDataProtection", NSDictionary.class);

        byte[] secureBackupiCloudIdentityPublicData
                = PLists.getAs(clientMetadata, "SecureBackupiCloudIdentityPublicData", NSData.class).bytes();

        Optional<PublicKeyInfo> optionalPublicKeyInfo
                = DERUtils.parse(secureBackupiCloudIdentityPublicData, PublicKeyInfo::new);
        logger.debug("-- decryptGetRecordsResponseMetadata() - publicKeyInfo: {}", optionalPublicKeyInfo);

        byte[] kPCSMetadataEscrowedKeys
                = PLists.getAs(secureBackupiCloudDataProtection, "kPCSMetadataEscrowedKeys", NSData.class).bytes();

        Optional<BackupEscrow> optionalBackupEscrow = DERUtils.parse(kPCSMetadataEscrowedKeys, BackupEscrow::new);
        logger.debug("-- decryptGetRecordsResponseMetadata() - backupEscrow: {}", optionalBackupEscrow);

        // Defaults = SECPR1/ NIST curves.
        // Import keys based on length.
        IntFunction<Optional<String>> fieldLengthToCurveName = ECAssistant.fieldLengthToCurveName(ECurves.defaults());
        Function<byte[], Optional<Key<ECPublic>>> importPublicKey
                = KeyImport.importPublicKey(fieldLengthToCurveName, true);

        Optional<KeyID> masterKeyID = optionalBackupEscrow
                .map(BackupEscrow::masterKeyPublic)
                .flatMap(importPublicKey)
                .map(Key::keyID);
        logger.debug("-- decryptGetRecordsResponseMetadata() - master key id: {}", masterKeyID);

        // Take master key d value if we have it in the key set.
        Optional<BigInteger> optionalMasterKeyD = masterKeyID.flatMap(getMasterKey::apply)
                .map(Key::keyData)
                .map(ECPrivate::d);
        if (!optionalMasterKeyD.isPresent()) {
            logger.warn("-- decryptGetRecordsResponseMetadata() - unable to locate master key");
            return Optional.empty();
        }
        BigInteger masterKeyD = optionalMasterKeyD.get();

        logger.debug("-- decryptGetRecordsResponseMetadata() - master key d: 0x{}", masterKeyD.toString(16));

        if (!optionalBackupEscrow.isPresent()) {
            logger.warn("-- decryptGetRecordsResponseMetadata() - unable to locate backup escrow");
            return Optional.empty();
        }
        BackupEscrow backupEscrow = optionalBackupEscrow.get();

        byte[] key = XUnwrapData.instance().apply(backupEscrow.wrappedKey(), masterKeyD);
        logger.debug("-- decryptGetRecordsResponseMetadata() - key: 0x{}", Hex.toHexString(key));

        byte[] decrypted = GCMDataB.decrypt(key, backupEscrow.data());
        logger.debug("-- decryptGetRecordsResponseMetadata() - decrypted escrow data: 0x{}",
                Hex.toHexString(decrypted));

        return DERUtils.parse(decrypted, KeySet::new)
                .flatMap(ServiceKeySetBuilder::build);
    }

}
// TODO srp records
