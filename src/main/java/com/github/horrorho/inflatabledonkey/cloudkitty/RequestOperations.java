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
import com.github.horrorho.inflatabledonkey.data.CKInit;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import com.github.horrorho.inflatabledonkey.requests.Headers;
import com.github.horrorho.inflatabledonkey.requests.ProtoBufsRequestFactory;
import com.github.horrorho.inflatabledonkey.responsehandler.InputStreamResponseHandler;
import java.io.IOException;
import java.math.BigInteger;
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
 * RequestOperations.
 *
 * @author Ahseya
 */
@Immutable
public final class RequestOperations {

    public static RequestOperations cloudKit(CKInit ckInit, String cloudKitToken, Headers coreHeaders) {

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

        return new RequestOperations(
                factory,
                container,
                bundle,
                cloudKitUserId,
                cloudKitToken,
                baseUrl,
                coreHeaders,
                responseHandler);
    }

    private static final Logger logger = LoggerFactory.getLogger(RequestOperations.class);

    private final RequestOperationFactory factory;
    private final String container;
    private final String bundle;
    private final String cloudKitUserId;
    private final String cloudKitToken;
    private final String baseUrl;
    private final Headers coreHeaders;
    private final InputStreamResponseHandler<List<CloudKit.ResponseOperation>> responseHandler;

    public RequestOperations(
            RequestOperationFactory factory,
            String container,
            String bundle,
            String cloudKitUserId,
            String cloudKitToken,
            String baseUrl,
            Headers coreHeaders,
            InputStreamResponseHandler<List<CloudKit.ResponseOperation>> responseHandler) {

        this.factory = Objects.requireNonNull(factory, "factory");
        this.container = Objects.requireNonNull(container, "container");
        this.bundle = Objects.requireNonNull(bundle, "bundle");
        this.cloudKitUserId = Objects.requireNonNull(cloudKitUserId, "cloudKitUserId");
        this.cloudKitToken = Objects.requireNonNull(cloudKitToken, "cloudKitToken");
        this.baseUrl = Objects.requireNonNull(baseUrl, "baseUrl");
        this.coreHeaders = Objects.requireNonNull(coreHeaders, "coreHeaders");
        this.responseHandler = Objects.requireNonNull(responseHandler, "responseHandler");
    }

    public List<CloudKit.ZoneRetrieveResponse>
            zoneRetrieveRequest(HttpClient httpClient, String zone)
            throws IOException {

        // M201
        CloudKit.RequestOperation requestOperation = factory.zoneRetrieveRequestOperation(zone);
        logger.debug("-- zoneRetrieveRequest() request operation: {}", requestOperation);

        HttpUriRequest uriRequest = ProtoBufsRequestFactory.defaultInstance().newRequest(
                baseUrl + "/zone/retrieve",
                container,
                bundle,
                cloudKitUserId,
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

    public List<CloudKit.RecordRetrieveResponse>
            recordRetrieveRequest(HttpClient httpClient, String zone, String... recordNames)
            throws IOException {

        return recordRetrieveRequest(httpClient, zone, Arrays.asList(recordNames));
    }

    public List<CloudKit.RecordRetrieveResponse>
            recordRetrieveRequest(HttpClient httpClient, String zone, Collection<String> recordNames)
            throws IOException {

        // M211
        List<CloudKit.RequestOperation> recordRetrieveRequestOperations
                = factory.recordRetrieveRequestOperations(zone, recordNames);
        logger.debug("-- recordRetrieveRequest() - request operation: {}", recordRetrieveRequestOperations);

        HttpUriRequest uriRequest = ProtoBufsRequestFactory.defaultInstance().newRequest(
                baseUrl + "/record/retrieve",
                container,
                bundle,
                cloudKitUserId,
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
}
// TODO rework coreHeaders to inject into ProtoBufsRequestFactory.
