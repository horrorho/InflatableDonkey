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
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.KeyParameter;

/**
 *
 * @author Ahseya
 */
@NotThreadSafe
public class XTSAESBlockCipher implements BlockCipher {

    private final XTSCore core;
    private final int blockSize;
    private final int dataUnitSize;
    private long dataUnit;
    private int index;

    XTSAESBlockCipher(
            XTSCore core,
            int blockSize,
            int dataUnitSize,
            long dataUnit,
            int index) {

        this.core = Objects.requireNonNull(core, "core");
        this.blockSize = blockSize;
        this.dataUnitSize = dataUnitSize;
        this.dataUnit = dataUnit;
        this.index = index;
    }

    XTSAESBlockCipher(XTSCore core, LongFunction<byte[]> tweakValueFunction, int dataUnitSize) {
        this(new XTSCore(new XTSTweak(tweakValueFunction)), core.getBlockSize(), dataUnitSize, 0, 0);
    }

    public XTSAESBlockCipher(LongFunction<byte[]> tweakValueFunction, int dataUnitSize) {
        this(new XTSCore(), tweakValueFunction, dataUnitSize);
    }

    public XTSAESBlockCipher(int dataUnitSize) {
        this(new XTSCore(), XTSTweak::defaultTweakFunction, dataUnitSize);
    }

    @Override
    public void init(boolean forEncryption, CipherParameters params) throws IllegalArgumentException {
        if (params instanceof KeyParameter) {
            core.init(forEncryption, (KeyParameter) params);
            return;
        }
        throw new IllegalArgumentException("invalid params: " + params.getClass().getName());
    }

    @Override
    public String getAlgorithmName() {
        return core.getAlgorithmName();
    }

    @Override
    public int getBlockSize() {
        return blockSize;
    }

    @Override
    public int processBlock(byte[] in, int inOff, byte[] out, int outOff)
            throws DataLengthException, IllegalStateException {

        if (index == 0) {
            core.reset(dataUnit);
        }

        if ((index += blockSize) == dataUnitSize) {
            dataUnit++;
            index = 0;
        }

        return core.processBlock(in, inOff, out, outOff);
    }

    @Override
    public void reset() {
        index = 0;
        dataUnit = 0;
    }
}
