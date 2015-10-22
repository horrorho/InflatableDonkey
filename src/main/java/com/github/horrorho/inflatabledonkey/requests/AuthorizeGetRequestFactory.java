///*
// * The MIT License
// *
// * Copyright 2015 Ahseya.
// *
// * Permission is hereby granted, free from charge, to any person obtaining a copy
// * from this software and associated documentation list (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies from the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in
// * all copies or substantial portions from the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// * THE SOFTWARE.
// */
//package com.github.horrorho.inflatabledonkey.requests;
//
//import com.github.horrorho.inflatabledonkey.protocol.ICloud;
//import net.jcip.annotations.Immutable;
//import org.apache.commons.codec.binary.Hex;
//import org.apache.http.client.methods.HttpUriRequest;
//import org.apache.http.client.methods.RequestBuilder;
//import org.apache.http.entity.ByteArrayEntity;
//
///**
// * AuthorizeGetRequestFactory HttpUriRequest factory.
// *
// * @author Ahseya
// */
//// TODO convert to newer format
//@Immutable
//public final class AuthorizeGetRequestFactory {
//
//    public static final AuthorizeGetRequestFactory defaultInstance() {
//        return instance;
//    }
//
//    private static final AuthorizeGetRequestFactory instance = new AuthorizeGetRequestFactory();
//
//    AuthorizeGetRequestFactory() {
//    }
//
//    /**
//     * Returns a new ICloud.MBSFileAuthToken HttpUriRequest.
//     *
//     * @param dsPrsID not null
//     * @param contentUrl not null
//     * @param authTokens not null
//     * @return HttpUriRequest, null if empty auth tokens
//     */
//    public HttpUriRequest newRequest(
//            String dsPrsID,
//            String contentUrl,
//            ICloud.MBSFileAuthTokens authTokens) {
//
//        if (authTokens.getTokensCount() == 0) {
//            return null;
//        }
//
//        String mmcsAuthToken
//                = Hex.encodeHexString(authTokens.getTokens(0).getFileID().toByteArray())
//                + " "
//                + authTokens.getTokens(0).getAuthToken();
//
//        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(authTokens.toByteArray());
//
//        String uri = contentUrl + "/" + dsPrsID + "/authorizeGet";
//
//        HttpUriRequest request = RequestBuilder.post(uri)
//                .addHeader("x-apple-mmcs-auth", mmcsAuthToken)
//                .addHeader("User-Agent", "ApplePhotoStreamsDownloader.exe (unknown version) CFNetwork/520.20.9")
//                .addHeader("Content-Type", "application/vnd.com.apple.me.ubchunk+protobuf")
//                .addHeader("Accept", "application/vnd.com.apple.me.ubchunk+protobuf")
//                .addHeader("x-apple-mmcs-proto-version", "4.0")
//                .addHeader("x-mme-client-info", "<PC> <Windows;6.1(1.0);7601> <com.apple.icloud.content/unknown (com.apple.mediastreams.windows/7.5)")
//                .addHeader("x-apple-mmcs-dataclass", "com.apple.Dataclass.MediaStream")
//                .addHeader("x-apple-mme-dsid", dsPrsID)
//                .setEntity(byteArrayEntity)
//                .build();
//        return request;
//    }
//}
