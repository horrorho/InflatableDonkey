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
package com.github.horrorho.inflatabledonkey.data;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;
import com.dd.plist.NSString;
import com.github.horrorho.inflatabledonkey.exception.BadDataException;
import com.github.horrorho.inflatabledonkey.requests.AuthenticationRequestFactory;
import com.github.horrorho.inflatabledonkey.requests.Headers;
import com.github.horrorho.inflatabledonkey.responsehandler.PropertyListResponseHandler;
import com.github.horrorho.inflatabledonkey.util.PLists;
import java.io.IOException;
import java.util.Objects;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Auth factory.
 *
 * @author Ahseya
 */
@Immutable
public final class Authenticator {

    private static final Logger logger = LoggerFactory.getLogger(Authenticator.class);

    private final AuthenticationRequestFactory authenticationRequestFactory;
    private final ResponseHandler<NSDictionary> nsDictionaryResponseHandler;

    Authenticator(
            AuthenticationRequestFactory authenticationRequestFactory,
            ResponseHandler<NSDictionary> nsDictionaryResponseHandler) {
        this.authenticationRequestFactory = Objects.requireNonNull(authenticationRequestFactory);
        this.nsDictionaryResponseHandler = Objects.requireNonNull((nsDictionaryResponseHandler));
    }

    public Authenticator(Headers headers) {
        this(new AuthenticationRequestFactory(headers), PropertyListResponseHandler.nsDictionaryResponseHandler());
    }

    public Auth authenticate(HttpClient httpClient, String id, String password) throws BadDataException, IOException {
        logger.trace("<< authenticate() < id: {} password: {}", id, password);

        try {
            NSDictionary authentication
                    = httpClient.execute(
                            authenticationRequestFactory.apply(id, password),
                            nsDictionaryResponseHandler);

            logger.debug("-- authenticate() - authentication: {}", authentication.toASCIIPropertyList());

            NSDictionary appleAccountInfo = PLists.get(authentication, "appleAccountInfo");
            String dsPrsID = PLists.<NSNumber>get(appleAccountInfo, "dsPrsID").toString();

            NSDictionary tokens = PLists.get(authentication, "tokens");
            String mmeAuthToken = PLists.<NSString>get(tokens, "mmeAuthToken").toString();

            logger.debug("-- authenticate() -  dsPrsID: {}", dsPrsID);
            logger.debug("-- authenticate() -  mmeAuthToken: {}", mmeAuthToken);

            Auth auth = new Auth(dsPrsID, mmeAuthToken);

            logger.trace(">> authenticate() > auth: {}", auth);
            return auth;

        } catch (HttpResponseException ex) {
            int statusCode = ex.getStatusCode();

            if (statusCode == 401) {
                throw new HttpResponseException(statusCode, "Bad appleId/ password or not an iCloud account?");
            }

            if (statusCode == 409) {
                throw new HttpResponseException(statusCode, "Two-step enabled or partial iCloud account activation?");
            }

            throw ex;
        }
    }
}
