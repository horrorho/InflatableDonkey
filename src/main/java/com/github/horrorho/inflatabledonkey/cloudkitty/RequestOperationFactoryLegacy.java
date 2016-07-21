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

import com.github.horrorho.inflatabledonkey.protobuf.CloudKit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RequestOperationFactory.
 * <p>
 * Small and inflexible subset of CloudKit API calls. No error handling.
 * <p>
 * The protobuf definitions are experimental.
 *
 * @author Ahseya
 */
@Immutable
public final class RequestOperationFactoryLegacy {

    private static final Logger logger = LoggerFactory.getLogger(RequestOperationFactoryLegacy.class);

    public static final CloudKit.RequestOperationHeader baseRequestOperationHeader
            = CloudKit.RequestOperationHeader.newBuilder()
            .setF4("4.0.0.0")
            .setDeviceSoftwareVersion("9.0.1")
            .setDeviceLibraryName("com.apple.cloudkit.CloudKitDaemon")
            .setDeviceLibraryVersion("479")
            .setDeviceFlowControlBudget(40000)
            .setDeviceFlowControlBudgetCap(40000)
            .setVersion("4.0")
            .setF19(1)
            .setDeviceAssignedName("My iPhone")
            .setF23(1)
            .setF25(1)
            .build();

    private static final String ckdFetchRecordZonesOperation = "CKDFetchRecordZonesOperation";
    private static final String ckdQueryOperation = "CKDQueryOperation";
    private static final String getRecordsURLRequest = "GetRecordsURLRequest";

    private final CloudKit.RequestOperationHeader requestOperationHeaderProto;
    private final CloudKit.Identifier cloudKitUserId;
    private final String container;
    private final String bundle;

    // TODO complete
    public RequestOperationFactoryLegacy(
            String cloudKitUserId,
            String container,
            String bundle,
            String deviceHardwareID,
            String deviceID) {

        CloudKit.Identifier deviceIdentifier = identifier(deviceID, 2);

        this.requestOperationHeaderProto = CloudKit.RequestOperationHeader.newBuilder(baseRequestOperationHeader)
                .setDeviceIdentifier(deviceIdentifier)
                .setDeviceHardwareID(deviceHardwareID)
                .build();

        this.cloudKitUserId = identifier(cloudKitUserId, 7);
        this.container = Objects.requireNonNull(container, "container");
        this.bundle = Objects.requireNonNull(bundle, "bundle");
    }

    public List<CloudKit.RequestOperation> zoneRetrieveRequestOperation(Collection<String> zones) {
        CloudKit.RequestOperationHeader requestOperationHeader
                = requestOperationHeader(ckdFetchRecordZonesOperation);

        return zoneRetrieveRequestOperation(requestOperationHeader, zones);
    }

    public List<CloudKit.RequestOperation> zoneRetrieveRequestOperation(
            CloudKit.RequestOperationHeader requestOperationHeader,
            Collection<String> zones) {

        List<CloudKit.RequestOperation> operations = new ArrayList<>();

        Optional<CloudKit.RequestOperationHeader> optionalRequestOperationheader = Optional.of(requestOperationHeader);
        for (String zone : zones) {
            operations.add(
                    zoneRetrieveRequestOperation(optionalRequestOperationheader, zone));

            if (optionalRequestOperationheader.isPresent()) {
                optionalRequestOperationheader = Optional.empty();
            }
        }

        return operations;
    }

    CloudKit.RequestOperation zoneRetrieveRequestOperation(
            Optional<CloudKit.RequestOperationHeader> requestOperationHeader,
            String zone) {

        CloudKit.Operation operation = operation(201);
        CloudKit.ZoneRetrieveRequest zoneRetrieveRequest = zoneRetrieveRequest(zone);

        CloudKit.RequestOperation.Builder builder = CloudKit.RequestOperation.newBuilder()
                .setOperation(operation)
                .setZoneRetrieveRequest(zoneRetrieveRequest);
        requestOperationHeader.map(header -> builder.setRequestOperationHeader(header));

        return builder.build();
    }

