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
