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

import com.github.horrorho.inflatabledonkey.exception.BadDataException;
import com.github.horrorho.inflatabledonkey.protocol.ChunkServer;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.StreamBlockCipher;
import org.bouncycastle.crypto.digests.GeneralDigest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.engines.AESEngine;
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
@ThreadSafe
public final class ChunkDecrypter implements BiFunction<List<ChunkServer.ChunkInfo>, byte[], List<byte[]>> {

    public static ChunkDecrypter instance() {
        return instance;
    }

    private static final ChunkDecrypter instance = new ChunkDecrypter(
            () -> new CFBBlockCipher(new AESEngine(), 128),
            () -> new SHA256Digest());

    private static final Logger logger = LoggerFactory.getLogger(ChunkDecrypter.class);

    private final Supplier<StreamBlockCipher> cipherSupplier;
    private final Supplier<GeneralDigest> digestSupplier;

    public ChunkDecrypter(Supplier<StreamBlockCipher> cipherSupplier, Supplier<GeneralDigest> digestSupplier) {
        this.cipherSupplier = Objects.requireNonNull(cipherSupplier);
        this.digestSupplier = Objects.requireNonNull(digestSupplier);
    }

    @Override
    public List<byte[]> apply(List<ChunkServer.ChunkInfo> chunkInfoList, byte[] data) throws UncheckedIOException {

        try {
            List<byte[]> decrypted = new ArrayList<>();

            int offset = 0;
            for (ChunkServer.ChunkInfo chunkInfo : chunkInfoList) {
                byte[] decryptedChunk = decryptChunk(chunkInfo, data, offset);
                decrypted.add(decryptedChunk);
                offset += chunkInfo.getChunkLength();
            }

            return decrypted;

        } catch (BadDataException ex) {
            throw new UncheckedIOException(ex);
        }
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

    byte[] decryptChunk(ChunkServer.ChunkInfo chunkInfo, byte[] data, int offset) throws BadDataException {
        logger.debug("-- decryptChunk() - chunk info: {}", chunkInfo, data.length);

        logger.debug("-- decryptChunk() - chunk checksum: {}", 
                Hex.encodeHexString(chunkInfo.getChunkChecksum().toByteArray()));
        logger.debug("-- decryptChunk() - chunk encryption key: {}", 
                Hex.encodeHexString(chunkInfo.getChunkEncryptionKey().toByteArray()));
        logger.debug("-- decryptChunk() - chunk length: 0x{}", Integer.toHexString(chunkInfo.getChunkLength()));

        String filename = Hex.encodeHexString(chunkInfo.getChunkChecksum().toByteArray()) + ".bin";
        logger.debug("-- decryptChunk() - dumping data to: {}", filename);
        write(filename, data, offset, chunkInfo.getChunkLength());

        try {
            if (!chunkInfo.hasChunkEncryptionKey()) {
                throw new BadDataException("Missing key");
            }

//            if (keyType(chunkInfo) != 1) {
//                throw new BadDataException("Unknown key type: " + keyType(chunkInfo));
//            }

            byte[] decrypted = decryptData(key(chunkInfo), chunkInfo.getChunkLength(), data, offset);

            if (chunkInfo.hasChunkChecksum()) {
                ByteString chunkInfoChecksum = checksum(chunkInfo);
                ByteString decryptedChecksum = checksum(decrypted);

                if (!chunkInfoChecksum.equals(decryptedChecksum)) {
                    logger.debug("-- decryptChunk() >  checksum failed: {} expected: {}",
                            Hex.encodeHexString(decryptedChecksum.toByteArray()),
                            Hex.encodeHexString(chunkInfoChecksum.toByteArray()));
                    throw new BadDataException("Decrypt bad checksum");
                }
            }
            return decrypted;

        } catch (DataLengthException | ArrayIndexOutOfBoundsException | NullPointerException ex) {
            throw new BadDataException("Decrypt failed", ex);
        }
    }

    byte[] decryptData(KeyParameter key, int length, byte[] data, int offset) throws BadDataException {
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
        byte[] key = chunkInfo.getChunkEncryptionKey().substring(9, 25).toByteArray();
        logger.debug("-- key() - key: 0x{}", Hex.encodeHexString(key));
        return new KeyParameter(key);
    }

    byte keyType(ChunkServer.ChunkInfo chunkInfo) {
        return chunkInfo.getChunkEncryptionKey().substring(0, 1).byteAt(0);
    }

    ByteString checksum(byte[] data) {
        GeneralDigest digest = digestSupplier.get();

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
