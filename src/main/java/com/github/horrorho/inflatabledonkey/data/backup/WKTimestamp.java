/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.data.backup;

import java.time.Instant;
import net.jcip.annotations.Immutable;

/**
 * WKTimestamp. Cocoa/ Webkit timestamp utility.
 *
 * @author Ahseya
 */
@Immutable
public final class WKTimestamp {

    // 01 Jan 2001 00:00:00 GMT Cocoa/ Webkit reference date 
    // 01 Jan 1970 00:00:00 GMT Epoch.
    private static final long WK = 978307200000L;

    public static Instant toInstant(double timestamp) {
        long unixTimestamp = (long) timestamp * 1000L + WK;
        return Instant.ofEpochMilli(unixTimestamp);
    }
}
