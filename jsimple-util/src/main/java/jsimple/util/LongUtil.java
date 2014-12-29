/*
 * Copyright (c) 2012-2014 Microsoft Mobile.  All Rights Reserved.
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

/**
 * The wrapper for the primitive type {@code long}.
 * <p/>
 * As with the specification, this implementation relies on code laid out in <a href="http://www.hackersdelight.org/">Henry
 * S. Warren, Jr.'s Hacker's Delight, (Addison Wesley, 2002)</a> as well as <a href="http://aggregate.org/MAGIC/">The
 * Aggregate's Magic Algorithms</a>.
 *
 * @see java.lang.Number
 * @since 1.0
 */
public final class LongUtil {
    /**
     * Constant for the maximum {@code long} value, 2<sup>63</sup>-1.
     */
    public static final long MAX_VALUE = 0x7FFFFFFFFFFFFFFFL;

    /**
     * Constant for the minimum {@code long} value, -2<sup>63</sup>.
     */
    public static final long MIN_VALUE = 0x8000000000000000L;

    /**
     * Parses the specified string and returns a {@code Long} instance if the string can be decoded into a long value.
     * The string may be an optional minus sign "-" followed by a hexadecimal ("0x..." or "#..."), octal ("0..."), or
     * decimal ("...") representation of a long.
     *
     * @param string a string representation of a long value.
     * @return a {@code Long} containing the value represented by {@code string}.
     * @throws InvalidFormatException if {@code string} can not be parsed as a long value.
     */
    public static Long decode(String string) throws InvalidFormatException {
        int length = string.length(), i = 0;
        if (length == 0) {
            throw new InvalidFormatException("Long can't be empty string");
        }
        char firstDigit = string.charAt(i);
        boolean negative = firstDigit == '-';
        if (negative) {
            if (length == 1) {
                throw new InvalidFormatException(string);
            }
            firstDigit = string.charAt(++i);
        }

        int base = 10;
        if (firstDigit == '0') {
            if (++i == length) {
                return 0L;
            }
            if ((firstDigit = string.charAt(i)) == 'x' || firstDigit == 'X') {
                if (i == length) {
                    throw new InvalidFormatException(string);
                }
                i++;
                base = 16;
            } else {
                base = 8;
            }
        } else if (firstDigit == '#') {
            if (i == length) {
                throw new InvalidFormatException(string);
            }
            i++;
            base = 16;
        }

        return parse(string, i, base, negative);
    }

    /**
     * Parses the specified string as a signed decimal long value. The ASCII character \u002d ('-') is recognized as the
     * minus sign.
     *
     * @param string the string representation of a long value.
     * @return the primitive long value represented by {@code string}.
     * @throws InvalidFormatException if {@code string} is {@code null}, has a length of zero or can not be parsed as a
     *                                long value.
     */
    public static long parseLong(String string) throws InvalidFormatException {
        return parseLong(string, 10);
    }

    /**
     * Parses the specified string as a signed long value using the specified radix. The ASCII character \u002d ('-') is
     * recognized as the minus sign.
     *
     * @param string the string representation of a long value.
     * @param radix  the radix to use when parsing.
     * @return the primitive long value represented by {@code string} using {@code radix}.
     * @throws InvalidFormatException if {@code string} is {@code null} or has a length of zero, {@code radix <
     *                                Character.MIN_RADIX}, {@code radix > Character.MAX_RADIX}, or if {@code string}
     *                                can not be parsed as a long value.
     */
    public static long parseLong(String string, int radix) throws InvalidFormatException {
        if (string == null || radix < IntegerUtil.MIN_RADIX || radix > IntegerUtil.MAX_RADIX) {
            throw new InvalidFormatException("null string or invalid radix");
        }
        int length = string.length(), i = 0;
        if (length == 0) {
            throw new InvalidFormatException(string);
        }
        boolean negative = string.charAt(i) == '-';
        if (negative && ++i == length) {
            throw new InvalidFormatException(string);
        }

        return parse(string, i, radix, negative);
    }

    private static long parse(String string, int offset, int radix, boolean negative) {
        long max = MIN_VALUE / radix;
        long result = 0, length = string.length();
        while (offset < length) {
            int digit = IntegerUtil.digit(string.charAt(offset++), radix);
            if (digit == -1) {
                throw new InvalidFormatException(string);
            }
            if (max > result) {
                throw new InvalidFormatException(string);
            }
            long next = result * radix - digit;
            if (next > result) {
                throw new InvalidFormatException(string);
            }
            result = next;
        }
        if (!negative) {
            result = -result;
            if (result < 0) {
                throw new InvalidFormatException(string);
            }
        }
        return result;
    }

