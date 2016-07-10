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
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.crypto.io.DigestInputStream;
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

    private final BiFunction<byte[], Integer, Optional<byte[]>> unwrapKey;
    private final FilePath filePath;

    public FileAssembler(BiFunction<byte[], Integer, Optional<byte[]>> unwrapKey, FilePath filePath) {
        this.unwrapKey = Objects.requireNonNull(unwrapKey, "unwrapKey");
        this.filePath = Objects.requireNonNull(filePath, "filePath");
    }

    public FileAssembler(BiFunction<byte[], Integer, Optional<byte[]>> unwrapKey, Path outputFolder) {
        this(unwrapKey, new FilePath(outputFolder));
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
        boolean success = filePath.apply(asset)
                .filter(DirectoryAssistant::createParent)
                .map(path -> encryptionkeyOp(path, asset, chunks))
                .orElse(false);
        logger.trace(">> test() - success: {}", success);
        return success;
    }

    public boolean encryptionkeyOp(Path path, Asset asset, List<Chunk> chunks) {
        return asset.encryptionKey()
                .map(wrappedKey -> unwrapKeyOp(path, asset, chunks, wrappedKey))
                .orElseGet(() -> assemble(path, chunks, Optional.empty(), asset.fileChecksum()));
    }

    public boolean unwrapKeyOp(Path path, Asset asset, List<Chunk> chunks, byte[] wrappedKey) {
        Boolean orElse = unwrapKey.apply(wrappedKey, asset.protectionClass())
                .map(key -> assemble(path, chunks, Optional.of(key), asset.fileChecksum()))
                .orElse(false);
        return orElse;
    }

    public boolean assemble(Path path, List<Chunk> chunks, Optional<byte[]> decryptKey, Optional<byte[]> fileChecksum) {
        try (OutputStream out = Files.newOutputStream(path);
                InputReferenceStream<Optional<DigestInputStream>> in = chain(chunks, decryptKey, fileChecksum)) {

            IOUtils.copyLarge(in, out, new byte[BUFFER_SIZE]);

            boolean status = fileChecksum.map(fc -> testFileChecksum(fc, in.reference()))
                    .orElse(true);
            logger.info("-- assemble() - written: {} complete: {}", path, status);

            return status;

        } catch (IOException ex) {
            logger.warn("-- assemble() - file error: ", ex);
            return false;
        }
    }

    InputReferenceStream<Optional<DigestInputStream>>
            chain(List<Chunk> chunks, Optional<byte[]> decryptKey, Optional<byte[]> fileChecksum) {

        InputStream one = fileData(chunks);

        Optional<DigestInputStream> digestInputStream = fileChecksum.flatMap(fc -> FileSignatures.like(one, fc));
        InputStream two = digestInputStream.map(dis -> (InputStream) dis)
                .orElse(one);

        InputStream three = decryptKey.map(key -> (InputStream) FileDecrypterInputStreams.create(two, key))
                .orElse(two);

        return new InputReferenceStream<>(three, digestInputStream);
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
