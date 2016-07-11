/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.pcs.zone;

import com.github.horrorho.inflatabledonkey.crypto.FC3394Wrap;
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

        Optional<byte[]> key = FC3394Wrap.unwrapAES(ctrHMac, wrappedKey);
        logger.debug("-- apply() - key: 0x:{}", key.map(Hex::toHexString).orElse("NULL"));

        return key;
    }
}
