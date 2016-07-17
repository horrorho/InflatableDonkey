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
package com.github.horrorho.inflatabledonkey.file;

import com.github.horrorho.inflatabledonkey.args.Property;
import com.github.horrorho.inflatabledonkey.chunk.Chunk;
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.io.CipherInputStream;
import org.bouncycastle.crypto.io.DigestInputStream;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FileAssembler.
 *
 * @author Ahseya
 */
@Immutable
public final class FileAssemblerLegacy implements BiConsumer<Asset, List<Chunk>>, BiPredicate<Asset, List<Chunk>> {

    private static final Logger logger = LoggerFactory.getLogger(FileAssemblerLegacy.class);

    private static final int BUFFER_SIZE = Property.FILE_WRITER_BUFFER_LENGTH.asInteger().orElse(8192);

    private final Optional<Supplier<BlockCipher>> ciphers;
    private final Function<EncryptionKeyBlob, Optional<byte[]>> fileKey;
    private final FilePath filePath;

    public FileAssemblerLegacy(
            Optional<Supplier<BlockCipher>> ciphers,
            Function<EncryptionKeyBlob, Optional<byte[]>> fileKey,
            FilePath filePath) {

        this.ciphers = Objects.requireNonNull(ciphers, "ciphers");
        this.fileKey = Objects.requireNonNull(fileKey, "fileKey");
        this.filePath = Objects.requireNonNull(filePath, "filePath");
    }

    public FileAssemblerLegacy(
            Optional<Supplier<BlockCipher>> ciphers,
            Function<EncryptionKeyBlob, Optional<byte[]>> fileKey,
            Path outputFolder) {

        this(ciphers, fileKey, new FilePath(outputFolder));
    }

    @Override
    public void accept(Asset asset, List<Chunk> chunks) {
        boolean test = test(asset, chunks);
        if (!test) {
            logger.warn("-- accept() - failed to write asset: {}", asset.relativePath());
        }
    }

    @Override
    public boolean test(Asset asset, List<Chunk> chunks) {
        logger.trace("<< test() - asset: {} chunks: {}", asset, chunks.size());
        boolean success = pathOp(asset, chunks);
        logger.trace(">> test() - success: {}", success);
        return success;
    }

    public boolean pathOp(Asset asset, List<Chunk> chunks) {
        return filePath.apply(asset)
                .filter(DirectoryAssistant::createParent)
                .map(path -> truncateOp(path, asset, chunks))
                .orElse(false);
    }

    public boolean truncateOp(Path path, Asset asset, List<Chunk> chunks) {
        if (encryptionKeyOp(path, asset, chunks)) {
            try {
                asset.attributeSize()
                        .filter(size -> size != asset.size())
                        .ifPresent(size -> FileTruncater.truncate(path, size));
                return true;
            } catch (UncheckedIOException ex) {
                logger.warn("-- truncateOp() - UncheckedIOException: ", ex);
            }
        }
        return false;
    }

    public boolean encryptionKeyOp(Path path, Asset asset, List<Chunk> chunks) {
        return asset.encryptionKey()
                .map(wrappedKey -> unwrapKeyOp(path, asset, chunks, wrappedKey))
                .orElseGet(() -> assemble(path, chunks, Optional.empty(), "", asset.fileChecksum()));
    }

    public boolean unwrapKeyOp(Path path, Asset asset, List<Chunk> chunks, byte[] encryptionKey) {
        return EncryptionKeyBlob.create(encryptionKey)
                .map(fileKeyMetaData -> unwrapKeyOp(path, asset, chunks, fileKeyMetaData))
                .orElseGet(() -> {
                    logger.warn("-- unwrapKeyOp() - failed to extract file key metadata");
                    return false;
                });
    }

    public boolean unwrapKeyOp(Path path, Asset asset, List<Chunk> chunks, EncryptionKeyBlob blob) {
        if (asset.protectionClass() != blob.protectionClass()) {
            logger.warn("-- unwrapKeyOp() - negative protection class match asset: {} metadata: {}",
                    asset.protectionClass(), blob.protectionClass());
        }

        String diagnostic = Integer.toHexString(blob.u3());

        return fileKey.apply(blob)
                .map(key -> assemble(path, chunks, Optional.of(key), diagnostic + " " + asset.protectionClass(), asset.fileChecksum()))
                .orElseGet(() -> {
                    logger.warn("-- unwrapKeyOp() - failed to recover file key: ", blob);
                    return false;
                });
    }

    public boolean assemble(Path path, List<Chunk> chunks, Optional<byte[]> key, String diagnostic, Optional<byte[]> signature) {
        logger.debug("-- assemble() - path: {} key: {} signature: {}",
                path, key.map(Hex::toHexString), signature.map(Hex::toHexString));

        Digest digest = signature.flatMap(FileSignature::type)
                .orElse(FileSignature.ONE)
                .newDigest();

        try (OutputStream out = Files.newOutputStream(path);
                DigestInputStream digestInputStream = new DigestInputStream(chunkStream(chunks), digest)) {

            IOUtils.copyLarge(FileAssemblerLegacy.this.decryptStream(digestInputStream, key), out, new byte[BUFFER_SIZE]);

            boolean status = FileAssemblerLegacy.this.testSignature(digestInputStream.getDigest(), signature);

            logger.info("-- assemble() - written: {} status: {} diagnostic: {}", path, status, diagnostic);
            return status;

        } catch (IOException | DataLengthException | IllegalStateException ex) {
            logger.warn("-- assemble() - error: ", ex);
            return false;
        }
    }

    InputStream chunkStream(List<Chunk> chunks) {
        List<InputStream> inputStreams = chunks.stream()
                .map(Chunk::inputStream)
                .collect(Collectors.toList());
        Enumeration<InputStream> enumeration = Collections.enumeration(inputStreams);
        return new SequenceInputStream(enumeration);
    }

    InputStream decryptStream(InputStream in, Optional<byte[]> key) {
        return key
                .flatMap(this::decryptStream)
                .map(BufferedBlockCipher::new)
                .map(cipher -> (InputStream) new CipherInputStream(in, cipher))
                .orElse(in);
    }

    Optional<BlockCipher> decryptStream(byte[] key) {
        return ciphers
                .map(Supplier::get)
                .map(cipher -> {
                    cipher.init(false, new KeyParameter(key));
                    return cipher;
                });
    }

    boolean testSignature(Digest digest, Optional<byte[]> signature) {
        return signature
                .map(c -> testSignature(digest, c))
                .orElseGet(() -> {
                    byte[] out = signature(digest);
                    logger.debug("-- testSignature() - signature: 0x{}", Hex.toHexString(out));
                    return true;
                });
    }

    boolean testSignature(Digest digest, byte[] signature) {
        byte[] out = signature(digest);
        boolean match = Arrays.areEqual(out, signature);
        if (match) {
            logger.debug("-- testSignature() - positive match out: 0x{} target: 0x{}",
                    Hex.toHexString(out), Hex.toHexString(signature));
        } else {

            logger.debug("-- testSignature() - negative match out: 0x{} target: 0x{}",
                    Hex.toHexString(out), Hex.toHexString(signature));
        }
        return match;
    }

    byte[] signature(Digest digest) {
        byte[] out = new byte[digest.getDigestSize()];
        digest.doFinal(out, 0);
        return out;
    }
}
