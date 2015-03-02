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
 * This class was based on, and modified from, the Apache Harmony java.util.StringTokenizer class.  Unlike the standard
 * Java class, this class returns BasicException instead of NoSuchElementException when there are no more tokens & the
 * caller didn't properly check for that case first (via calling hasMoreElements).  Other that that pretty minor
 * difference, it's the same as the standard Java class.
 * <p>
 * The {@code StringTokenizer} class allows an application to break a string into tokens by performing code point
 * comparison. The {@code StringTokenizer} methods do not distinguish among identifiers, numbers, and quoted strings,
 * nor do they recognize and skip comments.
 * <p>
 * The set of delimiters (the codepoints that separate tokens) may be specified either at creation time or on a
 * per-token basis.
 * <p>
 * An instance of {@code StringTokenizer} behaves in one of three ways, depending on whether it was created with the
 * {@code returnDelimiters} flag having the value {@code true} or {@code false}: <ul> <li>If returnDelims is {@code
 * false}, delimiter code points serve to separate tokens. A token is a maximal sequence of consecutive code points that
 * are not delimiters. <li>If returnDelims is {@code true}, delimiter code points are themselves considered to be
 * tokens. In this case a token will be received for each delimiter code point. </ul>
 * <p>
 * A token is thus either one delimiter code point, or a maximal sequence of consecutive code points that are not
 * delimiters.
 * <p>
 * A {@code StringTokenizer} object internally maintains a current position within the string to be tokenized. Some
 * operations advance this current position past the code point processed.
 * <p>
 * A token is returned by taking a substring of the string that was used to create the {@code StringTokenizer} object.
 * <p>
 * Here's an example of the use of the default delimiter {@code StringTokenizer} : <blockquote>
 * <p>
 * <pre>
 * StringTokenizer st = new StringTokenizer(&quot;this is a test&quot;);
 * while (st.hasMoreTokens()) {
 *     println(st.nextToken());
 * }
 * </pre>
 * <p>
 * </blockquote>
 * <p>
 * This prints the following output: <blockquote>
 * <p>
 * <pre>
 *     this
 *     is
 *     a
 *     test
 * </pre>
 * <p>
 * </blockquote>
 * <p>
 * Here's an example of how to use a {@code StringTokenizer} with a user specified delimiter: <blockquote>
 * <p>
 * <pre>
 * StringTokenizer st = new StringTokenizer(
 *         &quot;this is a test with supplementary characters \ud800\ud800\udc00\udc00&quot;,
 *         &quot; \ud800\udc00&quot;);
 * while (st.hasMoreTokens()) {
 *     println(st.nextToken());
 * }
 * </pre>
 * <p>
 * </blockquote>
 * <p>
 * This prints the following output: <blockquote>
 * <p>
 * <pre>
 *     this
 *     is
 *     a
 *     test
 *     with
 *     supplementary
 *     characters
 *     \ud800
 *     \udc00
 * </pre>
 * <p>
 * </blockquote>
 */
public class StringTokenizer {
    private String string;
    private String delimiters;
    private boolean returnDelimiters;
    private int position;

    /**
     * Constructs a new {@code StringTokenizer} for the parameter string using whitespace as the delimiter. The {@code
     * returnDelimiters} flag is set to {@code false}.
     *
     * @param string the string to be tokenized.
     */
    public StringTokenizer(String string) {
        this(string, " \t\n\r\f", false);
    }

    /**
     * Constructs a new {@code StringTokenizer} for the parameter string using the specified delimiters. The {@code
     * returnDelimiters} flag is set to {@code false}.
     *
     * @param string     the string to be tokenized.
     * @param delimiters the delimiters to use.
     */
    public StringTokenizer(String string, String delimiters) {
        this(string, delimiters, false);
    }

    /**
     * Constructs a new {@code StringTokenizer} for the parameter string using the specified delimiters, returning the
     * delimiters as tokens if the parameter {@code returnDelimiters} is {@code true}.
     *
     * @param string           the string to be tokenized.
     * @param delimiters       the delimiters to use.
     * @param returnDelimiters {@code true} to return each delimiter as a token.
     */
    public StringTokenizer(String string, String delimiters, boolean returnDelimiters) {
        if (string == null || delimiters == null)
            throw new ProgrammerError("string and delimiters can't be null");

        this.string = string;
        this.delimiters = delimiters;
        this.returnDelimiters = returnDelimiters;
        this.position = 0;
    }

    /**
     * Returns the number of unprocessed tokens remaining in the string.
     *
     * @return number of tokens that can be retrieved before an {@code Exception} will result from a call to {@code
     * nextToken()}.
     */
    public int countTokens() {
        int count = 0;
        boolean inToken = false;
        for (int i = position, length = string.length(); i < length; i++) {
            if (delimiters.indexOf(string.charAt(i), 0) >= 0) {
                if (returnDelimiters)
                    count++;
                if (inToken) {
                    count++;
                    inToken = false;
                }
            } else {
                inToken = true;
            }
        }
        if (inToken)
            count++;
        return count;
    }

    /**
     * Returns {@code true} if unprocessed tokens remain. This method is implemented in order to satisfy the {@code
     * Enumeration} interface.
     *
     * @return {@code true} if unprocessed tokens remain.
     */
    public boolean hasMoreElements() {
        return hasMoreTokens();
    }

    /**
     * Returns {@code true} if unprocessed tokens remain.
     */
    public boolean hasMoreTokens() {
        int length = string.length();
        if (position < length) {
            if (returnDelimiters)
                return true; // there is at least one character and even if it is a delimiter it is a token

            // otherwise find a character which is not a delimiter
            for (int i = position; i < length; i++)
                if (delimiters.indexOf(string.charAt(i), 0) == -1)
                    return true;
        }
        return false;
    }

    /**
     * Returns the next token in the string as an {@code Object}. This method is implemented in order to satisfy the
     * {@code Enumeration} interface.
     *
     * @return next token in the string as an {@code Object}
     * @throws BasicException if no tokens remain.
     */
    public Object nextElement() {
        return nextToken();
    }

    /**
     * Returns the next token in the string as a {@code String}.
     *
     * @return next token in the string as a {@code String}.
     * @throws BasicException if no tokens remain.
     */
    public String nextToken() {
        int i = position;
        int length = string.length();

        if (i < length) {
            if (returnDelimiters) {
                // TODO: Test this if-statement, as it was a change to the original code
                if (delimiters.indexOf(string.charAt(position), 0) >= 0) {
                    ++position;
                    return string.substring(position - 1, position);
                }
                for (position++; position < length; position++)
                    if (delimiters.indexOf(string.charAt(position), 0) >= 0)
                        return string.substring(i, position);
                return string.substring(i);
            }

            while (i < length && delimiters.indexOf(string.charAt(i), 0) >= 0)
                i++;
            position = i;
            if (i < length) {
                for (position++; position < length; position++)
                    if (delimiters.indexOf(string.charAt(position), 0) >= 0)
                        return string.substring(i, position);
                return string.substring(i);
            }
        }

        throw new BasicException("No more tokens");
    }

    /**
     * Returns the next token in the string as a {@code String}. The delimiters used are changed to the specified
     * delimiters.
     *
     * @param delims the new delimiters to use.
     * @return next token in the string as a {@code String}.
     * @throws BasicException if no tokens remain.
     */
    public String nextToken(String delims) {
        this.delimiters = delims;
        return nextToken();
    }
}
