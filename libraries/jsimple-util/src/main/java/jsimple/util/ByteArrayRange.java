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

package jsimple.util;

/**
 * This class represents a subset of a byte array, starting at the specified position & of the specified length.
 * Basically, it's used as a shortcut to passing around the array, position, and length separately.
 *
 * @author Bret Johnson
 * @since 8/3/13 10:46 AM
 */
public class ByteArrayRange {
    private byte[] bytes;
    private int position;
    private int length;

    public ByteArrayRange(byte[] bytes, int position, int length) {
        this.bytes = bytes;
        this.position = position;
        this.length = length;
    }

    public ByteArrayRange(byte[] bytes) {
        this.bytes = bytes;
        this.position = 0;
        this.length = bytes.length;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getPosition() {
        return position;
    }

    public int getLength() {
        return length;
    }

    /**
     * Get a byte array just for this range.  If the range occupies the full source array, then the source array is
     * returned rather than making a copy.  The caller should be aware that the returned byte array may or may not be
     * the same as the source array.  Generally, it's best to only use this when you are sure both the source array and
     * returned byte array won't be changed.
     *
     * @return
     */
    public byte[] toByteArray() {
        if (position == 0 && bytes.length == length)
            return this.bytes;
        else {
            byte[] copy = new byte[length];
            PlatformUtils.copyBytes(bytes, position, copy, 0, length);
            return copy;
        }
    }
}
