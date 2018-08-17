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
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit.AssetsToDownload;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit.Operation;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit.Query;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit.QueryRetrieveRequest;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit.QueryRetrieveResponse;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit.Record;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit.RecordIdentifier;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit.RequestOperation;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit.ResponseOperation;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import static java.util.stream.Collectors.toList;
import javax.annotation.concurrent.Immutable;
import org.apache.http.client.HttpClient;

/**
 * CloudKit M220 RecordRetrieveRequestOperation.
 *
 * @author Ahseya
 */
@Immutable
public final class QueryRetrieveRequestOperations {

    public static List<QueryRetrieveResponse> get(CloudKitty kitty, HttpClient httpClient, String zone, String... recordNames)
            throws IOException {
        return get(kitty, httpClient, zone, Arrays.asList(recordNames));
    }

    public static List<QueryRetrieveResponse>
            get(CloudKitty kitty, HttpClient httpClient, String zone, Collection<String> recordNames)
            throws IOException {
        List<RequestOperation> operations = operations(kitty.cloudKitUserId(), zone, recordNames);
        return kitty.get(httpClient, KEY, operations, ResponseOperation::getQueryRetrieveResponse);
    }

    static List<RequestOperation> operations(String cloudKitUserId, String zone, Collection<String> recordNames) {
        return Arrays.asList(operation(cloudKitUserId, zone, recordNames));
    }

    static RequestOperation operation(String cloudKitUserId, String zone, Collection<String> recordNames) {
        return RequestOperation.newBuilder()
                .setRequest(CKProto.operation(Operation.Type.QUERY_RETRIEVE_TYPE))
                .setQueryRetrieveRequest(request(cloudKitUserId, zone, recordNames))
                .build();
    }

    static QueryRetrieveRequest request(String cloudKitUserId, String zone, Collection<String> recordNames) {
        Query query = query(cloudKitUserId, zone, recordNames);
        AssetsToDownload assetsToDownload = AssetsToDownload.newBuilder().setAllAssets(true).build();
        return QueryRetrieveRequest.newBuilder()
                .setQuery(query)
                .setZoneIdentifier(CKProto.recordZoneIdentifier(zone, cloudKitUserId))
                .setAssetsToDownload(assetsToDownload)
                .build();
    }

    static Query query(String cloudKitUserId, String zone, Collection<String> recordNames) {
        List<RecordIdentifier> recordIdentifierList = recordNames
                .stream()
                .map(u -> CKProto.recordIdentifier(zone, u, cloudKitUserId))
                .collect(toList());
        Record.Field.Value fieldValue = valueList(recordIdentifierList);
        Record.Field.Identifier fieldName = CKProto.recordFieldIdentifier("___recordID");
        Query.Filter filter = filter(fieldName, fieldValue, Query.Filter.Type.IN);

        return Query.newBuilder()
                .addTypes(CKProto.recordType("PrivilegedBatchRecordFetch"))
                .addFilters(filter)
                .build();
    }

    static Query.Filter filter(Record.Field.Identifier fieldName, Record.Field.Value fieldValue, Query.Filter.Type type) {
        return Query.Filter.newBuilder()
                .setFieldName(fieldName)
                .setFieldValue(fieldValue)
                .setType(type)
                .build();
    }

    static Record.Field.Value valueList(List<RecordIdentifier> recordIdentifierList) {
        Record.Field.Value.Builder builder = Record.Field.Value.newBuilder()
                .setType(Record.Field.Value.Type.REFERENCE_LIST_TYPE);
        recordIdentifierList.stream()
                .map(QueryRetrieveRequestOperations::value)
                .forEach(builder::addListValues);
        return builder.build();
    }

    static Record.Field.Value value(RecordIdentifier recordIdentifierList) {
        return Record.Field.Value.newBuilder()
                .setType(Record.Field.Value.Type.REFERENCE_TYPE)
                .setReferenceValue(CKProto.reference(Record.Reference.Type.WEAK, recordIdentifierList))
                .build();
    }

    private static final String KEY = "CKDQueryOperation";
}
