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
package com.github.horrorho.inflatabledonkey.args;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.jcip.annotations.Immutable;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Args parser.
 *
 * @author Ahseya
 * @param <K> key type
 */
@Immutable
public final class ArgsParser<K extends Enum<K>> implements BiFunction<String[], Map<K, String>, List<String>> {

    private final Supplier<Map<Option, K>> optionKeyMapSupplier;

    public ArgsParser(Supplier<Map<Option, K>> optionKeyMapSupplier) {
        this.optionKeyMapSupplier = Objects.requireNonNull(optionKeyMapSupplier);
    }

    @Override
    public List<String> apply(String[] args, Map<K, String> map) throws IllegalArgumentException {
        try {
            Map<Option, K> optionKeyMap = optionKeyMapSupplier.get();

            Options options = new Options();
            optionKeyMap.keySet().forEach(options::addOption);

            CommandLineParser parser = new DefaultParser();
            CommandLine commandLine = parser.parse(options, args);

            Stream.of(commandLine.getOptions())
                    .forEach(option -> map.put(optionKeyMap.get(option), parse(option)));

            return commandLine.getArgList();

        } catch (ParseException ex) {
            throw new IllegalArgumentException(ex.getLocalizedMessage());
        }
    }

    String parse(Option option) {
        if (option.hasArgs()) {
            // Array
            return option.getValuesList()
                    .stream()
                    .collect(Collectors.joining(" "));
        }

        if (option.hasArg()) {
            // Value
            return option.getValue();
        }

        // Boolean
        return Boolean.TRUE.toString();
    }
}
