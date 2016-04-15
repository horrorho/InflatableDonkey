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
import java.util.Optional;
import net.jcip.annotations.Immutable;

/**
 * Configuration properties.
 *
 * @author Ahseya
 */
@Immutable
public enum Property {

    APP_NAME("InflatableDonkey"),
    ARGS_HELP,
    ARGS_TOKEN,
    ARGS_VERSION,
    AUTHENTICATION_APPLEID,
    AUTHENTICATION_PASSWORD,
    AUTHENTICATION_TOKEN,
    DECRYPTION_BLOCK_LENGTH("4096"),
    PATH_PROTOC("protoc"),
    PATH_CHUNK_STORE("chunks"),
    PATH_CHUNK_STORE_SUBSPLIT("3"),
    SELECT_DEVICE_INDEX("0"),
    SELECT_SNAPSHOT_INDEX("0"),
    SELECT_MANIFEST_INDEX("0"),
    PROPERTIES_RESOURCE("/inflatable_donkey.properties");

    public static DateTimeFormatter commandLineInputDateTimeFormatter() {
        return DateTimeFormatter.ISO_DATE;
    }

    public static DateTimeFormatter outputDateTimeFormatter() {
        return DateTimeFormatter.RFC_1123_DATE_TIME;
    }

    private final String defaultValue;

    private Property() {
        this(null);
    }

    private Property(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String defaultValue() {
        return defaultValue;
    }

    public Optional<Integer> intValue() {
        try {
            return Optional.of(Integer.parseInt(defaultValue));

        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }
}
