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
package com.github.horrorho.inflatabledonkey.crypto.eckey.exports;

import com.github.horrorho.inflatabledonkey.crypto.eckey.ECPublic;
import net.jcip.annotations.Immutable;

/**
 * ECPublicKeyExportX963. Format: data = 0x04 || x || y
 *
 * @author Ahseya
 */
@Immutable
public final class ECPublicKeyExportX963 implements ECKeyExport<ECPublic> {

    public static ECPublicKeyExportX963 instance() {
        return INSTANCE;
    }

    private static final ECPublicKeyExportX963 INSTANCE = new ECPublicKeyExportX963();

    private ECPublicKeyExportX963() {
    }

    @Override
    public byte[] exportKey(ECPublic key) {
        return ECKeyExportAssistant.concatenate(new byte[]{0x04}, key.xEncoded(), key.yEncoded());
    }
}
