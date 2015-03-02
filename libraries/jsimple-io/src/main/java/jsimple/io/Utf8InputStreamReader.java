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

package jsimple.io;

import jsimple.util.ByteArrayRange;
import jsimple.util.InvalidFormatException;
import jsimple.util.ProgrammerError;

/**
 * This class was based on, and modified from, the Apache Harmony java.io.InputStreamWriter class.  Unlike the standard
 * Java Writer subclasses, this doesn't throw any checked exceptions.
 *
 * @author Bret Johnson
 * @see Utf8OutputStreamWriter
 * @since 10/7/12 6:29 PM
 */
public class Utf8InputStreamReader extends Reader {
    private InputStream inputStream;
    private byte[] srcBuffer = new byte[BUFFER_SIZE];          // src in original code
    private int srcPosition = BUFFER_SIZE;                     // s in original code
    private int srcEnd = BUFFER_SIZE;                          // src_actual_end in original code
    private int state = UTF8_ACCEPT;                           // 'state' in original code
    private boolean queuedSurrogatePair = false;               // Only set when there's a surrogate pair that wouldn't fit previously
    private int codePoint = 0;                                 // 'codepoint' & 'codep' in original code
    private static final int BUFFER_SIZE = 1024;

    /**
     * Constructs a new {@code InputStreamReader} on the {@link java.io.InputStream} {@code in}. This constructor sets
     * the character converter to the encoding specified in the "file.encoding" property and falls back to ISO 8859_1
     * (ISO-Latin-1) if the property doesn't exist.
     *
     * @param inputStream the input stream from which to read characters.
     */
    public Utf8InputStreamReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Utf8InputStreamReader(ByteArrayRange byteArrayRange) {
        this.inputStream = new ByteArrayInputStream(byteArrayRange);
    }

    /**
     * Closes this reader. This implementation closes the source InputStream.
     */
    @Override public void close() {
        inputStream.close();
    }

    /**
     * Reads a single character from this reader and returns it as an integer with the two higher-order bytes set to 0.
     * Returns -1 if the end of the reader has been reached. The byte value is either obtained from converting bytes in
     * this reader's buffer or by first filling the buffer from the source InputStream and then reading from the
     * buffer.
     *
     * @return the character read or -1 if the end of the reader has been reached
     */
    @Override public int read() {
        char[] buf = new char[1];
        return read(buf, 0, 1) != -1 ? buf[0] : -1;
    }


    private static final int UTF8_ACCEPT = 0;
    private static final int UTF8_REJECT = 1;

    // UTF-8 decoder lookup table
    static final int[] utf8d = {
            // 1st 256 entries (UTF-8 byte lookup)
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 00..1f
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 20..3f
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 40..5f
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 60..7f
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, // 80..9f
            7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, // a0..bf
            8, 8, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, // c0..df
            0xa, 0x3, 0x3, 0x3, 0x3, 0x3, 0x3, 0x3, 0x3, 0x3, 0x3, 0x3, 0x3, 0x4, 0x3, 0x3, // e0..ef
            0xb, 0x6, 0x6, 0x6, 0x5, 0x8, 0x8, 0x8, 0x8, 0x8, 0x8, 0x8, 0x8, 0x8, 0x8, 0x8, // f0..ff

            // Remaining 144 entries (next state)
            0x0, 0x1, 0x2, 0x3, 0x5, 0x8, 0x7, 0x1, 0x1, 0x1, 0x4, 0x6, 0x1, 0x1, 0x1, 0x1, // s0..s0
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, // s1..s2
            1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, // s3..s4
            1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, // s5..s6
            1, 3, 1, 1, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1  // s7..s8
    };

