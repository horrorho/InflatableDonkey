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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toMap;
import javax.annotation.concurrent.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class Assets {

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

    public Map<AssetID, String> nonEmptyMap() {
        return assets.stream()
                .filter(a -> a.size() > 0)
                .distinct()
                .collect(toMap(Function.identity(), u -> domain));
    }

    public List<AssetID> nonEmpty() {
        return assets.stream()
                .filter(a -> a.size() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Assets{" + "manifestID=" + manifestID + ", domain=" + domain + ", assets=" + assets + '}';
    }
}
