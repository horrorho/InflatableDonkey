/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.requests;

import com.github.horrorho.inflatabledonkey.protocol.ProtoBufArray;
import com.google.protobuf.GeneratedMessage;
import java.io.IOException;
import java.util.List;
import net.jcip.annotations.Immutable;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;

/**
 * Post Message Factory.
 *
 * @author Ahseya
 */
@Immutable
public final class PostMessageFactory {

    public static PostMessageFactory defaultInstance() {
        return instance;
    }

    private static final PostMessageFactory instance = new PostMessageFactory();

    public <T extends GeneratedMessage> HttpUriRequest newRequest(
            String url,
            String container,
            String bundle,
            String cloudKitUserId,
            String cloudKitToken,
            String uuid,
            List<T> messages,
            Headers headers
    ) throws IOException {

        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(ProtoBufArray.encode(messages));

        HttpPost post = new HttpPost(url);
        post.setHeader(Headers.xAppleRequestUUID, uuid);
        post.setHeader(Headers.xCloudKitUserId, cloudKitUserId);
        post.setHeader(Headers.xCloudKitAuthToken, cloudKitToken);
        post.setHeader(Headers.xCloudKitContainerId, container);
        post.setHeader(Headers.xCloudKitBundleId, bundle);
        post.setHeader(Headers.accept, "application/x-protobuf");
        post.setHeader(Headers.contentType, "application/x-protobuf; desc=\"https://p33-ckdatabase.icloud.com:443/static/protobuf/CloudDB/CloudDBClient.desc\"; messageType=RequestOperation; delimited=true");
        post.setHeader(headers.get(Headers.userAgent));
        post.setHeader(headers.get(Headers.xCloudKitProtocolVersion));
        post.setHeader(headers.get(Headers.xMmeClientInfo));
        post.setEntity(byteArrayEntity);

        return post;
    }
}
