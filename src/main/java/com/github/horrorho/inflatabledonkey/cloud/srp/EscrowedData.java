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
package com.github.horrorho.inflatabledonkey.cloud.srp;

import com.dd.plist.NSData;
import com.dd.plist.NSDictionary;
import com.github.horrorho.inflatabledonkey.crypto.AESCBC;
import com.github.horrorho.inflatabledonkey.crypto.PBKDF2;
import com.github.horrorho.inflatabledonkey.data.blob.BlobA0;
import com.github.horrorho.inflatabledonkey.data.blob.BlobA6;
import com.github.horrorho.inflatabledonkey.util.PLists;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EscrowedData.
 *
 * @author Ahseya
 */
@Immutable
public final class EscrowedData {

    private static final Logger logger = LoggerFactory.getLogger(EscrowedData.class);

    @Deprecated
    public static byte[] decryptLegacy(BlobA6 blob, byte[] key) {
        logger.debug("-- decryptRecoverResponseBlob() - response blob: {}", blob);

        byte[] pcsData = AESCBC.decryptAESCBC(key, blob.iv(), blob.data());
        logger.debug("-- decryptRecoverResponseBlob() - pcs data: 0x{}", Hex.toHexString(pcsData));

        BlobA0 pcsBlob = new BlobA0(ByteBuffer.wrap(pcsData));
        logger.debug("-- decryptRecoverResponseBlob() - pcs blob: {}", pcsBlob);

        byte[] derivedKey
                = PBKDF2.generate(new SHA256Digest(), pcsBlob.dsid(), pcsBlob.salt(), pcsBlob.iterations(), 16 * 8);
        logger.debug("-- decryptRecoverResponseBlob() - derived key: 0x{}", Hex.toHexString(derivedKey));

        byte[] saltIV = Arrays.copyOf(pcsBlob.salt(), 0x10);
        logger.debug("-- decryptRecoverResponseBlob() - salt/ iv: 0x{}", Hex.toHexString(saltIV));

        byte[] dictionaryData = AESCBC.decryptAESCBC(derivedKey, saltIV, pcsBlob.data());
        logger.debug("-- decryptRecoverResponseBlob() - dictionary data: 0x{}", Hex.toHexString(dictionaryData));

        NSDictionary dictionary = PLists.parseDictionary(dictionaryData);
        logger.debug("-- decryptRecoverResponseBlob() - dictionary: {}", dictionary.toXMLPropertyList());

        byte[] backupBagPassword = PLists.getAs(dictionary, "BackupBagPassword", NSData.class).bytes();
        logger.debug("-- decryptRecoverResponseBlob() - BackupBagPassword: 0x{}", Hex.toHexString(backupBagPassword));

        Optional<NSData> backupKeybagDigest = PLists.optionalAs(dictionary, "BackupKeybagDigest", NSData.class);
        if (!backupKeybagDigest.isPresent()) {
            logger.debug("-- decryptRecoverResponseBlob() - no backup keybag digest");
            return backupBagPassword;
        }
        byte[] checksum = backupKeybagDigest.get().bytes();
        logger.debug("-- decryptRecoverResponseBlob() - BackupKeybagDigest: 0x{}", Hex.toHexString(checksum));

        SHA1Digest digest = new SHA1Digest();
        byte[] out = new byte[digest.getDigestSize()];
        digest.update(backupBagPassword, 0, backupBagPassword.length);
        digest.doFinal(out, 0);
        logger.debug("-- decryptRecoverResponseBlob() - calculated digest: 0x{}", Hex.toHexString(out));
        logger.debug("-- decryptRecoverResponseBlob() - digests match: {}", Arrays.equals(checksum, out));
        return backupBagPassword;
    }

    public static NSDictionary decrypt(BlobA6 blob, byte[] key) {
        logger.debug("-- decrypt() - response blob: {}", blob);

        byte[] pcsData = AESCBC.decryptAESCBC(key, blob.iv(), blob.data());
        logger.debug("-- decrypt() - pcs data: 0x{}", Hex.toHexString(pcsData));

        BlobA0 pcsBlob = new BlobA0(ByteBuffer.wrap(pcsData));
        logger.debug("-- decrypt() - pcs blob: {}", pcsBlob);

        byte[] derivedKey
                = PBKDF2.generate(new SHA256Digest(), pcsBlob.dsid(), pcsBlob.salt(), pcsBlob.iterations(), 16 * 8);
        logger.debug("-- decrypt() - derived key: 0x{}", Hex.toHexString(derivedKey));

        byte[] saltIV = Arrays.copyOf(pcsBlob.salt(), 0x10);
        logger.debug("-- decrypt() - salt/ iv: 0x{}", Hex.toHexString(saltIV));

        byte[] dictionaryData = AESCBC.decryptAESCBC(derivedKey, saltIV, pcsBlob.data());
        logger.debug("-- decrypt() - dictionary data: 0x{}", Hex.toHexString(dictionaryData));

        NSDictionary dictionary = PLists.parseDictionary(dictionaryData);
        logger.debug("-- decrypt() - dictionary: {}", dictionary.toXMLPropertyList());
        return dictionary;
    } 
}
// TODO
