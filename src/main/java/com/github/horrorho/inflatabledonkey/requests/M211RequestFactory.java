/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
