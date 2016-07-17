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
import com.github.horrorho.inflatabledonkey.crypto.xts.XTSAESBlockCipher;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.io.CipherInputStream;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

/**
 * AES-CBC/ AES-XTS Data Protection decryption.
 *
 * @author Ahseya
 */
@Immutable
public final class FileDecrypters {

    private final Optional<Boolean> xtsOverride;

    public FileDecrypters(Optional<Boolean> xtsOverride) {
        this.xtsOverride = Objects.requireNonNull(xtsOverride, "xtsOverride");
    }

    public FileDecrypters() {
        this(Optional.empty());
    }

    public static InputStream decrypt(InputStream in, Optional<FileKey> key) {
        return key
                .map(FileDecrypters::cipher)
                .map(BufferedBlockCipher::new)
                .map(cipher -> (InputStream) new CipherInputStream(in, cipher))
                .orElse(in);
    }

    static BlockCipher cipher(FileKey key) {
        BlockCipher cipher = useXTS(key)
                ? new XTSAESBlockCipher(FileDecrypters::iosTweakFunction, key.dataUnitSize())
                : new FileBlockCipher();
        cipher.init(false, new KeyParameter(key.key()));
        return cipher;
    }

    static boolean useXTS(FileKey key) {
        if (Property.XTS_FORCE.booleanValue().orElse(false)) {
            return true;
        }

        if (Property.XTS_DISABLE.booleanValue().orElse(false)) {
            return false;
        }

        return key.isXTS();
    }

    static byte[] iosTweakFunction(long tweakValue) {
        byte[] bs = Pack.longToLittleEndian(tweakValue);
        return Arrays.concatenate(bs, bs);
    }
}
