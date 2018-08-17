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

import com.github.horrorho.inflatabledonkey.protobuf.CloudKit.Identifier;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit.RequestOperation;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import javax.annotation.concurrent.Immutable;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class RequestOperationHeaders implements Function<String, RequestOperation.Header> {

    public static final RequestOperation.Header requestOperationHeader(UUID deviceID, String deviceHardwareID) {
        return RequestOperation.Header.newBuilder(REQUESTOPERATIONHEADERPROTO)
                .setDeviceIdentifier(CKProto.identifier(deviceID.toString(), Identifier.Type.DEVICE))
                .setDeviceHardwareID(deviceHardwareID)
                .build();
    }

    static final RequestOperation.Header REQUESTOPERATIONHEADERPROTO
            = RequestOperation.Header.newBuilder()
                    .setApplicationVersion("4.0.0.0")
                    .setDeviceSoftwareVersion("9.3.5")
                    .setDeviceLibraryName("com.apple.cloudkit.CloudKitDaemon")
                    .setDeviceLibraryVersion("482.30")
                    .setDeviceFlowControlBudget(0)
                    .setDeviceFlowControlBudgetCap(0)
                    .setMmcsProtocolVersion("4.0")
                    .setApplicationContainerEnvironment(RequestOperation.Header.ContainerEnvironment.PRODUCTION)
                    .setDeviceAssignedName("Apple")
                    .setTargetDatabase(RequestOperation.Header.Database.PRIVATE_DB)
                    .setIsolationLevel(RequestOperation.Header.IsolationLevel.ZONE)
                    .build();

    private final RequestOperation.Header proto;
    private final String container;
    private final String bundle;

    public RequestOperationHeaders(RequestOperation.Header proto, String container, String bundle) {
        this.proto = Objects.requireNonNull(proto, "proto");
        this.container = Objects.requireNonNull(container, "container");
        this.bundle = Objects.requireNonNull(bundle, "bundle");
    }

    public RequestOperationHeaders(String container, String bundle, UUID deviceID, String deviceHardwareID) {
        this(requestOperationHeader(deviceID, deviceHardwareID), container, bundle);
    }

    @Override
    public RequestOperation.Header apply(String key) {
        return RequestOperation.Header.newBuilder(proto)
                .setApplicationContainer(container)
                .setApplicationBundle(bundle)
                .setDeviceFlowControlKey(key)
                .build();
    }

    public String container() {
        return container;
    }

    public String bundle() {
        return bundle;
    }
}
