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
package com.github.horrorho.inflatabledonkey.file;

import java.util.function.UnaryOperator;
import javax.annotation.concurrent.Immutable;
import org.apache.commons.lang3.SystemUtils;

/**
 * FileNameCleaners.
 *
 * @author Ahseya
 */
@Immutable
public final class FileNameCleaners {

    public static UnaryOperator<String> instance() {
        return INSTANCE;
    }

    private static final String WINDOWS_FILTER = "[\u0001-\u001f\\\\:*?\"<>|\u007f]";
    private static final String REPLACE = "_";

    private static final UnaryOperator<String> INSTANCE
            = SystemUtils.IS_OS_WINDOWS
                    ? path -> path.replaceAll(WINDOWS_FILTER, REPLACE)
                    : UnaryOperator.identity(); // Assumed Nix/ MacOS mapping directly.
}
