/*
 * Copyright (c) 2012-2015 Microsoft Mobile.  All Rights Reserved.
 *
 * This file is based on or incorporates material from JUnit http://junit.org.
 * (collectively, "Third Party Code"). Microsoft Mobile is not the original
 * author of the Third Party Code.
 *
 * This program and the accompanying materials are made available under the terms
 * of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html.
 */

package jsimple.unit;

import org.jetbrains.annotations.Nullable;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Bret Johnson
 * @since 1/1/13 9:06 PM
 */
public class UnitTest extends UnitTestBase {
    private static volatile @Nullable String projectDirectory = null;

    @Before public void setUp() {
    }

    @After public void tearDown() {
    }

    @Override public String getProjeectDirectory() {
        if (projectDirectory == null) {
            projectDirectory = System.getProperty("user.dir");

/*
            @Nullable URL rootResourceURL = getClass().getResource("/");
            if (rootResourceURL == null)
                throw new BasicException("Root resource, used for test files, not found");

            String resourcePath = rootResourceURL.getPath();
            if (resourcePath.isEmpty())
                throw new BasicException("Root resource, used for test files, is unexpectedly the empty string");

            // To find the project directory, we find the "build" directory in the path to the and assume that its
            // parent is the project directory
            File file = new File(resourcePath);
            while (!file.getName().equals("build")) {
                @Nullable File parentFile = file.getParentFile();

                if (parentFile == null)
                    throw new BasicException("'build' directory, used to locate test resources, not found in path {}", resourcePath);

                file = parentFile;
            }

            projectDirectory = file.getParentFile().getAbsolutePath();
            System.out.println(projectDirectory);
*/
        }
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

    @Override public void assertSame(@Nullable String message, @Nullable Object expected, @Nullable Object actual) {
        Assert.assertSame(message, expected, actual);
    }

    @Override public void fail(@Nullable String message) {
        Assert.fail(message);
    }
}
