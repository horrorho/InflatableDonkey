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
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit.RequestedFields;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit.ResponseOperation;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import static java.util.stream.Collectors.toList;
import javax.annotation.concurrent.Immutable;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CloudKit M220 RecordRetrieveRequestOperation.
 *
 * @author Ahseya
 */
@Immutable
public final class QueryRetrieveRequestOperations {

    private static final Logger logger = LoggerFactory.getLogger(QueryRetrieveRequestOperations.class);

    public static List<QueryRetrieveResponse>
            get(CloudKitty kitty, HttpClient httpClient, String type, String zone, List<String> requestedFields, Collection<String> recordNames)
            throws IOException {
        List<RequestOperation> operations = operations(kitty.cloudKitUserId(), type, zone, requestedFields, recordNames);
        return kitty.get(httpClient, API, KEY, operations, ResponseOperation::getQueryRetrieveResponse);
    }

    static List<RequestOperation> operations(String cloudKitUserId, String type, String zone, List<String> requestedFields, Collection<String> recordNames) {
        return Arrays.asList(operation(cloudKitUserId, type, zone, requestedFields, recordNames));
    }

    static RequestOperation operation(String cloudKitUserId, String type, String zone, List<String> requestedFields, Collection<String> recordNames) {
        return RequestOperation.newBuilder()
                .setRequest(CKProto.operation(Operation.Type.QUERY_RETRIEVE_TYPE))
                .setQueryRetrieveRequest(request(cloudKitUserId, type, zone, requestedFields, recordNames))
                .build();
    }

    static QueryRetrieveRequest request(String cloudKitUserId, String type, String zone, List<String> requestedFields, Collection<String> recordNames) {
        List<Record.Field.Identifier> fieldList = requestedFields.stream()
                .map(u -> Record.Field.Identifier.newBuilder().setName(u).build())
                .collect(toList());
        RequestedFields _requestedFields = RequestedFields.newBuilder().addAllFields(fieldList).build();

        Query query = query(cloudKitUserId, type, zone, recordNames);
        AssetsToDownload assetsToDownload = AssetsToDownload.newBuilder().setAllAssets(true).build();
        QueryRetrieveRequest.Builder builder = QueryRetrieveRequest.newBuilder()
                .setQuery(query)
                .setZoneIdentifier(CKProto.recordZoneIdentifier(zone, cloudKitUserId))
                .setAssetsToDownload(assetsToDownload);
        if (_requestedFields.getFieldsCount() > 0) {
            builder.setRequestedFields(_requestedFields);
        }
        return builder.build();
    }

    static Query query(String cloudKitUserId, String type, String zone, Collection<String> recordNames) {
        List<RecordIdentifier> recordIdentifierList = recordNames
                .stream()
                .map(u -> CKProto.recordIdentifier(zone, u, cloudKitUserId))
                .collect(toList());
        Record.Field.Value fieldValue = valueList(recordIdentifierList);
        Record.Field.Identifier fieldName = CKProto.recordFieldIdentifier("___recordID");
        Query.Filter filter = filter(fieldName, fieldValue, Query.Filter.Type.IN);

        return Query.newBuilder()
                .addTypes(CKProto.recordType(type))
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

    private static final String API = "/query/retrieve";
}
