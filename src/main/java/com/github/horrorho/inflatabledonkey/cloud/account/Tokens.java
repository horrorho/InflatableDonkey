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
package com.github.horrorho.inflatabledonkey.cloud.account;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSString;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import net.jcip.annotations.Immutable;

/**
 * Tokens.
 *
 * @author Ahseya
 */
@Immutable
public final class Tokens {

    static Map<Token, String> tokens(NSDictionary dictionary) {
        return dictionary.entrySet()
                .stream()
                .filter(e -> Token.token(e.getKey()).isPresent())
                .filter(e -> e.getValue() instanceof NSString)
                .collect(Collectors.toMap(
                        e -> Token.token(e.getKey()).get(),
                        e -> ((NSString) e.getValue()).getContent()));
    }

    private final Map<Token, String> tokens;

    private Tokens(Map<Token, String> tokens) {
        this.tokens = Objects.requireNonNull(tokens, "tokens");
    }

    public Tokens(NSDictionary tokens) {
        this(tokens(tokens));
    }

    public Optional<String> token(Token token) {
        return Optional.ofNullable(tokens.get(token));
    }

    @Override
    public String toString() {
        return "Tokens{" + "tokens=" + tokens + '}';
    }
}
