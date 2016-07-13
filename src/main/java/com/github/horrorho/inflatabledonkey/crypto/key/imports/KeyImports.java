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
import com.github.horrorho.inflatabledonkey.crypto.ec.key.ECPublicKey;
import com.github.horrorho.inflatabledonkey.crypto.ec.key.imports.ECPrivateKeyImport;
import com.github.horrorho.inflatabledonkey.crypto.ec.key.imports.ECPrivateKeyImportCompact;
import com.github.horrorho.inflatabledonkey.crypto.ec.key.imports.ECPublicKeyImportCompact;
import com.github.horrorho.inflatabledonkey.crypto.ec.key.imports.ECPublicKeyImportX963;
import com.github.horrorho.inflatabledonkey.pcs.key.Key;
import com.github.horrorho.inflatabledonkey.pcs.key.KeyFactories;
import com.github.horrorho.inflatabledonkey.data.der.PrivateKey;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import net.jcip.annotations.Immutable;

/**
 * KeyImports.
 *
 * @author Ahseya
 */
@Immutable
public final class KeyImports {

    public static Function<byte[], Optional<Key<ECPublicKey>>>
            importPublicX963Key(IntFunction<Optional<String>> fieldLengthToCurveName) {

        return PublicKeyImport.importPublicKeyData(
                ECPublicKeyImportX963.instance().importKey(fieldLengthToCurveName),
                KeyFactories.createKeyECPublic().createKey(false));
    }

    public static Function<byte[], Optional<Key<ECPublicKey>>>
            importPublicCompactKey(IntFunction<Optional<String>> fieldLengthToCurveName) {

        return PublicKeyImport.importPublicKeyData(
                ECPublicKeyImportCompact.instance().importKey(fieldLengthToCurveName),
                KeyFactories.createKeyECPublic().createKey(true));
    }

    public static Function<byte[], Optional<Key<ECPublicKey>>>
            importPublicKey(IntFunction<Optional<String>> fieldLengthToCurveName, boolean useCompactKeys) {

        if (useCompactKeys) {
            return exportedKey -> {
                // TODO tidy
                Optional<Key<ECPublicKey>> optional
                        = importPublicCompactKey(fieldLengthToCurveName)
                        .apply(exportedKey);

                if (optional.isPresent()) {
                    return optional;
                }

                return importPublicX963Key(fieldLengthToCurveName)
                        .apply(exportedKey);
            };
        }

        return exportedKey
                -> importPublicX963Key(fieldLengthToCurveName)
                .apply(exportedKey);
    }

    public static Function<PrivateKey, Optional<Key<ECPrivateKey>>>
            importPrivateSECKey(IntFunction<Optional<String>> fieldLengthToCurveName) {

        return PrivateSECKeyImport.importKeyData(
                ECPrivateKeyImport.instance().importKey(fieldLengthToCurveName),
                KeyFactories.createKeyECPrivate().createKey(false));
    }

    public static Function<PrivateKey, Optional<Key<ECPrivateKey>>>
            importCompactPrivateKey(IntFunction<Optional<String>> fieldLengthToCurveName) {

        return PrivateKeyImport.importPrivateKeyData(
                ECPrivateKeyImportCompact.instance().importKey(fieldLengthToCurveName),
                KeyFactories.createKeyECPrivate().createKey(true));
    }

    public static Function<PrivateKey, Optional<Key<ECPrivateKey>>>
            importPrivateKey(IntFunction<Optional<String>> fieldLengthToCurveName, boolean useCompactKeys) {

        if (useCompactKeys) {
            return privateKey -> {

                Optional<Key<ECPrivateKey>> optional
                        = importCompactPrivateKey(fieldLengthToCurveName)
                        .apply(privateKey);

                if (optional.isPresent()) {
                    return optional;
                }

                return importPrivateSECKey(fieldLengthToCurveName)
                        .apply(privateKey);
            };
        }

        return privateKey
                -> importPrivateSECKey(fieldLengthToCurveName)
                .apply(privateKey);
    }
}
