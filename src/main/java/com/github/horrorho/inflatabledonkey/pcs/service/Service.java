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
package com.github.horrorho.inflatabledonkey.pcs.service;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service.
 *
 * @author Ahseya
 */
public enum Service {
    // Internal non-standard: service is specified but not recognised as an enum
    UNKNOWN(-1),
    // Raw
    RAW(0),
    // MasterKey
    PCS_MASTERKEY(1),
    // iCloudDrive
    BLADERUNNER(2),
    // Photos
    HYPERION(3),
    // CloudKit
    LIVERPOOL(4),
    // Escrow
    PCS_ESCROW(5),
    // FDE
    PCS_FDE(6),
    // Maildrop
    PIANOMOVER(7),
    // Backup
    PCS_BACKUP(8),
    // Notes
    PCS_NOTES(9),
    // iMessage
    PCS_IMESSAGE(10),
    // News
    NEWS(11),
    // Sharing
    SHARING(12),
    // KeyboardServices
    KEYBOARD_SERVICES(13),
    // Activities
    ACTIVITIES(14),
    // Gaming
    GAMING(15),
    // iAD
    IAD(16),
    // BulkMail
    BULK_MAIL(17),
    // BTPairing
    BT_PAIRING(18),
    // BTAnnouncement
    BT_ANNOUNCEMENT(19),
    // TTYCallHistory
    TTY_CALL_HISTORY(20),
    // Continuity
    CONTINUITY(21),
    // Placeholders
    LOBSTER_ROMANCE(22),
    HAPPY_STONES(23),
    HORROR_HO(24),
    RED_SKIES(25),
    BROKEN_PANDA(26),
    FROZEN_MOOSE(27);

    private static final Map<Integer, Service> map = Arrays.asList(Service.values())
            .stream()
            .collect(Collectors.toMap(x -> x.number(), Function.identity()));

    public static Optional<Service> optional(int number) {
        return Optional.ofNullable(map.get(number));
    }

    public static Service service(int number) {
        return optional(number)
                .orElse(UNKNOWN);
    }

    private final int number;

    private Service(int number) {
        this.number = number;
    }

    public int number() {
        return number;
    }
}
