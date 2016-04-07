/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.crypto.srp.data;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;
import com.dd.plist.NSString;
import com.github.horrorho.inflatabledonkey.exception.BadDataException;
import com.github.horrorho.inflatabledonkey.util.PLists;
import java.util.Objects;
import net.jcip.annotations.Immutable;

/**
 * SRPGetRecordsMetadata.
 *
 * @author Ahseya
 */
@Immutable
public final class SRPGetRecordsMetadata {

    private static final NSString empty = new NSString("");
    private static final NSString zeroString = new NSString("0");
    private static final NSNumber zero = new NSNumber(0);

    private final boolean silentAttemptAllowed;
    private final String recordStatus;
    private final int remainingAttempts;
    private final String label;
    private final String metadata;

    SRPGetRecordsMetadata(
            boolean silentAttemptAllowed,
            String recordStatus,
            int remainingAttempts,
            String label,
            String metadata) {

        this.silentAttemptAllowed = silentAttemptAllowed;
        this.recordStatus = Objects.requireNonNull(recordStatus, "recordStatus");
        this.remainingAttempts = remainingAttempts;
        this.label = Objects.requireNonNull(label, "label");
        this.metadata = Objects.requireNonNull(metadata, "metadata");
    }

    SRPGetRecordsMetadata(NSDictionary metadata) throws BadDataException {
        silentAttemptAllowed = PLists.<NSNumber>getOrDefault(metadata, "silentAttemptAllowed", zero).boolValue();
        recordStatus = PLists.<NSString>getOrDefault(metadata, "recordStatus", empty).getContent();
        label = PLists.<NSString>getOrDefault(metadata, "label", empty).getContent();

        String remainingAttemptsString
                = PLists.<NSString>getOrDefault(metadata, "remainingAttempts", zeroString).getContent();
        remainingAttempts = Integer.parseInt(remainingAttemptsString);  // TODO cast exception

        this.metadata = PLists.<NSString>getOrDefault(metadata, "metadata", empty).getContent();
    }

    public boolean silentAttemptAllowed() {
        return silentAttemptAllowed;
    }

    public String recordStatus() {
        return recordStatus;
    }

    public int remainingAttempts() {
        return remainingAttempts;
    }

    public String label() {
        return label;
    }

    public String metadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return "SRPGetRecordsMetadata{"
                + "silentAttemptAllowed=" + silentAttemptAllowed
                + ", recordStatus=" + recordStatus
                + ", remainingAttempts=" + remainingAttempts
                + ", label=" + label
                + ", metadata=" + metadata
                + '}';
    }
}
