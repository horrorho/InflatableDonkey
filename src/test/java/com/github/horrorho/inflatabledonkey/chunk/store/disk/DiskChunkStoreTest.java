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
package com.github.horrorho.inflatabledonkey.chunk.store.disk;

import com.github.horrorho.inflatabledonkey.chunk.Chunk;
import com.github.horrorho.inflatabledonkey.chunk.store.ChunkDigests;
import com.github.horrorho.inflatabledonkey.io.DirectoryAssistant;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Supplier;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@RunWith(JUnitParamsRunner.class)
public class DiskChunkStoreTest {

    @AfterClass
    public static void tearDownClass() throws Exception {
        if (Files.exists(TEMP)) {
            DirectoryAssistant.deleteEmptyTree(BASE, TEMP);
        }
        if (Files.exists(CACHE)) {
            DirectoryAssistant.deleteEmptyTree(BASE, CACHE);
        }
    }

    private static byte[] digest(Supplier<Digest> digests, byte[] data) {
        Digest digest = digests.get();
        byte[] out = new byte[digest.getDigestSize()];
        digest.update(data, 0, data.length);
        digest.doFinal(out, 0);
        return out;
    }

    private static final Logger logger = LoggerFactory.getLogger(DiskChunkStoreTest.class);

    private static final Path BASE = Paths.get("");
    private static final Path TEMP = BASE.resolve("testDiskChunkStore").resolve("temp");
    private static final Path CACHE = BASE.resolve("testDiskChunkStore").resolve("cache");
    private static final Supplier<Digest> DIGESTS = SHA1Digest::new;

    @Test
    @Parameters
    public void test(byte[] data) throws IOException {
        DiskChunkStore store = new DiskChunkStore(DIGESTS, ChunkDigests::test, CACHE, TEMP);

        byte[] checksum = digest(DIGESTS, data);
        Optional<OutputStream> oos = store.outputStream(checksum);
        assertTrue("OutputStream present", oos.isPresent());
        try (OutputStream os = oos.get()) {
            os.write(data);
        }

        Optional<Chunk> oc = store.chunk(checksum);
        assertTrue("Chunk present", oc.isPresent());

        Chunk chunk = oc.get();
        assertArrayEquals("checksum match", checksum, chunk.checksum());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream is = chunk.inputStream()) {
            IOUtils.copy(is, baos);
        }
        assertArrayEquals("data match", data, baos.toByteArray());

        boolean deleted = store.delete(checksum);
        assertTrue("was deleted", deleted);

        Optional<Chunk> odc = store.chunk(checksum);
        assertFalse(odc.isPresent());

        assertFalse("temp folder is empty", Files.list(TEMP).findFirst().isPresent());
    }

    public static Object[] parametersForTest() {
        return new Object[]{
            new Object[]{"".getBytes(StandardCharsets.UTF_8)},
            new Object[]{"0123456789".getBytes(StandardCharsets.UTF_8)},
            new Object[]{"01234567890123456789".getBytes(StandardCharsets.UTF_8)},
            new Object[]{"0123456789012345678901234567890123456789".getBytes(StandardCharsets.UTF_8)}};
    }
}
