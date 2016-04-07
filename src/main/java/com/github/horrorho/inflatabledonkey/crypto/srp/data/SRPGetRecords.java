/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.crypto.srp.data;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;
import com.dd.plist.NSObject;
import com.dd.plist.NSString;
import com.github.horrorho.inflatabledonkey.exception.BadDataException;
import com.github.horrorho.inflatabledonkey.util.PLists;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SRPGetRecords.
 *
 * @author Ahseya
 */
@Immutable
public final class SRPGetRecords {

    static Map<String, SRPGetRecordsMetadata> metadata(NSArray metadataList) throws BadDataException {
        // TODO rework once BadDataException removed
        Map<String, SRPGetRecordsMetadata> map = new HashMap<>();

        for (NSObject nSObject : metadataList.getArray()) {

            if (nSObject instanceof NSDictionary) {

                SRPGetRecordsMetadata srpGetRecordsMetadata = new SRPGetRecordsMetadata((NSDictionary) nSObject);
                map.put(srpGetRecordsMetadata.label(), srpGetRecordsMetadata);

            } else {
                logger.warn("-- metadata() - dictionary expected: {}", nSObject);
            }
        }

        return map;
    }

    private static final Logger logger = LoggerFactory.getLogger(SRPGetRecords.class);

    private static final NSString empty = new NSString("");
    private static final NSNumber zero = new NSNumber(0);

    private final int version;
    private final String status;
    private final String message;
    private final int dsid;
    private final Map<String, SRPGetRecordsMetadata> metadata;

    SRPGetRecords(
            int version,
            String status,
            String message,
            int dsid,
            Map<String, SRPGetRecordsMetadata> metadata) {

        this.version = version;
        this.status = Objects.requireNonNull(status, "status");
        this.message = Objects.requireNonNull(message, "message");
        this.dsid = Objects.requireNonNull(dsid, "dsid");
        this.metadata = Objects.requireNonNull(metadata, "metadata");
    }

    public SRPGetRecords(NSDictionary response) throws BadDataException {
        version = PLists.<NSNumber>getOrDefault(response, "version", zero).intValue();
        status = PLists.<NSString>getOrDefault(response, "status", empty).getContent();
        message = PLists.<NSString>getOrDefault(response, "message", empty).getContent();
        dsid = PLists.<NSNumber>getOrDefault(response, "dsid", zero).intValue();

        NSArray metadataList = PLists.<NSArray>get(response, "metadataList");
        this.metadata = metadata(metadataList);
    }

    public int version() {
        return version;
    }

    public String status() {
        return status;
    }

    public String message() {
        return message;
    }

    public int dsid() {
        return dsid;
    }

    public Optional<SRPGetRecordsMetadata> metadata(String label) {
        return Optional.ofNullable(metadata.get(label));
    }

    public Map<String, SRPGetRecordsMetadata> metadata() {
        return new HashMap<>(metadata);
    }

    public int remainingAttempts() {
        return metadata.values()
                .stream()
                .mapToInt(SRPGetRecordsMetadata::remainingAttempts)
                .min()
                .orElse(0);
    }

    @Override
    public String toString() {
        return "SRPGetRecords{"
                + "version=" + version
                + ", status=" + status
                + ", message=" + message
                + ", dsid=" + dsid
                + ", metadata=" + metadata
                + '}';
    }
}
