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
package com.github.horrorho.inflatabledonkey.pcs.zone;

import com.github.horrorho.inflatabledonkey.crypto.ec.key.ECPrivateKey;
import com.github.horrorho.inflatabledonkey.pcs.key.Key;
import com.github.horrorho.inflatabledonkey.pcs.key.KeyID;
import com.github.horrorho.inflatabledonkey.data.der.DERUtils;
import com.github.horrorho.inflatabledonkey.data.der.ProtectionInfo;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ProtectionZoneFactory.
 *
 * @author Ahseya
 */
@Immutable
public final class PZFactory {

    public static PZFactory instance() {
        return INSTANCE;
    }

    private static final Logger logger = LoggerFactory.getLogger(PZFactory.class);

    private static final PZFactory INSTANCE
            = new PZFactory(
                    PZKeyDerivationFunction.instance(),
                    PZKeyUnwrap.instance(),
                    PZAssistant.instance(),
                    PZAssistantLight.instance());

    private final PZKeyDerivationFunction kdf;
    private final PZKeyUnwrap unwrapKey;
    private final PZAssistant assistant;
    private final PZAssistantLight assistantLight;

    public PZFactory(
            PZKeyDerivationFunction kdf,
            PZKeyUnwrap unwrapKey,
            PZAssistant assistant,
            PZAssistantLight assistantLight) {

        this.kdf = Objects.requireNonNull(kdf, "kdf");
        this.unwrapKey = Objects.requireNonNull(unwrapKey, "unwrapKey");
        this.assistant = Objects.requireNonNull(assistant, "assistant");
        this.assistantLight = Objects.requireNonNull(assistantLight, "assistantLight");
    }

    public ProtectionZone create(Collection<Key<ECPrivateKey>> keys) {
        return new ProtectionZone(unwrapKey, Optional.empty(), Optional.empty(), keys(keys), "");
    }

    public Optional<ProtectionZone> create(ProtectionZone base, CloudKit.ProtectionInfo protectionInfo) {
        return protectionInfo.hasProtectionInfo() && protectionInfo.hasProtectionInfoTag()
                ? create(
                        base.keys(),
                        protectionInfo.getProtectionInfoTag(),
                        protectionInfo.getProtectionInfo().toByteArray())
                : Optional.empty();
    }

    Optional<ProtectionZone>
            create(LinkedHashMap<KeyID, Key<ECPrivateKey>> keys, String protectionInfoTag, byte[] protectionInfoData) {

        if (protectionInfoData.length == 0) {
            logger.warn("-- create() - empty protectionInfo");
            return Optional.empty();
        }
        return protectionInfoData[0] == -1
                ? Optional.of(protectionZoneLight(keys, protectionInfoTag, protectionInfoData))
                : protectionZoneDER(keys, protectionInfoTag, protectionInfoData);
    }

    Optional<ProtectionZone> protectionZoneDER(
            LinkedHashMap<KeyID, Key<ECPrivateKey>> keys,
            String protectionInfoTag,
            byte[] protectionInfo) {

        return DERUtils.parse(protectionInfo, ProtectionInfo::new)
                .map(pi -> protectionZone(keys, protectionInfoTag, pi));
    }

    ProtectionZone protectionZone(
            LinkedHashMap<KeyID, Key<ECPrivateKey>> keys,
            String protectionInfoTag,
            ProtectionInfo protectionInfo) {

        Optional<byte[]> masterKey = assistant.masterKey(protectionInfo, keys);

        Optional<byte[]> decryptKey = masterKey.map(kdf::apply);

        LinkedHashMap newKeys = decryptKey.map(key -> assistant.keys(protectionInfo, key))
                .map(this::keys)
                .orElse(new LinkedHashMap());
        newKeys.putAll(keys);

        return new ProtectionZone(unwrapKey, masterKey, decryptKey, newKeys, protectionInfoTag);
    }

    ProtectionZone protectionZoneLight(
            LinkedHashMap<KeyID, Key<ECPrivateKey>> keys,
            String protectionInfoTag,
            byte[] protectionInfoData) {

        Optional<byte[]> masterKey = assistantLight.masterKey(protectionInfoData, keys.values());

        Optional<byte[]> decryptKey = masterKey.map(kdf::apply);

        return new ProtectionZone(unwrapKey, masterKey, decryptKey, keys, protectionInfoTag);
    }

    LinkedHashMap<KeyID, Key<ECPrivateKey>> keys(Collection<Key<ECPrivateKey>> keys) {
        return keys.stream()
                .collect(Collectors.toMap(
                        Key::keyID,
                        Function.identity(),
                        (a, b) -> {
                            logger.warn("-- keys() - collision: {} {}", a, b);
                            return a;
                        },
                        LinkedHashMap::new));
    }
}
