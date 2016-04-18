/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.data.blob;

import java.nio.ByteBuffer;
import net.jcip.annotations.Immutable;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.github.horrorho.inflatabledonkey.data.blob.BlobUtils.align;

/**
 * Unspecified format.
 *
 * @author Ahseya
 */
@Immutable
public final class BlobA6 extends BlobBase {

    private static final Logger logger = LoggerFactory.getLogger(BlobA6.class);

    private final int x;
    private final byte[] tag = new byte[0x10];
    private final BlobLists list;

    public BlobA6(ByteBuffer blob) {
        super(blob);

        if (type() != 0x000000A6) {
            // Presumed type 0x000000A6, consider throwing exception.
            logger.warn("** RespA6() - unexpected type: 0x{}", Integer.toHexString(type()));
        }

        x = blob.getInt();
        blob.get(tag);
        align(blob);

        list = new BlobLists(blob);
        if (list.size() < 3) {
            throw new IllegalArgumentException("too few blob fields: " + list.size());
        }
    }

    public byte[] tag() {
        return Arrays.copyOf(tag, tag.length);
    }

    public byte[] m2() {
        return list.get(0);
    }

    public byte[] iv() {
        return list.get(1);
    }

    public byte[] data() {
        return list.get(2);
    }

    @Override
    public String toString() {
        return "BlobA6{"
                + "type=0x" + Integer.toHexString(type())
                + ",length=0x" + Integer.toHexString(length())
                + ", x=" + x
                + ", tag=0x" + Hex.encodeHexString(tag)
                + ", m2=0x" + Hex.encodeHexString(m2())
                + ", iv=0x" + Hex.encodeHexString(iv())
                + ", data=0x" + Hex.encodeHexString(data())
                + '}';
    }
}
