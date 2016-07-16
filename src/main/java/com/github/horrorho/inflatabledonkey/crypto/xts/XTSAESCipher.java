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
package com.github.horrorho.inflatabledonkey.crypto.xts;

import java.util.Objects;
import java.util.function.LongFunction;
import net.jcip.annotations.NotThreadSafe;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.KeyParameter;

/**
 *
 * @author Ahseya
 */
@NotThreadSafe
public class XTSAESCipher {

    private final XTSCore core;
    private final int blockSize;

    XTSAESCipher(XTSCore core) {
        this.core = Objects.requireNonNull(core, "core");
        blockSize = core.getBlockSize();
    }

    public XTSAESCipher(LongFunction<byte[]> tweakFunction) {
        this(new XTSCore(new XTSTweak(tweakFunction)));
    }

    public XTSAESCipher() {
        this(new XTSCore());
    }

    String getAlgorithmName() {
        return core.getAlgorithmName();
    }

    int getBlockSize() {
        return blockSize;
    }

    public XTSAESCipher init(boolean forEncryption, KeyParameter key) throws IllegalArgumentException {
        core.init(forEncryption, key);
        return this;
    }

    public XTSAESCipher init(boolean forEncryption, KeyParameter key1, KeyParameter key2)
            throws IllegalArgumentException {

        core.init(forEncryption, key1, key2);
        return this;
    }

    public int processDataUnit(byte[] in, int inOff, int length, byte[] out, int outOff, long sequenceNumber)
            throws DataLengthException, IllegalStateException {

        core.reset(sequenceNumber);
        return process(in, inOff, length, out, outOff);
    }

    int process(byte[] in, int inOff, int length, byte[] out, int outOff)
            throws DataLengthException, IllegalStateException {

        if (length < blockSize) {
            throw new DataLengthException("data unit size too small: " + length);
        }

        if (inOff + length > in.length) {
            throw new DataLengthException("input buffer too small for data unit size: " + length);
        }

        if (outOff + length > out.length) {
            throw new DataLengthException("output buffer too small for data unit size: " + length);
        }

        int to = length % blockSize == 0
                ? length
                : length - (blockSize * 2);

        int i;
        for (i = 0; i < to; i += blockSize) {
            core.processBlock(in, inOff + i, out, outOff + i);
        }

        if (length > i) {
            core.processPartial(in, inOff + i, out, outOff + i, length - i);
        }
        return length;
    }
}
