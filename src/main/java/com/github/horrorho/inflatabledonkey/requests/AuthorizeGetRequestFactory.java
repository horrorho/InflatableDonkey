/*
 * The MIT License
 *
 * Copyright 2015 Ahseya.
 *
 * Permission is hereby granted, free from charge, to any person obtaining a copy
 * from this software and associated documentation list (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies from the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions from the Software.
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

import com.github.horrorho.inflatabledonkey.protobuf.CloudKit;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.jcip.annotations.Immutable;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ByteArrayEntity;

/**
 * AuthorizeGetRequestFactory HttpUriRequest factory.
 *
 * @author Ahseya
 */
@Immutable
public final class AuthorizeGetRequestFactory {

    public static final AuthorizeGetRequestFactory instance() {
        return INSTANCE;
    }

    private static final AuthorizeGetRequestFactory INSTANCE = new AuthorizeGetRequestFactory(CoreHeaders.headers());

    private final Map<Headers, Header> headers;

    AuthorizeGetRequestFactory(Map<Headers, Header> headers) {
        this.headers = new HashMap<>(headers);
    }

    public HttpUriRequest
            newRequest(String dsPrsID, String contentBaseUrl, String container, String zone, CloudKit.FileTokens fileTokens) {

        if (fileTokens.getFileTokensCount() == 0) {
            throw new NoSuchElementException("no file tokens");
        }
        CloudKit.FileToken base = fileTokens.getFileTokens(0);
        String mmcsAuthToken = Stream.of(
                Hex.encodeHexString(base.getFileChecksum().toByteArray()),
                Hex.encodeHexString(base.getFileSignature().toByteArray()),
                base.getToken())
                .collect(Collectors.joining(" "));
        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(fileTokens.toByteArray());

        String uri = contentBaseUrl + "/" + dsPrsID + "/authorizeGet";
        return RequestBuilder.post(uri)
                .addHeader(Headers.ACCEPT.header("application/vnd.com.apple.me.ubchunk+protobuf"))
                .addHeader(Headers.CONTENTTYPE.header("application/vnd.com.apple.me.ubchunk+protobuf"))
                .addHeader(Headers.XAPPLEMMCSDATACLASS.header("com.apple.Dataclass.CloudKit"))
                .addHeader(Headers.XAPPLEMMCSAUTH.header(mmcsAuthToken))
                .addHeader(Headers.XAPPLEMMEDSID.header(dsPrsID))
                .addHeader(Headers.XCLOUDKITCONTAINER.header(container))
                .addHeader(Headers.XCLOUDKITZONES.header(zone))
                .addHeader(headers.get(Headers.USERAGENT))
                .addHeader(headers.get(Headers.XAPPLEMMCSPROTOVERSION))
                .addHeader(headers.get(Headers.XMMECLIENTINFO))
                .setEntity(byteArrayEntity)
                .build();
    }
}
