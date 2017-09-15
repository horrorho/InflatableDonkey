/*
 * The MIT License
 *
 * Copyright 2017 Ahseya.
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
package com.github.horrorho.inflatabledonkey.protobuf.util;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.TextFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static java.util.stream.Collectors.joining;
import javax.annotation.concurrent.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class ProtobufAssistant {
    private static final Logger logger = LoggerFactory.getLogger(ProtobufAssistant.class);
    
    public static void logDebugUnknownFields(GeneratedMessage message) {
       if (logger.isTraceEnabled()) {
           debugUnknownFields(message)
                   .ifPresent(u -> logger.info("-- logDebugUnknownFields() - unknown fields: {} in: {}", u, message));
       }
    }

    public static Optional<String> debugUnknownFields(GeneratedMessage message) {
        return Optional.of(unknownFields(message))
                .filter(u -> !u.isEmpty())
                .map(u -> u.stream().collect(joining(", ")));
    }

    public static List<String> unknownFields(GeneratedMessage message) {
        return unknownFields(message.getDescriptorForType().getFullName(), message);
    }

    static List<String> unknownFields(String fullName, GeneratedMessage message) {
        List<String> list = new ArrayList<>();
        message.getAllFields()
                .forEach((k, v) -> {
                    if (v instanceof GeneratedMessage) {
                        unknownFields(k.getFullName(), (GeneratedMessage) v).forEach(list::add);
                    }
                });
        String debug = TextFormat.shortDebugString(message.getUnknownFields());
        if (!debug.isEmpty()) {
            list.add(fullName + " -> " + debug);
        }
        return list;
    }
}
