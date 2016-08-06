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
package com.github.horrorho.inflatabledonkey.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import junitparams.JUnitParamsRunner;
import static junitparams.JUnitParamsRunner.$;
import junitparams.Parameters;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BiMapSetTest.
 *
 * @author Ahseya
 */
@RunWith(JUnitParamsRunner.class)
public class BiMapSetTest {

    public static final Logger logger = LoggerFactory.getLogger(BiMapSetTest.class);

    public static int maxEven = 1000;

    public final static Map<String, Set<Integer>> numbers = new HashMap<>();
    public final static Map<String, Set<Integer>> empty = new HashMap<>();
    public final static Map<String, Set<Integer>> nullKey = new HashMap<>();
    public final static Map<String, Set<Integer>> nullValue = new HashMap<>();
    public final static Map<String, Set<Integer>> nullKeyValue = new HashMap<>();
    public final static Map<String, Set<Integer>> nullMap = null;

    public final static Map<String, Set<Integer>> oneStringToNumber = new HashMap<>();
    public final static Map<Integer, Set<String>> oneNumberToString = new HashMap<>();

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        numbers.clear();
        numbers.put("odd", set(1, 3, 5, 9));
        numbers.put("even", set(0, 2, 4, 6));
        numbers.put("square", set(4, 9));
        numbers.put("zero", set(0));
        numbers.put("hundred", set());

        nullKey.clear();
        nullKey.put("odd", set(1, 3, 5));
        nullKey.put(null, set(2, 4, 6));

        nullValue.clear();
        nullValue.put("odd", set(1, null, 5));
        nullValue.put("even", set(2, 4, 6));

        nullKeyValue.clear();
        nullKeyValue.put("odd", set(1, null, 5));
        nullKeyValue.put(null, set(2, 4, 6));

        oneStringToNumber.clear();
        oneStringToNumber.put("odd", set(1, 3, 5, 9));
        oneStringToNumber.put("even", set(0, 2, 4, 6));
        oneStringToNumber.put("zero", set(0));
        oneStringToNumber.put("square", set(4, 9));

