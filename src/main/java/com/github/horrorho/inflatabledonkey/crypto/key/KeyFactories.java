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

import com.github.horrorho.inflatabledonkey.crypto.ec.key.ECPrivateKey;
import com.github.horrorho.inflatabledonkey.crypto.ec.key.ECPublicKey;
import com.github.horrorho.inflatabledonkey.crypto.ec.key.exports.ECKeyExport;
import com.github.horrorho.inflatabledonkey.crypto.ec.key.exports.ECPublicKeyExportCompact;
import com.github.horrorho.inflatabledonkey.crypto.ec.key.exports.ECPublicKeyExportX963;
import com.github.horrorho.inflatabledonkey.data.der.PublicKeyInfo;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Key factories.
 *
 * @author Ahseya
 */
@Immutable
public final class KeyFactories {

    private static final Logger logger = LoggerFactory.getLogger(KeyFactories.class);

    static byte[] publicExportData(ECPublicKey key, boolean isCompact) {
        ECKeyExport<ECPublicKey> export = isCompact
                ? ECPublicKeyExportCompact.instance()
                : ECPublicKeyExportX963.instance();

        return export.exportKey(key);
    }

    static void checkPublicExportData(byte[] publicExportData, Optional<PublicKeyInfo> publicKeyInfo) {
        if (publicKeyInfo.isPresent()) {
            byte[] publicInfoExportedKeyData = publicKeyInfo.get().key();

            if (Arrays.areEqual(publicExportData, publicInfoExportedKeyData)) {
                logger.debug("-- checkPublicExportData() - public export data match: {}",
                        Hex.toHexString(publicExportData));
            } else {
                logger.warn("-- checkPublicExportData() - public export data mismatch, supplied: {}, public key info: {}",
                        Hex.toHexString(publicExportData), Hex.toHexString(publicInfoExportedKeyData));
            }
        }
    }

    public static KeyFactory<ECPublicKey> createKeyECPublic() {
        return (ecPublicKey, publicKeyInfo, isCompact) -> {

            byte[] publicExportData = publicExportData(ecPublicKey, isCompact);
            checkPublicExportData(publicExportData, publicKeyInfo);

            return new Key<>(KeyID.of(publicExportData), ecPublicKey, publicExportData, publicKeyInfo, isCompact);
        };
    }

    public static KeyFactory<ECPrivateKey> createKeyECPrivate() {
        return (ECPrivateKey ecPrivateKey, Optional<PublicKeyInfo> publicKeyInfo, boolean isCompact) -> {

            byte[] publicExportData = publicExportData(ecPrivateKey.publicKey(), isCompact);
            checkPublicExportData(publicExportData, publicKeyInfo);

            return new Key<>(KeyID.of(publicExportData), ecPrivateKey, publicExportData, publicKeyInfo, isCompact);
        };
    }
}
