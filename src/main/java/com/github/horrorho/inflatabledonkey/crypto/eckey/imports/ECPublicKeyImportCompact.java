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
package com.github.horrorho.inflatabledonkey.crypto.eckey.imports;

import com.github.horrorho.inflatabledonkey.crypto.eckey.ECAssistant;
import com.github.horrorho.inflatabledonkey.crypto.eckey.ECKeys;
import com.github.horrorho.inflatabledonkey.crypto.eckey.ECPublic;
import com.github.horrorho.inflatabledonkey.crypto.ecpoint.ECPointsCompact;
import java.math.BigInteger;
import net.jcip.annotations.Immutable;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.util.BigIntegers;

/**
 * ECPublicKeyImportCompact. Format: data = x(compact)
 *
 * @author Ahseya
 */
@Immutable
public final class ECPublicKeyImportCompact implements ECKeyImport<ECPublic> {

    public static ECPublicKeyImportCompact instance() {
        return INSTANCE;
    }

    private static final ECPublicKeyImportCompact INSTANCE
            = new ECPublicKeyImportCompact();

    private ECPublicKeyImportCompact() {
    }

    @Override
    public ECPublic importKey(String curveName, byte[] data) {
        X9ECParameters x9ECParameters = ECAssistant.x9ECParameters(curveName);
        int fieldLength = ECAssistant.fieldLength(x9ECParameters);

        ECKeyImportAssistant.checkDataLength(data, this::fieldLength, fieldLength);

        BigInteger x = BigIntegers.fromUnsignedByteArray(data);
        BigInteger y = ECPointsCompact.y(x9ECParameters.getCurve(), x);

        return ECKeys.publicKeyFactory()
                .createECPublicKey(x, y, curveName, x9ECParameters);
    }

    @Override
    public int fieldLength(int dataLength) {
        return dataLength > 0
                ? dataLength
                : -1;
    }
}
