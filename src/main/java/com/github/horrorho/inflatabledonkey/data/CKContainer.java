/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import net.jcip.annotations.Immutable;

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
