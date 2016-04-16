/*
 * The MIT License
 *
 * Copyright 2016 Ahseya.
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

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

/**
 * Headers.
 *
 * @author Ahseya
 */
@Immutable
public enum Headers {
    ACCEPT("Accept"),
    CONTENTTYPE("Content-Type"),
    USERAGENT("User-Agent"),
    XAPPLEMMCSDATACLASS("x-apple-mmcs-dataclass"),
    XAPPLEMMCSAUTH("x-apple-mmcs-auth"),
    XAPPLEMMCSPROTOVERSION("x-apple-mmcs-proto-version"),
    XAPPLEMMEDSID("x-apple-mme-dsid"),
    XAPPLEREQUESTUUID("X-Apple-Request-UUID"),
    XCLOUDKITAUTHTOKEN("X-CloudKit-AuthToken"),
    XCLOUDKITBUNDLEID("X-CloudKit-BundleId"),
    XCLOUDKITCONTAINERID("X-CloudKit-ContainerId"),
    XCLOUDKITCONTAINER("X-CloudKit-Container"),
    XCLOUDKITPROTOCOLVERSION("X-CloudKit-ProtocolVersion"),
    XCLOUDKITUSERID("X-CloudKit-UserId"),
    XCLOUDKITZONES("X-CloudKit-Zone"),
    XMMECLIENTINFO("X-Mme-Client-Info");

    private final String name;

    private Headers(String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    public Header header(String value) {
        return new BasicHeader(this.name, value);
    }

    public Map.Entry<Headers, String> mapEntry(String value) {
        return new AbstractMap.SimpleImmutableEntry<>(this, value);
    }

    @Override
    public String toString() {
        return name;
    }

    public static Map<Headers, Header> headers(Map.Entry<Headers, String>... headers) {
        return Arrays.asList(headers)
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getKey().header(e.getValue())));
    }
}
