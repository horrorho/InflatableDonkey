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
package com.github.horrorho.inflatabledonkey.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import javax.annotation.concurrent.Immutable;

/**
 * CKContainer.
 *
 * @author Ahseya
 */
@Immutable
public final class CKContainer {

    private final String name;
    private final String env;
    private final String ckDeviceUrl;
    private final String url;

    @JsonCreator
    public CKContainer(
            @JsonProperty("name") String name,
            @JsonProperty("env") String env,
            @JsonProperty("ckDeviceUrl") String ckDeviceUrl,
            @JsonProperty("url") String url) {

        this.name = name;
        this.env = env;
        this.ckDeviceUrl = ckDeviceUrl;
        this.url = url;
    }

    public String name() {
        return name;
    }

    public String env() {
        return env;
    }

    public String ckDeviceUrl() {
        return ckDeviceUrl;
    }

    public String url() {
        return url;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.name);
        hash = 71 * hash + Objects.hashCode(this.env);
        hash = 71 * hash + Objects.hashCode(this.ckDeviceUrl);
        hash = 71 * hash + Objects.hashCode(this.url);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CKContainer other = (CKContainer) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.env, other.env)) {
            return false;
        }
        if (!Objects.equals(this.ckDeviceUrl, other.ckDeviceUrl)) {
            return false;
        }
        if (!Objects.equals(this.url, other.url)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Values{" + "name=" + name + ", env=" + env + ", ckDeviceUrl=" + ckDeviceUrl + ", url=" + url + '}';
    }
}
