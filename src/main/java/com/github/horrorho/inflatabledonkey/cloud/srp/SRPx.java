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
import com.dd.plist.NSObject;
import com.dd.plist.NSString;
import com.dd.plist.PropertyListFormatException;
import com.dd.plist.PropertyListParser;
import com.github.horrorho.inflatabledonkey.args.Property;
import com.github.horrorho.inflatabledonkey.crypto.srp.EscrowTest;
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
import com.github.horrorho.inflatabledonkey.pcs.service.ServiceKeySet;
import com.github.horrorho.inflatabledonkey.requests.EscrowProxyRequestAPI;
import com.github.horrorho.inflatabledonkey.requests.HeadersLegacy;
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

    private static final int MIN_REMAINING_ATTEMPTS = Property.SRP_MIN_REMAINING_ATTEMPTS.intValue().orElse(3);

    public static ServiceKeySet srp(HttpClient httpClient, MobileMe mobileMe, Tokens tokens, AccountInfo accountInfo) throws IOException, PropertyListFormatException, ParseException, ParserConfigurationException, SAXException {
        String escrowProxyUrl = mobileMe.get("com.apple.Dataclass.KeychainSync", "escrowProxyUrl");
        EscrowProxyRequestAPI escrowProxy
                = new EscrowProxyRequestAPI(accountInfo.dsPrsID(), tokens.get(Token.MMEAUTHTOKEN), escrowProxyUrl);

        return srp(httpClient, escrowProxy);
    }

    static ServiceKeySet srp(HttpClient httpClient, EscrowProxyRequestAPI escrowProxy) throws IOException, PropertyListFormatException, ParseException, ParserConfigurationException, SAXException {
        /* 
         EscrowService SRP-6a exchange.
        
         Coding critical as repeated errors will lock the account.                 
         https://en.wikipedia.org/wiki/Secure_Remote_Password_protocol
        
         escrow user id = dsid
         escrow password = dsid                
         */
 /* 
         EscrowService SRP-6a exchanges: GETRECORDS
        
         We'll do the last step first, so we can abort if only a few attempts remain rather than risk further depleting
         them with experimental code. 
         */
        NSDictionary records = escrowProxy.getRecords(httpClient);
        logger.debug("-- srp() - getRecords response: {}", records.toASCIIPropertyList());

        SRPGetRecords srpGetRecords = new SRPGetRecords(records);
        int remainingAttempts = srpGetRecords.remainingAttempts();
        logger.debug("-- srp() - remaining attempts: {}", remainingAttempts);
        if (remainingAttempts < MIN_REMAINING_ATTEMPTS) {
            throw new IllegalStateException("srp minimum remaining attempts threshold exceeded: " + remainingAttempts);
        }

        /* 
         EscrowService SRP-6a exchanges: SRP_INIT
        
         Randomly generated ephemeral key A presented to escrow server along with id (mmeAuthToken).
         Server returns amongst other things a salt and an ephemeral key B.
         */
        SRPClient srp = SRPFactory.rfc5054(new SecureRandom());
        byte[] ephemeralKeyA = srp.generateClientCredentials();
        NSDictionary srpInit = escrowProxy.srpInit(httpClient, ephemeralKeyA);
        logger.debug("-- srp() - SRP_INIT dictionary: {}", srpInit.toASCIIPropertyList());

        SRPInitResponse srpInitResponse = new SRPInitResponse(srpInit);
        logger.debug("-- srp() - SRP_INIT data: {}", srpInitResponse);

        /* 
         EscrowService SRP-6a exchanges: RECOVER
        
         CRITICAL step. Generate M1 verification message. Failure here will deplete attempts (we have 10 attempts max).
         Server will abort on an invalid M1 or present us with, amongst other things, M2 which we can verify (or not).
         */
        byte[] dsid = srpInitResponse.dsid().getBytes(StandardCharsets.UTF_8);

        Optional<byte[]> optionalM1
                = srp.calculateClientEvidenceMessage(srpInitResponse.salt(), dsid, dsid, srpInitResponse.ephemeralKeyB());

        if (!optionalM1.isPresent()) {
            throw new IllegalStateException("bad SRP ephemeral key recieved from server: 0x"
                    + Hex.toHexString(srpInitResponse.ephemeralKeyB()));
        }
        byte[] m1 = optionalM1.get();
        logger.debug("-- srp() - m1: 0x{}", Hex.toHexString(m1));

        NSDictionary recover
                = escrowProxy.recover(httpClient, m1, srpInitResponse.uid(), srpInitResponse.tag());
        logger.debug("-- srp() - srpInit response: {}", recover.toASCIIPropertyList());

        // Verify the server M2 message (or not). Either way we have a session key and an encrypted key set.
        byte[] recoverRespBlob = Base64.getDecoder()
                .decode(PLists.getAs(recover, "respBlob", NSString.class).getContent());
        BlobA6 blobA6 = new BlobA6(ByteBuffer.wrap(recoverRespBlob));

        Optional<byte[]> optionalKey = srp.verifyServerEvidenceMessage(blobA6.m2());
        if (!optionalKey.isPresent()) {
            throw new IllegalStateException("failed to verify SRP m2");
        }

        byte[] key = optionalKey.get();
        logger.debug("-- srp() - session key: {}", Hex.toHexString(key));

        /*
         EscrowService decrypt M2 key set.
         
         BlobA6 contains amongst other things, salt, iv and encrypted key set data.
         */
        Optional<ServiceKeySet> optionalServiceKeySet = EscrowTest.decryptRecoveryResponseBlob(blobA6, key);

        if (!optionalServiceKeySet.isPresent()) {
            throw new IllegalStateException("SRP failed to recover key set");
        }
        ServiceKeySet keySet = optionalServiceKeySet.get();

        /* 
         EscrowService SRP-6a exchanges: decrypt escrowed keys
        
         Verify the server M2 message (or not). Either way we have a session key and an encrypted key set.
         */
        Optional<byte[]> pcsRecordMetadata = srpGetRecords.metadata("com.apple.protectedcloudstorage.record")
                .map(SRPGetRecordsMetadata::metadata)
                .map(s -> Base64.getDecoder().decode(s));

        if (!pcsRecordMetadata.isPresent()) {
            logger.warn("-- main() - unable to locate 'com.apple.protectedcloudstorage.record' metadata");
            System.exit(-1);
        }

        Optional<ServiceKeySet> optionalEscrowServiceKeySet
                = EscrowTest.decryptGetRecordsResponseMetadata(pcsRecordMetadata.get(), keySet::key);
        if (!optionalEscrowServiceKeySet.isPresent()) {
            logger.warn("-- main() - failed to retrieve escrow key set");
            System.exit(-1);
        }
        ServiceKeySet escrowServiceKeySet = optionalEscrowServiceKeySet.get();
        return escrowServiceKeySet;
    }
}
// TODO srp records
