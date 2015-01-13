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

import org.jetbrains.annotations.Nullable;

/**
 * This class was based on, and modified from, the Apache Harmony java.io.OutputStreamWriter class.  Unlike the standard
 * Java Writer subclasses, this doesn't throw any checked exceptions.
 * <p/>
 * A class for turning a character stream into a byte stream. Data written to the target writer is converted into bytes,
 * using UTF-8 encoding.
 *
 * @author Bret Johnson
 * @see Utf8InputStreamReader
 * @since 10/14/12 9:52 PM
 */
public class Utf8OutputStreamWriter extends Writer {
    private @Nullable OutputStream outputStream;
    private boolean closeOuterStream;
    private byte[] destBuffer;
    private int destPosition = 0;
    private int queuedLeadSurrogate = -1;

    private static final int BUFFER_SIZE = 1024;

    /**
     * Constructs a new OutputStreamWriter using {@code out} as the target stream to write converted characters to.
     *
     * @param outputStream the non-null target stream to write converted bytes to
     */
    public Utf8OutputStreamWriter(OutputStream outputStream) {
        this(outputStream, true);
    }

    /**
     * Constructs a new OutputStreamWriter using {@code out} as the target stream to write converted characters to.
     *
     * @param outputStream     the non-null target stream to write converted bytes to
     * @param closeOuterStream whether or not to close the outer stream when this stream is close
     */
    public Utf8OutputStreamWriter(OutputStream outputStream, boolean closeOuterStream) {
        this.outputStream = outputStream;
        destBuffer = new byte[BUFFER_SIZE];
        this.closeOuterStream = closeOuterStream;
    }

    /**
     * @throws IOException if an error occurs while closing this writer.
     * @Override protected void finalize() { close(); }
     * <p/>
     * /** Closes this writer. This implementation flushes the buffer as well as the target stream. The target stream is
     * then closed and the resources for the buffer and converter are released.
     * <p/>
     * Only the first invocation of this method has any effect. Subsequent calls do nothing.
     */
    @Override public void close() {
        // If already closed, do nothing
        if (outputStream == null)
            return;

        flush();

        assert outputStream != null : "@SuppressWarnings(nullness)";
        if (closeOuterStream)
            outputStream.close();
        outputStream = null;
    }

    /**
     * Flushes this writer. This implementation ensures that all buffered bytes are written to the target stream. After
     * writing the bytes, the target stream is flushed as well.
     *
     * @throws IOException if an error occurs while flushing this writer
     */
    @Override public void flush() {
        if (outputStream == null)
            throw new IOException("Can't call flush on a Utf8OutputStreamWriter that's already closed");

        if (destPosition > 0) {
            outputStream.write(destBuffer, 0, destPosition);
            destPosition = 0;
        }

        assert outputStream != null : "@SuppressWarnings(nullness)";
        outputStream.flush();
    }

    /**
     * Writes {@code count} characters starting at {@code offset} in {@code buf} to this writer. The characters are
     * immediately converted to bytes by the character converter and stored in a local buffer. If the buffer gets full
     * as a result of the conversion, this writer is flushed.
     *
     * @param buf    the array containing characters to write.
     * @param offset the index of the first character in {@code buf} to write.
     * @param count  the maximum number of characters to write.
     * @throws IndexOutOfBoundsException if {@code offset < 0} or {@code count < 0}, or if {@code offset + count} is
     *                                   greater than the size of {@code buf}.
     * @throws IOException               if this writer has already been closed or another I/O error occurs.
     */
    @Override public void write(char[] buf, int offset, int count) {
        if (outputStream == null)
            throw new IOException("Can't call write on a Utf8OutputStreamWriter that's already closed");

        int srcPosition = offset;
        int srcEnd = offset + count;
        while (srcPosition < srcEnd) {
            // If the buffer is approaching full (< 256 bytes left), then write it to the output stream.  In this way
            // the inner loop runs from 256 character iterations down to 64 character iterations, but never goes below
            // that when large chunks are being written.
            if (destBuffer.length - destPosition < 256) {
                assert outputStream != null : "@SuppressWarnings(nullness)";
                outputStream.write(destBuffer, 0, destPosition);
                destPosition = 0;
            }

            // Don't process any more source characters in the inner loop than will possibly fit in the buffer, encoded.
            // Worst case, each source character maps to 3 bytes, so only process floor(<bytes left in buffer> / 3).
            int srcCurrEnd = srcPosition + (destBuffer.length - destPosition) / 3;

            // Don't process more source characters than actually available
            if (srcCurrEnd > srcEnd)
                srcCurrEnd = srcEnd;

            while (srcPosition < srcCurrEnd) {
                char c = buf[srcPosition++];

                if (queuedLeadSurrogate != -1) {
                    int trailSurrogate = ((int) c) & 0xFFFF;

                    if (trailSurrogate < 0xDC00 || trailSurrogate > 0xDFFF)
                        throw new CharConversionException("Character " + trailSurrogate +
                                " unexpected; only a valid trail surrogate should come after a lead surrogate");

                    // Note, the Unicode scalar value n is defined as follows:
                    // n = (jchar-0xD800)*0x400+(jchar2-0xDC00)+0x10000
                    // Where jchar is a high-surrogate, jchar2 is a low-surrogate.
                    int n = (queuedLeadSurrogate << 10) + trailSurrogate + -56613888 /* 0xFCA02400 */;

                    destBuffer[destPosition++] = (byte) (0xF0 + ((n >> 18) & 0x07));
                    destBuffer[destPosition++] = (byte) (0x80 + ((n >> 12) & 0x3F));
                    destBuffer[destPosition++] = (byte) (0x80 + ((n >> 6) & 0x3F));
                    destBuffer[destPosition++] = (byte) (0x80 + (n & 0x3F));

                    queuedLeadSurrogate = -1;
                } else if (c <= 0x7F) {
                    destBuffer[destPosition++] = (byte) c;
                } else if (c <= 0x7FF) {
                    destBuffer[destPosition++] = (byte) (0xC0 + ((c >> 6) & 0x1F));
                    destBuffer[destPosition++] = (byte) (0x80 + (c & 0x3F));
                } else if (c >= 0xD800 && c <= 0xDFFF) {
                    int firstSurrogate = ((int) c) & 0xFFFF;
                    if (firstSurrogate > 0xDBFF)
                        throw new CharConversionException("Character " + firstSurrogate +
                                " unexpected; surrogate pair must start with a lead surrogate");

                    queuedLeadSurrogate = firstSurrogate;
                } else {
                    destBuffer[destPosition++] = (byte) (0xE0 + ((c >> 12) & 0x0F));
                    destBuffer[destPosition++] = (byte) (0x80 + ((c >> 6) & 0x3F));
                    destBuffer[destPosition++] = (byte) (0x80 + (c & 0x3F));
                }
            }
        }
    }
}
