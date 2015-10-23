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
import java.util.List;
import net.jcip.annotations.Immutable;

/**
 * M201Request factory.
 *
 * @author Ahseya
 */
@Immutable
public class M220RequestFactory {

    public static M220RequestFactory instance() {
        return instance;
    }

    private static final M220RequestFactory instance = new M220RequestFactory();

    private M220RequestFactory() {
    }

    public CloudKit.Request newRequest( 
            String container,
            String bundle,
            String operation,
            String uuid,
            CloudKit.Name subField,
            CloudKit.Name field,
            CloudKit.Names columns,
            CloudKit.String recordName,
            CloudKit.String zoneName,
            CloudKit.String userID,
            CloudKit.Info coreInfo) {

        CloudKit.Info info = CloudKit.Info.newBuilder(coreInfo)
                .setContainer(container)
                .setBundle(bundle)
                .setOperation(operation)
                .build();

        CloudKit.Message message = CloudKit.Message.newBuilder()
                .setUuid(uuid)
                .setType(220)
                .setF4(1)
                .build();

        CloudKit.RecordZoneID recordZoneID = CloudKit.RecordZoneID.newBuilder()
                .setZoneName(zoneName)
                .setOwnerName(userID)
                .build();

        CloudKit.RecordID recordID = CloudKit.RecordID.newBuilder()
                .setRecordName(recordName)
                .setZoneID(recordZoneID)
                .build();

        CloudKit.XRecordID xRecordID = CloudKit.XRecordID.newBuilder()
                .setRecordID(recordID)
                .build();

        CloudKit.UInt32 one = CloudKit.UInt32.newBuilder()
                .setValue(1)
                .build();

        CloudKit.Data data = CloudKit.Data.newBuilder()
                .setId(5)
                .setXRecordID(xRecordID)
                .build();

        CloudKit.Container ckContainer = CloudKit.Container.newBuilder()
                .setName(field)
                .setData(data)
                .setF4(1)
                .build();

        CloudKit.NameContainer nameContainer = CloudKit.NameContainer.newBuilder()
                .setName(subField)
                .setContainer(ckContainer)
                .build();

        CloudKit.M220Request m220Request = CloudKit.M220Request.newBuilder()
                .setNameContainer(nameContainer)
                .setFields(columns)
                .setF6(one)
                .build();

        return CloudKit.Request.newBuilder()
                .setInfo(info)
                .setMessage(message)
                .setM220Request(m220Request)
                .build();
    }
}
