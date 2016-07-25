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

import com.github.horrorho.inflatabledonkey.chunk.engine.SHCLContainer;
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer;
import com.google.protobuf.ByteString;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.jcip.annotations.Immutable;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class Voodoo {

    private final ByteString fileSignature;
    private final ByteString fileChecksum;
    private final Asset asset;
    private final List<SHCLContainer> containers;
    private final List<ChunkServer.ChunkReference> chunkReferences;

    public Voodoo(
            ByteString fileSignature,
            ByteString fileChecksum,
            Asset asset,
            List<SHCLContainer> containers,
            List<ChunkServer.ChunkReference> chunkReferences) {

        this.fileSignature = Objects.requireNonNull(fileSignature);
        this.fileChecksum = Objects.requireNonNull(fileChecksum);
        this.asset = Objects.requireNonNull(asset);
        this.containers = new ArrayList<>(containers);
        this.chunkReferences = new ArrayList<>(chunkReferences);
    }

    public Asset asset() {
        return asset;
    }

    public ByteString fileSignature() {
        return fileSignature;
    }

    public ByteString fileChecksum() {
        return fileChecksum;
    }

    public List<SHCLContainer> containers() {
        return new ArrayList<>(containers);
    }

    public List<ChunkServer.ChunkReference> chunkReferences() {
        return new ArrayList<>(chunkReferences);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.fileSignature);
        hash = 37 * hash + Objects.hashCode(this.fileChecksum);
        hash = 37 * hash + Objects.hashCode(this.containers);
        hash = 37 * hash + Objects.hashCode(this.chunkReferences);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Voodoo other = (Voodoo) obj;
        if (!Objects.equals(this.fileSignature, other.fileSignature)) {
            return false;
        }
        if (!Objects.equals(this.fileChecksum, other.fileChecksum)) {
            return false;
        }
        if (!Objects.equals(this.asset, other.asset)) {
            return false;
        }
        if (!Objects.equals(this.containers, other.containers)) {
            return false;
        }
        if (!Objects.equals(this.chunkReferences, other.chunkReferences)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Voodoo{"
                + "fileSignature=" + fileSignature
                + ", fileChecksum=" + fileChecksum
                + ", asset=" + asset
                + ", containers=" + containers
                + ", chunkReferences=" + chunkReferences
                + '}';
    }
}
