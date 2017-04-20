/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.cache;

import com.dd.plist.NSData;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSString;
import com.dd.plist.PropertyListFormatException;
import com.dd.plist.PropertyListParser;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import javax.xml.parsers.ParserConfigurationException;
import net.jcip.annotations.NotThreadSafe;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author Ayesha
 */
@NotThreadSafe
public final class InflatableData {

    public static InflatableData generate() {
        UUID deviceID = UUID.randomUUID();
        String deviceHardwareID
                = new BigInteger(256, ThreadLocalRandom.current()).toString(16).toUpperCase(Locale.US);
        return new InflatableData(deviceID, deviceHardwareID);
    }

    public static Optional<InflatableData> from(byte[] bs) {
        InflatableData data;
        try {
            NSDictionary parse = (NSDictionary) PropertyListParser.parse(bs);
            byte[] escrowedKeys = ((NSData) parse.get("escrowedKeys")).bytes();
            UUID deviceUuid = UUID.fromString(((NSString) parse.get("deviceUuid")).getContent());
            String deviceHardWareId = ((NSString) parse.get("deviceHardWareId")).getContent();
            data = new InflatableData(escrowedKeys, deviceUuid, deviceHardWareId);

        } catch (ClassCastException | IllegalArgumentException | IOException | NullPointerException
                | PropertyListFormatException | ParseException | ParserConfigurationException | SAXException ex) {
            logger.warn("-- from() - exception: ", ex);
            data = null;
        }
        return Optional.ofNullable(data);
    }

    private static final Logger logger = LoggerFactory.getLogger(InflatableData.class);

    private byte[] escrowedKeys;
    private UUID deviceUuid;
    private String deviceHardWareId;

    public InflatableData(byte[] escrowedKeys, UUID deviceUuid, String deviceHardWareId) {
        this.escrowedKeys = Arrays.copyOf(escrowedKeys, escrowedKeys.length);
        this.deviceUuid = Objects.requireNonNull(deviceUuid);
        this.deviceHardWareId = Objects.requireNonNull(deviceHardWareId);
    }

    public InflatableData(UUID deviceUuid, String deviceHardWareId) {
        this(new byte[]{}, deviceUuid, deviceHardWareId);
    }

    public byte[] encoded() {
        try {
            NSDictionary dict = new NSDictionary();
            dict.put("escrowedKeys", new NSData(escrowedKeys));
            dict.put("deviceUuid", new NSString(deviceUuid.toString()));
            dict.put("deviceHardWareId", new NSString(deviceHardWareId));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PropertyListParser.saveAsBinary(dict, baos);
            return baos.toByteArray();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public boolean hasEscrowedKeys() {
        return escrowedKeys.length > 0;
    }

    public byte[] escrowedKeys() {
        return Arrays.copyOf(escrowedKeys, escrowedKeys.length);
    }

    public void setEscrowedKeys(byte[] escrowedKeys) {
        this.escrowedKeys = Arrays.copyOf(escrowedKeys, escrowedKeys.length);
    }

    public UUID deviceUuid() {
        return deviceUuid;
    }

    public void setDeviceUuid(UUID deviceUuid) {
        this.deviceUuid = deviceUuid;
    }

    public String deviceHardWareId() {
        return deviceHardWareId;
    }

    public void setDeviceHardWareId(String deviceHardWareId) {
        this.deviceHardWareId = deviceHardWareId;
    }

    @Override
    public String toString() {
        return "InflatableData{"
                + "escrowedKeys=0x" + Hex.toHexString(escrowedKeys)
                + ", deviceUuid=" + deviceUuid
                + ", deviceHardWareId=" + deviceHardWareId
                + '}';
    }
}
