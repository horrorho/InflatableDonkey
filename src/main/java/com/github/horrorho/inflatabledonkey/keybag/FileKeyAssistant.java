/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.keybag;

import com.github.horrorho.inflatabledonkey.crypto.AESWrap;
import com.github.horrorho.inflatabledonkey.crypto.Curve25519;
import java.nio.ByteBuffer;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.digests.SHA256Digest;
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

    public static Optional<byte[]> unwrap(KeyBag keyBag, int protectionClass, byte[] fileKey) {
        Optional<byte[]> optionalPublicKey = keyBag.publicKey(protectionClass);
        Optional<byte[]> optionalPrivateKey = keyBag.privateKey(protectionClass);

        return optionalPublicKey.isPresent() && optionalPrivateKey.isPresent()
                ? unwrap(optionalPublicKey.get(), optionalPrivateKey.get(), protectionClass, fileKey)
                : Optional.empty();
    }

    public static Optional<byte[]>
            unwrap(byte[] myPublicKey, byte[] myPrivateKey, int protectionClass, byte[] fileKey) {
        // Version 2.
        ByteBuffer buffer = ByteBuffer.wrap(fileKey);

        byte[] uid = new byte[0x10];
        buffer.get(uid);

        buffer.getInt(); // ignored
        buffer.getInt(); // ignored
        int pc = buffer.getInt();
        buffer.getInt(); // ignored
        int length = buffer.getInt();

        byte[] longKey = new byte[buffer.limit() - buffer.position()];
        buffer.get(longKey);

        if (longKey.length != length) {
            logger.warn("-- unwrap() - incongruent key length");
        }

        if (pc != protectionClass) {
            logger.warn("-- unwrap() - incongruent protection class");
        }

        return FileKeyAssistant.unwrap(myPublicKey, myPrivateKey, longKey);
    }

    public static Optional<byte[]> unwrap(byte[] myPublicKey, byte[] myPrivateKey, byte[] longKey) {
        byte[] otherPublicKey = new byte[0x20];
        byte[] wrappedKey = new byte[longKey.length - 0x20];

        ByteBuffer buffer = ByteBuffer.wrap(longKey);
        buffer.get(otherPublicKey);
        buffer.get(wrappedKey);

        return curve25519Unwrap(myPublicKey, myPrivateKey, otherPublicKey, wrappedKey);
    }

    public static Optional<byte[]> curve25519Unwrap(
            byte[] myPublicKey,
            byte[] myPrivateKey,
            byte[] otherPublicKey,
            byte[] wrappedKey) {

        SHA256Digest sha256 = new SHA256Digest();

        byte[] shared = Curve25519.agreement(otherPublicKey, myPrivateKey);
        byte[] pad = new byte[]{0x00, 0x00, 0x00, 0x01};
        byte[] hash = new byte[sha256.getDigestSize()];

        sha256.reset();
        sha256.update(pad, 0, pad.length);
        sha256.update(shared, 0, shared.length);
        sha256.update(otherPublicKey, 0, otherPublicKey.length);
        sha256.update(myPublicKey, 0, myPublicKey.length);
        sha256.doFinal(hash, 0);

        try {
            return Optional.of(AESWrap.unwrap(hash, wrappedKey));

        } catch (InvalidCipherTextException ex) {
            logger.warn("-- curve25519Unwrap() - InvalidCipherTextException: {}", ex);
            return Optional.empty();
        }
    }
}
