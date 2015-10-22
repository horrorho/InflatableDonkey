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
import com.github.horrorho.inflatabledonkey.exception.BadDataException;
import net.jcip.annotations.Immutable;

/**
 * PropertyLists helper.
 *
 * @author Ahseya
 */
@Immutable
public final class PLists {

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
}
