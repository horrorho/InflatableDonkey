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
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.FileChecksumStorageHostChunkLists;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.FileGroups;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.HostInfo;
import com.github.horrorho.inflatabledonkey.protobuf.ChunkServer.StorageHostChunkList;
import com.github.horrorho.inflatabledonkey.responsehandler.DonkeyResponseHandler;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
public class AuthorizeAssetsResponseHandler extends DonkeyResponseHandler<FileGroups> {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizeAssetsResponseHandler.class);

    private final IOFunction<InputStream, FileGroups> parser;
    private final long fallbackDurationMS;

    public AuthorizeAssetsResponseHandler(IOFunction<InputStream, FileGroups> parser, long fallbackDurationMS) {
        this.parser = Objects.requireNonNull(parser);
        this.fallbackDurationMS = fallbackDurationMS;
    }

    public AuthorizeAssetsResponseHandler(long fallbackDurationMS) {
        this(FileGroups.PARSER::parseFrom, fallbackDurationMS);
    }

    @Override
    public FileGroups
            handleEntityTimestampOffset(HttpEntity entity, Optional<Long> timestampOffset) throws IOException {
        logger.trace("-- handleEntityTimestampOffset() - timestamp offset: {}", timestampOffset);
        FileGroups fileGroups = handleEntity(entity);
        return adjustExpiryTimestamp(fileGroups, timestampOffset);
    }

    @Override
    public FileGroups handleEntity(HttpEntity entity) throws IOException {
        try (InputStream inputStream = entity.getContent()) {
            return parser.apply(inputStream);
        }
    }

    FileGroups adjustExpiryTimestamp(FileGroups fileGroups, Optional<Long> timestampOffset) {
        // We adjust the FileGroups timestamps based on machine time/ server time deltas. This allows us to function
        // with inaccurate machine clocks. 
        List<FileChecksumStorageHostChunkLists> fileGroupsList = fileGroups.getFileGroupsList()
                .stream()
                .map(u -> adjustExpiryTimestamp(u, timestampOffset))
                .collect(toList());
        return fileGroups
                .toBuilder()
                .clearFileGroups()
                .addAllFileGroups(fileGroupsList)
                .build();
    }

    FileChecksumStorageHostChunkLists
            adjustExpiryTimestamp(FileChecksumStorageHostChunkLists fileGroup, Optional<Long> timestampOffset) {
        List<StorageHostChunkList> list = fileGroup.getStorageHostChunkListList()
                .stream()
                .map(u -> adjustExpiryTimestamp(u, timestampOffset))
                .collect(toList());
        return fileGroup
                .toBuilder()
                .clearStorageHostChunkList()
                .addAllStorageHostChunkList(list)
                .build();
    }

    StorageHostChunkList adjustExpiryTimestamp(StorageHostChunkList container, Optional<Long> timestampOffset) {
        if (!container.getHostInfo().hasExpiry()) {
            // Shouldn't happen.
            logger.warn("-- adjustExpiryTimestamp() - no expiry timestamp: {} reverting to default",
                    container.getHostInfo().getUri());
            return defaultExpiryTimestamp(container);
        }
        if (!timestampOffset.isPresent()) {
            // Probably shouldn't happen.
            logger.debug("-- adjustExpiryTimestamp() - no timestamp offset: {} reverting to default",
                    container.getHostInfo().getUri());
            return defaultExpiryTimestamp(container);
        }
        long timestamp = container.getHostInfo().getExpiry() + timestampOffset.get();
        if (timestamp < System.currentTimeMillis()) {
            // Shouldn't happen.
            logger.warn("-- adjustExpiryTimestamp() - negative timestamp offset: {} reverting to default",
                    container.getHostInfo().getUri());
            return defaultExpiryTimestamp(container);
        }
        return setExpiryTimestamp(container, timestamp);
    }

    StorageHostChunkList defaultExpiryTimestamp(StorageHostChunkList container) {
        return setExpiryTimestamp(container, System.currentTimeMillis() + fallbackDurationMS);
    }

    StorageHostChunkList setExpiryTimestamp(StorageHostChunkList container, long timestamp) {
        HostInfo hostInfo = container
                .getHostInfo()
                .toBuilder()
                .setExpiry(timestamp)
                .build();
        return container
                .toBuilder()
                .setHostInfo(hostInfo)
                .build();
    }
}
