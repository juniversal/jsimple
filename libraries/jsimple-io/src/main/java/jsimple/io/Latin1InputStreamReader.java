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
import jsimple.util.ProgrammerError;
import jsimple.lang.Math;

public class Latin1InputStreamReader extends Reader {
    private InputStream inputStream;
    private byte[] byteBuffer = new byte[BUFFER_SIZE];

    private static final int BUFFER_SIZE = 1024;

    /**
     * Constructs a new {@code InputStreamReader} on the {@link java.io.InputStream} {@code in}. This constructor sets
     * the character converter to the encoding specified in the "file.encoding" property and falls back to ISO 8859_1
     * (ISO-Latin-1) if the property doesn't exist.
     *
     * @param inputStream the input stream from which to read characters.
     */
    public Latin1InputStreamReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Latin1InputStreamReader(ByteArrayRange byteArrayRange) {
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
     * Returns -1 if the end of the reader has been reached.
     *
     * @return the character read or -1 if the end of the reader has been reached
     * @throws jsimple.io.IOException if this reader is closed or some other I/O error occurs
     */
    @Override public int read() {
        return inputStream.read();
    }

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
     * @throws jsimple.io.CharConversionException if the stream isn't valid UTF-8
     */
    @Override public int read(char[] buffer, int offset, int length) {
        if (length < 0)
            throw new ProgrammerError("read length parameter can't be negative");

        int totalBytesRead = 0;
        while (totalBytesRead < length) {
            int totalBytesRemaining = length - totalBytesRead;
            int bytesToRead = Math.min(totalBytesRemaining, BUFFER_SIZE);
            int bytesRead = inputStream.read(byteBuffer, 0, bytesToRead);

            if (bytesRead > 0) {
                for (int i = 0; i < bytesRead; i++)
                    buffer[offset + totalBytesRead + i] = (char) byteBuffer[i];
                totalBytesRead += bytesRead;
            }

            if (bytesRead != bytesToRead)
                return totalBytesRead > 0 ? totalBytesRead : bytesRead;
        }

        return totalBytesRead;
    }
}
