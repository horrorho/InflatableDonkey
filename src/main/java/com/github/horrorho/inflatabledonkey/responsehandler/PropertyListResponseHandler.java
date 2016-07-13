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
package com.github.horrorho.inflatabledonkey.responsehandler;

import com.dd.plist.NSArray;
import com.dd.plist.NSData;
import com.dd.plist.NSDate;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;
import com.dd.plist.NSObject;
import com.dd.plist.NSSet;
import com.dd.plist.PropertyListFormatException;
import com.dd.plist.PropertyListParser;
import com.dd.plist.UID;
import com.github.horrorho.inflatabledonkey.exception.BadDataException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;
import javax.xml.parsers.ParserConfigurationException;
import net.jcip.annotations.Immutable;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.xml.sax.SAXException;

/**
 * PropertyList ResponseHandler.
 *
 * @author Ahseya
 * @param <T> type
 */
@Immutable
public final class PropertyListResponseHandler<T extends NSObject> extends DonkeyResponseHandler<T> {

    public static PropertyListResponseHandler<NSArray> array() {
        return ARRAY;
    }

    public static PropertyListResponseHandler<NSData> data() {
        return DATA;
    }

    public static PropertyListResponseHandler<NSDate> date() {
        return DATE;
    }

    public static PropertyListResponseHandler<NSDictionary> dictionary() {
        return DICTIONARY;
    }

    public static PropertyListResponseHandler<NSNumber> number() {
        return NUMBER;
    }

    public static PropertyListResponseHandler<NSSet> set() {
        return SET;
    }

    public static PropertyListResponseHandler<UID> uid() {
        return UID;
    }

    private static final PropertyListResponseHandler<NSArray> ARRAY = new PropertyListResponseHandler<>(NSArray.class);

    private static final PropertyListResponseHandler<NSData> DATA = new PropertyListResponseHandler<>(NSData.class);

    private static final PropertyListResponseHandler<NSDate> DATE = new PropertyListResponseHandler<>(NSDate.class);

    private static final PropertyListResponseHandler<NSDictionary> DICTIONARY = new PropertyListResponseHandler<>(NSDictionary.class);

    private static final PropertyListResponseHandler<NSNumber> NUMBER = new PropertyListResponseHandler<>(NSNumber.class);

    private static final PropertyListResponseHandler<NSSet> SET = new PropertyListResponseHandler<>(NSSet.class);

    private static final PropertyListResponseHandler<UID> UID = new PropertyListResponseHandler<>(UID.class);

    private final Class<T> to;

    PropertyListResponseHandler(Class<T> to) {
        this.to = Objects.requireNonNull(to, "to");
    }

    @Override
    public T handleEntity(HttpEntity entity) throws IOException {
        NSObject nsObject;

        try {
            // Avoiding PropertyListParser#parse(InputStream) as the current Maven build (1.16) can bug out.
            nsObject = PropertyListParser.parse(EntityUtils.toByteArray(entity));

        } catch (PropertyListFormatException | ParseException | ParserConfigurationException | SAXException ex) {
            throw new BadDataException("failed to parse property list", ex);
        }

        if (to.isAssignableFrom(nsObject.getClass())) {
            return to.cast(nsObject);
        }

        throw new BadDataException("failed to cast property list: " + nsObject.getClass());
    }
}
