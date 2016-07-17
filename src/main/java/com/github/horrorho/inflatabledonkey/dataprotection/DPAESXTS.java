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
package com.github.horrorho.inflatabledonkey.dataprotection;

import com.github.horrorho.inflatabledonkey.args.Property;
import com.github.horrorho.inflatabledonkey.crypto.xts.XTSAESBlockCipher;
import net.jcip.annotations.NotThreadSafe;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

/**
 * Data Protection AES XTS mode with modified tweak function.
 *
 * @author Ahseya
 */
@NotThreadSafe
public class DPAESXTS extends XTSAESBlockCipher {

    static byte[] tweakFunction(long tweakValue) {
        byte[] bs = Pack.longToLittleEndian(tweakValue);
        return Arrays.concatenate(bs, bs);
    }
    private static final int BLOCK_SIZE = Property.DP_AESXTS_BLOCK_SIZE.intValue().orElse(4096);

    public DPAESXTS(int blockSize) {
        super(DPAESXTS::tweakValue, blockSize);
    }

    public DPAESXTS() {
        this(BLOCK_SIZE);
    }
}