    /**
     * Reads at most {@code length} characters from this reader and stores them at position {@code offset} in the
     * character array {@code buf}. Returns the number of characters actually read or -1 if the end of the reader has
     * been reached. The bytes are either obtained from converting bytes in this reader's buffer or by first filling the
     * buffer from the source InputStream and then reading from the buffer.
     *
     * @param buffer the array to store the characters read
     * @param offset the initial position in {@code buf} to store the characters read from this reader
     * @param length the maximum number of characters to read
     * @return the number of characters read or -1 if the end of the reader has been reached
     * @throws InvalidFormatException if the stream isn't valid UTF-8
     */
    @Override public int read(char[] buffer, int offset, int length) {
        if (length < 0)
            throw new ProgrammerError("read length parameter can't be negative");

        /*
        The implementation of this method is adapted from the code here: http://bjoern.hoehrmann.de/utf-8/decoder/dfa/
        Basically, I took the "Transcoding to UTF-16 buffer" code and incorporated the performance enhancement
        below it preceded with "For the sample UTF-16 transcoder a more substantial increase in performance can be
        achieved by manually including the decode code in the inner loop; then it is also worthwhile to make code
        points in the US-ASCII range a special case".  This code should be fairly high performance, compared with
        alternatives.
        */

        //toUtf16(uint8_t* src, size_t srcBytes, uint16_t* dst, size_t dstWords, ...)

        char[] dest = buffer;               // 'dst' in original code
        int destIndex = offset;             // 'd' in original code
        int destEnd = offset + length;      // 1 past the index of the last char that should be added (dstWords in original code)

        // If there's a queued up surrogate pair, add it first (assuming there's room)
        if (queuedSurrogatePair) {
            if (destEnd - destIndex < 2)          // Return if no room
                return 0;

            dest[destIndex++] = (char) (0xD7C0 + (codePoint >> 10));      // High surrogate
            dest[destIndex++] = (char) (0xDC00 + (codePoint & 0x3FF));    // Low surrogate

            queuedSurrogatePair = false;
        }

        // Loop until we run out of space in the dest buffer or until we reach the end of the input stream
        while (true) {
            int destCharsFree = destEnd - destIndex;            // 'dst_words_free' in original code
            if (destCharsFree == 0)                             // If the dest buffer is full, we're done
                return destIndex - offset;

            // If there's no more room in the buffer, then fill it up again
            if (srcPosition == srcEnd) {
                int bytesRead = inputStream.read(srcBuffer);
                if (bytesRead < 1) {
                    if (bytesRead < 0) {                  // End of stream
                        if (state == UTF8_REJECT)
                            throw new InvalidFormatException("Invalid UTF-8 encoding--stream contains an invalid UTF-8 byte");
                        else break;
                    }

                    // If the stream for some reason returned 0 characters (not end of stream) when we asked to read
                    // BUFFER_SIZE characters, just return & let the caller call us again.  Hopefully, the stream will
                    // return something different in the future
                    return destIndex - offset;
                }

                srcPosition = 0;
                srcEnd = bytesRead;
            }

            // The original code subtracted one here, but we check the surrogate case specially below & don't need to;
            // that simplifies some things
            int srcCurrentEnd = srcPosition + destCharsFree;       // 'src_current_end' in original code

            if (srcEnd < srcCurrentEnd)
                srcCurrentEnd = srcEnd;

            // This inner loop is constructed to only need to do one bounds check, for performance
            while (srcPosition < srcCurrentEnd) {
                int srcByte = (int) srcBuffer[srcPosition++] & 0xFF;    // 'byte' in original code; mask makes it unsigned
                int type = utf8d[srcByte];                           // 'type' in original code

                if (state != UTF8_ACCEPT) {
                    codePoint = (codePoint << 6) | (srcByte & 63);
                    state = utf8d[256 + state * 16 + type];

                    if (state != UTF8_ACCEPT)
                        continue;
                } else if (srcByte > 0x7f) {
                    codePoint = (srcByte) & (255 >> type);
                    state = utf8d[256 + type];
                    continue;
                } else {
                    dest[destIndex++] = (char) srcByte;
                    continue;
                }

                if (codePoint > 0xffff) {
                    // If there's not enough room for both characters, return and get them both next time.  We never
                    // add just half of a surrogate
                    if (destEnd - destIndex < 2) {
                        queuedSurrogatePair = true;
                        return destIndex - offset;
                    }

                    // Decode Unicode supplementary characters
                    dest[destIndex++] = (char) (0xD7C0 + (codePoint >> 10));      // High surrogate
                    dest[destIndex++] = (char) (0xDC00 + (codePoint & 0x3FF));    // Low surrogate
                } else dest[destIndex++] = (char) codePoint;
            }
        }

        // At this point we've read until the end of the input stream

        if (state == UTF8_ACCEPT) {
            int charsRead = destIndex - offset;
            return charsRead == 0 ? -1 : charsRead;
        } else if (state == UTF8_REJECT)
            throw new InvalidFormatException("Invalid UTF-8 encoding--stream contains an invalid UTF-8 byte");
        else
            throw new InvalidFormatException("Invalid UTF-8 encoding--stream ends with a partially defined UTF-8 character");
    }
}
