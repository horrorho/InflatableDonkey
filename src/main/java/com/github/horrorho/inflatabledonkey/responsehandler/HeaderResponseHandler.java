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
package com.github.horrorho.inflatabledonkey.responsehandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import net.jcip.annotations.NotThreadSafe;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.utils.DateUtils;

/**
 * Mutable ResponseHandler wrapper that captures header information.
 *
 * @author Ahseya
 * @param <T>
 */
@NotThreadSafe
public final class HeaderResponseHandler<T> implements ResponseHandler<T> {

    private final ResponseHandler<T> responseHandler;
    private Map<String, List<Header>> headers;
    private Locale locale;
    private ProtocolVersion protocolVersion;
    private StatusLine statusLine;

    HeaderResponseHandler(ResponseHandler<T> responseHandler, Map<String, List<Header>> headers, Locale locale,
            ProtocolVersion protocolVersion, StatusLine statusLine) {
        this.responseHandler = Objects.requireNonNull(responseHandler);
        this.headers = headers;
        this.locale = locale;
        this.protocolVersion = protocolVersion;
        this.statusLine = statusLine;
    }

    public HeaderResponseHandler(ResponseHandler<T> responseHandler) {
        this(responseHandler, null, null, null, null);
    }

    @Override
    public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        locale = response.getLocale();
        protocolVersion = response.getProtocolVersion();
        statusLine = response.getStatusLine();
        headers = Arrays.asList(response.getAllHeaders())
                .stream()
                .collect(groupingBy(header -> header.getName().toLowerCase(Locale.US)));

        return responseHandler.handleResponse(response);
    }

    public ResponseHandler<T> getResponseHandler() {
        return responseHandler;
    }

    public List<Header> headers() {
        return headers == null
                ? Collections.emptyList()
                : headers.values().stream().flatMap(Collection::stream).collect(toList());
    }

    public List<Header> header(String headerName) {
        return headers.getOrDefault(headerName.toLowerCase(Locale.US), Collections.emptyList());
    }

    public Optional<Locale> locale() {
        return Optional.ofNullable(locale);
    }

    public Optional<ProtocolVersion> protocolVersion() {
        return Optional.ofNullable(protocolVersion);
    }

    public Optional<StatusLine> statusLine() {
        return Optional.ofNullable(statusLine);
    }

    public Optional<Date> date() {
        return Optional.ofNullable(headers.get(HttpHeaders.DATE.toLowerCase(Locale.US)))
                .flatMap(u -> u.stream()
                        .map(Header::getValue)
                        .map(DateUtils::parseDate)
                        .findFirst());
    }

    @Override
    public String toString() {
        return "HeaderResponseHandler{"
                + "responseHandler=" + responseHandler
                + ", headers=" + headers
                + ", locale=" + locale
                + ", protocolVersion=" + protocolVersion
                + ", statusLine=" + statusLine
                + '}';
    }
}
