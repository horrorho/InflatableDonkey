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
package com.github.horrorho.inflatabledonkey.crypto.key.imports;

import com.github.horrorho.inflatabledonkey.crypto.ec.key.ECPrivateKey;
import com.github.horrorho.inflatabledonkey.crypto.key.Key;
import com.github.horrorho.inflatabledonkey.data.der.PrivateKey;
import com.github.horrorho.inflatabledonkey.data.der.PublicKeyInfo;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.jcip.annotations.Immutable;

/**
 * PrivateKeyImport.
 *
 * @author Ahseya
 */
@Immutable
public final class PrivateKeyImport {

    // TODO rationalize, probably doesn't belong in this package
    public static Function<PrivateKey, Optional<Key<ECPrivateKey>>>
            importPrivateKeyData(
                    Function<byte[], Optional<ECPrivateKey>> keyDataImport,
                    BiFunction<ECPrivateKey, Optional<PublicKeyInfo>, Key<ECPrivateKey>> buildKey) {

        return privateKey -> keyDataImport.apply(privateKey.privateKey())
                .map(keyData -> buildKey.apply(keyData, privateKey.publicKeyInfo()));
    }
}
