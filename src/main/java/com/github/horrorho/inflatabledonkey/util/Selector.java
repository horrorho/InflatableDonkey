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
package com.github.horrorho.inflatabledonkey.util;

import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.jcip.annotations.NotThreadSafe;

/**
 *
 * @author Ahseya
 * @param <T> item type
 */
@NotThreadSafe
public class Selector<T> implements Supplier<Optional<List<T>>> {

    public static <T> Selector.Builder<T> builder(Map<String, T> options) {
        return new Builder(options);
    }

    static <T> Map<String, T> toLowerCase(Map<String, T> map) {
        return map.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        u -> u.getKey().toLowerCase(Locale.US), Map.Entry::getValue, (u, v) -> u, LinkedHashMap::new));
    }

    private final String prompt;
    private final String quit;
    private final Map<String, T> options;
    private final String delimiter;
    private final InputStream in;
    private final PrintStream out;
    private final boolean isCaseSensitive;

    Selector(
            String prompt,
            String quit,
            Map<String, T> options,
            String delimiter,
            InputStream in,
            PrintStream out,
            boolean isCaseSensitive) {

        this.prompt = Objects.requireNonNull(prompt);
        this.quit = Objects.requireNonNull(quit);
        this.delimiter = Objects.requireNonNull(delimiter);
        this.in = Objects.requireNonNull(in);
        this.out = Objects.requireNonNull(out);
        this.isCaseSensitive = isCaseSensitive;
        this.options = isCaseSensitive
                ? Objects.requireNonNull(options)
                : toLowerCase(options);
    }

    @Override
    public Optional<List<T>> get() {
        Scanner console = new Scanner(in, StandardCharsets.UTF_8.name());
        while (true) {
            out.print(prompt);
            String line = console.nextLine();
            if (line == null || line.toLowerCase(Locale.US).equals(quit)) {
                return Optional.empty();
            }

            if (line.isEmpty()) {
                return Optional.of(Collections.emptyList());
            }

            Optional<List<T>> mapped = map(line);
            if (mapped.isPresent()) {
                return Optional.of(mapped.get());
            }
        }
    }

    Optional<List<T>> map(String line) {
        if (!isCaseSensitive) {
            line = line.toLowerCase(Locale.US);
        }
        List<String> tokens = Arrays.asList(line.split(delimiter));
        Set<T> mapped = validate(tokens)
                ? tokens.stream().map(options::get).collect(Collectors.toCollection(LinkedHashSet::new))
                : null;
        return Optional.ofNullable(new ArrayList<>(mapped));
    }

    boolean validate(Collection<String> tokens) {
        Set<String> set = options.keySet();
        for (String token : tokens) {
            if (!set.contains(token)) {
                out.println("Invalid selection: " + token);
                return false;
            }
        }
        return true;
    }

    public static class Builder<T> {

        private final Map<String, T> options;
        private String quit = "q";
        private String prompt = ": ";
        private String delimiter = "[,\\s]+";
        private InputStream in = System.in;
        private PrintStream out = System.out;
        private boolean isCaseSensitive = false;

        /**
         * Selector Builder.
         *
         * @param options the available options, not null and no null elements
         */
        public Builder(Map<String, T> options) {
            this.options = new LinkedHashMap<>(options);
        }

        public Builder<T> prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public Builder<T> quit(String quit) {
            this.quit = quit;
            return this;
        }

        public Builder<T> delimiter(String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        public Builder<T> input(InputStream in) {
            this.in = in;
            return this;
        }

        public Builder<T> output(PrintStream out) {
            this.out = out;
            return this;
        }

        public Builder<T> isCaseSensitive(boolean isCaseSensitive) {
            this.isCaseSensitive = isCaseSensitive;
            return this;
        }

        public Selector<T> build() {
            return new Selector<>(prompt, quit, options, delimiter, in, out, isCaseSensitive);
        }
    }
}