    /**
     * Converts the specified long value into its binary string representation. The returned string is a concatenation
     * of '0' and '1' characters.
     *
     * @param l the long value to convert.
     * @return the binary string representation of {@code l}.
     */
    public static String toBinaryString(long l) {
        int count = 1;
        long j = l;

        if (l < 0) {
            count = 64;
        } else {
            while ((j >>= 1) != 0) {
                count++;
            }
        }

        char[] buffer = new char[count];
        do {
            buffer[--count] = (char) ((l & 1) + '0');
            l >>= 1;
        } while (count > 0);
        return new String(buffer);
    }

    /**
     * Converts the specified long value into its hexadecimal string representation. The returned string is a
     * concatenation of characters from '0' to '9' and 'a' to 'f'.
     *
     * @param l the long value to convert.
     * @return the hexadecimal string representation of {@code l}.
     */
    public static String toHexString(long l) {
        int count = 1;
        long j = l;

        if (l < 0) {
            count = 16;
        } else {
            while ((j >>= 4) != 0) {
                count++;
            }
        }

        char[] buffer = new char[count];
        do {
            int t = (int) (l & 15);
            if (t > 9) {
                t = t - 10 + 'a';
            } else {
                t += '0';
            }
            buffer[--count] = (char) t;
            l >>= 4;
        } while (count > 0);
        return new String(buffer);
    }

    /**
     * Converts the specified long value into its decimal string representation. The returned string is a concatenation
     * of a minus sign if the number is negative and characters from '0' to '9'.
     *
     * @param l the long to convert.
     * @return the decimal string representation of {@code l}.
     */
    public static String toString(long l) {
        if (l == 0) {
            return "0";
        }

        int count = 2;
        long j = l;
        boolean negative = l < 0;
        if (!negative) {
            count = 1;
            j = -l;
        }
        while ((l /= 10) != 0) {
            count++;
        }

        char[] buffer = new char[count];
        do {
            int ch = 0 - (int) (j % 10);
            ch += '0';
            buffer[--count] = (char) ch;
        } while ((j /= 10) != 0);
        if (negative) {
            buffer[0] = '-';
        }
        return new String(buffer);
    }

    /**
     * Parses the specified string as a signed decimal long value.
     *
     * @param string the string representation of a long value.
     * @return a {@code Long} instance containing the long value represented by {@code string}.
     * @throws InvalidFormatException if {@code string} is {@code null}, has a length of zero or can not be parsed as a
     *                                long value.
     * @see #parseLong(String)
     */
    public static Long valueOf(String string) throws InvalidFormatException {
        return parseLong(string);
    }

    /**
     * Parses the specified string as a signed long value using the specified radix.
     *
     * @param string the string representation of a long value.
     * @param radix  the radix to use when parsing.
     * @return a {@code Long} instance containing the long value represented by {@code string} using {@code radix}.
     * @throws InvalidFormatException if {@code string} is {@code null} or has a length of zero, {@code radix <
     *                                Character.MIN_RADIX}, {@code radix > Character.MAX_RADIX}, or if {@code string}
     *                                can not be parsed as a long value.
     * @see #parseLong(String, int)
     */
    public static Long valueOf(String string, int radix) throws InvalidFormatException {
        return parseLong(string, radix);
    }

    /**
     * Determines the highest (leftmost) bit of the specified long value that is 1 and returns the bit mask value for
     * that bit. This is also referred to as the Most Significant 1 Bit. Returns zero if the specified long is zero.
     *
     * @param lng the long to examine.
     * @return the bit mask indicating the highest 1 bit in {@code lng}.
     * @since 1.5
     */
    public static long highestOneBit(long lng) {
        lng |= (lng >> 1);
        lng |= (lng >> 2);
        lng |= (lng >> 4);
        lng |= (lng >> 8);
        lng |= (lng >> 16);
        lng |= (lng >> 32);
        return (lng & ~(lng >>> 1));
    }

    /**
     * Determines the lowest (rightmost) bit of the specified long value that is 1 and returns the bit mask value for
     * that bit. This is also referred to as the Least Significant 1 Bit. Returns zero if the specified long is zero.
     *
     * @param lng the long to examine.
     * @return the bit mask indicating the lowest 1 bit in {@code lng}.
     * @since 1.5
     */
    public static long lowestOneBit(long lng) {
        return (lng & (-lng));
    }

