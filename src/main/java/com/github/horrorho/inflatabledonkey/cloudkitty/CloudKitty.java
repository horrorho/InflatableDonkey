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

import com.github.horrorho.inflatabledonkey.RawProtoDecoderLogger;
import com.github.horrorho.inflatabledonkey.cloud.cloudkit.CKInit;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import com.github.horrorho.inflatabledonkey.requests.ProtoBufsRequestFactory;
import com.github.horrorho.inflatabledonkey.responsehandler.InputStreamResponseHandler;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CloudKitty.
 *
 * @author Ahseya
 */
@Immutable
public final class CloudKitty {

    public static CloudKitty backupd(CKInit ckInit, String cloudKitToken) {

        String container = "com.apple.backup.ios";
        String bundle = "com.apple.backupd";

        String cloudKitUserId = ckInit.cloudKitUserId();
        String baseUrl = ckInit.production().url();

        String deviceID = UUID.randomUUID().toString();
        String deviceHardwareID = new BigInteger(256, ThreadLocalRandom.current()).toString(16).toUpperCase(Locale.US);

        RequestOperationFactory factory
                = new RequestOperationFactory(
                        cloudKitUserId,
                        container,
                        bundle,
                        deviceHardwareID,
                        deviceID);

        InputStreamResponseHandler<List<CloudKit.ResponseOperation>> responseHandler
                = new InputStreamResponseHandler<>(new RawProtoDecoderLogger(null));

        return new CloudKitty(
                factory,
                container,
                bundle,
                cloudKitUserId,
                cloudKitToken,
                baseUrl,
                responseHandler);
    }

    private static final Logger logger = LoggerFactory.getLogger(CloudKitty.class);

    // TODO test limit
    // TODO inject via Property
    private static final int REQUEST_LIMIT = 400;

    private final RequestOperationFactory factory;
    private final String container;
    private final String bundle;
    private final String cloudKitUserId;
    private final String cloudKitToken;
    private final String baseUrl;
    private final InputStreamResponseHandler<List<CloudKit.ResponseOperation>> responseHandler;

    public CloudKitty(
            RequestOperationFactory factory,
            String container,
            String bundle,
            String cloudKitUserId,
            String cloudKitToken,
            String baseUrl,
            InputStreamResponseHandler<List<CloudKit.ResponseOperation>> responseHandler) {

        this.factory = Objects.requireNonNull(factory, "factory");
        this.container = Objects.requireNonNull(container, "container");
        this.bundle = Objects.requireNonNull(bundle, "bundle");
        this.cloudKitUserId = Objects.requireNonNull(cloudKitUserId, "cloudKitUserId");
        this.cloudKitToken = Objects.requireNonNull(cloudKitToken, "cloudKitToken");
        this.baseUrl = Objects.requireNonNull(baseUrl, "baseUrl");
        this.responseHandler = Objects.requireNonNull(responseHandler, "responseHandler");
    }

    public List<CloudKit.ZoneRetrieveResponse>
            zoneRetrieveRequest(HttpClient httpClient, String... zones)
            throws IOException {
        return zoneRetrieveRequest(httpClient, Arrays.asList(zones));
    }

    public List<CloudKit.ZoneRetrieveResponse>
            zoneRetrieveRequest(HttpClient httpClient, Collection<String> zones)
            throws IOException {

        // M201
        List<CloudKit.RequestOperation> requestOperations = factory.zoneRetrieveRequestOperation(zones);
        logger.debug("-- zoneRetrieveRequest() request operation: {}", requestOperations);

        List<CloudKit.ResponseOperation> response = doRequest(httpClient, requestOperations);
        logger.debug("-- zoneRetrieveRequest() response: {}", response);

        return response.stream()
                .map(CloudKit.ResponseOperation::getZoneRetrieveResponse)
                .collect(Collectors.toList());
    }

    public List<CloudKit.RecordRetrieveResponse>
            recordRetrieveRequest(HttpClient httpClient, String zone, String... recordNames)
            throws IOException {
        return recordRetrieveRequest(httpClient, zone, Arrays.asList(recordNames));
    }

    public List<CloudKit.RecordRetrieveResponse>
            recordRetrieveRequest(HttpClient httpClient, String zone, Collection<String> recordNames)
            throws IOException {

        // M211
        List<CloudKit.RequestOperation> requestOperations = factory.recordRetrieveRequestOperations(zone, recordNames);
        List<CloudKit.ResponseOperation> response = doRequest(httpClient, requestOperations);

        return response.stream()
                .map(CloudKit.ResponseOperation::getRecordRetrieveResponse)
                .collect(Collectors.toList());
    }

    List<CloudKit.ResponseOperation>
            doRequest(HttpClient httpClient, List<CloudKit.RequestOperation> operations) throws IOException {

        if (operations.isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<CloudKit.ResponseOperation> responses = new ArrayList<>();
        for (int i = 0; i < operations.size(); i += REQUEST_LIMIT) {
            int fromIndex = i * REQUEST_LIMIT;
            int toIndex = fromIndex + REQUEST_LIMIT;
            toIndex = toIndex > operations.size()
                    ? operations.size()
                    : toIndex;

            List<CloudKit.RequestOperation> subList = operations.subList(fromIndex, toIndex);
            List<CloudKit.ResponseOperation> subListResponses = doSubListRequest(httpClient, subList);
            responses.addAll(subListResponses);
        }
        return responses;
    }

    List<CloudKit.ResponseOperation>
            doSubListRequest(HttpClient httpClient, List<CloudKit.RequestOperation> operationsSubList)
            throws IOException {

        if (operationsSubList.size() > REQUEST_LIMIT) {
            throw new IllegalArgumentException("sub list size over request limit: " + operationsSubList.size());
        }

        HttpUriRequest uriRequest = ProtoBufsRequestFactory.instance().newRequest(
                baseUrl + "/record/retrieve",
                container,
                bundle,
                cloudKitUserId,
                cloudKitToken,
                UUID.randomUUID().toString(),
                operationsSubList);

        return httpClient.execute(uriRequest, responseHandler);
    }
}
