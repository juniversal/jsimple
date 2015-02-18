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

public class LongsTest extends UnitTest {
    /**
     * @tests java.lang.Long#decode(java.lang.String)
     */
    @Test public void test_decodeLjava_lang_String2() {
        assertEquals("Returned incorrect value for hex string", 255L, Longs.decode("0xFF"));
        assertEquals("Returned incorrect value for dec string", -89000L, Longs.decode("-89000"));
        assertEquals("Returned incorrect value for 0 decimal", 0, Longs.decode("0"));
        assertEquals("Returned incorrect value for 0 hex", 0, Longs.decode("0x0"));
        assertTrue("Returned incorrect value for most negative value decimal",
                Longs.decode("-9223372036854775808") == 0x8000000000000000L);
        assertTrue("Returned incorrect value for most negative value hex",
                Longs.decode("-0x8000000000000000") == 0x8000000000000000L);
        assertTrue("Returned incorrect value for most positive value decimal",
                Longs.decode("9223372036854775807") == 0x7fffffffffffffffL);
        assertTrue("Returned incorrect value for most positive value hex",
                Longs.decode("0x7fffffffffffffff") == 0x7fffffffffffffffL);
        assertTrue("Failed for 07654321765432", Longs.decode("07654321765432") == 0x7d6347eb1aL);

        boolean exception = false;
        try {
            Longs.decode("999999999999999999999999999999999999999999999999999999");
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for value > ilong", exception);

        exception = false;
        try {
            Longs.decode("9223372036854775808");
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for MAX_VALUE + 1", exception);

        exception = false;
        try {
            Longs.decode("-9223372036854775809");
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for MIN_VALUE - 1", exception);

        exception = false;
        try {
            Longs.decode("0x8000000000000000");
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for hex MAX_VALUE + 1", exception);

        exception = false;
        try {
            Longs.decode("-0x8000000000000001");
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for hex MIN_VALUE - 1", exception);

        exception = false;
        try {
            Longs.decode("42325917317067571199");
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for 42325917317067571199",
                exception);
    }

    /**
     * @tests java.lang.Long#parseLong(java.lang.String)
     */
    @Test public void test_parseLongLjava_lang_String2() {
        // Test for method long java.lang.LongUtil.parseLong(java.lang.String)

        long l = Longs.parseLong("89000000005");
        assertEquals("Parsed to incorrect long value", 89000000005L, l);
        assertEquals("Returned incorrect value for 0", 0, Longs.parseLong("0"));
        assertTrue("Returned incorrect value for most negative value",
                Longs.parseLong("-9223372036854775808") == 0x8000000000000000L);
        assertTrue("Returned incorrect value for most positive value",
                Longs.parseLong("9223372036854775807") == 0x7fffffffffffffffL);

        boolean exception = false;
        try {
            Longs.parseLong("9223372036854775808");
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for MAX_VALUE + 1", exception);

        exception = false;
        try {
            Longs.parseLong("-9223372036854775809");
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for MIN_VALUE - 1", exception);
    }

    /**
     * @tests java.lang.Long#parseLong(java.lang.String, int)
     */
    @Test public void test_parseLongLjava_lang_StringI() {
        // Test for method long java.lang.LongUtil.parseLong(java.lang.String, int)
        assertEquals("Returned incorrect value", 100000000L, Longs.parseLong("100000000", 10));
        assertEquals("Returned incorrect value from hex string", 68719476735L, Longs.parseLong(
                "FFFFFFFFF", 16));
        assertTrue("Returned incorrect value from octal string: "
                + Longs.parseLong("77777777777"), Longs.parseLong("77777777777", 8) == 8589934591L);
        assertEquals("Returned incorrect value for 0 hex", 0, Longs.parseLong("0", 16));
        assertTrue("Returned incorrect value for most negative value hex",
                Longs.parseLong("-8000000000000000", 16) == 0x8000000000000000L);
        assertTrue("Returned incorrect value for most positive value hex",
                Longs.parseLong("7fffffffffffffff", 16) == 0x7fffffffffffffffL);
        assertEquals("Returned incorrect value for 0 decimal", 0, Longs.parseLong(
                "0", 10));
        assertTrue(
                "Returned incorrect value for most negative value decimal",
                Longs.parseLong("-9223372036854775808", 10) == 0x8000000000000000L);
        assertTrue(
                "Returned incorrect value for most positive value decimal",
                Longs.parseLong("9223372036854775807", 10) == 0x7fffffffffffffffL);

        boolean exception = false;
        try {
            Longs.parseLong("999999999999", 8);
        } catch (InvalidFormatException e) {
            // correct
            exception = true;
        }
        assertTrue("Failed to throw exception when passed invalid string",
                exception);

        exception = false;
        try {
            Longs.parseLong("9223372036854775808", 10);
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for MAX_VALUE + 1", exception);

        exception = false;
        try {
            Longs.parseLong("-9223372036854775809", 10);
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for MIN_VALUE - 1", exception);

        exception = false;
        try {
            Longs.parseLong("8000000000000000", 16);
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for hex MAX_VALUE + 1", exception);

        exception = false;
        try {
            Longs.parseLong("-8000000000000001", 16);
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for hex MIN_VALUE + 1", exception);

        exception = false;
        try {
            Longs.parseLong("42325917317067571199", 10);
        } catch (InvalidFormatException e) {
            // Correct
            exception = true;
        }
        assertTrue("Failed to throw exception for 42325917317067571199",
                exception);
    }

    /**
     * @tests java.lang.Long#toBinaryString(long)
     */
    @Test public void test_toBinaryStringJ() {
        // Test for method java.lang.String java.lang.Long.toBinaryString(long)
        assertEquals("Incorrect binary string returned", "11011001010010010000",
                Longs.toBinaryString(890000L));
        assertEquals("Incorrect binary string returned",
                "1000000000000000000000000000000000000000000000000000000000000000",
                Longs.toBinaryString(Longs.MIN_VALUE)
        );
        assertEquals("Incorrect binary string returned",
                "111111111111111111111111111111111111111111111111111111111111111",
                Longs.toBinaryString(Longs.MAX_VALUE)
        );
    }

    /**
     * @tests java.lang.Long#toHexString(long)
     */
    @Test public void test_toHexStringJ() {
        assertEquals("Incorrect hex string returned", "54e0845", Longs.toHexString(89000005L));
        assertEquals("Incorrect hex string returned", "8000000000000000", Longs.toHexString(Longs.MIN_VALUE));
        assertEquals("Incorrect hex string returned", "7fffffffffffffff", Longs.toHexString(Longs.MAX_VALUE));
    }
    
    /**
     * @tests java.lang.Long#toString(long)
     */
    @Test public void test_toStringJ2() {
        assertEquals("Returned incorrect String", "89000000005", Longs.toString(89000000005L));
        assertEquals("Returned incorrect String", "-9223372036854775808", Longs.toString(Longs.MIN_VALUE));
        assertEquals("Returned incorrect String", "9223372036854775807", Longs.toString(Longs.MAX_VALUE));
    }
    
    /**
     * @tests java.lang.Long#toString(long, int)
     */
    @Test public void test_toStringJI() {
        // Test for method java.lang.String java.lang.LongUtil.toString(long, int)
        assertEquals("Returned incorrect dec string", "100000000", Longs.toString(100000000L));
        assertEquals("Returned incorrect min string", "-9223372036854775808", Longs.toString(0x8000000000000000L));
        assertEquals("Returned incorrect max string", "9223372036854775807", Longs.toString(0x7fffffffffffffffL));
    }
    
    /**
     * @tests java.lang.Long#valueOf(java.lang.String)
     */
    @Test public void test_valueOfLjava_lang_String2() {
        // Test for method java.lang.Long
        // java.lang.LongUtil.valueOf(java.lang.String)
        assertEquals("Returned incorrect value", 100000000L, Longs.parseLong("100000000"));
        assertTrue("Returned incorrect value", Longs.parseLong("9223372036854775807") == Longs.MAX_VALUE);
        assertTrue("Returned incorrect value", Longs.parseLong("-9223372036854775808") == Longs.MIN_VALUE);

        boolean exception = false;
        try {
            Longs.parseLong("999999999999999999999999999999999999999999999999999999999999");
        } catch (InvalidFormatException e) {
            // correct
            exception = true;
        }
        assertTrue("Failed to throw exception when passed invalid string", exception);

        exception = false;
        try {
            Longs.parseLong("9223372036854775808");
        } catch (InvalidFormatException e) {
            // correct
            exception = true;
        }
        assertTrue("Failed to throw exception when passed invalid string", exception);

        exception = false;
        try {
            Longs.parseLong("-9223372036854775809");
        } catch (InvalidFormatException e) {
            // correct
            exception = true;
        }
        assertTrue("Failed to throw exception when passed invalid string", exception);
    }

    /**
     * @tests java.lang.Long#valueOf(java.lang.String, int)
     */
    @Test public void test_valueOfLjava_lang_StringI() {
        // Test for method java.lang.Long
        // java.lang.LongUtil.valueOf(java.lang.String, int)
        assertEquals("Returned incorrect value", 100000000L, Longs.parseLong("100000000", 10));
        assertEquals("Returned incorrect value from hex string", 68719476735L, Longs.parseLong("FFFFFFFFF", 16));
        assertTrue("Returned incorrect value from octal string: " + Longs.toString(Longs.parseLong("77777777777", 8)),
                Longs.parseLong("77777777777", 8) == 8589934591L);
        assertTrue("Returned incorrect value", Longs.parseLong("9223372036854775807", 10) == Longs.MAX_VALUE);
        assertTrue("Returned incorrect value", Longs.parseLong("-9223372036854775808", 10) == Longs.MIN_VALUE);
        assertTrue("Returned incorrect value", Longs.parseLong("7fffffffffffffff", 16) == Longs.MAX_VALUE);
        assertTrue("Returned incorrect value", Longs.parseLong("-8000000000000000", 16) == Longs.MIN_VALUE);

        boolean exception = false;
        try {
            Longs.parseLong("999999999999", 8);
        } catch (InvalidFormatException e) {
            // correct
            exception = true;
        }
        assertTrue("Failed to throw exception when passed invalid string", exception);

        exception = false;
        try {
            Longs.parseLong("9223372036854775808", 10);
        } catch (InvalidFormatException e) {
            // correct
            exception = true;
        }
        assertTrue("Failed to throw exception when passed invalid string",
                exception);

        exception = false;
        try {
            Longs.parseLong("-9223372036854775809", 10);
        } catch (InvalidFormatException e) {
            // correct
            exception = true;
        }
        assertTrue("Failed to throw exception when passed invalid string", exception);
    }
    
    /**
     * @tests java.lang.Long#toString
     */
    @Test public void test_toStringJ() {
        assertEquals("-1", Longs.toString(-1));
        assertEquals("0", Longs.toString(0));
        assertEquals("1", Longs.toString(1));
        assertEquals("-1", Longs.toString(0xFFFFFFFF));
    }

    /**
     * @tests java.lang.Long#parseLong(String)
     */
    @Test public void test_parseLongLjava_lang_String() {
        assertEquals(0, Longs.parseLong("0"));
        assertEquals(1, Longs.parseLong("1"));
        assertEquals(-1, Longs.parseLong("-1"));

        try {
            Longs.parseLong("0x1");
            fail("Expected InvalidFormatException with hex string.");
        } catch (InvalidFormatException e) {
        }

        try {
            Longs.parseLong("9.2");
            fail("Expected InvalidFormatException with floating point string.");
        } catch (InvalidFormatException e) {
        }

        try {
            Longs.parseLong("");
            fail("Expected InvalidFormatException with empty string.");
        } catch (InvalidFormatException e) {
        }

        try {
            Longs.parseLong(null);
            fail("Expected InvalidFormatException with null string.");
        } catch (InvalidFormatException e) {
        }
    }

    /**
     * @tests java.lang.Long#parseLong(String, long)
     */
    @Test public void test_parseLongLjava_lang_StringJ() {
        assertEquals(0, Longs.parseLong("0", 10));
        assertEquals(1, Longs.parseLong("1", 10));
        assertEquals(-1, Longs.parseLong("-1", 10));
        
        //must be consistent with Character.digit()
        assertEquals(Integers.digit('1', 2), Longs.parseLong("1", 2));
        assertEquals(Integers.digit('F', 16), Longs.parseLong("F", 16));
        
        try {
            Longs.parseLong("0x1", 10);
            fail("Expected InvalidFormatException with hex string.");
        } catch (InvalidFormatException e) {
        }

        try {
            Longs.parseLong("9.2", 10);
            fail("Expected InvalidFormatException with floating point string.");
        } catch (InvalidFormatException e) {
        }

        try {
            Longs.parseLong("", 10);
            fail("Expected InvalidFormatException with empty string.");
        } catch (InvalidFormatException e) {
        }

        try {
            Longs.parseLong(null, 10);
            fail("Expected InvalidFormatException with null string.");
        } catch (InvalidFormatException e) {
        }
    }

    /**
     * @tests java.lang.Long#decode(String)
     */
    @Test public void test_decodeLjava_lang_String() {
        assertEquals(0, Longs.decode("0"));
        assertEquals(1, Longs.decode("1"));
        assertEquals(-1, Longs.decode("-1"));
        assertEquals(0xF, Longs.decode("0xF"));
        assertEquals(0xF, Longs.decode("#F"));
        assertEquals(0xF, Longs.decode("0XF"));
        assertEquals(7, Longs.decode("07"));

        try {
            Longs.decode("9.2");
            fail("Expected InvalidFormatException with floating point string.");
        } catch (InvalidFormatException ignored) {
        }

        try {
            Longs.decode("");
            fail("Expected InvalidFormatException with empty string.");
        } catch (InvalidFormatException ignored) {
        }

/*
        try {
            LongUtil.decode(null);
            //undocumented NPE, but seems consistent across JREs
            fail("Expected NullPointerException with null string.");
        } catch (NullPointerException e) {
        }
*/
    }

    /**
     * @tests java.lang.Long#highestOneBit(long)
     */
    @Test public void test_highestOneBitJ() {
        assertEquals(0x08, Longs.highestOneBit(0x0A));
        assertEquals(0x08, Longs.highestOneBit(0x0B));
        assertEquals(0x08, Longs.highestOneBit(0x0C));
        assertEquals(0x08, Longs.highestOneBit(0x0F));
        assertEquals(0x80, Longs.highestOneBit(0xFF));

        assertEquals(0x080000, Longs.highestOneBit(0x0F1234));
        assertEquals(0x800000, Longs.highestOneBit(0xFF9977));

        assertEquals(0x8000000000000000L, Longs.highestOneBit(0xFFFFFFFFFFFFFFFFL));

        assertEquals(0, Longs.highestOneBit(0));
        assertEquals(1, Longs.highestOneBit(1));
        assertEquals(0x8000000000000000L, Longs.highestOneBit(-1));
    }

    /**
     * @tests java.lang.Long#lowestOneBit(long)
     */
    @Test public void test_lowestOneBitJ() {
        assertEquals(0x10, Longs.lowestOneBit(0xF0));

        assertEquals(0x10, Longs.lowestOneBit(0x90));
        assertEquals(0x10, Longs.lowestOneBit(0xD0));

        assertEquals(0x10, Longs.lowestOneBit(0x123490));
        assertEquals(0x10, Longs.lowestOneBit(0x1234D0));

        assertEquals(0x100000, Longs.lowestOneBit(0x900000));
        assertEquals(0x100000, Longs.lowestOneBit(0xD00000));

        assertEquals(0x40, Longs.lowestOneBit(0x40));
        assertEquals(0x40, Longs.lowestOneBit(0xC0));

        assertEquals(0x4000, Longs.lowestOneBit(0x4000));
        assertEquals(0x4000, Longs.lowestOneBit(0xC000));

        assertEquals(0x4000, Longs.lowestOneBit(0x99994000));
        assertEquals(0x4000, Longs.lowestOneBit(0x9999C000));

        assertEquals(0, Longs.lowestOneBit(0));
        assertEquals(1, Longs.lowestOneBit(1));
        assertEquals(1, Longs.lowestOneBit(-1));
    }

    /**
     * @tests java.lang.Long#numberOfLeadingZeros(long)
     */
    @Test public void test_numberOfLeadingZerosJ() {
        assertEquals(64, Longs.numberOfLeadingZeros(0x0L));
        assertEquals(63, Longs.numberOfLeadingZeros(0x1));
        assertEquals(62, Longs.numberOfLeadingZeros(0x2));
        assertEquals(62, Longs.numberOfLeadingZeros(0x3));
        assertEquals(61, Longs.numberOfLeadingZeros(0x4));
        assertEquals(61, Longs.numberOfLeadingZeros(0x5));
        assertEquals(61, Longs.numberOfLeadingZeros(0x6));
        assertEquals(61, Longs.numberOfLeadingZeros(0x7));
        assertEquals(60, Longs.numberOfLeadingZeros(0x8));
        assertEquals(60, Longs.numberOfLeadingZeros(0x9));
        assertEquals(60, Longs.numberOfLeadingZeros(0xA));
        assertEquals(60, Longs.numberOfLeadingZeros(0xB));
        assertEquals(60, Longs.numberOfLeadingZeros(0xC));
        assertEquals(60, Longs.numberOfLeadingZeros(0xD));
        assertEquals(60, Longs.numberOfLeadingZeros(0xE));
        assertEquals(60, Longs.numberOfLeadingZeros(0xF));
        assertEquals(59, Longs.numberOfLeadingZeros(0x10));
        assertEquals(56, Longs.numberOfLeadingZeros(0x80));
        assertEquals(56, Longs.numberOfLeadingZeros(0xF0));
        assertEquals(55, Longs.numberOfLeadingZeros(0x100));
        assertEquals(52, Longs.numberOfLeadingZeros(0x800));
        assertEquals(52, Longs.numberOfLeadingZeros(0xF00));
        assertEquals(51, Longs.numberOfLeadingZeros(0x1000));
        assertEquals(48, Longs.numberOfLeadingZeros(0x8000));
        assertEquals(48, Longs.numberOfLeadingZeros(0xF000));
        assertEquals(47, Longs.numberOfLeadingZeros(0x10000));
        assertEquals(44, Longs.numberOfLeadingZeros(0x80000));
        assertEquals(44, Longs.numberOfLeadingZeros(0xF0000));
        assertEquals(43, Longs.numberOfLeadingZeros(0x100000));
        assertEquals(40, Longs.numberOfLeadingZeros(0x800000));
        assertEquals(40, Longs.numberOfLeadingZeros(0xF00000));
        assertEquals(39, Longs.numberOfLeadingZeros(0x1000000));
        assertEquals(36, Longs.numberOfLeadingZeros(0x8000000));
        assertEquals(36, Longs.numberOfLeadingZeros(0xF000000));
        assertEquals(35, Longs.numberOfLeadingZeros(0x10000000));
        assertEquals(0, Longs.numberOfLeadingZeros(0x80000000));
        assertEquals(0, Longs.numberOfLeadingZeros(0xF0000000));

        assertEquals(1, Longs.numberOfLeadingZeros(Longs.MAX_VALUE));
        assertEquals(0, Longs.numberOfLeadingZeros(Longs.MIN_VALUE));
    }

    /**
     * @tests java.lang.Long#numberOfTrailingZeros(long)
     */
    @Test public void test_numberOfTrailingZerosJ() {
        assertEquals(64, Longs.numberOfTrailingZeros(0x0));
        assertEquals(63, Longs.numberOfTrailingZeros(Longs.MIN_VALUE));
        assertEquals(0, Longs.numberOfTrailingZeros(Longs.MAX_VALUE));

        assertEquals(0, Longs.numberOfTrailingZeros(0x1));
        assertEquals(3, Longs.numberOfTrailingZeros(0x8));
        assertEquals(0, Longs.numberOfTrailingZeros(0xF));

        assertEquals(4, Longs.numberOfTrailingZeros(0x10));
        assertEquals(7, Longs.numberOfTrailingZeros(0x80));
        assertEquals(4, Longs.numberOfTrailingZeros(0xF0));

        assertEquals(8, Longs.numberOfTrailingZeros(0x100));
        assertEquals(11, Longs.numberOfTrailingZeros(0x800));
        assertEquals(8, Longs.numberOfTrailingZeros(0xF00));

        assertEquals(12, Longs.numberOfTrailingZeros(0x1000));
        assertEquals(15, Longs.numberOfTrailingZeros(0x8000));
        assertEquals(12, Longs.numberOfTrailingZeros(0xF000));

        assertEquals(16, Longs.numberOfTrailingZeros(0x10000));
        assertEquals(19, Longs.numberOfTrailingZeros(0x80000));
        assertEquals(16, Longs.numberOfTrailingZeros(0xF0000));

        assertEquals(20, Longs.numberOfTrailingZeros(0x100000));
        assertEquals(23, Longs.numberOfTrailingZeros(0x800000));
        assertEquals(20, Longs.numberOfTrailingZeros(0xF00000));

        assertEquals(24, Longs.numberOfTrailingZeros(0x1000000));
        assertEquals(27, Longs.numberOfTrailingZeros(0x8000000));
        assertEquals(24, Longs.numberOfTrailingZeros(0xF000000));

        assertEquals(28, Longs.numberOfTrailingZeros(0x10000000));
        assertEquals(31, Longs.numberOfTrailingZeros(0x80000000));
        assertEquals(28, Longs.numberOfTrailingZeros(0xF0000000));
    }

    /**
     * @tests java.lang.Long#bitCount(long)
     */
    @Test public void test_bitCountJ() {
        assertEquals(0, Longs.bitCount(0x0));
        assertEquals(1, Longs.bitCount(0x1));
        assertEquals(1, Longs.bitCount(0x2));
        assertEquals(2, Longs.bitCount(0x3));
        assertEquals(1, Longs.bitCount(0x4));
        assertEquals(2, Longs.bitCount(0x5));
        assertEquals(2, Longs.bitCount(0x6));
        assertEquals(3, Longs.bitCount(0x7));
        assertEquals(1, Longs.bitCount(0x8));
        assertEquals(2, Longs.bitCount(0x9));
        assertEquals(2, Longs.bitCount(0xA));
        assertEquals(3, Longs.bitCount(0xB));
        assertEquals(2, Longs.bitCount(0xC));
        assertEquals(3, Longs.bitCount(0xD));
        assertEquals(3, Longs.bitCount(0xE));
        assertEquals(4, Longs.bitCount(0xF));

        assertEquals(8, Longs.bitCount(0xFF));
        assertEquals(12, Longs.bitCount(0xFFF));
        assertEquals(16, Longs.bitCount(0xFFFF));
        assertEquals(20, Longs.bitCount(0xFFFFF));
        assertEquals(24, Longs.bitCount(0xFFFFFF));
        assertEquals(28, Longs.bitCount(0xFFFFFFF));
        assertEquals(64, Longs.bitCount(0xFFFFFFFFFFFFFFFFL));
    }

    /**
     * @tests java.lang.Long#rotateLeft(long, long)
     */
    @Test public void test_rotateLeftJI() {
        assertEquals(0xF, Longs.rotateLeft(0xF, 0));
        assertEquals(0xF0, Longs.rotateLeft(0xF, 4));
        assertEquals(0xF00, Longs.rotateLeft(0xF, 8));
        assertEquals(0xF000, Longs.rotateLeft(0xF, 12));
        assertEquals(0xF0000, Longs.rotateLeft(0xF, 16));
        assertEquals(0xF00000, Longs.rotateLeft(0xF, 20));
        assertEquals(0xF000000, Longs.rotateLeft(0xF, 24));
        assertEquals(0xF0000000L, Longs.rotateLeft(0xF, 28));
        assertEquals(0xF000000000000000L, Longs.rotateLeft(0xF000000000000000L, 64));
    }

    /**
     * @tests java.lang.Long#rotateRight(long, long)
     */
    @Test public void test_rotateRightJI() {
        assertEquals(0xF, Longs.rotateRight(0xF0, 4));
        assertEquals(0xF, Longs.rotateRight(0xF00, 8));
        assertEquals(0xF, Longs.rotateRight(0xF000, 12));
        assertEquals(0xF, Longs.rotateRight(0xF0000, 16));
        assertEquals(0xF, Longs.rotateRight(0xF00000, 20));
        assertEquals(0xF, Longs.rotateRight(0xF000000, 24));
        assertEquals(0xF, Longs.rotateRight(0xF0000000L, 28));
        assertEquals(0xF000000000000000L, Longs.rotateRight(0xF000000000000000L, 64));
        assertEquals(0xF000000000000000L, Longs.rotateRight(0xF000000000000000L, 0));

    }

    /**
     * @tests java.lang.Long#reverseBytes(long)
     */
    @Test public void test_reverseBytesJ() {
        assertEquals(0xAABBCCDD00112233L, Longs.reverseBytes(0x33221100DDCCBBAAL));
        assertEquals(0x1122334455667788L, Longs.reverseBytes(0x8877665544332211L));
        assertEquals(0x0011223344556677L, Longs.reverseBytes(0x7766554433221100L));
        assertEquals(0x2000000000000002L, Longs.reverseBytes(0x0200000000000020L));
    }

    /**
     * @tests java.lang.Long#reverse(long)
     */
    @Test public void test_reverseJ() {
        assertEquals(0, Longs.reverse(0));
        assertEquals(-1, Longs.reverse(-1));
        assertEquals(0x8000000000000000L, Longs.reverse(1));
    }
}