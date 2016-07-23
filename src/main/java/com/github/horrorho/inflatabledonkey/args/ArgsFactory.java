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

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.apache.commons.cli.Option;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class ArgsFactory {

    private ArgsFactory() {
    }

    public static Args defaults() {
        String header = "iOS9 iCloud backup retrieval proof of concept tool.\n\n";
        String footer = null;
        String cmdLineSyntax = Property.APP_NAME.peek().orElse("") + " (<token> | <appleid> <password>) [OPTION]...";
        return new Args(ArgsFactory::defaultArgList, cmdLineSyntax, header, footer);
    }

    static List<Arg> defaultArgList() {
        List<Arg> args = new ArrayList<>();
        args.add(outputFolder());
        args.add(filterDevice());
        args.add(filterSnapshot());
        args.add(filterDomain());
        args.add(filterRelativePath());
        args.add(filterExtension());
        args.add(filterDateMin());
        args.add(filterDateMax());
        args.add(mode());
        args.add(snapshots());
        args.add(domains());
        args.add(token());
        args.add(help());
        return args;
    }

    static Arg filterDateMin() {
        Option option = Option.builder()
                .longOpt("min-date")
                .desc("Minimum last-modified timestamp, ISO format date. E.g. 2000-12-31.")
                .argName("date")
                .hasArg()
                .build();
        return new Arg(Property.FILTER_ASSET_STATUS_CHANGED_MIN, option, ArgsFactory::parseTimestamp);
    }

    static Arg filterDateMax() {
        Option option = Option.builder()
                .longOpt("max-date")
                .desc("Maximum last-modified timestamp, ISO format date. E.g. 2000-12-31.")
                .argName("date")
                .hasArg()
                .build();
        return new Arg(Property.FILTER_ASSET_STATUS_CHANGED_MAX, option, ArgsFactory::parseTimestamp);
    }

    static Arg filterDevice() {
        Option option = Option.builder("d")
                .longOpt("device")
                .desc("Device filter/s, case insensitive.")
                .argName("string")
                .hasArgs()
                .build();
        return new Arg(Property.FILTER_DEVICE, option);
    }

    static Arg filterDomain() {
        Option option = Option.builder()
                .longOpt("domain")
                .desc("Domain filter/s, case insensitive.")
                .argName("string")
                .hasArgs()
                .build();
        return new Arg(Property.FILTER_ASSET_DOMAIN, option, ArgsFactory::parseLowerCase);
    }

    static Arg filterExtension() {
        Option option = Option.builder()
                .longOpt("extension")
                .desc("File extension filter/s, case insensitive.")
                .argName("string")
                .hasArgs()
                .build();
        return new Arg(Property.FILTER_ASSET_EXTENSION, option, ArgsFactory::parseLowerCase);
    }

    static Arg filterRelativePath() {
        Option option = Option.builder()
                .longOpt("relative-path")
                .desc("Relative path filter/s, case insensitive.")
                .argName("string")
                .hasArgs()
                .build();
        return new Arg(Property.FILTER_ASSET_RELATIVE_PATH, option);
    }

    static Arg filterSnapshot() {
        Option option = Option.builder("s")
                .longOpt("snapshot")
                .desc("Snapshot filter/s, default: 0 = first snapshot.")
                .argName("int")
                .hasArgs()
                .build();
        return new Arg(Property.FILTER_SNAPSHOT, option);
    }

    static Arg help() {
        Option option = Option.builder()
                .longOpt("help")
                .desc("Display this help and exit.")
                .hasArg(false)
                .build();
        return new Arg(Property.ARGS_HELP, option);
    }

    static Arg mode() {
        Option option = Option.builder()
                .longOpt("mode")
                .desc("Data Protection decryption mode: " + optionsDefault(PropertyDP.class, Property.DP_MODE))
                .argName("mode")
                .hasArg()
                .build();
        return new Arg(Property.DP_MODE, option, enumParser(PropertyDP::valueOf));
    }

    static Arg outputFolder() {
        Option option = Option.builder("o")
                .longOpt("folder")
                .desc("Output folder.")
                .argName("string")
                .hasArg()
                .build();
        return new Arg(Property.OUTPUT_FOLDER, option);
    }

    static Arg snapshots() {
        Option option = Option.builder()
                .longOpt("snapshots")
                .desc("List device/ snapshot information and exit.")
                .hasArg(false)
                .build();
        return new Arg(Property.PRINT_SNAPSHOTS, option);
    }

    static Arg domains() {
        Option option = Option.builder()
                .longOpt("domains")
                .desc("List domains/ file count for the selected snapshot/s and exit.")
                .hasArg(false)
                .build();
        return new Arg(Property.PRINT_DOMAIN_LIST, option, ArgsFactory::parseLowerCase);
    }

    static Arg token() {
        Option option = Option.builder()
                .longOpt("token")
                .desc("Display dsPrsID:mmeAuthToken and exit.")
                .hasArg(false)
                .build();
        return new Arg(Property.ARGS_TOKEN, option);
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
        return values(e) + dv;
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

    static <E extends Enum<E>> UnaryOperator<String> enumParser(Function<String, E> valueOf) {
        return u -> valueOf.apply(u.toUpperCase(Property.locale())).name();
    }

    static String parseLowerCase(String string) {
        return string.toLowerCase(Property.locale());
    }

    static String parseNumber(String number) {
        return "" + Long.parseLong(number);
    }

    static String parseTimestamp(String date) {
        return "" + LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
                .atStartOfDay(ZoneId.systemDefault())
                .toEpochSecond();
    }
}
