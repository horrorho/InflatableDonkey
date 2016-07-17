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

import com.github.horrorho.inflatabledonkey.args.Property;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.io.CipherInputStream;
import org.bouncycastle.crypto.io.DigestInputStream;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class FileStreamWriter {

    private static final Logger logger = LoggerFactory.getLogger(FileStreamWriter.class);

    private static final int BUFFER_SIZE = Property.FILE_WRITER_BUFFER_LENGTH.asInteger().orElse(8192);

    public static boolean
            copy(InputStream in, OutputStream out, Optional<FileKeyCipher> keyCipher, Optional<byte[]> signature)
            throws IOException {

        Digest digest = signature.flatMap(CKSignature::type)
                .orElse(CKSignature.ONE)
                .newDigest();

        DigestInputStream digestInputStream = new DigestInputStream(in, digest);

        IOUtils.copyLarge(FileStreamWriter.decryptStream(digestInputStream, keyCipher), out, new byte[BUFFER_SIZE]);

        return testSignature(digestInputStream.getDigest(), signature);
    }

    static InputStream decryptStream(InputStream in, Optional<FileKeyCipher> keyCipher) {
        return keyCipher
                .map(kc -> decryptStream(in, kc))
                .orElse(in);
    }

    static InputStream decryptStream(InputStream in, FileKeyCipher keyCipher) {
        BlockCipher cipher = keyCipher.ciphers().get();
        cipher.init(false, new KeyParameter(keyCipher.key()));
        return new CipherInputStream(in, new BufferedBlockCipher(cipher));
    }

    static boolean testSignature(Digest digest, Optional<byte[]> signature) {
        return signature
                .map(c -> testSignature(digest, c))
                .orElseGet(() -> {
                    byte[] out = signature(digest);
                    logger.debug("-- testSignature() - signature: 0x{}", Hex.toHexString(out));
                    return true;
                });
    }

    static boolean testSignature(Digest digest, byte[] signature) {
        byte[] out = signature(digest);
        boolean match = Arrays.areEqual(out, signature);
        if (match) {
            logger.debug("-- testSignature() - positive match out: 0x{} target: 0x{}",
                    Hex.toHexString(out), Hex.toHexString(signature));
        } else {

            logger.debug("-- testSignature() - negative match out: 0x{} target: 0x{}",
                    Hex.toHexString(out), Hex.toHexString(signature));
        }
        return match;
    }

    static byte[] signature(Digest digest) {
        byte[] out = new byte[digest.getDigestSize()];
        digest.doFinal(out, 0);
        return out;
    }
}
