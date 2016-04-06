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
package com.github.horrorho.inflatabledonkey.crypto.srp;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;
import com.dd.plist.NSString;
import com.github.horrorho.inflatabledonkey.exception.BadDataException;
import com.github.horrorho.inflatabledonkey.util.PLists;
import java.util.Base64;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import net.jcip.annotations.Immutable;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  *
 * @author Ahseya
 */
@Immutable
public final class SRPInitResponse {

    private static final Logger logger = LoggerFactory.getLogger(SRPInitResponse.class);
    private static final NSString empty = new NSString("");
    private static final NSNumber zero = new NSNumber(0);

    private final int version;
    private final String status;
    private final String message;
    private final String dsid;
    private final byte[] salt;
    private final BigInteger B;
    private final byte[] tag; // Unknown string identifier
    private final byte[] x; // Unknown 256 bit entity

    SRPInitResponse(int version, String status, String message, String dsid, byte[] salt, BigInteger B, byte[] tag, byte[] x) {
        this.version = version;
        this.status = status;
        this.message = message;
        this.dsid = dsid;
        this.salt = salt;
        this.B = B;
        this.tag = tag;
        this.x = x;
    }

    public SRPInitResponse(NSDictionary response) throws BadDataException {
        version = PLists.<NSNumber>getOrDefault(response, "version", zero).intValue();
        status = PLists.<NSString>getOrDefault(response, "status", empty).toString();
        message = PLists.<NSString>getOrDefault(response, "message", empty).toString();

        dsid = PLists.<NSString>get(response, "dsid").toString();
        String respBlob = PLists.<NSString>get(response, "respBlob").toString();

        byte[] data;
        try {
            data = Base64.getDecoder().decode(respBlob);

        } catch (IllegalArgumentException ex) {
            throw new BadDataException(ex);
        }

        logger.trace("-- SRPInitResponse() - respBlob data: 0x{}", Hex.encodeHexString(data));

        // TODO brittle code
        // TODO at a minimum length validation
        // TODO ideally formal format parsing
        // TODO tidy
        // Network byte ordering/ big endian.
        ByteBuffer buffer = ByteBuffer.wrap(data);

        x = Arrays.copyOfRange(data, 0x0C, 0x1C);

        int b1 = buffer.getInt(0x1C); //0
        int tagIndex = b1 + 0x30;
        int tagLength = buffer.getInt(b1 + 0x2C);
        tag = Arrays.copyOfRange(data, tagIndex, tagIndex + tagLength);

        int b2 = buffer.getInt(0x20); //14
        int saltIndex = b2 + 0x30;
        int saltLength = buffer.getInt(b2 + 0x2C);
        salt = Arrays.copyOfRange(data, saltIndex, saltIndex + saltLength);

        int b3 = buffer.getInt(0x24); //14
        int BIndex = b3 + 0x30;
        int BLength = buffer.getInt(b3 + 0x2C);
        byte[] Bdata = Arrays.copyOfRange(data, BIndex, BIndex + BLength);
        B = new BigInteger(1, Bdata);

        logger.trace("-- SRPInitResponse() - salt: 0x{}", Hex.encodeHexString(salt));
        logger.trace("-- SRPInitResponse() - B: 0x{}", B.toString(16));
        logger.trace("-- SRPInitResponse() - tag: 0x{}", Hex.encodeHexString(tag));
        logger.trace("-- SRPInitResponse() - x: 0x{}", Hex.encodeHexString(x));
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

    public byte[] salt() {
        return Arrays.copyOf(salt, salt.length);
    }

    public BigInteger B() {
        return B;
    }

    public byte[] tag() {
        return Arrays.copyOf(tag, tag.length);
    }

    public byte[] x() {
        return Arrays.copyOf(x, x.length);
    }

    @Override
    public String toString() {
        return "SRPInitResponse{"
                + "version=" + version
                + ", status=" + status
                + ", message=" + message
                + ", dsid=" + dsid
                + ", salt=0x" + Hex.encodeHexString(salt)
                + ", B=0x" + B.toString(16)
                + ", tag=" + Hex.encodeHexString(tag)
                + ", x=0x" + Hex.encodeHexString(x)
                + '}';
    }
}
