/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.data.backup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Assets.
 *
 * @author Ahseya
 */
public final class Assets {

    private final Optional<String> domain;
    private final List<String> files;

    public Assets(Optional<String> domain, List<String> files) {
        this.domain = Objects.requireNonNull(domain, "domain");
        this.files = new ArrayList<>(files);
    }

    public Optional<String> getDomain() {
        return domain;
    }

    public List<String> getFiles() {
        return new ArrayList<>(files);
    }

    @Override
    public String toString() {
        return "Assets{" + "domain=" + domain + ", files=" + files + '}';
    }
}
