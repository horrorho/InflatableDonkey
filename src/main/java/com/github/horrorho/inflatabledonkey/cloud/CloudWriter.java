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
package com.github.horrorho.inflatabledonkey.cloud;

import com.github.horrorho.inflatabledonkey.chunk.Chunk;
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.keybag.KeyBag;
import com.github.horrorho.inflatabledonkey.pcs.xfile.FileAssembler;
import com.github.horrorho.inflatabledonkey.pcs.xfile.FileKeyAssistant;
import com.google.protobuf.ByteString;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CloudWriter.
 *
 * @author Ahseya
 */
@Immutable
public final class CloudWriter implements BiConsumer<Asset, List<Chunk>> {

    static Map<ByteString, KeyBag> keyBags(Collection<KeyBag> keyBags) {
        return keyBags.stream()
                .collect(Collectors.toMap(
                        keyBag -> ByteString.copyFrom(keyBag.uuid()),
                        Function.identity()));
    }

    private static final Logger logger = LoggerFactory.getLogger(CloudWriter.class);

    private final Path outputFolder;
    private final Map<ByteString, KeyBag> keyBags;

    CloudWriter(Path outputFolder, Map<ByteString, KeyBag> keyBags) {
        this.outputFolder = Objects.requireNonNull(outputFolder, "outputFolder");
        this.keyBags = Objects.requireNonNull(keyBags, "keyBags");
    }

    public CloudWriter(Path outputFolder, Collection<KeyBag> keyBags) {
        this(outputFolder, keyBags(keyBags));
    }

    public CloudWriter(Path outputFolder, KeyBag... keyBags) {
        this(outputFolder, Arrays.asList(keyBags));
    }

    @Override
    public void accept(Asset asset, List<Chunk> assetChunkList) {
        Path file = outputFolder
                .resolve(asset.domain().get())
                .resolve(asset.relativePath().get());

        logger.debug("-- assembleFile() - asset: {}", asset);
        Optional<byte[]> encryptionKey = encryptionKey(asset);

        logger.debug("-- assembleFile() - encryption key: {}", asset);
        FileAssembler.assemble(file, assetChunkList, asset.size(), encryptionKey);
    }

    Optional<byte[]> encryptionKey(Asset asset) {
        return asset.encryptionKey()
                .flatMap(encryptionKey -> encryptionKey(asset.protectionClass(), encryptionKey));
    }

    Optional<byte[]> encryptionKey(int protectionClass, byte[] fileEncryptionKey) {
        Optional<byte[]> uuid = FileKeyAssistant.uuid(fileEncryptionKey);
        if (!uuid.isPresent()) {
            logger.warn("-- encryptionKey() - no uuid for key: 0x{}", Hex.toHexString(fileEncryptionKey));
            return Optional.empty();
        }

        Optional<KeyBag> keyBag = uuid
                .map(ByteString::copyFrom)
                .map(keyBags::get);
        if (!keyBag.isPresent()) {
            logger.warn("-- encryptionKey() - no keybag for uuid: 0x{}", uuid.map(Hex::toHexString));
            return Optional.empty();
        }

        return keyBag.flatMap(kb -> FileKeyAssistant.unwrap(kb, protectionClass, fileEncryptionKey));
    }

    @Override
    public String toString() {
        return "CloudWriter{" + "outputFolder=" + outputFolder + ", keyBags=" + keyBags + '}';
    }
}
