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
import com.github.horrorho.inflatabledonkey.args.PropertyDP;
import static com.github.horrorho.inflatabledonkey.args.PropertyDP.AUTO;
import static com.github.horrorho.inflatabledonkey.args.PropertyDP.CBC;
import static com.github.horrorho.inflatabledonkey.args.PropertyDP.OFF;
import static com.github.horrorho.inflatabledonkey.args.PropertyDP.XTS;
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
public class XFileKeyInjectorFactory {

    public static UnaryOperator<Optional<XFileKey>> defaults() {
        return DEFAULT;
    }

    public static UnaryOperator<Optional<XFileKey>> injector(PropertyDP mode) {
        switch (mode) {
            case AUTO:
                return UnaryOperator.identity();
            case CBC:
                return injector(DPCipherFactories.AES_CBC);
            case XTS:
                return injector(DPCipherFactories.AES_XTS);
            case OFF:
                return key -> Optional.empty();
            default:
                throw new UnsupportedOperationException("unknown mode: " + mode);
        }
    }

    public static UnaryOperator<Optional<XFileKey>> injector(Supplier<BlockCipher> cipherFactory) {
        return key -> inject(key, Objects.requireNonNull(cipherFactory, "cipherFactory"));
    }

    static Optional<XFileKey> inject(Optional<XFileKey> key, Supplier<BlockCipher> mode) {
        return key.map(u -> new XFileKey(u.key(), mode, u.flags()));
    }

    private static final PropertyDP DEFAULT_MODE
            = Property.DP_MODE.as(PropertyDP::parse).orElse(PropertyDP.AUTO);

    private static final UnaryOperator<Optional<XFileKey>> DEFAULT = XFileKeyInjectorFactory.injector(DEFAULT_MODE);
}
