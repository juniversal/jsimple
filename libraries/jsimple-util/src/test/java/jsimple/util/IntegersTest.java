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

public class IntegersTest extends UnitTest {
    /**
     * @tests java.lang.Integer#decode(java.lang.String)
     */
    @Test public void test_decodeLjava_lang_String2() {
        // Test for method java.lang.Integer
        // java.lang.IntegerUtil.decode(java.lang.String)
        assertEquals("Failed for 132233", 132233, Integers.decode("132233"));
        assertEquals("Failed for 07654321", 0x1f58d1, Integers.decode("07654321"));
        assertTrue("Failed for #1234567", Integers.decode("#1234567") == 0x1234567);
        assertTrue("Failed for 0xdAd", Integers.decode("0xdAd") == 0xdad);
        assertEquals("Failed for -23", -23, Integers.decode("-23"));
        assertEquals("Returned incorrect value for 0 decimal", 0, Integers.decode("0"));
        assertEquals("Returned incorrect value for 0 hex", 0, Integers.decode("0x0"));
        assertTrue("Returned incorrect value for most negative value decimal",
                Integers.decode("-2147483648") == 0x80000000);
        assertTrue("Returned incorrect value for most negative value hex",
                Integers.decode("-0x80000000") == 0x80000000);
        assertTrue("Returned incorrect value for most positive value decimal",
                Integers.decode("2147483647") == 0x7fffffff);
        assertTrue("Returned incorrect value for most positive value hex",
                Integers.decode("0x7fffffff") == 0x7fffffff);

        boolean exception = false;
        try {
            Integers.decode("0a");
        } catch (InvalidFormatException e) {
            // correct
            exception = true;
        }
        assertTrue("Failed to throw InvalidFormatException for \"Oa\"", exception);

        exception = false;
        try {
            Integers.decode("2147483648");
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for MAX_VALUE + 1", exception);

        exception = false;
        try {
            Integers.decode("-2147483649");
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for MIN_VALUE - 1", exception);

        exception = false;
        try {
            Integers.decode("0x80000000");
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for hex MAX_VALUE + 1", exception);

        exception = false;
        try {
            Integers.decode("-0x80000001");
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for hex MIN_VALUE - 1", exception);

        exception = false;
        try {
            Integers.decode("9999999999");
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for 9999999999", exception);

        try {
            Integers.decode("-");
            fail("Expected exception for -");
        } catch (InvalidFormatException e) {
            // Expected
        }

        try {
            Integers.decode("0x");
            fail("Expected exception for 0x");
        } catch (InvalidFormatException e) {
            // Expected
        }

        try {
            Integers.decode("#");
            fail("Expected exception for #");
        } catch (InvalidFormatException e) {
            // Expected
        }

        try {
            Integers.decode("x123");
            fail("Expected exception for x123");
        } catch (InvalidFormatException e) {
            // Expected
        }

/*
        try {
            IntegerUtil.decode(null);
            fail("Expected exception for null");
        } catch (NullPointerException e) {
            // Expected
        }
*/

        try {
            Integers.decode("");
            fail("Expected exception for empty string");
        } catch (InvalidFormatException ex) {
            // Expected
        }

        try {
            Integers.decode(" ");
            fail("Expected exception for single space");
        } catch (InvalidFormatException ex) {
            // Expected
        }

    }

    /**
     * @tests java.lang.Integer#parseInt(java.lang.String)
     */
    @Test public void test_parseIntLjava_lang_String2() {
        // Test for method int java.lang.IntegerUtil.parseInt(java.lang.String)

        int i = Integers.parseInt("-8900");
        assertEquals("Returned incorrect int", -8900, i);
        assertEquals("Returned incorrect value for 0", 0, Integers.parseInt("0"));
        assertTrue("Returned incorrect value for most negative value", Integers
                .parseInt("-2147483648") == 0x80000000);
        assertTrue("Returned incorrect value for most positive value", Integers
                .parseInt("2147483647") == 0x7fffffff);

        boolean exception = false;
        try {
            Integers.parseInt("999999999999");
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for value > int", exception);

        exception = false;
        try {
            Integers.parseInt("2147483648");
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for MAX_VALUE + 1", exception);

        exception = false;
        try {
            Integers.parseInt("-2147483649");
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for MIN_VALUE - 1", exception);
    }

    /**
     * @tests java.lang.Integer#parseInt(java.lang.String, int)
     */
    @Test public void test_parseIntLjava_lang_StringI2() {
        // Test for method int java.lang.IntegerUtil.parseInt(java.lang.String, int)
        assertEquals("Parsed dec val incorrectly",
                -8000, Integers.parseInt("-8000", 10));
        assertEquals("Parsed hex val incorrectly",
                255, Integers.parseInt("FF", 16));
        assertEquals("Parsed oct val incorrectly",
                16, Integers.parseInt("20", 8));
        assertEquals("Returned incorrect value for 0 hex", 0, Integers.parseInt("0",
                16));
        assertTrue("Returned incorrect value for most negative value hex",
                Integers.parseInt("-80000000", 16) == 0x80000000);
        assertTrue("Returned incorrect value for most positive value hex",
                Integers.parseInt("7fffffff", 16) == 0x7fffffff);
        assertEquals("Returned incorrect value for 0 decimal", 0, Integers.parseInt(
                "0", 10));
        assertTrue("Returned incorrect value for most negative value decimal",
                Integers.parseInt("-2147483648", 10) == 0x80000000);
        assertTrue("Returned incorrect value for most positive value decimal",
                Integers.parseInt("2147483647", 10) == 0x7fffffff);

        boolean exception = false;
        try {
            Integers.parseInt("FFFF", 10);
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue(
                "Failed to throw exception when passes hex string and dec parm",
                exception);

        exception = false;
        try {
            Integers.parseInt("2147483648", 10);
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for MAX_VALUE + 1", exception);

        exception = false;
        try {
            Integers.parseInt("-2147483649", 10);
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for MIN_VALUE - 1", exception);

        exception = false;
        try {
            Integers.parseInt("80000000", 16);
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for hex MAX_VALUE + 1", exception);

        exception = false;
        try {
            Integers.parseInt("-80000001", 16);
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for hex MIN_VALUE + 1", exception);

        exception = false;
        try {
            Integers.parseInt("9999999999", 10);
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for 9999999999", exception);
    }

    /**
     * @tests java.lang.Integer#toBinaryString(int)
     */
    @Test public void test_toBinaryStringI() {
        // Test for method java.lang.String
        // java.lang.Integer.toBinaryString(int)
        assertEquals("Incorrect string returned", "11", Integers.toBinaryString(3));
        assertEquals("Incorrect string returned", "1111111111111111111111111111111",
                Integers.toBinaryString(Integers.MAX_VALUE));
        assertEquals("Incorrect string returned", "10000000000000000000000000000000",
                Integers.toBinaryString(Integers.MIN_VALUE));
    }

    /**
     * @tests java.lang.Integer#toHexString(int)
     */
    @Test public void test_toHexStringI() {
        // Test for method java.lang.String java.lang.IntegerUtil.toHexString(int)

        String[] hexvals = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "a", "b", "c", "d", "e", "f"};

        for (int i = 0; i < 16; i++) {
            assertTrue("Incorrect string returned " + hexvals[i], Integers.toHexString(i).equals(hexvals[i]));
        }

        assertTrue("Returned incorrect hex string: "
                + Integers.toHexString(Integers.MAX_VALUE), Integers.toHexString(
                Integers.MAX_VALUE).equals("7fffffff"));
        assertTrue("Returned incorrect hex string: "
                + Integers.toHexString(Integers.MIN_VALUE), Integers.toHexString(
                Integers.MIN_VALUE).equals("80000000"));
    }

    /**
     * @tests java.lang.Integer#toString()
     */
    @Test public void test_toString2() {
        // Test for method java.lang.String java.lang.IntegerUtil.toString()

        assertEquals("Returned incorrect String", "-80001", Integers.toString(-80001));
    }

    /**
     * @tests java.lang.Integer#toString(int)
     */
    @Test public void test_toStringI2() {
        // Test for method java.lang.String java.lang.IntegerUtil.toString(int)

        assertEquals("Returned incorrect String", "-80765", Integers.toString(-80765));
        assertEquals("Returned incorrect string", "2147483647", Integers.toString(Integers.MAX_VALUE));
        assertEquals("Returned incorrect string", "-2147483647", Integers.toString(-Integers.MAX_VALUE));
        assertEquals("Returned incorrect string", "-2147483648", Integers.toString(Integers.MIN_VALUE));

        // Test for HARMONY-6068
        assertEquals("Returned incorrect String", "-1000", Integers.toString(-1000));
        assertEquals("Returned incorrect String", "1000", Integers.toString(1000));
        assertEquals("Returned incorrect String", "0", Integers.toString(0));
        assertEquals("Returned incorrect String", "708", Integers.toString(708));
        assertEquals("Returned incorrect String", "-100", Integers.toString(-100));
        assertEquals("Returned incorrect String", "-1000000008", Integers.toString(-1000000008));
        assertEquals("Returned incorrect String", "2000000008", Integers.toString(2000000008));
    }

    /**
     * @tests java.lang.Integer#valueOf(java.lang.String)
     */
    @Test public void test_valueOfLjava_lang_String2() {
        // Test for method java.lang.Integer
        // java.lang.IntegerUtil.valueOf(java.lang.String)
        assertEquals("Returned incorrect int", 8888888, Integers.parseInt("8888888"));
        assertTrue("Returned incorrect int", Integers.parseInt("2147483647") == Integers.MAX_VALUE);
        assertTrue("Returned incorrect int", Integers.parseInt("-2147483648") == Integers.MIN_VALUE);

        boolean exception = false;
        try {
            Integers.parseInt("2147483648");
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception with MAX_VALUE + 1", exception);

        exception = false;
        try {
            Integers.parseInt("-2147483649");
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception with MIN_VALUE - 1", exception);
    }

    /**
     * @tests java.lang.Integer#valueOf(java.lang.String, int)
     */
    @Test public void test_valueOfLjava_lang_StringI2() {
        // Test for method java.lang.Integer
        // java.lang.IntegerUtil.valueOf(java.lang.String, int)
        assertEquals("Returned incorrect int for hex string", 255, Integers.parseInt("FF", 16));
        assertEquals("Returned incorrect int for oct string", 16, Integers.parseInt("20", 8));
        assertEquals("Returned incorrect int for bin string", 4, Integers.parseInt("100", 2));

        assertEquals("Returned incorrect int for - hex string", -255, Integers.parseInt("-FF", 16));
        assertEquals("Returned incorrect int for - oct string", -16, Integers.parseInt("-20", 8));
        assertEquals("Returned incorrect int for - bin string", -4, Integers.parseInt("-100", 2));
        assertTrue("Returned incorrect int", Integers.parseInt("2147483647", 10) == Integers.MAX_VALUE);
        assertTrue("Returned incorrect int", Integers.parseInt("-2147483648", 10) == Integers.MIN_VALUE);
        assertTrue("Returned incorrect int", Integers.parseInt("7fffffff", 16) == Integers.MAX_VALUE);
        assertTrue("Returned incorrect int", Integers.parseInt("-80000000", 16) == Integers.MIN_VALUE);

        boolean exception = false;
        try {
            Integers.parseInt("FF", 2);
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception with hex string and base 2 radix", exception);

        exception = false;
        try {
            Integers.parseInt("2147483648", 10);
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception with MAX_VALUE + 1", exception);

        exception = false;
        try {
            Integers.parseInt("-2147483649", 10);
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception with MIN_VALUE - 1", exception);

        exception = false;
        try {
            Integers.parseInt("80000000", 16);
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception with hex MAX_VALUE + 1", exception);

        exception = false;
        try {
            Integers.parseInt("-80000001", 16);
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception with hex MIN_VALUE - 1", exception);
    }

    /**
     * @tests java.lang.Integer#toString
     */
    @Test public void test_toStringI() {
        assertEquals("-1", Integers.toString(-1));
        assertEquals("0", Integers.toString(0));
        assertEquals("1", Integers.toString(1));
        assertEquals("-1", Integers.toString(0xFFFFFFFF));
    }

    /**
     * @tests java.lang.Integer#parseInt(String)
     */
    @Test public void test_parseIntLjava_lang_String() {
        assertEquals(0, Integers.parseInt("0"));
        assertEquals(1, Integers.parseInt("1"));
        assertEquals(-1, Integers.parseInt("-1"));

        try {
            Integers.parseInt("0x1");
            fail("Expected InvalidFormatException with hex string.");
        } catch (InvalidFormatException ignored) {
        }

        try {
            Integers.parseInt("9.2");
            fail("Expected InvalidFormatException with floating point string.");
        } catch (InvalidFormatException ignored) {
        }

        try {
            Integers.parseInt("");
            fail("Expected InvalidFormatException with empty string.");
        } catch (InvalidFormatException ignored) {
        }

        try {
            Integers.parseInt(null);
            fail("Expected InvalidFormatException with null string.");
        } catch (InvalidFormatException ignored) {
        }
    }

    /**
     * @tests java.lang.Integer#parseInt(String, int)
     */
    @Test public void test_parseIntLjava_lang_StringI() {
        assertEquals(0, Integers.parseInt("0", 10));
        assertEquals(1, Integers.parseInt("1", 10));
        assertEquals(-1, Integers.parseInt("-1", 10));

        //must be consistent with Character.digit()
        assertEquals(1, Integers.parseInt("1", 2));
        assertEquals(15, Integers.parseInt("F", 16));

        try {
            Integers.parseInt("0x1", 10);
            fail("Expected InvalidFormatException with hex string.");
        } catch (InvalidFormatException ignored) {
        }

        try {
            Integers.parseInt("9.2", 10);
            fail("Expected InvalidFormatException with floating point string.");
        } catch (InvalidFormatException ignored) {
        }

        try {
            Integers.parseInt("", 10);
            fail("Expected InvalidFormatException with empty string.");
        } catch (InvalidFormatException ignored) {
        }

        try {
            Integers.parseInt(null, 10);
            fail("Expected InvalidFormatException with null string.");
        } catch (InvalidFormatException ignored) {
        }
    }

    /**
     * @tests java.lang.Integer#decode(String)
     */
    @Test public void test_decodeLjava_lang_String() {
        assertEquals(0, Integers.decode("0"));
        assertEquals(1, Integers.decode("1"));
        assertEquals(-1, Integers.decode("-1"));
        assertEquals(0xF, Integers.decode("0xF"));
        assertEquals(0xF, Integers.decode("#F"));
        assertEquals(0xF, Integers.decode("0XF"));
        assertEquals(7, Integers.decode("07"));

        try {
            Integers.decode("9.2");
            fail("Expected InvalidFormatException with floating point string.");
        } catch (InvalidFormatException ignored) {
        }

        try {
            Integers.decode("");
            fail("Expected InvalidFormatException with empty string.");
        } catch (InvalidFormatException ignored) {
        }

/*
        try {
            IntegerUtil.decode(null);
            //undocumented NPE, but seems consistent across JREs
            fail("Expected NullPointerException with null string.");
        } catch (NullPointerException ignored) {
        }
*/
    }

    /**
     * @tests java.lang.Integer#highestOneBit(int)
     */
    @Test public void test_highestOneBitI() {
        assertEquals(0x08, Integers.highestOneBit(0x0A));
        assertEquals(0x08, Integers.highestOneBit(0x0B));
        assertEquals(0x08, Integers.highestOneBit(0x0C));
        assertEquals(0x08, Integers.highestOneBit(0x0F));
        assertEquals(0x80, Integers.highestOneBit(0xFF));

        assertEquals(0x080000, Integers.highestOneBit(0x0F1234));
        assertEquals(0x800000, Integers.highestOneBit(0xFF9977));

        assertEquals(0x80000000, Integers.highestOneBit(0xFFFFFFFF));

        assertEquals(0, Integers.highestOneBit(0));
        assertEquals(1, Integers.highestOneBit(1));
        assertEquals(0x80000000, Integers.highestOneBit(-1));
    }

    /**
     * @tests java.lang.Integer#lowestOneBit(int)
     */
    @Test public void test_lowestOneBitI() {
        assertEquals(0x10, Integers.lowestOneBit(0xF0));

        assertEquals(0x10, Integers.lowestOneBit(0x90));
        assertEquals(0x10, Integers.lowestOneBit(0xD0));

        assertEquals(0x10, Integers.lowestOneBit(0x123490));
        assertEquals(0x10, Integers.lowestOneBit(0x1234D0));

        assertEquals(0x100000, Integers.lowestOneBit(0x900000));
        assertEquals(0x100000, Integers.lowestOneBit(0xD00000));

        assertEquals(0x40, Integers.lowestOneBit(0x40));
        assertEquals(0x40, Integers.lowestOneBit(0xC0));

        assertEquals(0x4000, Integers.lowestOneBit(0x4000));
        assertEquals(0x4000, Integers.lowestOneBit(0xC000));

        assertEquals(0x4000, Integers.lowestOneBit(0x99994000));
        assertEquals(0x4000, Integers.lowestOneBit(0x9999C000));

        assertEquals(0, Integers.lowestOneBit(0));
        assertEquals(1, Integers.lowestOneBit(1));
        assertEquals(1, Integers.lowestOneBit(-1));
    }

    /**
     * @tests java.lang.Integer#numberOfLeadingZeros(int)
     */
    @Test public void test_numberOfLeadingZerosI() {
        assertEquals(32, Integers.numberOfLeadingZeros(0x0));
        assertEquals(31, Integers.numberOfLeadingZeros(0x1));
        assertEquals(30, Integers.numberOfLeadingZeros(0x2));
        assertEquals(30, Integers.numberOfLeadingZeros(0x3));
        assertEquals(29, Integers.numberOfLeadingZeros(0x4));
        assertEquals(29, Integers.numberOfLeadingZeros(0x5));
        assertEquals(29, Integers.numberOfLeadingZeros(0x6));
        assertEquals(29, Integers.numberOfLeadingZeros(0x7));
        assertEquals(28, Integers.numberOfLeadingZeros(0x8));
        assertEquals(28, Integers.numberOfLeadingZeros(0x9));
        assertEquals(28, Integers.numberOfLeadingZeros(0xA));
        assertEquals(28, Integers.numberOfLeadingZeros(0xB));
        assertEquals(28, Integers.numberOfLeadingZeros(0xC));
        assertEquals(28, Integers.numberOfLeadingZeros(0xD));
        assertEquals(28, Integers.numberOfLeadingZeros(0xE));
        assertEquals(28, Integers.numberOfLeadingZeros(0xF));
        assertEquals(27, Integers.numberOfLeadingZeros(0x10));
        assertEquals(24, Integers.numberOfLeadingZeros(0x80));
        assertEquals(24, Integers.numberOfLeadingZeros(0xF0));
        assertEquals(23, Integers.numberOfLeadingZeros(0x100));
        assertEquals(20, Integers.numberOfLeadingZeros(0x800));
        assertEquals(20, Integers.numberOfLeadingZeros(0xF00));
        assertEquals(19, Integers.numberOfLeadingZeros(0x1000));
        assertEquals(16, Integers.numberOfLeadingZeros(0x8000));
        assertEquals(16, Integers.numberOfLeadingZeros(0xF000));
        assertEquals(15, Integers.numberOfLeadingZeros(0x10000));
        assertEquals(12, Integers.numberOfLeadingZeros(0x80000));
        assertEquals(12, Integers.numberOfLeadingZeros(0xF0000));
        assertEquals(11, Integers.numberOfLeadingZeros(0x100000));
        assertEquals(8, Integers.numberOfLeadingZeros(0x800000));
        assertEquals(8, Integers.numberOfLeadingZeros(0xF00000));
        assertEquals(7, Integers.numberOfLeadingZeros(0x1000000));
        assertEquals(4, Integers.numberOfLeadingZeros(0x8000000));
        assertEquals(4, Integers.numberOfLeadingZeros(0xF000000));
        assertEquals(3, Integers.numberOfLeadingZeros(0x10000000));
        assertEquals(0, Integers.numberOfLeadingZeros(0x80000000));
        assertEquals(0, Integers.numberOfLeadingZeros(0xF0000000));

        assertEquals(1, Integers.numberOfLeadingZeros(Integers.MAX_VALUE));
        assertEquals(0, Integers.numberOfLeadingZeros(Integers.MIN_VALUE));
    }

    /**
     * @tests java.lang.Integer#numberOfTrailingZeros(int)
     */
    @Test public void test_numberOfTrailingZerosI() {
        assertEquals(32, Integers.numberOfTrailingZeros(0x0));
        assertEquals(31, Integers.numberOfTrailingZeros(Integers.MIN_VALUE));
        assertEquals(0, Integers.numberOfTrailingZeros(Integers.MAX_VALUE));

        assertEquals(0, Integers.numberOfTrailingZeros(0x1));
        assertEquals(3, Integers.numberOfTrailingZeros(0x8));
        assertEquals(0, Integers.numberOfTrailingZeros(0xF));

        assertEquals(4, Integers.numberOfTrailingZeros(0x10));
        assertEquals(7, Integers.numberOfTrailingZeros(0x80));
        assertEquals(4, Integers.numberOfTrailingZeros(0xF0));

        assertEquals(8, Integers.numberOfTrailingZeros(0x100));
        assertEquals(11, Integers.numberOfTrailingZeros(0x800));
        assertEquals(8, Integers.numberOfTrailingZeros(0xF00));

        assertEquals(12, Integers.numberOfTrailingZeros(0x1000));
        assertEquals(15, Integers.numberOfTrailingZeros(0x8000));
        assertEquals(12, Integers.numberOfTrailingZeros(0xF000));

        assertEquals(16, Integers.numberOfTrailingZeros(0x10000));
        assertEquals(19, Integers.numberOfTrailingZeros(0x80000));
        assertEquals(16, Integers.numberOfTrailingZeros(0xF0000));

        assertEquals(20, Integers.numberOfTrailingZeros(0x100000));
        assertEquals(23, Integers.numberOfTrailingZeros(0x800000));
        assertEquals(20, Integers.numberOfTrailingZeros(0xF00000));

        assertEquals(24, Integers.numberOfTrailingZeros(0x1000000));
        assertEquals(27, Integers.numberOfTrailingZeros(0x8000000));
        assertEquals(24, Integers.numberOfTrailingZeros(0xF000000));

        assertEquals(28, Integers.numberOfTrailingZeros(0x10000000));
        assertEquals(31, Integers.numberOfTrailingZeros(0x80000000));
        assertEquals(28, Integers.numberOfTrailingZeros(0xF0000000));
    }

    /**
     * @tests java.lang.Integer#bitCount(int)
     */
    @Test public void test_bitCountI() {
        assertEquals(0, Integers.bitCount(0x0));
        assertEquals(1, Integers.bitCount(0x1));
        assertEquals(1, Integers.bitCount(0x2));
        assertEquals(2, Integers.bitCount(0x3));
        assertEquals(1, Integers.bitCount(0x4));
        assertEquals(2, Integers.bitCount(0x5));
        assertEquals(2, Integers.bitCount(0x6));
        assertEquals(3, Integers.bitCount(0x7));
        assertEquals(1, Integers.bitCount(0x8));
        assertEquals(2, Integers.bitCount(0x9));
        assertEquals(2, Integers.bitCount(0xA));
        assertEquals(3, Integers.bitCount(0xB));
        assertEquals(2, Integers.bitCount(0xC));
        assertEquals(3, Integers.bitCount(0xD));
        assertEquals(3, Integers.bitCount(0xE));
        assertEquals(4, Integers.bitCount(0xF));

        assertEquals(8, Integers.bitCount(0xFF));
        assertEquals(12, Integers.bitCount(0xFFF));
        assertEquals(16, Integers.bitCount(0xFFFF));
        assertEquals(20, Integers.bitCount(0xFFFFF));
        assertEquals(24, Integers.bitCount(0xFFFFFF));
        assertEquals(28, Integers.bitCount(0xFFFFFFF));
        assertEquals(32, Integers.bitCount(0xFFFFFFFF));
    }

    /**
     * @tests java.lang.Integer#rotateLeft(int, int)
     */
    @Test public void test_rotateLeftII() {
        assertEquals(0xF, Integers.rotateLeft(0xF, 0));
        assertEquals(0xF0, Integers.rotateLeft(0xF, 4));
        assertEquals(0xF00, Integers.rotateLeft(0xF, 8));
        assertEquals(0xF000, Integers.rotateLeft(0xF, 12));
        assertEquals(0xF0000, Integers.rotateLeft(0xF, 16));
        assertEquals(0xF00000, Integers.rotateLeft(0xF, 20));
        assertEquals(0xF000000, Integers.rotateLeft(0xF, 24));
        assertEquals(0xF0000000, Integers.rotateLeft(0xF, 28));
        assertEquals(0xF0000000, Integers.rotateLeft(0xF0000000, 32));
    }

    /**
     * @tests java.lang.Integer#rotateRight(int, int)
     */
    @Test public void test_rotateRightII() {
        assertEquals(0xF, Integers.rotateRight(0xF0, 4));
        assertEquals(0xF, Integers.rotateRight(0xF00, 8));
        assertEquals(0xF, Integers.rotateRight(0xF000, 12));
        assertEquals(0xF, Integers.rotateRight(0xF0000, 16));
        assertEquals(0xF, Integers.rotateRight(0xF00000, 20));
        assertEquals(0xF, Integers.rotateRight(0xF000000, 24));
        assertEquals(0xF, Integers.rotateRight(0xF0000000, 28));
        assertEquals(0xF0000000, Integers.rotateRight(0xF0000000, 32));
        assertEquals(0xF0000000, Integers.rotateRight(0xF0000000, 0));
    }

    /**
     * @tests java.lang.Integer#reverseBytes(int)
     */
    @Test public void test_reverseBytesI() {
        assertEquals(0xAABBCCDD, Integers.reverseBytes(0xDDCCBBAA));
        assertEquals(0x11223344, Integers.reverseBytes(0x44332211));
        assertEquals(0x00112233, Integers.reverseBytes(0x33221100));
        assertEquals(0x20000002, Integers.reverseBytes(0x02000020));
    }

    /**
     * @tests java.lang.Integer#reverse(int)
     */
    @Test public void test_reverseI() {
        assertEquals(-1, Integers.reverse(-1));
        assertEquals(0x80000000, Integers.reverse(1));
    }

}
