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

import com.github.horrorho.inflatabledonkey.io.InputReferenceStream;
import com.github.horrorho.inflatabledonkey.args.Property;
import com.github.horrorho.inflatabledonkey.chunk.Chunk;
import com.github.horrorho.inflatabledonkey.crypto.xts.XTSAESBlockCipher;
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
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.io.CipherInputStream;
import org.bouncycastle.crypto.io.DigestInputStream;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FileAssembler.
 *
 * @author Ahseya
 */
@Immutable
public final class FileAssembler implements BiConsumer<Asset, List<Chunk>>, BiPredicate<Asset, List<Chunk>> {

    private static final Logger logger = LoggerFactory.getLogger(FileAssembler.class);

    private static final int BUFFER_SIZE = Property.FILE_ASSEMBLER_BUFFER_LENGTH.intValue().orElse(8192);
    private static final int DATA_UNIT_SIZE = 0x1000; // TODO inject via Property

    private final Function<EncryptionKeyBlob, Optional<byte[]>> fileKey;
    private final FilePath filePath;

    public FileAssembler(Function<EncryptionKeyBlob, Optional<byte[]>> fileKey, FilePath filePath) {
        this.fileKey = Objects.requireNonNull(fileKey, "fileKey");
        this.filePath = Objects.requireNonNull(filePath, "filePath");
    }

    public FileAssembler(Function<EncryptionKeyBlob, Optional<byte[]>> fileKey, Path outputFolder) {
        this(fileKey, new FilePath(outputFolder));
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
                .orElseGet(() -> assemble(path, chunks, Optional.empty(), false, asset.fileChecksum()));
    }

    public boolean unwrapKeyOp(Path path, Asset asset, List<Chunk> chunks, byte[] encryptionKey) {
        return EncryptionKeyBlob.create(encryptionKey)
                .map(fileKeyMetaData -> unwrapKeyOp(path, asset, chunks, fileKeyMetaData))
                .orElseGet(() -> {
                    logger.warn("-- unwrapKeyOp() - failed to extract file key metadata");
                    return false;
                });
    }

    public boolean unwrapKeyOp(Path path, Asset asset, List<Chunk> chunks, EncryptionKeyBlob fileKeyMetaData) {
        if (asset.protectionClass() != fileKeyMetaData.protectionClass()) {
            logger.warn("-- unwrapKeyOp() - negative protection class match asset: {} metadata: {}",
                    asset.protectionClass(), fileKeyMetaData.protectionClass());
        }

        return fileKey.apply(fileKeyMetaData)
                .map(key -> assemble(path, chunks, Optional.of(key), fileKeyMetaData.isXTS(), asset.fileChecksum()))
                .orElseGet(() -> {
                    logger.warn("-- unwrapKeyOp() - failed to recover file key: ", fileKeyMetaData);
                    return false;
                });
    }

    public boolean assemble(Path path, List<Chunk> chunks, Optional<byte[]> decryptKey, boolean isXTS, Optional<byte[]> fileChecksum) {
        logger.debug("-- assemble() - path: {} decryptKey: {} fileChecksum: {} size: {}", path,
                decryptKey.map(Hex::toHexString), fileChecksum.map(Hex::toHexString));
        try (OutputStream out = Files.newOutputStream(path);
                InputReferenceStream<Optional<DigestInputStream>> in = chain(chunks, decryptKey, isXTS, fileChecksum)) {

            IOUtils.copyLarge(in, out, new byte[BUFFER_SIZE]);

            boolean status = fileChecksum.map(fc -> testFileChecksum(fc, in.reference()))
                    .orElse(true);
            logger.info("-- assemble() - written: {} status: {}", path, status);
            return status;

        } catch (IOException | DataLengthException | IllegalStateException ex) {
            logger.warn("-- assemble() - error: ", ex);
            return false;
        }
    }

    InputReferenceStream<Optional<DigestInputStream>>
            chain(List<Chunk> chunks, Optional<byte[]> decryptKey, boolean isXTS, Optional<byte[]> fileChecksum) {
        return chainFileChecksumOp(fileData(chunks), decryptKey, isXTS, fileChecksum);
    }

    InputReferenceStream<Optional<DigestInputStream>>
            chainFileChecksumOp(InputStream input, Optional<byte[]> fileKey, boolean isXTS, Optional<byte[]> fileChecksum) {
        return fileChecksum
                .flatMap(fc -> FileSignatures.like(input, fc))
                .map(dis -> chainDecryptOp(dis, Optional.of(dis), fileKey, isXTS))
                .orElseGet(() -> chainDecryptOp(input, Optional.empty(), fileKey, isXTS));
    }

    InputReferenceStream<Optional<DigestInputStream>>
            chainDecryptOp(InputStream input, Optional<DigestInputStream> digestInput, Optional<byte[]> fileKey, boolean isXTS) {
        return fileKey
                .map(KeyParameter::new)
                .map(key -> cipher(isXTS, key))
                .map(BufferedBlockCipher::new)
                .map(cipher -> (InputStream) new CipherInputStream(input, cipher))
                .map(cis -> new InputReferenceStream<>(cis, digestInput))
                .orElseGet(() -> new InputReferenceStream<>(input, digestInput));
    }

    BlockCipher cipher(boolean isXTS, KeyParameter fileKey) {
        BlockCipher cipher = isXTS
                ? new XTSAESBlockCipher(this::iosTweakFunction, DATA_UNIT_SIZE)
                : new FileBlockCipher();
        cipher.init(false, fileKey);
        return cipher;
    }

    byte[] iosTweakFunction(long tweakValue) {
        byte[] bs = Pack.longToLittleEndian(tweakValue);
        return Arrays.concatenate(bs, bs);
    }

    InputStream fileData(List<Chunk> chunks) {
        List<InputStream> inputStreams = chunks.stream()
                .map(Chunk::inputStream)
                .collect(Collectors.toList());
        Enumeration<InputStream> enumeration = Collections.enumeration(inputStreams);
        return new SequenceInputStream(enumeration);
    }

    boolean testFileChecksum(byte[] fileChecksum, Optional<DigestInputStream> digestInputStream) {
        return digestInputStream.map(dis -> FileSignatures.compare(dis, fileChecksum))
                .orElse(true);
    }
}
