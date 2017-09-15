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
package com.github.horrorho.inflatabledonkey.cloud.escrow;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;
import com.dd.plist.NSString;
import com.github.horrorho.inflatabledonkey.crypto.AESCBC;
import com.github.horrorho.inflatabledonkey.crypto.PBKDF2;
import com.github.horrorho.inflatabledonkey.crypto.srp.SRPClient;
import com.github.horrorho.inflatabledonkey.crypto.srp.SRPFactory;
import com.github.horrorho.inflatabledonkey.data.blob.BlobA0;
import com.github.horrorho.inflatabledonkey.data.blob.BlobA4;
import com.github.horrorho.inflatabledonkey.data.blob.BlobA6;
import com.github.horrorho.inflatabledonkey.requests.EscrowProxyRequestFactory;
import com.github.horrorho.inflatabledonkey.responsehandler.PropertyListResponseHandler;
import com.github.horrorho.inflatabledonkey.util.PListsLegacy;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import javax.annotation.concurrent.Immutable;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EscrowOperationsRecover.
 *
 * @author Ahseya
 */
@Immutable
public final class EscrowOperationsRecover {

    private static final Logger logger = LoggerFactory.getLogger(EscrowOperationsRecover.class);

    private static final PropertyListResponseHandler<NSDictionary> RESPONSE_HANDLER
            = PropertyListResponseHandler.dictionary();

    public static NSDictionary recover(HttpClient httpClient, EscrowProxyRequestFactory requests) throws IOException {
        SRPClient srpClient = SRPFactory.rfc5054(new SecureRandom());

        NSDictionary srpInitResponse = srpInit(httpClient, requests, srpClient);
        NSDictionary recover = recover(httpClient, requests, srpClient, srpInitResponse);
        NSDictionary decrypt = decrypt(srpClient, recover);

        logger.debug("-- recover() - escrowed data: {}", decrypt.toXMLPropertyList());
        return decrypt;
    }

    static NSDictionary recover(
            HttpClient httpClient,
            EscrowProxyRequestFactory requests,
            SRPClient srpClient,
            NSDictionary srpInitResponse) throws IOException {

        validateSrpInitResponse(srpInitResponse);

        String dsid = PListsLegacy.getAs(srpInitResponse, "dsid", NSString.class).getContent();
        String respBlob = PListsLegacy.getAs(srpInitResponse, "respBlob", NSString.class).getContent();

        BlobA4 blob = blobA4(respBlob);

        byte[] m1 = calculateM1(srpClient, blob, dsid);
        byte[] uid = blob.uid();
        byte[] tag = blob.tag();

        return recover(httpClient, requests, uid, tag, m1);
    }

    static NSDictionary
            recover(HttpClient httpClient, EscrowProxyRequestFactory requests, byte[] uid, byte[] tag, byte[] m1)
            throws IOException {
        logger.debug("-- recover() - uid: 0x{} tag: 0x{} m1: 0x{}",
                Hex.toHexString(uid), Hex.toHexString(tag), Hex.toHexString(m1));
        /* 
         SRP-6a RECOVER
        
         Failures will deplete attempts (we have 10 attempts max).
         Server will abort on an invalid M1 or present us with, amongst other things, M2 which we can verify (or not).
         */
        HttpUriRequest recoverRequest = requests.recover(m1, uid, tag);
        NSDictionary response = httpClient.execute(recoverRequest, RESPONSE_HANDLER);
        logger.debug("-- recover() - response: {}", response.toXMLPropertyList());

        return response;
    }

    static NSDictionary
            srpInit(HttpClient httpClient, EscrowProxyRequestFactory requests, SRPClient srpClient) throws IOException {
        /* 
         SRP-6a SRP_INIT
        
         Randomly generated ephemeral key A presented to escrow server along with id (mmeAuthToken header).
         Server returns amongst other things a salt and an ephemeral key B.
         */
        byte[] ephemeralKeyA = srpClient.generateClientCredentials();

        HttpUriRequest srpInitRequest = requests.srpInit(ephemeralKeyA);
        NSDictionary dictionary = httpClient.execute(srpInitRequest, RESPONSE_HANDLER);
        logger.debug("-- srpInit() - SRP_INIT: {}", dictionary.toXMLPropertyList());

        return dictionary;
    }

