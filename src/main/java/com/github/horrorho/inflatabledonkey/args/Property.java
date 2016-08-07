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
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    DP_MODE("AUTO"),
    ENGINE_BATCH_THRESHOLD("1048576"),
    ENGINE_FRAGMENTATION_THRESHOLD("64"),
    ENGINE_FRAGMENTATION_POOL_MULTIPLIER("4"),
    ENGINE_THREADS("4"),
    FILTER_ASSET_BIRTH_MAX(),
    FILTER_ASSET_BIRTH_MIN(),
    FILTER_ASSET_DOMAIN(),
    FILTER_ASSET_EXTENSION(),
    FILTER_ASSET_RELATIVE_PATH(),
    FILTER_ASSET_SIZE_MAX(),
    FILTER_ASSET_SIZE_MIN(),
    FILTER_ASSET_STATUS_CHANGED_MAX(),
    FILTER_ASSET_STATUS_CHANGED_MIN(),
    FILTER_DEVICE(),
    FILTER_SNAPSHOT(),
    FILE_WRITER_BUFFER_LENGTH("16384"),
    HTTP_CLIENT_CONNECTIONS_MAX_TOTAL("256"),
    HTTP_CLIENT_CONNECTIONS_MAX_PER_ROUTE("32"),
    HTTP_CLIENT_TIMEOUT_MS("60000"),
    OUTPUT_FOLDER("backups"),
    PRINT_DOMAIN_LIST("false"),
    PRINT_SNAPSHOTS("false"),
    PATH_CHUNK_STORE("chunks"),
    PATH_CHUNK_STORE_SUBSPLIT("3"),
    PATH_TEMP("temp"),
    PROTOC_PATH("protoc"),
    PROTOC_TIMEOUT_MS("15000"),
    SRP_REMAINING_ATTEMPTS_THRESHOLD("3"),
    PROPERTIES_RESOURCE("/inflatable_donkey.properties");

    static synchronized void setProperties(Map<Property, String> properties) {
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

    @Deprecated
    public static Locale locale() {
        return Locale.US;
    }

    public static DateTimeFormatter outputDateTimeFormatter() {
        return DateTimeFormatter.RFC_1123_DATE_TIME;
    }

    private static final Logger logger = LoggerFactory.getLogger(Property.class);

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE;

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

    Optional<String> peek() {
        return Optional.ofNullable(value);
    }

    public Optional<String> value() {
        touch();
        return Optional.ofNullable(value);
    }

    public Optional<Boolean> asBoolean() {
        return as(Boolean::parseBoolean);
    }

    public Optional<Integer> asInteger() throws NumberFormatException {
        return as(Integer::parseInt);
    }

    public Optional<Long> asLong() throws NumberFormatException {
        return as(Long::parseLong);
    }

    public Optional<ZonedDateTime> asDate() throws DateTimeParseException {
        return as(u -> LocalDate.parse(u, DATE_TIME_FORMATTER)
                .atStartOfDay(ZoneId.systemDefault()));
    }

    public Optional<List<String>> asList() {
        return value()
                .map(v -> Arrays.asList(v.split(" ")));
    }

    public <T> Optional<List<T>> asList(Function<String, T> to) {
        return value()
                .map(u -> Arrays.asList(u.split(" ")))
                .map(u -> u.stream().map(to).collect(Collectors.toList()));
    }

    public <T> Optional<T> as(Function<String, T> to) {
        return value().map(to);
    }

    public <T> Optional<T> optional(Function<String, Optional<T>> function) {
        return value().flatMap(function);
    }

    @Deprecated
    public Optional<List<Integer>> asIntegerList() {
        return asList()
                .map(l -> l
                        .stream()
                        .map(Integer::parseInt)
                        .collect(Collectors.toList()));
    }
}
