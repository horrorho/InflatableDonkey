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
import javax.xml.parsers.ParserConfigurationException;
import net.jcip.annotations.Immutable;
import org.apache.http.HttpEntity;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.apache.http.util.EntityUtils;
import org.xml.sax.SAXException;

/**
 * PropertyList ResponseHandler.
 *
 * @author Ahseya
 * @param <T> type
 */
@Immutable
public final class PropertyListResponseHandler<T extends NSObject> extends AbstractResponseHandler<T> {

    public static PropertyListResponseHandler<NSArray> nsArrayResponseHandler() {
        return nsArrayResponseHandlerInstance;
    }

    public static PropertyListResponseHandler<NSData> nsDataResponseHandler() {
        return nsDataResponseHandlerInstance;
    }

    public static PropertyListResponseHandler<NSDate> nsDateResponseHandler() {
        return nsDateResponseHandlerInstance;
    }

    public static PropertyListResponseHandler<NSDictionary> nsDictionaryResponseHandler() {
        return nsDictionaryResponseHandlerInstance;
    }

    public static PropertyListResponseHandler<NSNumber> nsNumberResponseHandler() {
        return nsNumberResponseHandlerInstance;
    }

    public static PropertyListResponseHandler<NSSet> nsSetResponseHandler() {
        return nsSetResponseHandlerInstance;
    }

    public static PropertyListResponseHandler<UID> nsUIDResponseHandler() {
        return nsUIDResponseHandlerInstance;
    }

    private static final PropertyListResponseHandler<NSArray> nsArrayResponseHandlerInstance
            = new PropertyListResponseHandler<>();

    private static final PropertyListResponseHandler<NSData> nsDataResponseHandlerInstance
            = new PropertyListResponseHandler<>();

    private static final PropertyListResponseHandler<NSDate> nsDateResponseHandlerInstance
            = new PropertyListResponseHandler<>();

    private static final PropertyListResponseHandler<NSDictionary> nsDictionaryResponseHandlerInstance
            = new PropertyListResponseHandler<>();

    private static final PropertyListResponseHandler<NSNumber> nsNumberResponseHandlerInstance
            = new PropertyListResponseHandler<>();

    private static final PropertyListResponseHandler<NSSet> nsSetResponseHandlerInstance
            = new PropertyListResponseHandler<>();

    private static final PropertyListResponseHandler<UID> nsUIDResponseHandlerInstance
            = new PropertyListResponseHandler<>();

    PropertyListResponseHandler() {
    }

    @Override
    public T handleEntity(HttpEntity entity) throws IOException {
        try {
            return (T) PropertyListParser.parse(EntityUtils.toByteArray(entity));

        } catch (ClassCastException |
                IOException |
                PropertyListFormatException |
                ParseException |
                ParserConfigurationException |
                SAXException ex) {

            throw new BadDataException("Failed to parse property list", ex);
        }
    }
}
