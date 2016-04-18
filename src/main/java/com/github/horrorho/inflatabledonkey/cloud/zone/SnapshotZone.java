///*
// * The MIT License
// *
// * Copyright 2016 Ahseya.
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy
// * of this software and associated documentation files (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in
// * all copies or substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// * THE SOFTWARE.
// */
//package com.github.horrorho.inflatabledonkey.cloud.zone;
//
//import com.github.horrorho.inflatabledonkey.cloudkitty.CloudKitty;
//import com.github.horrorho.inflatabledonkey.data.backup.Manifests;
//import com.github.horrorho.inflatabledonkey.data.backup.ManifestsFactory;
//import com.github.horrorho.inflatabledonkey.data.backup.ZoneItem;
//import com.github.horrorho.inflatabledonkey.pcs.xzone.XZone;
//import com.github.horrorho.inflatabledonkey.pcs.xzone.XZones;
//import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
//import java.io.IOException;
//import java.util.List;
//import net.jcip.annotations.Immutable;
//import org.apache.http.client.HttpClient;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// *
// * @author Ahseya
// */
//@Immutable
//public final class SnapshotZone {
//
//    private static final Logger logger = LoggerFactory.getLogger(SnapshotZone.class);
//
//    public static ZoneItem<Manifests> create(HttpClient httpClient, CloudKitty kitty, XZone zone, String snapshot) throws IOException {
//
//        System.out.println("\n\n" + zone);
//
//        List<CloudKit.RecordRetrieveResponse> responses
//                = kitty.recordRetrieveRequest(httpClient, "mbksync", snapshot);
//        logger.debug("-- create() - responses: {}", responses);
//
//        XZones zones = XZones.create(zone);
//        System.out.println("\n\n" + zones);
//
//        List<String> protectionInfoTags = ZoneAssistant.addRecordProtectionInfo211(responses, zones);
//        if (protectionInfoTags.size() != 1) {
//            throw new IllegalArgumentException("bad server response");
//        }
//        XZone newZone = zones.zone(protectionInfoTags.get(0)).get();
//        System.out.println("\n\nnew\n" + newZone);
//
//        if (responses.size() != 1) {
//            throw new IllegalArgumentException("bad server response");
//        }
//        CloudKit.RecordRetrieveResponse response = responses.get(0);
//
//        Manifests manifests = ManifestsFactory.from(response.getRecord(), newZone::decrypt);
//
//        return new ZoneItem<>(newZone, manifests);
//    }
//}
