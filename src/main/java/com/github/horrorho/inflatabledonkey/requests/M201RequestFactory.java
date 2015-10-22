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
public class M201RequestFactory {

    public static M201RequestFactory instance() {
        return instance;
    }

    private static final M201RequestFactory instance = new M201RequestFactory();

    private M201RequestFactory() {
    }

    public CK.Request newRequest(
            String container,
            String bundle,
            String operation,
            String uuid,
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
                .setType(201)
                .setF4(1)
                .build();

        CK.Op ckOp = CK.Op.newBuilder()
                .setItem(op)
                .setCkUserID(userID)
                .build();

        CK.M201Request m201Request = CK.M201Request.newBuilder()
                .setOp(ckOp)
                .build();

        return CK.Request.newBuilder()
                .setInfo(info)
                .setMessage(message)
                .setM201Request(m201Request)
                .build();
    }
}
