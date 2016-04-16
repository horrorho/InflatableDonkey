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
package com.github.horrorho.inflatabledonkey.cloud.accounts;

import java.util.Objects;
import net.jcip.annotations.Immutable;

/**
 * Account.
 *
 * @author Ahseya
 */
@Immutable
public final class Account {

    private final MobileMe mobileMe;
    private final AccountInfo accountInfo;
    private final Tokens tokens;

    public Account(MobileMe mobileMe, AccountInfo accountInfo, Tokens tokens) {
        this.mobileMe = Objects.requireNonNull(mobileMe, "mobileMe");
        this.accountInfo = Objects.requireNonNull(accountInfo, "accountInfo");
        this.tokens = Objects.requireNonNull(tokens, "tokens");
    }

    public MobileMe mobileMe() {
        return mobileMe;
    }

    public AccountInfo accountInfo() {
        return accountInfo;
    }

    public Tokens tokens() {
        return tokens;
    }

    @Override
    public String toString() {
        return "Account{" + "mobileMe=" + mobileMe + ", accountInfo=" + accountInfo + ", tokens=" + tokens + '}';
    }
}
