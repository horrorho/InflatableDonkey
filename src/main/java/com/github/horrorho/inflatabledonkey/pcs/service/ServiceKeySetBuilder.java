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
package com.github.horrorho.inflatabledonkey.pcs.service;

import com.github.horrorho.inflatabledonkey.crypto.eckey.ECAssistant;
import com.github.horrorho.inflatabledonkey.crypto.eckey.ECPrivate;
import com.github.horrorho.inflatabledonkey.crypto.eckey.ECurves;
import com.github.horrorho.inflatabledonkey.crypto.key.Key;
import com.github.horrorho.inflatabledonkey.crypto.key.KeyID;
import com.github.horrorho.inflatabledonkey.data.der.KeySet;
import com.github.horrorho.inflatabledonkey.data.der.PrivateKey;
import com.github.horrorho.inflatabledonkey.data.der.SignatureInfo;
import com.github.horrorho.inflatabledonkey.data.der.TypeData;
import com.github.horrorho.inflatabledonkey.pcs.xzone.KeyImport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.IntFunction;
import net.jcip.annotations.NotThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Objects;
import org.bouncycastle.util.encoders.Hex;

/**
 * ServiceKeySetBuilder.
 *
 * @author Ahseya
 */
@NotThreadSafe
public final class ServiceKeySetBuilder {

    public static ServiceKeySetBuilder builder() {
        return new ServiceKeySetBuilder();
    }

    public static Optional<ServiceKeySet> build(KeySet keySet) {
        return builder().setKeySet(keySet).build();
    }

    private static final Logger logger = LoggerFactory.getLogger(ServiceKeySetBuilder.class);

    private static final IntFunction<Optional<String>> SECPR1 = ECAssistant.fieldLengthToCurveName(ECurves.secpr1());
    private static final boolean USE_COMPACT_KEYS = true;

    private IntFunction<Optional<String>> fieldLengthToCurveName = SECPR1;
    private boolean useCompactKeys = USE_COMPACT_KEYS;
    private final Map<Integer, KeyID> serviceKeyIDs = new HashMap<>();
    private final List<PrivateKey> keys = new ArrayList<>();
    private SignatureInfo signature;    // TODO
    private String ksID = "";
    private String name = "";
    private int flags = 0;

    public ServiceKeySetBuilder setCurveFunction(IntFunction<Optional<String>> fieldLengthToCurveName) {
        this.fieldLengthToCurveName = Objects.requireNonNull(fieldLengthToCurveName, "fieldLengthToCurveName");
        return this;
    }

    public ServiceKeySetBuilder setUseCompactKeys(boolean useCompactKeys) {
        this.useCompactKeys = useCompactKeys;
        return this;
    }

    public ServiceKeySetBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ServiceKeySetBuilder addPrivateKeys(Collection<PrivateKey> keys) {
        keys.forEach(this::addPrivateKey);
        return this;
    }

    public ServiceKeySetBuilder addPrivateKey(PrivateKey key) {
        keys.add(key);
        return this;
    }

    public ServiceKeySetBuilder addServiceKeys(Collection<TypeData> serviceKeys) {
        serviceKeys.forEach(this::addServiceKey);
        return this;
    }

    public ServiceKeySetBuilder addServiceKey(TypeData serviceKey) {
        addServiceKey(serviceKey.type(), KeyID.importKeyID(serviceKey.data()));
        return this;
    }

    public ServiceKeySetBuilder addServiceKey(int service, KeyID keyID) {
        serviceKeyIDs.put(service, keyID);
        return this;
    }

    public ServiceKeySetBuilder setChecksum(byte[] checksum) {
        setKsID(Hex.toHexString(checksum));
        return this;
    }

    public ServiceKeySetBuilder setKsID(String ksID) {
        this.ksID = ksID;
        return this;
    }

    public ServiceKeySetBuilder setFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public ServiceKeySetBuilder setSignature(SignatureInfo signature) {
        this.signature = signature;
        return this;
    }

    public ServiceKeySetBuilder setKeySet(KeySet keySet) {
        setName(keySet.name());
        addPrivateKeys(keySet.keys());
        addServiceKeys(keySet.serviceKeyIDs());
        setChecksum(keySet.checksum());
        setFlags(keySet.flags().orElse(0));
        setSignature(keySet.signatureInfo().orElse(null));

        return this;
    }

    public Optional<ServiceKeySet> build() {
        // TODO signatureInfo verification.
        // TODO flags isCompact management/ anomalies

        boolean isCompact = (flags & 0x01) == 0x01;

        // Import keys.
        Map<KeyID, Key<ECPrivate>> privateKeys
                = ServiceKeySetAssistant.importPrivateKeys(
                        keys,
                        KeyImport.importPrivateKey(fieldLengthToCurveName, useCompactKeys));

        // If a single key with service 0/ null return basic Identity.
        if (privateKeys.size() == 1) {
            Key<ECPrivate> key = privateKeys.values().iterator().next();
            if (key.service().map(s -> s == 0).orElse(true)) {

                Map<Integer, Key<ECPrivate>> serviceKeys = new HashMap<>();
                serviceKeys.put(0, key);
                ServiceKeySet serviceKeySet = new ServiceKeySet(serviceKeys, name, ksID, isCompact);
                return Optional.of(serviceKeySet);
            }
        }

        Optional<Key<ECPrivate>> masterKeyOptional
                = ServiceKeySetAssistant.keyForService(privateKeys.values(), Service.MASTER.number());
        if (!masterKeyOptional.isPresent()) {
            logger.warn("-- build() - master key not found: {}");
            return Optional.empty();
        }

        // Key verification.
        Key<ECPrivate> masterKey = masterKeyOptional.get().selfVerify();
        privateKeys.putAll(ServiceKeySetAssistant.verifyKeys(privateKeys.values(), masterKey));

        List<KeyID> untrustedKeys = ServiceKeySetAssistant.untrustedKeys(privateKeys.values());
        if (!untrustedKeys.isEmpty()) {
            logger.warn("-- build() - untrusted keys: {}", untrustedKeys);
        }

        // Service to Key.
        Map<Integer, Key<ECPrivate>> serviceKeys = ServiceKeySetAssistant.serviceKeys(serviceKeyIDs, privateKeys);

        // Test for but leave in unreferenced keys.
        List<Key<ECPrivate>> unreferencedKeys = ServiceKeySetAssistant.unreferencedKeys(serviceKeyIDs, privateKeys);

        // Test for but leave in incongruent keys (for now).
        Map<Integer, Key<ECPrivate>> incongruentKeys = ServiceKeySetAssistant.incongruentKeys(serviceKeys);

        // Debug information.
        logger.debug("-- build() - name: {}", name);
        logger.debug("-- build() - ksID: {}", ksID);
        logger.debug("-- build() - flags: {}", flags);
        logger.debug("-- build() - key: {}", masterKey.keyID());
        logger.debug("-- build() - unreferenced keys: {}", unreferencedKeys);
        logger.debug("-- build() - incongruent keys: {}", incongruentKeys);

        serviceKeyIDs.forEach((service, keyID) -> logger.debug("-- build() - service: {} key id: {}", service, keyID));

        privateKeys.forEach((keyId, key) -> logger.debug("-- build() - key id: {} trusted: {} public export: 0x{}",
                keyId, key.isTrusted(), Hex.toHexString(key.publicExportData())));

        // Build.
        ServiceKeySet serviceKeySet = new ServiceKeySet(serviceKeys, name, ksID, isCompact);
        return Optional.ofNullable(serviceKeySet);
    }
}
