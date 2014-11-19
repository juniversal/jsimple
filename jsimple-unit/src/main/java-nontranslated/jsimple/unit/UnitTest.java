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
import org.junit.Assert;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Bret Johnson
 * @since 1/1/13 9:06 PM
 */
public class UnitTest extends UnitTestBase {
    private static volatile @Nullable String projectDirectory = null;

    @Override public String getJavaProjectDirectory() {
        if (projectDirectory == null)
            projectDirectory = System.getProperty("user.dir");
        return projectDirectory;
    }

    @Override public void assertEquals(@Nullable String message, @Nullable Object expected, @Nullable Object actual) {
        Assert.assertEquals(message, expected, actual);
    }

    @Override public void assertArrayEquals(@Nullable String message, Object[] expecteds, Object[] actuals) {
        Assert.assertArrayEquals(message, expecteds, actuals);
    }

    @Override public void assertArrayEquals(@Nullable String message, byte[] expecteds, byte[] actuals) {
        Assert.assertArrayEquals(message, expecteds, actuals);
    }

    @Override public void assertArrayEquals(@Nullable String message, char[] expecteds, char[] actuals) {
        Assert.assertArrayEquals(message, expecteds, actuals);
    }

    @Override public void assertArrayEquals(@Nullable String message, short[] expecteds, short[] actuals) {
        Assert.assertArrayEquals(message, expecteds, actuals);
    }

    @Override public void assertArrayEquals(@Nullable String message, int[] expecteds, int[] actuals) {
        Assert.assertArrayEquals(message, expecteds, actuals);
    }

    @Override public void assertArrayEquals(@Nullable String message, long[] expecteds, long[] actuals) {
        Assert.assertArrayEquals(message, expecteds, actuals);
    }

    @Override public void fail(@Nullable String message) {
        Assert.fail(message);
    }
}
