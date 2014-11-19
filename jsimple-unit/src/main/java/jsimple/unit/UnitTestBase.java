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
 *
 *
 * This code was adapted from JUnit (http://junit.org).  JUnit itself is licensed
 * under the Eclipse Public License 1.0.
 */

package jsimple.unit;

import org.jetbrains.annotations.Nullable;

public abstract class UnitTestBase {
    /**
     * Get the Java project directory, with the Java source etc. underneath it.  This directory can then be used to
     * locate test data files, etc.
     *
     * @return
     */
    public abstract String getJavaProjectDirectory();

    /**
     * Asserts that two objects are equal. If they are not, a test framework assertion error without a message is
     * thrown. If <code>expected</code> and <code>actual</code> are <code>null</code>, they are considered equal.
     *
     * @param expected expected value
     * @param actual   the value to check against <code>expected</code>
     */
    public void assertEquals(@Nullable Object expected, @Nullable Object actual) {
        assertEquals(null, expected, actual);
    }

    /**
     * Asserts that two objects are equal. If they are not, a test framework assertion error is thrown with the given
     * message. If <code>expected</code> and <code>actual</code> are <code>null</code>, they are considered equal.
     *
     * @param message  the identifying message for the assertion error (<code>null</code> okay)
     * @param expected expected value
     * @param actual   actual value
     */
    public abstract void assertEquals(@Nullable String message, @Nullable Object expected, @Nullable Object actual);

    /**
     * Asserts that two longs are equal. If they are not, a test framework assertion error is thrown.
     *
     * @param expected expected long value.
     * @param actual   actual long value
     */
    public void assertEquals(long expected, long actual) {
        assertEquals(null, expected, actual);
    }

    /**
     * Asserts that two longs are equal. If they are not, a test framework assertion error is thrown with the given
     * message.
     *
     * @param message  the identifying message for the test framework assertion error (<code>null</code> okay)
     * @param expected long expected value.
     * @param actual   long actual value
     */
    public void assertEquals(@Nullable String message, long expected, long actual) {
        assertEquals(message, (Long) expected, (Long) actual);
    }

    /**
     * Asserts that two object arrays are equal. If they are not, a test framework assertion error is thrown with the
     * given message. If <code>expecteds</code> and <code>actuals</code> are <code>null</code>, they are considered
     * equal.
     *
     * @param message   the identifying message for the test framework assertion error (<code>null</code> okay)
     * @param expecteds Object array or array of arrays (multi-dimensional array) with expected values.
     * @param actuals   Object array or array of arrays (multi-dimensional array) with actual values
     */
    public abstract void assertArrayEquals(@Nullable String message, Object[] expecteds, Object[] actuals);

    /**
     * Asserts that two object arrays are equal. If they are not, a test framework assertion error is thrown. If
     * <code>expected</code> and <code>actual</code> are <code>null</code>, they are considered equal.
     *
     * @param expecteds Object array or array of arrays (multi-dimensional array) with expected values
     * @param actuals   Object array or array of arrays (multi-dimensional array) with actual values
     */
    public void assertArrayEquals(Object[] expecteds, Object[] actuals) {
        assertArrayEquals(null, expecteds, actuals);
    }

    /**
     * Asserts that two byte arrays are equal. If they are not, a test framework assertion error is thrown with the
     * given message.
     *
     * @param message   the identifying message for the test framework assertion error (<code>null</code> okay)
     * @param expecteds byte array with expected values
     * @param actuals   byte array with actual values
     */
    public abstract void assertArrayEquals(@Nullable String message, byte[] expecteds, byte[] actuals);

    /**
     * Asserts that two byte arrays are equal. If they are not, a test framework assertion error is thrown.
     *
     * @param expecteds byte array with expected values.
     * @param actuals   byte array with actual values
     */
    public void assertArrayEquals(byte[] expecteds, byte[] actuals) {
        assertArrayEquals(null, expecteds, actuals);
    }

    /**
     * Asserts that two char arrays are equal. If they are not, a test framework assertion error is thrown with the
     * given message.
     *
     * @param message   the identifying message for the test framework assertion error (<code>null</code> okay)
     * @param expecteds char array with expected values.
     * @param actuals   char array with actual values
     */
    public abstract void assertArrayEquals(@Nullable String message, char[] expecteds, char[] actuals);

    /**
     * Asserts that two char arrays are equal. If they are not, a test framework assertion error is thrown.
     *
     * @param expecteds char array with expected values.
     * @param actuals   char array with actual values
     */
    public void assertArrayEquals(char[] expecteds, char[] actuals) {
        assertArrayEquals(null, expecteds, actuals);
    }

    /**
     * Asserts that two short arrays are equal. If they are not, a test framework assertion error is thrown with the
     * given message.
     *
     * @param message   the identifying message for the test framework assertion error (<code>null</code> okay)
     * @param expecteds short array with expected values.
     * @param actuals   short array with actual values
     */
    public abstract void assertArrayEquals(@Nullable String message, short[] expecteds, short[] actuals);

    /**
     * Asserts that two short arrays are equal. If they are not, a test framework assertion error is thrown.
     *
     * @param expecteds short array with expected values.
     * @param actuals   short array with actual values
     */
    public void assertArrayEquals(short[] expecteds, short[] actuals) {
        assertArrayEquals(null, expecteds, actuals);
    }

    /**
     * Asserts that two int arrays are equal. If they are not, a test framework assertion error is thrown with the given
     * message.
     *
     * @param message   the identifying message for the test framework assertion error (<code>null</code> okay)
     * @param expecteds int array with expected values.
     * @param actuals   int array with actual values
     */
    public abstract void assertArrayEquals(@Nullable String message, int[] expecteds, int[] actuals);

    /**
     * Asserts that two int arrays are equal. If they are not, a test framework assertion error is thrown.
     *
     * @param expecteds int array with expected values.
     * @param actuals   int array with actual values
     */
    public void assertArrayEquals(int[] expecteds, int[] actuals) {
        assertArrayEquals(null, expecteds, actuals);
    }

    /**
     * Asserts that two long arrays are equal. If they are not, a test framework assertion error is thrown with the
     * given message.
     *
     * @param message   the identifying message for the test framework assertion error (<code>null</code> okay)
     * @param expecteds long array with expected values.
     * @param actuals   long array with actual values
     */
    public abstract void assertArrayEquals(@Nullable String message, long[] expecteds, long[] actuals);

    /**
     * Asserts that two long arrays are equal. If they are not, a test framework assertion error is thrown.
     *
     * @param expecteds long array with expected values.
     * @param actuals   long array with actual values
     */
    public void assertArrayEquals(long[] expecteds, long[] actuals) {
        assertArrayEquals(null, expecteds, actuals);
    }

    /**
     * Asserts that a condition is true. If it isn't it throws a test framework assertion error with the given message.
     *
     * @param message   the identifying message for the test framework assertion error (<code>null</code> okay)
     * @param condition condition to be checked
     */
    public void assertTrue(@Nullable String message, boolean condition) {
        if (!condition)
            fail(message);
    }

    /**
     * Asserts that a condition is true. If it isn't it throws a test framework assertion error without a message.
     *
     * @param condition condition to be checked
     */
    public void assertTrue(boolean condition) {
        assertTrue(null, condition);
    }

    /**
     * Asserts that a condition is false. If it isn't it throws a test framework assertion error with the given
     * message.
     *
     * @param message   the identifying message for the test framework assertion error (<code>null</code> okay)
     * @param condition condition to be checked
     */
    public void assertFalse(@Nullable String message, boolean condition) {
        assertTrue(message, !condition);
    }

    /**
     * Asserts that a condition is false. If it isn't it throws a test framework assertion error without a message.
     *
     * @param condition condition to be checked
     */
    public void assertFalse(boolean condition) {
        assertFalse(null, condition);
    }

    /**
     * Asserts that an object isn't null. If it is a test framework assertion error is thrown with the given message.
     *
     * @param message the identifying message for the test framework assertion error (<code>null</code> okay)
     * @param object  Object to check or <code>null</code>
     */
    public void assertNotNull(@Nullable String message, @Nullable Object object) {
        assertTrue(message, object != null);
    }

    /**
     * Asserts that an object isn't null. If it is a test framework assertion error is thrown.
     *
     * @param object Object to check or <code>null</code>
     */
    public void assertNotNull(@Nullable Object object) {
        assertNotNull(null, object);
    }

    /**
     * Asserts that an object is null. If it is not, a test framework assertion error is thrown with the given message.
     *
     * @param message the identifying message for the test framework assertion error (<code>null</code> okay)
     * @param object  Object to check or <code>null</code>
     */
    public void assertNull(@Nullable String message, @Nullable Object object) {
        assertTrue(message, object == null);
    }

    /**
     * Asserts that an object is null. If it isn't a test framework assertion error is thrown.
     *
     * @param object Object to check or <code>null</code>
     */
    public void assertNull(@Nullable Object object) {
        assertNull(null, object);
    }

    /**
     * Fails a test with the given message.
     *
     * @param message the identifying message for the test framework assertion error (<code>null</code> okay)
     */
    public abstract void fail(@Nullable String message);

    /**
     * Fails a test with no message.
     */
    public void fail() {
        fail(null);
    }
}
