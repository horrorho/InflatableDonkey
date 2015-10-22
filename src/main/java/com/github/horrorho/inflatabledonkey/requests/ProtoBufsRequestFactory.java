/* 
 * The MIT License
 *
 * Copyright 2015 Ahseya.
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
 * Protobuf array HttpUriRequest factory.
 * <p>
 * Encodes the supplied protobuf messages with {@link ProtoBufArray}
 *
 * @author Ahseya
 */
@Immutable
public final class ProtoBufsRequestFactory {

    public static ProtoBufsRequestFactory defaultInstance() {
        return instance;
    }

    private static final ProtoBufsRequestFactory instance = new ProtoBufsRequestFactory();

    public <T extends GeneratedMessage> HttpUriRequest newRequest(
            String url,
            String container,
            String bundle,
            String cloudKitUserId,
            String cloudKitToken,
            String uuid,
            List<T> protobufs,
            Headers headers
    ) throws IOException {

        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(ProtoBufArray.encode(protobufs));

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
