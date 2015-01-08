/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2012-2014 Microsoft Mobile.  All Rights Reserved.
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
 *
 *
 * This file is based on or incorporates material from SLF4J http://www.slf4j.org
 * (collectively, "Third Party Code"). Microsoft Mobile is not the original
 * author of the Third Party Code.  The original copyright notice and the
 * license, under which Microsoft Mobile received such Third Party Code, are set
 * forth below.
 *
 *
 * Copyright (c) 2004-2011 QOS.ch
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package jsimple.util;

import org.jetbrains.annotations.Nullable;

/**
 * Formats messages according to very simple substitution rules. Substitutions can be made 1, 2 or more arguments.
 * <p/>
 * <p/>
 * For example,
 * <p/>
 * <pre>
 * MessageFormatter.format("Hi {}.", "there")
 * </pre>
 * <p/>
 * will return the string "Hi there.".
 * <p/>
 * The {} pair is called the <em>formatting anchor</em>. It serves to designate the location where arguments need to be
 * substituted within the message pattern.
 * <p/>
 * In case your message contains the '{' or the '}' character, you do not have to do anything special unless the '}'
 * character immediately follows '{'. For example,
 * <p/>
 * <pre>
 * MessageFormatter.format("Set {1,2,3} is not equal to {}.", "1,2");
 * </pre>
 * <p/>
 * will return the string "Set {1,2,3} is not equal to 1,2.".
 * <p/>
 * <p/>
 * If for whatever reason you need to place the string "{}" in the message without its <em>formatting anchor</em>
 * meaning, then you need to escape the '{' character with '\', that is the backslash character. Only the '{' character
 * should be escaped. There is no need to escape the '}' character. For example,
 * <p/>
 * <pre>
 * MessageFormatter.format("Set \\{} is not equal to {}.", "1,2");
 * </pre>
 * <p/>
 * will return the string "Set {} is not equal to 1,2.".
 * <p/>
 * <p/>
 * The escaping behavior just described can be overridden by escaping the escape character '\'. Calling
 * <p/>
 * <pre>
 * MessageFormatter.format("File name is C:\\\\{}.", "file.zip");
 * </pre>
 * <p/>
 * will return the string "File name is C:\file.zip".
 * <p/>
 * <p/>
 * The formatting conventions are different than those of {@link java.text.MessageFormat} which ships with the Java
 * platform. This is justified by the fact that SLF4J's implementation is 10 times faster than that of {@link
 * java.text.MessageFormat}. This local performance difference is both measurable and significant in the larger context
 * of the complete logging processing chain.
 * <p/>
 * <p/>
 * See also {@link #format(String, Object)}, {@link #format(String, Object, Object)} and {@link #arrayFormat(String,
 * Object[])} methods for more details.
 * <p/>
 * <p/>
 * This class was adapted for JSimple from SLF4J, version 1.7.5  The primary change was removing support for including
 * array contents in formatted messages with array args.  That was primarily made to reduce code size and make things a
 * bit simpler, keeping with the JSimple philosophy.  Callers that wish to have fancy formatting for any logged messages
 * they have with array arguments can create a wrapper class with a toString method that formats as they desire.  In
 * many cases that's more useful anyway as it gives the caller control in how the array data should be formatted, so
 * it's most user readable and not excessively big (e.g. maybe they only want to log first X elements).
 *
 * @author Ceki G&uuml;lc&uuml;
 * @author Joern Huxhorn
 * @author Bret Johnson modified for JSimple
 */
final public class MessageFormatter {
    static final char DELIM_START = '{';
    static final String DELIM_STR = "{}";
    private static final char ESCAPE_CHAR = '\\';

    /**
     * Performs single argument substitution for the 'messagePattern' passed as parameter.
     * <p/>
     * For example,
     * <p/>
     * <pre>
     * MessageFormatter.format("Hi {}.", "there");
     * </pre>
     * <p/>
     * will return the string "Hi there.".
     * <p/>
     *
     * @param messagePattern The message pattern which will be parsed and formatted
     * @param arg            The argument to be substituted in place of the formatting anchor
     * @return The formatted message
     */
    public static FormattingTuple format(String messagePattern, Object arg) {
        return arrayFormat(messagePattern, new Object[]{arg});
    }

    /**
     * Performs a two argument substitution for the 'messagePattern' passed as parameter.
     * <p/>
     * For example,
     * <p/>
     * <pre>
     * MessageFormatter.format("Hi {}. My name is {}.", "Alice", "Bob");
     * </pre>
     * <p/>
     * will return the string "Hi Alice. My name is Bob.".
     *
     * @param messagePattern The message pattern which will be parsed and formatted
     * @param arg1           The argument to be substituted in place of the first formatting anchor
     * @param arg2           The argument to be substituted in place of the second formatting anchor
     * @return The formatted message
     */
    public static FormattingTuple format(final String messagePattern, Object arg1, Object arg2) {
        return arrayFormat(messagePattern, new Object[]{arg1, arg2});
    }

