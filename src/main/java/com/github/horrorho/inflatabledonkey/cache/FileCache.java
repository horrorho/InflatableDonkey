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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.crypto.RuntimeCryptoException;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.io.InvalidCipherTextIOException;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ayesha
 */
@Immutable
public final class FileCache {

    public static FileCache defaultInstance() {
        StreamCryptorPBKDF2 kdf = new StreamCryptorPBKDF2(SHA256Digest::new, 4096, 32);
        StreamCryptor streamCryptor = new StreamCryptor(kdf, 32, 12, 16);
        return new FileCache(streamCryptor);
    }

    private static final Logger logger = LoggerFactory.getLogger(FileCache.class);

    private final StreamCryptor fileCryptor;

    FileCache(StreamCryptor fileCryptor) {
        this.fileCryptor = Objects.requireNonNull(fileCryptor);
    }

    public boolean remove(Path file) throws IOException {
        boolean isDeleted = Files.deleteIfExists(file);
        logger.debug("-- remove() - file: {} deleted: {}", file, isDeleted);
        return isDeleted;
    }

    public void store(Path file, byte[] password, byte[] bs) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (OutputStream os = fileCryptor.newCipherOutputStream(baos, password)) {
            os.write(bs);
        }
        AtomicWriter.write(file, baos.toByteArray());
        logger.debug("-- store() - file: {} bs: 0x{}", file, Hex.toHexString(bs));
    }

    public Optional<byte[]> load(Path file, byte[] password) throws IOException {
        byte[] bs = null;
        if (Files.exists(file)) {
            try (InputStream is = fileCryptor.newCipherInputStream(Files.newInputStream(file), password)) {
                bs = IOUtils.toByteArray(is);
            } catch (RuntimeCryptoException | InvalidCipherTextIOException ex) {
                logger.debug("-- load() - exception: ", ex);
            }
        } else {
            logger.debug("-- load() - no file: {}", file);
        }
        Optional<byte[]> _bs = Optional.ofNullable(bs);
        logger.debug("-- load() - file: {} bs: 0x{}", file, _bs.map(Hex::toHexString));
        return _bs;
    }

    @Override
    public String toString() {
        return "FileCache{" + "fileCryptor=" + fileCryptor + '}';
    }
}
