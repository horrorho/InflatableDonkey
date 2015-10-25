/* 
 * The MIT License
 *
 * Copyright 2015 Ahseya.
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
package com.github.horrorho.inflatabledonkey.requests;

import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import net.jcip.annotations.Immutable;

/**
 * M201Request factory.
 *
 * @author Ahseya
 */
@Immutable
public class M201RequestFactory {

    public static M201RequestFactory instance() {
        return instance;
    }

    private static final M201RequestFactory instance = new M201RequestFactory();

    private M201RequestFactory() {
    }

    public CloudKit.Request newRequest(
            String container,
            String bundle,
            String operation,
            String uuid,
            CloudKit.Identifier zoneName,
            CloudKit.Identifier userID,
            CloudKit.RequestOperationHeader requestOperationHeaderProto) {

        CloudKit.RequestOperationHeader requestOperationHeader
                = CloudKit.RequestOperationHeader.newBuilder(requestOperationHeaderProto)
                .setApplicationContainer(container)
                .setApplicationBundle(bundle)
                .setOperation(operation)
                .build();

        CloudKit.Message message = CloudKit.Message.newBuilder()
                .setUuid(uuid)
                .setType(201)
                .setF4(1)
                .build();

        CloudKit.RecordZoneIdentifier recordZoneID = CloudKit.RecordZoneIdentifier.newBuilder()
                .setValue(zoneName)
                .setOwnerIdentifier(userID)
                .build();

        CloudKit.M201Request m201Request = CloudKit.M201Request.newBuilder()
                .setRecordZoneID(recordZoneID)
                .build();

        return CloudKit.Request.newBuilder()
                .setRequestOperationHeader(requestOperationHeader)
                .setMessage(message)
                .setM201Request(m201Request)
                .build();
    }
}
