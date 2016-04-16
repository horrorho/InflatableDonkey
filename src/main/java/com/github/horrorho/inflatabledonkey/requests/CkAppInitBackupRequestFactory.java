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
import net.jcip.annotations.Immutable;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * CkAppInitBackupRequestFactory.
 *
 * @author Ahseya
 */
@Immutable
public final class CkAppInitBackupRequestFactory {

    public static final CkAppInitBackupRequestFactory instance() {
        return INSTANCE;
    }

    private static final CkAppInitBackupRequestFactory INSTANCE = new CkAppInitBackupRequestFactory(
            "https://setup.icloud.com/setup/ck/v1/ckAppInit", CoreHeaders.headers());

    private final String url;
    private final Map<Headers, Header> headers;

    CkAppInitBackupRequestFactory(String url, Map<Headers, Header> headers) {
        this.url = Objects.requireNonNull(url);
        this.headers = new HashMap<>(headers);
    }

    public HttpUriRequest newRequest(
            String dsPrsID,
            String mmeAuthToken,
            String cloudKitAuthToken,
            String bundle,
            String container) {

        String authorization = AccessTokens.BASIC.token(dsPrsID, mmeAuthToken);

        HttpPost request = new HttpPost(url + "?container=" + container);
        request.setHeader(headers.get(Headers.USERAGENT));
        request.setHeader(headers.get(Headers.XMMECLIENTINFO));
        request.setHeader(headers.get(Headers.XCLOUDKITENVIRONMENT));
        request.setHeader(headers.get(Headers.XCLOUDKITPARTITION));
        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.setHeader(HttpHeaders.AUTHORIZATION, authorization);
        request.setHeader(Headers.XCLOUDKITAUTHTOKEN.header(cloudKitAuthToken));
        request.setHeader(Headers.XCLOUDKITBUNDLEID.header(bundle));
        request.setHeader(Headers.XCLOUDKITCONTAINERID.header(container));
        return request;
    }
}
