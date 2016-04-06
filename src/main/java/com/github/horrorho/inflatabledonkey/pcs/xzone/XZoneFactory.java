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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * XZoneFactory. Experimental proof of concept code.
 *
 * @author Ahseya
 */
@Immutable
public final class XZoneFactory {

    public static XZoneFactory instance() {
        return INSTANCE;
    }

    // TODO sort out this mess.
    private static final Logger logger = LoggerFactory.getLogger(XZoneFactory.class);

    private static final byte[] KEY_DERIVATION_KEY = Hex.decode("656E6372797074696F6E206B6579206B6579206D");
    private static final int DERIVED_KEY_LENGTH = 0x10;
    private static final boolean USE_COMPACT_KEYS = true;

    private static final IntFunction<Optional<String>> FIELD_LENGTH_TO_CURVE_NAME
            = ECAssistant.fieldLengthToCurveName(ECurves.defaults());

    private static final XZoneFactory INSTANCE = new XZoneFactory(
            XUnwrapData.instance(),
            data -> KeyImport.importPublicKey(FIELD_LENGTH_TO_CURVE_NAME, USE_COMPACT_KEYS).apply(data),
            kdk -> NISTKDF.ctrHMac(kdk, KEY_DERIVATION_KEY, SHA256Digest::new, DERIVED_KEY_LENGTH)
    );

    private final BiFunction<byte[], BigInteger, byte[]> unwrapData;
    private final Function<byte[], Optional<Key<ECPublic>>> importPublicKey;
    private final UnaryOperator<byte[]> keyDerivationFunction;

    public XZoneFactory(
            BiFunction<byte[], BigInteger, byte[]> unwrapData,
            Function<byte[], Optional<Key<ECPublic>>> importPublicKey,
            UnaryOperator<byte[]> keyDerivationFunction) {

        this.unwrapData = Objects.requireNonNull(unwrapData, "unwrapData");
        this.importPublicKey = Objects.requireNonNull(importPublicKey, "importPublicKey");
        this.keyDerivationFunction = Objects.requireNonNull(keyDerivationFunction, "keyDerivationFunction");
    }

    public Optional<XZone> create(byte[] protectionInfo, String protectionInfoTag, XKeySet xKeySet) {

        return DERUtils.parse(protectionInfo, ProtectionInfo::new)
                .flatMap(pi -> create(pi, protectionInfoTag, xKeySet));
    }

    public Optional<XZone> create(ProtectionInfo protectionInfo, String protectionInfoTag, XKeySet xKeySet) {
        Optional<byte[]> optionalKDK = kdk(protectionInfo, xKeySet);
        if (!optionalKDK.isPresent()) {
            return Optional.empty();
        }
        byte[] kdk = optionalKDK.get();

        byte[] dk = keyDerivationFunction.apply(kdk);

        Optional<byte[]> optionalData = protectionInfo.data();
        if (!optionalData.isPresent()) {
            return Optional.of(new XZone(protectionInfoTag, kdk, dk));
        }

        byte[] decrypted = GCMDataA.decrypt(dk, optionalData.get());

        Optional<ProtectionObject> optionalProtectionObject = DERUtils.parse(decrypted, ProtectionObject::new);
        if (!optionalProtectionObject.isPresent()) {
            return Optional.of(new XZone(protectionInfoTag, kdk, dk));
        }

        ProtectionObject protectionObject = optionalProtectionObject.get();

        Optional<Set<NOS>> optionalMasterKeySet = protectionObject.getMasterKeySet();
        if (!optionalMasterKeySet.isPresent()) {
            logger.debug("-- create() - no master key set;");
            return Optional.of(new XZone(protectionInfoTag, kdk, dk));
        }

        Collection<Key<ECPrivate>> privateKeys = privateKeys(optionalMasterKeySet.get());
        return Optional.of(new XZone(protectionInfoTag, kdk, dk, privateKeys));
    }

    Collection<Key<ECPrivate>> privateKeys(Set<NOS> masterKeySet) {
        List<Key<ECPrivate>> privateKeys = masterKeySet.stream()
                .map(this::privateKeys)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        logger.debug("-- privateKeys() - private keys: {}", privateKeys);
        return privateKeys;
    }

    Optional<Collection<Key<ECPrivate>>> privateKeys(NOS masterKey) {
        return DERUtils.parse(masterKey.key(), KeySet::new)
                .flatMap(ServiceKeySetBuilder::build)
                .map(ServiceKeySet::services)
                .map(Map::values);
    }

    Optional<byte[]> kdk(ProtectionInfo protectionInfo, XKeySet xKeySet) {
        Set<EncryptedKey> encryptedKeySet = protectionInfo.encryptedKeys().encryptedKeySet();
        logger.debug("-- kdk() - encrypted key set: {}", encryptedKeySet);

        if (encryptedKeySet.size() != 1) {
            logger.warn("-- kdk() - unexpected encrypted key set count: {}", encryptedKeySet.size());
        }

        Optional<byte[]> optionalKdk = encryptedKeySet.stream()
                .map(ek -> kdk(ek, xKeySet))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();

        if (!optionalKdk.isPresent()) {
            logger.warn("-- kdk() - no kdk recovered, protectionInfo: {} key set: {}", protectionInfo, xKeySet);
        }
        return optionalKdk;
    }

    Optional<byte[]> kdk(EncryptedKey encryptedKey, XKeySet xKeySet) {
        return importPublicKey.apply(encryptedKey.masterKey().key())
                .map(Key::keyID)
                .flatMap(xKeySet::key)
                .map(Key::keyData)
                .map(ECPrivate::d)
                .map(d -> unwrapData.apply(encryptedKey.wrappedKey(), d));
    }
}
