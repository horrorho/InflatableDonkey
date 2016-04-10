/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.cloudkitty;

import com.github.horrorho.inflatabledonkey.data.CKInit;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import com.github.horrorho.inflatabledonkey.requests.Headers;
import com.github.horrorho.inflatabledonkey.requests.ProtoBufsRequestFactory;
import com.github.horrorho.inflatabledonkey.responsehandler.InputStreamResponseHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CloudKit helper.
 * <p>
 * Small and inflexible subset of CloudKit API calls. No error handling.
 * <p>
 * The protobuf definitions are experimental.
 *
 * @author Ahseya
 */
@Immutable
public final class CloudKitty {

    private static final Logger logger = LoggerFactory.getLogger(CloudKitty.class);

    private static final String ckdFetchRecordZonesOperation = "CKDFetchRecordZonesOperation";
    private static final String ckdQueryOperation = "CKDQueryOperation";
    private static final String getRecordsURLRequest = "GetRecordsURLRequest";

    private static final CloudKit.RecordType privilegedBatchRecordFetch = CloudKit.RecordType.newBuilder()
            .setName("PrivilegedBatchRecordFetch")
            .build();

    private static final CloudKit.RecordFieldIdentifier recordID = CloudKit.RecordFieldIdentifier.newBuilder()
            .setName("___recordID")
            .build();

    private static final CloudKit.UInt32 one = CloudKit.UInt32.newBuilder()
            .setValue(1)
            .build();

    private final Headers coreHeaders;
    private final InputStreamResponseHandler<List<CloudKit.ResponseOperation>> responseHandler;
    private final CloudKit.RequestOperationHeader requestOperationHeaderProto;
    private final CloudKit.Identifier userId;
    private final CKInit init;
    private final String cloudKitToken;

    public CloudKitty(
            CKInit init,
            String cloudKitToken,
            String deviceHardwareID,
            String deviceIdentifier,
            Headers coreHeaders,
            InputStreamResponseHandler<List<CloudKit.ResponseOperation>> responseHandler) {

        this.coreHeaders = Objects.requireNonNull(coreHeaders);
        this.responseHandler = Objects.requireNonNull(responseHandler);
        this.init = Objects.requireNonNull(init);
        this.cloudKitToken = Objects.requireNonNull(cloudKitToken);

        this.requestOperationHeaderProto = CloudKit.RequestOperationHeader.newBuilder()
                .setF4("4.0.0.0")
                .setDeviceIdentifier(CloudKit.Identifier.newBuilder().setName(deviceIdentifier).setType(2))
                .setDeviceSoftwareVersion("9.0.1")
                .setDeviceLibraryName("com.apple.cloudkit.CloudKitDaemon")
                .setDeviceLibraryVersion("479")
                .setDeviceFlowControlBudget(40000)
                .setDeviceFlowControlBudgetCap(40000)
                .setVersion("4.0")
                .setF19(1)
                .setDeviceAssignedName("My iPhone")
                .setDeviceHardwareID(deviceHardwareID)
                .setF23(1)
                .setF25(1)
                .build();

        this.userId = CloudKit.Identifier.newBuilder()
                .setName(init.cloudKitUserId())
                .setType(7)
                .build();
    }

    public List<CloudKit.ZoneRetrieveResponse>
            zoneRetrieveRequest(HttpClient httpClient, String container, String bundle, String zone)
            throws IOException {

        // M201
        CloudKit.RequestOperation requestOperation = zoneRetrieveRequestOperation(container, bundle, zone);
        logger.debug("-- zoneRetrieveRequest() request operation: {}", requestOperation);

        HttpUriRequest uriRequest = ProtoBufsRequestFactory.defaultInstance().newRequest(
                init.production().url() + "/zone/retrieve",
                container,
                bundle,
                init.cloudKitUserId(),
                cloudKitToken,
                UUID.randomUUID().toString(),
                Arrays.asList(requestOperation),
                coreHeaders);

        List<CloudKit.ResponseOperation> response = httpClient.execute(uriRequest, responseHandler);

        logger.debug("-- zoneRetrieveRequest() response: {}", response);

        return response.stream()
                .map(CloudKit.ResponseOperation::getZoneRetrieveResponse)
                .collect(Collectors.toList());
    }

    CloudKit.RequestOperation zoneRetrieveRequestOperation(String container, String bundle, String zone) {
        CloudKit.RequestOperationHeader requestOperationHeader
                = requestOperationHeader(ckdFetchRecordZonesOperation, container, bundle);

        return zoneRetrieveRequestOperation(requestOperationHeader, zone);
    }

