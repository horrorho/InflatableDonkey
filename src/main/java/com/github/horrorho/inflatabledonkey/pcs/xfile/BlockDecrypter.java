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
package com.github.horrorho.inflatabledonkey.pcs.xfile;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;
import net.jcip.annotations.NotThreadSafe;
import org.bouncycastle.crypto.BufferedBlockCipher;
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

    private final BufferedBlockCipher blockCipher;
    private final ParametersWithIV blockIVKey;
    private final KeyParameter key;

    public BlockDecrypter(
            BufferedBlockCipher blockCipher,
            ParametersWithIV blockIVKey,
            KeyParameter key) {

        this.blockCipher = Objects.requireNonNull(blockCipher, "blockCipher");
        this.blockIVKey = Objects.requireNonNull(blockIVKey, "blockIVKey");
        this.key = Objects.requireNonNull(key, "key");
    }

    public int decrypt(int block, byte[] in, int length, byte[] out) {
        return decrypt(block, in, 0, length, out, 0);
    }

    public int decrypt(int block, byte[] in, int inOff, int length, byte[] out, int outOff) {
        byte[] iv = blockIV(block);

        ParametersWithIV parameters = new ParametersWithIV(key, iv);

        blockCipher.init(false, parameters);
        return blockCipher.processBytes(in, inOff, length, out, outOff);
    }

    byte[] blockIV(int block) {
        byte[] blockHash = blockHash(block);
        byte[] iv = new byte[blockCipher.getBlockSize()];

        blockCipher.init(true, blockIVKey);
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
