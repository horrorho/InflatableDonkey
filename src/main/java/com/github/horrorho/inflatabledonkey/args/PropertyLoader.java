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
import javax.annotation.concurrent.Immutable;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(PropertyLoader.class);
    private static final PropertyLoader INSTANCE = new PropertyLoader(ArgsFactory.defaults());

    private final Args args;

    public PropertyLoader(Args args) {
        this.args = Objects.requireNonNull(args, "args");
    }

    @Override
    public boolean test(String[] arguments) throws IllegalArgumentException {
        try {
            ArgsManager manager = new ArgsManager(args.args());
            Options options = manager.options();
            CommandLineParser parser = new DefaultParser();
            CommandLine commandLine = parser.parse(options, arguments);
            Map<Property, String> properties = manager.process(commandLine.getOptions());

            if (properties.containsKey(Property.ARGS_HELP)) {
                help(manager.listOptions(), args.header(), args.footer(), args.cmdLineSyntax());
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
            properties.forEach((u, v) -> logger.info("-- test() - {} = {}", u.name(), v));
            Property.setProperties(properties);
            return true;

        } catch (ParseException ex) {
            throw new IllegalArgumentException(ex.getLocalizedMessage());
        }
    }

    void help(Collection<? extends Option> optionList, String header, String footer, String cmdLineSyntax) {
        Options options = new Options();
        optionList.forEach(options::addOption);

        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setOptionComparator(null);
        helpFormatter.printHelp(
                cmdLineSyntax,
                header,
                options,
                footer);
    }
}