    CloudKit.ZoneRetrieveRequest zoneRetrieveRequest(String zone) {
        CloudKit.RecordZoneIdentifier recordZoneID = recordZoneIdentifier(zone);

        return CloudKit.ZoneRetrieveRequest.newBuilder()
                .setZoneIdentifier(recordZoneID)
                .build();
    }

    public List<CloudKit.RequestOperation>
            recordRetrieveRequestOperations(String zone, Collection<String> recordNames) {
        CloudKit.RequestOperationHeader requestOperationHeader
                = requestOperationHeader(getRecordsURLRequest);

        return recordRetrieveRequestOperations(requestOperationHeader, zone, recordNames);
    }

    List<CloudKit.RequestOperation> recordRetrieveRequestOperations(
            CloudKit.RequestOperationHeader requestOperationHeader,
            String zone,
            Collection<String> recordNames) {

        List<CloudKit.RequestOperation> operations = new ArrayList<>();

        Optional<CloudKit.RequestOperationHeader> optionalRequestOperationheader = Optional.of(requestOperationHeader);
        for (String recordName : recordNames) {
            operations.add(
                    recordRetrieveRequestOperation(optionalRequestOperationheader, zone, recordName));

            if (optionalRequestOperationheader.isPresent()) {
                optionalRequestOperationheader = Optional.empty();
            }
        }

        return operations;
    }

    CloudKit.RequestOperation recordRetrieveRequestOperation(
            Optional<CloudKit.RequestOperationHeader> requestOperationHeader,
            String zone,
            String recordName) {

        CloudKit.Operation operation = operation(211);
        CloudKit.RecordRetrieveRequest recordRetrieveRequest
                = recordRetrieveRequest(zone, recordName);

        CloudKit.RequestOperation.Builder builder = CloudKit.RequestOperation.newBuilder()
                .setOperation(operation)
                .setRecordRetrieveRequest(recordRetrieveRequest);
        requestOperationHeader.map(header -> builder.setRequestOperationHeader(header));

        return builder.build();
    }

    CloudKit.RecordRetrieveRequest
            recordRetrieveRequest(String zone, String recordName) {
        CloudKit.RecordZoneIdentifier recordZoneID = recordZoneIdentifier(zone);
        CloudKit.RecordIdentifier recordIdentifier = recordIdentifier(recordZoneID, recordName);

        return CloudKit.RecordRetrieveRequest.newBuilder()
                .setRecordID(recordIdentifier)
                .build();
    }

    CloudKit.RequestOperationHeader requestOperationHeader(String operation) {
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
                .build();
    }

    CloudKit.RecordZoneIdentifier recordZoneIdentifier(String zone) {
        CloudKit.Identifier identifier = identifier(zone, 6);

        return CloudKit.RecordZoneIdentifier.newBuilder()
                .setValue(identifier)
                .setOwnerIdentifier(cloudKitUserId)
                .build();
    }

    CloudKit.RecordIdentifier recordIdentifier(CloudKit.RecordZoneIdentifier recordZoneID, String recordName) {
        CloudKit.Identifier identifier = identifier(recordName, 1);

        return CloudKit.RecordIdentifier.newBuilder()
                .setValue(identifier)
                .setZoneIdentifier(recordZoneID)
                .build();
    }

    static CloudKit.Identifier identifier(String name, int type) {
        return CloudKit.Identifier.newBuilder()
                .setName(name)
                .setType(type)
                .build();
    }

    @Override
    public String toString() {
        return "RequestOperationFactory{"
                + "requestOperationHeaderProto=" + requestOperationHeaderProto
                + ", userID=" + cloudKitUserId
                + ", container=" + container
                + ", bundle=" + bundle
                + '}';
    }
}
