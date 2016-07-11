/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.crypto.xblock;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;
import java.util.function.IntFunction;
import net.jcip.annotations.NotThreadSafe;
import org.bouncycastle.crypto.BlockCipher;

/**
 * XBlockIndexHash.
 *
 * @author Ahseya
 */
@NotThreadSafe
public class XBlockIndexHash implements IntFunction<byte[]> {

    private final BlockCipher cipher;

    public XBlockIndexHash(BlockCipher cipher) {
        this.cipher = Objects.requireNonNull(cipher, "cipher");
    }

    @Override
    public byte[] apply(int index) {
        byte[] hash = hash(index);
        byte[] out = new byte[cipher.getBlockSize()];

        cipher.processBlock(hash, 0, out, 0);

        return out;
    }

    protected byte[] hash(int index) {
        int offset = index << 12;
        byte[] hash = new byte[cipher.getBlockSize()];

        ByteBuffer buffer = ByteBuffer.wrap(hash);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < hash.length; i += 4) {
            offset = ((offset & 1) == 1)
                    ? 0x80000061 ^ (offset >>> 1)
                    : offset >>> 1;
            buffer.putInt(offset);
        }

        return hash;
    }
}
