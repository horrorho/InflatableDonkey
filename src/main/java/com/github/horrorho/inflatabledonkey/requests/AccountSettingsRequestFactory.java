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
import java.util.function.BiFunction;
import net.jcip.annotations.Immutable;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * Authentication NSDictionary HttpUriRequest factory.
 *
 * @author Ahseya
 */
@Immutable
public final class AccountSettingsRequestFactory implements BiFunction<String, String, HttpUriRequest> {

    public static final AccountSettingsRequestFactory instance() {
        return INSTANCE;
    }

    private static final AccountSettingsRequestFactory INSTANCE = new AccountSettingsRequestFactory(
            "https://setup.icloud.com/setup/get_account_settings", CoreHeaders.headers());

    private final String url;
    private final Map<Headers, Header> headers;

    AccountSettingsRequestFactory(String url, Map<Headers, Header> headers) {
        this.url = Objects.requireNonNull(url);
        this.headers = new HashMap<>(headers);
    }

    @Override
    public HttpUriRequest apply(String dsPrsID, String mmeAuthToken) {
        String authorization = AccessTokens.BASIC.token(dsPrsID, mmeAuthToken);

        HttpPost request = new HttpPost(url);
        request.setHeader(headers.get(Headers.USERAGENT));
        request.setHeader(headers.get(Headers.XMMECLIENTINFO));
        request.setHeader(HttpHeaders.AUTHORIZATION, authorization);

        return request;
    }
}