    /**
     * Determines the number of leading zeros in the specified long value prior to the {@link #highestOneBit(long)
     * highest one bit}.
     *
     * @param lng the long to examine.
     * @return the number of leading zeros in {@code lng}.
     * @since 1.5
     */
    public static int numberOfLeadingZeros(long lng) {
        lng |= lng >> 1;
        lng |= lng >> 2;
        lng |= lng >> 4;
        lng |= lng >> 8;
        lng |= lng >> 16;
        lng |= lng >> 32;
        return bitCount(~lng);
    }

    /**
     * Determines the number of trailing zeros in the specified long value after the {@link #lowestOneBit(long) lowest
     * one bit}.
     *
     * @param lng the long to examine.
     * @return the number of trailing zeros in {@code lng}.
     * @since 1.5
     */
    public static int numberOfTrailingZeros(long lng) {
        return bitCount((lng & -lng) - 1);
    }

    /**
     * Counts the number of 1 bits in the specified long value; this is also referred to as population count.
     *
     * @param lng the long to examine.
     * @return the number of 1 bits in {@code lng}.
     * @since 1.5
     */
    public static int bitCount(long lng) {
        lng = (lng & 0x5555555555555555L) + ((lng >> 1) & 0x5555555555555555L);
        lng = (lng & 0x3333333333333333L) + ((lng >> 2) & 0x3333333333333333L);
        // adjust for 64-bit integer
        int i = (int) ((lng >>> 32) + lng);
        i = (i & 0x0F0F0F0F) + ((i >> 4) & 0x0F0F0F0F);
        i = (i & 0x00FF00FF) + ((i >> 8) & 0x00FF00FF);
        i = (i & 0x0000FFFF) + ((i >> 16) & 0x0000FFFF);
        return i;
    }

    /**
     * Rotates the bits of the specified long value to the left by the specified number of bits.
     *
     * @param lng      the long value to rotate left.
     * @param distance the number of bits to rotate.
     * @return the rotated value.
     * @since 1.5
     */
    public static long rotateLeft(long lng, int distance) {
        if (distance == 0) {
            return lng;
        }
        /*
         * According to JLS3, 15.19, the right operand of a shift is always
         * implicitly masked with 0x3F, which the negation of 'distance' is
         * taking advantage of.
         */
        return ((lng << distance) | (lng >>> (-distance)));
    }

    /**
     * <p/>
     * Rotates the bits of the specified long value to the right by the specified number of bits.
     *
     * @param lng      the long value to rotate right.
     * @param distance the number of bits to rotate.
     * @return the rotated value.
     * @since 1.5
     */
    public static long rotateRight(long lng, int distance) {
        if (distance == 0) {
            return lng;
        }
        /*
         * According to JLS3, 15.19, the right operand of a shift is always
         * implicitly masked with 0x3F, which the negation of 'distance' is
         * taking advantage of.
         */
        return ((lng >>> distance) | (lng << (-distance)));
    }

    /**
     * Reverses the order of the bytes of the specified long value.
     *
     * @param lng the long value for which to reverse the byte order.
     * @return the reversed value.
     * @since 1.5
     */
    public static long reverseBytes(long lng) {
        long b7 = lng >>> 56;
        long b6 = (lng >>> 40) & 0xFF00L;
        long b5 = (lng >>> 24) & 0xFF0000L;
        long b4 = (lng >>> 8) & 0xFF000000L;
        long b3 = (lng & 0xFF000000L) << 8;
        long b2 = (lng & 0xFF0000L) << 24;
        long b1 = (lng & 0xFF00L) << 40;
        long b0 = lng << 56;
        return (b0 | b1 | b2 | b3 | b4 | b5 | b6 | b7);
    }

    /**
     * Reverses the order of the bits of the specified long value.
     *
     * @param lng the long value for which to reverse the bit order.
     * @return the reversed value.
     * @since 1.5
     */
    public static long reverse(long lng) {
        // From Hacker's Delight, 7-1, Figure 7-1
        lng = (lng & 0x5555555555555555L) << 1 | (lng >> 1)
                & 0x5555555555555555L;
        lng = (lng & 0x3333333333333333L) << 2 | (lng >> 2)
                & 0x3333333333333333L;
        lng = (lng & 0x0F0F0F0F0F0F0F0FL) << 4 | (lng >> 4)
                & 0x0F0F0F0F0F0F0F0FL;
        return reverseBytes(lng);
    }
}
