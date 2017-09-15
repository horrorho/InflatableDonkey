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

import com.github.horrorho.inflatabledonkey.protobuf.CloudKit.*;
import java.util.UUID;
import javax.annotation.concurrent.Immutable;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class CKProto {

    public static RequestOperation requestOperationWithHeader(RequestOperation operation, RequestOperationHeader header) {
        return RequestOperation.newBuilder(operation)
                .setRequestOperationHeader(header)
                .build();
    }

    public static Operation operation(int type) {
        return operation(UUID.randomUUID(), type);
    }

    public static Operation operation(UUID uuid, int type) {
        return Operation.newBuilder()
                .setUuid(uuid.toString())
                .setType(type)
                .build();
    }

    public static RecordIdentifier recordIdentifier(String zone, String recordName, String cloudKitUserId) {
        return recordIdentifier(recordZoneIdentifier(zone, cloudKitUserId), recordName);
    }

    public static RecordIdentifier recordIdentifier(RecordZoneIdentifier recordZoneID, String recordName) {
        return RecordIdentifier.newBuilder()
                .setValue(identifier(recordName, 1))
                .setZoneIdentifier(recordZoneID)
                .build();
    }

    public static RecordZoneIdentifier recordZoneIdentifier(String zone, String cloudKitUserId) {
        return RecordZoneIdentifier.newBuilder()
                .setValue(identifier(zone, 6))
                .setOwnerIdentifier(identifier(cloudKitUserId, 7))
                .build();
    }

    public static Identifier identifier(String name, int type) {
        return Identifier.newBuilder()
                .setName(name)
                .setType(type)
                .build();
    }
}
