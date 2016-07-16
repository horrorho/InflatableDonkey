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
public class Curve25519Test {

    @Test
    @Parameters
    public void testAgreement(String publicKey, String privateKey, String expected) {

        byte[] sharedKey = Curve25519.agreement(
                Hex.decode(publicKey),
                Hex.decode(privateKey));

        assertThat(Hex.toHexString(sharedKey).toLowerCase(), is(expected.toLowerCase()));
    }

    public static Object[] parametersForTestAgreement() {
        return new Object[]{
            new Object[]{
                "02917DC2542198EDEB1078C4D1EBAB74D9CA87890657BA02B9825DADF20A002F",
                "99B66345829D8C05041EEA1BA1ED5B2984C3E5EC7A756EF053473C7F22B49F14",
                "C4D9FE462A2EBBF0745195CE7DC5E8B49947BBD5B42DA74175D5F8125B44582B"}};
    }
}
