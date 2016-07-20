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

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.apache.commons.cli.Option;

/**
 * OptionsFactory.
 *
 * @author Ahseya
 */
@Immutable
public final class OptionsFactory {

    private OptionsFactory() {
    }

    public static Map<Option, Property> options() {
        LinkedHashMap<Option, Property> options = new LinkedHashMap<>();

        options.put(Option.builder("d").longOpt("device")
                .desc("Device filter, case insensitive.")
                .argName("string")
                .hasArgs()
                .build(),
                Property.FILTER_DEVICE);

        options.put(Option.builder("s").longOpt("snapshot")
                .desc("Snapshot, default: 0 = first snapshot.")
                .argName("int")
                .hasArgs()
                .build(),
                Property.FILTER_SNAPSHOT);

        options.put(Option.builder().longOpt("extension")
                .desc("File extension filter/ s, case insensitive.")
                .argName("string")
                .hasArgs()
                .build(),
                Property.FILTER_EXTENSION);

        options.put(Option.builder().longOpt("domain")
                .desc("Domain filter, case insensitive.")
                .argName("string")
                .hasArgs()
                .build(),
                Property.FILTER_DOMAIN);

        options.put(Option.builder("o").longOpt("folder")
                .desc("Output folder.")
                .argName("string")
                .hasArg()
                .build(),
                Property.OUTPUT_FOLDER);

        options.put(new Option(null, "snapshots", false, "List device/ snapshot information and exit."),
                Property.PRINT_SNAPSHOTS);

        options.put(new Option(null, "domains", false, "List domains/ file count for the selected snapshot and exit."),
                Property.PRINT_DOMAIN_LIST);

        options.put(
                new Option(null, "token", false, "Display dsPrsID:mmeAuthToken and exit."),
                Property.ARGS_TOKEN);

        options.put(Option.builder().longOpt("mode")
                .desc("Data Protection decryption mode: " + optionsDefault(PropertyDP.class, Property.DP_MODE))
                .argName("mode")
                .hasArg()
                .build(),
                Property.DP_MODE);

        options.put(
                new Option(null, "help", false, "Display this help and exit."),
                Property.ARGS_HELP);

        return options;
    }

    static String defaultValue(Property property) {
        return property.peek()
                .map(u -> " Default: " + u + ".")
                .orElse("");
    }

    static <E extends Enum<E>> String optionsDefault(Class<E> e, Property defaultValue) {
        String dv = defaultValue.peek()
                .map(u -> " (" + u + ").")
                .orElse(".");
        return   values(e) + dv;
    }

    static <E extends Enum<E>> String options(Class<E> e) {
        return values(e) + ".";
    }

    static <E extends Enum<E>> String values(Class<E> e) {
        return Arrays.asList(e.getEnumConstants())
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(" "));
    }
}
