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
 * JSimple doesn't (currently) have regular expression language support but it does have MatchBuilder and related
 * classes, which are similar and in some ways superior to normal regular expressions as they are compiled, not
 * interpreted, and "extensible" as a match expression can invoke other code that matches a nested expression.
 * <p/>
 * MatchBuilder is used to match some subsequence of characters in a larger string.   The core idea is that you continue
 * to call "match..." methods to expand the match, as desired, as if matching subsequent parts of a regular expression.
 * For each of the match... methods, if they return true then the currently matched subsequence is grown to include the
 * match in question but if they return false then it's unchanged.
 *
 * @author Bret Johnson
 * @since 1/11/2015
 */
public class MatchBuilder {
    private String str;
    private int strLength;
    private int startPosition;
    private int currPosition;

    /**
     * Create a MatchBuilder, starting at the specified position in the string.   The matched subsequence in the string
     * is initially empty and you cna then call match... methods to expand what's matched.
     *
     * @param str           string in question
     * @param startPosition starting position in string
     */
    public MatchBuilder(String str, int startPosition) {
        this.str = str;
        this.strLength = str.length();
        this.startPosition = startPosition;
        this.currPosition = startPosition;
    }

    /**
     * Create a MatchBuilder starting the match at the beginning of the string.
     *
     * @param str string in question
     */
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
        return str.substring(startPosition, currPosition);
    }

    /**
     * @return current character or '\0' if at end of str
     */
    public char getLookahead() {
        if (currPosition >= strLength)
            return '\0';
        else return str.charAt(currPosition);
    }

    public int getCurrPosition() {
        return currPosition;
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
        if (currPosition < strLength)
            ++currPosition;
    }

    public boolean hitEnd() {
        return currPosition >= strLength;
    }

    /**
     * Resets the MatchBuilder so the previous end position is now the start position for any new matching.
     */
    public void startOverAtCurrentPosition() {
        startPosition = currPosition;
    }

    /**
     * Matches the remainder of the outer string, however much is left.
     *
     * @return true, always
     */
    public boolean matchRestOfString() {
        currPosition = strLength;
        return true;
    }

    /**
     * Match all characters >= minChar and <= maxChar.
     *
     * @param minChar min character in the range (e.g. 'a')
     * @param maxChar max character in the range (e.g. 'z')
     * @return true if matched
     */
    public boolean matchCharRange(int minChar, int maxChar) {
        int lookahead = getLookahead();
        if (lookahead < minChar || lookahead > maxChar)
            return false;

        advance();
        return true;
    }

    /**
     * Match ASCII letter (a-z or A-Z).
     *
     * @return true if matched
     */
    public boolean matchAsciiLetter() {
        return matchCharRange('a', 'z') || matchCharRange('A', 'Z');
    }

    /**
     * Match the specified pattern.
     *
     * @param matchPattern pattern (essentially a callback to a matcher function) to match
     * @return true if matched
     */
    public boolean match(MatchPattern matchPattern) {
        return matchPattern.match(this);
    }

    /**
     * Matches until the specific pattern is encountered or the end of string is hit.   The pattern itself isn't
     * included in the match; it's just used to terminate it.
     */
    public void matchUntil(MatchPattern until) {
        while (true) {
            if (hitEnd())
                break;

            // Save the current position & reset it if the until pattern matches; we don't want to include the until
            // pattern itself in the match
            int savedPosition = currPosition;
            if (until.match(this)) {
                currPosition = savedPosition;
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

    /**
     * Match a single character.
     *
     * @param char1 character to match
     * @return true if matched
     */
    public boolean match(char char1) {
        if (char1 == getLookahead()) {
            advance();
            return true;
        } else return false;
    }

    /**
     * Match either char1 or char2.
     *
     * @param char1 character to match
     * @param char2 character to match
     * @return true if matched
     */
    public boolean match(char char1, char char2) {
        char lookahead = getLookahead();
        if (char1 == lookahead || char2 == lookahead) {
            advance();
            return true;
        } else return false;
    }

    /**
     * Match either char1 or char2 or char3.
     *
     * @param char1 character to match
     * @param char2 character to match
     * @param char3 character to match
     * @return true if matched
     */
    public boolean match(char char1, char char2, char char3) {
        char lookahead = getLookahead();
        if (char1 == lookahead || char2 == lookahead || char3 == lookahead) {
            advance();
            return true;
        } else return false;
    }

    /**
     * Match either char1 or char2 or char3 or char4.
     *
     * @param char1 character to match
     * @param char2 character to match
     * @param char3 character to match
     * @param char4 character to match
     * @return true if matched
     */
    public boolean match(char char1, char char2, char char3, char char4) {
        char lookahead = getLookahead();
        if (char1 == lookahead || char2 == lookahead || char3 == lookahead || char4 == lookahead) {
            advance();
            return true;
        } else return false;
    }

    /**
     * Match the specified string.
     *
     * @param stringValue string to match
     * @return true if matched
     */
    public boolean match(String stringValue) {
        int i = 0;
        int stringValueLength = stringValue.length();
        while (i < stringValueLength) {
            if (currPosition + stringValueLength >= strLength)
                return false;
            if (str.charAt(currPosition + i) != stringValue.charAt(i))
                return false;
        }

        currPosition += stringValueLength;
        return true;
    }
}
