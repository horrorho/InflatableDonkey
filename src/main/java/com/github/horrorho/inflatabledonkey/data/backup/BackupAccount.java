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

import com.github.horrorho.inflatabledonkey.protobuf.CloudKit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class BackupAccount extends AbstractRecord {

    private final Optional<byte[]> hmacKey;
    private final Collection<DeviceID> devices;

    public BackupAccount(CloudKit.Record record, Optional<byte[]> hmacKey, Collection<DeviceID> devices) {
        super(record);
        this.hmacKey = hmacKey.map(bs -> Arrays.copyOf(bs, bs.length));
        this.devices = new ArrayList<>(devices);
    }

    public Optional<byte[]> hmacKey() {
        return hmacKey.map(bs -> Arrays.copyOf(bs, bs.length));
    }

    public List<DeviceID> devices() {
        return new ArrayList<>(devices);
    }

    @Override
    public String toString() {
        return "BackupAccount{"
                + ", hmacKey=" + hmacKey.map(Hex::toHexString)
                + ", devices=" + devices
                + '}';
    }
}
