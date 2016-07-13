/*
 * The MIT License
 *
 * Copyright 2016 Ahseya.
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
package com.github.horrorho.inflatabledonkey.cloud.escrow;

import com.dd.plist.NSDictionary;
import com.github.horrorho.inflatabledonkey.requests.EscrowProxyRequestFactory;
import com.github.horrorho.inflatabledonkey.responsehandler.PropertyListResponseHandler;
import java.io.IOException;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EscrowOperationsRecords.
 *
 * @author Ahseya
 */
@Immutable
public final class EscrowOperationsRecords {

    private static final Logger logger = LoggerFactory.getLogger(EscrowOperationsRecords.class);

    private static final PropertyListResponseHandler<NSDictionary> RESPONSE_HANDLER
            = PropertyListResponseHandler.dictionary();

    public static NSDictionary records(HttpClient httpClient, EscrowProxyRequestFactory requests) throws IOException {
        /* 
        EscrowService SRP-6a exchanges: GETRECORDS
         */
        HttpUriRequest recordsRequest = requests.getRecords();
        NSDictionary dictionary = httpClient.execute(recordsRequest, RESPONSE_HANDLER);
        logger.debug("-- records() - GETRECORDS: {}", dictionary.toXMLPropertyList());
        return dictionary;
    }
}
