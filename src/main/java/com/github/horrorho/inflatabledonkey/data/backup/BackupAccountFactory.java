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
package com.github.horrorho.inflatabledonkey.data.backup;

import com.github.horrorho.inflatabledonkey.pcs.zone.ProtectionZone;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit;
import com.google.protobuf.ByteString;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class BackupAccountFactory {

    private static final String HMAC_KEY = "HMACKey";
    private static final String DEVICES = "devices";

    public static BackupAccount from(CloudKit.Record record, ProtectionZone zone) {
        List<CloudKit.RecordField> records = record.getRecordFieldList();
        Optional<byte[]> hmacKey = hmacKey(records)
                .flatMap(u -> zone.decrypt(u, HMAC_KEY));
        Collection<DeviceID> devices = devices(records);
        return new BackupAccount(record, hmacKey, devices);
    }

    public static Optional<byte[]> hmacKey(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(u -> u
                        .getIdentifier()
                        .getName()
                        .equals(HMAC_KEY))
                .map(u -> u
                        .getValue()
                        .getBytesValue())
                .map(ByteString::toByteArray)
                .findFirst();
    }

    public static Collection<DeviceID> devices(List<CloudKit.RecordField> records) {
        return records.stream()
                .filter(u -> u
                        .getIdentifier()
                        .getName()
                        .equals(DEVICES))
                .map(u -> u
                        .getValue()
                        .getRecordFieldValueList())
                .flatMap(Collection::stream)
                .map(u -> u
                        .getReferenceValue()
                        .getRecordIdentifier()
                        .getValue()
                        .getName())
                .map(DeviceID::from)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
