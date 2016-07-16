///* 
// * The MIT License
// *
// * Copyright 2016 Ahseya.
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy
// * of this software and associated documentation files (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in
// * all copies or substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// * THE SOFTWARE.
// */
//package com.github.horrorho.inflatabledonkey.pcs.xfile;
//
//import com.github.horrorho.inflatabledonkey.crypto.RFC3394Wrap;
//import com.github.horrorho.inflatabledonkey.crypto.Curve25519;
//import com.github.horrorho.inflatabledonkey.keybag.KeyBag;
//import java.nio.BufferUnderflowException;
//import java.nio.ByteBuffer;
//import java.util.Optional;
//import java.util.function.Function;
//import net.jcip.annotations.Immutable;
//import org.bouncycastle.crypto.digests.SHA256Digest;
//import org.bouncycastle.util.Arrays;
//import org.bouncycastle.util.encoders.Hex;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * FileKeyAssistant.
// *
// * @author Ahseya
// */
//@Deprecated
//@Immutable
//public final class FileKeyAssistantLegacy {
//
//    private static final Logger logger = LoggerFactory.getLogger(FileKeyAssistantLegacy.class);
//
//    public static Optional<byte[]> uuid(byte[] fileKey) {
//        return fileKey.length < 0x10
//                ? Optional.empty()
//                : Optional.of(Arrays.copyOfRange(fileKey, 0, 0x10));
//    }
//
//    public static Optional<byte[]> unwrap(Function<byte[], Optional<KeyBag>> keyBags, int protectionClass, byte[] fileKey) {
//        return uuid(fileKey)
//                .flatMap(keyBags::apply)
//                .flatMap(keyBag -> unwrap(keyBag, protectionClass, fileKey));
//    }
//
//    public static Optional<byte[]> unwrap(KeyBag keyBag, int protectionClass, byte[] fileKey) {
//        Optional<byte[]> optionalPublicKey = keyBag.publicKey(protectionClass);
//        Optional<byte[]> optionalPrivateKey = keyBag.privateKey(protectionClass);
//        Optional<byte[]> uuid = uuid(fileKey);
//
//        boolean uuidMatch = uuid.map(u -> Arrays.areEqual(keyBag.uuid(), u))
//                .orElse(false);
//
//        if (uuidMatch) {
//            logger.debug("-- unwrap() - positive uuid match fileKey: 0x{} keybag: 0x{}",
//                    uuid.map(Hex::toHexString), Hex.toHexString(keyBag.uuid()));
//        } else {
//            logger.warn("-- unwrap() - negative uuid match fileKey: 0x{} keybag: 0x{}",
//                    uuid.map(Hex::toHexString), Hex.toHexString(keyBag.uuid()));
//        }
//
//        return optionalPublicKey.isPresent() && optionalPrivateKey.isPresent()
//                ? unwrap(optionalPublicKey.get(), optionalPrivateKey.get(), protectionClass, fileKey)
//                : Optional.empty();
//    }
//
//    public static Optional<byte[]>
//            unwrap(byte[] myPublicKey, byte[] myPrivateKey, int protectionClass, byte[] fileKey) {
//
//        try {
//            logger.trace("<< unwrap() - wrapped key: 0x{} my public: 0x{} my private: 0x{} protection class: {}",
//                    Hex.toHexString(fileKey), Hex.toHexString(myPublicKey), Hex.toHexString(myPrivateKey), protectionClass);
//            // Version 2 support only.
//            ByteBuffer buffer = ByteBuffer.wrap(fileKey);
//
//            byte[] uuid = new byte[0x10];
//            buffer.get(uuid);
//
//            int u1 = buffer.getInt(); // ignored
//            int u2 = buffer.getInt(); // ignored
//            int pc = buffer.getInt();
//            int u3 = buffer.getInt(); // ignored
//            int length = buffer.getInt();
//
//            byte[] longKey = new byte[buffer.limit() - buffer.position()];
//            buffer.get(longKey);
//
//            if (longKey.length != length) {
//                logger.warn("-- unwrap() - incongruent key length");
//            }
//
//            if (pc != protectionClass) {
//                logger.warn("-- unwrap() - incongruent protection class");
//            }
//
//            logger.debug("-- unwrap() - u1: 0x{} u2: 0x{} u3: 0x{} pc: {} length: {} uuid: 0x{} long: 0x:{} ",
//                    Integer.toHexString(u1), Integer.toHexString(u2), Integer.toHexString(u3),
//                    pc, length, Hex.toHexString(uuid), Hex.toHexString(longKey));
//
//            Optional<byte[]> unwrapped = FileKeyAssistantLegacy.unwrap(myPublicKey, myPrivateKey, longKey);
//            logger.trace("<< unwrap() - unwrapped key: 0x{}", unwrapped.map(Hex::toHexString).orElse("NULL"));
//            return unwrapped;
//
//        } catch (BufferUnderflowException ex) {
//            logger.warn("-- unwrap() - BufferUnderflowException: {}", ex);
//            logger.trace("<< unwrap() - unwrapped key: {}", Optional.empty());
//            return Optional.empty();
//        }
//    }
//
//    public static Optional<byte[]> unwrap(byte[] myPublicKey, byte[] myPrivateKey, byte[] longKey) {
//        byte[] otherPublicKey = new byte[0x20];
//        byte[] wrappedKey = new byte[longKey.length - 0x20];
//
//        ByteBuffer buffer = ByteBuffer.wrap(longKey);
//        buffer.get(otherPublicKey);
//        buffer.get(wrappedKey);
//
//        return curve25519Unwrap(myPublicKey, myPrivateKey, otherPublicKey, wrappedKey);
//    }
//
//    public static Optional<byte[]> curve25519Unwrap(
//            byte[] myPublicKey,
//            byte[] myPrivateKey,
//            byte[] otherPublicKey,
//            byte[] wrappedKey) {
//
//        SHA256Digest sha256 = new SHA256Digest();
//
//        byte[] shared = Curve25519.agreement(otherPublicKey, myPrivateKey);
//        logger.debug("-- curve25519Unwrap() - shared agreement: 0x{}", Hex.toHexString(shared));
//
//        byte[] pad = new byte[]{0x00, 0x00, 0x00, 0x01};
//        byte[] hash = new byte[sha256.getDigestSize()];
//
//        sha256.reset();
//        sha256.update(pad, 0, pad.length);
//        sha256.update(shared, 0, shared.length);
//        sha256.update(otherPublicKey, 0, otherPublicKey.length);
//        sha256.update(myPublicKey, 0, myPublicKey.length);
//        sha256.doFinal(hash, 0);
//
//        logger.debug("-- curve25519Unwrap() - kek: {}", Hex.toHexString(hash));
//        return RFC3394Wrap.unwrapAES(hash, wrappedKey);
//    }
//}
//// TODO buffer underflow exceptions
