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
import jsimple.lang.Math;

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
public class Latin1OutputStreamWriter extends Writer {
    private @Nullable OutputStream outputStream;
    private boolean closeOuterStream;
    private byte[] byteBuffer;

    private static final int BUFFER_SIZE = 1024;

    /**
     * Constructs a new OutputStreamWriter using {@code out} as the target stream to write converted characters to.
     *
     * @param outputStream the non-null target stream to write converted bytes to
     */
    public Latin1OutputStreamWriter(OutputStream outputStream) {
        this(outputStream, true);
    }

    /**
     * Constructs a new OutputStreamWriter using {@code out} as the target stream to write converted characters to.
     *
     * @param outputStream     the non-null target stream to write converted bytes to
     * @param closeOuterStream whether or not to close the outer stream when this stream is close
     */
    public Latin1OutputStreamWriter(OutputStream outputStream, boolean closeOuterStream) {
        this.outputStream = outputStream;
        byteBuffer = new byte[BUFFER_SIZE];
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
     * @throws IOException               if this writer has already been closed or another I/O error occurs.
     */
    @Override public void write(char[] buf, int offset, int count) {
        if (outputStream == null)
            throw new IOException("Can't call write on a Utf8OutputStreamWriter that's already closed");

        int totalBytesWritten = 0;
        while (totalBytesWritten < count) {
            int totalBytesRemaining = count - totalBytesWritten;
            int bytesToWrite = Math.min(totalBytesRemaining, BUFFER_SIZE);

            for (int i = 0; i < bytesToWrite; i++)
                byteBuffer[offset + totalBytesWritten + i] = (byte) buf[i];

            outputStream.write(byteBuffer, 0, bytesToWrite);

            totalBytesWritten += bytesToWrite;
        }
    }
}
