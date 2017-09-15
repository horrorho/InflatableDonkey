/*
 * The MIT License
 *
 * Copyright 2017 Ayesha.
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
package com.github.horrorho.inflatabledonkey.cache;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import javax.annotation.concurrent.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 *
 * @author Ayesha
 */
@Immutable
public final class AtomicWriter {

    static void createParent(Path file) throws IOException {
        Path parent = file.normalize().toAbsolutePath().getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }

    static final Path tmp(Path path) throws IOException {
        if (Files.exists(path) && !Files.isRegularFile(path)) {
            throw new IOException("not a file: " + path);
        }
        return path.resolveSibling(path.getFileName() + ".tmp");
    }

    static void move(Path source, Path target) throws IOException {
        try {
            Files.move(source, target, ATOMIC_MOVE, REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException ex) {
            logger.debug(shortError, "-- move() - fallback, atomic move not supported: ", ex);
            Files.move(source, target, REPLACE_EXISTING);
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(AtomicWriter.class);
    private static final Marker shortError = MarkerFactory.getMarker("SHORT_ERROR");

    public static void write(Path path, byte[] data) throws IOException {
        Path tmp = tmp(path);
        createParent(tmp);
        try {
            Files.write(tmp, data);
            move(tmp, path);
        } finally {
            Files.deleteIfExists(tmp);
        }
    }
}
