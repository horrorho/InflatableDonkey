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
package com.github.horrorho.inflatabledonkey.cloud.accounts;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSString;
import com.github.horrorho.inflatabledonkey.util.PListsLegacy;
import java.util.Objects;
import javax.annotation.concurrent.Immutable;

/**
 * AccountInfo.
 *
 * @author Ahseya
 */
@Immutable
public final class AccountInfo {

    private final String appleId;
    private final String aDsID;
    private final String dsPrsID;
    private final String lastName;
    private final String firstName;

    public AccountInfo(String appleId, String aDsID, String dsPrsID, String lastName, String firstName) {
        this.appleId = Objects.requireNonNull(appleId);
        this.aDsID = Objects.requireNonNull(aDsID);
        this.dsPrsID = Objects.requireNonNull(dsPrsID);
        this.lastName = Objects.requireNonNull(lastName);
        this.firstName = Objects.requireNonNull(firstName);
    }

    public AccountInfo(NSDictionary accountInfo) {
        appleId = PListsLegacy.getAs(accountInfo, "appleId", NSString.class).getContent();
        aDsID = PListsLegacy.getAs(accountInfo, "aDsID", NSString.class).getContent();
        dsPrsID = PListsLegacy.getAs(accountInfo, "dsPrsID", NSString.class).getContent();
        lastName = PListsLegacy.getAs(accountInfo, "lastName", NSString.class).getContent();
        firstName = PListsLegacy.getAs(accountInfo, "firstName", NSString.class).getContent();
    }

    public String appleId() {
        return appleId;
    }

    public String aDsID() {
        return aDsID;
    }

    public String dsPrsID() {
        return dsPrsID;
    }

    public String lastName() {
        return lastName;
    }

    public String firstName() {
        return firstName;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.appleId);
        hash = 71 * hash + Objects.hashCode(this.aDsID);
        hash = 71 * hash + Objects.hashCode(this.dsPrsID);
        hash = 71 * hash + Objects.hashCode(this.lastName);
        hash = 71 * hash + Objects.hashCode(this.firstName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AccountInfo other = (AccountInfo) obj;
        if (!Objects.equals(this.appleId, other.appleId)) {
            return false;
        }
        if (!Objects.equals(this.aDsID, other.aDsID)) {
            return false;
        }
        if (!Objects.equals(this.dsPrsID, other.dsPrsID)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AccountInfo{"
                + "appleId=" + appleId
                + ", aDsID=" + aDsID
                + ", dsPrsID=" + dsPrsID
                + ", lastName=" + lastName
                + ", firstName=" + firstName
                + '}';
    }
}
