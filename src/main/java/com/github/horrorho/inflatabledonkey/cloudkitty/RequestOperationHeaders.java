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

import com.github.horrorho.inflatabledonkey.protobuf.CloudKit.RequestOperationHeader;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import javax.annotation.concurrent.Immutable;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class RequestOperationHeaders implements Function<String, RequestOperationHeader> {

    public static final RequestOperationHeader requestOperationHeader(UUID deviceID, String deviceHardwareID) {
        return RequestOperationHeader.newBuilder(REQUESTOPERATIONHEADERPROTO)
                .setDeviceIdentifier(CKProto.identifier(deviceID.toString(), 2))
                .setDeviceHardwareID(deviceHardwareID)
                .build();
    }

    static final RequestOperationHeader REQUESTOPERATIONHEADERPROTO
            = RequestOperationHeader.newBuilder()
                    .setF4("4.0.0.0")
                    .setDeviceSoftwareVersion("11.0.1")
                    .setDeviceLibraryName("com.apple.cloudkit.CloudKitDaemon")
                    .setDeviceLibraryVersion("735")
                    .setDeviceFlowControlBudget(40000)
                    .setDeviceFlowControlBudgetCap(40000)
                    .setVersion("4.0")
                    .setF19(1)
                    .setDeviceAssignedName("dB's iPhone")
                    .setF23(1)
                    .setF25(1)
                    .build();

    private final RequestOperationHeader proto;
    private final String container;
    private final String bundle;

    public RequestOperationHeaders(RequestOperationHeader proto, String container, String bundle) {
        this.proto = Objects.requireNonNull(proto, "proto");
        this.container = Objects.requireNonNull(container, "container");
        this.bundle = Objects.requireNonNull(bundle, "bundle");
    }

    public RequestOperationHeaders(String container, String bundle, UUID deviceID, String deviceHardwareID) {
        this(requestOperationHeader(deviceID, deviceHardwareID), container, bundle);
    }

    @Override
    public RequestOperationHeader apply(String operation) {
        return RequestOperationHeader.newBuilder(proto)
                .setApplicationContainer(container)
                .setApplicationBundle(bundle)
                .setOperation(operation)
                .build();
    }

    public String container() {
        return container;
    }

    public String bundle() {
        return bundle;
    }
}
