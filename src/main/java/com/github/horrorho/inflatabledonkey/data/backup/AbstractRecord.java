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
package com.github.horrorho.inflatabledonkey.data.backup;

import com.github.horrorho.inflatabledonkey.protocol.CloudKit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AbstractRecord.
 *
 * @author Ahseya
 */
public abstract class AbstractRecord {

    private static final Logger logger = LoggerFactory.getLogger(AbstractRecord.class);

    static Map<String, CloudKit.RecordField> map(Collection<CloudKit.RecordField> recordFields) {
        return recordFields.stream()
                .collect(Collectors.toMap(
                        x -> x.getIdentifier().getName(), Function.identity(),
                        (a, b) -> {
                            logger.warn("-- map() - collision: {} {}", a, b);
                            return a;
                        }));
    }

    private final Map<String, CloudKit.RecordField> recordFields;

    private AbstractRecord(Map<String, CloudKit.RecordField> recordFields) {
        this.recordFields = Objects.requireNonNull(recordFields, "recordFields");
    }

    public AbstractRecord(Collection<CloudKit.RecordField> recordFields) {
        this(map(recordFields));
    }

    public Map<String, CloudKit.RecordField> recordFields() {
        return new HashMap<>(recordFields);
    }

    public Optional<CloudKit.RecordField> recordField(String name) {
        return Optional.ofNullable(recordFields.get(name));
    }

    public Optional<CloudKit.RecordFieldValue> recordFieldValue(String name) {
        return Optional.ofNullable(recordFields.get(name).getValue());
    }

    @Override
    public String toString() {
        return "AbstractRecord{" + "recordFields=" + recordFields + '}';
    }
}
