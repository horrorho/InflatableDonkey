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
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.concurrent.Immutable;
import org.apache.http.client.HttpClient;

/**
 * CloudKit M211 RecordRetrieveRequestOperation.
 *
 * @author Ahseya
 */
@Immutable
public final class RecordRetrieveRequestOperations {

    public static List<RecordRetrieveResponse>
            get(CloudKitty kitty, HttpClient httpClient, String zone, String... recordNames)
            throws IOException {
        return get(kitty, httpClient, zone, Arrays.asList(recordNames));
    }

    public static List<RecordRetrieveResponse> get(CloudKitty kitty, HttpClient httpClient, String zone, Collection<String> recordNames) throws IOException {

        List<RequestOperation> operations = operations(zone, recordNames, kitty.cloudKitUserId());
        return kitty.get(httpClient, OPERATION, operations, ResponseOperation::getRecordRetrieveResponse);
    }

    static List<RequestOperation> operations(String zone, Collection<String> recordNames, String cloudKitUserId) {
        return recordNames.stream()
                .map(u -> operation(zone, u, cloudKitUserId))
                .collect(Collectors.toList());
    }

    static RequestOperation operation(String zone, String recordName, String cloudKitUserId) {
        return RequestOperation.newBuilder()
                .setOperation(CKProto.operation(211))
                .setRecordRetrieveRequest(request(zone, recordName, cloudKitUserId))
                .build();
    }

    static RecordRetrieveRequest request(String zone, String recordName, String cloudKitUserId) {
        return RecordRetrieveRequest.newBuilder()
                .setRecordID(CKProto.recordIdentifier(zone, recordName, cloudKitUserId))
                .build();
    }

    private static final String OPERATION = "GetRecordsURLRequest";
}
