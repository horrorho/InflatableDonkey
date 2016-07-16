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
package com.github.horrorho.inflatabledonkey.crypto.xtsaes;

import static com.github.horrorho.inflatabledonkey.crypto.xtsaes.XTSAESTestVector.BLOCK_LENGTH;
import java.util.Arrays;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertArrayEquals;

/**
 *
 * @author Ahseya
 */
@RunWith(JUnitParamsRunner.class)
public class XTSTweakTest {

    @Test
    @Parameters
    public void test(XTSAESTestVector testVector) {
        KeyParameter key = new KeyParameter(testVector.key2());
        XTSTweak tweak = new XTSTweak()
                .init(key)
                .reset(testVector.dataUnitSequenceNumber());

        byte[] twk = testVector.twk();
        for (int i = 0; i < twk.length; i += BLOCK_LENGTH) {
            byte[] value = tweak.value();
            byte[] expected = Arrays.copyOfRange(twk, i, i + BLOCK_LENGTH);

            assertArrayEquals(testVector.id(), value, expected);
            tweak.next();
        }
    }

    private Object[] parametersForTest() {
        return XTSAESTestVector.vectors();
    }
}
