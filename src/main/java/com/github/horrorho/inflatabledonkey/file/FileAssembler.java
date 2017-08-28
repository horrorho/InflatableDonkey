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
import com.github.horrorho.inflatabledonkey.io.DirectoryAssistant;
import com.github.horrorho.inflatabledonkey.io.IOFunction;
import com.github.horrorho.inflatabledonkey.io.IOSupplier;
import com.github.horrorho.inflatabledonkey.io.IOSupplierSequenceStream;
import com.github.horrorho.inflatabledonkey.util.LZFSEExtInputStream;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * FileAssembler.
 *
 * @author Ahseya
 */
@Immutable
public final class FileAssembler
        implements BiConsumer<Asset, Optional<List<Chunk>>>, BiPredicate<Asset, Optional<List<Chunk>>> {

    private static final Logger logger = LoggerFactory.getLogger(FileAssembler.class);

    private static final Path LZFSE = Property.PATH_LZFSE.as(Paths::get).orElse(null);

    private final Function<byte[], Optional<XFileKey>> fileKeys;
    private final UnaryOperator<Optional<XFileKey>> mutator;
    private final FilePath filePath;

    public FileAssembler(
            Function<byte[], Optional<XFileKey>> fileKeys,
            UnaryOperator<Optional<XFileKey>> mutator,
            FilePath filePath) {

        this.fileKeys = Objects.requireNonNull(fileKeys, "fileKeys");
        this.mutator = Objects.requireNonNull(mutator, "mutator");
        this.filePath = Objects.requireNonNull(filePath, "filePath");
    }

    public FileAssembler(Function<byte[], Optional<XFileKey>> fileKeys, Path outputFolder) {
        this(fileKeys, XFileKeyMutatorFactory.defaults(), new FilePath(outputFolder));
    }

    @Override
    public void accept(Asset asset, Optional<List<Chunk>> chunks) {
        boolean test = test(asset, chunks);
        if (!test) {
            logger.debug("-- accept() - failed to write asset: {}", asset.relativePath());
        }
    }

    @Override
    public boolean test(Asset asset, Optional<List<Chunk>> chunks) {
        logger.trace("<< test() - asset: {} chunks: {}",
                asset, chunks.map(List::size).map(Object::toString).orElse("NULL"));

        boolean success = chunks.isPresent()
                ? assemble(asset, chunks.get())
                : fail(asset);

        logger.trace(">> test() - success: {}", success);
        return success;
    }

    boolean fail(Asset asset) {
        logger.error("-- " + info(asset) + " failed");
        return false;
    }

    boolean assemble(Asset asset, List<Chunk> chunks) {
        return filePath.apply(asset)
                .filter(DirectoryAssistant::createParent)
                .filter(path -> assemble(path, asset, chunks))
                .filter(path -> FileTruncater.truncate(path, asset))
                .map(path -> FileTimestamp.set(path, asset))
                .orElse(false);
    }

    boolean assemble(Path path, Asset asset, List<Chunk> chunks) {
        String info = info(asset);
        asset.contentCompressionMethod()
                .ifPresent(u -> logger.info("-- assemble() - content compression method: {} {}", info, u));
        asset.contentEncodingMethod()
                .ifPresent(u -> logger.info("-- assemble() - content encoding method: {} {}", info, u));
        return asset.encryptionKey()
                .map(u -> decrypt(path, info, chunks, u, asset.fileChecksum(), asset.contentCompressionMethod()))
                .orElseGet(() -> write(path, info, chunks, Optional.empty(), asset.fileChecksum(), asset.contentCompressionMethod()));
    }

    boolean decrypt(Path path,
            String info,
            List<Chunk> chunks,
            byte[] encryptionKey,
            Optional<byte[]> signature,
            Optional<Integer> compression) {
        return fileKeys.apply(encryptionKey)
                .map(Optional::of)
                .map(mutator)
                .map(u -> write(path, info, chunks, u, signature, compression))
                .orElseGet(() -> {
                    logger.warn("-- decrypt() - failed to unwrap encryption key");
                    return false;
                });
    }

    boolean write(Path path,
            String info,
            List<Chunk> chunks, Optional<XFileKey> keyCipher,
            Optional<byte[]> signature,
            Optional<Integer> compression) {
        logger.debug("-- write() - path: {} key cipher: {} signature: 0x{}",
                path, keyCipher, signature.map(Hex::toHexString).orElse("NULL"));

        boolean status = true;
        Optional<IOFunction<InputStream, InputStream>> decompress;
        if (compression.isPresent()) {
            if (compression.get() == 2) {
                if (LZFSE == null) {
                    logger.warn("-- write() - no  decompressor: {} -> {}", info, compression.get());
                    decompress = Optional.empty();
                } else {
                    decompress = Optional.of(u -> LZFSEExtInputStream.create(LZFSE, u));
                }
            } else {
                logger.warn("-- write() - unsupported compression: {} -> {}", info, compression.get());
                decompress = Optional.empty();
            }
        } else {
            decompress = Optional.empty();
        }

        try (OutputStream out = Files.newOutputStream(path);
                InputStream in = chunkStream(chunks)) {
            status &= FileStreamWriter.copy(in, out, keyCipher, signature, decompress);
//            status &= FileStreamWriter.copy(in, out, keyCipher, signature);

            if (keyCipher.isPresent()) {
                XFileKey kc = keyCipher.get();
                logger.debug("-- write() - written: {} status: {} mode: {} flags: 0x{}",
                        path, status, kc.ciphers(), Hex.toHexString(kc.flags()));
                logger.info(">> " + info + " " + kc.ciphers() + " " + Hex.toHexString(kc.flags()));
            } else {
                logger.debug("-- write() - written: {} status: {}", path, status);
                logger.info(">> " + info);
            }
            return status;

        } catch (IOException | DataLengthException | IllegalStateException ex) {
            logger.warn("-- write() - error: ", ex);
            return false;
        }
    }

    InputStream chunkStream(List<Chunk> chunks) throws IOException {
        // Changed from java.io.SequenceInputStream which required open InputStreams as this was causing 'Too many open 
        // files' exceptions on assets with huge numbers of chunks.
        List<IOSupplier<InputStream>> suppliers = new ArrayList<>();
        for (Chunk chunk : chunks) {
            IOSupplier<InputStream> ios = () -> chunk.inputStream()
                    .orElseThrow(()
                            -> new IllegalStateException("chunk deleted: 0x" + Hex.toHexString(chunk.checksum())));
            suppliers.add(ios);
        }
        return new IOSupplierSequenceStream(suppliers);
    }

    String info(Asset asset) {
        return asset.domain().orElse("") + " " + asset.relativePath().orElse("");
    }

    @Override
    public String toString() {
        return "FileAssembler{" + "fileKeys=" + fileKeys + ", mutator=" + mutator + ", filePath=" + filePath + '}';
    }
}