    static void validateSrpInitResponse(NSDictionary srpInitResponseBlob) {
        Integer version = PListsLegacy.optionalAs(srpInitResponseBlob, "version", NSNumber.class)
                .map(NSNumber::intValue)
                .orElse(null);
        if (!version.equals(1)) {
            throw new UnsupportedOperationException("unknown SRP_INIT version: " + version);
        }
    }

    static byte[] calculateM1(SRPClient srpClient, BlobA4 blob, String dsid) {
        byte[] dsidBytes = dsid.getBytes(StandardCharsets.UTF_8);
        byte[] salt = blob.salt();
        byte[] ephemeralKey = blob.ephemeralKey();

        return calculateM1(srpClient, salt, dsidBytes, ephemeralKey);
    }

    static byte[] calculateM1(SRPClient srpClient, byte[] salt, byte[] dsid, byte[] ephemeralKeyB) {
        return srpClient.calculateClientEvidenceMessage(salt, dsid, dsid, ephemeralKeyB)
                .orElseThrow(() -> new IllegalArgumentException("bad ephemeral key received from server"));
    }

    static NSDictionary decrypt(SRPClient srpClient, NSDictionary recoverResponse) {
        String respBlob = PListsLegacy.getAs(recoverResponse, "respBlob", NSString.class).getContent();
        BlobA6 blob = blobA6(respBlob);

        byte[] key = sessionKey(srpClient, blob);

        return decrypt(blob, key);
    }

    static NSDictionary decrypt(BlobA6 blob, byte[] key) {
        logger.debug("-- decrypt() - response blob: {}", blob);

        byte[] pcsData = AESCBC.decryptAESCBC(key, blob.iv(), blob.data());
        logger.debug("-- decrypt() - pcs data: 0x{}", Hex.toHexString(pcsData));

        BlobA0 pcsBlob = new BlobA0(ByteBuffer.wrap(pcsData));
        logger.debug("-- decrypt() - pcs blob: {}", pcsBlob);

        byte[] derivedKey
                = PBKDF2.generate(new SHA256Digest(), pcsBlob.dsid(), pcsBlob.salt(), pcsBlob.iterations(), 16 * 8);
        logger.debug("-- decrypt() - derived key: 0x{}", Hex.toHexString(derivedKey));

        byte[] saltIV = Arrays.copyOf(pcsBlob.salt(), 0x10);
        logger.debug("-- decrypt() - salt/ iv: 0x{}", Hex.toHexString(saltIV));

        byte[] dictionaryData = AESCBC.decryptAESCBC(derivedKey, saltIV, pcsBlob.data());
        logger.debug("-- decrypt() - dictionary data: 0x{}", Hex.toHexString(dictionaryData));

        NSDictionary dictionary = PListsLegacy.parseDictionary(dictionaryData);
        logger.debug("-- decrypt() - dictionary: {}", dictionary.toXMLPropertyList());
        return dictionary;
    }

    static byte[] sessionKey(SRPClient srp, BlobA6 blob) {
        byte[] key = srp.verifyServerEvidenceMessage(blob.m2())
                .orElseThrow(() -> new IllegalStateException("failed to verify SRP m2"));
        logger.debug("-- sessionKey() - session key: 0x{}", Hex.toHexString(key));
        return key;
    }

    static BlobA4 blobA4(String respBlob) {
        byte[] data = Base64.getDecoder().decode(respBlob);
        ByteBuffer buffer = ByteBuffer.wrap(data);
        return new BlobA4(buffer);
    }

    static BlobA6 blobA6(String respBlob) {
        byte[] data = Base64.getDecoder().decode(respBlob);
        ByteBuffer buffer = ByteBuffer.wrap(data);
        return new BlobA6(buffer);
    }
}