    static @Nullable Throwable getThrowableCandidate(@Nullable Object[] argArray) {
        if (argArray.length == 0)
            return null;

        final Object lastEntry = argArray[argArray.length - 1];
        if (lastEntry instanceof Throwable)
            return (Throwable) lastEntry;
        return null;
    }

    /**
     * Same principle as the {@link #format(String, Object)} and {@link #format(String, Object, Object)} methods except
     * that any number of arguments can be passed in an array.
     *
     * @param messagePattern The message pattern which will be parsed and formatted
     * @param argArray       An array of arguments to be substituted in place of formatting anchors
     * @return The formatted message
     */
    public static FormattingTuple arrayFormat(final String messagePattern, final @Nullable Object[] argArray) {
        Throwable throwableCandidate = getThrowableCandidate(argArray);

        StringBuilder buffer = new StringBuilder(messagePattern.length() + 50);

        int i = 0;
        int L;
        for (L = 0; L < argArray.length; L++) {
            int j = messagePattern.indexOf(DELIM_STR, i);

            if (j == -1) {
                // no more variables
                if (i == 0) { // this is a simple string
                    return new FormattingTuple(messagePattern, argArray, throwableCandidate);
                } else {
                    // add the tail string which contains no variables and return the result.
                    buffer.append(messagePattern.substring(i, messagePattern.length()));
                    return new FormattingTuple(buffer.toString(), argArray, throwableCandidate);
                }
            } else {
                if (isEscapedDelimiter(messagePattern, j)) {
                    if (!isDoubleEscaped(messagePattern, j)) {
                        L--; // DELIM_START was escaped, thus should not be incremented
                        buffer.append(messagePattern.substring(i, j - 1));
                        buffer.append(DELIM_START);
                        i = j + 1;
                    } else {
                        // The escape character preceding the delimiter start is itself escaped: "abc x:\\{}"
                        // We have to consume one backward slash
                        buffer.append(messagePattern.substring(i, j - 1));
                        deeplyAppendParameter(buffer, argArray[L]);
                        i = j + 2;
                    }
                } else {
                    // normal case
                    buffer.append(messagePattern.substring(i, j));
                    deeplyAppendParameter(buffer, argArray[L]);
                    i = j + 2;
                }
            }
        }

        // Append the characters following the last {} pair.
        buffer.append(messagePattern.substring(i, messagePattern.length()));
        if (L < argArray.length - 1)
            return new FormattingTuple(buffer.toString(), argArray, throwableCandidate);
        else return new FormattingTuple(buffer.toString(), argArray, null);
    }

    static boolean isEscapedDelimiter(String messagePattern, int delimiterStartIndex) {
        if (delimiterStartIndex == 0)
            return false;

        char potentialEscape = messagePattern.charAt(delimiterStartIndex - 1);
        return potentialEscape == ESCAPE_CHAR;

    }

    static boolean isDoubleEscaped(String messagePattern, int delimiterStartIndex) {
        return delimiterStartIndex >= 2 && messagePattern.charAt(delimiterStartIndex - 2) == ESCAPE_CHAR;
    }

    private static void deeplyAppendParameter(StringBuilder buffer, @Nullable Object o) {
        if (o == null)
            buffer.append("null");
        else {
            try {
                String oAsString = o.toString();
                buffer.append(oAsString);
            } catch (Throwable t) {
                buffer.append("[FAILED toString(); toString threw exception: " + t.toString() + "]");
            }
        }
    }

    /**
     * Holds the results of formatting done by {@link MessageFormatter}.
     *
     * @author Joern Huxhorn
     * @author Bret Johnson adapted for JSimple
     */
    public static class FormattingTuple {
        static public FormattingTuple NULL = new FormattingTuple(null);

        private String formattedMessage;
        private @Nullable Throwable throwable;
        private @Nullable Object[] argArray;

        public FormattingTuple(String formattedMessage) {
            this(formattedMessage, null, null);
        }

        public FormattingTuple(String formattedMessage, @Nullable Object[] argArray, @Nullable Throwable throwable) {
            this.formattedMessage = formattedMessage;
            this.throwable = throwable;

            if (throwable == null)
                this.argArray = argArray;
            else this.argArray = trimmedCopy(argArray);
        }

        static Object[] trimmedCopy(@Nullable Object[] argArray) {
            if (argArray == null || argArray.length == 0)
                throw new BasicException("non-sensical empty or null argument array");

            final int trimemdLen = argArray.length - 1;
            Object[] trimmed = new Object[trimemdLen];
            PlatformUtil.arraycopy(argArray, 0, trimmed, 0, trimemdLen);
            return trimmed;
        }

        public String getFormattedMessage() {
            return formattedMessage;
        }

        public Object[] getArgArray() {
            return argArray;
        }

        public Throwable getThrowable() {
            return throwable;
        }
    }
}
