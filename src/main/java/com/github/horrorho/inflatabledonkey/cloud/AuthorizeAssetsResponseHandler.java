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

import com.github.horrorho.inflatabledonkey.io.IOFunction;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer;
import com.github.horrorho.inflatabledonkey.responsehandler.DonkeyResponseHandler;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import static java.util.stream.Collectors.toList;
import net.jcip.annotations.ThreadSafe;
import org.apache.http.HttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@ThreadSafe
public class AuthorizeAssetsResponseHandler extends DonkeyResponseHandler<ChunkServer.FileGroups> {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizeAssetsResponseHandler.class);

    private static final long FALLBACK_DURATION_MS = 30 * 60 * 1000;

    private final IOFunction<InputStream, ChunkServer.FileGroups> parser;
    private final long timestampTolerance;

    public AuthorizeAssetsResponseHandler(IOFunction<InputStream, ChunkServer.FileGroups> parser,
            long timestampTolerance) {
        this.parser = Objects.requireNonNull(parser);
        this.timestampTolerance = timestampTolerance;
    }

    public AuthorizeAssetsResponseHandler(long timestampTolerance) {
        this(ChunkServer.FileGroups.PARSER::parseFrom, timestampTolerance);
    }

    @Override
    public ChunkServer.FileGroups
            handleEntityTimestampOffset(HttpEntity entity, long timestampOffset) throws IOException {
        ChunkServer.FileGroups fileGroups = handleEntity(entity);

        if (Math.abs(timestampOffset) > timestampTolerance) {
            logger.debug("-- handleEntityTimestampOffset() - system timestamp out of tolerance: {} ms", timestampOffset);
            adjustExpiryTimestamp(fileGroups, timestampOffset);
        }
        return fileGroups;
    }

    @Override
    public ChunkServer.FileGroups handleEntity(HttpEntity entity) throws IOException {
        try (InputStream inputStream = entity.getContent()) {
            return parser.apply(inputStream);
        }
    }

    ChunkServer.FileGroups adjustExpiryTimestamp(ChunkServer.FileGroups fileGroups, long offset) {
        List<ChunkServer.FileChecksumStorageHostChunkLists> fileGroupsList = fileGroups.getFileGroupsList()
                .stream()
                .map(u -> AuthorizeAssetsResponseHandler.this.adjustExpiryTimestamp(u, offset))
                .collect(toList());
        return fileGroups.toBuilder().clearFileGroups().addAllFileGroups(fileGroupsList).build();
    }

    ChunkServer.FileChecksumStorageHostChunkLists
            adjustExpiryTimestamp(ChunkServer.FileChecksumStorageHostChunkLists fileGroup, long offset) {
        List<ChunkServer.StorageHostChunkList> list = fileGroup.getStorageHostChunkListList()
                .stream()
                .map(u -> AuthorizeAssetsResponseHandler.this.adjustExpiryTimestamp(u, offset))
                .collect(toList());
        return fileGroup.toBuilder().clearStorageHostChunkList().addAllStorageHostChunkList(list).build();
    }

    ChunkServer.StorageHostChunkList adjustExpiryTimestamp(ChunkServer.StorageHostChunkList container, long offset) {
        if (!container.getHostInfo().hasExpiry()) {
            // Shouldn't happen, can probably remove this check.
            logger.warn("-- adjustExpiryTimestamp() - no expiry timestamp: {}", container.getHostInfo());
            return setExpiryTimestamp(container, System.currentTimeMillis() + FALLBACK_DURATION_MS);
        }
        long timestamp = container.getHostInfo().getExpiry() + offset;
        return setExpiryTimestamp(container, timestamp);
    }

    ChunkServer.StorageHostChunkList setExpiryTimestamp(ChunkServer.StorageHostChunkList container, long timestamp) {
        ChunkServer.HostInfo hostInfo = container.getHostInfo().toBuilder().setExpiry(timestamp).build();
        return container.toBuilder().setHostInfo(hostInfo).build();
    }
}
