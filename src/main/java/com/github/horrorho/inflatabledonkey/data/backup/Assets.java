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
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Assets.
 *
 * @author Ahseya
 */
@Immutable
public final class Assets {

    public static boolean isEmpty(String asset) {
        // F:UUID:token:length:x
        String[] split = asset.split(":");
        if (split.length < 4) {
            logger.warn("-- isEmpty() - no file size field: {}", asset);
            return true;
        }

        return !split[3].equals("0");
    }

    private static final Logger logger = LoggerFactory.getLogger(Assets.class);

    private final Optional<String> domain;
    private final List<String> files;

    public Assets(Optional<String> domain, List<String> files) {
        this.domain = Objects.requireNonNull(domain, "domain");
        this.files = new ArrayList<>(files);
    }

    public Optional<String> domain() {
        return domain;
    }

    public List<String> files() {
        return new ArrayList<>(files);
    }

    public List<String> nonEmptyFiles() {
        return files.stream()
                .filter(Assets::isEmpty)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Assets{" + "domain=" + domain + ", files=" + files + '}';
    }
}
