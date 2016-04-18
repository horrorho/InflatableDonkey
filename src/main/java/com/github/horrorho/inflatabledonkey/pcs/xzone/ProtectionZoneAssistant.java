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
package com.github.horrorho.inflatabledonkey.pcs.xzone;

import com.github.horrorho.inflatabledonkey.crypto.GCMDataA;
import com.github.horrorho.inflatabledonkey.crypto.NISTKDF;
import com.github.horrorho.inflatabledonkey.crypto.eckey.ECAssistant;
import com.github.horrorho.inflatabledonkey.crypto.eckey.ECPrivate;
import com.github.horrorho.inflatabledonkey.crypto.eckey.ECPublic;
import com.github.horrorho.inflatabledonkey.crypto.eckey.ECurves;
import com.github.horrorho.inflatabledonkey.crypto.key.Key;
import com.github.horrorho.inflatabledonkey.crypto.key.KeyID;
import com.github.horrorho.inflatabledonkey.data.der.DERUtils;
import com.github.horrorho.inflatabledonkey.data.der.EncryptedKey;
import com.github.horrorho.inflatabledonkey.data.der.KeySet;
import com.github.horrorho.inflatabledonkey.data.der.NOS;
import com.github.horrorho.inflatabledonkey.data.der.ProtectionInfo;
import com.github.horrorho.inflatabledonkey.data.der.ProtectionObject;
import com.github.horrorho.inflatabledonkey.pcs.service.ServiceKeySet;
import com.github.horrorho.inflatabledonkey.pcs.service.ServiceKeySetBuilder;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZoneKeysAssistant.
 *
 * @author Ahseya
 */
@Immutable
public final class ProtectionZoneAssistant {

    // Superfluous methods left in intentionally to demonstrate data flow, although not necessarily efficient.
    private static final Logger logger = LoggerFactory.getLogger(ProtectionZoneAssistant.class);

    // TODO inject via Property
    private static final byte[] KEY_DERIVATION_KEY = Hex.decode("656E6372797074696F6E206B6579206B6579206D");
    private static final int DERIVED_KEY_LENGTH = 0x10;
    private static final boolean USE_COMPACT_KEYS = true;

    private static final IntFunction<Optional<String>> FIELD_LENGTH_TO_CURVE_NAME
            = ECAssistant.fieldLengthToCurveName(ECurves.defaults());

    static public Map<KeyID, Key<ECPrivate>> masterKeys(ProtectionInfo protectionInfo, Map<KeyID, Key<ECPrivate>> keys) {
        return derivedKey(protectionInfo, keys)
                .map(dk -> masterKeys(protectionInfo, dk))
                .orElse(new HashMap<>());
    }

    static public Map<KeyID, Key<ECPrivate>> masterKeys(ProtectionInfo protectionInfo, byte[] dk) {
        return protectionInfo.data()
                .map(bs -> masterKeys(bs, dk))
                .orElse(new HashMap<>());
    }

    static public Map<KeyID, Key<ECPrivate>> masterKeys(byte[] protectionInfoData, byte[] dk) {
        byte[] decrypted = GCMDataA.decrypt(dk, protectionInfoData);

        return DERUtils.parse(decrypted, ProtectionObject::new)
                .flatMap(ProtectionObject::getMasterKeySet)
                .map(ProtectionZoneAssistant::masterKeys)
                .orElse(new HashMap<>());
    }

    static Map<KeyID, Key<ECPrivate>> masterKeys(Set<NOS> masterKeySet) {
        Map<KeyID, Key<ECPrivate>> privateKeys = masterKeySet.stream()
                .map(ProtectionZoneAssistant::masterKeys)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(
                        Key::keyID,
                        Function.identity()));
        logger.debug("-- privateKeys() - private keys: {}", privateKeys);
        return privateKeys;
    }

    static Optional<Collection<Key<ECPrivate>>> masterKeys(NOS masterKey) {
        return DERUtils.parse(masterKey.key(), KeySet::new)
                .flatMap(ServiceKeySetBuilder::build)
                .map(ServiceKeySet::services)
                .map(Map::values);
    }

    static Optional< byte[]> derivedKey(ProtectionInfo protectionInfo, Map<KeyID, Key<ECPrivate>> keys) {
        return keyDerivationKey(protectionInfo, keys)
                .map(ProtectionZoneAssistant::derivedKey);
    }

    static byte[] derivedKey(byte[] keyDerivationKey) {
        return NISTKDF.ctrHMac(keyDerivationKey, KEY_DERIVATION_KEY, SHA256Digest::new, DERIVED_KEY_LENGTH);
    }

    static Optional<byte[]> keyDerivationKey(ProtectionInfo protectionInfo, Map<KeyID, Key<ECPrivate>> keys) {
        Set<EncryptedKey> encryptedKeySet = protectionInfo.encryptedKeys().encryptedKeySet();
        logger.debug("-- keyDerivationkey() - encrypted key set: {}", encryptedKeySet);

        return keyDerivationkey(encryptedKeySet, keys);
    }

    static Optional<byte[]> keyDerivationkey(Set<EncryptedKey> encryptedKeySet, Map<KeyID, Key<ECPrivate>> keys) {
        if (encryptedKeySet.size() != 1) {
            logger.warn("-- keyDerivationkey() - unexpected encrypted key count: {}", encryptedKeySet.size());
        }

        return encryptedKeySet.stream()
                .map(ek -> keyDerivationKey(ek, keys))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    static Optional<byte[]> keyDerivationKey(EncryptedKey encryptedKey, Map<KeyID, Key<ECPrivate>> keys) {
        return importPublicKey(encryptedKey.masterKey().key())
                .map(Key::keyID)
                .map(keys::get)
                .map(Key::keyData)
                .map(ECPrivate::d)
                .map(d -> unwrapData(encryptedKey.wrappedKey(), d));
    }

    static Optional<Key<ECPublic>> importPublicKey(byte[] keyData) {
        return KeyImport.importPublicKey(FIELD_LENGTH_TO_CURVE_NAME, USE_COMPACT_KEYS)
                .apply(keyData);
    }

    static Optional<ProtectionInfo> importProtectionInfo(byte[] data) {
        return DERUtils.parse(data, ProtectionInfo::new);
    }

    static byte[] unwrapData(byte[] data, BigInteger d) {
        return XUnwrapData.instance().apply(data, d);
    }
}
