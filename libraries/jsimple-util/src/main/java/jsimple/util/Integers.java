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

/**
 * The wrapper for the primitive type {@code int}.
 * <p/>
 * As with the specification, this implementation relies on code laid out in <a href="http://www.hackersdelight.org/">Henry
 * S. Warren, Jr.'s Hacker's Delight, (Addison Wesley, 2002)</a> as well as <a href="http://aggregate.org/MAGIC/">The
 * Aggregate's Magic Algorithms</a>.
 *
 * @see java.lang.Number
 * @since 1.1
 */
public final class Integers {
    /**
     * Constant for the maximum {@code int} value, 2<sup>31</sup>-1.
     */
    public static final int MAX_VALUE = 0x7FFFFFFF;

    /**
     * Constant for the minimum {@code int} value, -2<sup>31</sup>.
     */
    public static final int MIN_VALUE = 0x80000000;

    /**
     * The minimum radix used for conversions between characters and integers.
     */
    public static final int MIN_RADIX = 2;

    /**
     * The maximum radix used for conversions between characters and integers.
     */
    public static final int MAX_RADIX = 36;

    /**
     * Parses the specified string and returns an {@code int} if the string can be decoded into an integer
     * value. The string may be an optional minus sign "-" followed by a hexadecimal ("0x..." or "#..."), octal
     * ("0..."), or decimal ("...") representation of an integer.
     *
     * @param string a string representation of an integer value.
     * @return an int for the value represented by {@code string}
     * @throws InvalidFormatException if {@code string} can not be parsed as an integer value
     */
    public static int decode(String string) throws InvalidFormatException {
        int length = string.length(), i = 0;
        if (length == 0) {
            throw new InvalidFormatException("Integer can't be empty string");
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
                return 0;
            }
            if ((firstDigit = string.charAt(i)) == 'x' || firstDigit == 'X') {
                if (++i == length) {
                    throw new InvalidFormatException(string);
                }
                base = 16;
            } else {
                base = 8;
            }
        } else if (firstDigit == '#') {
            if (++i == length) {
                throw new InvalidFormatException(string);
            }
            base = 16;
        }

