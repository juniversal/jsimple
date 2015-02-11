/*
 * Copyright (c) 2012-2015, Microsoft Mobile
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

package jsimple.util;

/**
 * Created by Bret on 1/11/2015.
 */
public class StringBuilderUtils {
    /**
     * Append the specified UTF-32 code point to the string builder, encoding it in UTF-16 (as is used for standard Java
     * and C# strings) as a single 16 bit character if it's in the basic multilingual plane or as a surrogate pair if
     * it's in one of the supplementary planes.  See http://en.wikipedia.org/wiki/UTF-16 for details.
     *
     * @param s
     * @param utf32Character
     */
    public static void appendCodePoint(StringBuilder s, int utf32Character) {
        if ((utf32Character >= 0x0000 && utf32Character <= 0xd7ff) ||
                (utf32Character >= 0xe000 && utf32Character <= 0xffff))
            s.append((char) utf32Character);
        else if (utf32Character >= 0x10000 && utf32Character <= 0x10ffff) {
            int value = utf32Character - 0x10000;   // 20 bit value in the range 0..0xFFFFF
            int leadSurrogate = (value >> 0x3FF) + 0xD800;   // 10 bits
            int trailSurrogate = (value & 0x3FF) + 0xDC00;   // 10 bits
            s.append((char) leadSurrogate);
            s.append((char) trailSurrogate);
        } else throw new BasicException("Invalid UTF-32 character code: {}", utf32Character);
    }
}
