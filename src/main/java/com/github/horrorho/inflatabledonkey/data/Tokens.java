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
 * Tokens.
 *
 * @author Ahseya
 */
@Immutable
public final class Tokens {

    private final String mmeAuthToken;
    private final String cloudKitToken;

    public Tokens(String mmeAuthToken, String cloudKitToken) {
        this.mmeAuthToken = Objects.requireNonNull(mmeAuthToken);
        this.cloudKitToken = Objects.requireNonNull(cloudKitToken);
    }

    public Tokens(NSDictionary tokens) throws BadDataException {
        mmeAuthToken = PLists.<NSString>get(tokens, "mmeAuthToken").toString();
        cloudKitToken = PLists.<NSString>get(tokens, "cloudKitToken").toString();
    }

    public String mmeAuthToken() {
        return mmeAuthToken;
    }

    public String cloudKitToken() {
        return cloudKitToken;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.mmeAuthToken);
        hash = 67 * hash + Objects.hashCode(this.cloudKitToken);
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
        final Tokens other = (Tokens) obj;
        if (!Objects.equals(this.mmeAuthToken, other.mmeAuthToken)) {
            return false;
        }
        if (!Objects.equals(this.cloudKitToken, other.cloudKitToken)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tokens{" + "mmeAuthToken=" + mmeAuthToken + ", cloudKitToken=" + cloudKitToken + '}';
    }
}
