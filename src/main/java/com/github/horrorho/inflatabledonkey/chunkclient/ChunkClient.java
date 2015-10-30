/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.chunkclient;

import com.github.horrorho.inflatabledonkey.protocol.ChunkServer;
import com.github.horrorho.inflatabledonkey.requests.ChunkListRequestFactory;
import com.github.horrorho.inflatabledonkey.requests.Headers;
import com.github.horrorho.inflatabledonkey.responsehandler.ByteArrayResponseHandler;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * ChunkClient.
 *
 * @author Ahseya
 */
@Immutable
public final class ChunkClient {

    private final Function<ChunkServer.StorageHostChunkList, HttpUriRequest> chunkListRequestFactory;
    private final ResponseHandler<byte[]> byteArrayResponseHandler;
    private final BiFunction<List<ChunkServer.ChunkInfo>, byte[], List<byte[]>> chunkDecrypter;

    ChunkClient(
            Function<ChunkServer.StorageHostChunkList, HttpUriRequest> chunkListRequestFactory,
            ResponseHandler<byte[]> byteArrayResponseHandler,
            BiFunction<List<ChunkServer.ChunkInfo>, byte[], List<byte[]>> chunkDecrypter) {

        this.chunkListRequestFactory = Objects.requireNonNull(chunkListRequestFactory);
        this.byteArrayResponseHandler = Objects.requireNonNull(byteArrayResponseHandler);
        this.chunkDecrypter = Objects.requireNonNull(chunkDecrypter);
    }

    public ChunkClient(Headers headers) {
        this(new ChunkListRequestFactory(headers), ByteArrayResponseHandler.instance(), ChunkDecrypter.instance());
    }

    public void fileGroups(
            HttpClient httpClient,
            ChunkServer.FileGroups filegroups,
            BiConsumer<ByteString, List<byte[]>> dataConsumer) {

        filegroups.getFileGroupsList()
                .forEach(fileGroup -> fileGroup(httpClient, fileGroup, dataConsumer));
    }

    public void fileGroup(
            HttpClient httpClient,
            ChunkServer.FileChecksumStorageHostChunkLists fileGroup,
            BiConsumer<ByteString, List<byte[]>> dataConsumer) {

        HashMap<ChunkServer.ChunkReference, byte[]> data = get(httpClient, fileGroup);

        fileGroup.getFileChecksumChunkReferencesList()
                .forEach(fileChecksumchunkReference -> {

                    List<byte[]> fileData = fileChecksumchunkReference.getChunkReferencesList()
                    .stream()
                    .map(data::get)
                    .collect(Collectors.toList());

                    dataConsumer.accept(fileChecksumchunkReference.getFileChecksum(), fileData);
                });
    }

    public HashMap<ChunkServer.ChunkReference, byte[]>
            get(HttpClient httpClient, ChunkServer.FileChecksumStorageHostChunkLists fileGroup) {

        HashMap<ChunkServer.ChunkReference, byte[]> data = new HashMap<>();
        ChunkServer.ChunkReference.Builder builder = ChunkServer.ChunkReference.newBuilder();

        for (int container = 0; container < fileGroup.getStorageHostChunkListCount(); container++) {
            builder.setContainerIndex(container);

            ChunkServer.StorageHostChunkList storageHostChunkList = fileGroup.getStorageHostChunkList(container);
            byte[] chunkData = fetch(httpClient, storageHostChunkList);
            List<byte[]> chunkList = chunkDecrypter.apply(storageHostChunkList.getChunkInfoList(), chunkData);

            for (int index = 0; index < chunkList.size(); index++) {
                builder.setChunkIndex(index);
                data.put(builder.build(), chunkList.get(index));
            }
        }

        return data;
    }

    byte[] fetch(HttpClient httpClient, ChunkServer.StorageHostChunkList storageHostChunkList) {
        try {
            HttpUriRequest request = chunkListRequestFactory.apply(storageHostChunkList);
            return httpClient.execute(request, byteArrayResponseHandler);

        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
