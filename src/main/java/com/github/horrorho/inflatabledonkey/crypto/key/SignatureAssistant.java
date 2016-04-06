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
package com.github.horrorho.inflatabledonkey.crypto.key;

import com.github.horrorho.inflatabledonkey.crypto.eckey.ECKey;
import com.github.horrorho.inflatabledonkey.data.der.DERUtils;
import com.github.horrorho.inflatabledonkey.data.der.ECDSASignature;
import com.github.horrorho.inflatabledonkey.data.der.PublicKeyInfo;
import com.github.horrorho.inflatabledonkey.data.der.Signature;
import java.io.IOException;
import java.util.Optional;
import net.jcip.annotations.Immutable;
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
        // TODO tidy
        if (key.isTrusted()) {
            logger.warn("-- verify() - re-verifying a trusted key");
        }

        if (key != masterKey && !masterKey.isTrusted()) {
            logger.warn("-- verify() - master key is not trusted: {}", masterKey);
            return false;
        }

        Optional<PublicKeyInfo> optionalPublicKeyInfo = key.publicKeyInfo();
        if (!optionalPublicKeyInfo.isPresent()) {
            logger.warn("-- verify() - no publicKeyInfo");
        }

        Optional<Signature> optionalSignature = optionalPublicKeyInfo.flatMap(PublicKeyInfo::signature);
        if (!optionalSignature.isPresent()) {
            logger.warn("-- verify() - no signature");
        }

        Signature signature = optionalSignature.get();

        KeyID keyID = KeyID.importKeyID(signature.signerKeyID());
        if (!keyID.equals(masterKey.keyID())) {
            logger.warn("-- verify() - key/ master key id mismatch: {} / {}", keyID, masterKey.keyID());
        }

        Optional<byte[]> optionalMessage = SignatureAssistant.createMessage(optionalPublicKeyInfo.get());
        if (!optionalMessage.isPresent()) {
            logger.warn("-- verify() - failed to create message");
            return false;
        }

        Optional<ECDSASignature> optionalECDSASignature = SignatureAssistant.ecdsaSignature(optionalPublicKeyInfo.get());
        if (!optionalECDSASignature.isPresent()) {
            logger.warn("-- verify() - failed to import ECDSA signature");
            return false;
        }

        byte[] message = optionalMessage.get();
        ECDSASignature ecdsaSignature = optionalECDSASignature.get();

        boolean isVerified = masterKey.keyData()
                .verifySignature(message, ecdsaSignature.r(), ecdsaSignature.s());

        logger.debug("-- verify() - verified: {} key: {} master:{}", isVerified, key, masterKey);

        return isVerified;
    }

    public static Optional<byte[]> createMessage(PublicKeyInfo signer) {
        // TODO cont[2] support
        try {
            Optional<Digest> optionalDigest = signer.signature()
                    .flatMap(SignatureAssistant::digestForSignature);

            if (!optionalDigest.isPresent()) {
                logger.warn("-- createMessage() - no digest for signer: {}", signer);
            }

            Digest digest = optionalDigest.get();

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
            logger.warn("-- createMessage() - IOException: ", ex);
            return Optional.empty();
        }
    }

    public static Optional<Digest> digestForSignature(Signature signature) {
        switch (signature.type()) {
            case 0x01:
                return Optional.ofNullable(new SHA256Digest());

            case 0x02:
                return Optional.ofNullable(new SHA512Digest());

            default:
                logger.warn("-- digestForSignature() - unsupported signature type: {}", signature);
                return Optional.empty();
        }
    }

    public static Optional<ECDSASignature> ecdsaSignature(PublicKeyInfo publicKeyInfo) {
        // TODO expand to cont[2] signature data?
        return publicKeyInfo.signature()
                .map(Signature::data)
                .flatMap(data -> DERUtils.parse(data, ECDSASignature::new));
    }
}
