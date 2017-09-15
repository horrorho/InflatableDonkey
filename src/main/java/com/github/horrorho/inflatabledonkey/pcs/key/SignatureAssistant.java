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
package com.github.horrorho.inflatabledonkey.pcs.key;

import com.github.horrorho.inflatabledonkey.crypto.ec.ECCurvePoint;
import com.github.horrorho.inflatabledonkey.crypto.ec.key.ECKey;
import com.github.horrorho.inflatabledonkey.data.der.DERUtils;
import com.github.horrorho.inflatabledonkey.data.der.ECDSASignature;
import com.github.horrorho.inflatabledonkey.data.der.PublicKeyInfo;
import com.github.horrorho.inflatabledonkey.data.der.Signature;
import java.io.IOException;
import java.util.Optional;
import javax.annotation.concurrent.Immutable;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class SignatureAssistant {

    private static final Logger logger = LoggerFactory.getLogger(SignatureAssistant.class);

    public static boolean verify(Key<? extends ECKey> key, Key<? extends ECKey> masterKey) {
        boolean match = key.publicKeyInfo()
                .filter(publicKeyInfo -> keyIDMatch(publicKeyInfo, masterKey))
                .map(publicKeyInfo -> verify(publicKeyInfo, masterKey.keyData().point()))
                .orElseGet(() -> {
                    logger.warn("-- verify() - no public key info");
                    return false;
                });
        if (match) {
            logger.debug("-- verify() - passed key: {} masterKey: {}", key, masterKey);

        } else {
            logger.warn("-- verify() - failed key: {} masterKey: {}", key, masterKey);
        }
        return match;
    }

    static boolean verify(PublicKeyInfo publicKeyInfo, ECCurvePoint master) {
        return message(publicKeyInfo)
                .map(message -> verify(publicKeyInfo, message, master))
                .orElse(false);
    }

    static boolean verify(PublicKeyInfo publicKeyInfo, byte[] message, ECCurvePoint master) {
        return publicKeyInfo.signature()
                .map(Signature::data)
                .flatMap(data -> DERUtils.parse(data, ECDSASignature::new))
                .map(signature -> master.verifySignature(message, signature.r(), signature.s()))
                .orElse(false);
    }

    static boolean keyIDMatch(PublicKeyInfo publicKeyInfo, Key<? extends ECKey> masterKey) {
        return publicKeyInfo.signature()
                .map(Signature::signerKeyID)
                .map(KeyID::importKeyID)
                .map(keyID -> {
                    boolean match = keyID.equals(masterKey.keyID());
                    if (!match) {
                        logger.warn("-- keyIDMatch() - key/ master key id mismatch: {} / {}", keyID, masterKey.keyID());
                    }
                    return match;
                })
                .orElseGet(() -> {
                    logger.warn("-- keyIDMatch() - public key info has no signature");
                    return false;
                });
    }

    static Optional<byte[]> message(PublicKeyInfo signer) {
        return signer.signature()
                .flatMap(SignatureAssistant::digest)
                .flatMap(digest -> message(signer, digest));
    }

    static Optional<byte[]> message(PublicKeyInfo signer, Digest digest) {
        try {
            PublicKeyInfo info = new PublicKeyInfo(
                    signer.service(),
                    signer.type(),
                    signer.key(),
                    signer.buildAndTime(),
                    Optional.empty(),
                    Optional.empty());
            byte[] encoded = info.getEncoded();
            byte[] message = new byte[digest.getDigestSize()];
            digest.update(encoded, 0, encoded.length);
            digest.doFinal(message, 0);
            return Optional.ofNullable(message);

        } catch (IOException ex) {
            logger.warn("-- message() - IOException: ", ex);
            return Optional.empty();
        }
    }

    static Optional<Digest> digest(Signature signature) {
        switch (signature.type()) {
            case 0x01:
                return Optional.ofNullable(new SHA256Digest());
            case 0x02:
                return Optional.ofNullable(new SHA512Digest());
            default:
                logger.warn("-- digest() - unsupported signature type: {}", signature);
                return Optional.empty();
        }
    }
}
// TODO cont[2] support
