/*
 * The MIT License
 *
 * Copyright 2015 Ahseya.
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
package com.github.horrorho.inflatabledonkey.chunkclient;

import com.github.horrorho.inflatabledonkey.protocol.ChunkServer;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import net.jcip.annotations.Immutable;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.StreamBlockCipher;
import org.bouncycastle.crypto.digests.GeneralDigest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Chunk decrypter.
 *
 * @author Ahseya
 */
@Immutable
public final class ChunkDecrypter implements BiFunction<List<ChunkServer.ChunkInfo>, byte[], List<Optional<byte[]>>> {

    private static final Logger logger = LoggerFactory.getLogger(ChunkDecrypter.class);

    private final Supplier<StreamBlockCipher> cipherSupplier;
    private final Supplier<GeneralDigest> digestSupplier;
    private final Function<byte[], Optional<byte[]>> decryptor;

    public ChunkDecrypter(
            Supplier<StreamBlockCipher> cipherSupplier,
            Supplier<GeneralDigest> digestSupplier,
            Function<byte[], Optional<byte[]>> immutableDecryptor) {

        this.cipherSupplier = Objects.requireNonNull(cipherSupplier, "cipherSupplier");
        this.digestSupplier = Objects.requireNonNull(digestSupplier, "digestSupplier");
        this.decryptor = Objects.requireNonNull(immutableDecryptor, "decryptor");
    }

    public ChunkDecrypter(Function<byte[], Optional<byte[]>> immutableDecryptor) {
        this(() -> new CFBBlockCipher(new AESFastEngine(), 128), SHA256Digest::new, immutableDecryptor);
    }

    @Override
    public List<Optional<byte[]>> apply(List<ChunkServer.ChunkInfo> chunkInfoList, byte[] data) {

        List<Optional<byte[]>> decrypted = new ArrayList<>();

        int offset = 0;
        for (ChunkServer.ChunkInfo chunkInfo : chunkInfoList) {
            Optional<byte[]> decryptedChunk = decryptChunk(chunkInfo, data, offset);
            decrypted.add(decryptedChunk);
            offset += chunkInfo.getChunkLength();
        }

        return decrypted;
    }

    // Experimental phase use only, remove when stable.
    void write(String path, byte[] data, int offset, int length) {
        // Dump out binary data to file.
        try (OutputStream outputStream = Files.newOutputStream(Paths.get(path))) {
            outputStream.write(data, offset, length);
            logger.info("-- write() - file written: {}", path);

        } catch (IOException ex) {
            logger.warn("-- write() - exception: {}", ex);
        }
    }

    Optional<byte[]> decryptChunk(ChunkServer.ChunkInfo chunkInfo, byte[] data, int offset) {
        try {
            if (!chunkInfo.hasChunkEncryptionKey()) {
                logger.warn("-- decryptChunk() - missing chunk encryption key: {}", chunkInfo);
                return Optional.empty();
            }

            if (keyType(chunkInfo) != 2) {
                // TODO no idea if type 2 chunks still exist.
                logger.warn("-- decryptChunk() - chunk 1 decryption not yet implemented: {}", chunkInfo);
                return Optional.empty();
            }

            byte[] decrypted = decryptData(key(chunkInfo), chunkInfo.getChunkLength(), data, offset);

            if (chunkInfo.hasChunkChecksum()) {
                ByteString chunkInfoChecksum = checksum(chunkInfo);
                ByteString decryptedChecksum = checksum(decrypted);

                if (!chunkInfoChecksum.equals(decryptedChecksum)) {
                    logger.warn("-- decryptChunk() -  checksum failed: {} expected: {}",
                            Hex.encodeHexString(decryptedChecksum.toByteArray()),
                            Hex.encodeHexString(chunkInfoChecksum.toByteArray()));
                    // TODO empty Optional
                } else {
                    logger.debug("-- decryptChunk() - checksum passed");
                }
            }
            return Optional.of(decrypted);

        } catch (DataLengthException | ArrayIndexOutOfBoundsException | NullPointerException ex) {
            logger.warn("-- decryptChunk() - error: {}", ex);
            return Optional.empty();
        }
    }

    byte[] decryptData(KeyParameter key, int length, byte[] data, int offset) {
        StreamBlockCipher cipher = cipherSupplier.get();
        cipher.init(false, key);
        byte[] decrypted = new byte[length];
        cipher.processBytes(data, offset, length, decrypted, 0);
        return decrypted;
    }

    ByteString checksum(ChunkServer.ChunkInfo chunkInfo) {
        return chunkInfo.getChunkChecksum().substring(1);
    }

    KeyParameter key(ChunkServer.ChunkInfo chunkInfo) {
        // TODO bounds check/ Optional.
        byte[] wrappedKey = chunkInfo.getChunkEncryptionKey().substring(0x01, 0x19).toByteArray();
        logger.debug("-- key() - wrapped key: 0x{}", Hex.encodeHexString(wrappedKey));

        byte[] key = decryptor.apply(wrappedKey).get(); // TODO unsafe get
        logger.debug("-- key() - key: 0x{}", Hex.encodeHexString(key));

        return new KeyParameter(key);
    }

    byte keyType(ChunkServer.ChunkInfo chunkInfo) {
        return chunkInfo.getChunkEncryptionKey().substring(0, 1).byteAt(0);
    }

    ByteString checksum(byte[] data) {
        Digest digest = digestSupplier.get();

        byte[] hash = new byte[digest.getDigestSize()];
        byte[] hashHash = new byte[digest.getDigestSize()];

        digest.reset();
        digest.update(data, 0, data.length);
        digest.doFinal(hash, 0);
        digest.update(hash, 0, hash.length);
        digest.doFinal(hashHash, 0);

        return ByteString.copyFrom(hashHash).substring(0, 20);
    }
}
