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
import com.github.horrorho.inflatabledonkey.keybag.KeyBags;
import com.github.horrorho.inflatabledonkey.pcs.xfile.FileAssembler;
import com.github.horrorho.inflatabledonkey.pcs.xfile.FileKeyAssistant;
import com.github.horrorho.inflatabledonkey.util.FileNameCleaners;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AssetWriter.
 *
 * @author Ahseya
 */
@Immutable
public final class AssetWriter implements BiConsumer<Asset, List<Chunk>> {

    private static final Logger logger = LoggerFactory.getLogger(AssetWriter.class);

    private static final UnaryOperator<String> CLEAN = FileNameCleaners.instance();

    private final Path outputFolder;
    private final KeyBags keyBags;

    public AssetWriter(Path outputFolder, KeyBags keyBags) {
        this.outputFolder = Objects.requireNonNull(outputFolder, "outputFolder");
        this.keyBags = Objects.requireNonNull(keyBags, "keyBags");
    }

    public AssetWriter(Path outputFolder, KeyBag... keyBag) {
        this(outputFolder, new KeyBags(keyBag));
    }

    @Override
    public void accept(Asset asset, List<Chunk> assetChunkList) {
        if (!asset.domain().isPresent()) {
            logger.warn("-- accept() - asset has no domain: {}", asset);
            return;
        }

        if (!asset.relativePath().isPresent()) {
            logger.warn("-- accept() - asset has no relativePath: {}", asset);
            return;
        }

        String domain = CLEAN.apply(asset.domain().get());
        String relativePath = CLEAN.apply(asset.relativePath().get());

        Path file = outputFolder
                .resolve(domain)
                .resolve(relativePath);

        logger.debug("-- accept() - asset: {}", asset);
        Optional<byte[]> encryptionKey = encryptionKey(asset);

        logger.debug("-- accept() - encryption key: {}", asset);
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
                .flatMap(keyBags::keybag);
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
// TOFIX don't write files in no keybag present
