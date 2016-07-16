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
package com.github.horrorho.inflatabledonkey.file;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.io.DigestInputStream;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FileSignatures. MMCSSignatureGenerator/ CKFileSignatureGenerator.
 *
 * @author Ahseya
 */
@Immutable
public final class FileSignatures {

    enum Type {
        A;
    }

    public static Optional<DigestInputStream> like(InputStream inputStream, byte[] fileSignature) {
        return type(fileSignature)
                .map(type -> of(inputStream, type));
    }

    public static DigestInputStream of(InputStream inputStream, Type type) {
        switch (type) {
            case A:
                return typeA(inputStream);

            default:
                throw new IllegalStateException("bad type: " + type);
        }
    }

    public static Optional<Type> type(byte[] fileSignature) {
        if (fileSignature.length == 0) {
            return Optional.empty();
        }

        switch (fileSignature[0] & 0x7F) {
            case 0x01:
            case 0x02:
            case 0x0B:
                return fileSignature.length == 21
                        ? Optional.of(Type.A)
                        : Optional.empty();

            default:
                logger.warn("-- type() - unsupported file signature type: 0x{}", Hex.toHexString(fileSignature));
                return Optional.empty();
        }
    }

    public static boolean compare(DigestInputStream digestInputStream, byte[] fileSignature) {
        byte[] output = output(digestInputStream);
        boolean equals = Arrays.equals(
                Arrays.copyOfRange(output, 0, output.length),
                Arrays.copyOfRange(fileSignature, 1, fileSignature.length));

        if (equals) {
            logger.debug("-- compare() - positive match, output: 0x{} fileSignature: 0x{}",
                    Hex.toHexString(output), Hex.toHexString(fileSignature));
        } else {
            logger.warn("-- compare() - negative match, output: 0x{} fileSignature: 0x{}",
                    Hex.toHexString(output), Hex.toHexString(fileSignature));
        }

        return equals;
    }

    static byte[] output(DigestInputStream digestInputStream) {
        Digest digest = digestInputStream.getDigest();
        byte[] out = new byte[digest.getDigestSize()];
        digest.doFinal(out, 0);
        return out;
    }

    private static final Logger logger = LoggerFactory.getLogger(FileSignatures.class);

    private static final byte[] SALT_A = Hex.decode(
            "636F6D2E6170706C652E58617474724F626A65637453616C7400636F6D2E6170706C652E446174614F626A65637453616C7400");

    static DigestInputStream typeA(InputStream inputStream) {
        Digest digest = new SHA1Digest();
        digest.update(SALT_A, 0, SALT_A.length);
        return new DigestInputStream(inputStream, digest);
    }
}
