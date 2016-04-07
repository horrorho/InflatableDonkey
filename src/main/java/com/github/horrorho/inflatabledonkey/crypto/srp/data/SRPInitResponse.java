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
package com.github.horrorho.inflatabledonkey.crypto.srp.data;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;
import com.dd.plist.NSString;
import com.github.horrorho.inflatabledonkey.data.blob.BlobA4;
import com.github.horrorho.inflatabledonkey.exception.BadDataException;
import com.github.horrorho.inflatabledonkey.util.PLists;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Objects;
import net.jcip.annotations.Immutable;

/**
 * SRPInitResponse.
 *
 * @author Ahseya
 */
@Immutable
public final class SRPInitResponse {

    private static final NSString empty = new NSString("");
    private static final NSNumber zero = new NSNumber(0);

    private final int version;
    private final String status;
    private final String message;
    private final String dsid;
    private final BlobA4 blob;

    SRPInitResponse(
            int version,
            String status,
            String message,
            String dsid,
            BlobA4 blob) {

        this.version = version;
        this.status = Objects.requireNonNull(status, "status");
        this.message = Objects.requireNonNull(message, "message");
        this.dsid = Objects.requireNonNull(dsid, "dsid");
        this.blob = Objects.requireNonNull(blob, "blob");
    }

    public SRPInitResponse(NSDictionary response) throws BadDataException {
        version = PLists.<NSNumber>getOrDefault(response, "version", zero).intValue();
        status = PLists.<NSString>getOrDefault(response, "status", empty).getContent();
        message = PLists.<NSString>getOrDefault(response, "message", empty).getContent();
        dsid = PLists.<NSString>get(response, "dsid").toString();

        String respBlob = PLists.<NSString>get(response, "respBlob").getContent();

        byte[] data = Base64.getDecoder().decode(respBlob);

        ByteBuffer buffer = ByteBuffer.wrap(data);
        blob = new BlobA4(buffer);
    }

    public int version() {
        return version;
    }

    public String status() {
        return status;
    }

    public String message() {
        return message;
    }

    public String dsid() {
        return dsid;
    }

    public byte[] uid() {
        return blob.uid();
    }

    public byte[] salt() {
        return blob.salt();
    }

    public byte[] ephemeralKeyB() {
        return blob.ephemeralKey();
    }

    public byte[] tag() {
        return blob.tag();
    }

    @Override
    public String toString() {
        return "SRPInitResponse{"
                + "version=" + version
                + ", status=" + status
                + ", message=" + message
                + ", dsid=" + dsid
                + ", blob=" + blob
                + '}';
    }
}
