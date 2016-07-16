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
package com.github.horrorho.inflatabledonkey.crypto.xts;

import com.github.horrorho.inflatabledonkey.crypto.xts.XTSAESCipher;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 *
 * @author Ahseya
 */
@RunWith(JUnitParamsRunner.class)
public class XTSAESBlockCipherTest {

    private final XTSAESCipher cipher = new XTSAESCipher();

    @Test
    @Parameters
    public void testEncryption(XTSAESTestVector testVector) {
        KeyParameter key1 = new KeyParameter(testVector.key1());
        KeyParameter key2 = new KeyParameter(testVector.key2());
        byte[] data = testVector.ptx();
        long n = testVector.dataUnitSequenceNumber();

        int bytes = cipher.init(true, key1, key2)
                .processDataUnit(data, 0, data.length, data, 0, n);

        assertEquals(testVector.id(), bytes, data.length);
        assertArrayEquals(testVector.id(), data, testVector.ctx());
    }

    private Object[] parametersForTestEncryption() {
        return XTSAESTestVector.vectors();
    }

    @Test
    @Parameters
    public void testDecryption(XTSAESTestVector testVector) {
        KeyParameter key1 = new KeyParameter(testVector.key1());
        KeyParameter key2 = new KeyParameter(testVector.key2());
        byte[] data = testVector.ctx();
        long n = testVector.dataUnitSequenceNumber();

        int bytes = cipher.init(false, key1, key2)
                .processDataUnit(data, 0, data.length, data, 0, n);

        assertEquals(testVector.id(), bytes, data.length);
        assertArrayEquals(testVector.id(), data, testVector.ptx());
    }

    private Object[] parametersForTestDecryption() {
        return XTSAESTestVector.vectors();
    }
}
