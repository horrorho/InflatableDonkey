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
package com.github.horrorho.inflatabledonkey.crypto.srp;

import com.dd.plist.NSData;
import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListFormatException;
import com.dd.plist.PropertyListParser;
import com.github.horrorho.inflatabledonkey.crypto.PBKDF2;
import com.github.horrorho.inflatabledonkey.crypto.eckey.ECPrivate;
import com.github.horrorho.inflatabledonkey.crypto.key.Key;
import com.github.horrorho.inflatabledonkey.data.blob.BlobA0;
import com.github.horrorho.inflatabledonkey.data.blob.BlobA6;
import com.github.horrorho.inflatabledonkey.data.der.DERUtils;
import com.github.horrorho.inflatabledonkey.data.der.KeySet;
import com.github.horrorho.inflatabledonkey.pcs.service.ServiceKeySet;
import com.github.horrorho.inflatabledonkey.pcs.service.ServiceKeySetBuilder;
import com.github.horrorho.inflatabledonkey.pcs.xzone.XKeySet;
import com.github.horrorho.inflatabledonkey.pcs.xzone.XZoneFactory;
import com.github.horrorho.inflatabledonkey.pcs.xzone.XZones;
import com.github.horrorho.inflatabledonkey.util.PLists;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import javax.xml.parsers.ParserConfigurationException;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.xml.sax.SAXException;

/**
 *
 * @author Ahseya
 */
public class SRPTest {

    private static final Logger logger = LoggerFactory.getLogger(SRPTest.class);

    public static void test(BlobA6 blob, byte[] key) throws IOException, PropertyListFormatException, ParseException, ParserConfigurationException, SAXException {
        byte[] keySetData = decryptRecoverResponseBlob(blob, key);

        Optional<KeySet> optionalKeySet = DERUtils.parse(keySetData, KeySet::new);

        if (!optionalKeySet.isPresent()) {
            logger.error("-- test() - failed to acquire key set");
            System.exit(-1);
        }
 
        KeySet keySet = optionalKeySet.get();
        
        ServiceKeySet serviceKeySet = ServiceKeySetBuilder.builder().setKeySet(keySet).build().get();

        Collection<Key<ECPrivate>> keys = serviceKeySet.services().values();

        XZones zones = XZones.empty();
        XZoneFactory zoneFactory = XZoneFactory.instance();
        zones = zones.with(new XKeySet(keys));

    }

    public static byte[] decryptRecoverResponseBlob(BlobA6 blob, byte[] key) throws IOException, PropertyListFormatException, ParseException, ParserConfigurationException, SAXException {

        logger.debug("-- decryptRecoverResponseBlob() - response blob: {}", blob);

        byte[] pcsData = decryptAESCBC(key, blob.iv(), blob.data());
        logger.debug("-- decryptRecoverResponseBlob() - pcs data: 0x{}", Hex.toHexString(pcsData));

        BlobA0 pcsBlob = new BlobA0(ByteBuffer.wrap(pcsData));
        logger.debug("-- decryptRecoverResponseBlob() - pcs blob: {}", pcsBlob);

        byte[] derivedKey
                = PBKDF2.generate(new SHA256Digest(), pcsBlob.dsid(), pcsBlob.salt(), pcsBlob.iterations(), 16 * 8);
        logger.debug("-- decryptRecoverResponseBlob() - derived key: 0x{}", Hex.toHexString(derivedKey));

        byte[] saltIV = Arrays.copyOf(pcsBlob.salt(), 16);
        logger.debug("-- decryptRecoverResponseBlob() - salt/ iv: 0x{}", Hex.toHexString(saltIV));

        byte[] dictionaryData = decryptAESCBC(derivedKey, saltIV, pcsBlob.data());
        logger.debug("-- decryptRecoverResponseBlob() - dictionary data: 0x{}", Hex.toHexString(dictionaryData));

        NSDictionary dictionary = (NSDictionary) PropertyListParser.parse(dictionaryData);
        logger.debug("-- decryptRecoverResponseBlob() - dictionary: {}", dictionary.toXMLPropertyList());

        byte[] backupBagPassword = PLists.<NSData>get(dictionary, "BackupBagPassword").bytes();
        logger.debug("-- decryptRecoverResponseBlob() - BackupBagPassword: 0x{}", Hex.toHexString(backupBagPassword));

        byte[] backupKeybagDigest = PLists.<NSData>get(dictionary, "BackupKeybagDigest").bytes();
        logger.debug("-- decryptRecoverResponseBlob() - BackupKeybagDigest: 0x{}", Hex.toHexString(backupKeybagDigest));

        SHA1Digest digest = new SHA1Digest();
        byte[] out = new byte[digest.getDigestSize()];
        digest.update(backupBagPassword, 0, backupBagPassword.length);
        digest.doFinal(out, 0);
        logger.debug("-- decryptRecoverResponseBlob() - calculated digest: 0x{}", Hex.toHexString(out));
        logger.debug("-- decryptRecoverResponseBlob() - digests match: {}", Arrays.equals(backupKeybagDigest, out));

        return backupBagPassword;
    }

    static byte[] decryptAESCBC(byte[] key, byte[] iv, byte[] data) {
        // AES CBC PKCS7 decrypt
        try {
            CipherParameters cipherParameters = new ParametersWithIV(new KeyParameter(key), iv);
            PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESFastEngine()));
            cipher.init(false, cipherParameters);

            byte[] buffer = new byte[cipher.getOutputSize(data.length)];

            int pos = cipher.processBytes(data, 0, data.length, buffer, 0);
            pos += cipher.doFinal(buffer, pos);

            return Arrays.copyOf(buffer, pos);

        } catch (DataLengthException | IllegalStateException | InvalidCipherTextException ex) {
            throw new IllegalArgumentException("decrypt failed", ex);
        }
    }
}
