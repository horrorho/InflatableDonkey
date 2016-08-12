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
package com.github.horrorho.inflatabledonkey.file;

import com.github.horrorho.inflatabledonkey.args.Property;
import com.github.horrorho.inflatabledonkey.args.PropertyDataProtection;
import static com.github.horrorho.inflatabledonkey.args.PropertyDataProtection.AUTO;
import static com.github.horrorho.inflatabledonkey.args.PropertyDataProtection.CBC;
import static com.github.horrorho.inflatabledonkey.args.PropertyDataProtection.OFF;
import static com.github.horrorho.inflatabledonkey.args.PropertyDataProtection.XTS;
import com.github.horrorho.inflatabledonkey.dataprotection.DPCipherFactories;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import org.bouncycastle.crypto.BlockCipher;

/**
 *
 * @author Ahseya
 */
public class XFileKeyMutatorFactory {

    public static UnaryOperator<Optional<XFileKey>> defaults() {
        return DEFAULT;
    }

    public static UnaryOperator<Optional<XFileKey>> create(PropertyDataProtection mode) {
        switch (mode) {
            case AUTO:
                return UnaryOperator.identity();
            case CBC:
                return create(DPCipherFactories.AES_CBC);
            case XTS:
                return create(DPCipherFactories.AES_XTS);
            case OFF:
                return key -> Optional.empty();
            default:
                throw new UnsupportedOperationException("unknown mode: " + mode);
        }
    }

    public static UnaryOperator<Optional<XFileKey>> create(Supplier<BlockCipher> cipherFactory) {
        return key -> mutateCipher(key, Objects.requireNonNull(cipherFactory, "cipherFactory"));
    }

    static Optional<XFileKey> mutateCipher(Optional<XFileKey> key, Supplier<BlockCipher> mode) {
        return key.map(u -> new XFileKey(u.key(), mode, u.flags()));
    }

    private static final PropertyDataProtection DEFAULT_MODE
            = Property.DP_MODE.value().map(PropertyDataProtection::valueOf).orElse(PropertyDataProtection.AUTO);

    private static final UnaryOperator<Optional<XFileKey>> DEFAULT = XFileKeyMutatorFactory.create(DEFAULT_MODE);
}
