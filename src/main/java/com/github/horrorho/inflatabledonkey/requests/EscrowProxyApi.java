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
import com.github.horrorho.inflatabledonkey.data.blob.BlobA5;
import com.github.horrorho.inflatabledonkey.responsehandler.PropertyListResponseHandler;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.jcip.annotations.Immutable;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
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
    private final String mmeAuthToken;
    private final Map<Headers, Header> headers;

    EscrowProxyApi(String dsPrsID, String mmeAuthToken, String escrowProxyUrl, Map<Headers, Header> headers) {
        this.dsPrsID = Objects.requireNonNull(dsPrsID, "dsPrsID");
        this.mmeAuthToken = Objects.requireNonNull(mmeAuthToken, "mmeAuthToken");
        this.escrowProxyUrl = Objects.requireNonNull(escrowProxyUrl, "escrowProxyUrl");
        this.headers = new HashMap<>(headers);
    }

    public EscrowProxyApi(String dsPrsID, String mmeAuthToken, String escrowProxyUrl) {
        this(dsPrsID, mmeAuthToken, escrowProxyUrl, CoreHeaders.headers());
    }

    public NSDictionary getRecords(HttpClient httpClient) throws IOException {
        String uri = escrowProxyUrl + "/escrowproxy/api/get_records";
        String authorization = AccessTokens.MOBILEMEAUTHTOKEN.token(dsPrsID, mmeAuthToken);

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
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-apple-plist")
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .addHeader(HttpHeaders.AUTHORIZATION, authorization)
                .addHeader(headers.get(Headers.USERAGENT))
                .addHeader(headers.get(Headers.XMMECLIENTINFO))
                .setEntity(new StringEntity(post, StandardCharsets.UTF_8))
                .build();

        NSDictionary dictionary = httpClient.execute(request, PropertyListResponseHandler.nsDictionaryResponseHandler());
        return dictionary;
    }

    public NSDictionary srpInit(HttpClient httpClient, byte[] key) throws IOException {
        String uri = escrowProxyUrl + "/escrowproxy/api/srp_init";
        String authorization = AccessTokens.MOBILEMEAUTHTOKEN.token(dsPrsID, mmeAuthToken);
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
        logger.trace("-- srpInit() - post: {}", post);

        HttpUriRequest request = RequestBuilder.post(uri)
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-apple-plist")
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .addHeader(HttpHeaders.AUTHORIZATION, authorization)
                .addHeader(headers.get(Headers.USERAGENT))
                .addHeader(headers.get(Headers.XMMECLIENTINFO))
                .setEntity(new StringEntity(post, StandardCharsets.UTF_8))
                .build();

        NSDictionary dictionary = httpClient.execute(request, PropertyListResponseHandler.nsDictionaryResponseHandler());
        return dictionary;
    }

    public NSDictionary
            recover(HttpClient httpClient, byte[] m1, byte[] uuid, byte[] tag) throws IOException {

        String uri = escrowProxyUrl + "/escrowproxy/api/recover";
        String authorization = AccessTokens.MOBILEMEAUTHTOKEN.token(dsPrsID, mmeAuthToken);

        BlobA5 blob = new BlobA5(tag, uuid, m1);
        byte[] data = blob.export().array();
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
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-apple-plist")
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .addHeader(HttpHeaders.AUTHORIZATION, authorization)
                .addHeader(headers.get(Headers.USERAGENT))
                .addHeader(headers.get(Headers.XMMECLIENTINFO))
                .setEntity(new StringEntity(post, StandardCharsets.UTF_8))
                .build();

        // TODO 409 conflict on bad value
        return httpClient.execute(request, PropertyListResponseHandler.nsDictionaryResponseHandler());
    }
}
