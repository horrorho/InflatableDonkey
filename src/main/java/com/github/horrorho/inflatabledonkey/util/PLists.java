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
import java.util.function.Function;
import javax.xml.parsers.ParserConfigurationException;
import net.jcip.annotations.Immutable;
import org.xml.sax.SAXException;

/**
 * PropertyLists helper.
 *
 * @author Ahseya
 */
@Immutable
public final class PLists {

    /*
    TODO
    static <T> T cast(ASN1Primitive primitive, Class<T> to) {
        try {
            return to.cast(primitive);

        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("Unexpected ASN1Primitive type", ex);
        }
    }
    }
    
     */
    public static <T> T parse(byte[] data) {
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

    public static <T extends NSObject> T get(NSDictionary dictionary, String key) throws BadDataException {
        if (dictionary.containsKey(key)) {
            return cast(key, dictionary);
        }

        throw new BadDataException("Missing key: " + key);
    }

    public static <T extends NSObject> T getOrDefault(NSDictionary dictionary, String key, T defaultValue)
            throws BadDataException {

        return dictionary.containsKey(key)
                ? cast(key, dictionary)
                : defaultValue;
    }

    public static <T extends NSObject> T getOrNull(NSDictionary dictionary, String key) throws BadDataException {
        return getOrDefault(dictionary, key, null);
    }

    static <T extends NSObject> T cast(String key, NSDictionary dictionary) throws BadDataException {
        try {
            return (T) dictionary.get(key);

        } catch (ClassCastException ex) {
            throw new BadDataException("Bad type for key:" + key, ex);
        }
    }

    static <T extends NSObject> Optional<T> cast(NSObject object, Class<T> to) {
        return (to.isAssignableFrom(object.getClass()))
                ? Optional.of(to.cast(object))
                : Optional.empty();
    }

    public static Optional<NSObject> optional(NSDictionary dictionary, String key) {
        return dictionary.containsKey(key)
                ? Optional.of(dictionary.get(key))
                : Optional.empty();
    }

    public static <T extends NSObject> Optional<T> optional(NSDictionary dictionary, String key, Class<T> to) {
        return optional(dictionary, key)
                .flatMap(o -> cast(o, to));
    }

    public static <T extends NSObject, U>
            Optional<U> optional(NSDictionary dictionary, String key, Class<T> to, Function<T, U> then) {
        return optional(dictionary, key, to)
                .map(then);
    }

}
