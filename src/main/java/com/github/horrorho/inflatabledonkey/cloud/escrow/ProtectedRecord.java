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
package com.github.horrorho.inflatabledonkey.cloud.escrow;

import com.dd.plist.NSData;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSString;
import com.github.horrorho.inflatabledonkey.crypto.GCMDataB;
import com.github.horrorho.inflatabledonkey.crypto.ec.key.ECPrivateKey;
import com.github.horrorho.inflatabledonkey.crypto.ec.key.ECPublicKey;
import com.github.horrorho.inflatabledonkey.crypto.ec.ECAssistant;
import com.github.horrorho.inflatabledonkey.crypto.ec.ECurves;
import com.github.horrorho.inflatabledonkey.pcs.key.Key;
import com.github.horrorho.inflatabledonkey.pcs.key.KeyID;
import com.github.horrorho.inflatabledonkey.data.der.BackupEscrow;
import com.github.horrorho.inflatabledonkey.data.der.DERUtils;
import com.github.horrorho.inflatabledonkey.data.der.PublicKeyInfo;
import com.github.horrorho.inflatabledonkey.pcs.key.imports.KeyImports;
import com.github.horrorho.inflatabledonkey.pcs.zone.PZDataUnwrap;
import com.github.horrorho.inflatabledonkey.util.PListsLegacy;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import javax.annotation.concurrent.Immutable;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class ProtectedRecord {

    private static final Logger logger = LoggerFactory.getLogger(ProtectedRecord.class);

    // TODO tidy
    public static byte[] unlockData(
            byte[] metadata, Function<KeyID, Optional<Key<ECPrivateKey>>> getMasterKey) {

        NSDictionary dictionary = PListsLegacy.parseDictionary(metadata);

        byte[] escrowedKeys = kPCSMetadataEscrowedKeys(dictionary);

        BackupEscrow backupEscrow = backupEscrow(escrowedKeys);

        BigInteger d = d(backupEscrow, getMasterKey);

        byte[] key = PZDataUnwrap.instance().apply(backupEscrow.wrappedKey(), d);

        return GCMDataB.decrypt(key, backupEscrow.data());
    }

    static byte[] kPCSMetadataEscrowedKeys(NSDictionary metadata) {
        NSDictionary clientMetadata = PListsLegacy.getAs(metadata, "ClientMetadata", NSDictionary.class);

        NSDictionary secureBackupiCloudDataProtection
                = PListsLegacy.getAs(clientMetadata, "SecureBackupiCloudDataProtection", NSDictionary.class);

        return PListsLegacy.getAs(secureBackupiCloudDataProtection, "kPCSMetadataEscrowedKeys", NSData.class).bytes();
    }

    static BackupEscrow backupEscrow(byte[] kPCSMetadataEscrowedKeys) {
        BackupEscrow backupEscrow = DERUtils.parse(kPCSMetadataEscrowedKeys, BackupEscrow::new)
                .orElseThrow(() -> new IllegalArgumentException("failed to parse escrowed keys"));
        logger.debug("-- backupEscrow() - backupEscrow: {}", backupEscrow);
        return backupEscrow;
    }

    static BigInteger
            d(BackupEscrow backupEscrow, Function<KeyID, Optional<Key<ECPrivateKey>>> getMasterKey) {

        Optional<KeyID> masterKeyID = importPublicKey(backupEscrow.masterKeyPublic())
                .map(Key::keyID);
        logger.debug("-- d) - master key id: {}", masterKeyID);

        // Take master key d value if we have it in the key set.
        BigInteger d = masterKeyID.flatMap(getMasterKey::apply)
                .map(Key::keyData)
                .map(ECPrivateKey::d)
                .orElseThrow(() -> new IllegalArgumentException("no master key for escrowed record"));
        logger.debug("-- d() - master key d: 0x{}", d.toString(16));

        return d;
    }

    static Optional<Key<ECPublicKey>> importPublicKey(byte[] keyData) {
        // Defaults = SECPR1/ NIST curves.
        IntFunction<Optional<String>> fieldLengthToCurveName = ECAssistant.fieldLengthToCurveName(ECurves.defaults());

        // Import keys based on length.
        // TODO non-partial application form
        return KeyImports.importPublicKey(fieldLengthToCurveName, true)
                .apply(keyData);
    }

    static void diagnostic(byte[] metadata) {
        NSDictionary dictionary = PListsLegacy.parseDictionary(metadata);
        logger.debug("-- diagnostic() - dictionary: {}", dictionary.toXMLPropertyList());

        byte[] backupKeybagDigest = PListsLegacy.getAs(dictionary, "BackupKeybagDigest", NSData.class).bytes();
        logger.debug("-- diagnostic() - BackupKeybagDigest: 0x{}", Hex.toHexString(backupKeybagDigest));

        Optional<NSString> timestamp = PListsLegacy.optionalAs(dictionary, "com.apple.securebackup.timestamp", NSString.class);
        logger.debug("-- diagnostic() - com.apple.securebackup.timestamp: {}",
                timestamp.map(NSString::getContent));

        NSDictionary clientMetadata = PListsLegacy.getAs(dictionary, "ClientMetadata", NSDictionary.class);

        NSDictionary secureBackupiCloudDataProtection
                = PListsLegacy.getAs(clientMetadata, "SecureBackupiCloudDataProtection", NSDictionary.class);

        byte[] secureBackupiCloudIdentityPublicData
                = PListsLegacy.getAs(clientMetadata, "SecureBackupiCloudIdentityPublicData", NSData.class).bytes();

        Optional<PublicKeyInfo> optionalPublicKeyInfo
                = DERUtils.parse(secureBackupiCloudIdentityPublicData, PublicKeyInfo::new);
        logger.debug("-- diagnostic() - publicKeyInfo: {}", optionalPublicKeyInfo);

        byte[] kPCSMetadataEscrowedKeys
                = PListsLegacy.getAs(secureBackupiCloudDataProtection, "kPCSMetadataEscrowedKeys", NSData.class).bytes();
        logger.debug("-- diagnostic() - kPCSMetadataEscrowedKeys: 0x{}", Hex.toHexString(kPCSMetadataEscrowedKeys));
    }
}
