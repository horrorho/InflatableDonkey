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

import com.github.horrorho.inflatabledonkey.cloudkitty.operations.RecordRetrieveRequestOperations;
import com.github.horrorho.inflatabledonkey.cloudkitty.operations.ZoneRetrieveRequestOperations;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit.*;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;

/**
 * Super basic CloudKit API.
 *
 * @author Ahseya
 */
@Immutable
public final class CloudKitty {

    private final CKClient client;
    private final String cloudKitUserId;

    public CloudKitty(CKClient client, String cloudKitUserId) {
        this.client = client;
        this.cloudKitUserId = cloudKitUserId;
    }

    public List<ZoneRetrieveResponse>
            zoneRetrieveRequest(HttpClient httpClient, Collection<String> zones)
            throws UncheckedIOException {
        List<RequestOperation> requests = zones.stream()
                .map(u -> ZoneRetrieveRequestOperations.operation(u, cloudKitUserId))
                .collect(Collectors.toList());
        return client.get(httpClient, ZoneRetrieveRequestOperations.OPERATION, requests,
                ResponseOperation::getZoneRetrieveResponse);
    }

    public List<RecordRetrieveResponse>
            recordRetrieveRequest(HttpClient httpClient, String zone, Collection<String> recordNames)
            throws UncheckedIOException {
        List<RequestOperation> requests = recordNames.stream()
                .map(u -> RecordRetrieveRequestOperations.operation(zone, u, cloudKitUserId))
                .collect(Collectors.toList());
        return client.get(httpClient, RecordRetrieveRequestOperations.OPERATION, requests,
                ResponseOperation::getRecordRetrieveResponse);
    }
}
// TODO error response
