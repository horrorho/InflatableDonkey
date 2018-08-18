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

import com.github.horrorho.inflatabledonkey.exception.BadDataException;
import com.github.horrorho.inflatabledonkey.pcs.zone.ProtectionZone;
import com.github.horrorho.inflatabledonkey.protobuf.CloudKit;
import com.google.protobuf.ByteString;
import java.util.Optional;
import javax.annotation.concurrent.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class KeyBagFactory {

    private static final String KEYBAG_DATA = "keybagData";
    private static final String SECRET = "secret";

    private static final Logger logger = LoggerFactory.getLogger(KeyBagFactory.class);

    public static KeyBag
            from(CloudKit.Record record, ProtectionZone zone)
            throws BadDataException {

        Optional<byte[]> keyBagData = field(record, KEYBAG_DATA, zone);
        if (!keyBagData.isPresent()) {
            throw new BadDataException("KeyBagFactory, no key bag data");
        }

        Optional<byte[]> secret = field(record, SECRET, zone);
        if (!secret.isPresent()) {
            throw new BadDataException("KeyBagFactory, failed to acquire key bag pass code");
        }

        return KeyBagDecoder.decode(keyBagData.get(), secret.get());
    }

    static Optional<byte[]> field(CloudKit.Record record, String label, ProtectionZone zone) {
        return record.getRecordFieldList()
                .stream()
                .filter(u -> u
                    .getIdentifier()
                    .getName()
                    .equals(label))
                .map(u -> u
                    .getValue()
                    .getBytesValue())
                .map(ByteString::toByteArray)
                .map(bs -> zone.decrypt(bs, label))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}
