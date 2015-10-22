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

import java.nio.charset.StandardCharsets;
import net.jcip.annotations.Immutable;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;

/**
 * Authentication NSDictionary HttpUriRequest factory.
 *
 * @author Ahseya
 */
@Immutable
public final class GetRequestMetaData {

    public static GetRequestMetaData create() {
        return instance;
    }

    private static final GetRequestMetaData instance = new GetRequestMetaData();

    GetRequestMetaData() {
    }

    /**
     * Returns a new authentication NSDictionary HttpUriRequest.
     *
     * @param dsPrsID not null
     * @param mmeAuthToken not null
     * @param mediaStreamUrl not null
     * @return Authenticate property list, not null
     */
    public HttpUriRequest from(String dsPrsID, String mmeAuthToken, String mediaStreamUrl) {

        String url = mediaStreamUrl + "/" + dsPrsID + "/streams/getmetadata";
        String authorization = Headers.mobilemeAuthToken(dsPrsID, mmeAuthToken);

        HttpPost request = new HttpPost(url);
        request.setHeader(HttpHeaders.USER_AGENT, "iCloud.exe (unknown version) CFNetwork/520.20.9");
        request.setHeader("x-apple-mme-streams-version", "AfCPlf71FDRQ");
        request.setHeader("x-apple-mme-streams-client-token", "1a1f03da91f4a769a5b03b9c5b36667f245c3fbec3b2bcbe624f9f41b9f431e2");
        request.setHeader("x-apple-mme-streams-client-udid", "f5a277cb39ca5b4559b5c72880c4060800000000");
        request.setHeader("x-mme-client-info", "<PC><Windows;6.1(1.0);7601><com.apple.mediastreams.windows/7.5 (com.apple.mediastreams.windows/7.5)>");
        request.setHeader("Authorization", authorization);

        String post = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n"
                + "<plist version=\"1.0\">\n"
                + "<dict/>\n"
                + "</plist>";

        request.setEntity(new StringEntity(post, StandardCharsets.UTF_8));

        return request;
    }
}
