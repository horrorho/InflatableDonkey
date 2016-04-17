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
package com.github.horrorho.inflatabledonkey.cloud.srp;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSString;
import com.dd.plist.PropertyListFormatException;
import com.github.horrorho.inflatabledonkey.args.Property;
import com.github.horrorho.inflatabledonkey.crypto.srp.SRPFactory;
import com.github.horrorho.inflatabledonkey.crypto.srp.data.SRPGetRecords;
import com.github.horrorho.inflatabledonkey.crypto.srp.data.SRPGetRecordsMetadata;
import com.github.horrorho.inflatabledonkey.crypto.srp.data.SRPInitResponse;
import com.github.horrorho.inflatabledonkey.cloud.accounts.AccountInfo;
import com.github.horrorho.inflatabledonkey.cloud.accounts.MobileMe;
import com.github.horrorho.inflatabledonkey.cloud.accounts.Token;
import com.github.horrorho.inflatabledonkey.cloud.accounts.Tokens;
import com.github.horrorho.inflatabledonkey.crypto.srp.SRPClient;
import com.github.horrorho.inflatabledonkey.data.blob.BlobA6;
import com.github.horrorho.inflatabledonkey.data.der.DERUtils;
import com.github.horrorho.inflatabledonkey.data.der.KeySet;
import com.github.horrorho.inflatabledonkey.pcs.service.ServiceKeySet;
import com.github.horrorho.inflatabledonkey.pcs.service.ServiceKeySetBuilder;
import com.github.horrorho.inflatabledonkey.requests.EscrowProxyRequestFactory;
import com.github.horrorho.inflatabledonkey.responsehandler.PropertyListResponseHandler;
import com.github.horrorho.inflatabledonkey.util.PLists;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Base64;
import java.util.Optional;
import javax.xml.parsers.ParserConfigurationException;
import net.jcip.annotations.Immutable;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * SRPx. EscrowService SRP-6a exchange.
 *
 * @author Ahseya
 */
@Immutable
public final class SRPx {

    private static final Logger logger = LoggerFactory.getLogger(SRPx.class);

    private static final PropertyListResponseHandler<NSDictionary> RESPONSE_HANDLER
            = PropertyListResponseHandler.nsDictionaryResponseHandler();

    private static final int MIN_REMAINING_ATTEMPTS = Property.SRP_MIN_REMAINING_ATTEMPTS.intValue().orElse(3);

    public static ServiceKeySet srp(HttpClient httpClient, MobileMe mobileMe, Tokens tokens, AccountInfo accountInfo) throws IOException, PropertyListFormatException, ParseException, ParserConfigurationException, SAXException {
        String escrowProxyUrl = mobileMe.get("com.apple.Dataclass.KeychainSync", "escrowProxyUrl");
        EscrowProxyRequestFactory escrowProxy
                = new EscrowProxyRequestFactory(accountInfo.dsPrsID(), tokens.get(Token.MMEAUTHTOKEN), escrowProxyUrl);

//        return srp(httpClient, escrowProxy);
        return srp(httpClient, escrowProxy);
    }

    static ServiceKeySet srp(HttpClient httpClient, EscrowProxyRequestFactory escrowProxy) throws IOException {
        SRPGetRecords records = getRecords(httpClient, escrowProxy);
        // We'll do the last step first, so we can abort if only a few attempts remain rather than risk further 
        // depleting them.
        int remainingAttempts = records.remainingAttempts();
        logger.debug("-- getRecords() - remaining attempts: {}", remainingAttempts);
        if (remainingAttempts < MIN_REMAINING_ATTEMPTS) {
            throw new IllegalStateException("srp minimum remaining attempts threshold exceeded: " + remainingAttempts);
        };

        SRPClient srp = SRPFactory.rfc5054(new SecureRandom());
        SRPInitResponse srpInit = srpInit(httpClient, escrowProxy, srp);

        NSDictionary recover = recover(httpClient, escrowProxy, srp, srpInit);
        BlobA6 blob = blob(recover);

        byte[] key = verify(srp, blob);

        ServiceKeySet decryptM2 = decryptM2(blob, key);

        ServiceKeySet b = escrowedRecord(records, decryptM2);

        return b;
    }

    static SRPGetRecords getRecords(HttpClient httpClient, EscrowProxyRequestFactory factory) throws IOException {
        /* 
        EscrowService SRP-6a exchanges: GETRECORDS
         */
        HttpUriRequest recordsRequest = factory.getRecords();
        NSDictionary records = httpClient.execute(recordsRequest, RESPONSE_HANDLER);
        logger.debug("-- getRecords() - GETRECORDS response: {}", records.toASCIIPropertyList());

        return new SRPGetRecords(records);
    }

