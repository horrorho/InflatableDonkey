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

import com.github.horrorho.inflatabledonkey.dataprotection.DPCipherFactories;
import com.github.horrorho.inflatabledonkey.keybag.KeyBag;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.BlockCipher;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class XFileKeyFactory implements Function<byte[], Optional<XFileKey>> {

    private final Function<byte[], Optional<KeyBag>> keyBags;

    public XFileKeyFactory(Function<byte[], Optional<KeyBag>> keyBags) {
        this.keyBags = Objects.requireNonNull(keyBags, "keyBags");
    }

    @Override
    public Optional<XFileKey> apply(byte[] encryptionKey) {
        return KeyBlob.create(encryptionKey)
                .flatMap(this::fileKey);
    }

    Optional<XFileKey> fileKey(KeyBlob blob) {
        return KeyBlobCurve25519Unwrap.unwrap(blob, keyBags)
                .map(u -> fileKey(blob, u));
    }

    XFileKey fileKey(KeyBlob blob, byte[] key) {
        return new XFileKey(key, ciphers(blob), flags(blob));
    }

    byte[] flags(KeyBlob blob) {
        return ByteBuffer.allocate(12)
                .putInt(blob.u1())
                .putInt(blob.u2())
                .putInt(blob.u3())
                .array();
    }

    Supplier<BlockCipher> ciphers(KeyBlob blob) {
        // u3 - 0x00FF0000;
        // experimental
        return (blob.u3() & 0x00FF0000) == 0
                ? DPCipherFactories.AES_CBC
                : DPCipherFactories.AES_XTS;
    }
}
