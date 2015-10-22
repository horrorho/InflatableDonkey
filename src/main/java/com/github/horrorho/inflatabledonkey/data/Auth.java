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

import java.util.Objects;
import net.jcip.annotations.Immutable;

/**
 * Auth.
 *
 * @author Ahseya
 */
@Immutable
public final class Auth implements DsPrsID {

    private final String dsPrsID;
    private final String mmeAuthToken;

    public Auth(String dsPrsId, String mmeAuthToken) {
        this.dsPrsID = Objects.requireNonNull(dsPrsId);
        this.mmeAuthToken = Objects.requireNonNull(mmeAuthToken);
    }

    public Auth(String token) {
        String[] split = token.split(":");

        if (split.length != 2) {
            throw new IllegalArgumentException("Invalid authentication token format.");
        }

        dsPrsID = split[0];
        mmeAuthToken = split[1];
    }

    @Override
    public String dsPrsID() {
        return dsPrsID;
    }

    public String mmeAuthToken() {
        return mmeAuthToken;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(dsPrsID);
        hash = 97 * hash + Objects.hashCode(mmeAuthToken);
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

        final Auth other = (Auth) obj;
        return Objects.equals(this.dsPrsID, other.dsPrsID)
                && Objects.equals(this.mmeAuthToken, other.mmeAuthToken);
    }

    @Override
    public String toString() {
        return "Auth{" + "dsPrsID=" + dsPrsID + ", mmeAuthToken=" + mmeAuthToken + '}';
    }
}
