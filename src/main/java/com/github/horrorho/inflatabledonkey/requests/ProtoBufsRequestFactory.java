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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.concurrent.Immutable;
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
public final class ProtoBufsRequestFactory {

    private static final Map<Headers, Header> HEADERS = new HashMap<>(CoreHeaders.headers());
    private static final String ACCEPT = "application/x-protobuf";
    private static final String CONTENT_TYPE
            = "application/x-protobuf; "
            + "desc=\"https://p33-ckdatabase.icloud.com:443/static/protobuf/CloudDB/CloudDBClient.desc\"; "
            + "messageType=RequestOperation; delimited=true";

    private final Map<Headers, Header> headers;
    private final String baseUrl;
    private final String container;
    private final String bundle;
    private final String cloudKitUserId;
    private final String cloudKitToken;

    public ProtoBufsRequestFactory(
            Map<Headers, Header> headers,
            String url, String container, String bundle, String cloudKitUserId, String cloudKitToken) {
        this.headers = new HashMap<>(headers);
        this.baseUrl = Objects.requireNonNull(url, "url");
        this.container = Objects.requireNonNull(container, "container");
        this.bundle = Objects.requireNonNull(bundle, "bundle");
        this.cloudKitUserId = Objects.requireNonNull(cloudKitUserId, "cloudKitUserId");
        this.cloudKitToken = Objects.requireNonNull(cloudKitToken, "cloudKitToken");
    }

    public ProtoBufsRequestFactory(
            String baseUrl, String container, String bundle, String cloudKitUserId, String cloudKitToken) {
        this(HEADERS, baseUrl, container, bundle, cloudKitUserId, cloudKitToken);
    }

    public HttpUriRequest apply(String api, UUID uuid, byte[] delimitedMessages) {
        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(delimitedMessages);
        HttpPost post = new HttpPost(baseUrl + api);
        post.setHeader(Headers.XAPPLEREQUESTUUID.header(uuid.toString()));
        post.setHeader(Headers.XCLOUDKITUSERID.header(cloudKitUserId));
        post.setHeader(Headers.XCLOUDKITAUTHTOKEN.header(cloudKitToken));
        post.setHeader(Headers.XCLOUDKITCONTAINERID.header(container));
        post.setHeader(Headers.XCLOUDKITBUNDLEID.header(bundle));
        post.setHeader(HttpHeaders.ACCEPT, ACCEPT);
        post.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE);
        post.addHeader(headers.get(Headers.USERAGENT));
        post.addHeader(headers.get(Headers.XCLOUDKITPROTOCOLVERSION));
        post.addHeader(headers.get(Headers.XMMECLIENTINFO));
        post.setEntity(byteArrayEntity);
        return post;
    }

    public String url() {
        return baseUrl;
    }

    public String container() {
        return container;
    }

    public String bundle() {
        return bundle;
    }

    public String cloudKitUserId() {
        return cloudKitUserId;
    }

    public String cloudKitToken() {
        return cloudKitToken;
    }
}
