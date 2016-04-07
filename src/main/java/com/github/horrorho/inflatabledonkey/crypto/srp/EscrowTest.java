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
package com.github.horrorho.inflatabledonkey.crypto.srp;

import com.dd.plist.NSData;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSString;
import com.dd.plist.PropertyListFormatException;
import com.dd.plist.PropertyListParser;
import com.github.horrorho.inflatabledonkey.crypto.AESCBC;
import com.github.horrorho.inflatabledonkey.crypto.GCMDataB;
import com.github.horrorho.inflatabledonkey.crypto.PBKDF2;
import com.github.horrorho.inflatabledonkey.crypto.eckey.ECAssistant;
import com.github.horrorho.inflatabledonkey.crypto.eckey.ECPrivate;
import com.github.horrorho.inflatabledonkey.crypto.eckey.ECPublic;
import com.github.horrorho.inflatabledonkey.crypto.eckey.ECurves;
import com.github.horrorho.inflatabledonkey.crypto.key.Key;
import com.github.horrorho.inflatabledonkey.crypto.key.KeyID;
import com.github.horrorho.inflatabledonkey.data.blob.BlobA0;
import com.github.horrorho.inflatabledonkey.data.blob.BlobA6;
import com.github.horrorho.inflatabledonkey.data.der.BackupEscrow;
import com.github.horrorho.inflatabledonkey.data.der.DERUtils;
import com.github.horrorho.inflatabledonkey.data.der.KeySet;
import com.github.horrorho.inflatabledonkey.data.der.PublicKeyInfo;
import com.github.horrorho.inflatabledonkey.pcs.service.ServiceKeySet;
import com.github.horrorho.inflatabledonkey.pcs.service.ServiceKeySetBuilder;
import com.github.horrorho.inflatabledonkey.pcs.xzone.KeyImport;
import com.github.horrorho.inflatabledonkey.pcs.xzone.XKeySet;
import com.github.horrorho.inflatabledonkey.pcs.xzone.XUnwrapData;
import com.github.horrorho.inflatabledonkey.util.PLists;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import javax.xml.parsers.ParserConfigurationException;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author Ahseya
 */
public class EscrowTest {
    // Experimental testing class.

    private static final Logger logger = LoggerFactory.getLogger(EscrowTest.class);

//    public static void test(BlobA6 blob, byte[] key) throws IOException, PropertyListFormatException, ParseException, ParserConfigurationException, SAXException {
//        byte[] keySetData = decryptRecoveryResponseBlob(blob, key);
//
//        Optional<KeySet> optionalKeySet = DERUtils.parse(keySetData, KeySet::new);
//
//        if (!optionalKeySet.isPresent()) {
//            logger.error("-- test() - failed to acquire key set");
//            System.exit(-1);
//        }
//
//        KeySet keySet = optionalKeySet.get();
//
//        ServiceKeySet serviceKeySet = ServiceKeySetBuilder.builder().setKeySet(keySet).build().get();
//
//        Collection<Key<ECPrivate>> keys = serviceKeySet.services().values();
//
//        XZones zones = XZones.empty();
//        XZoneFactory zoneFactory = XZoneFactory.instance();
//        zones = zones.with(new XKeySet(keys));
//
//    }
    public static Optional<ServiceKeySet> decryptRecoveryResponseBlob(BlobA6 blob, byte[] key)
            throws IOException, PropertyListFormatException, ParseException, ParserConfigurationException, SAXException {

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

        NSDictionary dictionary = (NSDictionary) PropertyListParser.parse(dictionaryData);
        logger.debug("-- decryptRecoverResponseBlob() - dictionary: {}", dictionary.toXMLPropertyList());

        byte[] backupBagPassword = PLists.<NSData>get(dictionary, "BackupBagPassword").bytes();
        logger.debug("-- decryptRecoverResponseBlob() - BackupBagPassword: 0x{}", Hex.toHexString(backupBagPassword));

        byte[] backupKeybagDigest = PLists.<NSData>get(dictionary, "BackupKeybagDigest").bytes();
        logger.debug("-- decryptRecoverResponseBlob() - BackupKeybagDigest: 0x{}", Hex.toHexString(backupKeybagDigest));

        SHA1Digest digest = new SHA1Digest();
        byte[] out = new byte[digest.getDigestSize()];
        digest.update(backupBagPassword, 0, backupBagPassword.length);
        digest.doFinal(out, 0);
        logger.debug("-- decryptRecoverResponseBlob() - calculated digest: 0x{}", Hex.toHexString(out));
        logger.debug("-- decryptRecoverResponseBlob() - digests match: {}", Arrays.equals(backupKeybagDigest, out));

        return DERUtils.parse(backupBagPassword, KeySet::new)
                .flatMap(ServiceKeySetBuilder::build);
    }

    // getRecords response > metadatalist > 'com.apple.protectedcloudstorage.record' > metadata    
    public static Optional<ServiceKeySet> decryptGetRecordsResponseMetadata(
            byte[] metadata,
            Function<KeyID, Optional<Key<ECPrivate>>> getMasterKey
    ) throws IOException, PropertyListFormatException, ParseException, ParserConfigurationException, SAXException {

        NSDictionary dictionary = (NSDictionary) PropertyListParser.parse(metadata);
        logger.debug("-- decryptGetRecordsResponseMetadata() - dictionary: {}", dictionary.toXMLPropertyList());

        byte[] backupKeybagDigest = PLists.<NSData>get(dictionary, "BackupKeybagDigest").bytes();
        logger.debug("-- decryptGetRecordsResponseMetadata() - BackupKeybagDigest: 0x{}", Hex.toHexString(backupKeybagDigest));

        String timestamp = PLists.<NSString>get(dictionary, "com.apple.securebackup.timestamp").toString();
        logger.debug("-- decryptGetRecordsResponseMetadata() - com.apple.securebackup.timestamp: {}", timestamp);

        NSDictionary clientMetadata = PLists.<NSDictionary>get(dictionary, "ClientMetadata");

        NSDictionary secureBackupiCloudDataProtection
                = PLists.<NSDictionary>get(clientMetadata, "SecureBackupiCloudDataProtection");

        byte[] secureBackupiCloudIdentityPublicData
                = PLists.<NSData>get(clientMetadata, "SecureBackupiCloudIdentityPublicData").bytes();

        Optional<PublicKeyInfo> optionalPublicKeyInfo
                = DERUtils.parse(secureBackupiCloudIdentityPublicData, PublicKeyInfo::new);
        logger.debug("-- decryptGetRecordsResponseMetadata() - publicKeyInfo: {}", optionalPublicKeyInfo);

        byte[] kPCSMetadataEscrowedKeys
                = PLists.<NSData>get(secureBackupiCloudDataProtection, "kPCSMetadataEscrowedKeys").bytes();

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
