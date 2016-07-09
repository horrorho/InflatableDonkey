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
package com.github.horrorho.inflatabledonkey.pcs.xfile;

import com.github.horrorho.inflatabledonkey.args.Property;
import com.github.horrorho.inflatabledonkey.chunk.Chunk;
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.io.DigestInputStream;
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
    private static final UnaryOperator<String> CLEAN = FileNameCleaners.instance();

    private final BiFunction<byte[], Integer, Optional<byte[]>> unwrapKey;
    private final Path outputFolder;

    public FileAssembler(BiFunction<byte[], Integer, Optional<byte[]>> unwrapKey, Path outputFolder) {
        this.unwrapKey = Objects.requireNonNull(unwrapKey, "unwrapKey");
        this.outputFolder = Objects.requireNonNull(outputFolder);
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
        logger.trace("<< test() - asset: {}", asset);
        boolean success = decryptOperator(asset)
                .map(decrypt -> folderOp(asset, chunks, decrypt))
                .orElse(false);

        logger.trace(">> test() - success: {}", success);
        return success;
    }

    public boolean folderOp(Asset asset, List<Chunk> chunks, UnaryOperator<InputStream> chain) {
        return path(asset)
                .filter(DirectoryAssistant::createParent)
                .map(file -> assembleOp(file, chunks, chain, asset.fileChecksum()))
                .orElse(false);
    }

    boolean assembleOp(Path file, List<Chunk> chunks, UnaryOperator<InputStream> chain, Optional<byte[]> fileChecksum) {
        try (OutputStream out = Files.newOutputStream(file);
                InputStream in = chain.apply(fileData(chunks))) {

            Optional<DigestInputStream> digestInputStream = fileChecksum.flatMap(FileSignatureInputStreams::factoryFor)
                    .map(factory -> factory.apply(in));

            return digestInputStream.isPresent()
                    ? write(digestInputStream.get(), out, fileChecksum.get())
                    : write(in, out);

        } catch (IOException ex) {
            logger.warn("-- assembleOp() - file error: ", ex);
            return false;
        }
    }

    boolean write(InputStream in, OutputStream out) throws IOException {
        IOUtils.copyLarge(in, out, new byte[BUFFER_SIZE]);
        return true;
    }

    boolean write(DigestInputStream in, OutputStream out, byte[] fileChecksum) throws IOException {
        IOUtils.copyLarge(in, out, new byte[BUFFER_SIZE]);
        return fileChecksumTest(fileChecksum, in.getDigest());
    }

    InputStream fileData(List<Chunk> chunkData) {
        List<InputStream> inputStreams = chunkData.stream()
                .map(Chunk::inputStream)
                .collect(Collectors.toList());
        Enumeration<InputStream> enumeration = Collections.enumeration(inputStreams);
        return new SequenceInputStream(enumeration);
    }

    Optional<Path> path(Asset asset) {
        if (!asset.domain().isPresent()) {
            logger.warn("-- path() - asset has no domain: {}", asset);
            return Optional.empty();
        }

        if (!asset.relativePath().isPresent()) {
            logger.warn("-- path() - asset has no relativePath: {}", asset);
            return Optional.empty();
        }

        Path path = outputFolder
                .resolve(CLEAN.apply(asset.domain().get()))
                .resolve(CLEAN.apply(asset.relativePath().get()));
        return Optional.of(path);
    }

    Optional<UnaryOperator<InputStream>> decryptOperator(Asset asset) {
        return asset.encryptionKey()
                .map(wrappedKey -> decryptOperator(wrappedKey, asset.protectionClass()))
                .orElse(Optional.of(UnaryOperator.identity()));
    }

    Optional<UnaryOperator<InputStream>> decryptOperator(byte[] wrappedKey, int protectionClass) {
        return unwrapKey.apply(wrappedKey, protectionClass)
                .map(key -> inputStream -> FileDecrypterInputStreams.create(inputStream, key));
    }

    boolean fileChecksumTest(byte[] fileChecksum, Digest digest) {
        byte[] expected = Arrays.copyOfRange(fileChecksum, 1, fileChecksum.length);

        byte[] out = new byte[digest.getDigestSize()];
        digest.doFinal(out, 0);

        boolean equals = Arrays.equals(out, expected);
        if (!equals) {
            logger.warn("-- fileChecksumTest() - negative digest match, assembled: 0x{} expected: 0x{}",
                    Hex.toHexString(out), Hex.toHexString(expected));
        } else {
            logger.debug("-- fileChecksumTest() - positive digest match, assembled: 0x{} expected: 0x{}",
                    Hex.toHexString(out), Hex.toHexString(expected));
        }
        return equals;
    }
}
