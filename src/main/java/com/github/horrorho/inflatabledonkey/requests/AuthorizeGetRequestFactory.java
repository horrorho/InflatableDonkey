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

import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.jcip.annotations.Immutable;
import org.apache.commons.codec.binary.Hex;
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

    private final Headers headers;

    public AuthorizeGetRequestFactory(Headers headers) {
        this.headers = Objects.requireNonNull(headers);
    }

    public HttpUriRequest newRequest(
            String dsPrsID,
            String contentUrl,
            String container,
            String zone,
            CloudKit.FileTokens fileTokens) {

        if (fileTokens.getFileTokensCount() == 0) {
            return null;
        }

        CloudKit.FileToken base = fileTokens.getFileTokens(0);

        String mmcsAuthToken = Stream.of(
                Hex.encodeHexString(base.getFileChecksum().toByteArray()),
                Hex.encodeHexString(base.getFileSignature().toByteArray()),
                base.getToken())
                .collect(Collectors.joining(" "));

        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(fileTokens.toByteArray());

        String uri = contentUrl + "/" + dsPrsID + "/authorizeGet";

        HttpUriRequest request = RequestBuilder.post(uri)
                .addHeader(Headers.accept, "application/vnd.com.apple.me.ubchunk+protobuf")
                .addHeader(Headers.contentType, "application/vnd.com.apple.me.ubchunk+protobuf")
                .addHeader(Headers.xAppleMmcsDataclass, "com.apple.Dataclass.CloudKit")
                .addHeader(Headers.xCloudKitContainer, container)
                .addHeader(Headers.xCloudKitZones, zone)
                .addHeader(Headers.xAppleMmcsAuth, mmcsAuthToken)
                .addHeader(Headers.xAppleMmeDsid, dsPrsID)
                .addHeader(headers.get(Headers.userAgent))
                .addHeader(headers.get(Headers.xAppleMmcsProtoVersion))
                .addHeader(headers.get(Headers.xMmeClientInfo))
                .setEntity(byteArrayEntity)
                .build();

        return request;
    }
}
