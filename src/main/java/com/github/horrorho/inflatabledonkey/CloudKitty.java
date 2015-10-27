/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey;

import com.github.horrorho.inflatabledonkey.data.CKInit;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import com.github.horrorho.inflatabledonkey.requests.Headers;
import com.github.horrorho.inflatabledonkey.requests.ProtoBufsRequestFactory;
import com.github.horrorho.inflatabledonkey.responsehandler.InputStreamResponseHandler;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CloudKit helper.
 *
 * @author Ahseya
 */
@Immutable
public final class CloudKitty {

    private static final Logger logger = LoggerFactory.getLogger(CloudKitty.class);

    private static final String ckdFetchRecordZonesOperation = "CKDFetchRecordZonesOperation";
    private static final String ckdQueryOperation = "CKDQueryOperation";

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
            String deviceID,
            Headers coreHeaders,
            InputStreamResponseHandler<List<CloudKit.ResponseOperation>> responseHandler) {

        this.coreHeaders = Objects.requireNonNull(coreHeaders);
        this.responseHandler = Objects.requireNonNull(responseHandler);
        this.init = Objects.requireNonNull(init);
        this.cloudKitToken = Objects.requireNonNull(cloudKitToken);

        this.requestOperationHeaderProto = CloudKit.RequestOperationHeader.newBuilder()
                .setDeviceIdentifier(CloudKit.Identifier.newBuilder().setName(deviceID).setType(2))
                .setDeviceSoftwareVersion("9.0.1")
                .setDeviceLibraryName("com.apple.cloudkit.CloudKitDaemon")
                .setDeviceLibraryVersion("479")
                .setDeviceFlowControlBudget(40000)
                .setDeviceFlowControlBudgetCap(40000)
                .setVersion("4.0")
                .setF19(1)
                .setDeviceAssignedName("My iPhone")
                .setDeviceHardwareID(deviceID)
                .setF23(1)
                .setF25(1)
                .build();

        this.userId = CloudKit.Identifier.newBuilder()
                .setName(init.cloudKitUserId())
                .setType(7)
                .build();
    }

    List<CloudKit.ZoneRetrieveResponse>
            zoneRetrieveRequest(HttpClient httpClient, String container, String bundle, String zone)
            throws IOException {

        // M201
        CloudKit.RequestOperationHeader requestOperationHeader
                = CloudKit.RequestOperationHeader.newBuilder(requestOperationHeaderProto)
                .setApplicationContainer(container)
                .setApplicationBundle(bundle)
                .setOperation(ckdFetchRecordZonesOperation)
                .build();

        CloudKit.Operation operation = CloudKit.Operation.newBuilder()
                .setUuid(UUID.randomUUID().toString())
                .setType(201)
                .setF4(1)
                .build();

        CloudKit.RecordZoneIdentifier recordZoneID = CloudKit.RecordZoneIdentifier.newBuilder()
                .setValue(
                        CloudKit.Identifier.newBuilder().setName(zone).setType(6))
                .setOwnerIdentifier(userId)
                .build();

        CloudKit.ZoneRetrieveRequest requestProto = CloudKit.ZoneRetrieveRequest.newBuilder()
                .setZoneIdentifier(recordZoneID)
                .build();

        CloudKit.RequestOperation requestOperation = CloudKit.RequestOperation.newBuilder()
                .setRequestOperationHeader(requestOperationHeader)
                .setRequest(operation)
                .setZoneRetrieveRequest(requestProto)
                .build();
        logger.debug("-- zoneRetrieveRequest() request: {}", requestOperation);

        HttpUriRequest uriRequest = ProtoBufsRequestFactory.defaultInstance().newRequest(
                init.production().url() + "/record/retrieve",
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

    List<CloudKit.RecordRetrieveResponse>
            recordRetrieveRequest(HttpClient httpClient, String container, String bundle, String zone, String recordName)
            throws IOException {

        // M211
        CloudKit.RequestOperationHeader requestOperationHeader
                = CloudKit.RequestOperationHeader.newBuilder(requestOperationHeaderProto)
                .setApplicationContainer(container)
                .setApplicationBundle(bundle)
                .setOperation(ckdFetchRecordZonesOperation)
                .build();

        CloudKit.Operation operation = CloudKit.Operation.newBuilder()
                .setUuid(UUID.randomUUID().toString())
                .setType(211)
                .setF4(1)
                .build();

        CloudKit.RecordZoneIdentifier recordZoneID = CloudKit.RecordZoneIdentifier.newBuilder()
                .setValue(
                        CloudKit.Identifier.newBuilder().setName(zone).setType(6))
                .setOwnerIdentifier(userId)
                .build();

        CloudKit.RecordIdentifier recordIdentifier = CloudKit.RecordIdentifier.newBuilder()
                .setValue(CloudKit.Identifier.newBuilder().setName(recordName).setType(1))
                .setZoneIdentifier(recordZoneID)
                .build();

        CloudKit.RecordRetrieveRequest recordRetrieveRequest = CloudKit.RecordRetrieveRequest.newBuilder()
                .setRecordID(recordIdentifier)
                .setF6(one)
                .build();

        CloudKit.RequestOperation requestOperation = CloudKit.RequestOperation.newBuilder()
                .setRequestOperationHeader(requestOperationHeader)
                .setRequest(operation)
                .setRecordRetrieveRequest(recordRetrieveRequest)
                .build();
        logger.debug("-- recordRetrieveRequest() request: {}", requestOperation);

        HttpUriRequest uriRequest = ProtoBufsRequestFactory.defaultInstance().newRequest(
                init.production().url() + "/record/retrieve",
                container,
                bundle,
                init.cloudKitUserId(),
                cloudKitToken,
                UUID.randomUUID().toString(),
                Arrays.asList(requestOperation),
                coreHeaders);

        List<CloudKit.ResponseOperation> response = httpClient.execute(uriRequest, responseHandler);
        logger.debug("-- recordRetrieveRequest() response: {}", response);

        return response.stream()
                .map(CloudKit.ResponseOperation::getRecordRetrieveResponse)
                .collect(Collectors.toList());
    }

    List<CloudKit.QueryRetrieveRequestResponse>
            queryRetrieveRequest(HttpClient httpClient, String container, String bundle, String zone, String recordName)
            throws IOException {

        // M220
        CloudKit.RequestOperationHeader requestOperationHeader
                = CloudKit.RequestOperationHeader.newBuilder(requestOperationHeaderProto)
                .setApplicationContainer(container)
                .setApplicationBundle(bundle)
                .setOperation(ckdQueryOperation)
                .build();

        CloudKit.Operation operation = CloudKit.Operation.newBuilder()
                .setUuid(UUID.randomUUID().toString())
                .setType(220)
                .setF4(1)
                .build();

        CloudKit.RecordZoneIdentifier recordZoneID = CloudKit.RecordZoneIdentifier.newBuilder()
                .setValue(
                        CloudKit.Identifier.newBuilder().setName(zone).setType(6))
                .setOwnerIdentifier(userId)
                .build();

        CloudKit.RecordIdentifier recordIdentifier = CloudKit.RecordIdentifier.newBuilder()
                .setValue(CloudKit.Identifier.newBuilder().setName(recordName).setType(1))
                .setZoneIdentifier(recordZoneID)
                .build();

        CloudKit.RecordReference recordReference = CloudKit.RecordReference.newBuilder()
                .setRecordIdentifier(recordIdentifier)
                .build();

        CloudKit.RecordFieldValue recordFieldValue = CloudKit.RecordFieldValue.newBuilder()
                .setType(5)
                .setReferenceValue(recordReference)
                .build();

        CloudKit.QueryFilter queryFilter = CloudKit.QueryFilter.newBuilder()
                .setFieldName(recordID)
                .setFieldValue(recordFieldValue)
                .setType(1)
                .build();

        CloudKit.Query keyBagQuery = CloudKit.Query.newBuilder()
                .addType(privilegedBatchRecordFetch)
                .addFilter(queryFilter)
                .build();

        CloudKit.QueryRetrieveRequest keybagQueryRetrieveQuest = CloudKit.QueryRetrieveRequest.newBuilder()
                .setQuery(keyBagQuery)
                .setZoneIdentifier(recordZoneID)
                .setF6(CloudKit.UInt32.newBuilder().setValue(1))
                .build();

        CloudKit.RequestOperation requestOperation = CloudKit.RequestOperation.newBuilder()
                .setQueryRetrieveRequest(keybagQueryRetrieveQuest)
                .setRequestOperationHeader(requestOperationHeader)
                .setRequest(operation)
                .build();
        logger.debug("-- queryRetrieveRequest() request: {}", requestOperation);

        HttpUriRequest uriRequest = ProtoBufsRequestFactory.defaultInstance().newRequest(
                init.production().url() + "/query/retrieve",
                container,
                bundle,
                init.cloudKitUserId(),
                cloudKitToken,
                UUID.randomUUID().toString(),
                Arrays.asList(requestOperation),
                coreHeaders);

        List<CloudKit.ResponseOperation> response = httpClient.execute(uriRequest, responseHandler);
        logger.debug("-- queryRetrieveRequest() response: {}", response);

        return response.stream()
                .map(CloudKit.ResponseOperation::getQueryRetrieveRequestResponse)
                .collect(Collectors.toList());
    }
}
