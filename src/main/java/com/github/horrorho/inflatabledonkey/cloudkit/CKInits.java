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
package com.github.horrorho.inflatabledonkey.cloudkit;

import com.github.horrorho.inflatabledonkey.cloud.accounts.Account;
import com.github.horrorho.inflatabledonkey.cloud.accounts.Token;
import com.github.horrorho.inflatabledonkey.requests.CkAppInitBackupRequestFactory;
import com.github.horrorho.inflatabledonkey.responsehandler.JsonResponseHandler;
import java.io.IOException;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * CKInits.
 *
 * @author Ahseya
 */
@Immutable
public final class CKInits {

    public static CKInit ckInitBackupd(HttpClient httpClient, Account account) throws IOException {
        return ckInit(httpClient, account, "com.apple.backupd", "com.apple.backup.ios");
    }

    public static CKInit
            ckInit(HttpClient httpClient, Account account, String bundle, String container) throws IOException {

        String dsPrsID = account.accountInfo().dsPrsID();
        String mmeAuthToken = account.tokens().get(Token.MMEAUTHTOKEN);
        String cloudKitToken = account.tokens().get(Token.CLOUDKITTOKEN);

        HttpUriRequest ckAppInitRequest
                = CkAppInitBackupRequestFactory.instance()
                .newRequest(
                        dsPrsID,
                        mmeAuthToken,
                        cloudKitToken,
                        bundle,
                        container);

        JsonResponseHandler<CKInit> jsonResponseHandler = new JsonResponseHandler<>(CKInit.class);

        return httpClient.execute(ckAppInitRequest, jsonResponseHandler);
    }
}
// TODO consider bundle, container fields in CKInit.
