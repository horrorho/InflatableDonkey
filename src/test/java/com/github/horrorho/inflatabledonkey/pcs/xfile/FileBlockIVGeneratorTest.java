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
package com.github.horrorho.inflatabledonkey.pcs.xfile;

import java.nio.charset.StandardCharsets;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 *
 * @author Ahseya
 */
@RunWith(JUnitParamsRunner.class)
public class FileBlockIVGeneratorTest {

    @Test
    @Parameters
    public void testApply(FileBlockIVGenerator generator, int blockOffset, String hex) {
        byte[] expected = Hex.decode(hex);

        byte[] iv = generator.apply(blockOffset);
        assertArrayEquals(iv, expected);
    }

    private Object[] parametersForTestApply() {
        return PARAMETERS;
    }

    private static final byte[] KEY_A = "Nagsisipagsisinungasinungalingan".getBytes(StandardCharsets.UTF_8);
    private static final byte[] KEY_B = "Konstantynopolitanczykowianeczka".getBytes(StandardCharsets.UTF_8);

    private static final FileBlockIVGenerator A = new FileBlockIVGenerator(KEY_A);
    private static final FileBlockIVGenerator B = new FileBlockIVGenerator(KEY_B);

    private static final Object[] PARAMETERS = new Object[][]{
        new Object[]{A, 3, "799b72049d47b572399f489548238e38"},
        new Object[]{A, 6, "61c2e1555fb5b8d27c811f87873f7a08"},
        new Object[]{A, 13, "b62f91305c6f75aa7386b689cf69dcfe"},
        new Object[]{A, 28, "98d098d4ab6a29aa293484644d029f88"},
        new Object[]{A, 59, "123fd2ce01053de8ca7f2b0aac9625c0"},
        new Object[]{A, 122, "721fb347f910ddcb7c517d96193b14a4"},
        new Object[]{A, 249, "1dd48f241757388c8431c0f563337eb9"},
        new Object[]{B, 504, "1fcc567a2d4857da710c14d5386fca9b"},
        new Object[]{B, 1015, "e3279ee6fa06d7852e40b278f6c8371b"},
        new Object[]{B, 2038, "bf92d94f82d6ca57136e822a0b974d66"},
        new Object[]{B, 4085, "0741f0e24b117256241f8b892d5459aa"},
        new Object[]{B, 8180, "0d1c1c31e5a7fe750d193b0ec571ea54"},
        new Object[]{B, 16371, "ab6e558a99863e5f9f693a2c41eb1a44"},
        new Object[]{B, 32754, "6b0f2ccaf562d9dcfac34962c5473c31"},
        new Object[]{B, 65521, "63de57cf1d16e403205964dcf78699d0"}
    };
}
