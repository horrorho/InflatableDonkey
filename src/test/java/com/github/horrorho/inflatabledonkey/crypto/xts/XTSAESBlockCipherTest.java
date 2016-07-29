/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.crypto.xts;

import static com.github.horrorho.inflatabledonkey.crypto.xts.XTSAESTestVector.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Arrays;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

/**
 *
 * @author Ahseya
 */
public class XTSAESBlockCipherTest {

    private final KeyParameter key;
    private final byte[] ptx;
    private final byte[] ctx;
    private final int dataUnitLength;

    public XTSAESBlockCipherTest() throws IOException {
        // Key = key1 | key2
        byte[] keyData = Arrays.concatenate(VECTOR_4.key1(), VECTOR_4.key2());
        key = new KeyParameter(keyData);
        dataUnitLength = VECTOR_4.ctx().length;

        // Vectors 4, 5, 6 are sequential 512 byte data units starting from data unit sequence number 0.
        ByteArrayOutputStream ptxs = new ByteArrayOutputStream();
        ptxs.write(VECTOR_4.ptx());
        ptxs.write(VECTOR_5.ptx());
        ptxs.write(VECTOR_6.ptx());
        ptx = ptxs.toByteArray();

        ByteArrayOutputStream ctxs = new ByteArrayOutputStream();
        ctxs.write(VECTOR_4.ctx());
        ctxs.write(VECTOR_5.ctx());
        ctxs.write(VECTOR_6.ctx());
        ctx = ctxs.toByteArray();
    }

    @Test
    public void testEncryption() throws IOException {
        byte[] out = new byte[ptx.length];

        XTSAESBlockCipher cipher = new XTSAESBlockCipher(dataUnitLength);
        cipher.init(true, key);

        for (int i = 0; i < ptx.length; i += cipher.getBlockSize()) {
            cipher.processBlock(ptx, i, out, i);
        }
        assertArrayEquals(ctx, out);
    }

    @Test
    public void testDecryption() throws IOException {
        byte[] out = new byte[ctx.length];

        XTSAESBlockCipher cipher = new XTSAESBlockCipher(dataUnitLength);
        cipher.init(false, key);

        for (int i = 0; i < ctx.length; i += cipher.getBlockSize()) {
            cipher.processBlock(ctx, i, out, i);
        }
        assertArrayEquals(ptx, out);
    }
}
