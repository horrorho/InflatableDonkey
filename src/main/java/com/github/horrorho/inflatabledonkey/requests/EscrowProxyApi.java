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

import com.dd.plist.NSDictionary;
import com.github.horrorho.inflatabledonkey.responsehandler.PropertyListResponseHandler;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import net.jcip.annotations.Immutable;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.bouncycastle.util.BigIntegers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EscrowProxyApi.
 *
 * @author Ahseya
 */
@Immutable
public final class EscrowProxyApi {

    private static final Logger logger = LoggerFactory.getLogger(EscrowProxyApi.class);

    private final String dsPrsID;
    private final String escrowProxyUrl;
    private final Headers headers;

    public EscrowProxyApi(String dsPrsID, String escrowProxyUrl, Headers headers) {
        this.dsPrsID = Objects.requireNonNull(dsPrsID, "dsPrsID");
        this.escrowProxyUrl = Objects.requireNonNull(escrowProxyUrl, "escrowProxyUrl");
        this.headers = Objects.requireNonNull(headers, "headers");
    }

    public NSDictionary getRecords(HttpClient httpClient, String mmeAuthToken) throws IOException {
        String uri = escrowProxyUrl + "/escrowproxy/api/get_records";
        String mobilemeAuthToken = Headers.mobilemeAuthToken(dsPrsID, mmeAuthToken);

        String post = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n"
                + "<plist version=\"1.0\">\n"
                + "<dict>\n"
                + "	<key>command</key>\n"
                + "	<string>GETRECORDS</string>\n"
                + "	<key>label</key>\n"
                + "	<string>com.apple.protectedcloudstorage.record</string>\n"
                + "	<key>version</key>\n"
                + "	<integer>1</integer>\n"
                + "</dict>\n"
                + "</plist>";
        logger.trace("-- getRecords() - post: {}", post);

        HttpUriRequest request = RequestBuilder.post(uri)
                .addHeader(headers.get(Headers.userAgent))
                .addHeader(HttpHeaders.AUTHORIZATION, mobilemeAuthToken)
                .addHeader(headers.get(Headers.xMmeClientInfo))
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-apple-plist")
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .setEntity(new StringEntity(post, StandardCharsets.UTF_8))
                .build();

        NSDictionary dictionary = httpClient.execute(request, PropertyListResponseHandler.nsDictionaryResponseHandler());
        return dictionary;
    }

    public NSDictionary srpInit(HttpClient httpClient, String mmeAuthToken, byte[] key) throws IOException {
        String uri = escrowProxyUrl + "/escrowproxy/api/srp_init";
        String mobilemeAuthToken = Headers.mobilemeAuthToken(dsPrsID, mmeAuthToken);

        String encodedKey = Base64.getEncoder().encodeToString(key);

        String post = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n"
                + "<plist version=\"1.0\">\n"
                + "<dict>\n"
                + "	<key>blob</key>\n"
                + "	<string>" + encodedKey + "</string>\n"
                + "	<key>command</key>\n"
                + "	<string>SRP_INIT</string>\n"
                + "	<key>label</key>\n"
                + "	<string>com.apple.protectedcloudstorage.record</string>\n"
                + "	<key>version</key>\n"
                + "	<integer>1</integer>\n"
                + "</dict>\n"
                + "</plist>";
        logger.trace("-- srp_init() - post: {}", post);

        HttpUriRequest request = RequestBuilder.post(uri)
                .addHeader(headers.get(Headers.userAgent))
                .addHeader(HttpHeaders.AUTHORIZATION, mobilemeAuthToken)
                .addHeader(headers.get(Headers.xMmeClientInfo))
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-apple-plist")
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .setEntity(new StringEntity(post, StandardCharsets.UTF_8))
                .build();

        NSDictionary dictionary = httpClient.execute(request, PropertyListResponseHandler.nsDictionaryResponseHandler());
        return dictionary;
    }

    public NSDictionary recover(HttpClient httpClient, String mmeAuthToken, BigInteger M1, byte[] tag, byte[] x) throws IOException {
        String uri = escrowProxyUrl + "/escrowproxy/api/recover";
        String mobilemeAuthToken = Headers.mobilemeAuthToken(dsPrsID, mmeAuthToken);

        byte[] data = recoveryBlob(x, tag, BigIntegers.asUnsignedByteArray(M1));

        String encodedMessage = Base64.getEncoder().encodeToString(data);

        String post = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n"
                + "<plist version=\"1.0\">\n"
                + "<dict>\n"
                + "	<key>blob</key>\n"
                + "	<string>" + encodedMessage + "</string>\n"
                + "	<key>command</key>\n"
                + "	<string>RECOVER</string>\n"
                + "	<key>label</key>\n"
                + "	<string>com.apple.protectedcloudstorage.record</string>\n"
                + "	<key>version</key>\n"
                + "	<integer>1</integer>\n"
                + "</dict>\n"
                + "</plist>";
        logger.trace("-- recover() - post: {}", post);

        HttpUriRequest request = RequestBuilder.post(uri)
                .addHeader(headers.get(Headers.userAgent))
                .addHeader(HttpHeaders.AUTHORIZATION, mobilemeAuthToken)
                .addHeader(headers.get(Headers.xMmeClientInfo))
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-apple-plist")
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .setEntity(new StringEntity(post, StandardCharsets.UTF_8))
                .build();

        // TODO 409 conflict on bad value
        return httpClient.execute(request, PropertyListResponseHandler.nsDictionaryResponseHandler());
    }

    public byte[] recoveryBlob(byte[] x, byte[] tag, byte[] M1) {
        // Network byte ordering/ big endian.
        if (x.length != 0x10) {
            logger.warn("-- recoveryBlob() - unexpected x length: {}", x.length);
            // TODO 
        }

        // TODO merge into data.blob
        byte[] paddedTag = pad(tag);
        byte[] paddedM1 = pad(M1);

        int length = paddedTag.length + paddedM1.length + 0x30;

        ByteBuffer buffer = ByteBuffer.allocate(length);

        buffer.putInt(length);
        buffer.putInt(0x000000A5);
        buffer.putInt(0x00000000);
        buffer.put(x);
        buffer.putInt(0x00000000);
        buffer.putInt(paddedTag.length + 0x04);
        buffer.putInt(paddedTag.length + paddedM1.length + 0x08);
        buffer.putInt(tag.length);
        buffer.put(paddedTag);
        buffer.putInt(M1.length);
        buffer.put(paddedM1);

        return buffer.array();
    }

    private static byte[] pad(byte[] bytes) {
        int length = bytes.length + 3 & 0xFFFFFFFC;
        return Arrays.copyOf(bytes, length);
    }
}
