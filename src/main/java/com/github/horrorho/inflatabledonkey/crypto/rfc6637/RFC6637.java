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
package com.github.horrorho.inflatabledonkey.crypto.rfc6637;

import com.github.horrorho.inflatabledonkey.crypto.ec.ECPointsCompact;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;
import javax.annotation.concurrent.Immutable;
import org.bouncycastle.asn1.x9.ECNamedCurveTable;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * *
 * Modified RFC6637 unwrap with compact key support.
 *
 * @see
 * <a href="https://www.ietf.org/archive/id/draft-jivsov-ecc-compact-05.txt">https://www.ietf.org/archive/id/draft-jivsov-ecc-compact-05.txt</a>
 * @author Ahseya
 */
@Immutable
public final class RFC6637 {

    private static final Logger logger = LoggerFactory.getLogger(RFC6637.class);

    private final Supplier<Wrapper> wrapperFactory;
    private final String curveName;
    private final int symAlgIDKeyLength;
    private final RFC6637KDF kdf;

    public RFC6637(
            Supplier<Wrapper> wrapperFactory,
            String curveName,
            int symAlgIDKeyLength,
            RFC6637KDF kdf
    ) throws IOException {

        this.wrapperFactory = Objects.requireNonNull(wrapperFactory, "wrapperFactory");
        this.curveName = Objects.requireNonNull(curveName, "curveName");
        this.symAlgIDKeyLength = symAlgIDKeyLength;
        this.kdf = Objects.requireNonNull(kdf, "kdf");
    }

    public byte[] unwrap(byte[] data, byte[] fingerprint, BigInteger d) {
        try {
            logger.trace("-- unwrap() - data: 0x{} fingerprint: 0x{} d: 0x{}",
                    Hex.toHexString(data), Hex.toHexString(fingerprint), d.toString(16));
            // TODO write verifcation/ exception handling code.
            ByteBuffer buffer = ByteBuffer.wrap(data);

            int wKeySize = (buffer.getShort() + 7) / 8;
            byte[] wKey = new byte[wKeySize];
            buffer.get(wKey);

            int wrappedSize = Byte.toUnsignedInt(buffer.get());
            byte[] wrapped = new byte[wrappedSize];
            buffer.get(wrapped);

            ECPoint Q = decodePoint(wKey);

            // ECDH assuming curve has a cofactor of 1
            ECPoint S = Q.multiply(d).normalize();

            byte[] hash = kdf.apply(S, fingerprint);

            byte[] derivedKey = Arrays.copyOf(hash, symAlgIDKeyLength);

            Wrapper wrapper = wrapperFactory.get();
            wrapper.init(false, new KeyParameter(derivedKey));
            byte[] unwrap = wrapper.unwrap(wrapped, 0, wrapped.length);

            // TODO sym alg
            byte[] finalize = finalize(unwrap);
            logger.trace("-- unwrap() - unwrapped: 0x{}",Hex.toHexString(finalize));
            return finalize;
            
        } catch (IOException | InvalidCipherTextException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    ECPoint decodePoint(byte[] data) {
        ECCurve curve = ECNamedCurveTable.getByName(curveName).getCurve();
        int compactExportSize = (curve.getFieldSize() + 7) / 8;

        return data.length == compactExportSize
                ? ECPointsCompact.decodeFPPoint(curve, data) // Compact keys support, non RFC6636 compliant.
                : curve.decodePoint(data);
    }

    public static byte[] finalize(byte[] data) {
        // TODO break up method to allow for easier testing
        // data = 1 byte sym_alg_id || decrypted bytes || 2 byte checksum || padding 
        // Working backwards...
        int i = data.length - 1;

        // Verify padding PKCS5
        int paddedBytes = Byte.toUnsignedInt(data[i]);

        if (paddedBytes > data.length - 3) {
            throw new IllegalArgumentException("bad padding length");
        }

        if (paddedBytes > 0) {
            while (i-- > data.length - paddedBytes) {
                if (data[i] != paddedBytes) {
                    // TODO choose a more specific exception
                    throw new IllegalArgumentException("bad padding");
                }
            }
        }

        // Expected checksum
        int checksum = Byte.toUnsignedInt(data[i--]);
        checksum += Byte.toUnsignedInt(data[i]) << 8;

        // Checksum decrypted data
        int t = 0;
        while (i-- > 1) {
            t += Byte.toUnsignedInt(data[i]);
        }

        if ((t & 0xFFFF) != checksum) {
            throw new IllegalArgumentException("bad checksum");
        }

        // Remove sym_alg_id + padding
        byte[] out = new byte[data.length - paddedBytes - 3];
        System.arraycopy(data, 1, out, 0, out.length);
        return out;
    }
}
