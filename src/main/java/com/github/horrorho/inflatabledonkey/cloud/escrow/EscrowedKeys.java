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

import com.dd.plist.NSArray;
import com.dd.plist.NSData;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.dd.plist.NSString;
import com.github.horrorho.inflatabledonkey.args.Property;
import com.github.horrorho.inflatabledonkey.cloud.accounts.Account;
import com.github.horrorho.inflatabledonkey.cloud.accounts.Token;
import com.github.horrorho.inflatabledonkey.data.der.DERUtils;
import com.github.horrorho.inflatabledonkey.data.der.KeySet;
import com.github.horrorho.inflatabledonkey.pcs.service.ServiceKeySet;
import com.github.horrorho.inflatabledonkey.pcs.service.ServiceKeySetBuilder;
import com.github.horrorho.inflatabledonkey.requests.EscrowProxyRequestFactory;
import com.github.horrorho.inflatabledonkey.util.PListsLegacy;
import java.io.IOException;
import java.util.Base64;
import java.util.stream.Stream;
import org.apache.http.client.HttpClient;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EscrowedKeys.
 *
 * @author Ahseya
 */
public final class EscrowedKeys {

    private static final Logger logger = LoggerFactory.getLogger(EscrowedKeys.class);

    private static final int REMAINING_ATTEMPTS_THRESHOLD
            = Property.SRP_REMAINING_ATTEMPTS_THRESHOLD.asInteger().orElse(3);

    public static EscrowProxyRequestFactory requestFactory(Account account) {
        return new EscrowProxyRequestFactory(
                account.accountInfo().dsPrsID(),
                account.tokens().get(Token.MMEAUTHTOKEN),
                account.mobileMe().get("com.apple.Dataclass.KeychainSync", "escrowProxyUrl"));
    }

    public static byte[] data(HttpClient httpClient, Account account) throws IOException {
        EscrowProxyRequestFactory escrowProxyRequestFactory = new EscrowProxyRequestFactory(
                account.accountInfo().dsPrsID(),
                account.tokens().get(Token.MMEAUTHTOKEN),
                account.mobileMe().get("com.apple.Dataclass.KeychainSync", "escrowProxyUrl"));

        return keys(httpClient, escrowProxyRequestFactory);
    }

    public static byte[] keys(HttpClient httpClient, EscrowProxyRequestFactory requests) throws IOException {
        return keys(httpClient, requests, REMAINING_ATTEMPTS_THRESHOLD);
    }

    static byte[]
            keys(HttpClient httpClient, EscrowProxyRequestFactory requests, int remainingAttemptsThreshold)
            throws IOException {

        // TODO match BackupKeybagDigest, we are just assuming they will match.
        NSDictionary records = EscrowOperationsRecords.records(httpClient, requests);
        NSDictionary record = pcsRecord(records);

        validateRemainingAttempts(record, remainingAttemptsThreshold);

        NSDictionary escrowedData = EscrowOperationsRecover.recover(httpClient, requests);

        ServiceKeySet backupBagPassword = backupBagPassword(escrowedData);

        return escrowedKeys(record, backupBagPassword);
    }

    static void validateRemainingAttempts(NSDictionary record, int threshold) {
        int remainingAttempts = remainingAttempts(record);

        if (remainingAttempts < threshold) {
            throw new IllegalStateException("srp minimum remaining attempts threshold exceeded: " + threshold);
        };
    }

    static int remainingAttempts(NSDictionary record) {
        String remainingAttempts = PListsLegacy.getAs(record, "remainingAttempts", NSString.class).getContent();
        return Integer.parseInt(remainingAttempts);
    }

    static NSDictionary pcsRecord(NSDictionary records) {
        NSArray metadataList = PListsLegacy.getAs(records, "metadataList", NSArray.class);
        return Stream.of(metadataList.getArray())
                .filter(EscrowedKeys::isPCSRecord)
                .map(nsObject -> (NSDictionary) nsObject)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("no escrow PCS record found"));
    }

    static boolean isPCSRecord(NSObject metadata) {
        return PListsLegacy.cast(metadata, NSDictionary.class)
                .flatMap(d -> PListsLegacy.optionalAs(d, "label", NSString.class))
                .map(NSString::getContent)
                .map(label -> label.equals("com.apple.protectedcloudstorage.record"))
                .orElse(false);
    }

    static ServiceKeySet backupBagPassword(NSDictionary escrowedData) {
        byte[] backupBagPassword = PListsLegacy.getAs(escrowedData, "BackupBagPassword", NSData.class).bytes();
        logger.debug("-- backupBagPassword() - BackupBagPassword: 0x{}", Hex.toHexString(backupBagPassword));

        return DERUtils.parse(backupBagPassword, KeySet::new)
                .flatMap(ServiceKeySetBuilder::build)
                .orElseThrow(() -> new IllegalArgumentException("failed to create backup bag key set"));
    }

    static byte[] escrowedKeys(NSDictionary record, ServiceKeySet backupBagPassword) {
        String metadataBase64 = PListsLegacy.getAs(record, "metadata", NSString.class).getContent();
        byte[] metadata = Base64.getDecoder().decode(metadataBase64);

        byte[] data = ProtectedRecord.unlockData(metadata, backupBagPassword::key);
        logger.debug("-- escrowedKeys() - decrypted metadata: {}", Hex.toHexString(data));
        return data;
    }
}
