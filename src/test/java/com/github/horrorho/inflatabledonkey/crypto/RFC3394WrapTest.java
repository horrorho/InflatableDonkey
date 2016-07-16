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

import java.util.Optional;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
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
public class RFC3394WrapTest {

    @Test
    @Parameters
    public void testWrap(String keyEncryptionKey, String key, String expected) {

        byte[] wrapped = RFC3394Wrap
                .wrapAES(Hex.decode(keyEncryptionKey), Hex.decode(key));

        assertThat(Hex.toHexString(wrapped).toLowerCase(), is(expected.toLowerCase()));
    }

    public static Object[] parametersForTestWrap() {
        return new Object[]{
            new Object[]{
                "000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F",
                "00112233445566778899AABBCCDDEEFF000102030405060708090A0B0C0D0E0F",
                "28C9F404C4B810F4CBCCB35CFB87F8263F5786E2D80ED326CBC7F0E71A99F43BFB988B9B7A02DD21"}};
    }

    @Test
    @Parameters
    public void testUnwrap(String keyEncryptionKey, String key, String expected) throws InvalidCipherTextException {

        Optional<byte[]> unwrapped = RFC3394Wrap
                .unwrapAES(Hex.decode(keyEncryptionKey), Hex.decode(key));

        assertTrue(unwrapped.isPresent());
        assertThat(Hex.toHexString(unwrapped.get()).toLowerCase(), is(expected.toLowerCase()));
    }

    public static Object[] parametersForTestUnwrap() {
        return new Object[]{
            new Object[]{
                "000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F",
                "28C9F404C4B810F4CBCCB35CFB87F8263F5786E2D80ED326CBC7F0E71A99F43BFB988B9B7A02DD21",
                "00112233445566778899AABBCCDDEEFF000102030405060708090A0B0C0D0E0F"}};
    }
}
