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
package com.github.horrorho.inflatabledonkey.cloud.zone;

import com.github.horrorho.inflatabledonkey.cloudkitty.CloudKitty;
import com.github.horrorho.inflatabledonkey.data.backup.BackupAccount;
import com.github.horrorho.inflatabledonkey.data.backup.BackupAccountFactory;
import com.github.horrorho.inflatabledonkey.pcs.xzone.ProtectionZone;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BackupAccountClient.
 *
 * @author Ahseya
 */
@Immutable
public final class BackupAccountClient {

    private static final Logger logger = LoggerFactory.getLogger(BackupAccountClient.class);

    public static Optional<ProtectedItem<BackupAccount>>
            backupAccount(HttpClient httpClient, CloudKitty kitty, ProtectionZone zone) throws IOException {

        List<CloudKit.RecordRetrieveResponse> responses
                = kitty.recordRetrieveRequest(httpClient, "mbksync", "BackupAccount");
        logger.debug("-- backupAccount() - responses: {}", responses);

        if (responses.size() != 1) {
            logger.warn("-- backupAccount() - bad response list size: {}", responses);
            return Optional.empty();
        }

        CloudKit.ProtectionInfo protectionInfo = responses.get(0)
                .getRecord()
                .getProtectionInfo();

        Optional<ProtectionZone> optionalNewZone = zone.newProtectionZone(protectionInfo);
        if (!optionalNewZone.isPresent()) {
            logger.warn("-- backupAccount() - failed to retrieve protection info");
            return Optional.empty();
        }
        ProtectionZone newZone = optionalNewZone.get();

        BackupAccount backupAccount
                = BackupAccountFactory.from(responses.get(0).getRecord(), (bs, id) -> newZone.decrypt(bs, id).get());    // TODO rework BackupAccount
        logger.debug("-- backupAccount() - backup account: {}", backupAccount);

        ProtectedItem<BackupAccount> protectedItem = new ProtectedItem<>(newZone, backupAccount);
        return Optional.of(protectedItem);
    }
}
