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
package com.github.horrorho.inflatabledonkey.pcs.xfile;

import com.github.horrorho.inflatabledonkey.crypto.RFC3394Wrap;
import com.github.horrorho.inflatabledonkey.crypto.Curve25519;
import com.github.horrorho.inflatabledonkey.keybag.KeyBag;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FileKeyAssistant.
 *
 * @author Ahseya
 */
@Immutable
public final class FileKeyAssistant {

    private static final Logger logger = LoggerFactory.getLogger(FileKeyAssistant.class);

    public static Optional<byte[]> unwrap(FileKeyMetaData fileMetaData, Function<byte[], Optional<KeyBag>> keyBags) {
        return keyBags.apply(fileMetaData.uuid())
                .map(keyBag -> unwrap(fileMetaData, keyBag))
                .orElseGet(() -> {
                    logger.warn("-- unwrap() - no keybag matching uuid: 0x{}", fileMetaData.uuid());
                    return Optional.empty();
                });
    }

    public static Optional<byte[]> unwrap(FileKeyMetaData fileMetaData, KeyBag keyBag) {
        if (Arrays.equals(keyBag.uuid(), fileMetaData.uuid())) {
            logger.debug("-- unwrap() - positive uuid match fileKey: 0x{} keybag: 0x{}",
                    Hex.toHexString(fileMetaData.uuid()), Hex.toHexString(keyBag.uuid()));
        } else {
            logger.warn("-- unwrap() - negative uuid match fileKey: 0x{} keybag: 0x{}",
                    Hex.toHexString(fileMetaData.uuid()), Hex.toHexString(keyBag.uuid()));
            return Optional.empty();
        }

        Optional<byte[]> publicKey = keyBag.publicKey(fileMetaData.protectionClass());
        if (!publicKey.isPresent()) {
            logger.warn("-- unwrap() - no public key for protection class: {}", fileMetaData.protectionClass());
            return Optional.empty();
        }

        Optional<byte[]> privateKey = keyBag.privateKey(fileMetaData.protectionClass());
        if (!privateKey.isPresent()) {
            logger.warn("-- unwrap() - no private key for protection class: {}", fileMetaData.protectionClass());
            return Optional.empty();
        }

        return curve25519Unwrap(publicKey.get(), privateKey.get(), fileMetaData.publicKey(), fileMetaData.wrappedKey());
    }

    public static Optional<byte[]> curve25519Unwrap(
            byte[] myPublicKey,
            byte[] myPrivateKey,
            byte[] otherPublicKey,
            byte[] wrappedKey) {

        SHA256Digest sha256 = new SHA256Digest();

        byte[] shared = Curve25519.agreement(otherPublicKey, myPrivateKey);
        logger.debug("-- curve25519Unwrap() - shared agreement: 0x{}", Hex.toHexString(shared));

        // Stripped down NIST SP 800-56A KDF.
        byte[] counter = new byte[]{0x00, 0x00, 0x00, 0x01};
        byte[] hash = new byte[sha256.getDigestSize()];

        sha256.reset();
        sha256.update(counter, 0, counter.length);
        sha256.update(shared, 0, shared.length);
        sha256.update(otherPublicKey, 0, otherPublicKey.length);
        sha256.update(myPublicKey, 0, myPublicKey.length);
        sha256.doFinal(hash, 0);

        logger.debug("-- curve25519Unwrap() - kek: {}", Hex.toHexString(hash));
        return RFC3394Wrap.unwrapAES(hash, wrappedKey);
    }
}
