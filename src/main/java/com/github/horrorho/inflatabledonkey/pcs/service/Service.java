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
    UNKNOWN(-1),
    RAW(0), // Raw
    PCS_MASTERKEY(1), // MasterKey
    BLADERUNNER(2), // iCloudDrive
    HYPERION(3), // Photos
    LIVERPOOL(4), // CloudKit
    PCS_ESCROW(5), // Escrow
    PCS_FDE(6), // FDE
    PIANOMOVER(7), // Maildrop
    PCS_BACKUP(8), // Backup
    PCS_NOTES(9), // Notes
    PCS_IMESSAGE(10), // iMessage
    FELDSPAR(11), // News.
    BURNTCOOKIE(12), // * placeholder *
    WOBBLYJELLY(13), // * placeholder *
    MIDNIGHTBLUE(14), // * placeholder *
    DRIEDWATER(15), // * placeholder *
    FURIOUSPOTATO(16), // * placeholder *
    LUMPMATTRESS(17);   // * placeholder *

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
