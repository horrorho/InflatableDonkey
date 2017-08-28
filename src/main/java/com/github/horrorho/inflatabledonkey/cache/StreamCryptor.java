/*
 * The MIT License
 *
 * Copyright 2017 Ayesha.
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
package com.github.horrorho.inflatabledonkey.cache;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import net.jcip.annotations.Immutable;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.io.CipherInputStream;
import org.bouncycastle.crypto.io.CipherOutputStream;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;

/**
 * Basic AES GCM password based stream encryption.
 * <p>
 * File format: salt || nonce || cipher_text
 *
 * @author Ayesha
 */
@Immutable
public final class StreamCryptor {

    @FunctionalInterface
    @Immutable
    public interface KDF {

        byte[] apply(byte[] password, byte[] salt);
    }

    static byte[] randomBytes(int len) {
        byte[] bs = new byte[len];
        ThreadLocalRandom.current().nextBytes(bs);
        return bs;
    }

    private final KDF kdf;
    private final int saltLength;
    private final int nonceLength;
    private final int tagLength;

    public StreamCryptor(KDF kdf, int saltLength, int nonceLength, int tagLength) {
        this.kdf = Objects.requireNonNull(kdf);
        this.saltLength = saltLength;
        this.nonceLength = nonceLength;
        this.tagLength = tagLength;
    }

    public CipherOutputStream newCipherOutputStream(OutputStream os, byte[] password) throws IOException {
        byte[] salt = randomBytes(saltLength);
        byte[] nonce = randomBytes(nonceLength);
        os.write(salt);
        os.write(nonce);
        byte[] dk = kdf.apply(password, salt);
        GCMBlockCipher cipher = new GCMBlockCipher(new AESEngine());
        AEADParameters parameters = new AEADParameters(new KeyParameter(dk), tagLength * 8, nonce);
        cipher.init(true, parameters);
        return new CipherOutputStream(os, cipher);
    }

    public CipherInputStream newCipherInputStream(InputStream is, byte[] password) throws IOException {
        byte[] salt = IOUtils.readFully(is, saltLength);
        byte[] nonce = IOUtils.readFully(is, nonceLength);
        byte[] dk = kdf.apply(password, salt);
        GCMBlockCipher cipher = new GCMBlockCipher(new AESEngine());
        AEADParameters parameters = new AEADParameters(new KeyParameter(dk), tagLength * 8, nonce);
        cipher.init(false, parameters);
        return new CipherInputStream(is, cipher);
    }

    @Override
    public String toString() {
        return "StreamCryptor{"
                + "kdf=" + kdf
                + ", saltLength=" + saltLength
                + ", nonceLength=" + nonceLength
                + ", tagLength=" + tagLength
                + '}';
    }
}
