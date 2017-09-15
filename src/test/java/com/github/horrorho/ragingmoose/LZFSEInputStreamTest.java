/*
 * The MIT License
 *
 * Copyright 2017 Ayesha.
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
package com.github.horrorho.ragingmoose;

import static com.github.horrorho.ragingmoose.ProcessAssistant.copy;
import static com.github.horrorho.ragingmoose.ProcessAssistant.firstInPath;
import static com.github.horrorho.ragingmoose.ProcessAssistant.newInputStream;
import static com.github.horrorho.ragingmoose.ProcessAssistant.newPipedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.annotation.Nullable;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeTrue;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Ayesha
 */
@RunWith(JUnitParamsRunner.class)
public class LZFSEInputStreamTest {

    private final static int[] LENGTHS = new int[]{
        // bvx-
        7,
        // bvxn
        256, 1023, 2046, 4093,
        // bvx1/ bvx2
        5120, 8191, 16282, 32765, 65532, 131067, 262138, 524281, 1000000
    };

    @Nullable
    private static final String LZFSE = firstInPath("lzfse", "lzfse.exe").orElse(null);

    @Nullable
    private static final String TCGEN = firstInPath("tcgen", "tcgen.exe").orElse(null);

    private final byte[] buffer = new byte[16384];

    public LZFSEInputStreamTest() {
    }

    /**
     * Test using resource data.
     * <p>
     * Format: SHA-256 digest | LZFSE encoded data
     * <p>
     * lzfse.test: contains bvx-, bvx1, bvx2, bvxn encrypted random words (text)
     *
     * @throws IOException
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.DigestException
     */
    @Test
    public void defaultTest() throws IOException, NoSuchAlgorithmException, DigestException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("lzfse.test");
        assertNotNull("lzfse.test", is);

        byte[] digest = new byte[32];
        is.read(digest);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream dis = new LZFSEInputStream(is)) {
            copy(dis, baos, buffer);
        }

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] _digest = md.digest(baos.toByteArray());

        assertArrayEquals("SHA-256", digest, _digest);
    }

    /**
     * Tcgen data into LZFSE external compressor into RagingMoose decompressor.
     *
     * @param arg
     * @throws IOException
     */
    @Ignore
    @Test
    @Parameters(method = "tcgen")
    public void tcgenTest(String arg) throws IOException {
        assumeTrue("lzfse", LZFSE != null);
        assumeTrue("tcgen", TCGEN != null);

        ProcessBuilder tcgen = new ProcessBuilder(TCGEN, "-", arg);
        ProcessBuilder encode = new ProcessBuilder(LZFSE, "-encode");

        // Generate compression test data.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream is = newInputStream(tcgen)) {
            copy(is, baos, buffer);
        }
        byte[] bs = baos.toByteArray();

        // Test data into LZFSE external compressor into RagingMoose decompressor.
        for (int len : LENGTHS) {
            byte[] head = Arrays.copyOf(bs, len);
            
            baos.reset();
            try (InputStream is = new LZFSEInputStream(newPipedInputStream(encode, new ByteArrayInputStream(head)))) {
                copy(is, baos, buffer);
            }
            byte[] _head = baos.toByteArray();

            assertArrayEquals(arg + ":" + len, head, _head);
        }
    }

    /**
     * Tcgen data into LZFSE external compressor into LZFSE external decompressor
     *
     * @param arg
     * @throws IOException
     */
    @Ignore
    @Test
    @Parameters(method = "tcgen")
    public void tcgenTestExt(String arg) throws IOException {
        assumeTrue("lzfse", LZFSE != null);
        assumeTrue("tcgen", TCGEN != null);

        ProcessBuilder tcgen = new ProcessBuilder(TCGEN, "-", arg);
        ProcessBuilder encode = new ProcessBuilder(LZFSE, "-encode");
        ProcessBuilder decode = new ProcessBuilder(LZFSE, "-decode");

        // Generate compression test data.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream is = newInputStream(tcgen)) {
            copy(is, baos, buffer);
        }
        byte[] bs = baos.toByteArray();

        // Test data into LZFSE external compressor into RagingMoose decompressor.
        for (int len : LENGTHS) {
            byte[] head = Arrays.copyOf(bs, len);

            baos.reset();
            try (InputStream is = newPipedInputStream(decode, newPipedInputStream(encode, new ByteArrayInputStream(head)))) {
                copy(is, baos, buffer);
            }
            byte[] _head = baos.toByteArray();

            assertArrayEquals(arg + ":" + len, head, _head);
        }
    }

    // Based on 'Compression test file generator' with tcgen
    // Matt Mahoney
    // https://encode.ru/threads/1306-Compression-test-file-generator
    // http://mattmahoney.net/
    Object tcgen() {
        return new Object[]{
            // Zeros:
            new Object[]{"m1b1"},
            // Ascending sequence:
            new Object[]{"m1c1b1d1"},
            // Descending sequence:
            new Object[]{"m1c2551b1d1"},
            // Repeated random string copies:
            new Object[]{"r10k100"},
            // Random:
            new Object[]{"m1v256"},
            // Random 16 character alphabet:
            new Object[]{"m1v16"},
            // Random bit string from '0' and '1' (text):
            new Object[]{"m1v2c48b2"},
            // Random 16 character alphabet changed every nth:
            new Object[]{"r10k100v16"},
            // 10 copies of n size random string:
            new Object[]{"n100w10000"},
            // 10 copies of n size random string (text):
            new Object[]{"n100w10000c48b2"},
            // 100 copies of n size random string:
            new Object[]{"k1w1000"},
            // 100 copies of n size random string (text):
            new Object[]{"k1w1000c48b2"},
            // 1000 copies of n size random string:
            new Object[]{"k10w100"},
            // 1000 copies of n size random string (text):
            new Object[]{"k10w100c48b2"},
            // Order 2, 16 word vocabulary:
            new Object[]{"k500w2v16"},
            // Order 2, 256 word vocabulary:
            new Object[]{"k500w2v256"},
            // Order 2, 256 word vocabulary changed every nth:
            new Object[]{"r100k5w2v256"},
            // Order 3, 256 word vocabulary changed every nth:
            new Object[]{"r30k5w3v256"},
            // Order 4, 256 word vocabulary changed every nth:
            new Object[]{"r5k50w4v256"},
            // Order 4, 4k word vocabulary:
            new Object[]{"k250v4096w4"},
            // Order 8 word vocabulary = {'0', '1'}:
            new Object[]{"k100v2w8c48b2"},
            // Order 8, 16 word vocabulary:
            new Object[]{"k100v16w8"},
            // Order 8, 256 word vocabulary:
            new Object[]{"k100v256w8"},
            // Order 8, 4k word vocabulary:
            new Object[]{"k100v4096w8"},
            // Order 20, 256 word vocabulary of 20 bit strings (text):
            new Object[]{"k50w20v256b2c48"},
            // Random from alphabet -1, 0, 1
            new Object[]{"m1c255b3v3"},
            // Random from alphabet -1, 0, 1 but delta coded
            new Object[]{"m1c255b3v3d1"},
            // Random from alphabet -1, 0, 1 but delta coded with stride 2
            new Object[]{"m1c255b3v3d2"},
            // Random from alphabet -1, 0, 1 but delta coded with stride 4
            new Object[]{"m1c255b3v3d4"},
            // Random from alphabet -1, 0, 1 but delta coded with stride 1000
            new Object[]{"m1c255b3v3d1000"}
        };
    }
}
