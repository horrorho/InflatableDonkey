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
package com.github.horrorho.inflatabledonkey.chunk.engine;

import com.github.horrorho.inflatabledonkey.chunk.Chunk;
import static com.github.horrorho.inflatabledonkey.chunk.engine.ChunkListDecrypterTestVector.*;
import static com.github.horrorho.inflatabledonkey.chunk.engine.ChunkListDecrypterTestVector.Type.*;
import com.github.horrorho.inflatabledonkey.chunk.store.ChunkDigest;
import com.github.horrorho.inflatabledonkey.chunk.store.ChunkDigests;
import com.github.horrorho.inflatabledonkey.chunk.store.ChunkStore;
import com.github.horrorho.inflatabledonkey.chunk.store.disk.DiskChunkStore;
import com.github.horrorho.inflatabledonkey.io.DirectoryAssistant;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.ChunkInfo;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.HostInfo;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.StorageHostChunkList;
import com.google.protobuf.ByteString;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import static java.util.stream.Collectors.toList;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author Ahseya
 */
@RunWith(JUnitParamsRunner.class)
public class ChunkListDecrypterTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        if (Files.exists(TEMP)) {
            DirectoryAssistant.deleteEmptyBranch(BASE, TEMP);
        }
        if (Files.exists(CACHE)) {
            DirectoryAssistant.deleteEmptyBranch(BASE, CACHE);
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(ChunkListDecrypterTest.class);

    private static final Path BASE = Paths.get("");
    private static final Path TEMP = BASE.resolve("testDiskChunkStore").resolve("temp");
    private static final Path CACHE = BASE.resolve("testDiskChunkStore").resolve("cache");

    private static final HostInfo HOSTINFO = HostInfo.newBuilder()
            .setHostname("DUMMY")
            .setPort(80)
            .setMethod("GET")
            .setUri("null")
            .setTransportProtocol("HTTP")
            .setTransportProtocolVersion("1.1")
            .setScheme("http")
            .build();

    @Ignore
    @Test
    @Parameters
    public void test(List<ChunkListDecrypterTestVector> vectors) throws IOException {
        ChunkStore store = new DiskChunkStore(ChunkDigest::new, ChunkDigests::test, CACHE, TEMP);

        ByteArrayOutputStream data = new ByteArrayOutputStream();
        StorageHostChunkList container = container(vectors, ChunkListDecrypterTestVector::keyTypeOne, data);
        ChunkListDecrypter decrypter = ChunkListDecrypter.instance();
        ByteArrayInputStream dataIs = new ByteArrayInputStream(data.toByteArray());
        decrypter.apply(container, dataIs, store);

        Optional<List<Chunk>> chunks = chunks(container, store);
        assertTrue("not all chunks are present", chunks.isPresent());

        for (Chunk chunk : chunks.get()) {
            ByteArrayOutputStream chunkOs = new ByteArrayOutputStream();
            try (InputStream chunkIs = chunk.inputStream()
                    .orElseThrow(() -> new IllegalStateException("chunk deleted"))) {
                IOUtils.copy(chunkIs, chunkOs);
            }

            Optional<ChunkListDecrypterTestVector> optional = vectors.stream()
                    .filter(u -> Arrays.equals(u.chunkChecksum(), chunk.checksum()))
                    .findFirst();
            if (!optional.isPresent()) {
                fail("unknown checksum");
                continue;
            }
            ChunkListDecrypterTestVector vector = optional.get();
            assertArrayEquals("plaintext: " + vector.id(), vector.plaintext(), chunkOs.toByteArray());
            assertArrayEquals("ciphertext: " + vector.id(), vector.chunkChecksum(), chunk.checksum());
        }

        for (Chunk chunk : chunks.get()) {
            store.delete(chunk.checksum());
        }
    }

    public static Object[] parametersForTest() {
        return new Object[]{
            Arrays.asList(VECTOR_1),
            Arrays.asList(VECTOR_2),
            Arrays.asList(VECTOR_3),
            Arrays.asList(VECTOR_4),
            Arrays.asList(VECTOR_5),
            Arrays.asList(VECTOR_6),
            Arrays.asList(VECTOR_PAD, VECTOR_1, VECTOR_2),
            Arrays.asList(VECTOR_1, VECTOR_2, VECTOR_PAD),
            Arrays.asList(VECTOR_PAD, VECTOR_1, VECTOR_2, VECTOR_PAD),
            Arrays.asList(VECTOR_PAD, VECTOR_1, VECTOR_PAD, VECTOR_2, VECTOR_PAD),
            Arrays.asList(VECTOR_1, VECTOR_1),
            Arrays.asList(VECTOR_2, VECTOR_2_ALT),
            Arrays.asList(VECTOR_2, VECTOR_2_ALT, VECTOR_3),
            Arrays.asList(VECTOR_1, VECTOR_1, VECTOR_2, VECTOR_2, VECTOR_3),
            Arrays.asList(VECTOR_1, VECTOR_2, VECTOR_2_ALT, VECTOR_PAD, VECTOR_3),
            Arrays.asList(VECTOR_1, VECTOR_2, VECTOR_PAD, VECTOR_3, VECTOR_3, VECTOR_PAD),
            Arrays.asList(VECTOR_1, VECTOR_2, VECTOR_3, VECTOR_4, VECTOR_5, VECTOR_6)
        };
    }

    @Ignore
    @Test
    @Parameters
    public void testIOException(List<ChunkListDecrypterTestVector> vectors) throws IOException {
        thrown.expect(IOException.class);

        ChunkStore store = new DiskChunkStore(ChunkDigest::new, ChunkDigests::test, CACHE, TEMP);

        ByteArrayOutputStream data = new ByteArrayOutputStream();
        StorageHostChunkList container = container(vectors, ChunkListDecrypterTestVector::keyTypeOne, data);
        ChunkListDecrypter decrypter = ChunkListDecrypter.instance();
        ByteArrayInputStream dataIs = new ByteArrayInputStream(data.toByteArray());
        decrypter.apply(container, dataIs, store);
    }

    public static Object[] parametersForTestIOException() {
        return new Object[]{
            Arrays.asList(VECTOR_FAIL_KEY),
            Arrays.asList(VECTOR_FAIL_CHECKSUM),};
    }

    @Ignore
    @Test
    public void testIllegalArgumentException() throws IOException {
        thrown.expect(IllegalArgumentException.class);

        ChunkStore store = new DiskChunkStore(ChunkDigest::new, ChunkDigests::test, CACHE, TEMP);
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        List<ChunkListDecrypterTestVector> vectors = Arrays.asList(VECTOR_1, VECTOR_2);
        StorageHostChunkList container = container(vectors, ChunkListDecrypterTestVector::keyTypeTwo, data);
        ChunkListDecrypter decrypter = ChunkListDecrypter.instance();
        ByteArrayInputStream dataIs = new ByteArrayInputStream(data.toByteArray());
        decrypter.apply(container, dataIs, store);
    }

    Optional<List<Chunk>> chunks(StorageHostChunkList container, ChunkStore store) {
        List<byte[]> checksums = checksums(container);
        return store.allChunks(checksums);
    }

    List<byte[]> checksums(StorageHostChunkList container) {
        return container.getChunkInfoList()
                .stream()
                .map(ci -> ci.getChunkChecksum().toByteArray())
                .collect(toList());
    }

    StorageHostChunkList container(List<ChunkListDecrypterTestVector> vectors,
            Function<ChunkListDecrypterTestVector, byte[]> key, OutputStream data) throws IOException {
        List<ChunkInfo> chunkInfos = new ArrayList<>();
        byte[] lastCiphertext = null;
        for (int index = 0, offset = 0, length = 0, n = vectors.size(); index < n; index++) {
            ChunkListDecrypterTestVector vector = vectors.get(index);
            byte[] ciphertext = vector.ciphertext();

            if (vector.type() == PAD) {
                data.write(ciphertext);
            } else {
                if (Arrays.equals(ciphertext, lastCiphertext)) {
                    offset -= length;
                } else {
                    data.write(ciphertext);
                }
                ChunkInfo chunkInfo = ChunkInfo.newBuilder()
                        .setChunkChecksum(ByteString.copyFrom(vector.chunkChecksum()))
                        .setChunkEncryptionKey(ByteString.copyFrom(key.apply(vector)))
                        .setChunkLength(ciphertext.length)
                        .setChunkOffset(offset)
                        .build();
                chunkInfos.add(chunkInfo);
            }

            length = ciphertext.length;
            offset += length;
            lastCiphertext = ciphertext;
        }

        return StorageHostChunkList.newBuilder()
                .setHostInfo(HOSTINFO)
                .setStorageContainerKey("test")
                .setStorageContainerAuthorizationToken("test")
                .addAllChunkInfo(chunkInfos)
                .build();
    }
}
