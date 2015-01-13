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
 * The wrapper for the primitive type {@code char}. This class also provides a number of utility methods for working
 * with characters.
 * <p/>
 * Character data is based upon the Unicode Standard, 4.0. The Unicode specification, character tables and other
 * information are available at <a href="http://www.unicode.org/">http://www.unicode.org/</a>.
 * <p/>
 * Unicode characters are referred to as <i>code points</i>. The range of valid code points is U+0000 to U+10FFFF. The
 * <i>Basic Multilingual Plane (BMP)</i> is the code point range U+0000 to U+FFFF. Characters above the BMP are referred
 * to as <i>Supplementary Characters</i>. On the Java platform, UTF-16 encoding and {@code char} pairs are used to
 * represent code points in the supplementary range. A pair of {@code char} values that represent a supplementary
 * character are made up of a <i>high surrogate</i> with a value range of 0xD800 to 0xDBFF and a <i>low surrogate</i>
 * with a value range of 0xDC00 to 0xDFFF.
 * <p/>
 * On the Java platform a {@code char} value represents either a single BMP code point or a UTF-16 unit that's part of a
 * surrogate pair. The {@code int} type is used to represent all Unicode code points.
 *
 * @since 1.0
 */
public final class CharacterUtil {
    /**
     * Indicates whether the specified character is a Unicode space character. That is, if it is a member of one of the
     * Unicode categories Space Separator, Line Separator, or Paragraph Separator.
     *
     * @param c the character to check.
     * @return {@code true} if {@code c} is a Unicode space character, {@code false} otherwise.
     */
    public static boolean isSpaceChar(char c) {
        if (c == 0x20 || c == 0xa0 || c == 0x1680) {
            return true;
        }
        if (c < 0x2000) {
            return false;
        }
        return c <= 0x200b || c == 0x2028 || c == 0x2029 || c == 0x202f || c == 0x3000;
    }

    /**
     * Reverses the order of the first and second byte in the specified character.
     *
     * @param c the character to reverse.
     * @return the character with reordered bytes.
     */
    public static char reverseBytes(char c) {
        return (char) ((c << 8) | (c >> 8));
    }

    /**
     * Converts the specified character to its string representation.
     *
     * @param value the character to convert
     * @return the character converted to a string
     */
    public static String toString(char value) {
        char[] charArray = { value };
        return new String(charArray);
    }
}
