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
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FileAssembler.
 *
 * @author Ahseya
 */
@Immutable
public final class FileAssembler {

    private static final int BUFFER_SIZE = Property.FILE_ASSEMBLER_BUFFER_LENGTH.intValue().orElse(8192);

    private static final Logger logger = LoggerFactory.getLogger(FileAssembler.class);

    public static void
            assemble(Path file, List<Chunk> chunkData, int length, Optional<byte[]> key) throws UncheckedIOException {

        logger.debug("-- assemble() - file: {} chunks: {} length: {} key: 0x{}",
                file, chunkData.size(), length, key.map(Hex::toHexString).orElse("NULL"));
        logger.info("-- assemble() - file: {}", file);

        if (!createDirectories(file)) {
            return;
        }
        copy(file, chunkData, key
        );
        truncate(file, length);
    }

    static void copy(Path file, List<Chunk> chunkData, Optional<byte[]> key) throws UncheckedIOException {
        try (OutputStream output = Files.newOutputStream(file, CREATE, WRITE, TRUNCATE_EXISTING);
                InputStream input = inputStream(chunkData, key)) {

            IOUtils.copyLarge(input, output);

        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    static InputStream inputStream(List<Chunk> chunkData, Optional<byte[]> key) throws UncheckedIOException {
        return key.isPresent()
                ? inputStream(chunkData, key.get())
                : inputStream(chunkData);
    }

    static InputStream inputStream(List<Chunk> chunkData, byte[] key) throws UncheckedIOException {
        InputStream inputStream = inputStream(chunkData);
        return FileDecrypterInputStreams.create(inputStream, key);
    }

    static InputStream inputStream(List<Chunk> chunkData) throws UncheckedIOException {
        List<InputStream> inputStreams = chunkData.stream()
                .map(Chunk::inputStream)
                .collect(Collectors.toList());

        Enumeration<InputStream> enumeration = Collections.enumeration(inputStreams);
        return new BufferedInputStream(new SequenceInputStream(enumeration), BUFFER_SIZE);
    }

    static boolean createDirectories(Path file) {
        Path parent = file.getParent();
        if (parent != null) {
            if (Files.exists(parent)) {
                if (Files.isDirectory(parent)) {
                    return true;

                } else {
                    logger.warn("-- createDirectories() - path exists but is not a directory: {}", file);
                    return false;
                }
            }

            try {
                Files.createDirectories(parent);
                return true;

            } catch (IOException ex) {
                logger.debug("-- createDirectories() - IOException: {}", ex);
                return false;
            }
        }
        return true;
    }

    static void truncate(Path file, long to) throws UncheckedIOException {
        // TODO should really limit our written data stream.
        try {
            if (to == 0) {
                return;
            }

            long size = Files.size(file);
            if (size > to) {
                Files.newByteChannel(file, WRITE)
                        .truncate(to)
                        .close();
                logger.debug("-- truncate() - truncated: {}, {} > {}", file, size, to);

            } else if (size < to) {
                logger.warn("-- truncate() - cannot truncate: {}, {} > {}", file, size, to);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
