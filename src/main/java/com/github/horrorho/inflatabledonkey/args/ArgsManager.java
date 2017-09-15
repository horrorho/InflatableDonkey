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
package com.github.horrorho.inflatabledonkey.args;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import javax.annotation.concurrent.NotThreadSafe;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@NotThreadSafe
public class ArgsManager {

    private static final Logger logger = LoggerFactory.getLogger(ArgsManager.class);

    static Arg merge(Arg a, Arg b) {
        throw new IllegalArgumentException("option collision");
    }

    private final Map<Option, Arg> args;

    public ArgsManager(Collection<Arg> args) {
        this.args = args
                .stream()
                .collect(Collectors.toMap(Arg::option, Function.identity(), ArgsManager::merge, LinkedHashMap::new));
    }

    public Set<Option> listOptions() {
        return args.keySet();
    }

    public Options options() {
        Options options = new Options();
        args.values()
                .stream()
                .map(Arg::option)
                .forEach(options::addOption);
        return options;
    }

    public Map<Property, String> process(Option... options) {
        return Arrays.asList(options)
                .stream()
                .map(this::value)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }

    Map.Entry<Property, String> value(Option option) {
        Arg arg = args.get(option);
        if (arg == null) {
            throw new IllegalArgumentException("unreferenced option");
        }
        String value = parse(option, arg::map);
        return new SimpleImmutableEntry<>(arg.property(), value);
    }

    String parse(Option option, UnaryOperator<String> parser) {
        if (option.hasArgs()) {
            return option.getValuesList()
                    .stream()
                    .map(u -> parse(option.getLongOpt(), u, parser))
                    .collect(Collectors.joining(" "));
        }
        if (option.hasArg()) {
            return parse(option.getLongOpt(), option.getValue(), parser);
        }
        return Boolean.TRUE.toString();
    }

    String parse(String optionName, String value, UnaryOperator<String> parser) {
        try {
            return parser.apply(value);
        } catch (RuntimeException ex) {
            logger.debug("-- test() - RuntimeException: {}", ex.getMessage());
            throw new IllegalArgumentException(optionName + " has bad value '" + value + "'");
        }
    }
}
