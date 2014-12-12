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

package jsimple.net;

import jsimple.io.IOUtils;
import jsimple.util.StringTokenizer;
import jsimple.util.StringUtils;
import jsimple.util.TextualPath;

/**
 * This class was based on, and modified from, the Apache Harmony java.net.URLDecoder class.  Unlike the standard Java
 * class, it only supports UTF-8 character encoding (as is the W3C standard for URLs).
 *
 * @author Bret Johnson
 * @since 11/25/12 8:33 PM
 */
public class UrlDecoder {

    /**
     * Decodes the argument which is assumed to be encoded in the {@code x-www-form-urlencoded} MIME content type using
     * the specified encoding scheme.
     * <p/>
     * '+' will be converted to space, '%' and two following hex digit characters are converted to the equivalent byte
     * value. All other characters are passed through unmodified. For example "A+B+C %24%25" -> "A B C $%".
     *
     * @param s the encoded string
     * @return the decoded clear-text representation of the given string
     */
    public static String decode(String s) {
        if (s.indexOf('%') == -1) {
            if (s.indexOf('+') == -1)
                return s;

            char[] str = s.toCharArray();
            for (int i = 0; i < str.length; i++) {
                if (str[i] == '+')
                    str[i] = ' ';
            }

            return new String(str);
        } else return decodeEncodedString(s);
    }

    private static String decodeEncodedString(String s) {
        StringBuilder str_buf = new StringBuilder();
        //char str_buf[] = new char[s.length()];
        byte[] buf = new byte[s.length() / 3];

        for (int i = 0; i < s.length(); ) {
            char c = s.charAt(i);
            if (c == '+')
                str_buf.append(' ');
            else if (c == '%') {
                int len = 0;
                do {
                    if (i + 2 >= s.length())
                        throw new RuntimeException("Incomplete % sequence at: " + i);

                    int d1 = StringUtils.toByteFromHexCharacter(s.charAt(i + 1));
                    int d2 = StringUtils.toByteFromHexCharacter(s.charAt(i + 2));

                    buf[len++] = (byte) ((d1 << 4) + d2);
                    i += 3;
                } while (i < s.length() && s.charAt(i) == '%');

                str_buf.append(IOUtils.toStringFromUtf8Bytes(buf, 0, len));
                continue;
            } else str_buf.append(c);
            i++;
        }

        return str_buf.toString();
    }

    /**
     * Decode a path, delimited with "/" characters.  Note that decoding then looking for / characters isn't completely
     * correct--that produces the wrong result if elements of the path contact URL encoded / characters not intended to
     * be path delimiters.  That's one reason for this method's existence--it first finds the / characters then decodes
     * each element of the path, producing the correct result.
     *
     * @param pathString path string
     * @return TextualPath, holding a list of strings forming the elements of the path
     */
    public static TextualPath decodePath(String pathString) {
        TextualPath path = new TextualPath();

        StringTokenizer stringTokenizer = new StringTokenizer(pathString, "/");
        while (stringTokenizer.hasMoreTokens()) {
            String pathElement = decode(stringTokenizer.nextToken());
            path.add(pathElement);
        }

        return path;
    }
}
