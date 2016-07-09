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

import java.io.InputStream;
import java.util.Optional;
import java.util.function.Function;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.io.DigestInputStream;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FileSignatureInputStreams. MMCSSignatureGenerator/ CKFileSignatureGenerator.
 *
 * @author Ahseya
 */
@Immutable
public final class FileSignatureInputStreams {

    public static Optional<Function<InputStream, DigestInputStream>> factoryFor(byte[] fileSignature) {
        return fileSignature.length != 0
                ? factoryOf(fileSignature[0])
                : Optional.empty();
    }

    public static Optional<Function<InputStream, DigestInputStream>> factoryOf(int type) {
        switch (type & 0x7F) {
            case 0x01:
            case 0x02:
            case 0x0B:
                return Optional.of(FileSignatureInputStreams::typeA);

            default:
                logger.warn("-- factoryOf() - unsupported file signature type: 0x{}", Integer.toHexString(type));
                return Optional.empty();
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(FileSignatureInputStreams.class);

    private static final byte[] SALT_A = Hex.decode(
            "636F6D2E6170706C652E58617474724F626A65637453616C7400636F6D2E6170706C652E446174614F626A65637453616C7400");

    public static DigestInputStream typeA(InputStream inputStream) {
        Digest digest = new SHA1Digest();
        digest.update(SALT_A, 0, SALT_A.length);
        return new DigestInputStream(inputStream, digest);
    }
}
