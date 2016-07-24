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
package com.github.horrorho.inflatabledonkey.cloud.clients;

import com.github.horrorho.inflatabledonkey.cloudkitty.CloudKitty;
import com.github.horrorho.inflatabledonkey.cloudkitty.operations.RecordRetrieveRequestOperations;
import com.github.horrorho.inflatabledonkey.data.backup.BackupAccount;
import com.github.horrorho.inflatabledonkey.data.backup.BackupAccountFactory;
import com.github.horrorho.inflatabledonkey.pcs.zone.PZFactory;
import com.github.horrorho.inflatabledonkey.pcs.zone.ProtectionZone;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class BackupAccountClient {

    private static final Logger logger = LoggerFactory.getLogger(BackupAccountClient.class);

    public static Optional<BackupAccount>
            backupAccount(HttpClient httpClient, CloudKitty kitty, ProtectionZone zone) throws UncheckedIOException {

        List<CloudKit.RecordRetrieveResponse> responses
                = RecordRetrieveRequestOperations.get(kitty, httpClient, "mbksync", "BackupAccount");
        if (responses.size() != 1) {
            logger.warn("-- backupAccount() - bad response list size: {}", responses);
            return Optional.empty();
        }
        
        CloudKit.RecordRetrieveResponse response = responses.get(0);
        if (!response.hasRecord()) {
            logger.warn("-- backupAccount() - no BackupAccount record");
            return Optional.empty();
        }
        
        CloudKit.ProtectionInfo protectionInfo = response.getRecord().getProtectionInfo();
        Optional<ProtectionZone> optionalNewZone = PZFactory.instance().create(zone, protectionInfo);
        if (!optionalNewZone.isPresent()) {
            logger.warn("-- backupAccount() - failed to retrieve protection info");
            return Optional.empty();
        }
        ProtectionZone newZone = optionalNewZone.get();

        BackupAccount backupAccount = BackupAccountFactory.from(responses.get(0).getRecord(), newZone);
        logger.debug("-- backupAccount() - backup account: {}", backupAccount);
        return Optional.of(backupAccount);
    }
}
