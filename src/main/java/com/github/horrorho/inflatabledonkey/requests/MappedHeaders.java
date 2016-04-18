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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.apache.http.Header;

/**
 * Mapped Headers.
 *
 * @author ahseya
 */
@Immutable
public final class MappedHeaders implements HeadersLegacy {

    private final Map<String, Header> headers;

    MappedHeaders(Map<String, Header> headers) {
        this.headers = new HashMap<>();
        this.headers.putAll(headers);
    }

    public MappedHeaders(Collection<Header> headers) {
        this.headers = headers.stream()
                .collect(Collectors.toMap(header -> header.getName().toLowerCase(Locale.US), Function.identity()));
    }

    public MappedHeaders(Header... headers) {
        this(Arrays.asList(headers));
    }

    @Override
    public Header get(String header) throws NullPointerException {
        if (headers.containsKey(header.toLowerCase(Locale.US))) {
            return headers.get(header.toLowerCase(Locale.US));
        }

        throw new NullPointerException("Missing header: " + header);
    }

    @Override
    public List<Header> getAll() {
        return new ArrayList<>(headers.values());
    }
}
