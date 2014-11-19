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

package jsimple.io;

import jsimple.util.PlatformUtils;
import org.jetbrains.annotations.Nullable;

/**
 * This class was based on, and modified from, the Apache Harmony java.io.StringReader class.  Unlike the standard Java
 * StringReader class, this doesn't do locking, doesn't throw any checked exceptions, and doesn't support
 * mark/reset/skip.
 * <p/>
 * A specialized {@link Reader} that reads characters from a {@code String} in a sequential manner.
 *
 * @see java.io.StringWriter
 */
public class StringReader extends Reader {
    private @Nullable String str;
    private int pos;
    private int count;

    /**
     * Construct a new {@code StringReader} with {@code str} as source. The size of the reader is set to the {@code
     * length()} of the string.
     *
     * @param str the source string for this reader.
     */
    public StringReader(String str) {
        super();
        this.str = str;
        this.count = str.length();
    }

    /**
     * Closes this reader. Once it is closed, read operations on this reader will throw an {@code IOException}. Only the
     * first invocation of this method has any effect.
     */
    @Override public void close() {
        str = null;
    }

    /**
     * Reads a single character from the source string and returns it as an integer with the two higher-order bytes set
     * to 0. Returns -1 if the end of the source string has been reached.
     *
     * @return the character read or -1 if the end of the source string has been reached
     * @throws IOException if this reader is closed
     */
    @Override public int read() {
        if (str == null)
            throw new IOException("Reader is closed");
        if (pos == count)
            return -1;
        return str.charAt(pos++);
    }

    /**
     * Reads at most {@code len} characters from the source string and stores them at {@code offset} in the character
     * array {@code buf}. Returns the number of characters actually read or -1 if the end of the source string has been
     * reached.
     *
     * @param buf    the character array to store the characters read
     * @param offset the initial position in {@code buffer} to store the characters read from this reader
     * @param length the maximum number of characters to read
     * @return the number of characters read or -1 if the end of the reader has been reached
     * @throws IndexOutOfBoundsException if {@code offset < 0} or {@code len < 0}, or if {@code offset + len} is greater
     *                                   than the size of {@code buf}.
     * @throws IOException               if this reader is closed
     */
    @Override public int read(char[] buf, int offset, int length) {
        if (str == null)
            throw new IOException("Reader is closed");
        if (length < 0)
            throw new RuntimeException("read length parameter can't be negative");
        if (pos == this.count)
            return -1;
        int end = pos + length > this.count ? this.count : pos + length;
        //TODO: Add test for this
        PlatformUtils.copyChars(str, pos, buf, offset, end - pos);
        //str.getChars(pos, end, buf, offset);
        int charsRead = end - pos;
        pos = end;
        return charsRead;
    }
}
