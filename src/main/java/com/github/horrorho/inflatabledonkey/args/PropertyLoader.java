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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.jcip.annotations.Immutable;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * PropertyLoader.
 *
 * @author Ahseya
 */
@Immutable
public final class PropertyLoader implements Predicate<String[]> {

    public static PropertyLoader instance() {
        return INSTANCE;
    }

    private static final PropertyLoader INSTANCE = new PropertyLoader(OptionsFactory::options);

    private final Supplier<Map<Option, Property>> optionKeyMapSupplier;

    public PropertyLoader(Supplier<Map<Option, Property>> optionKeyMapSupplier) {
        this.optionKeyMapSupplier = Objects.requireNonNull(optionKeyMapSupplier, "optionKeyMapSupplier");
    }

    @Override
    public boolean test(String[] args) throws IllegalArgumentException {
        try {
            Map<Option, Property> optionKeyMap = optionKeyMapSupplier.get();

            Options options = new Options();
            optionKeyMap.keySet().forEach(options::addOption);

            CommandLineParser parser = new DefaultParser();
            CommandLine commandLine = parser.parse(options, args);

            Map<Property, String> properties = Stream.of(commandLine.getOptions())
                    .collect(Collectors.toMap(optionKeyMap::get, this::parse));

            if (properties.containsKey(Property.ARGS_HELP)) {
                help(OptionsFactory.options().keySet());
                return false;
            }

            List<String> operands = commandLine.getArgList();
            switch (operands.size()) {
                case 0:
                    // No setAuthenticationProperties credentials
                    throw new IllegalArgumentException(
                            "missing appleid/ password or authentication token");
                case 1:
                    // Authentication token
                    properties.put(Property.AUTHENTICATION_TOKEN, operands.get(0));
                    break;
                case 2:
                    // AppleId/ password pair
                    properties.put(Property.AUTHENTICATION_APPLEID, operands.get(0));
                    properties.put(Property.AUTHENTICATION_PASSWORD, operands.get(1));
                    break;
                default:
                    throw new IllegalArgumentException(
                            "too many non-optional arguments, expected appleid/ password or authentication token only");
            }
            Property.setProperties(properties);
            return true;

        } catch (ParseException ex) {
            throw new IllegalArgumentException(ex.getLocalizedMessage());
        }
    }

    public void help(Collection<? extends Option> optionList) {
        Options options = new Options();
        optionList.forEach(options::addOption);

        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setOptionComparator(null);
        helpFormatter.printHelp(
                Property.APP_NAME.value().orElse("") + " [OPTION]... (<token> | <appleid> <password>) ",
                options);
    }

    String parse(Option option) {
        testIntegers(option);

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

    void testIntegers(Option option) {
        if (!"int".equals(option.getArgName())) {
            return;
        }
        option.getValuesList()
                .forEach(v -> testInteger(option.getLongOpt(), v));
    }

    void testInteger(String name, String integer) {
        try {
            Integer.parseInt(integer);

        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(name + ", bad integer '" + integer + "'");
        }
    }
}
