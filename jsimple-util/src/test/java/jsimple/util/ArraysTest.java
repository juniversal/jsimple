/*
 * Copyright (c) 2012-2015 Microsoft Mobile.  All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * This file is based on or incorporates material from Apache Harmony
 * http://harmony.apache.org (collectively, "Third Party Code"). Microsoft Mobile
 * is not the original author of the Third Party Code. The original copyright
 * notice and the license, under which Microsoft Mobile received such Third Party
 * Code, are set forth below.
 *
 *
 * Copyright 2006, 2010 The Apache Software Foundation.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jsimple.util;

import jsimple.unit.UnitTest;
import org.junit.Test;

public class ArraysTest extends UnitTest {

    public Comparator<BoxedInteger> reversedIntegerComparator = new Comparator<BoxedInteger>() {
        public int compare(BoxedInteger o1, BoxedInteger o2) {
            return -Utils.compare(o1.intValue(), o2.intValue());
        }
    };

    final static int arraySize = 100;

    private BoxedInteger[] objArray;
    private BoxedInteger[] objectArray;

    @Override public void setUp() {
        objArray = new BoxedInteger[arraySize];
        for (int i = 0; i < objArray.length; i++)
            objArray[i] = BoxedInteger.valueOf(i);

        objectArray = new BoxedInteger[arraySize];
        for (int counter = 0; counter < arraySize; counter++) {
            objectArray[counter] = objArray[counter];
        }
    }

    /**
     * @tests java.util.Arrays#binarySearch(java.lang.Object[], java.lang.Object, java.util.Comparator)
     */
    @Test public void test_binarySearch_Ljava_lang_ObjectLjava_lang_ObjectLjava_util_Comparator() {
        // Test for method int java.util.Arrays.binarySearch(java.lang.Object
        // [], java.lang.Object, java.util.Comparator)
        for (int counter = 0; counter < arraySize; counter++)
            objectArray[counter] = objArray[arraySize - counter - 1];
        assertTrue(
                "Binary search succeeded for value not present in array 1",
                Arrays.binarySearch(objectArray, BoxedInteger.valueOf(-1), reversedIntegerComparator) == -(arraySize + 1));
        assertEquals("Binary search succeeded for value not present in array 2",
                -1, Arrays.binarySearch(objectArray, BoxedInteger.valueOf(arraySize), reversedIntegerComparator));
        for (int counter = 0; counter < arraySize; counter++)
            assertTrue(
                    "Binary search on Object[] with custom comparator answered incorrect position",
                    Arrays.binarySearch(objectArray, objArray[counter], reversedIntegerComparator) == arraySize
                            - counter - 1);
    }

    @Test public void test_Arrays_binaraySearch_T() {
        assertEquals(-1, Arrays.binarySearch(new BoxedInteger[]{BoxedInteger.valueOf(-1)},
                0, 0, BoxedInteger.valueOf(0), reversedIntegerComparator));
        assertEquals(-2, Arrays.binarySearch(new BoxedInteger[]{BoxedInteger.valueOf(-1)},
                1, 1, BoxedInteger.valueOf(0), reversedIntegerComparator));
        assertEquals(-2, Arrays.binarySearch(new BoxedInteger[]{BoxedInteger.valueOf(-1),
                BoxedInteger.valueOf(0)}, 1, 1, BoxedInteger.valueOf(1), reversedIntegerComparator));
        assertEquals(-3, Arrays.binarySearch(new BoxedInteger[]{BoxedInteger.valueOf(-1),
                BoxedInteger.valueOf(0)}, 2, 2, BoxedInteger.valueOf(1), reversedIntegerComparator));
    }

    /**
     * @tests java.util.Arrays#fill(java.lang.Object[], java.lang.Object)
     */
    @Test public void test_fill_Ljava_lang_ObjectLjava_lang_Object() {
        // Test for method void java.util.Arrays.fill(java.lang.Object [],
        // java.lang.Object)
        Object val = new Object();
        Object[] d = new Object[1000];
        Arrays.fill(d, 0, d.length, val);
        for (int i = 0; i < d.length; i++)
            assertTrue("Failed to fill Object array correctly", d[i] == val);
    }

    /**
     * @tests java.util.Arrays#fill(java.lang.Object[], int, int, java.lang.Object)
     */
    @Test public void test_fill_Ljava_lang_ObjectIILjava_lang_Object() {
        // Test for method void java.util.Arrays.fill(java.lang.Object [], int,
        // int, java.lang.Object)
        Object val = new Object();
        Object[] d = new Object[1000];
        Arrays.fill(d, 400, d.length, val);
        for (int i = 0; i < 400; i++)
            assertTrue("Filled elements not in range", !(d[i] == val));
        for (int i = 400; i < d.length; i++)
            assertTrue("Failed to fill Object array correctly", d[i] == val);

        Arrays.fill(d, 400, d.length, null);
        for (int i = 400; i < d.length; i++)
            assertNull("Failed to fill Object array correctly with nulls",
                    d[i]);
    }

    /**
     * @tests java.util.Arrays#equals(java.lang.Object[], java.lang.Object[])
     */
    @Test public void test_equals_Ljava_lang_Object_Ljava_lang_Object() {
        // Test for method boolean java.util.Arrays.equals(java.lang.Object [],
        // java.lang.Object [])
        Object[] d = new Object[1000];
        Object[] x = new Object[1000];
        Object o = new Object();
        Arrays.fill(d, o);
        Arrays.fill(x, new Object());
        assertTrue("Inequal arrays returned true", !Arrays.equals(d, x));
        Arrays.fill(x, o);
        d[50] = null;
        x[50] = null;
        assertTrue("equal arrays returned false", Arrays.equals(d, x));
    }

    /**
     * @tests java.util.Arrays#sort(java.lang.Object[], int, int, java.util.Comparator)
     */
    @Test public void test_sort_Ljava_lang_ObjectIILjava_util_Comparator() {
        // Test for method void java.util.Arrays.sort(java.lang.Object [], int,
        // int, java.util.Comparator)
        int startIndex = arraySize / 4;
        int endIndex = 3 * arraySize / 4;
        BoxedInteger[] originalArray = new BoxedInteger[arraySize];
        for (int counter = 0; counter < arraySize; counter++)
            originalArray[counter] = objectArray[counter];
        Arrays.sort(objectArray, startIndex, endIndex, reversedIntegerComparator);
        for (int counter = 0; counter < startIndex; counter++)
            assertTrue("Array modified outside of bounds",
                    objectArray[counter] == originalArray[counter]);
        for (int counter = startIndex; counter < endIndex - 1; counter++)
            assertTrue("Array not sorted within bounds", reversedIntegerComparator.compare(
                    objectArray[counter], objectArray[counter + 1]) <= 0);
        for (int counter = endIndex; counter < arraySize; counter++)
            assertTrue("Array modified outside of bounds",
                    objectArray[counter] == originalArray[counter]);
    }

    /**
     * @tests java.util.Arrays#sort(java.lang.Object[], java.util.Comparator)
     */
    @Test public void test_sort_Ljava_lang_ObjectLjava_util_Comparator() {
        // Test for method void java.util.Arrays.sort(java.lang.Object [],
        // java.util.Comparator)
        Arrays.sort(objectArray, reversedIntegerComparator);
        for (int counter = 0; counter < arraySize - 1; counter++)
            assertTrue("Array not sorted correctly with custom comparator",
                    reversedIntegerComparator.compare(objectArray[counter],
                            objectArray[counter + 1]) <= 0);
    }

    // Regression HARMONY-6076
    @Test public void test_sort_Ljava_lang_ObjectLjava_util_Comparator_stable() {
        Element[] array = new Element[11];
        array[0] = new Element(122);
        array[1] = new Element(146);
        array[2] = new Element(178);
        array[3] = new Element(208);
        array[4] = new Element(117);
        array[5] = new Element(146);
        array[6] = new Element(173);
        array[7] = new Element(203);
        array[8] = new Element(56);
        array[9] = new Element(208);
        array[10] = new Element(96);

        Comparator<Element> comparator = new Comparator<Element>() {
            public int compare(Element object1, Element object2) {
                return object1.value - object2.value;
            }
        };

        Arrays.sort(array, comparator);

        for (int i = 1; i < array.length; i++) {
            assertTrue(comparator.compare(array[i - 1], array[i]) <= 0);
            if (comparator.compare(array[i - 1], array[i]) == 0) {
                assertTrue(array[i - 1].index < array[i].index);
            }
        }
    }

    public static class Element {
        public int value;

        public int index;

        private static int count = 0;

        public Element(int value) {
            this.value = value;
            index = count++;
        }
    }

    /**
     * @tests java.util.Arrays#hashCode(Object[] a)
     */
    @Test public void test_hashCode_Ljava_lang_Object() {
        int listHashCode;
        int arrayHashCode;

        Object[] objectArr = {BoxedInteger.valueOf(1), "abc", null};
        ArrayList<Object> listOfObject = new ArrayList<Object>();
        for (int i = 0; i < objectArr.length; i++) {
            listOfObject.add(objectArr[i]);
        }
        listHashCode = listOfObject.hashCode();
        arrayHashCode = Arrays.hashCode(objectArr);
        assertEquals(listHashCode, arrayHashCode);
    }

    /**
     * @tests java.util.Arrays#binarySearch(java.lang.Object[], java.lang.Object, java.util.Comparator)
     */
    @Test public void test_binarySearch_Ljava_lang_ObjectIILjava_lang_ObjectLjava_util_Comparator() {
        for (int counter = 0; counter < arraySize; counter++) {
            objectArray[counter] = objArray[arraySize - counter - 1];
        }
        assertTrue("Binary search succeeded for value not present in array 1",
                Arrays.binarySearch(objectArray, 0, arraySize, BoxedInteger.valueOf(-1),
                        reversedIntegerComparator) == -(arraySize + 1));
        assertEquals(
                "Binary search succeeded for value not present in array 2", -1,
                Arrays.binarySearch(objectArray, 0, arraySize, BoxedInteger.valueOf(arraySize),
                        reversedIntegerComparator));
        for (int counter = 0; counter < arraySize; counter++) {
            assertTrue(
                    "Binary search on Object[] with custom comparator answered incorrect position",
                    Arrays.binarySearch(objectArray, objArray[counter], reversedIntegerComparator) == arraySize
                            - counter - 1);
        }
        try {
            Arrays.binarySearch(objectArray, 2, 1, BoxedInteger.valueOf(arraySize), reversedIntegerComparator);
            fail("should throw IllegalArgumentException");
        } catch (ProgrammerError e) {
            // expected
        }
        assertEquals(-1, Arrays.binarySearch(objectArray, 0, 0,
                BoxedInteger.valueOf(arraySize), reversedIntegerComparator));
        try {
            Arrays.binarySearch(objectArray, -1, -2, BoxedInteger.valueOf(arraySize), reversedIntegerComparator);
            fail("should throw IllegalArgumentException");
        } catch (ProgrammerError e) {
            // expected
        }
        try {
            Arrays.binarySearch(objectArray, arraySize + 2, arraySize + 1, BoxedInteger.valueOf(arraySize),
                    reversedIntegerComparator);
            fail("should throw IllegalArgumentException");
        } catch (ProgrammerError e) {
            // expected
        }
        try {
            Arrays.binarySearch(objectArray, -1, 0, BoxedInteger.valueOf(arraySize), reversedIntegerComparator);
            fail("should throw ArrayIndexOutOfBoundsException");
        } catch (ProgrammerError e) {
            // expected
        }
        try {
            Arrays.binarySearch(objectArray, 0, arraySize + 1,
                    BoxedInteger.valueOf(arraySize), reversedIntegerComparator);
            fail("should throw ArrayIndexOutOfBoundsException");
        } catch (ProgrammerError e) {
            // expected
        }
/*

        try {
            Arrays.binarySearch(objectArray, 0, arraySize, new Double(1.0), reversedIntegerComparator);
            fail("should throw ClassCastException");
        } catch (ClassCastException e) {
            // expected
        }
*/
    }
}
