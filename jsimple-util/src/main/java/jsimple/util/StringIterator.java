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
 */

package jsimple.util;

/**
 * @author Bret Johnson
 * @since 11/25/12 4:00 PM
 */
public class StringIterator {
    private String str;
    private int index = 0;
    private int length;

    public StringIterator(String string) {
        this.str = string;
        this.length = string.length();
    }

    public String getString() {
        return str;
    }

    public String getRemaining() {
        return str.substring(index);
    }

    public int getIndex() {
        return index;
    }

    public boolean atEnd() {
        return index >= length;
    }

    /**
     * @return current character or '\0' if at end of str
     */
    public char curr() {
        if (index >= length)
            return '\0';
        else return str.charAt(index);
    }

    /**
     * Return the current character (same as curr()) then advance to the next one.
     *
     * @return current character or -1 if at end of str
     */
    public char read() {
        char c = curr();
        advance();
        return c;
    }

    /**
     * Advance the iterator to the next character.  If already at the end of the str, this method is a no-op.
     */
    public void advance() {
        if (index < length)
            ++index;
    }

    private void advance(int charCount) {
        index += charCount;
        if (index > length)
            index = length;
    }

    public boolean match(char value) {
        if (value != curr())
            return false;
        advance();
        return true;
    }

    public boolean match(char value, StringBuilder buffer) {
        if (value != curr())
            return false;
        buffer.append(read());
        return true;
    }

    public boolean match(String substring) {
        if (getRemaining().startsWith(substring)) {
            advance(substring.length());
            return true;
        }
        return false;
    }

    // TODO: Add this back
/*
    public @Nullable Double matchDouble() {
        StringBuilder buffer = new StringBuilder();

        int savedIndex = index;
        match('-', buffer);
        if (!matchDigits(buffer)) {
            index = savedIndex;
            return null;
        }

        if (match('.', buffer))
            matchDigits(buffer);

        return Double.parseDouble(buffer.toString());
    }
*/

    public boolean matchDigits(StringBuilder buffer) {
        if (!matchDigit(buffer))
            return false;
        while (matchDigit(buffer))
            ;
        return true;
    }

    public boolean matchCharRange(int minChar, int maxChar, StringBuilder buffer) {
        int lookahead = curr();
        if (lookahead < minChar || lookahead > maxChar)
            return false;

        buffer.append(read());
        return true;
    }

    public boolean matchDigit(StringBuilder buffer) {
        return matchCharRange('0', '9', buffer);
    }

    /**
     * Search for the the next occurrence of the specified substring & advance the iterator just past it.  If that
     * substring isn't found, an exception is thrown.
     *
     * @param substr substring to search for
     */
    public void skipAheadPast(String substr) {
        if (!skipAheadPastIfExists(substr))
            throw new InvalidFormatException("'{}' not found in string '{}'", substr, str);
    }

    /**
     * Search for the the next occurrence of the specified substring & advance the iterator just past it.  Return true
     * if the substring is found, false if it isn't.
     *
     * @param substr substring to search for
     * @return true if the substring is found
     */
    public boolean skipAheadPastIfExists(String substr) {
        int substringIndex = str.indexOf(substr, index);
        if (substringIndex == -1)
            return false;

        index = substringIndex + substr.length();
        return true;
    }

    /**
     * Verify that the current character is c, throwing an exception if it isn't, then advance to the next character.
     *
     * @param c character to verify that occurs at the current iterator position
     */
    public void checkAndAdvance(char c) {
        if (atEnd())
            throw new InvalidFormatException("Already at end of string");

        if (str.charAt(index) != c)
            throw new InvalidFormatException("Character '{}' expected but not found at position '{}'", c,
                    getCurrentPositionWithContext());

        ++index;
    }

    private String getCurrentPositionWithContext() {
        int start = index - 50;
        if (start < 0)
            start = 0;

        int length = str.length();
        int end = index + 50;
        if (end > length)
            end = length;

        StringBuilder markedUpText = new StringBuilder();

        if (start > 0)
            markedUpText.append("...");

        markedUpText.append(str.substring(start, index));
        markedUpText.append("[^^^^]");
        markedUpText.append(str.substring(index, end));

        if (end < length)
            markedUpText.append("...");

        return markedUpText.toString();
    }

    public boolean isLineBreak() {
        int c = curr();
        return c == '\r' || c == '\n';
    }

    /**
     * @return true if the current character is a whitespace character.
     */
    public boolean isWhitespace() {
        int c = curr();
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }

    public boolean isDigit() {
        int c = curr();
        return c >= '0' && c <= '9';
    }

    public boolean isAsciiLetter() {
        int c = curr();
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    public boolean isWhitespaceOnSameLine() {
        int c = curr();
        return c == ' ' || c == '\t';
    }

    /**
     * Advance past any whitespace characters in the str, leaving the iterator at the first non-whitespace character
     * following.
     */
    public void advancePastWhitespace() {
        while (isWhitespace())
            advance();
    }

    public void advancePastWhitespaceOnSameLine() {
        while (isWhitespaceOnSameLine())
            advance();
    }

    public String readWhitespaceDelimitedToken() {
        StringBuilder token = new StringBuilder();
        while (!isWhitespace() && !atEnd())
            token.append(read());
        return token.toString();
    }

    public String readWhitespaceDelimitedTokenOnSameLine() {
        StringBuilder token = new StringBuilder();
        while (!isWhitespaceOnSameLine() && !isLineBreak() && !atEnd())
            token.append(read());
        return token.toString();
    }
}
