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

import com.github.horrorho.inflatabledonkey.protocol.ChunkServer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

/**
 * Headers.
 *
 * @author ahseya
 */
@Immutable
public interface Headers {

    static final String accept = "Accept";
    static final String contentType = "Content-Type";
    static final String userAgent = "User-Agent";
    static final String xAppleMmcsDataclass = "x-apple-mmcs-dataclass";
    static final String xAppleMmcsAuth = "x-apple-mmcs-auth";
    static final String xAppleMmcsProtoVersion = "x-apple-mmcs-proto-version";
    static final String xAppleMmeDsid = "x-apple-mme-dsid";
    static final String xAppleRequestUUID = "X-Apple-Request-UUID";
    static final String xCloudKitAuthToken = "X-CloudKit-AuthToken";
    static final String xCloudKitBundleId = "X-CloudKit-BundleId";
    static final String xCloudKitContainerId = "X-CloudKit-ContainerId";
    static final String xCloudKitContainer = "X-CloudKit-Container";
    static final String xCloudKitProtocolVersion = "X-CloudKit-ProtocolVersion";
    static final String xCloudKitUserId = "X-CloudKit-UserId";
    static final String xCloudKitZones = "X-CloudKit-Zone";
    static final String xMmeClientInfo = "X-Mme-Client-Info";

    Header get(String header) throws NullPointerException;

    List<Header> getAll();

    default List<Header> getAll(List<String> headers) {
        return headers.stream()
                .map(this::get)
                .collect(Collectors.toList());
    }

    static Header header(String name, String value) {
        return new BasicHeader(name, value);
    }

    static Header header(ChunkServer.NameValuePair header) {
        return header(header.getName(), header.getValue());
    }

    static String basicToken(String left, String right) {
        return token("Basic", left, right);
    }

    static String mobilemeAuthToken(String left, String right) {
        return token("X-MobileMe-AuthToken", left, right);
    }

    static String token(String type, String left, String right) {
        return type + " " + Base64.getEncoder().encodeToString((left + ":" + right).getBytes(StandardCharsets.UTF_8));
    }
}
