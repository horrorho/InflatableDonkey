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
import static com.github.horrorho.inflatabledonkey.chunk.engine.ChunkListDecrypterTestVector.VECTOR_1;
import static com.github.horrorho.inflatabledonkey.chunk.engine.ChunkListDecrypterTestVector.VECTOR_2;
import static com.github.horrorho.inflatabledonkey.chunk.engine.ChunkListDecrypterTestVector.VECTOR_3;
import static com.github.horrorho.inflatabledonkey.chunk.engine.ChunkListDecrypterTestVector.VECTOR_4;
import static com.github.horrorho.inflatabledonkey.chunk.engine.ChunkListDecrypterTestVector.VECTOR_5;
import static com.github.horrorho.inflatabledonkey.chunk.engine.ChunkListDecrypterTestVector.VECTOR_6;
import com.github.horrorho.inflatabledonkey.chunk.store.ChunkDigest;
import com.github.horrorho.inflatabledonkey.chunk.store.ChunkDigests;
import com.github.horrorho.inflatabledonkey.chunk.store.disk.DiskChunkStore;
import com.github.horrorho.inflatabledonkey.io.DirectoryAssistant;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer;
import com.google.protobuf.ByteString;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.crypto.Digest;
import org.junit.AfterClass;
import static org.junit.Assert.assertArrayEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@RunWith(JUnitParamsRunner.class)
public class ChunkListDecrypterTest {

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
    private static final Supplier<Digest> DIGESTS = ChunkDigest::new;

    private static final ChunkServer.HostInfo HOSTINFO = ChunkServer.HostInfo.newBuilder()
            .setHostname("DUMMY")
            .setPort(80)
            .setMethod("GET")
            .setUri("null")
            .setTransportProtocol("HTTP")
            .setTransportProtocolVersion("1.1")
            .setScheme("http")
            .build();

    @Test
    @Parameters
    public void test(List<ChunkListDecrypterTestVector> vectors) throws IOException {
        DiskChunkStore store = new DiskChunkStore(ChunkDigest::new, ChunkDigests::test, CACHE, TEMP);

        Map<Integer, byte[]> keyEncryptionKeys = new HashMap<>();
        List<ChunkServer.ChunkInfo> chunkInfos = new ArrayList<>();
        int offset = 0;
        ByteArrayOutputStream dataOs = new ByteArrayOutputStream();

        for (int index = 0, n = vectors.size(); index < n; index++) {
            ChunkListDecrypterTestVector vector = vectors.get(index);
            byte[] ciphertext = vector.ciphertext();
            dataOs.write(ciphertext);
            keyEncryptionKeys.put(index, vector.kek());
            ChunkServer.ChunkInfo chunkInfo = ChunkServer.ChunkInfo.newBuilder()
                    .setChunkChecksum(ByteString.copyFrom(vector.chunkChecksum()))
                    .setChunkEncryptionKey(ByteString.copyFrom(vector.chunkEncryptionKey()))
                    .setChunkLength(ciphertext.length)
                    .setChunkOffset(offset)
                    .build();
            chunkInfos.add(chunkInfo);
            offset += ciphertext.length;
        }

        ChunkServer.StorageHostChunkList shcl = ChunkServer.StorageHostChunkList.newBuilder()
                .setHostInfo(HOSTINFO)
                .setStorageContainerKey("test")
                .setStorageContainerAuthorizationToken("test")
                .addAllChunkInfo(chunkInfos)
                .build();

        Function<Integer, Optional<byte[]>> keks = i -> Optional.ofNullable(keyEncryptionKeys.get(i));
        SHCLContainer container = new SHCLContainer(shcl, keks, 0);
        ChunkListDecrypter decrypter = ChunkListDecrypterFactory.defaults().apply(store, container);
        ByteArrayInputStream dataIs = new ByteArrayInputStream(dataOs.toByteArray());
        Map<ChunkServer.ChunkReference, Chunk> chunks = decrypter.apply(dataIs);

        for (Map.Entry<ChunkServer.ChunkReference, Chunk> entry : chunks.entrySet()) {
            ChunkServer.ChunkReference reference = entry.getKey();
            Chunk chunk = entry.getValue();

            int index = (int) reference.getChunkIndex();

            ByteArrayOutputStream chunkOs = new ByteArrayOutputStream();
            try (InputStream chunkIs = chunk.inputStream()
                    .orElseThrow(() -> new IllegalStateException("chunk deleted"))) {
                IOUtils.copy(chunkIs, chunkOs);
            }

            ChunkListDecrypterTestVector vector = vectors.get(index);
            assertArrayEquals("plaintext: " + vector.id(), chunkOs.toByteArray(), vector.plaintext());
            assertArrayEquals("ciphertext: " + vector.id(), chunk.checksum(), vector.chunkChecksum());
        };

        for (Chunk c : chunks.values()) {
            store.delete(c.checksum());
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
            Arrays.asList(VECTOR_1, VECTOR_1),
            Arrays.asList(VECTOR_5, VECTOR_6),
            Arrays.asList(VECTOR_1, VECTOR_2, VECTOR_3, VECTOR_4),
            Arrays.asList(VECTOR_1, VECTOR_2, VECTOR_3, VECTOR_4, VECTOR_5, VECTOR_6),
            Arrays.asList(VECTOR_1, VECTOR_5, VECTOR_6, VECTOR_6, VECTOR_5, VECTOR_1)
        };
    }
}
