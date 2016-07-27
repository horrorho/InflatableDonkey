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
package com.github.horrorho.inflatabledonkey.chunk.engine;

import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import org.bouncycastle.util.encoders.Hex;

/**
 *
 * @author Ahseya
 */
public class ChunkListDecrypterTestVector {

    // Randomly generated vectors.
    // 512 bytes
    public static final ChunkListDecrypterTestVector VECTOR_1
            = new ChunkListDecrypterTestVector(
                    "Vector 1",
                    "023092c9f9ab92a9841efca0fb431a0ce73d7be5722e1a21c2",
                    "f65aa507381b30f40df155f972fe450e",
                    "01a81db3319113dc2158167fa9f1eb8db45cd6593d",
                    "IvItjbaXZ5+AE+WDTllJh/ZSAj3zo+MpdK16f+su36FdHP5DneYRhrCo9IGgzh5q"
                    + "iN5gR3crZyKVcB2E2pucW5jmlEFvE8EsraQe38qIwuyxIJ+NGLEEgFLLYmwC9i5Z"
                    + "47z2n1M7QwSNrUx3qeKIhK52+wKhTR2C07n6SYs/pGk52rGUttYKCPbH8G2CL181"
                    + "RKSapSWu2qES5vw9aMEdddIYO8FB2z4E/BMema/jWKLOKEr4oLDfCIOhjREn/han"
                    + "Itv+qFgMeOJ/Dk2Kqh6ArGtBlJnnC/BCWnixoxjmxK/l2HbiDvt5L2+y1efOEc6d"
                    + "2/Ch23LG3HUTA0Bx2HFQki1H+mBDG3eoq0E/62/MuoURxWY/4/C9xc6omghHqe1Z"
                    + "Ukb2wSVkZQAgVnM4pTaRRfNFPMeAoynzV+0GsvosEx2gfnjMJl4dWOU3fQiWIrmW"
                    + "ofXjl7mwmTXsYCOS+3hDJYakWzcVjERNDEMwlBp1bjEi/qh95kAUX/8A9ZlHszp+"
                    + "2ZyLFRd84joXZZUTBPPOD58OtyX95vBRB4ZFKkABYp1Ua92/o956M3JOC+AmaMwZ"
                    + "RJDWVYc/qrZT7Or0JMv4ySJLq9SBb337eDW49lybI6Jkpg73atiFTNOtnykBVXaY"
                    + "QgUYMoxd4REyAD5ar63eFwRrdbM1jLHKytFwH3boNAE=",
                    "D6jCKhS+mRLtLdP48I2rv5QNXaxlIU3dqxhLkwCdmqXFSrSNMAj/XuWwqk1f8WbW"
                    + "OWqQL0m5JH93TiuWrhyEoSXbRMdkGWF9d17bVuAAW+AUsrBRV+XSNYbyBtSDluUj"
                    + "krC66Xc9A5X8MN9EmHGrkEzn3gMpMWUQcsQYLZjoMST7/A5H9m56O6TmK9EpxROj"
                    + "a/1/oD0A9/9p62pU00HMKyWbgJqeIbhJsEfuANYKHQmwBQylaTbAK2aKGJ+CddPi"
                    + "uzAXx3Fif01ockZVbVuL/X+pExJ59vRpLVPnGOldAuC3O2LEkbhml5K+yh2cFSjx"
                    + "9TwGSfU7tx+++ICNib+UyAJFrbhwQ66ucsAilQJAyS3QcuGOpOyY8LWj4Qi5AJar"
                    + "3CaVEc04fA/wsYW0Uc8rU+DMPlej60u6qtOPEnusKj7+UjUupzr7ujLqTpCKqLQo"
                    + "XYJ7GMHR29uSRTYo4G6buC38DsEMC0oBvAX98b2Zndh1sYhf/DcTRRpE+2mjj51F"
                    + "mX1A5vnthnAB6l4g5celxHmAd/tBLNQfPJ2sXaJLDyKpyJ2NEV5/O5Yvrnp2yrte"
                    + "d6JkaayoI2oVEIMvn5H89SN/5vXG//ULyblT+pZJW/wyKuEc39xaghlC5R5MyKTa"
                    + "fSFy8BycKE2ytTAmEMvIPUfuy0wY/IU5mwDG69f0uOw=");

//    private static final ChunkListDecrypterTestVector VECTOR_1
//            = new ChunkListDecrypterTestVector(
//                    "Vector 1",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "");
    // chunkEncryptionKey,   kek, plaintext, ciphertext, checksum
 
    private final String id;
    private final byte[] chunkEncryptionKey;
    private final byte[] kek;
    private final byte[] checksum;
    private final byte[] plaintext;
    private final byte[] ciphertext;

    public ChunkListDecrypterTestVector(
            String id, String chunkEncryptionKey, String kek, String checksum, String plaintext, String ciphertext) {
        this.id = Objects.requireNonNull(id);
        this.chunkEncryptionKey = Hex.decode(chunkEncryptionKey);
        this.kek = Hex.decode(kek);
        this.checksum = Hex.decode(checksum);
        this.plaintext = Base64.getDecoder().decode(plaintext);
        this.ciphertext = Base64.getDecoder().decode(ciphertext);
    }

    public String id() {
        return id;
    }

    public byte[] chunkEncryptionKey() {
        return Arrays.copyOf(chunkEncryptionKey, chunkEncryptionKey.length);
    }

    public byte[] kek() {
        return Arrays.copyOf(kek, kek.length);
    }

    public byte[] chunkChecksum() {
        return Arrays.copyOf(checksum, checksum.length);
    }

    public byte[] plaintext() {
        return Arrays.copyOf(plaintext, plaintext.length);
    }

    public byte[] ciphertext() {
        return Arrays.copyOf(ciphertext, ciphertext.length);
    }
}