    CloudKit.RequestOperation zoneRetrieveRequestOperation(
            CloudKit.RequestOperationHeader requestOperationHeader,
            String zone) {

        CloudKit.Operation operation = operation(201);

        CloudKit.ZoneRetrieveRequest zoneRetrieveRequest = zoneRetrieveRequest(zone);

        CloudKit.RequestOperation build = CloudKit.RequestOperation.newBuilder()
                .setRequestOperationHeader(requestOperationHeader)
                .setOperation(operation)
                .setZoneRetrieveRequest(zoneRetrieveRequest)
                .build();

        return build;
    }

    CloudKit.ZoneRetrieveRequest zoneRetrieveRequest(String zone) {
        CloudKit.RecordZoneIdentifier recordZoneID = recordZoneIdentifier(zone);

        return CloudKit.ZoneRetrieveRequest.newBuilder()
                .setZoneIdentifier(recordZoneID)
                .build();
    }

    public List<CloudKit.RecordRetrieveResponse>
            recordRetrieveRequest(HttpClient httpClient, String container, String bundle, String zone, String... recordNames)
            throws IOException {

        return recordRetrieveRequest(httpClient, container, bundle, zone, Arrays.asList(recordNames));
    }

    public List<CloudKit.RecordRetrieveResponse>
            recordRetrieveRequest(HttpClient httpClient, String container, String bundle, String zone, Collection<String> recordNames)
            throws IOException {

        // M211
        List<CloudKit.RequestOperation> recordRetrieveRequestOperations
                = recordRetrieveRequestOperations(container, bundle, zone, recordNames);
        logger.debug("-- recordRetrieveRequest() - request operation: {}", recordRetrieveRequestOperations);

        HttpUriRequest uriRequest = ProtoBufsRequestFactory.defaultInstance().newRequest(
                init.production().url() + "/record/retrieve",
                container,
                bundle,
                init.cloudKitUserId(),
                cloudKitToken,
                UUID.randomUUID().toString(),
                recordRetrieveRequestOperations,
                coreHeaders);

        List<CloudKit.ResponseOperation> response = httpClient.execute(uriRequest, responseHandler);
        logger.debug("-- recordRetrieveRequest() response: {}", response);

        return response.stream()
                .map(CloudKit.ResponseOperation::getRecordRetrieveResponse)
                .collect(Collectors.toList());
    }

    List<CloudKit.RequestOperation>
            recordRetrieveRequestOperations(String container, String bundle, String zone, Collection<String> recordNames) {
        CloudKit.RequestOperationHeader requestOperationHeader
                = requestOperationHeader(getRecordsURLRequest, container, bundle);

        CloudKit.RecordZoneIdentifier recordZoneIdentifier = recordZoneIdentifier(zone);

        return recordRetrieveRequestOperations(requestOperationHeader, recordZoneIdentifier, recordNames);
    }

    List<CloudKit.RequestOperation> recordRetrieveRequestOperations(
            CloudKit.RequestOperationHeader requestOperationHeader,
            CloudKit.RecordZoneIdentifier recordZoneID,
            Collection<String> recordNames) {

        List<CloudKit.RequestOperation> operations = new ArrayList<>();

        Optional<CloudKit.RequestOperationHeader> optionslRequestOperationheader = Optional.of(requestOperationHeader);
        for (String recordName : recordNames) {
            operations.add(
                    recordRetrieveRequestOperation(optionslRequestOperationheader, recordZoneID, recordName));

            if (optionslRequestOperationheader.isPresent()) {
                optionslRequestOperationheader = Optional.empty();
            }
        }

        return operations;
    }

    CloudKit.RequestOperation recordRetrieveRequestOperation(
            Optional<CloudKit.RequestOperationHeader> requestOperationHeader,
            CloudKit.RecordZoneIdentifier recordZoneID,
            String recordName) {

        CloudKit.Operation operation = operation(211);
        CloudKit.RecordRetrieveRequest recordRetrieveRequest
                = CloudKitty.this.recordRetrieveRequest(recordZoneID, recordName);

        CloudKit.RequestOperation.Builder builder = CloudKit.RequestOperation.newBuilder()
                .setOperation(operation)
                .setRecordRetrieveRequest(recordRetrieveRequest);

        requestOperationHeader.map(h -> builder.setRequestOperationHeader(h));

        return builder.build();
    }

    CloudKit.RecordRetrieveRequest
            recordRetrieveRequest(CloudKit.RecordZoneIdentifier recordZoneID, String recordName) {

        CloudKit.RecordIdentifier recordIdentifier = CloudKit.RecordIdentifier.newBuilder()
                .setValue(CloudKit.Identifier.newBuilder().setName(recordName).setType(1))
                .setZoneIdentifier(recordZoneID)
                .build();

        return CloudKit.RecordRetrieveRequest.newBuilder()
                .setRecordID(recordIdentifier)
                //                .setF6(one)
                .build();
    }

