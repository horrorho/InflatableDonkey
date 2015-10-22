/* 
 * The MIT License
 *
 * Copyright 2015 Ahseya.
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
package com.github.horrorho.inflatabledonkey.requests;

import com.github.horrorho.inflatabledonkey.exception.BadDataException;
import net.jcip.annotations.Immutable;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * iOS backup ckAppInit HttpUriRequest factory.
 *
 * @author Ahseya
 */
@Immutable
public final class CkAppInitBackupRequestFactory {

    public static final CkAppInitBackupRequestFactory create() {
        return instance;
    }

    private static final CkAppInitBackupRequestFactory instance = new CkAppInitBackupRequestFactory();

    public CkAppInitBackupRequestFactory() {
    }

    /**
     * Returns a new iOS backup ckAppInit HttpUriRequest.
     *
     * @param dsPrsID not null
     * @param mmeAuthToken not null
     * @param cloudKitAuthToken not null
     * @return HttpUriRequest, not null
     * @throws BadDataException
     */
    public HttpUriRequest newRequest(
            String dsPrsID,
            String mmeAuthToken,
            String cloudKitAuthToken
    ) throws BadDataException {

        String authorization = Headers.basicToken(dsPrsID, mmeAuthToken);

        HttpPost request = new HttpPost("https://setup.icloud.com/setup/ck/v1/ckAppInit?container=com.apple.backup.ios");

        request.setHeader("Accept", "application/json");
        request.setHeader("User-Agent", "CloudKitWin/1.3.17.0 (Windows/6.1.1.0)");
        request.setHeader("Authorization", authorization);
        request.setHeader("X-CloudKit-AuthToken", cloudKitAuthToken);
        request.setHeader("X-CloudKit-BundleId", "com.apple.backupd");
        request.setHeader("X-CloudKit-ContainerId", "com.apple.backup.ios");
        request.setHeader("X-CloudKit-Environment", "production");
        request.setHeader("X-CloudKit-Partition", "production");
        request.setHeader("X-Mme-Client-Info", "<PC> <Windows; 6.1.7601/SP1.0.7601; Win7 Ultimate; x64> <CloudKitWin/1.3.17.0 (iCloudDrive/4.1.0.0)");
        return request;
    }
}
