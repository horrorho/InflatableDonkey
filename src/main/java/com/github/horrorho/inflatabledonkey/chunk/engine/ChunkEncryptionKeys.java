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
package com.github.horrorho.inflatabledonkey.chunk.engine;

import com.github.horrorho.inflatabledonkey.crypto.FC3394Wrap;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ChunkEncryptionKeys.
 *
 * @author Ahseya
 */
@Immutable
public final class ChunkEncryptionKeys {
    // 447E254DA96F1A75C8FB:10007C60
    private static final Logger logger = LoggerFactory.getLogger(ChunkEncryptionKeys.class);
    
    private ChunkEncryptionKeys() {
    }
    
    public static Optional<byte[]> unwrapKey(Optional<byte[]> kek, byte[] keyData) {        
        int keyType = keyType(keyData);        
        switch (keyType) {
            case 1:
                // TODO. No idea if iOS 8 type 1 encryption is in use.
                logger.warn("-- unwrapKey() - chunk 1 decryption not yet implemented");
                return Optional.empty();
            
            case 2:
                return unwrapType2(kek, keyData);
            
            default:
                logger.warn("-- unwrapKey() - unsupported key type: {}", keyType);
                return Optional.empty();
        }
    }
    
    static Optional<byte[]> unwrapType2(Optional<byte[]> kek, byte[] keyData) {
        if (!kek.isPresent()) {
            logger.warn("-- unwrapType2() - kek required, but not present");
            return Optional.empty();
        }
        
        if (keyData.length < 0x19) {
            logger.warn("-- unwrapType2() - key data too short: 0x{}", Hex.toHexString(keyData));
        }
        
        byte[] wrappedKey = Arrays.copyOfRange(keyData, 0x01, 0x19);
        logger.debug("-- key() - wrapped key: 0x{} kek: 0x{}", 
                Hex.toHexString(wrappedKey), kek.map(Hex::toHexString).orElse("NULL"));
        
        Optional<byte[]> unwrappedKey = FC3394Wrap.unwrapAES(kek.get(), wrappedKey);
        logger.debug("-- key() - unwrapped key: 0x{}", unwrappedKey.map(Hex::toHexString).orElse("NULL"));
        
        return unwrappedKey;
    }
    
    public static int keyType(byte[] keyData) {
        if (keyData.length == 0) {
            logger.warn("-- keyType() - empty key data");
            return -1;
        }
        return keyData[0];
    }
}
