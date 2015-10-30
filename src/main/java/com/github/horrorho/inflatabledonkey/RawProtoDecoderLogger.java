/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey;

import com.github.horrorho.inflatabledonkey.io.IOFunction;
import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import net.jcip.annotations.Immutable;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * protoc --decode_raw logger
 *
 * @author Ahseya
 */
@Immutable
public class RawProtoDecoderLogger implements IOFunction<InputStream, List<CloudKit.ResponseOperation>> {

    private static final Logger logger = LoggerFactory.getLogger(RawProtoDecoderLogger.class);

    private final RawProtoDecoder rawProtoDecoder;

    public RawProtoDecoderLogger(RawProtoDecoder rawProtoDecoder) {
        this.rawProtoDecoder = rawProtoDecoder;
    }

    @Override
    public List<CloudKit.ResponseOperation> apply(InputStream input) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            IOUtils.copy(input, baos);

            if (rawProtoDecoder != null) {
                List<String> rawProtos = rawProtoDecoder.decodeProtos(new ByteArrayInputStream(baos.toByteArray()));
                logger.debug("-- main() - raw decode: {}", rawProtos);

            } else {
                logger.debug("-- main() - raw decode: no protoc decoder specified");
            }

            InputStream copy = new ByteArrayInputStream(baos.toByteArray());
            List<CloudKit.ResponseOperation> responseOperations = new ArrayList<>();
            CloudKit.ResponseOperation responseOperation;
            while ((responseOperation = CloudKit.ResponseOperation.parseDelimitedFrom(copy)) != null) {
                responseOperations.add(responseOperation);
            }

            return responseOperations;

        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
