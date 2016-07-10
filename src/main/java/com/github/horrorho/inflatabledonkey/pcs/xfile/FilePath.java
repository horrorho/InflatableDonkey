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

import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FilePath.
 *
 * @author Ahseya
 */
@Immutable
public final class FilePath implements Function<Asset, Optional<Path>> {

    private static final UnaryOperator<String> CLEAN = FileNameCleaners.instance();

    private static final Logger logger = LoggerFactory.getLogger(FilePath.class);

    private final Path outputFolder;

    public FilePath(Path outputFolder) {
        this.outputFolder = Objects.requireNonNull(outputFolder, "outputFolder");
    }

    public Optional<Path> apply(Asset asset) {
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
}
