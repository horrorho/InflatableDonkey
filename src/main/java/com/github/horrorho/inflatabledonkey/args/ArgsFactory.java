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
import java.util.Locale;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import javax.annotation.concurrent.Immutable;
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
        String footer = "\nDates are ISO format e.g. 2000-12-31. Filters are case insensitive."
                + "\nPass multiple argument values separated by spaces e.g. --extension png jpg";
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
        args.add(filterItemType());
        args.add(filterSizeMax());
        args.add(filterSizeMin());
        args.add(filterBirthMin());
        args.add(filterBirthMax());
        args.add(filterStatusMin());
        args.add(filterStatusMax());
        args.add(mode());
        args.add(threads());
        args.add(turbo());
        args.add(snapshots());
        args.add(domains());
        args.add(token());
        args.add(lzfse());
        args.add(quiet());
        args.add(help());
        return args;
    }

    static Arg filterBirthMax() {
        Option option = Option.builder()
                .longOpt("date-max")
                .desc("Maximum created timestamp.")
                .argName("date")
                .hasArg()
                .build();
        return new Arg(Property.FILTER_ASSET_BIRTH_MAX, option, ArgsFactory::mapTimestamp);
    }

    static Arg filterBirthMin() {
        Option option = Option.builder()
                .longOpt("date-min")
                .desc("Minimum created timestamp.")
                .argName("date")
                .hasArg()
                .build();
        return new Arg(Property.FILTER_ASSET_BIRTH_MIN, option, ArgsFactory::mapTimestamp);
    }

    static Arg filterDevice() {
        Option option = Option.builder("d")
                .longOpt("device")
                .desc("Device filter/s. Leave empty to select all devices/ disable user selection.")
                .argName("id/s")
                .hasArgs()
                .optionalArg(true)
                .build();
        return new Arg(Property.FILTER_DEVICE, option, ArgsFactory::mapToLowerCase);
    }

    static Arg filterDomain() {
        Option option = Option.builder()
                .longOpt("domain")
                .desc("Domain filter/s.")
                .argName("string/s")
                .hasArgs()
                .build();
        return new Arg(Property.FILTER_ASSET_DOMAIN, option, ArgsFactory::mapToLowerCase);
    }

    static Arg filterExtension() {
        Option option = Option.builder()
                .longOpt("extension")
                .desc("File extension filter/s.")
                .argName("string/s")
                .hasArgs()
                .build();
        return new Arg(Property.FILTER_ASSET_EXTENSION, option, ArgsFactory::mapToLowerCase);
    }

    static Arg filterItemType() {
        Option option = Option.builder()
                .longOpt("item-type")
                .desc("Only download the specified item type/s: " + PropertyItemType.DETAILS)
                .argName("item-type/s")
                .hasArgs()
                .build();
        return new Arg(Property.FILTER_ASSET_ITEM_TYPE, option, mapEnum(PropertyItemType::valueOf));
    }

    static Arg filterRelativePath() {
        Option option = Option.builder()
                .longOpt("relative-path")
                .desc("Relative path filter/s.")
                .argName("string/s")
                .hasArgs()
                .build();
        return new Arg(Property.FILTER_ASSET_RELATIVE_PATH, option, ArgsFactory::mapToLowerCase);
    }

    static Arg filterSnapshot() {
        Option option = Option.builder("s")
                .longOpt("snapshot")
                .desc("Snapshot filter/s, 0 = first, 1 = next etc. "
                        + "Reverse selection with -1 = last, -2 = previous etc.")
                .argName("int/s")
                .hasArgs()
                .build();
        return new Arg(Property.FILTER_SNAPSHOT, option, ArgsFactory::mapNumber);
    }

    static Arg filterSizeMax() {
        Option option = Option.builder()
                .longOpt("size-max")
                .desc("Maximum file size.")
                .argName("kB")
                .hasArg()
                .build();
        return new Arg(Property.FILTER_ASSET_SIZE_MAX, option, ArgsFactory::mapNumber);
    }

    static Arg filterSizeMin() {
        Option option = Option.builder()
                .longOpt("size-min")
                .desc("Minimum file size.")
                .argName("kB")
                .hasArg()
                .build();
        return new Arg(Property.FILTER_ASSET_SIZE_MIN, option, ArgsFactory::mapNumber);
    }

    static Arg filterStatusMax() {
        Option option = Option.builder()
                .longOpt("mod-max")
                .desc("Maximum last-modified timestamp.")
                .argName("date")
                .hasArg()
                .build();
        return new Arg(Property.FILTER_ASSET_STATUS_CHANGED_MAX, option, ArgsFactory::mapTimestamp);
    }

    static Arg filterStatusMin() {
        Option option = Option.builder()
                .longOpt("mod-min")
                .desc("Minimum last-modified timestamp.")
                .argName("date")
                .hasArg()
                .build();
        return new Arg(Property.FILTER_ASSET_STATUS_CHANGED_MIN, option, ArgsFactory::mapTimestamp);
    }

    static Arg help() {
        Option option = Option.builder()
                .longOpt("help")
                .desc("Display this help and exit.")
                .hasArg(false)
                .build();
        return new Arg(Property.ARGS_HELP, option);
    }

    static Arg lzfse() {
        Option option = Option.builder()
                .longOpt("lzfse")
                .desc("Use external lzfse compressor. Optional path.")
                .argName("path")
                .hasArg()
                .optionalArg(true)
                .build();
        return new Arg(Property.PATH_LZFSE, option, ArgsFactory::mapNull);
    }

    static Arg mode() {
        Option option = Option.builder()
                .longOpt("mode")
                .desc("Data Protection decryption mode: " + optionsDefault(PropertyDataProtection.class, Property.DP_MODE))
                .argName("mode")
                .hasArg()
                .build();
        return new Arg(Property.DP_MODE, option, mapEnum(PropertyDataProtection::valueOf));
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

    static Arg quiet() {
        Option option = Option.builder()
                .longOpt("quiet")
                .desc("Reduced output verbosity.")
                .hasArg(false)
                .build();
        return new Arg(Property.QUIET, option);
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
        return new Arg(Property.PRINT_DOMAIN_LIST, option, ArgsFactory::mapToLowerCase);
    }

    static Arg threads() {
        Option option = Option.builder()
                .longOpt("threads")
                .desc("Number of concurrent download threads." + defaultValue(Property.ENGINE_THREADS))
                .argName("int")
                .hasArg()
                .build();
        return new Arg(Property.ENGINE_THREADS, option, ArgsFactory::mapNumber);
    }

    static Arg token() {
        Option option = Option.builder()
                .longOpt("token")
                .desc("Display dsPrsID:mmeAuthToken and exit.")
                .hasArg(false)
                .build();
        return new Arg(Property.ARGS_TOKEN, option);
    }

    static Arg turbo() {
        Option option = Option.builder()
                .longOpt("turbo")
                .argName("int")
                .desc("Engine thread multiplier for small assets." + defaultValue(Property.ENGINE_FRAGMENTATION_POOL_MULTIPLIER))
                .hasArgs()
                .build();
        return new Arg(Property.ENGINE_FRAGMENTATION_POOL_MULTIPLIER, option, ArgsFactory::mapNumber);
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

    static <E extends Enum<E>> UnaryOperator<String> mapEnum(Function<String, E> valueOf) {
        return u -> valueOf.apply(u.toUpperCase(Locale.US)).name();
    }

    static String mapToLowerCase(String string) {
        return string.toLowerCase(Locale.US);
    }

    static String mapNumber(String number) {
        return "" + Long.parseLong(number);
    }

    static String mapTimestamp(String date) {
        return "" + LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
                .atStartOfDay(ZoneId.systemDefault())
                .toEpochSecond();
    }

    static String mapNull(String string) {
        return string == null ? "" : string;
    }
}
// TODO negative integer rejection
