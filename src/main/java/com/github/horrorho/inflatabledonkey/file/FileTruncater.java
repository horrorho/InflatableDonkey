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

import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.WRITE;
import java.util.Objects;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FileTruncater.
 *
 * @author Ahseya
 */
@Immutable
public final class FileTruncater {

    private static final Logger logger = LoggerFactory.getLogger(FileTruncater.class);

    public static boolean truncate(Path path, Asset asset) {
        try {
            // Truncate if the final attribute size is different from the initial size (padded/ encrypted).
            // Don't truncate if either optional is missing.
            logger.debug("-- truncate() - path: {} asset: {}", path, asset);
            asset.attributeSize()
                    .filter(u -> asset.size().map(t -> !Objects.equals(t, u)).orElse(false))
                    //                    .filter(u -> asset.sizeBeforeCopy().isPresent())
                    .filter(u -> !asset.contentCompressionMethod().isPresent())
                    .ifPresent(u -> FileTruncater.truncate(path, u));
            return true;
        } catch (UncheckedIOException ex) {
            logger.warn("-- truncate() - UncheckedIOException: ", ex);
            return false;
        }
    }

    public static boolean checkSize(Path file, long size) {
        try {
            return Files.size(file) == size;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static void truncate(Path file, long to) throws UncheckedIOException {
        try {
            if (to <= 0) {  // TODO consider:  to < 0
                return;
            }
            logger.info("-- truncate() - file: {} -> {}", file, to);
            long size = Files.size(file);
            if (size > to) {
                Files.newByteChannel(file, WRITE)
                        .truncate(to)
                        .close();
            } else if (size < to) {
                logger.warn("-- truncate() - cannot truncate: {}, {} > {}", file, size, to);
            }

        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
