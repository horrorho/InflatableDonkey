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

import com.github.horrorho.inflatabledonkey.crypto.ec.key.ECPrivateKey;
import com.github.horrorho.inflatabledonkey.crypto.ec.ECAssistant;
import com.github.horrorho.inflatabledonkey.crypto.ec.ECurves;
import com.github.horrorho.inflatabledonkey.pcs.key.Key;
import com.github.horrorho.inflatabledonkey.pcs.key.KeyID;
import com.github.horrorho.inflatabledonkey.data.der.KeySet;
import com.github.horrorho.inflatabledonkey.data.der.PrivateKey;
import com.github.horrorho.inflatabledonkey.data.der.SignatureInfo;
import com.github.horrorho.inflatabledonkey.data.der.TypeData;
import com.github.horrorho.inflatabledonkey.pcs.key.imports.KeyImports;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.IntFunction;
import javax.annotation.concurrent.NotThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
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
    private final Map<Integer, Set<KeyID>> serviceKeyIDs = new HashMap<>();
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
        serviceKeyIDs.computeIfAbsent(service, u -> new HashSet<>()).add(keyID);
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

        // Import keys.
        Set<Key<ECPrivateKey>> keys = importVerifiedKeys();
        if (keys.isEmpty()) {
            logger.debug("-- build() - no keys");
            return Optional.empty();
        }

        // Build.      
        Map<Integer, Set<Key<ECPrivateKey>>> serviceKeys = ServiceKeySetAssistant.serviceKeys(keys);
        boolean isCompact = (flags & 0x01) == 0x01;
        ServiceKeySet serviceKeySet = new ServiceKeySet(serviceKeys, name, ksID, isCompact);

        // Debug/ warn output.
        debug(keys);

        // All done.
        return Optional.of(serviceKeySet);
    }

    Set<Key<ECPrivateKey>> importVerifiedKeys() {
        Set<Key<ECPrivateKey>> privateKeys = ServiceKeySetAssistant.importPrivateKeys(keys, importPrivateKey());
        return ServiceKeySetAssistant.verifiedPrivateKeys(privateKeys);
    }

    Function<PrivateKey, Optional<Key<ECPrivateKey>>> importPrivateKey() {
        return KeyImports.importPrivateKey(fieldLengthToCurveName, useCompactKeys);
    }

    void debug(Set<Key<ECPrivateKey>> keys) {
        if (!logger.isDebugEnabled()) {
            return;
        }
        logger.debug("-- debug() - name: {}", name);
        logger.debug("-- debug() - ksID: {}", ksID);
        logger.debug("-- debug() - flags: {}", flags);
        serviceKeyIDs.forEach((k, v)
                -> logger.debug("-- debug() - service: {} key id: {} name: {} ", k, v, Service.service(k)));

        keys.forEach(u -> logger.debug("-- debug() - key id: {} trusted: {} public export: 0x{}",
                u.keyID(), u.isTrusted(), Hex.toHexString(u.exportPublicData())));

        Map<Integer, Key<ECPrivateKey>> incongruentKeys
                = ServiceKeySetAssistant.incongruentServiceKeys(keys, serviceKeyIDs);
        logger.debug("-- debug() - incongruent service keys: {}", incongruentKeys);
    }
}