    static SRPInitResponse
            srpInit(HttpClient httpClient, EscrowProxyRequestFactory factory, SRPClient srp) throws IOException {
        /* 
         EscrowService SRP-6a exchanges: SRP_INIT
        
         Randomly generated ephemeral key A presented to escrow server along with id (mmeAuthToken).
         Server returns amongst other things a salt and an ephemeral key B.
         */
        byte[] ephemeralKeyA = srp.generateClientCredentials();

        HttpUriRequest srpInitRequest = factory.srpInit(ephemeralKeyA);
        NSDictionary srpInit = httpClient.execute(srpInitRequest, RESPONSE_HANDLER);
        logger.debug("-- srpInit() - SRP_INIT response: {}", srpInit.toASCIIPropertyList());

        return new SRPInitResponse(srpInit);
    }

    static NSDictionary recover(
            HttpClient httpClient,
            EscrowProxyRequestFactory factory,
            SRPClient srp,
            SRPInitResponse response
    ) throws IOException {
        /* 
         EscrowService SRP-6a exchanges: RECOVER
        
         CRITICAL step. Generate M1 verification message. Failure here will deplete attempts (we have 10 attempts max).
         Server will abort on an invalid M1 or present us with, amongst other things, M2 which we can verify (or not).
         */
        byte[] dsid = response.dsid().getBytes(StandardCharsets.UTF_8);

        byte[] m1 = srp.calculateClientEvidenceMessage(response.salt(), dsid, dsid, response.ephemeralKeyB())
                .orElseThrow(() -> new IllegalStateException("bad SRP ephemeral key received from server"));
        logger.debug("-- recover() - m1: 0x{}", Hex.toHexString(m1));

        HttpUriRequest recoverRequest = factory.recover(m1, response.uid(), response.tag());
        NSDictionary recover = httpClient.execute(recoverRequest, RESPONSE_HANDLER);
        logger.debug("-- recover() - srpInit response: {}", recover.toASCIIPropertyList());

        return recover;
    }

    static byte[] verify(SRPClient srp, BlobA6 blob) {
        byte[] key = srp.verifyServerEvidenceMessage(blob.m2())
                .orElseThrow(() -> new IllegalStateException("failed to verify SRP m2"));
        logger.debug("-- verify() - session key: {}", Hex.toHexString(key));
        return key;
    }

    static BlobA6 blob(NSDictionary recover) {
        byte[] recoverRespBlob = Base64.getDecoder()
                .decode(PLists.getAs(recover, "respBlob", NSString.class).getContent());
        return new BlobA6(ByteBuffer.wrap(recoverRespBlob));
    }

    static ServiceKeySet decryptM2(BlobA6 blob, byte[] key) {
        /*
            EscrowService decrypt M2 key set.
            
            BlobA6 contains amongst other things, salt, iv and encrypted key set data.
         */
        byte[] backupBagPassword = EscrowedData.decryptLegacy(blob, key);

        Optional<ServiceKeySet> optionalServiceKeySet = DERUtils.parse(backupBagPassword, KeySet::new)
                .flatMap(ServiceKeySetBuilder::build);

        if (!optionalServiceKeySet.isPresent()) {
            throw new IllegalStateException("SRP failed to recover key set");
        }
        ServiceKeySet keySet = optionalServiceKeySet.get();
        return keySet;
    }

    static ServiceKeySet escrowedRecord(SRPGetRecords records, ServiceKeySet keySet) {
        /* 
         EscrowService SRP-6a exchanges: decrypt escrowed keys
        
         Verify the server M2 message (or not). Either way we have a session key and an encrypted key set.
         */
        Optional<byte[]> pcsRecordMetadata = records.metadata("com.apple.protectedcloudstorage.record")
                .map(SRPGetRecordsMetadata::metadata)
                .map(s -> Base64.getDecoder().decode(s));

        if (!pcsRecordMetadata.isPresent()) {
            logger.warn("-- main() - unable to locate 'com.apple.protectedcloudstorage.record' metadata");
            System.exit(-1);
        }

        Optional<ServiceKeySet> optionalEscrowServiceKeySet
                = EscrowedRecords.decrypt(pcsRecordMetadata.get(), keySet::key);
        if (!optionalEscrowServiceKeySet.isPresent()) {
            logger.warn("-- main() - failed to retrieve escrow key set");
            System.exit(-1);
        }
        ServiceKeySet escrowServiceKeySet = optionalEscrowServiceKeySet.get();
        return escrowServiceKeySet;
    }

}
// TODO tidy