        return parse(string, i, base, negative);
    }

    /**
     * Parses the specified string as a signed decimal integer value. The ASCII character \u002d ('-') is recognized as
     * the minus sign.
     *
     * @param string the string representation of an integer value.
     * @return the primitive integer value represented by {@code string}.
     * @throws InvalidFormatException if {@code string} cannot be parsed as an integer value.
     */
    public static int parseInt(String string) throws InvalidFormatException {
        return parseInt(string, 10);
    }

    /**
     * Parses the specified string as a signed integer value using the specified radix. The ASCII character \u002d ('-')
     * is recognized as the minus sign.
     *
     * @param string the string representation of an integer value.
     * @param radix  the radix to use when parsing.
     * @return the primitive integer value represented by {@code string} using {@code radix}.
     * @throws InvalidFormatException if {@code string} is {@code null} or has a length of zero, {@code radix <
     *                                Character.MIN_RADIX}, {@code radix > Character.MAX_RADIX}, or if {@code string}
     *                                can not be parsed as an integer value.
     */
    public static int parseInt(String string, int radix) throws InvalidFormatException {
        if (string == null || radix < MIN_RADIX || radix > MAX_RADIX) {
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

    private static int parse(String string, int offset, int radix, boolean negative) throws InvalidFormatException {
        int max = MIN_VALUE / radix;
        int result = 0, length = string.length();
        while (offset < length) {
            int parsedDigit = digit(string.charAt(offset++), radix);
            if (max > result) {
                throw new InvalidFormatException(string);
            }
            int next = result * radix - parsedDigit;
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
     * Converts the specified integer into its binary string representation. The returned string is a concatenation of
     * '0' and '1' characters.
     *
     * @param i the integer to convert.
     * @return the binary string representation of {@code i}.
     */
    public static String toBinaryString(int i) {
        int count = 1, j = i;

        if (i < 0) {
            count = 32;
        } else {
            while ((j = j >>> 1) != 0) {
                count++;
            }
        }

        char[] buffer = new char[count];
        do {
            buffer[--count] = (char) ((i & 1) + '0');
            i = i >>> 1;
        } while (count > 0);
        return new String(buffer);
    }

    /**
     * Converts the specified integer into its hexadecimal string representation. The returned string is a concatenation
     * of characters from '0' to '9' and 'a' to 'f'.
     *
     * @param i the integer to convert.
     * @return the hexadecimal string representation of {@code i}.
     */
    public static String toHexString(int i) {
        int count = 1, j = i;

        if (i < 0) {
            count = 8;
        } else {
            while ((j = j >>> 4) != 0) {
                count++;
            }
        }

        char[] buffer = new char[count];
        do {
            int t = i & 15;
            if (t > 9) {
                t = t - 10 + 'a';
            } else {
                t += '0';
            }
            buffer[--count] = (char) t;
            i = i >>> 4;
        } while (count > 0);
        return new String(buffer);
    }

    public static String toDecimalString(int value) {
        if (value == 0) {
            return "0";
        }

        int count = 2, j = value;
        boolean negative = value < 0;
        if (!negative) {
            count = 1;
            j = -value;
        }
        while ((value /= 10) != 0) {
            count++;
        }

        char[] buffer = new char[count];
        do {
            int ch = 0 - (j % 10);
            ch += '0';
            buffer[--count] = (char) ch;
        } while ((j /= 10) != 0);
        if (negative) {
            buffer[0] = '-';
        }
        return new String(buffer);
    }

    /**
     * Converts the specified integer into its decimal string representation. The returned string is a concatenation of
     * a minus sign if the number is negative and characters from '0' to '9'.
     *
     * @param value the integer to convert.
     * @return the decimal string representation of {@code value}.
     */
    public static String toString(int value) {
        if (value == 0) {
            return "0";
        }

        int count = 2, j = value;
        boolean negative = value < 0;
        if (!negative) {
            count = 1;
            j = -value;
        }
        while ((value /= 10) != 0) {
            count++;
        }

        char[] buffer = new char[count];
        do {
            int ch = 0 - (j % 10);
            ch += '0';
            buffer[--count] = (char) ch;
        } while ((j /= 10) != 0);
        if (negative) {
            buffer[0] = '-';
        }
        return new String(buffer);
    }

    /**
     * Determines the highest (leftmost) bit of the specified integer that is 1 and returns the bit mask value for that
     * bit. This is also referred to as the Most Significant 1 Bit. Returns zero if the specified integer is zero.
     *
     * @param i the integer to examine.
     * @return the bit mask indicating the highest 1 bit in {@code i}.
     * @since 1.5
     */
    public static int highestOneBit(int i) {
        i |= (i >> 1);
        i |= (i >> 2);
        i |= (i >> 4);
        i |= (i >> 8);
        i |= (i >> 16);
        return (i & ~(i >>> 1));
    }

    /**
     * Determines the lowest (rightmost) bit of the specified integer that is 1 and returns the bit mask value for that
     * bit. This is also referred to as the Least Significant 1 Bit. Returns zero if the specified integer is zero.
     *
     * @param i the integer to examine.
     * @return the bit mask indicating the lowest 1 bit in {@code i}.
     * @since 1.5
     */
    public static int lowestOneBit(int i) {
        return (i & (-i));
    }

    /**
     * Determines the number of leading zeros in the specified integer prior to the {@link #highestOneBit(int) highest
     * one bit}.
     *
     * @param i the integer to examine.
     * @return the number of leading zeros in {@code i}.
     * @since 1.5
     */
    public static int numberOfLeadingZeros(int i) {
        i |= i >> 1;
        i |= i >> 2;
        i |= i >> 4;
        i |= i >> 8;
        i |= i >> 16;
        return bitCount(~i);
    }

    /**
     * Determines the number of trailing zeros in the specified integer after the {@link #lowestOneBit(int) lowest one
     * bit}.
     *
     * @param i the integer to examine.
     * @return the number of trailing zeros in {@code i}.
     * @since 1.5
     */
    public static int numberOfTrailingZeros(int i) {
        return bitCount((i & -i) - 1);
    }

    /**
     * Counts the number of 1 bits in the specified integer; this is also referred to as population count.
     *
     * @param i the integer to examine.
     * @return the number of 1 bits in {@code i}.
     * @since 1.5
     */
    public static int bitCount(int i) {
        i -= ((i >> 1) & 0x55555555);
        i = (i & 0x33333333) + ((i >> 2) & 0x33333333);
        i = (((i >> 4) + i) & 0x0F0F0F0F);
        i += (i >> 8);
        i += (i >> 16);
        return (i & 0x0000003F);
    }

    /**
     * Rotates the bits of the specified integer to the left by the specified number of bits.
     *
     * @param i        the integer value to rotate left.
     * @param distance the number of bits to rotate.
     * @return the rotated value.
     * @since 1.5
     */
    public static int rotateLeft(int i, int distance) {
        if (distance == 0) {
            return i;
        }
        /*
         * According to JLS3, 15.19, the right operand of a shift is always
         * implicitly masked with 0x1F, which the negation of 'distance' is
         * taking advantage of.
         */
        return ((i << distance) | (i >>> (-distance)));
    }

    /**
     * Rotates the bits of the specified integer to the right by the specified number of bits.
     *
     * @param i        the integer value to rotate right.
     * @param distance the number of bits to rotate.
     * @return the rotated value.
     * @since 1.5
     */
    public static int rotateRight(int i, int distance) {
        if (distance == 0) {
            return i;
        }
        /*
         * According to JLS3, 15.19, the right operand of a shift is always
         * implicitly masked with 0x1F, which the negation of 'distance' is
         * taking advantage of.
         */
        return ((i >>> distance) | (i << (-distance)));
    }

    /**
     * Reverses the order of the bytes of the specified integer.
     *
     * @param i the integer value for which to reverse the byte order.
     * @return the reversed value.
     * @since 1.5
     */
    public static int reverseBytes(int i) {
        int b3 = i >>> 24;
        int b2 = (i >>> 8) & 0xFF00;
        int b1 = (i & 0xFF00) << 8;
        int b0 = i << 24;
        return (b0 | b1 | b2 | b3);
    }

    /**
     * Reverses the order of the bits of the specified integer.
     *
     * @param i the integer value for which to reverse the bit order.
     * @return the reversed value.
     * @since 1.5
     */
    public static int reverse(int i) {
        // From Hacker's Delight, 7-1, Figure 7-1
        i = (i & 0x55555555) << 1 | (i >> 1) & 0x55555555;
        i = (i & 0x33333333) << 2 | (i >> 2) & 0x33333333;
        i = (i & 0x0F0F0F0F) << 4 | (i >> 4) & 0x0F0F0F0F;
        return reverseBytes(i);
    }

    /**
     * Determine the value of the specified character {@code c} in the supplied radix. The value of {@code radix} must
     * be between 2 and 36.   If the character isn't a digit/letter or the value is outside the specified radix (e.g.
     * it's an 'f' when the radix is 10) then an InvalidFormatException is thrown.   Only ASCII characters (that is,
     * Arabic number characters 0-9 and letters a-z / A-Z) are supported.
     *
     * @param c     the character to determine the value of.
     * @param radix the radix.
     * @return the value of {@code c} in {@code radix} if {@code radix} lies between {@link #MIN_RADIX} and {@link
     * #MAX_RADIX}
     * @throws InvalidFormatException if character isn't digit/letter or returned value would be outside specified
     *                                radix
     */
    public static int digit(char c, int radix) {
        int result;
        if ('0' <= c && c <= '9') {
            result = c - '0';
        } else if ('a' <= c && c <= 'z') {
            result = c - ('a' - 10);
        } else if ('A' <= c && c <= 'Z') {
            result = c - ('A' - 10);
        } else throw new InvalidFormatException("Invalid digit character: {}", c);

        if (result < radix)
            return result;
        else throw new InvalidFormatException("Digit {} is invalid for specified radix {}", c, radix);
    }
}
