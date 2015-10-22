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
package com.github.horrorho.inflatabledonkey.args;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.jcip.annotations.Immutable;

/**
 * Authentication mapper.
 *
 * @author Ahseya
 * @param <K> key type
 */
@Immutable
public final class AuthenticationMapper implements Function<List<String>, Map<Property, String>> {

    public static AuthenticationMapper instance() {
        return instance;
    }

    private static final AuthenticationMapper instance = new AuthenticationMapper();

    private AuthenticationMapper() {

    }

    @Override
    public Map<Property, String> apply(List<String> operands) {
        Map<Property, String> keyValueMap = new HashMap<>();

        switch (operands.size()) {
            case 0:
                // No setAuthenticationProperties credentials
                throw new IllegalArgumentException(
                        "Missing appleid/ password or authentication token");

            case 1:
                // Authentication token
                keyValueMap.put(Property.AUTHENTICATION_TOKEN, operands.get(0));

                break;

            case 2:
                // AppleId/ password pair
                keyValueMap.put(Property.AUTHENTICATION_APPLEID, operands.get(0));
                keyValueMap.put(Property.AUTHENTICATION_PASSWORD, operands.get(1));
                break;

            default:
                throw new IllegalArgumentException(
                        "Too many non-optional arguments, expected appleid/ password or authentication token only");
        }

        return keyValueMap;
    }
}
