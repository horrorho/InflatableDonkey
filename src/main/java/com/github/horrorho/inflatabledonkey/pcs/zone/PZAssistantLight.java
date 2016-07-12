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

import com.github.horrorho.inflatabledonkey.crypto.RFC3394Wrap;
import com.github.horrorho.inflatabledonkey.crypto.RFC5869KDF;
import com.github.horrorho.inflatabledonkey.crypto.ec.ECAssistant;
import com.github.horrorho.inflatabledonkey.crypto.ec.ECurves;
import com.github.horrorho.inflatabledonkey.crypto.ec.key.ECPrivateKey;
import com.github.horrorho.inflatabledonkey.crypto.ec.key.ECPublicKey;
import com.github.horrorho.inflatabledonkey.crypto.ec.key.imports.ECPublicKeyImportCompact;
import com.github.horrorho.inflatabledonkey.crypto.key.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ProtectionInfoLight.
 *
 * @author Ahseya
 */
@Immutable
public final class PZAssistantLight {

    // D221163FBCAB1D23DD55:100165A0
    public static PZAssistantLight instance() {
        return INSTANCE;
    }

    private static final Logger logger = LoggerFactory.getLogger(PZAssistantLight.class);

    // TODO inject constants via Property
    private static final PZAssistantLight INSTANCE
            = new PZAssistantLight(
                    SHA256Digest::new,
                    ECurves.defaultCurve(),
                    Hex.decode("4561676C6544616E6365"),
                    0x10);

    private final Supplier<Digest> digestSupplier;
    private final String curveName;
    private final byte[] info;
    private final int curveFieldLength;
    private final int keyLength;

    public PZAssistantLight(
            Supplier<Digest> digestSupplier,
            String curveName,
            byte[] info,
            int keyLength) {

        this.digestSupplier = Objects.requireNonNull(digestSupplier, "digestSupplier");
        this.curveName = Objects.requireNonNull(curveName, "curveName");
        this.info = Arrays.copyOf(info, info.length);
        this.keyLength = keyLength;

        curveFieldLength = ECAssistant.fieldLength(curveName);
    }

    public Optional<byte[]> masterKey(byte[] protectionInfoData, Collection<Key<ECPrivateKey>> keys) {
        logger.trace("<< apply() - protectionInfo: 0x{} keys: {}", Hex.toHexString(protectionInfoData), keys);
        if (protectionInfoData[0] != -1) {
            throw new IllegalArgumentException("not a light object");
        }

        if (protectionInfoData.length < curveFieldLength + 3) {
            logger.warn("-- apply() - short light object: 0x{}", Hex.toHexString(protectionInfoData));
            return Optional.empty();
        }

        byte[] publicKey = Arrays.copyOfRange(protectionInfoData, 1, 1 + curveFieldLength);
        byte[] wrappedKey = Arrays.copyOfRange(protectionInfoData, 1 + curveFieldLength, protectionInfoData.length - 2);
        byte[] tag = Arrays.copyOfRange(protectionInfoData, protectionInfoData.length - 2, protectionInfoData.length);
        logger.debug("-- apply() - public key: 0x{}", Hex.toHexString(publicKey));
        logger.debug("-- apply() - wrapped key: 0x{}", Hex.toHexString(wrappedKey));
        logger.debug("-- apply() - tag: 0x{}", Hex.toHexString(tag));

        Optional<byte[]> masterKey = ECPublicKeyImportCompact.instance().importKey(curveName, publicKey)
                .flatMap(k -> unwrap(k, keys, wrappedKey));
        logger.trace(">> apply() - master key: 0x{}", masterKey.map(Hex::toHexString));
        return masterKey;
    }

    Optional<byte[]> unwrap(ECPublicKey otherPublicKey, Collection<Key<ECPrivateKey>> keys, byte[] wrappedKey) {
        Digest digest = digestSupplier.get();

        return keys.stream()
                .map(Key::keyData)
                .map(k -> unwrap(otherPublicKey, k, digest, wrappedKey))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    public Optional<byte[]> unwrap(ECPublicKey otherPublicKey, ECPrivateKey myPrivateKey, Digest digest, byte[] wrappedKey) {
        byte[] S = myPrivateKey.agreement(otherPublicKey);
        logger.debug("-- unwrap() - shared secret: 0x{}", Hex.toHexString(S));

        byte[] salt = myPrivateKey.publicKey().point().xEncoded();
        logger.debug("-- unwrap() - salt: 0x{}", Hex.toHexString(salt));

        byte[] dk = RFC5869KDF.apply(S, salt, info, SHA256Digest::new, keyLength);
        logger.debug("-- unwrap() - dk: 0x{}", Hex.toHexString(salt));

        Optional<byte[]> unwrapped = RFC3394Wrap.unwrapAES(dk, wrappedKey);
        logger.debug("-- unwrap() - unwrapped key: 0x{}", unwrapped.map(Hex::toHexString));

        return unwrapped;
    }
}
// TODO tag check
