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
import com.dd.plist.NSString;
import com.github.horrorho.inflatabledonkey.crypto.GCMDataB;
import com.github.horrorho.inflatabledonkey.crypto.eckey.ECAssistant;
import com.github.horrorho.inflatabledonkey.crypto.eckey.ECPrivate;
import com.github.horrorho.inflatabledonkey.crypto.eckey.ECPublic;
import com.github.horrorho.inflatabledonkey.crypto.eckey.ECurves;
import com.github.horrorho.inflatabledonkey.crypto.key.Key;
import com.github.horrorho.inflatabledonkey.crypto.key.KeyID;
import com.github.horrorho.inflatabledonkey.data.der.BackupEscrow;
import com.github.horrorho.inflatabledonkey.data.der.DERUtils;
import com.github.horrorho.inflatabledonkey.data.der.KeySet;
import com.github.horrorho.inflatabledonkey.data.der.PublicKeyInfo;
import com.github.horrorho.inflatabledonkey.pcs.service.ServiceKeySet;
import com.github.horrorho.inflatabledonkey.pcs.service.ServiceKeySetBuilder;
import com.github.horrorho.inflatabledonkey.pcs.xzone.KeyImport;
import com.github.horrorho.inflatabledonkey.pcs.xzone.XUnwrapData;
import com.github.horrorho.inflatabledonkey.util.PLists;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EscrowedRecords.
 *
 * @author Ahseya
 */
@Immutable
public final class EscrowedRecords {

    private static final Logger logger = LoggerFactory.getLogger(EscrowedRecords.class);

    public static Optional<ServiceKeySet> decrypt(
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
// TODO tidy