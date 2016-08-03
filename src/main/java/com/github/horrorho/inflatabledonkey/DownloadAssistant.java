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
package com.github.horrorho.inflatabledonkey;

import com.github.horrorho.inflatabledonkey.cloud.Donkey;
import com.github.horrorho.inflatabledonkey.data.backup.Asset;
import com.github.horrorho.inflatabledonkey.file.FileAssembler;
import com.github.horrorho.inflatabledonkey.file.XFileKeyFactory;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import net.jcip.annotations.ThreadSafe;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DownloadAssistant.
 *
 * @author Ahseya
 */
@ThreadSafe
public final class DownloadAssistant {

    private static final Logger logger = LoggerFactory.getLogger(DownloadAssistant.class);

    private final Function<FileAssembler, Donkey> donkeyFactory;
    private final Function<Set<Asset>, List<Set<Asset>>> batchFunction;
    private final KeyBagManager keyBagManager;
    private final ForkJoinPool forkJoinPool;
    private final Path folder;

    public DownloadAssistant(
            Function<FileAssembler, Donkey> donkeyFactory,
            Function<Set<Asset>, List<Set<Asset>>> batchFunction,
            KeyBagManager keyBagManager,
            ForkJoinPool forkJoinPool,
            Path folder) {

        this.donkeyFactory = Objects.requireNonNull(donkeyFactory);
        this.batchFunction = Objects.requireNonNull(batchFunction);
        this.keyBagManager = Objects.requireNonNull(keyBagManager);
        this.forkJoinPool = Objects.requireNonNull(forkJoinPool);
        this.folder = Objects.requireNonNull(folder);
    }

    public void download(HttpClient httpClient, Set<Asset> assets, Path relativePath) {
        logger.trace("<< download() - assets: {}", assets.size());
        Path outputFolder = folder.resolve(relativePath);
        keyBagManager.update(httpClient, assets);
        XFileKeyFactory fileKeys = new XFileKeyFactory(keyBagManager::keyBag);
        FileAssembler fileAssembler = new FileAssembler(fileKeys, outputFolder);

        Donkey donkey = donkeyFactory.apply(fileAssembler);

        List<Set<Asset>> batchedAssets = batchFunction.apply(assets);

        execute(httpClient, donkey, batchedAssets);

        logger.trace(">> download()");
    }

    public void execute(HttpClient httpClient, Donkey donkey, List<Set<Asset>> batchedAssets) {
        logger.debug("-- download() - threads: {} batch count: {}",
                forkJoinPool.getParallelism(), batchedAssets.size());

        try {
            forkJoinPool
                    .submit(() -> batchedAssets
                            .parallelStream()
                            .forEach(u -> donkey.apply(httpClient, u)))
                    .get();

        } catch (InterruptedException ex) {
            logger.warn("-- execute() - InterruptedException: {}", ex.getMessage());
            Thread.currentThread().interrupt();

        } catch (ExecutionException ex) {
            throw new RuntimeException(ex);
        }
    }
}
