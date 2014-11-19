/*
 * Copyright (c) 2012-2014, Microsoft Mobile
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
 *
 *
 * This code was adapted from Apache Harmony (http://http://harmony.apache.org).
 * The original Apache Harmony copyright is below.
 *
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package jsimple.net;

import jsimple.io.IOUtils;
import jsimple.util.ByteArrayRange;
import jsimple.util.TextualPath;

/**
 * @author Bret Johnson
 * @since 11/25/12 9:03 PM
 */
public class UrlEncoder {
    /**
     * Encodes the given string {@code s} in a x-www-form-urlencoded string using UTF8 character encoding.
     * <p/>
     * All characters except letters ('a'..'z', 'A'..'Z') and numbers ('0'..'9') and characters '.', '-', '*', '_' are
     * converted into their hexadecimal value prepended by '%'. For example: '#' -> %23. In addition, spaces are
     * substituted by '+'
     *
     * @param s the string to be encoded.
     * @return the encoded string.
     * @throws java.io.UnsupportedEncodingException if the specified encoding scheme is invalid.
     */
    public static String encode(String s) {
        // Guess a bit bigger for encoded form
        StringBuilder buffer = new StringBuilder(s.length() + 16);
        int start = -1;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') ||
                    (ch >= '0' && ch <= '9') || " .-*_".indexOf(ch) > -1) {
                if (start >= 0) {
                    convert(s.substring(start, i), buffer);
                    start = -1;
                }
                if (ch != ' ')
                    buffer.append(ch);
                else buffer.append('+');
            } else {
                if (start < 0) {
                    start = i;
                }
            }
        }

        if (start >= 0)
            convert(s.substring(start, s.length()), buffer);

        return buffer.toString();
    }

    public static String encode(TextualPath path) {
        if (path.isEmpty())
            return "/";
        else {
            StringBuilder encodedPath = new StringBuilder();
            for (String pathElement : path.getPathElements()) {
                encodedPath.append("/");
                encodedPath.append(encode(pathElement));
            }
            return encodedPath.toString();
        }
    }

    private static final String digits = "0123456789ABCDEF";

    private static void convert(String s, StringBuilder buffer) {
        ByteArrayRange byteArrayRange = IOUtils.toUtf8BytesFromString(s);
        int length = byteArrayRange.getLength();
        byte[] bytes = byteArrayRange.getBytes();

        for (int j = byteArrayRange.getPosition(); j < length; j++) {
            buffer.append('%');
            byte currByte = bytes[j];
            buffer.append(digits.charAt((currByte & 0xf0) >> 4));
            buffer.append(digits.charAt(currByte & 0xf));
        }
    }
}
