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
package com.github.horrorho.inflatabledonkey.data.backup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
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

//    public static List<String> files(List<Assets> assetsList, Predicate<Assets> filter) {
//        return assetsList
//                .stream()
//                .filter(assets -> filter.test(assets))
//                .map(Assets::assets)
//                .flatMap(Collection::stream)
//                .collect(Collectors.toList());
//    }
//
//    // TODO rationalize
//    public static boolean isNonEmpty(String asset) {
//        // F:UUID:token:length:x
//        String[] split = asset.split(":");
//        if (split.length < 4) {
//            logger.warn("-- isEmpty() - no file size field: {}", asset);
//            return true;
//        }
//        String x = split[3];
//        if (x.equals("D")) {
//            return false;
//        }
//
//        try {
//            int size = Integer.parseInt(x); 
//            return size != 0;
//        } catch (NumberFormatException ex) {
//            logger.warn("-- isEmpty() - failed to parse asset: {}", asset);
//            return false;
//        }
//    }
//
//    // TODO rationalize
//    public static int size(String asset) {
//        // F:UUID:token:length:x
//        String[] split = asset.split(":");
//        if (split.length < 4) {
//            logger.warn("-- isEmpty() - no file size field: {}", asset);
//            return 0;
//        }
//
//        try {
//            return Integer.parseInt(split[3]);
//        } catch (NumberFormatException ex) {
//            logger.warn("-- size() - failed to parse: {}", asset);
//            return 0;
//        }
//    }

    private static final Logger logger = LoggerFactory.getLogger(Assets.class);

    private final ManifestID manifestID;
    private final String domain;
    private final List<AssetID> assets;

    public Assets(ManifestID manifestID, String domain, List<AssetID> assets) {
        this.manifestID = Objects.requireNonNull(manifestID, "manifestID");
        this.domain = Objects.requireNonNull(domain, "domain");
        this.assets = new ArrayList<>(assets);
    }

    public String domain() {
        return domain;
    }

    public ManifestID manifestID() {
        return manifestID;
    }

    public List<AssetID> assets() {
        return new ArrayList<>(assets);
    }

    public int count() {
        return assets.size();
    }

    public List<AssetID> nonEmpty() {
        return assets.stream()
                .filter(u -> u.size() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Assets{" + "manifestID=" + manifestID + ", domain=" + domain + ", assets=" + assets + '}';
    }
}
