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
import com.github.horrorho.inflatabledonkey.crypto.srp.EscrowTest;
import com.github.horrorho.inflatabledonkey.crypto.srp.SRPFactory;
import com.github.horrorho.inflatabledonkey.crypto.srp.data.SRPGetRecords;
import com.github.horrorho.inflatabledonkey.crypto.srp.data.SRPGetRecordsMetadata;
import com.github.horrorho.inflatabledonkey.crypto.srp.data.SRPInitResponse;
import com.github.horrorho.inflatabledonkey.cloud.accounts.AccountInfo;
import com.github.horrorho.inflatabledonkey.cloud.accounts.MobileMe;
import com.github.horrorho.inflatabledonkey.cloud.accounts.Token;
import com.github.horrorho.inflatabledonkey.cloud.accounts.Tokens;
import com.github.horrorho.inflatabledonkey.data.blob.BlobA6;
import com.github.horrorho.inflatabledonkey.pcs.service.ServiceKeySet;
import com.github.horrorho.inflatabledonkey.requests.EscrowProxyApi;
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
import org.apache.commons.codec.binary.Hex;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class SRPClientX {

    private static final Logger logger = LoggerFactory.getLogger(SRPClientX.class);

    public static ServiceKeySet get(HttpClient httpClient, MobileMe mobileMe, Tokens tokens, AccountInfo accountInfo, HeadersLegacy coreHeaders) throws IOException, PropertyListFormatException, ParseException, ParserConfigurationException, SAXException {
        /* 
         EscrowService SRP-6a exchange.
        
         Coding critical as repeated errors will lock the account.                 
         https://en.wikipedia.org/wiki/Secure_Remote_Password_protocol
        
         escrow user id = dsid
         escrow password = dsid                
         */
        logger.info("-- main() - *** EscrowService SRP exchange ***");

        Optional<String> optionalEscrowProxyUrl = mobileMe.optional("com.apple.Dataclass.KeychainSync", "escrowProxyUrl");
        if (!optionalEscrowProxyUrl.isPresent()) {
            logger.error("-- main() - no escrowProxyUrl");
            System.exit(-1);
        }

        String escrowProxyUrl = optionalEscrowProxyUrl.get();
        EscrowProxyApi escrowProxy = new EscrowProxyApi(accountInfo.dsPrsID(), escrowProxyUrl, coreHeaders);

        /* 
         EscrowService SRP-6a exchanges: GETRECORDS
        
         We'll do the last step first, so we can abort if only a few attempts remain rather than risk further depleting
         them with experimental code. 
         */
        logger.info("-- main() - *** EscrowService SRP exchange: GETRECORDS ***");

        NSDictionary records = escrowProxy.getRecords(httpClient, tokens.get(Token.MMEAUTHTOKEN));
        logger.debug("-- main() - getRecords response: {}", records.toASCIIPropertyList());

        SRPGetRecords srpGetRecords = new SRPGetRecords(records);

        srpGetRecords.metadata()
                .values()
                .stream()
                .forEach(v -> {
                    byte[] metadata = Base64.getDecoder().decode(v.metadata());
                    try {
                        NSObject nsObject = PropertyListParser.parse(metadata);
                        if (nsObject instanceof NSDictionary) {
                            logger.debug("-- main() - label: {} dictionary: {}",
                                    v.label(), ((NSDictionary) nsObject).toXMLPropertyList());
                        }
                    } catch (IOException | PropertyListFormatException | ParseException | ParserConfigurationException | SAXException ex) {
                        logger.warn("-- main() - failed to parse property list: {}", v.metadata());
                    }
                });

        int remainingAttempts = srpGetRecords.remainingAttempts();
        logger.debug("-- main() - remaining attempts: {}", remainingAttempts);
        if (remainingAttempts < 5) {
            logger.warn("-- main() - remaining attempt threshold, aborting");
            System.exit(-1);
        }

        /* 
         EscrowService SRP-6a exchanges: SRP_INIT
        
         Randomly generated ephemeral key A presented to escrow server along with id (mmeAuthToken).
         Server returns amongst other things a salt and an ephemeral key B.
         */
        logger.info("-- main() - *** EscrowService SRP exchange: SRP_INIT ***");

        com.github.horrorho.inflatabledonkey.crypto.srp.SRPClient srp = SRPFactory.rfc5054(new SecureRandom());
        byte[] ephemeralKeyA = srp.generateClientCredentials();
        NSDictionary srpInit = escrowProxy.srpInit(httpClient, tokens.get(Token.MMEAUTHTOKEN), ephemeralKeyA);

        SRPInitResponse srpInitResponse = new SRPInitResponse(srpInit);

        logger.debug("-- main() - SRP_INIT dictionary: {}", srpInit.toASCIIPropertyList());
        logger.debug("-- main() - SRP_INIT data: {}", srpInitResponse);

        /* 
         EscrowService SRP-6a exchanges: RECOVER
        
         CRITICAL step. Generate M1 verification message. Failure here will deplete attempts (we have 10 attempts max).
         Server will abort on an invalid M1 or present us with, amongst other things, M2 which we can verify (or not).
         */
        logger.info("-- main() - *** EscrowService SRP exchange: RECOVER ***");

        byte[] dsid = srpInitResponse.dsid().getBytes(StandardCharsets.UTF_8);
        logger.debug("-- main() - escrow service id: {}", Hex.encodeHexString(dsid));
        logger.debug("-- main() - escrow service password: {}", Hex.encodeHexString(dsid));

        Optional<byte[]> optionalM1
                = srp.calculateClientEvidenceMessage(srpInitResponse.salt(), dsid, dsid, srpInitResponse.ephemeralKeyB());

        if (!optionalM1.isPresent()) {
            logger.error("-- main() - bad ephemeral key from server: 0x{}",
                    Hex.encodeHex(srpInitResponse.ephemeralKeyB()));
            System.exit(-1);
        }
        byte[] m1 = optionalM1.get();
        logger.debug("-- main() - m1: 0x{}", Hex.encodeHexString(m1));

        NSDictionary recover
                = escrowProxy.recover(httpClient, tokens.get(Token.MMEAUTHTOKEN), m1, srpInitResponse.uid(), srpInitResponse.tag());
        logger.debug("-- main() - srpInit response: {}", recover.toASCIIPropertyList());

        /* 
         EscrowService SRP-6a exchanges: session key
        
         Verify the server M2 message (or not). Either way we have a session key and an encrypted key set.
         */
        logger.info("-- main() - *** EscrowService SRP exchange: session key ***");

        byte[] recoverRespBlob = Base64.getDecoder().decode(PLists.<NSString>fetch(recover, "respBlob").getContent());
        BlobA6 blobA6 = new BlobA6(ByteBuffer.wrap(recoverRespBlob));

        Optional<byte[]> optionalKey = srp.verifyServerEvidenceMessage(blobA6.m2());
        if (!optionalKey.isPresent()) {
            logger.debug("-- main() - failed to verify m2");
        }

        byte[] key = optionalKey.get();
        logger.debug("-- main() - session key: {}", Hex.encodeHexString(key));

        /*
         EscrowService decrypt M2 key set.
         
         BlobA6 contains amongst other things, salt, iv and encrypted key set data.
         */
        Optional<ServiceKeySet> optionalServiceKeySet = EscrowTest.decryptRecoveryResponseBlob(blobA6, key);

        if (!optionalServiceKeySet.isPresent()) {
            logger.warn("-- main() - failed to recover key set");
            System.exit(-1);
        }
        ServiceKeySet keySet = optionalServiceKeySet.get();


        /* 
         EscrowService SRP-6a exchanges: decrypt escrowed keys
        
         Verify the server M2 message (or not). Either way we have a session key and an encrypted key set.
         */
        logger.info("-- main() - *** EscrowService SRP exchange: decrypt escrowed keys ***");

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
