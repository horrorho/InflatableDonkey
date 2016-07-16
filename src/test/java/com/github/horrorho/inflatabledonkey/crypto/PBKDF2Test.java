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
package com.github.horrorho.inflatabledonkey.crypto;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.bouncycastle.util.encoders.Hex;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 *
 * @author Ahseya
 */
@RunWith(JUnitParamsRunner.class)
public class PBKDF2Test {

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    @Test
    @Parameters
    public void testGenerate(String password, String salt, int iterations, int keyLengthBytes, String expected) {

        byte[] generated = PBKDF2.generate(
                password.getBytes(CHARSET),
                salt.getBytes(CHARSET), iterations,
                keyLengthBytes * 8);

        assertThat(Hex.toHexString(generated).toLowerCase(), is(expected.toLowerCase()));
    }

    public static Object[] parametersForTestGenerate() {
        return new Object[]{
            new Object[]{"password", "salt", 1, 20, "0c60c80f961f0e71f3a9b524af6012062fe037a6"},
            new Object[]{"password", "salt", 2, 20, "ea6c014dc72d6f8ccd1ed92ace1d41f0d8de8957"},
            new Object[]{"password", "salt", 4096, 20, "4b007901b765489abead49d926f721d065a429c1"},
            new Object[]{"pass\0word", "sa\0lt", 4096, 16, "56fa6aa75548099dcc37d7f03425e0c3"}};
    }
}
