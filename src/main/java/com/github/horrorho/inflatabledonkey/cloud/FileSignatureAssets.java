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

import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.google.protobuf.ByteString;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;

/**
 * FileSignatureAssets.
 *
 * @author Ahseya
 */
@Immutable
public final class FileSignatureAssets {

    public static FileSignatureAssets create(Collection<Asset> assets) {
        // TODO double check filtering.
        Map<ByteString, Asset> map = assets.stream()
                .filter(a -> a.size() > 0)
                .filter(a -> a.fileSignature().isPresent())
                .filter(a -> a.keyEncryptionKey().isPresent())
                .filter(a -> a.domain().isPresent())
                .filter(a -> a.relativePath().isPresent())
                .collect(Collectors.toMap(
                        a -> ByteString.copyFrom(a.fileSignature().get()),
                        Function.identity()));

        return new FileSignatureAssets(map);
    }

    private final Map<ByteString, Asset> assets;

    private FileSignatureAssets(Map<ByteString, Asset> assets) {
        this.assets = assets;
    }

    public Optional<Asset> asset(ByteString fileSignature) {
        return assets.containsKey(fileSignature)
                ? Optional.of(assets.get(fileSignature))
                : Optional.empty();
    }

    public Collection<Asset> assets() {
        return assets.values();
    }

    @Override
    public String toString() {
        return "FileSignatureAssets{" + "assets=" + assets + '}';
    }
}