    CloudKit.RequestOperationHeader requestOperationHeader(String operation, String container, String bundle) {
        return CloudKit.RequestOperationHeader.newBuilder(requestOperationHeaderProto)
                .setApplicationContainer(container)
                .setApplicationBundle(bundle)
                .setOperation(operation)
                .build();
    }

    CloudKit.Operation operation(int type) {
        return CloudKit.Operation.newBuilder()
                .setUuid(UUID.randomUUID().toString())
                .setType(type)
                //                .setF4(0)
                .build();
    }

    CloudKit.RecordZoneIdentifier recordZoneIdentifier(String zone) {
        return CloudKit.RecordZoneIdentifier.newBuilder()
                .setValue(
                        CloudKit.Identifier.newBuilder().setName(zone).setType(6))
                .setOwnerIdentifier(userId)
                .build();
    }

//    public List<CloudKit.QueryRetrieveRequestResponse>
//            queryRetrieveRequest(HttpClient httpClient, String container, String bundle, String zone, String recordName)
//            throws IOException {
//
//        // M220
//        CloudKit.RequestOperationHeader requestOperationHeader
//                = CloudKit.RequestOperationHeader.newBuilder(requestOperationHeaderProto)
//                .setApplicationContainer(container)
//                .setApplicationBundle(bundle)
//                .setOperation(ckdQueryOperation)
//                .build();
//
//        CloudKit.Operation operation = CloudKit.Operation.newBuilder()
//                .setUuid(UUID.randomUUID().toString())
//                .setType(220)
//                .setF4(1)
//                .build();
//
//        CloudKit.RecordZoneIdentifier recordZoneID = CloudKit.RecordZoneIdentifier.newBuilder()
//                .setValue(
//                        CloudKit.Identifier.newBuilder().setName(zone).setType(6))
//                .setOwnerIdentifier(userId)
//                .build();
//
//        CloudKit.RecordIdentifier recordIdentifier = CloudKit.RecordIdentifier.newBuilder()
//                .setValue(CloudKit.Identifier.newBuilder().setName(recordName).setType(1))
//                .setZoneIdentifier(recordZoneID)
//                .build();
//
//        CloudKit.RecordReference recordReference = CloudKit.RecordReference.newBuilder()
//                .setRecordIdentifier(recordIdentifier)
//                .build();
//
//        CloudKit.RecordFieldValue recordFieldValue = CloudKit.RecordFieldValue.newBuilder()
//                .setType(5)
//                .setReferenceValue(recordReference)
//                .build();
//
//        CloudKit.QueryFilter queryFilter = CloudKit.QueryFilter.newBuilder()
//                .setFieldName(recordID)
//                .setFieldValue(recordFieldValue)
//                .setType(1)
//                .build();
//
//        CloudKit.Query keyBagQuery = CloudKit.Query.newBuilder()
//                .addType(privilegedBatchRecordFetch)
//                .addFilter(queryFilter)
//                .build();
//
//        CloudKit.QueryRetrieveRequest keybagQueryRetrieveQuest = CloudKit.QueryRetrieveRequest.newBuilder()
//                .setQuery(keyBagQuery)
//                .setZoneIdentifier(recordZoneID)
//                .setF6(CloudKit.UInt32.newBuilder().setValue(1))
//                .build();
//
//        CloudKit.RequestOperation requestOperation = CloudKit.RequestOperation.newBuilder()
//                .setQueryRetrieveRequest(keybagQueryRetrieveQuest)
//                .setRequestOperationHeader(requestOperationHeader)
//                .setRequest(operation)
//                .build();
//        logger.debug("-- queryRetrieveRequest() request: {}", requestOperation);
//
//        HttpUriRequest uriRequest = ProtoBufsRequestFactory.defaultInstance().newRequest(
//                init.production().url() + "/query/retrieve",
//                container,
//                bundle,
//                init.cloudKitUserId(),
//                cloudKitToken,
//                UUID.randomUUID().toString(),
//                Arrays.asList(requestOperation),
//                coreHeaders);
//
//        List<CloudKit.ResponseOperation> response = httpClient.execute(uriRequest, responseHandler);
//        logger.debug("-- queryRetrieveRequest() response: {}", response);
//
//        return response.stream()
//                .map(CloudKit.ResponseOperation::getQueryRetrieveRequestResponse)
//                .collect(Collectors.toList());
//    }
}
// Note: Protobufs, we could pass builders on to other builders, save us an objection creation.
// TODO too much going on, break up class into smaller parts
// TODO fix operation types
