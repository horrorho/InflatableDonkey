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
import com.github.horrorho.inflatabledonkey.crypto.xts.XTSAESBlockCipher;
import com.github.horrorho.inflatabledonkey.io.InputReferenceStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.io.CipherInputStream;
import org.bouncycastle.crypto.io.DigestInputStream;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class FileStreams {

    public static InputReferenceStream<Optional<DigestInputStream>>
            inputStream(List<Chunk> chunks, Optional<FileKey> key, Optional<byte[]> checksum) {
        return fileChecksumOp(chunkStream(chunks), key, checksum);
    }

    public static InputReferenceStream<Optional<DigestInputStream>>
            fileChecksumOp(InputStream input, Optional<FileKey> key, Optional<byte[]> checksum) {
        return checksum
                .flatMap(fc -> FileSignatures.like(input, fc))
                .map(dis -> decryptOp(dis, Optional.of(dis), key))
                .orElseGet(() -> decryptOp(input, Optional.empty(), key));
    }

    static InputReferenceStream<Optional<DigestInputStream>>
            decryptOp(InputStream input, Optional<DigestInputStream> digestInput, Optional<FileKey> data) {
        return data
                .map(FileStreams::cipher)
                .map(BufferedBlockCipher::new)
                .map(cipher -> (InputStream) new CipherInputStream(input, cipher))
                .map(cis -> new InputReferenceStream<>(cis, digestInput))
                .orElseGet(() -> new InputReferenceStream<>(input, digestInput));
    }

    static BlockCipher cipher(FileKey key) {
        BlockCipher cipher = key.isXTS()
                ? new XTSAESBlockCipher(FileStreams::iosTweakFunction, key.dataUnitSize())
                : new FileBlockCipher();
        cipher.init(false, new KeyParameter(key.key()));
        return cipher;
    }

    static byte[] iosTweakFunction(long tweakValue) {
        byte[] bs = Pack.longToLittleEndian(tweakValue);
        return Arrays.concatenate(bs, bs);
    }

    static InputStream chunkStream(List<Chunk> chunks) {
        List<InputStream> inputStreams = chunks.stream()
                .map(Chunk::inputStream)
                .collect(Collectors.toList());
        Enumeration<InputStream> enumeration = Collections.enumeration(inputStreams);
        return new SequenceInputStream(enumeration);
    }
}
// TODO consider purity/ removing chunkStream
