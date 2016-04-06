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

/**
 * Service.
 *
 * @author Ahseya
 */
public enum Service {
    NULL(0),            // Unspecified.
    MASTER(1),          // MasterKey
    BLADERUNNER(2),     // iCloudDrive
    HYPERION(3),        // Photos
    LIVERPOOL(4),       // CloudKit
    IMESSAGE(5),        // iMessage
    FDE(6),             // FDE
    PIANOMOVER(7),      // Maildrop
    LUMPYMATTRESS(8),   // * placeholder *
    FURIOUSPOTATO(9),   // * placeholder *
    BROKENCOCONUT(10),  // * placeholder *
    DRIEDWATER(11),     // * placeholder *
    BURNTCOOKIE(12),    // * placeholder *
    WOBBLYQUARK(13),    // * placeholder *
    MIDNIGHTBLUE(14),   // * placeholder *
    MISSINGBUTTON(15),  // * placeholder *
    ELECTRICSHEEP(16),  // * placeholder *
    EXMACHINA(17);      // * placeholder *
        
    private final int number;

    private Service(int number) {
        this.number = number;
    }

    public int number() {
        return number;
    }
}
