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
package com.github.horrorho.inflatabledonkey.data.der;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import net.jcip.annotations.Immutable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Key set.
 *
 * @author Ahseya
 */
@Immutable
public final class KeySet extends ASN1Object {

    /*
        Template:

        SEQUENCE  (6)
         UTF8STRING             String name
         SET  (1)               Set<PrivateKey> privateKeys
         SET  (1)               Set<TypeData> serviceKeyIDs
         OCTET_STRING OPTIONAL  byte[] checksum
         INTEGER optional       int flags
         SignatureInfo optional SignatureInfo signatureInfo
     */
    public static final int APPLICATION_TAG = 2;

    private static final Logger logger = LoggerFactory.getLogger(KeySet.class);

    private static final Supplier<Digest> DIGEST = SHA256Digest::new;

    private final String name;
    private final Set<PrivateKey> keys;
    private final Set<TypeData> serviceKeyIDs;
    private final byte[] checksum;
    private final Optional<Integer> flags;    // bit 0 -> 1 = compact
    private final Optional<SignatureInfo> signatureInfo;

    KeySet(
            String name,
            Set<PrivateKey> keys,
            Set<TypeData> services,
            byte[] checksum,
            Optional<Integer> flags,
            Optional<SignatureInfo> signatureInfo) {

        this.name = Objects.requireNonNull(name, "name");
        this.keys = new HashSet<>(keys);
        this.serviceKeyIDs = new HashSet<>(services);
        this.checksum = Arrays.copyOf(checksum, checksum.length);
        this.flags = Objects.requireNonNull(flags, "flags");
        this.signatureInfo = Objects.requireNonNull(signatureInfo, "signatureInfo");
    }

    public KeySet(
            String name,
            Set<PrivateKey> keys,
            Set<TypeData> services,
            Optional<Integer> flags,
            Optional<SignatureInfo> signatureInfo) {

        this.name = Objects.requireNonNull(name, "name");
        this.keys = new HashSet<>(keys);
        this.serviceKeyIDs = new HashSet<>(services);
        this.flags = Objects.requireNonNull(flags, "flags");
        this.signatureInfo = Objects.requireNonNull(signatureInfo, "signatureInfo");
        this.checksum = calculateChecksum();
    }

    public KeySet(ASN1Primitive primitive) {

        ASN1Primitive app = DER.asApplicationSpecific(APPLICATION_TAG, primitive);
        DERIterator i = DER.asSequence(app);

        name = DER.as(DERUTF8String.class, i)
                .getString();

        keys = DER.asSet(i, PrivateKey::new);

        serviceKeyIDs = DER.asSet(i, TypeData::new);

        Optional<byte[]> optionalChecksum = i.nextIf(DEROctetString.class)
                .map(ASN1OctetString::getOctets);

        flags = i.nextIf(ASN1Integer.class)
                .map(ASN1Integer::getValue)
                .map(BigInteger::intValue);

        signatureInfo = i.optional()
                .map(SignatureInfo::new);

        checksum = calculateChecksum();

        Optional<Boolean> match = optionalChecksum.map(c -> Arrays.equals(c, checksum));

        if (match.isPresent()) {
            if (match.get()) {
                logger.debug("** KeySet() - checksums match");

            } else {
                logger.warn("** KeySet()  - bad checksum OR code failure");
            }
        }
    }

    byte[] calculateChecksum() {
        try {
            // Re-encode the data minus the supplied checksum then calculate SHA256 hash.
            // This should match the supplied checksum.
            // Verifies data integrity AND our decode/ encode processes.

            byte[] contents = toASN1Primitive(false).getEncoded();

            Digest digest = DIGEST.get();
            byte[] calculatedChecksum = new byte[digest.getDigestSize()];
            digest.update(contents, 0, contents.length);
            digest.doFinal(calculatedChecksum, 0);

            return calculatedChecksum;

        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public String name() {
        return name;
    }

    public Set<PrivateKey> keys() {
        return new HashSet<>(keys);
    }

    public Set<TypeData> serviceKeyIDs() {
        return new HashSet<>(serviceKeyIDs);
    }

    public byte[] checksum() {
        return Arrays.copyOf(checksum, checksum.length);
    }

    public Optional<Integer> flags() {
        return flags;
    }

    public Optional<SignatureInfo> signatureInfo() {
        return signatureInfo;
    }

    ASN1Primitive toASN1Primitive(boolean includeChecksum) {

        DEROctetString checksumEncodable = includeChecksum
                ? new DEROctetString(checksum())
                : null;

        ASN1Integer flagsEncodable = flags.map(ASN1Integer::new)
                .orElse(null);

        ASN1EncodableVector vector = DER.vector(
                new DERUTF8String(name),
                DER.toSet(keys),
                DER.toSet(serviceKeyIDs),
                checksumEncodable,
                flagsEncodable,
                signatureInfo.orElse(null));

        DERSequence sequence = new DERSequence(vector);
        return DER.toApplicationSpecific(APPLICATION_TAG, sequence);
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return toASN1Primitive(true);
    }

    @Override
    public String toString() {
        return "KeySet{"
                + "id=" + name
                + ", keys=" + keys
                + ", references=" + serviceKeyIDs
                + ", checksum=" + Hex.toHexString(checksum)
                + ", flags=" + flags
                + ", signatureInfo=" + signatureInfo
                + '}';
    }
}
