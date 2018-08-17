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
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit.Operation;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit.Record;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit.Record.Reference;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit.RecordIdentifier;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit.RecordZoneIdentifier;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit.RequestOperation;
import java.util.UUID;
import javax.annotation.concurrent.Immutable;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class CKProto {

    public static Record.Type recordType(String name) {
        return Record.Type.newBuilder()
                .setName(name)
                .build();
    }

    public static Reference reference(Reference.Type type, RecordIdentifier recordIdentifier) {
        return Reference.newBuilder()
                .setType(type)
                .setRecordIdentifier(recordIdentifier)
                .build();
    }

    public static RequestOperation requestOperationWithHeader(RequestOperation operation, RequestOperation.Header header) {
        return RequestOperation.newBuilder(operation)
                .setHeader(header)
                .build();
    }

    public static Operation operation(Operation.Type type) {
        return operation(UUID.randomUUID(), type);
    }

    public static Operation operation(UUID uuid, Operation.Type type) {
        return Operation.newBuilder()
                .setOperationUUID(uuid.toString())
                .setType(type)
                .build();
    }

    public static Record.Field.Identifier recordFieldIdentifier(String name) {
        return Record.Field.Identifier.newBuilder()
                .setName(name)
                .build();
    }

    public static RecordIdentifier recordIdentifier(String zone, String recordName, String cloudKitUserId) {
        return recordIdentifier(recordZoneIdentifier(zone, cloudKitUserId), recordName);
    }

    public static RecordIdentifier recordIdentifier(RecordZoneIdentifier recordZoneID, String recordName) {
        return RecordIdentifier.newBuilder()
                .setValue(identifier(recordName, Identifier.Type.RECORD))
                .setZoneIdentifier(recordZoneID)
                .build();
    }

    public static RecordZoneIdentifier recordZoneIdentifier(String zone, String cloudKitUserId) {
        return RecordZoneIdentifier.newBuilder()
                .setValue(identifier(zone, Identifier.Type.RECORD_ZONE))
                .setOwnerIdentifier(identifier(cloudKitUserId, Identifier.Type.USER))
                .build();
    }

    public static Identifier identifier(String name, Identifier.Type type) {
        return Identifier.newBuilder()
                .setName(name)
                .setType(type)
                .build();
    }
}
