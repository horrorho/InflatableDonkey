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
package com.github.horrorho.inflatabledonkey.util;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.dd.plist.PropertyListFormatException;
import com.dd.plist.PropertyListParser;
import com.github.horrorho.inflatabledonkey.exception.BadDataException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;
import javax.xml.parsers.ParserConfigurationException;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * PropertyLists helper.
 *
 * @author Ahseya
 */
@Immutable
public final class PListsLegacy {

    private static final Logger logger = LoggerFactory.getLogger(PListsLegacy.class);

    public static <T extends NSObject> Optional<T> cast(NSObject object, Class<T> to) {
        return (to.isAssignableFrom(object.getClass()))
                ? Optional.of(to.cast(object))
                : Optional.empty();
    }

    public static Optional<NSObject> parse(byte[] data) {
        try {
            NSObject nsObject = PropertyListParser.parse(data);
            return Optional.of(nsObject);

        } catch (IOException | PropertyListFormatException | ParseException | ParserConfigurationException | SAXException ex) {
            logger.warn("-- parse() - failed to parse NSObject data: 0x{}", Hex.toHexString(data));
            return Optional.empty();
        }
    }

    public static NSDictionary parseDictionary(byte[] data) {
        NSObject nsObject = parse(data)
                .orElseThrow(() -> new IllegalArgumentException("failed to parse NSObject data"));

        return cast(nsObject, NSDictionary.class)
                .orElseThrow(() -> new IllegalArgumentException("failed to cast to NSDictionary"));
    }

    @Deprecated
    public static <T> T parseLegacy(byte[] data) {
        try {
            return (T) PropertyListParser.parse(data);

        } catch (ClassCastException |
                IOException |
                PropertyListFormatException |
                ParseException |
                ParserConfigurationException |
                SAXException ex) {

            throw new IllegalArgumentException("failed to parse property list", ex);
        }
    }

    @Deprecated
    public static <T extends NSObject> T fetch(NSDictionary dictionary, String key) throws BadDataException {
        if (dictionary.containsKey(key)) {
            return cast(key, dictionary);
        }

        throw new BadDataException("Missing key: " + key);
    }

    @Deprecated
    public static <T extends NSObject> T fetchOrDefault(NSDictionary dictionary, String key, T defaultValue)
            throws BadDataException {

        return dictionary.containsKey(key)
                ? cast(key, dictionary)
                : defaultValue;
    }

    @Deprecated
    public static <T extends NSObject> T fetchOrNull(NSDictionary dictionary, String key) throws BadDataException {
        return fetchOrDefault(dictionary, key, null);
    }

    static <T extends NSObject> T cast(String key, NSDictionary dictionary) throws BadDataException {
        try {
            return (T) dictionary.get(key);

        } catch (ClassCastException ex) {
            throw new BadDataException("Bad type for key:" + key, ex);
        }
    }

    public static Optional<NSObject> optional(NSDictionary dictionary, String key) {
        return dictionary.containsKey(key)
                ? Optional.of(dictionary.get(key))
                : Optional.empty();
    }

    public static <T extends NSObject> Optional<T> optionalAs(NSDictionary dictionary, String key, Class<T> to) {
        return optional(dictionary, key)
                .flatMap(o -> cast(o, to));
    }

    public static NSObject get(NSDictionary dictionary, String key) {
        return optional(dictionary, key)
                .orElseThrow(() -> new IllegalArgumentException("missing key: " + key));
    }

    public static <T extends NSObject> T getAs(NSDictionary dictionary, String key, Class<T> to) {
        NSObject nsObject = get(dictionary, key);
        return cast(nsObject, to)
                .orElseThrow(() -> new IllegalArgumentException("failed cast: " + key + " to " + to));
    }

//    public static <T extends NSObject, U>
//            Optional<U> optional(NSDictionary dictionary, String key, Class<T> to, Function<T, U> then) {
//        return optional(dictionary, key, to)
//                .map(then);
//    }
//            
}