        oneNumberToString.clear();
        oneNumberToString.put(0, set("zero", "even"));
        oneNumberToString.put(1, set("odd"));
        oneNumberToString.put(2, set("even"));
        oneNumberToString.put(3, set("odd"));
        oneNumberToString.put(4, set("even", "square"));
        oneNumberToString.put(5, set("odd"));
        oneNumberToString.put(6, set("even"));
        oneNumberToString.put(9, set("odd", "square"));
    }

    @Test
    public void testFrom() {
        BiMapSet<String, Integer> oneSet = BiMapSet.from(numbers);
        assertThat(oneSet.kToVSet, is(oneStringToNumber));
        assertThat(oneSet.vToKSet, is(oneNumberToString));
    }

    @Test
    @Parameters
    public void testFromException(Map map) {
        exception.expect(NullPointerException.class);
        BiMapSet.from(map);
    }

    public static Object[] parametersForTestFromException() {
        return new Object[]{nullMap, nullKey, nullValue, nullKeyValue};
    }

    @Test
    public void testKeySet() {
        BiMapSet<String, Integer> oneSet = BiMapSet.from(numbers);
        assertThat(oneSet.keySet(), is(oneStringToNumber.keySet()));
    }

    @Test
    public void testValueSet() {
        BiMapSet<String, Integer> oneSet = BiMapSet.from(numbers);
        assertThat(oneSet.valueSet(), is(oneNumberToString.keySet()));
    }

    @Test
    @Parameters
    public void testKey(Map<String, Set<Integer>> map, Integer value, Set<String> keys) {
        BiMapSet<String, Integer> mapSet = BiMapSet.from(map);
        assertThat(mapSet.keys(value), is(keys));
    }

    public static Object[] parametersForTestKey() {
        return new Object[]{
            $(numbers, null, set()),
            $(numbers, -1, set()),
            $(numbers, 0, set("even", "zero")),
            $(numbers, 1, set("odd")),
            $(numbers, 2, set("even")),
            $(numbers, 3, set("odd")),
            $(numbers, 4, set("even", "square")),
            $(numbers, 5, set("odd")),
            $(numbers, 6, set("even")),
            $(numbers, 9, set("odd", "square"))
        };
    }

    @Test
    @Parameters
    public void testValue(Map<String, Set<Integer>> map, String key, Set<Integer> values) {
        BiMapSet<String, Integer> mapSet = BiMapSet.from(map);
        assertThat(mapSet.values(key), is(values));
    }

    public static Object[] parametersForTestValue() {
        return new Object[]{
            $(numbers, null, set()),
            $(numbers, "", set()),
            $(numbers, "null", set()),
            $(numbers, "zero", set(0)),
            $(numbers, "odd", set(1, 3, 5, 9)),
            $(numbers, "even", set(0, 2, 4, 6)),
            $(numbers, "square", set(4, 9)),
            $(numbers, "hundred", set())
        };
    }

    @Test
    public void testRemoveKey() {
        BiMapSet<String, Integer> set = BiMapSet.from(numbers);
        assertThat(set.keySet(), is(set("zero", "square", "even", "odd")));
        assertThat(set.valueSet(), is(set(0, 1, 2, 3, 4, 5, 6, 9)));

        assertThat(set.removeKey(null), is(set()));
        assertThat(set.keySet(), is(set("zero", "square", "even", "odd")));
        assertThat(set.valueSet(), is(set(0, 1, 2, 3, 4, 5, 6, 9)));

        assertThat(set.removeKey("null"), is(set()));
        assertThat(set.keySet(), is(set("zero", "square", "even", "odd")));
        assertThat(set.valueSet(), is(set(0, 1, 2, 3, 4, 5, 6, 9)));

        assertThat(set.removeKey("odd"), is(set(1, 3, 5)));
        assertThat(set.keySet(), is(set("zero", "square", "even")));
        assertThat(set.valueSet(), is(set(0, 2, 4, 6, 9)));

        assertThat(set.removeKey("even"), is(set(2, 6)));
        assertThat(set.keySet(), is(set("zero", "square")));
        assertThat(set.valueSet(), is(set(0, 4, 9)));

        assertThat(set.removeKey("square"), is(set(4, 9)));
        assertThat(set.keySet(), is(set("zero")));
        assertThat(set.valueSet(), is(set(0)));

        assertThat(set.removeKey("square"), is(set()));
        assertThat(set.keySet(), is(set("zero")));
        assertThat(set.valueSet(), is(set(0)));

        assertThat(set.removeKey("zero"), is(set(0)));
        assertThat(set.keySet(), is(set()));
        assertThat(set.valueSet(), is(set()));
        assertThat(set.isEmpty(), is(true));

        assertThat(set.removeKey("null"), is(set()));
        assertThat(set.keySet(), is(set()));
        assertThat(set.valueSet(), is(set()));
        assertThat(set.isEmpty(), is(true));

        assertThat(set.removeKey(null), is(set()));
        assertThat(set.keySet(), is(set()));
        assertThat(set.valueSet(), is(set()));
        assertThat(set.isEmpty(), is(true));
    }

    @Test
    public void testRemoveValue() {
        BiMapSet<String, Integer> set = BiMapSet.from(numbers);
        assertThat(set.keySet(), is(set("zero", "square", "even", "odd")));
        assertThat(set.valueSet(), is(set(0, 1, 2, 3, 4, 5, 6, 9)));

        assertThat(set.removeValue(null), is(set()));
        assertThat(set.keySet(), is(set("zero", "square", "even", "odd")));
        assertThat(set.valueSet(), is(set(0, 1, 2, 3, 4, 5, 6, 9)));

        assertThat(set.removeValue(0), is(set("zero")));
        assertThat(set.keySet(), is(set("square", "even", "odd")));
        assertThat(set.valueSet(), is(set(1, 2, 3, 4, 5, 6, 9)));

        assertThat(set.removeValue(1), is(set()));
        assertThat(set.keySet(), is(set("square", "even", "odd")));
        assertThat(set.valueSet(), is(set(2, 3, 4, 5, 6, 9)));

        assertThat(set.removeValue(2), is(set()));
        assertThat(set.keySet(), is(set("square", "even", "odd")));
        assertThat(set.valueSet(), is(set(3, 4, 5, 6, 9)));

        assertThat(set.removeValue(3), is(set()));
        assertThat(set.keySet(), is(set("square", "even", "odd")));
        assertThat(set.valueSet(), is(set(4, 5, 6, 9)));

        assertThat(set.removeValue(4), is(set()));
        assertThat(set.keySet(), is(set("square", "even", "odd")));
        assertThat(set.valueSet(), is(set(5, 6, 9)));

        assertThat(set.removeValue(5), is(set()));
        assertThat(set.keySet(), is(set("square", "even", "odd")));
        assertThat(set.valueSet(), is(set(6, 9)));

        assertThat(set.removeValue(6), is(set("even")));
        assertThat(set.keySet(), is(set("square", "odd")));
        assertThat(set.valueSet(), is(set(9)));

        assertThat(set.removeValue(9), is(set("odd", "square")));
        assertThat(set.keySet(), is(set()));
        assertThat(set.valueSet(), is(set()));
        assertThat(set.isEmpty(), is(true));

        assertThat(set.removeValue(-1), is(set()));
        assertThat(set.keySet(), is(set()));
        assertThat(set.valueSet(), is(set()));
        assertThat(set.isEmpty(), is(true));

        assertThat(set.removeValue(null), is(set()));
        assertThat(set.keySet(), is(set()));
        assertThat(set.valueSet(), is(set()));
        assertThat(set.isEmpty(), is(true));
    }

    @Test
    public void testIsEmpty() {
        BiMapSet<String, Integer> oneSet = BiMapSet.from(numbers);
        assertThat(oneSet.isEmpty(), is(false));

        BiMapSet<String, Integer> emptySet = BiMapSet.from(empty);
        assertThat(emptySet.isEmpty(), is(true));
    }

    @Test
    public void testCompound() {
        BiMapSet<String, Integer> set = BiMapSet.from(numbers);
        assertThat(set.keySet(), is(set("zero", "square", "even", "odd")));
        assertThat(set.valueSet(), is(set(0, 1, 2, 3, 4, 5, 6, 9)));

        assertThat(set.removeKey("null"), is(set()));
        assertThat(set.keySet(), is(set("zero", "square", "even", "odd")));
        assertThat(set.valueSet(), is(set(0, 1, 2, 3, 4, 5, 6, 9)));

        assertThat(set.removeKey(null), is(set()));
        System.out.println("c " + set.keySet() + " " + set.valueSet());
        assertThat(set.keySet(), is(set("zero", "square", "even", "odd")));
        assertThat(set.valueSet(), is(set(0, 1, 2, 3, 4, 5, 6, 9)));

        assertThat(set.removeValue(-1), is(set()));
        assertThat(set.keySet(), is(set("zero", "square", "even", "odd")));
        assertThat(set.valueSet(), is(set(0, 1, 2, 3, 4, 5, 6, 9)));

        assertThat(set.removeKey("odd"), is(set(1, 3, 5)));
        assertThat(set.keySet(), is(set("zero", "square", "even")));
        assertThat(set.valueSet(), is(set(0, 2, 4, 6, 9)));

        assertThat(set.removeValue(0), is(set("zero")));
        assertThat(set.keySet(), is(set("square", "even")));
        assertThat(set.valueSet(), is(set(2, 4, 6, 9)));

        assertThat(set.removeValue(4), is(set()));
        assertThat(set.keySet(), is(set("square", "even")));
        assertThat(set.valueSet(), is(set(2, 6, 9)));

        assertThat(set.removeValue(9), is(set("square")));
        assertThat(set.keySet(), is(set("even")));
        assertThat(set.valueSet(), is(set(2, 6)));

        assertThat(set.removeKey("even"), is(set(2, 6)));
        assertThat(set.keySet(), is(set()));
        assertThat(set.valueSet(), is(set()));
        assertThat(set.isEmpty(), is(true));
    }

    public static <T> Set<T> set(T... t) {
        return Stream.of(t).collect(Collectors.toSet());
    }
}
