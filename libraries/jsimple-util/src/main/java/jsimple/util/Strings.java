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

import jsimple.util.function.CharPredicate;
import org.jetbrains.annotations.Nullable;

/**
 * @author Bret Johnson
 * @since 10/23/12 12:23 AM
 */
public class Strings {
    public static boolean isNullOrEmpty(@Nullable String string) {
        return string == null || string.isEmpty();
    }

    /**
     * Convert a byte array to a hexadecimal string.  Leading zeros are of course preserved.  Hex letters are upper
     * case.
     *
     * @param bytes byte array
     * @return hexadecimal string
     */
    public static String toHexStringFromBytes(byte[] bytes) {
        char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];  // Formerly v/16
            hexChars[j * 2 + 1] = hexArray[v & 0xF];   // Formerly v%16
        }
        return new String(hexChars);
    }

    /**
     * Convert a hexadecimal string to a byte array.  An exception is thrown if the string contains any non-hex
     * characters.  A-F and a-f (both upper & lower case) are allowed for input.
     *
     * @param hexString hex string, containing only 0-9, A-F, or a-f
     * @return byte array
     */
    public static byte[] toBytesFromHexString(String hexString) {
        int len = hexString.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2)
            bytes[i / 2] = (byte) ((toByteFromHexCharacter(hexString.charAt(i)) << 4)
                    + toByteFromHexCharacter(hexString.charAt(i + 1)));

        return bytes;
    }

    /**
     * Convert a hex character to it's equivalent byte.  Similar to standard Java's Character.digit.  If character isn't
     * a valid hex character, an exception is thrown.
     *
     * @param hexCharacter hex character (0-9, A-F, or a-f)
     * @return number equivalent (0-15)
     */
    public static byte toByteFromHexCharacter(char hexCharacter) {
        if ('0' <= hexCharacter && hexCharacter <= '9')
            return (byte) (hexCharacter - '0');
        else if ('A' <= hexCharacter && hexCharacter <= 'F')
            return (byte) (hexCharacter - 'A' + 10);
        else if ('a' <= hexCharacter && hexCharacter <= 'f')
            return (byte) (hexCharacter - 'a' + 10);
        else throw new BasicException("Invalid hex character: '{}'", hexCharacter);
    }

    /**
     * Convert a byte array, assumed to be a Latin1 (aka 8859-1 aka Windows 1252) encoded string, to a regular Java
     * string.  Latin1 is the first 256 characters of Unicode, so this conversion is just a straight pass through,
     * turning bytes in chars.  Note that ASCII is a subset of Latin1.
     *
     * @param bytes byte array
     * @return resulting string
     */
    public static String toStringFromLatin1Bytes(byte[] bytes) {
        return toStringFromLatin1Bytes(new ByteArrayRange(bytes));
    }

    /**
     * Convert a ByteArrayRange, assumed to be a Latin1 (aka 8859-1 aka Windows 1252) encoded string, to a regular Java
     * string.  Latin1 is the first 256 characters of Unicode, so this conversion is just a straight pass through,
     * turning bytes in chars.  Note that ASCII is a subset of Latin1.
     *
     * @param byteArrayRange byte array data
     * @return resulting string
     */
    public static String toStringFromLatin1Bytes(ByteArrayRange byteArrayRange) {
        byte[] bytes = byteArrayRange.getBytes();

        int length = byteArrayRange.getLength();
        char[] chars = new char[length];
        for (int i = byteArrayRange.getPosition(); i < length; i++)
            chars[i] = (char) bytes[i];
        return new String(chars);
    }

    /**
     * Convert a string, assumed to contain only Latin1 (aka 8859-1 aka Windows 1252) characters, to a byte array.  The
     * byte array is encoded one byte per character--just a straight pass through from the Unicode, dropping the high
     * byte (which should be zero) of each character.  If any non-Latin1 characters are in the string an exception is
     * thrown.  This method is an efficient way to only ASCII text as a byte array, since ASCII is a subset of Latin1.
     *
     * @param string input string, which should contain only Latin1 characters
     * @return resulting byte array
     */
    public static byte[] toLatin1BytesFromString(String string) {
        int length = string.length();
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            char c = string.charAt(i);
            if (c >= 256)
                throw new BasicException("Character '{}' at index {} isn't a Latin1 character", c, i);
            bytes[i] = (byte) c;
        }
        return bytes;
    }

    /**
     * Splits this string using the supplied pattern as the delimiter.   Does the same thing as split(string, delimiter,
     * 0).
     *
     * @param string    string to split
     * @param delimiter the pattern used to delimit substrings
     * @return a list of Strings created by separating the via matches of the pattern
     */
    public static ArrayList<String> split(String string, MatchPattern delimiter) {
        return split(string, delimiter, 0);
    }

    /**
     * Splits this string using the supplied pattern as the delimiter.   In typical cases, the {@code delimiter} pattern
     * is character or literal string, but it can be arbitrarily complex.
     * <p/>
     * The parameter {@code max} controls how many times the pattern is applied, by specifying the max number of items
     * in the returned list.  If the limit is reached, the last item returned includes the remainder of the string.
     * Specify 0 for no limit.
     *
     * @param string    string to split
     * @param delimiter the pattern used to delimit substrings
     * @param max       the max number of entries in the resulting array; if 0 then unlimited
     * @return a list of Strings created by separating the via matches of the pattern
     */
    public static ArrayList<String> split(String string, MatchPattern delimiter, int max) {
        ArrayList<String> strings = new ArrayList<String>();

        MatchBuilder matchBuilder = new MatchBuilder(string);

        while (true) {
            if (max != 0 && strings.size() >= max - 1)
                matchBuilder.matchRestOfString();
            else matchBuilder.matchUntil(delimiter);

            strings.add(matchBuilder.toString());

            if (matchBuilder.hitEnd())
                break;

            matchBuilder.match(delimiter);
            matchBuilder.startOverAtCurrentPosition();
        }

        return strings;
    }

    /**
     * Splits this string using the supplied character as the delimiter.
     *
     * @param string    string to split
     * @param delimiter the character used to delimit substrings
     * @return a list of Strings created by separating the via matches of the character
     */
    public static ArrayList<String> split(String string, final char delimiter) {
        return split(string, delimiter, 0);
    }

    /**
     * Splits this string using the supplied character as the delimiter, returning at most {@code max} items.
     *
     * @param string    string to split
     * @param delimiter the character used to delimit substrings
     * @param max       the max number of entries in the resulting array; if 0 then unlimited
     * @return a list of Strings created by separating the via matches of the character
     */
    public static ArrayList<String> split(String string, final char delimiter, int max) {
        return split(string, new MatchPattern() {
            @Override public boolean match(MatchBuilder matchBuilder) {
                return matchBuilder.match(delimiter);
            }
        }, max);
    }

    public static String replaceAll(String string, MatchPattern pattern, String replacement) {
        StringBuilder buffer = new StringBuilder();

        StringIterator stringIterator = new StringIterator(string);
        while (! stringIterator.atEnd()) {
            if (stringIterator.match(pattern))
                buffer.append(replacement);
            else buffer.append(stringIterator.read());
        }

        return buffer.toString();
    }

    public static String replaceAll(String string, CharPredicate charPredicate, String replacement) {
        StringBuilder buffer = new StringBuilder();

        StringIterator stringIterator = new StringIterator(string);
        while (! stringIterator.atEnd()) {
            if (stringIterator.match(charPredicate))
                buffer.append(replacement);
            else buffer.append(stringIterator.read());
        }

        return buffer.toString();
    }

    public static String replaceFirst(String string, MatchPattern pattern, String replacement) {
        StringBuilder buffer = new StringBuilder();

        StringIterator stringIterator = new StringIterator(string);
        boolean foundMatch = false;
        while (! stringIterator.atEnd()) {
            if (! foundMatch && stringIterator.match(pattern)) {
                buffer.append(replacement);
                foundMatch = true;
            }
            else buffer.append(stringIterator.read());
        }

        return buffer.toString();
    }

    public static String replaceFirst(String string, CharPredicate charPredicate, String replacement) {
        StringBuilder buffer = new StringBuilder();

        StringIterator stringIterator = new StringIterator(string);
        boolean foundMatch = false;
        while (! stringIterator.atEnd()) {
            if (! foundMatch && stringIterator.match(charPredicate)) {
                buffer.append(replacement);
                foundMatch = true;
            }
            else buffer.append(stringIterator.read());
        }

        return buffer.toString();
    }
}
