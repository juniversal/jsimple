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
 * @author Bret Johnson
 * @since 1/11/2015
 */
public class MatchBuilder {
    private String str;
    private int strLength;
    private int start;
    private int curr;

    public MatchBuilder(String str, int start) {
        this.str = str;
        this.strLength = str.length();
        this.start = start;
        this.curr = start;
    }

    public MatchBuilder(String str) {
        this(str, 0);
    }

    /**
     * Returns the substring currently matched by this MatchBuilder.  This method is generally used to get the output of
     * the MatchBuilder.
     *
     * @return substring currently matched
     */
    @Override public String toString() {
        return str.substring(start, curr);
    }

    /**
     * @return current character or '\0' if at end of str
     */
    public char getLookahead() {
        if (curr >= strLength)
            return '\0';
        else return str.charAt(curr);
    }

    /**
     * Return the current character (same as getLookahead()) then advance to the next one.
     *
     * @return current character or -1 if at end of str
     */
    public char read() {
        char c = getLookahead();
        advance();
        return c;
    }

    /**
     * Advance the iterator to the next character.  If already at the end of the str, this method is a no-op.
     */
    public void advance() {
        if (curr < strLength)
            ++curr;
    }

    public boolean hitEnd() {
        return curr >= strLength;
    }

    /**
     * Resets the MatchBuilder so the previous end position is now the start position for any new matching.
     */
    public void startOverAtCurrentPosition() {
        start = curr;
    }

    /**
     * Matches the remainder of the outer string, however much is left.
     *
     * @return true, always
     */
    public boolean matchRestOfString() {
        curr = strLength;
        return true;
    }

    public boolean matchCharRange(int minChar, int maxChar) {
        int lookahead = getLookahead();
        if (lookahead < minChar || lookahead > maxChar)
            return false;

        advance();
        return true;
    }

    public boolean match(Pattern pattern) {
        return pattern.match(this);
    }

    /**
     * Matches until the specific pattern is encountered or the end of string is hit.   The pattern itself isn't
     * included in the match; it's just used to terminate it.
     */
    public void matchUntil(Pattern until) {
        while (true) {
            if (hitEnd())
                break;

            // Save the current position & reset it if the until pattern matches; we don't want to include the until
            // pattern itself in the match
            int savedPosition = curr;
            if (until.match(this)) {
                curr = savedPosition;
                break;
            }

            advance();
        }
    }

    /**
     * Matches a single digit (0 - 9).
     *
     * @return true a digit was matched
     */
    public boolean matchDigit() {
        return matchCharRange('0', '9');
    }

    /**
     * Matches one or more digits (0 - 9).
     *
     * @return true if at least one digit was matched
     */
    public boolean matchDigits() {
        if (!matchDigit())
            return false;
        while (matchDigit())
            ;
        return true;
    }

    public boolean match(char charValue) {
        if (charValue != getLookahead())
            return false;
        advance();
        return true;
    }

    public boolean match(String stringValue) {
        int i = 0;
        int stringValueLength = stringValue.length();
        while (i < stringValueLength) {
            if (curr + stringValueLength >= strLength)
                return false;
            if (str.charAt(curr + i) != stringValue.charAt(i))
                return false;
        }

        curr += stringValueLength;
        return true;
    }
}
