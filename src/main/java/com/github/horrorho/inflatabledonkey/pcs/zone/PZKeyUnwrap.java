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
package com.github.horrorho.inflatabledonkey.pcs.zone;

import com.github.horrorho.inflatabledonkey.crypto.RFC3394Wrap;
import com.github.horrorho.inflatabledonkey.crypto.NISTKDF;
import java.util.Optional;
import java.util.function.BiFunction;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PZKeyUnwrap.
 *
 * @author Ahseya
 */
@Immutable
public final class PZKeyUnwrap implements BiFunction<byte[], byte[], Optional<byte[]>> {

    public static PZKeyUnwrap instance() {
        return INSTANCE;
    }

    private static final Logger logger = LoggerFactory.getLogger(PZKeyUnwrap.class);

    // TODO inject via Property
    private static final PZKeyUnwrap INSTANCE
            = new PZKeyUnwrap(Hex.decode("7772617070696E67206B65792077726170206D65"), 0x10);

    private final byte[] label;
    private final int keyLength;

    public PZKeyUnwrap(byte[] label, int keyLength) {
        this.label = Arrays.copyOf(label, label.length);
        this.keyLength = keyLength;
    }

    @Override
    public Optional<byte[]> apply(byte[] keyDerivationKey, byte[] wrappedKey) {
        byte[] ctrHMac = NISTKDF.ctrHMac(keyDerivationKey, label, SHA256Digest::new, keyLength);
        logger.debug("-- apply() - ctrHMac: 0x{}", Hex.toHexString(ctrHMac));

        Optional<byte[]> key = RFC3394Wrap.unwrapAES(ctrHMac, wrappedKey);
        logger.debug("-- apply() - key: 0x:{}", key.map(Hex::toHexString).orElse("NULL"));

        return key;
    }
}
