/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.pcs.xzone;

import com.github.horrorho.inflatabledonkey.crypto.AESWrap;
import com.github.horrorho.inflatabledonkey.crypto.NISTKDF;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * XFPKeyUnwrap.
 *
 * @author Ahseya
 */
@Immutable
public final class XFPKeyUnwrap {

    private static final Logger logger = LoggerFactory.getLogger(XFPKeyUnwrap.class);

    private static final byte[] LABEL = Hex.decode("7772617070696E67206B65792077726170206D65");
    private static final int DERIVED_KEY_LENGTH = 0x10;

    public static Optional<byte[]> unwrap(byte[] keyDerivationKey, byte[] wrappedKey) {
        byte[] ctrHMac = NISTKDF.ctrHMac(keyDerivationKey, LABEL, SHA256Digest::new, DERIVED_KEY_LENGTH);
        logger.debug("-- unwrap() - ctrHMac: 0x{}", Hex.toHexString(ctrHMac));

        Optional<byte[]> key = AESWrap.unwrap(ctrHMac, wrappedKey);
        logger.debug("-- unwrap() - key: 0x:{}", key.map(Hex::toHexString).orElse("NULL"));

        return key;
    }
}
