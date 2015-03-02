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

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Bret Johnson
 * @since 10/21/12 10:39 PM
 */
public class SystemUtils extends SystemUtilsBase {
    /**
     * Get the number of milliseconds since Jan 1, 1970, UTC time.  That's also known as epoch time.  It's the time unit
     * we generally use in JSimple.
     *
     * @return number of milliseconds since 1/1/70 UTC/GMT
     */
    public static long platformGetCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    private static String lineSeparator = System.getProperty("line.separator");

    /**
     * Get the default line separator (typically newline or carriage return/newline) for the platform.
     *
     * @return default line separator for the platform
     */
    public static String getLineSeparator() {
        return lineSeparator;
    }

    /**
     * Get the message + stack trace associated with this exception.
     *
     * @param e throwable in question
     * @return string containing the message & stack trace for the exception
     */
    public static String getExceptionDescription(Throwable e) {
        try {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            try {
                e.printStackTrace(printWriter);
                return stringWriter.getBuffer().toString();
            } finally {
                stringWriter.close();
                printWriter.close();
            }
        } catch (IOException e2) {
            return e.getMessage() + "\r\n<error generating stack trace>";
        }
    }

    /**
     * Make the current thread sleep for the specified number of milliseconds.
     *
     * @param sleepTimeInMilliseconds time to sleep, in milliseconds
     */
    public static void sleep(int sleepTimeInMilliseconds) {
        try {
            Thread.sleep(sleepTimeInMilliseconds);
        } catch (InterruptedException e) {
            throw new BasicException(e);
        }
    }

    /**
     * Copy data from source byte array to destination byte array.
     *
     * @param src     source array
     * @param srcPos  starting position in source array
     * @param dest    destination array
     * @param destPos position in destination array
     * @param length  number of elements to copy
     */
    public static void copyBytes(byte[] src, int srcPos, byte[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    /**
     * Copy data from source char array to destination char array.
     *
     * @param src     source array
     * @param srcPos  starting position in source array
     * @param dest    destination array
     * @param destPos position in destination array
     * @param length  number of elements to copy
     */
    public static void copyChars(char[] src, int srcPos, char[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    /**
     * Copy characters from source string to destination char array.
     *
     * @param src     source string
     * @param srcPos  starting position in source string
     * @param dest    destination array
     * @param destPos position in destination array
     * @param length  number of elements to copy
     */
    public static void copyChars(String src, int srcPos, char[] dest, int destPos, int length) {
        src.getChars(srcPos, srcPos + length, dest, destPos);
    }

    /**
     * Use the whatever proxy settings are configured in the OS.  On some platform, by default no web proxy is used.
     * Calling this method causes the OS proxy default to be used instead.
     */
    public static void useSystemProxy() {
        System.setProperty("java.net.useSystemProxies", "true");    // Use OS configured proxy
    }

    /**
     * Encode a double in a long.   The precise format of the bits in the long are platform dependent; for most
     * platforms it's IEEE 754 but that's not guaranteed (e.g. a future Java -> C++ translator wouldn't guarantee that).
     * The only guarantee made here is that rawLlongBitsToDouble(doubleToRawLongBits(x)) should produce the original x
     * input.
     *
     * @param value input double value
     * @return long representation of that double
     */
    public static long doubleToRawLongBits(double value) {
        return Double.doubleToRawLongBits(value);
    }

    /**
     * Encode a long in a double.  The precise format of the bits in the double are platform dependent.  The only
     * guarantee made here is that rawLongBitsToDouble(doubleToRawLongBits(x)) should produce the original x input.
     *
     * @param value input long value
     * @return double representation of that value, undoing the transformation done by doubleToRawLongBits
     */
    public static double rawLongBitsToDouble(long value) {
        return Double.longBitsToDouble(value);
    }

    /**
     * The default value for reference types is null and for value types is the "natural" value default (e.g. 0 for an
     * int).   Java only supports reference types, so always returns null here; C# and other languages that support
     * value types will return the value type default when T is a value type.
     *
     * @param <T> type in question
     * @return default value for type T (which is null for reference types)
     */
    public static <T> T defaultValue() {
        return null;
    }

    /**
     * Copies the number of {@code length} elements of the Array {@code src} starting at the offset {@code srcPos} into
     * the Array {@code dest} at the position {@code destPos}.
     *
     * @param src     the source array to copy the content.
     * @param srcPos  the starting index of the content in {@code src}.
     * @param dest    *            the destination array to copy the data into.
     * @param destPos the starting index for the copied content in {@code dest}.
     * @param length  the number of elements of the {@code array1} content they have to be copied.
     */
    public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length) {
        java.lang.System.arraycopy(src, srcPos, dest, destPos, length);
    }

    /**
     * Causes the virtual machine to stop running and the program to exit. If {@link #runFinalizersOnExit(boolean)} has
     * been previously invoked with a {@code true} argument, then all objects will be properly garbage-collected and
     * finalized first.
     *
     * @param code the return code.
     * @throws SecurityException if the running thread has not enough permission to exit the virtual machine.
     * @see SecurityManager#checkExit
     */
    public static void exit(int code) {
        java.lang.System.exit(code);
    }

    /**
     * Return true if the two objects are equal, according to their equals method.  For reference types, this method
     * allows the objects to be null, returning true only if both are null.   And this method properly supports value
     * types for languages that support them (e.g. in C#).
     *
     * @param object1 object 1
     * @param object2 object 2
     * @return true if both objects are equal or both are null, false otherwise
     */
    public static <T> boolean equals(@Nullable T object1, @Nullable T object2) {
        if (object1 == null)
            return object2 == null;
        else return object1.equals(object2);
    }

    public static <T extends Equatable<T>> boolean equalTo(@Nullable T object1, @Nullable T object2) {
        if (object1 == null)
            return object2 == null;
        else return object1.equalTo(object2);
    }

    /**
     * Returns true if the object has the default value for the type.   The default is null for reference types.   For
     * languages that support value types (e.g. C# and C++), the default value is 0 for integers, etc.
     *
     * @param object object in question
     * @param <T>    type in question
     * @return true if object is the default value for the type otherwise
     */
    public static <T> boolean isNullOrTypeDefault(@Nullable T object) {
        return object == null;
    }
}
