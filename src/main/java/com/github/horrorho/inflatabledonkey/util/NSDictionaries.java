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
import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;
import javax.xml.parsers.ParserConfigurationException;
import javax.annotation.concurrent.Immutable;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * NSDictionary helper.
 *
 * @author Ahseya
 */
@Immutable
public final class NSDictionaries {

    private static final Logger logger = LoggerFactory.getLogger(NSDictionaries.class);

    static <T extends NSObject> Optional<T> cast(NSObject object, Class<T> to) {
        if (to.isAssignableFrom(object.getClass())) {
            return Optional.of(to.cast(object));
        } else {
            logger.warn("-- cast() - failed cast {} > {}", object.getClass(), to);
            return Optional.empty();
        }
    }

    static Optional<NSObject> parseNSObject(byte[] data) {
        try {
            NSObject nsObject = PropertyListParser.parse(data);
            return Optional.of(nsObject);

        } catch (IOException | PropertyListFormatException | ParseException | ParserConfigurationException |
                SAXException ex) {
            logger.warn("-- parseNSObject() - failed to parse data: {} 0x{}", ex.getMessage(), Hex.toHexString(data));
            return Optional.empty();
        }
    }

    public static Optional<NSDictionary> parse(byte[] data) {
        return parseNSObject(data)
                .flatMap(u -> cast(u, NSDictionary.class));
    }

    public static Optional<NSObject> get(NSDictionary dictionary, String key) {
        return dictionary.containsKey(key)
                ? Optional.of(dictionary.get(key))
                : Optional.empty();
    }

    public static <T extends NSObject> Optional<T> as(NSDictionary dictionary, String key, Class<T> to) {
        return get(dictionary, key)
                .flatMap(u -> cast(u, to));
    }
}
