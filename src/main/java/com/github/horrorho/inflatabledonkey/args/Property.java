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

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration properties.
 *
 * @author Ahseya
 */
public enum Property {

    APP_NAME("InflatableDonkey"),
    ARGS_HELP,
    ARGS_TOKEN,
    ARGS_VERSION,
    AUTHENTICATION_APPLEID,
    AUTHENTICATION_PASSWORD,
    AUTHENTICATION_TOKEN,
    DP_AESCBC_BLOCK_SIZE("4096"),
    DP_AESXTS_BLOCK_SIZE("4096"),
    DP_MODE("auto"),
    FILTER_DEVICE(""),
    FILTER_DOMAIN(""),
    FILTER_EXTENSION(""),
    FILTER_SNAPSHOT(),
    FILE_WRITER_BUFFER_LENGTH("16384"),
    OUTPUT_FOLDER("backups"),
    PRINT_DOMAIN_LIST("false"),
    PRINT_SNAPSHOTS("false"),
    PATH_CHUNK_STORE("chunks"),
    PATH_CHUNK_STORE_SUBSPLIT("3"),
    PROTOC_PATH("protoc"),
    PROTOC_TIMEOUT_MS("15000"),
    SRP_REMAINING_ATTEMPTS_THRESHOLD("3"),
    PROPERTIES_RESOURCE("/inflatable_donkey.properties");

    static void setProperties(Map<Property, String> properties) {
        if (touched) {
            throw new IllegalStateException("Property already in use");
        }
        properties.forEach((p, v) -> p.setValue(v));

        if (touched) {
            throw new IllegalStateException("Property used during initialization");
        }
        touched = true;

        if (logger.isDebugEnabled()) {
            properties.forEach((p, v) -> logger.debug("-- setProperties() - property: {} value: {}", p, v));
        }
    }

    static void touch() {
        touched = true;
    }

    static int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            throw ex;
        }
    }

    static long parseLong(String s) {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException ex) {
            throw ex;
        }
    }

    public static Locale locale() {
        return Locale.US;
    }

    public static DateTimeFormatter commandLineInputDateTimeFormatter() {
        return DateTimeFormatter.ISO_DATE;
    }

    public static DateTimeFormatter outputDateTimeFormatter() {
        return DateTimeFormatter.RFC_1123_DATE_TIME;
    }

    private static final Logger logger = LoggerFactory.getLogger(Property.class);

    private static volatile boolean touched = false;

    private String value;

    private Property() {
        this(null);
    }

    private Property(String value) {
        this.value = value;
    }

    private void setValue(String value) {
        this.value = value;
    }

    public Optional<String> value() {
        touch();
        return Optional.ofNullable(value);
    }

    public Optional<Boolean> asBoolean() {
        return value()
                .map(v -> Boolean.TRUE.toString().equals(v));
    }

    public Optional<Integer> asInteger() {
        return value().map(Property::parseInt);
    }

    public Optional<List<Integer>> asIntegerList() {
        return asStringList()
                .map(l -> l
                        .stream()
                        .map(Property::parseInt)
                        .collect(Collectors.toList()));
    }

    public Optional<Long> asLong() {
        return value().map(Property::parseLong);
    }

    public Optional<List<String>> asStringList() {
        return value()
                .map(v -> Arrays.asList(v.split(" ")));
    }

    public <T> Optional<T> as(Function<String, Optional<T>> function) {
        return value().flatMap(function);
    }
}
