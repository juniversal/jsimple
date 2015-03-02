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

/**
 * @author Bret Johnson
 * @since 10/13/12 4:26 PM
 */
public class IOUtils {
    /**
     * Converts the string to a UTF-8 byte array.  The array is returned inside a ByteArrayRange.  The array can be
     * bigger than needed; only the specified length should be used.
     *
     * @param s string input
     * @return byte array, for the UTF-8 encoded string
     */
    public static ByteArrayRange toUtf8BytesFromString(String s) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(s.length());

        Utf8OutputStreamWriter utf8OutputStreamWriter = new Utf8OutputStreamWriter(byteArrayOutputStream);
        try {
            utf8OutputStreamWriter.write(s);
            utf8OutputStreamWriter.flush();

            return byteArrayOutputStream.getByteArray();
        } finally {
            utf8OutputStreamWriter.close();
        }
    }

    /**
     * Converts the string to a UTF-8 byte array, which is returned.  Unlike the method above, which returns the
     * length[0] bytes to use, the entire array array should be used here.  This method can be slightly less efficient
     * than the above version, since a copy of the array is made in some cases, but the tradeoff is more convenience. If
     * the input string only contains Latin1 characters, then both methods are the same, as no copy is made since we can
     * easily predict the exact needed byte array length before hand.
     *
     * @param s string input
     * @return byte array, for the UTF-8 encoded string
     */
/*
    public static byte[] toUtf8BytesFromString(String s) {
        int[] length = new int[1];
        byte[] bytes = toUtf8BytesFromString(s, length);

        if (bytes.length == length[0])
            return bytes;
        else {
            byte[] copy = new byte[length[0]];
            PlatformUtils.copyBytes(bytes, 0, copy, 0, length[0]);
            return copy;
        }
    }
*/

    /**
     * Convert the byte array, assumed to be UTF-8 character data, to a string.
     *
     * @param utf8Bytes UTF-8 byte array
     * @return String for UTF-8 data
     */
    public static String toStringFromUtf8Bytes(byte[] utf8Bytes) {
        return toStringFromUtf8Bytes(utf8Bytes, 0, utf8Bytes.length);
    }

    /**
     * Convert the subset of the byte array, from position through position + length - 1, assumed to be a UTF-8
     * character data, to a string.
     *
     * @param utf8Bytes UTF-8 byte array
     * @param position  starting position in array
     * @param length    number of bytes to read
     * @return String for UTF-8 data
     */
    public static String toStringFromUtf8Bytes(byte[] utf8Bytes, int position, int length) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(utf8Bytes, position, length);
        return toStringFromReader(new Utf8InputStreamReader(byteArrayInputStream));
    }

    public static String toStringFromUtf8Bytes(ByteArrayRange byteArrayRange) {
        return toStringFromReader(new Utf8InputStreamReader(byteArrayRange));
    }

    /**
     * Converts the stream contents, assumed to be a UTF-8 character data, to a string.  The inputStream is closed on
     * success.  If an exception is thrown,  the caller should close() inputStream (e.g. in a finally clause, since
     * calling close multiple times is benign).
     *
     * @param inputStream input stream, assumed to be UTF-8
     * @return string
     */
    public static String toStringFromUtf8Stream(InputStream inputStream) {
        return toStringFromReader(new Utf8InputStreamReader(inputStream));
    }

    /**
     * Converts the reader contents to a string.  The reader is closed on success.  If an exception is thrown,  the
     * caller should close() the reader or at least close the input stream that it's based on (e.g. it may close it in a
     * finally clause, since calling close multiple times is benign).
     *
     * @param reader reader object
     * @return string contents of reader
     */
    public static String toStringFromReader(Reader reader) {
        char[] buffer = new char[8 * 1024];
        StringBuilder out = new StringBuilder();
        int charsRead;
        do {
            charsRead = reader.read(buffer, 0, buffer.length);
            if (charsRead > 0)
                out.append(buffer, 0, charsRead);
        } while (charsRead >= 0);
        reader.close();
        return out.toString();
    }

}
