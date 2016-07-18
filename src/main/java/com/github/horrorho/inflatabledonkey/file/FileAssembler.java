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
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.DataLengthException;
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

    private final Optional<Supplier<BlockCipher>> cipherSupplier;
    private final Function<EncryptionKeyBlob, Optional<byte[]>> fileKeyDecrypter;
    private final FilePath filePath;

    public FileAssembler(
            Optional<Supplier<BlockCipher>> cipherSupplier,
            Function<EncryptionKeyBlob, Optional<byte[]>> fileKeyDecrypter,
            FilePath filePath) {

        this.cipherSupplier = Objects.requireNonNull(cipherSupplier, "cipherSupplier");
        this.fileKeyDecrypter = Objects.requireNonNull(fileKeyDecrypter, "fileKeyDecrypter");
        this.filePath = Objects.requireNonNull(filePath, "filePath");
    }

    public FileAssembler(
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
        boolean success = assemble(asset, chunks);
        logger.trace(">> test() - success: {}", success);
        return success;
    }

    boolean assemble(Asset asset, List<Chunk> chunks) {
        return filePath.apply(asset)
                .filter(DirectoryAssistant::createParent)
                .filter(path -> write(path, chunks, fileKeyCipher(asset), asset.fileChecksum()))
                .map(path -> truncate(path, asset))
                .orElse(false);
    }

    boolean write(Path path, List<Chunk> chunks, Optional<FileKeyCipher> keyCipher, Optional<byte[]> signature) {
        logger.debug("-- write() - path: {} key cipher: {} signature: 0x{}",
                path, keyCipher, signature.map(Hex::toHexString).orElse("NULL"));

        try (OutputStream out = Files.newOutputStream(path);
                InputStream in = chunkStream(chunks)) {

            boolean status = FileStreamWriter.copy(in, out, keyCipher, signature);

            logger.info("-- write() - written: {} status: {}", path, status);
            return status;

        } catch (IOException | DataLengthException | IllegalStateException ex) {
            logger.warn("-- write() - error: ", ex);
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

    Optional<FileKeyCipher> fileKeyCipher(Asset asset) {
        return cipherSupplier
                .flatMap(ciphers -> fileKeyCipher(asset, ciphers));
    }

    Optional<FileKeyCipher> fileKeyCipher(Asset asset, Supplier<BlockCipher> ciphers) {
        return asset.encryptionKey()
                .flatMap(EncryptionKeyBlob::create)
                .flatMap(fileKeyDecrypter::apply)
                .map(fileKey -> new FileKeyCipher(fileKey, ciphers));
    }

    boolean truncate(Path path, Asset asset) {
        try {
            asset.attributeSize()
                    .filter(size -> size != asset.size())
                    .ifPresent(size -> FileTruncater.truncate(path, size));
            return true;

        } catch (UncheckedIOException ex) {
            logger.warn("-- truncate() - UncheckedIOException: ", ex);
            return false;
        }
    }
}
