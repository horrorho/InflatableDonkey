/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.data;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSString;
import com.github.horrorho.inflatabledonkey.exception.BadDataException;
import com.github.horrorho.inflatabledonkey.util.PLists;
import java.util.Objects;
import net.jcip.annotations.Immutable;

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

    public AccountInfo(NSDictionary accountInfo) throws BadDataException {
        appleId = PLists.<NSString>get(accountInfo, "appleId").toString();
        aDsID = PLists.<NSString>get(accountInfo, "aDsID").toString();
        dsPrsID = PLists.<NSString>get(accountInfo, "dsPrsID").toString();
        lastName = PLists.<NSString>get(accountInfo, "lastName").toString();
        firstName = PLists.<NSString>get(accountInfo, "firstName").toString();
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
