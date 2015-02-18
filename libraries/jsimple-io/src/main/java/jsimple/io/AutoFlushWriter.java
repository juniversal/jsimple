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

/**
 * Wraps an existing {@link jsimple.io.Writer} and performs some transformation on the
 * output data while it is being written. Transformations can be anything from a
 * simple byte-wise filtering output data to an on-the-fly compression or
 * decompression of the underlying writer. Writers that wrap another writer and
 * provide some additional functionality on top of it usually inherit from this
 * class.
 *
 * @see FilterReader
 */
public class AutoFlushWriter extends FilterWriter {
    /**
     * Constructs a new FilterWriter on the Writer {@code out}. All writes are
     * now filtered through this writer.
     *
     * @param out the target Writer to filter writes on.
     */
    public AutoFlushWriter(Writer out) {
        super(out);
    }

    /**
     * Writes {@code count} characters from the char array {@code buffer}
     * starting at position {@code offset} to the target writer.
     *
     * @param buffer the buffer to write.
     * @param offset the index of the first character in {@code buffer} to write.
     * @param count  the number of characters in {@code buffer} to write.
     * @throws jsimple.io.IOException if an error occurs while writing to this writer.
     */
    @Override public void write(char[] buffer, int offset, int count) {
        super.write(buffer, offset, count);
        flush();
    }

    /**
     * Writes the specified character {@code oneChar} to the target writer. Only the
     * two least significant bytes of the integer {@code oneChar} are written.
     *
     * @param oneChar the char to write to the target writer.
     * @throws jsimple.io.IOException if an error occurs while writing to this writer.
     */
    @Override public void write(int oneChar) throws IOException {
        super.write(oneChar);
        flush();
    }

    /**
     * Writes {@code count} characters from the string {@code str} starting at
     * position {@code index} to this writer. This implementation writes
     * {@code str} to the target writer.
     *
     * @param str    the string to be written.
     * @param offset the index of the first character in {@code str} to write.
     * @param count  the number of chars in {@code str} to write.
     * @throws jsimple.io.IOException if an error occurs while writing to this writer.
     */
    @Override public void write(String str, int offset, int count) {
        super.write(str, offset, count);
        flush();
    }
}
