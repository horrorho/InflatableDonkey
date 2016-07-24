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
package com.github.horrorho.inflatabledonkey.cloudkitty.operations;

import com.github.horrorho.inflatabledonkey.cloudkitty.CKProto;
import com.github.horrorho.inflatabledonkey.cloudkitty.CloudKitty;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;

/**
 * CloudKit M211 RecordRetrieveRequestOperation.
 *
 * @author Ahseya
 */
@Immutable
public final class ZoneRetrieveRequestOperations {

    public static Optional<List<ZoneRetrieveResponse>> get(CloudKitty kitty, HttpClient httpClient, String... zones) {
        return get(kitty, httpClient, Arrays.asList(zones));
    }

    public static Optional<List<ZoneRetrieveResponse>>
            get(CloudKitty kitty, HttpClient httpClient, Collection<String> zones) {
        List<RequestOperation> operations = operations(zones, kitty.cloudKitUserId());
        return kitty.get(httpClient, OPERATION, operations, ResponseOperation::getZoneRetrieveResponse);
    }

    public static List<RequestOperation> operations(Collection<String> zones, String cloudKitUserId) {
        return zones.stream()
                .map(u -> operation(u, cloudKitUserId))
                .collect(Collectors.toList());
    }

    public static RequestOperation operation(String zone, String cloudKitUserId) {
        return RequestOperation.newBuilder()
                .setOperation(CKProto.operation(201))
                .setZoneRetrieveRequest(request(zone, cloudKitUserId))
                .build();
    }

    static ZoneRetrieveRequest request(String zone, String cloudKitUserId) {
        RecordZoneIdentifier recordZoneID = CKProto.recordZoneIdentifier(zone, cloudKitUserId);
        return ZoneRetrieveRequest.newBuilder()
                .setZoneIdentifier(recordZoneID)
                .build();
    }

    private static final String OPERATION = "CKDFetchRecordZonesOperation";
}
