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

import com.google.protobuf.GeneratedMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.jcip.annotations.Immutable;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
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
public final class ProtoBufsRequestFactoryLegacy {

    public static ProtoBufsRequestFactoryLegacy instance() {
        return instance;
    }

    private static final ProtoBufsRequestFactoryLegacy instance = new ProtoBufsRequestFactoryLegacy(CoreHeaders.headers());

    private final Map<Headers, Header> headers;

    public ProtoBufsRequestFactoryLegacy(Map<Headers, Header> headers) {
        this.headers = new HashMap<>(headers);
    }

    public <T extends GeneratedMessage> HttpUriRequest newRequest(
            String url,
            String container,
            String bundle,
            String cloudKitUserId,
            String cloudKitToken,
            String uuid,
            List<T> protobufs
    ) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        protobufs.forEach(message -> {
            try {
                message.writeDelimitedTo(baos);

            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        });

        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(baos.toByteArray());

        HttpPost post = new HttpPost(url);
        post.setHeader(Headers.XAPPLEREQUESTUUID.header(uuid));
        post.setHeader(Headers.XCLOUDKITUSERID.header(cloudKitUserId));
        post.setHeader(Headers.XCLOUDKITAUTHTOKEN.header(cloudKitToken));
        post.setHeader(Headers.XCLOUDKITCONTAINERID.header(container));
        post.setHeader(Headers.XCLOUDKITBUNDLEID.header(bundle));
        post.setHeader(HttpHeaders.ACCEPT, "application/x-protobuf");
        post.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-protobuf; desc=\"https://p33-ckdatabase.icloud.com:443/static/protobuf/CloudDB/CloudDBClient.desc\"; messageType=RequestOperation; delimited=true");
        post.addHeader(headers.get(Headers.USERAGENT));
        post.addHeader(headers.get(Headers.XCLOUDKITPROTOCOLVERSION));
        post.addHeader(headers.get(Headers.XMMECLIENTINFO));
        post.setEntity(byteArrayEntity);

        return post;
    }

}
