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

import net.jcip.annotations.NotThreadSafe;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.util.encoders.Hex;

/**
 * CKFileSignatureGenerator default type 0x01.
 *
 * @author Ahseya
 */
@NotThreadSafe
public class FileDigestA implements Digest {

    private static final byte[] SALT
            = Hex.decode("636F6D2E6170706C652E58617474724F626A65637453616C7400636F6D2E6170706C652E446174614F626A65637453616C7400");

    private final Digest digest;

    public FileDigestA() {
        digest = new SHA1Digest();
        digest.update(SALT, 0, SALT.length);
    }

    @Override
    public String getAlgorithmName() {
        return "FileDigestA";
    }

    @Override
    public int getDigestSize() {
        return digest.getDigestSize() + 1;
    }

    @Override
    public void update(byte in) {
        digest.update(in);
    }

    @Override
    public void update(byte[] in, int inOff, int len) {
        digest.update(in, inOff, len);
    }

    @Override
    public int doFinal(byte[] out, int outOff) {
        out[0] = 0x01;
        digest.doFinal(out, outOff + 1);
        reset();
        return getDigestSize();
    }

    @Override
    public void reset() {
        digest.reset();
        digest.update(SALT, 0, SALT.length);
    }
}
