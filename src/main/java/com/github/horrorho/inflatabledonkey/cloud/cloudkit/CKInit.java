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
package com.github.horrorho.inflatabledonkey.cloud.cloudkit;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.horrorho.inflatabledonkey.data.CKContainer;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;
import jdk.nashorn.internal.ir.annotations.Immutable;

/**
 * CKInit
 *
 * @author Ahseya
 */
@Immutable
public final class CKInit {

    private final String cloudKitDeviceUrl;
    private final String cloudKitDatabaseUrl;
    private final CKContainer[] containers;
    private final String cloudKitShareUrl;
    private final String cloudKitUserId;

    @JsonCreator
    public CKInit(
            @JsonProperty("cloudKitDeviceUrl") String cloudKitDeviceUrl,
            @JsonProperty("cloudKitDatabaseUrl") String cloudKitDatabaseUrl,
            @JsonProperty("values") CKContainer[] containers,
            @JsonProperty("cloudKitShareUrl") String cloudKitShareUrl,
            @JsonProperty("cloudKitUserId") String cloudKitUserId) {

        this.cloudKitDeviceUrl = cloudKitDeviceUrl;
        this.cloudKitDatabaseUrl = cloudKitDatabaseUrl;
        this.containers = Arrays.copyOf(containers, containers.length);
        this.cloudKitShareUrl = cloudKitShareUrl;
        this.cloudKitUserId = cloudKitUserId;
    }

    public String cloudKitDeviceUrl() {
        return cloudKitDeviceUrl;
    }

    public String cloudKitDatabaseUrl() {
        return cloudKitDatabaseUrl;
    }

    public CKContainer container(String env) {
        return Stream.of(containers)
                .filter(container -> container.env().equalsIgnoreCase(env))
                .findFirst()
                .orElse(null);
    }

    public CKContainer production() {
        return container("production");
    }

    public CKContainer sandbox() {
        return container("sandbox");
    }

    public String cloudKitShareUrl() {
        return cloudKitShareUrl;
    }

    public String cloudKitUserId() {
        return cloudKitUserId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.cloudKitDeviceUrl);
        hash = 19 * hash + Objects.hashCode(this.cloudKitDatabaseUrl);
        hash = 19 * hash + Arrays.deepHashCode(this.containers);
        hash = 19 * hash + Objects.hashCode(this.cloudKitShareUrl);
        hash = 19 * hash + Objects.hashCode(this.cloudKitUserId);
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
        final CKInit other = (CKInit) obj;
        if (!Objects.equals(this.cloudKitDeviceUrl, other.cloudKitDeviceUrl)) {
            return false;
        }
        if (!Objects.equals(this.cloudKitDatabaseUrl, other.cloudKitDatabaseUrl)) {
            return false;
        }
        if (!Arrays.deepEquals(this.containers, other.containers)) {
            return false;
        }
        if (!Objects.equals(this.cloudKitShareUrl, other.cloudKitShareUrl)) {
            return false;
        }
        if (!Objects.equals(this.cloudKitUserId, other.cloudKitUserId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CKAppInit{"
                + "cloudKitDeviceUrl=" + cloudKitDeviceUrl
                + ", cloudKitDatabaseUrl=" + cloudKitDatabaseUrl
                + ", values=" + Arrays.asList(containers)
                + ", cloudKitShareUrl=" + cloudKitShareUrl
                + ", cloudKitUserId=" + cloudKitUserId
                + '}';
    }
}
