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
package com.github.horrorho.inflatabledonkey.requests;

import com.github.horrorho.inflatabledonkey.protocol.CK;
import net.jcip.annotations.Immutable;

/**
 * M201Request factory.
 *
 * @author Ahseya
 */
@Immutable
public class M211RequestFactory {

    public static M211RequestFactory instance() {
        return instance;
    }

    private static final M211RequestFactory instance = new M211RequestFactory();

    private M211RequestFactory() {
    }

    public CK.Request newRequest(
            String container,
            String bundle,
            String operation,
            String uuid,
            CK.Item item,
            CK.Item op,
            CK.Item userID,
            CK.Info coreInfo) {

        CK.Info info = CK.Info.newBuilder(coreInfo)
                .setContainer(container)
                .setBundle(bundle)
                .setOperation(operation)
                .build();

        CK.Message message = CK.Message.newBuilder()
                .setUuid(uuid)
                .setType(211)
                .setF4(1)
                .build();

        CK.Op ckOp = CK.Op.newBuilder()
                .setItem(op)
                .setCkUserID(userID)
                .build();

        CK.ItemOp itemOp = CK.ItemOp.newBuilder()
                .setItem(item)
                .setOp(ckOp)
                .build();

        CK.UInt32 one = CK.UInt32.newBuilder()
                .setValue(1)
                .build();

        CK.M211Request m211Request = CK.M211Request.newBuilder()
                .setItemOp(itemOp)
                .setF6(one)
                .build();

        return CK.Request.newBuilder()
                .setInfo(info)
                .setMessage(message)
                .setM211Request(m211Request)
                .build();
    }
}
