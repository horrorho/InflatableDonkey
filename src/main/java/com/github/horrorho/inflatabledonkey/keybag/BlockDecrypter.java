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
package com.github.horrorho.inflatabledonkey.keybag;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Objects;
import net.jcip.annotations.NotThreadSafe;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BlockDecrypter.
 *
 * @author Ahseya
 */
@NotThreadSafe
public final class BlockDecrypter {

    private static final Logger logger = LoggerFactory.getLogger(BlockDecrypter.class);

    public static BlockDecrypter create(byte[] key) {
        BufferedBlockCipher cipher = new BufferedBlockCipher(new CBCBlockCipher(new AESFastEngine()));
        SHA1Digest digest = new SHA1Digest();

        return create(cipher, digest, key);
    }

    public static BlockDecrypter create(BufferedBlockCipher cipher, Digest digest, byte[] key) {
        ParametersWithIV ivKey = ivKey(digest, cipher.getBlockSize(), key);
        KeyParameter keyParameter = key(key);

        return new BlockDecrypter(cipher, digest, ivKey, keyParameter);
    }

    public static ParametersWithIV ivKey(Digest digest, int length, byte[] key) {
        byte[] hash = new byte[digest.getDigestSize()];

        digest.reset();
        digest.update(key, 0, key.length);
        digest.doFinal(hash, 0);

        KeyParameter keyParameter = new KeyParameter(Arrays.copyOfRange(hash, 0, length));
        byte[] iv = new byte[length];

        return new ParametersWithIV(keyParameter, iv);
    }

    public static KeyParameter key(byte[] key) {
        return new KeyParameter(key);
    }

    private final BufferedBlockCipher blockCipher;
    private final Digest digest;
    private final ParametersWithIV ivKey;
    private final KeyParameter key;

    public BlockDecrypter(
            BufferedBlockCipher blockCipher,
            Digest digest,
            ParametersWithIV ivKey,
            KeyParameter key) {

        this.blockCipher = Objects.requireNonNull(blockCipher, "blockCipher");
        this.digest = Objects.requireNonNull(digest, "digest");
        this.ivKey = Objects.requireNonNull(ivKey, "ivKey");
        this.key = Objects.requireNonNull(key, "key");
    }

    public byte[] decryptBlocks(InputStream input, OutputStream output, int blockLength) throws IOException {
        byte[] in = new byte[blockLength];
        byte[] out = new byte[blockLength];

        byte[] hash = new byte[digest.getDigestSize()];
        digest.reset();

        int block = 0;
        int length;
        while ((length = read(input, in)) != -1) {
            digest.update(in, 0, length);
            decryptBlock(block, in, length, out);
            output.write(out, 0, length);
        }

        digest.doFinal(hash, 0);
        return hash;
    }

    int read(InputStream input, byte[] buffer) throws IOException {
        // Read complete blocks where possible, as read on streams may return partial data which leads to incomplete 
        // block filling and broken decryption.
        int offset = 0;
        do {
            int read = input.read(buffer, offset, buffer.length - offset);

            if (read == -1) {
                return offset == 0 ? -1 : offset;
            }

            offset += read;

        } while (offset < buffer.length);
        return offset;
    }

    public void decryptBlock(int block, byte[] in, int length, byte[] out) {
        decryptBlock(block, in, 0, length, out, 0);
    }

    public void decryptBlock(int block, byte[] in, int inOff, int length, byte[] out, int outOff) {
        byte[] iv = blockIV(block);

        ParametersWithIV parameters = new ParametersWithIV(key, iv);

        blockCipher.init(false, parameters);
        blockCipher.processBytes(in, inOff, length, out, outOff);
    }

    byte[] blockIV(int block) {
        byte[] blockHash = blockHash(block);
        byte[] iv = new byte[blockCipher.getBlockSize()];

        blockCipher.init(true, ivKey);
        blockCipher.processBytes(blockHash, 0, blockHash.length, iv, 0);

        return iv;
    }

    byte[] blockHash(int block) {
        int offset = block << 12;
        byte[] hash = new byte[0x10];
        ByteBuffer buffer = ByteBuffer.wrap(hash);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < 4; i++) {
            offset = ((offset & 1) == 1)
                    ? 0x80000061 ^ (offset >>> 1)
                    : offset >>> 1;
            buffer.putInt(offset);
        }

        return hash;
    }
}
// TODO cipher exceptions, notably: BufferUnderflowException | DataLengthException