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
package com.github.horrorho.inflatabledonkey.cloudkitty;

import com.github.horrorho.inflatabledonkey.cloud.accounts.Account;
import com.github.horrorho.inflatabledonkey.cloud.accounts.Token;
import com.github.horrorho.inflatabledonkey.cloud.cloudkit.CKInit;
import com.github.horrorho.inflatabledonkey.requests.ProtoBufsRequestFactory;
import java.util.concurrent.ForkJoinPool;
import net.jcip.annotations.Immutable;

/**
 * CloudKitty factory.
 *
 * @author Ahseya
 */
@Immutable
public final class CloudKitties {

    public static CloudKitty backupd(ForkJoinPool forkJoinPool, CKInit ckInit, Account account) {
        return create(forkJoinPool, ckInit, account, "com.apple.backup.ios", "com.apple.backupd");
    }

    public static CloudKitty
            create(ForkJoinPool forkJoinPool, CKInit ckInit, Account account, String container, String bundle) {
        String cloudKitToken = account.tokens().get(Token.CLOUDKITTOKEN);
        String cloudKitUserId = ckInit.cloudKitUserId();
        // Re-direct issues with ckInit baseUrl.
        String baseUrl = account.mobileMe()
                .optional("com.apple.Dataclass.CKDatabaseService", "url")
                .map(url -> url + "/api/client")
                .orElseGet(() -> ckInit.production().url());

        return create(forkJoinPool, container, bundle, cloudKitUserId, cloudKitToken, baseUrl);
    }

    static CloudKitty test() {
        String container = "CONTAINER";
        String bundle = "BUNDLE";
        String cloudKitToken = "CLOUDKIT_TOKEN";
        String cloudKitUserId = "CLOUDKIT_USERID";
        String baseUrl = "BASE_URL";
        return create(new ForkJoinPool(1), container, bundle, cloudKitUserId, cloudKitToken, baseUrl);
    }

    static CloudKitty create(ForkJoinPool forkJoinPool, String container, String bundle, String cloudKitUserId,
            String cloudKitToken, String baseUrl) {

        String url = baseUrl + RECORD_RETRIEVE;
        RequestOperationHeaders requestOperationHeaders = new RequestOperationHeaders(container, bundle);
        ProtoBufsRequestFactory requestFactory
                = new ProtoBufsRequestFactory(url, container, bundle, cloudKitUserId, cloudKitToken);
        return new CloudKitty(requestOperationHeaders, requestFactory, forkJoinPool);
    }
    private static final String RECORD_RETRIEVE = "/record/retrieve";
}
