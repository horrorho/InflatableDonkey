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
package com.github.horrorho.inflatabledonkey.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DirectoryAssistant.
 *
 * @author Ahseya
 */
@Immutable
public final class DirectoryAssistant {

    private static final Logger logger = LoggerFactory.getLogger(DirectoryAssistant.class);

    public static boolean createParent(Path file) {
        Path parent = file.toAbsolutePath().normalize().getParent();
        if (parent == null) {
            return true;
        }

        return create(parent);
    }

    public static boolean create(Path directory) {
        if (Files.exists(directory)) {
            if (Files.isDirectory(directory)) {
                return true;

            } else {
                logger.warn("-- create() - directory path exists but is not a directory: {}", directory);
                return false;
            }
        }

        try {
            Files.createDirectories(directory);
            logger.debug("-- create() - directory created: {}", directory);
            return true;

        } catch (IOException ex) {
            logger.debug("-- create() - IOException: {}", ex);
            return false;
        }
    }

    public static boolean deleteEmptyTree(Path base, Path folder) {
        return doDeleteEmptyTree(base.normalize().toAbsolutePath(), folder.normalize().toAbsolutePath());
    }

    private static boolean doDeleteEmptyTree(Path base, Path directory) {
        if (!directory.startsWith(base)) {
            logger.warn("-- doDeleteEmptyTree() - path not in base directory, base: {} directory: {}", base, directory);
            return false;
        }
        if (!Files.exists(directory)) {
            logger.warn("-- doDeleteEmptyTree() - directory doesn't exist: {}", directory);
            return false;
        }

        boolean deleted = false;
        try {
            while (!directory.equals(base)) {
                if (!Files.isDirectory(directory)) {
                    logger.warn("-- doDeleteEmptyTree() - not a directory: {}", directory);
                    break;
                }
                if (Files.list(directory).findFirst().isPresent()) {
                    logger.debug("-- doDeleteEmptyTree() - directory not empty: {}", directory);
                    break;
                }

                Files.delete(directory);
                logger.debug(" --doDeleteEmptyTree() - deleted: {}", directory);
                deleted = true;

                directory = directory.getParent();
            }
        } catch (IOException ex) {
            logger.warn("-- doDeleteEmptyTree() - IOException: ", ex);
        }
        return deleted;
    }
}
