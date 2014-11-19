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
 * This code was adapted from Apache Harmony (http://harmony.apache.org).
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

/**
 * This class was based on, and modified from, the Apache Harmony java.io.BufferedReader class.  This class supports
 * mark and reset, while the base JSimple Reader class doesn't; unlike standard Java there's no markSupported method
 * since the only Reader that may support mark/reset--BufferedReader--always supports it.  Also, unlike the standard
 * BufferedReader, this class isn't synchronized & thus isn't thread safe--the caller needs to synchronize if required.
 * Basically, this class has these purposes in life: (1) Add buffering, though that's not required as much since
 * Utf8InputStreamReader has a little 1K buffer built in and StringReader is one huge buffer (2) Add mark & reset
 * support, so you can "unread" stuff and (3) add readLine (which requires unreading the get the line terminators
 * right).
 * <p/>
 * Wraps an existing {@link Reader} and <em>buffers</em> the input. Expensive interaction with the underlying reader is
 * minimized, since most (smaller) requests can be satisfied by accessing the buffer alone. The drawback is that some
 * extra space is required to hold the buffer and that copying takes place when filling that buffer, but this is usually
 * outweighed by the performance benefits.
 * <p/>
 * <p/>A typical application pattern for the class looks like this:
 * <pre>
 * BufferedReader buf = new BufferedReader(new FileReader("file.java"));
 * </pre>
 */
public class BufferedReader extends Reader {
    private Reader reader;

    /**
     * The characters that can be read and refilled in bulk. We maintain three
     * indices into this buffer:<pre>
     *     { X X X X X X X X X X X X - - }
     *           ^     ^             ^
     *           |     |             |
     *         mark   pos           end</pre>
     * Pos points to the next readable character. End is one greater than the last readable character. When {@code pos
     * == end}, the buffer is empty and must be {@link #fillBuf() filled} before characters can be read.
     * <p/>
     * <p>Mark is the value pos will be set to on calls to {@link #reset}. Its value is in the range {@code [0...pos]}.
     * If the mark is {@code -1}, the buffer cannot be reset.
     * <p/>
     * <p>MarkLimit limits the distance between the mark and the pos. When this limit is exceeded, {@link #reset} is
     * permitted (but not required) to throw an exception. For shorter distances, {@link #reset} shall not throw (unless
     * the reader is closed).
     */
    private char[] buf;
    private int pos;
    private int end;
    private int markPos = -1;
    private int markLimit = -1;

    /**
     * Constructs a new BufferedReader on the Reader {@code in}. The buffer gets the default size (8 KB).
     *
     * @param reader the Reader that is buffered.
     */
    public BufferedReader(Reader reader) {
        this.reader = reader;
        buf = new char[8192];
    }

    /**
     * Constructs a new BufferedReader on the Reader {@code in}. The buffer size is specified by the parameter {@code
     * size}.
     *
     * @param reader the Reader that is buffered.
     * @param size   the size of the buffer to allocate.
     * @throws IllegalArgumentException if {@code size <= 0}.
     */
    public BufferedReader(Reader reader, int size) {
        this.reader = reader;
        buf = new char[size];
    }

    /**
     * Closes this reader. This implementation closes the buffered source reader and releases the buffer. Nothing is
     * done if this reader has already been closed.
     */
    @Override public void close() {
        if (!isClosed()) {
            reader.close();
            buf = null;
        }
    }

    /**
     * Populates the buffer with data. It is an error to call this method when the buffer still contains data; ie. if
     * {@code pos < end}.
     *
     * @return the number of bytes read into the buffer, or -1 if the end of the source stream has been reached.
     */
    private int fillBuf() {
        // assert(pos == end);

        if (markPos == -1 || (pos - markPos >= markLimit)) {
            /* mark isn't set or has exceeded its limit. use the whole buffer */
            int result = reader.read(buf, 0, buf.length);
            if (result > 0) {
                markPos = -1;
                pos = 0;
                end = result;
            }
            return result;
        }

        if (markPos == 0 && markLimit > buf.length) {
            /* the only way to make room when mark=0 is by growing the buffer */
            int newLength = buf.length * 2;
            if (newLength > markLimit) {
                newLength = markLimit;
            }
            char[] newbuf = new char[newLength];
            PlatformUtils.copyChars(buf, 0, newbuf, 0, buf.length);
            buf = newbuf;
        } else if (markPos > 0) {
            /* make room by shifting the buffered data to left mark positions */
            PlatformUtils.copyChars(buf, markPos, buf, 0, buf.length - markPos);
            pos -= markPos;
            end -= markPos;
            markPos = 0;
        }

        /* Set the new position and mark position */
        int count = reader.read(buf, pos, buf.length - pos);
        if (count != -1)
            end += count;

        return count;
    }

    /**
     * Indicates whether or not this reader is closed.
     *
     * @return {@code true} if this reader is closed, {@code false} otherwise.
     */
    private boolean isClosed() {
        return buf == null;
    }

    /**
     * Sets a mark position in this reader. The parameter {@code markLimit} indicates how many characters can be read
     * before the mark is invalidated. Calling {@code reset()} will reposition the reader back to the marked position if
     * {@code markLimit} has not been surpassed.
     *
     * @param markLimit the number of characters that can be read before the mark is invalidated.
     * @see #reset()
     */
    public void mark(int markLimit) {
        verifyNotClosed();
        this.markLimit = markLimit;
        markPos = pos;
    }

    /**
     * Reads a single character from this reader and returns it with the two higher-order bytes set to 0. If possible,
     * BufferedReader returns a character from the buffer. If there are no characters available in the buffer, it fills
     * the buffer and then returns a character. It returns -1 if there are no more characters in the source reader.
     *
     * @return the character read or -1 if the end of the source reader has been reached.
     * @throws IOException if this reader is closed or some other I/O error occurs.
     */
    @Override public int read() {
        verifyNotClosed();

        /* Are there buffered characters available? */
        if (pos < end || fillBuf() != -1)
            return buf[pos++];

        return -1;
    }

    /**
     * Reads at most {@code length} characters from this reader and stores them at {@code offset} in the character array
     * {@code buffer}. Returns the number of characters actually read or -1 if the end of the source reader has been
     * reached. If all the buffered characters have been used, a mark has not been set and the requested number of
     * characters is larger than this readers buffer size, BufferedReader bypasses the buffer and simply places the
     * results directly into {@code buffer}.
     *
     * @param buffer the character array to store the characters read.
     * @param offset the initial position in {@code buffer} to store the bytes read from this reader.
     * @param length the maximum number of characters to read, must be non-negative.
     * @return number of characters read or -1 if the end of the source reader has been reached.
     * @throws IndexOutOfBoundsException if {@code offset < 0} or {@code length < 0}, or if {@code offset + length} is
     *                                   greater than the size of {@code buffer}.
     */
    @Override public int read(char[] buffer, int offset, int length) {
        verifyNotClosed();

        if (offset < 0 || offset > buffer.length - length || length < 0)
            throw new IndexOutOfBoundsException();

        int outstanding = length;
        while (outstanding > 0) {

            /*
             * If there are bytes in the buffer, grab those first.
             */
            int available = end - pos;
            if (available > 0) {
                int count = available >= outstanding ? outstanding : available;
                PlatformUtils.copyChars(buf, pos, buffer, offset, count);
                pos += count;
                offset += count;
                outstanding -= count;
            }

            /*
             * Before attempting to read from the underlying stream, make
             * sure we really, really want to. We won't bother if we're
             * done, or if we've already got some bytes and reading from the
             * underlying stream would block.
             * [This used to also check ready() on the underlying reader, but
             * removed that in the JSimple implementation.
             */
            if (outstanding == 0)
                break;

            // assert(pos == end);

            /*
             * If we're unmarked and the requested size is greater than our
             * buffer, read the bytes directly into the caller's buffer. We
             * don't read into smaller buffers because that could result in
             * a many reads.
             */
            if ((markPos == -1 || (pos - markPos >= markLimit)) && outstanding >= buf.length) {
                int count = reader.read(buffer, offset, outstanding);
                if (count > 0) {
                    offset += count;
                    outstanding -= count;
                    markPos = -1;
                }

                break; // assume the source stream gave us all that it could
            }

            if (fillBuf() == -1)
                break; // source is exhausted
        }

        int amountRead = length - outstanding;
        return (amountRead > 0 || amountRead == length) ? amountRead : -1;
    }

    /**
     * Peeks at the next input character, refilling the buffer if necessary. If this character is a newline character
     * ("\n"), it is discarded.
     */
    final void chompNewline() {
        if ((pos != end || fillBuf() != -1)
                && buf[pos] == '\n') {
            pos++;
        }
    }

    /**
     * Returns the next line of text available from this reader. A line is represented by zero or more characters
     * followed by {@code '\n'}, {@code '\r'}, {@code "\r\n"} or the end of the reader. The string does not include the
     * newline sequence.
     *
     * @return the contents of the line or {@code null} if no characters were read before the end of the reader has been
     * reached.
     */
    public String readLine() {
        verifyNotClosed();

        /* has the underlying stream been exhausted? */
        if (pos == end && fillBuf() == -1)
            return null;

        for (int charPos = pos; charPos < end; charPos++) {
            char ch = buf[charPos];
            if (ch > '\r') {
                continue;
            }
            if (ch == '\n') {
                String res = new String(buf, pos, charPos - pos);
                pos = charPos + 1;
                return res;
            } else if (ch == '\r') {
                String res = new String(buf, pos, charPos - pos);
                pos = charPos + 1;
                if (((pos < end) || (fillBuf() != -1))
                        && (buf[pos] == '\n')) {
                    pos++;
                }
                return res;
            }
        }

        char eol = '\0';
        StringBuilder result = new StringBuilder(80);
            /* Typical Line Length */

        result.append(buf, pos, end - pos);
        while (true) {
            pos = end;

            /* Are there buffered characters available? */
            if (eol == '\n') {
                return result.toString();
            }
            // attempt to fill buffer
            if (fillBuf() == -1) {
                // characters or null.
                return result.length() > 0 || eol != '\0'
                        ? result.toString()
                        : null;
            }
            for (int charPos = pos; charPos < end; charPos++) {
                char c = buf[charPos];
                if (eol == '\0') {
                    if ((c == '\n' || c == '\r')) {
                        eol = c;
                    }
                } else if (eol == '\r' && c == '\n') {
                    if (charPos > pos) {
                        result.append(buf, pos, charPos - pos - 1);
                    }
                    pos = charPos + 1;
                    return result.toString();
                } else {
                    if (charPos > pos) {
                        result.append(buf, pos, charPos - pos - 1);
                    }
                    pos = charPos;
                    return result.toString();
                }
            }
            if (eol == '\0')
                result.append(buf, pos, end - pos);
            else
                result.append(buf, pos, end - pos - 1);
        }

    }

    /**
     * Resets this reader's position to the last {@code mark()} location. Invocations of {@code read()} and {@code
     * skip()} will occur from this new location.
     *
     * @throws IOException if this reader is closed or no mark has been set.
     * @see #mark(int)
     */
    public void reset() {
        verifyNotClosed();

        if (markPos == -1)
            throw new IOException("Attempt to call reset on BufferedReader when mark is invalid");

        pos = markPos;
    }

    /**
     * Skips {@code amount} characters in this reader. Subsequent {@code read()}s will not return these characters
     * unless {@code reset()} is used. Skipping characters may invalidate a mark if {@code markLimit} is surpassed.
     *
     * @param amount the maximum number of characters to skip.
     * @return the number of characters actually skipped.
     * @throws IllegalArgumentException if {@code amount < 0}.
     * @throws IOException              if this reader is closed or some other I/O error occurs.
     * @see #mark(int)
     * @see #reset()
     */
    public long skip(long amount) {
        if (amount < 0)
            throw new IllegalArgumentException();

        verifyNotClosed();
        if (amount < 1)
            return 0;

        if (end - pos >= amount) {
            pos += amount;
            return amount;
        }

        long read = end - pos;
        pos = end;
        while (read < amount) {
            if (fillBuf() == -1) {
                return read;
            }
            if (end - pos >= amount - read) {
                pos += amount - read;
                return amount;
            }
            // Couldn't get all the characters, skip what we read
            read += (end - pos);
            pos = end;
        }

        return amount;
    }

    private void verifyNotClosed() {
        if (isClosed())
            throw new IOException("BufferedReader is closed");
    }
}
