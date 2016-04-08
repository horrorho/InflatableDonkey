/*
 * The MIT License
 *
 * Copyright 2015 Ahseya.
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
package com.github.horrorho.inflatabledonkey.keybag;

import com.github.horrorho.inflatabledonkey.crypto.AESWrap;
import com.github.horrorho.inflatabledonkey.crypto.PBKDF2;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * KeyBagFactory.
 *
 * @author Ahseya
 */
@Immutable
public final class KeyBagFactory {

    private static final Logger logger = LoggerFactory.getLogger(KeyBagFactory.class);

    private static final int WRAP_DEVICE = 1;
    private static final int WRAP_PASSCODE = 2;

    private static final int KEK_BITLENGTH = 256;

    public static KeyBag create(byte[] data, byte[] passcode) {
        return create(TagLengthValue.parse(data), passcode);
    }

    public static KeyBag create(List<TagLengthValue> list, byte[] passCode) {
        if (list.size() < 3) {
            throw new IllegalArgumentException("list too small");
        }

        Iterator<TagLengthValue> iterator = list.iterator();
        TagLengthValue version = iterator.next();
        if (!version.tag().equals("VERS") || integer(version.value()) != 3) {
            logger.warn("-- create() - unexpected VERS: {}", version);
        }

        TagLengthValue type = iterator.next();
        if (!type.tag().equals("TYPE")) {
            logger.warn("-- create() - unexpected TYPE: {}", type);
        }

        KeyBagType keyBagType = KeyBagType.from(integer(type.value()));
        if (keyBagType != KeyBagType.BACKUP && keyBagType != KeyBagType.OTA) {
            throw new IllegalArgumentException("not a backup keybag");
        }

        List<Map<String, byte[]>> blocks = KeyBagFactory.block(iterator);

        Map<String, byte[]> header = blocks.get(0);
        byte[] uuid = header.get("UUID");
//        byte[] hmck = header.get("HMCK");
//        byte[] wrap = header.get("WRAP");
        byte[] salt = header.get("SALT");
        byte[] iter = header.get("ITER");

        byte[] kek = kek(passCode, salt, iter);

        Map<Integer, byte[]> publicKeys = new HashMap<>();
        Map<Integer, byte[]> privateKeys = new HashMap<>();

        blocks.forEach(b -> unlockBlock(kek, b, privateKeys, publicKeys));

        publicKeys.forEach((c, k) -> logger.debug("-- create() - protection class: {} public key: 0x{}",
                c, Hex.toHexString(k)));
        privateKeys.forEach((c, k) -> logger.debug("-- create() - protection class: {} private key: 0x{}",
                c, Hex.toHexString(k)));

        return new KeyBag(keyBagType, uuid, publicKeys, privateKeys);
    }

    static List<Map<String, byte[]>> block(Iterator<TagLengthValue> iterator) {
        TagLengthValue uuid = iterator.next();
        if (!uuid.tag().equals("UUID")) {
            throw new IllegalArgumentException("bad format");
        }

        List<Map<String, byte[]>> blocks = new ArrayList<>();
        block(uuid, iterator, blocks);
        return blocks;
    }

    static void block(
            TagLengthValue uuid,
            Iterator<TagLengthValue> iterator,
            List<Map<String, byte[]>> blocks) {

        Map<String, byte[]> tagValues = new HashMap<>();
        tagValues.put(uuid.tag(), uuid.value());

        while (iterator.hasNext()) {
            TagLengthValue tlv = iterator.next();

            if (tlv.tag().equals("UUID")) {
                blocks.add(tagValues);
                block(tlv, iterator, blocks);
                return;
            }

            tagValues.put(tlv.tag(), tlv.value());
        }
        blocks.add(tagValues);
    }

    static byte[] kek(byte[] passCode, byte[] salt, byte[] iterations) {
        byte[] kek = PBKDF2.generate(passCode, salt, integer(iterations), KEK_BITLENGTH);
        logger.debug("-- kek() - kek: 0x{}", Hex.toHexString(kek));
        return kek;
    }

    static void unlockBlock(
            byte[] kek,
            Map<String, byte[]> block,
            Map<Integer, byte[]> privateKeys,
            Map<Integer, byte[]> publicKeys) {

        if (!block.containsKey("CLAS")) {
            return;
        }

        int wrap = integer(block.get("WRAP"));
        int clas = integer(block.get("CLAS"));
        byte[] wpky = block.get("WPKY");
        byte[] pbky = block.get("PBKY");

        unwrap(wrap, kek, wpky)
                .map(key -> privateKeys.put(clas, key));

        publicKeys.put(clas, pbky);
    }

    static Optional<byte[]> unwrap(int wrap, byte[] kek, byte[] wpky) {
        if ((wrap & WRAP_DEVICE) != 0 || (wrap & WRAP_PASSCODE) == 0) {
            return Optional.empty();
        }

        try {
            byte[] key = AESWrap.unwrap(kek, wpky);
            logger.debug("-- unwrap() - unwrap kek: 0x{} wpky: 0x{} > key: 0x{}",
                    Hex.toHexString(kek), Hex.toHexString(wpky), Hex.toHexString(key));
            return Optional.of(key);

        } catch (InvalidCipherTextException ex) {
            logger.warn("-- unwrap() - failed unwrap kek: 0x{} wpky: 0x{}",
                    Hex.toHexString(kek), Hex.toHexString(wpky));
            return Optional.empty();
        }
    }

    static int integer(byte[] bytes) {
        return new BigInteger(bytes).intValueExact();
    }
}
