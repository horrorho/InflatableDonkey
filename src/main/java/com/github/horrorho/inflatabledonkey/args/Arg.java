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
package com.github.horrorho.inflatabledonkey.args;

import java.util.Objects;
import java.util.function.UnaryOperator;
import net.jcip.annotations.NotThreadSafe;
import org.apache.commons.cli.Option;

/**
 *
 * @author Ahseya
 */
@NotThreadSafe
public class Arg {

    private final Property property;
    private final Option option;
    private final UnaryOperator<String> parser;

    public Arg(Property property, Option option, UnaryOperator<String> parser) {
        this.property = property;
        this.option = option;
        this.parser = parser;
    }

    public Arg(Property property, Option option) {
        this(property, option, UnaryOperator.identity());
    }

    public Option option() {
        return option;
    }

    public String parse(String value) {
        return parser.apply(value);
    }

    public Property property() {
        return property;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.option);
        hash = 41 * hash + Objects.hashCode(this.parser);
        hash = 41 * hash + Objects.hashCode(this.property);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Arg other = (Arg) obj;
        if (!Objects.equals(this.option, other.option)) {
            return false;
        }
        if (!Objects.equals(this.parser, other.parser)) {
            return false;
        }
        if (this.property != other.property) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Arg{" + "property=" + property + ", option=" + option + ", parser=" + parser + '}';
    }
}
