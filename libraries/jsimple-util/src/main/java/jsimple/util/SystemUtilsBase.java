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
 * This class is used to wrap one off utility methods that need to be implemented in a platform dependent way.  Methods
 * here are static & not actually defined in the base class--just a template comment is given here, and the method
 * should actually be implemented in the platform dependent PlatformUtils subclass.  Callers should call, for example,
 * with the syntax PlatformUtils.stringGetChars().
 * <p/>
 * By using the above scheme, calls are static & most efficient, there's no need for extra infrastructure around a
 * factory class, and perhaps most importantly if the platform doesn't implement a method which is actually needed by
 * some code it uses, that problem is caught at compile time not run time.
 *
 * @author Bret Johnson
 * @since 10/21/12 3:26 PM
 */
public class SystemUtilsBase {
    public static long currentTimeOverride = DateTime.NULL_DATE;

    /**
     * Get the number of milliseconds since Jan 1, 1970, UTC time.  That's also known as epoch time.  It's the time unit
     * we generally use in JSimple.
     *
     * @return number of milliseconds since 1/1/70 UTC/GMT
     */
    public static long getCurrentTimeMillis() {
        if (currentTimeOverride != DateTime.NULL_DATE)
            return currentTimeOverride;
        else return SystemUtils.platformGetCurrentTimeMillis();
    }

    /**
     * Set an override for the current time, that will then be returned from getCurrentTimeMillis instead of the actual
     * time being returned.   This method can be used in test jigs, to simulate the time, so tests are deterministic
     * with respect to time or perhaps to make a certain amount of time pass for code that checks getCurrentTimeMillis.
     * If the specified time is DateTime.NULL_DATE, then any override is cleared and the real time will be returned
     * again from getCurrentTimeMillis.
     *
     * @param currentTimeOverrideInMillis override for current time, in millis, or DateTime.NULL_DATE to clear override
     */
    public static void setCurrentTimeOverride(long currentTimeOverrideInMillis) {
        SystemUtilsBase.currentTimeOverride = currentTimeOverrideInMillis;
    }

    /**
     * Sort the elements of the list in their natural order (that is, as specified by the Comparable interface they
     * implement).  The sortList isn't guaranteed to be stable (it actually is in the Java version but not in the C# version).
     *
     * @param list input list
     * @param <T>  list element type
     */
    //public static <T extends Comparable<? super T>> void sortList(List<T> list)

    /**
     * Sort the elements of the list based on the Comparator callback passed in.  The sort isn't guaranteed to be stable
     * (it actually is in the Java version but not in the C# version).
     *
     * @param list input list
     * @param <T>  list element type
     */
    //public static <T> void sortList(List<T> list, Comparator<? super T> comparator)

    /**
     * Get the default line separator (typically newline or carriage return/newline) for the platform.
     *
     * @return default line separator for the platform
     */
    //public static String getLineSeparator()

    /**
     * Get the message + stack trace associated with this exception.
     *
     * @param e throwable in question
     * @return string containing the message & stack trace for the exception
     */
    //public static String getMessageAndStackTrace(Throwable e);

    /**
     * Make the current thread sleep for the specified number of milliseconds.
     *
     * @param sleepTimeInMilliseconds time to sleep, in milliseconds
     */
    //public static void sleep(int sleepTimeInMilliseconds);

    /**
     * Copy data from source byte array to destination byte array.
     *
     * @param src source array
     * @param srcPos starting position in source array
     * @param dest destination array
     * @param destPos position in destination array
     * @param length number of elements to copy
     */
    //public static void copyBytes(byte[] src, int srcPos, byte[] dest, int destPos, int length);

    /**
     * Copy data from source char array to destination char array.
     *
     * @param src source array
     * @param srcPos starting position in source array
     * @param dest destination array
     * @param destPos position in destination array
     * @param length number of elements to copy
     */
    //public static void copyChars(char[] src, int srcPos, char[] dest, int destPos, int length);

    /**
     * Copy characters from source string to destination char array.
     *
     * @param src     source string
     * @param srcPos  starting position in source string
     * @param dest    destination array
     * @param destPos position in destination array
     * @param length  number of elements to copy
     */
    //public static void copyChars(String src, int srcPos, char[] dest, int destPos, int length);

    /**
     * Use the whatever proxy settings are configured in the OS.  On some platform, by default no web proxy is used.
     * Calling this method causes the OS proxy default to be used instead.
     */
    //public static void useSystemProxy();

    /**
     * Encode a double in a long.   The precise format of the bits in the long are platform dependent; for most
     * platforms it's IEEE 754 but that's not guaranteed (e.g. a future Java -> C++ translator wouldn't guarantee that).
     * The only guarantee made here is that rawLlongBitsToDouble(doubleToRawLongBits(x)) should produce the original x
     * input.
     *
     * @param value input double value
     * @return long representation of that double
     */
    //long doubleToRawLongBits(double value);

    /**
     * Encode a long in a double.  The precise format of the bits in the double are platform dependent.  The only
     * guarantee made here is that rawLongBitsToDouble(doubleToRawLongBits(x)) should produce the original x input.
     *
     * @param value input long value
     * @return double representation of that value, undoing the transformation done by doubleToRawLongBits
     */
    //double rawLongBitsToDouble(long value);

    /*
     * Copies the number of {@code length} elements of the Array {@code src}
     * starting at the offset {@code srcPos} into the Array {@code dest} at
     * the position {@code destPos}.
     *
     * @param src
     *            the source array to copy the content.
     * @param srcPos
     *            the starting index of the content in {@code src}.
     * @param dest
     *            the destination array to copy the data into.
     * @param destPos
     *            the starting index for the copied content in {@code dest}.
     * @param length
     *            the number of elements of the {@code array1} content they have
     *            to be copied.
     */
    //public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length);

    /*
     * Causes the virtual machine to stop running and the program to exit. If
     * {@link #runFinalizersOnExit(boolean)} has been previously invoked with a
     * {@code true} argument, then all objects will be properly
     * garbage-collected and finalized first.
     *
     * @param code
     *            the return code.
     * @throws SecurityException
     *             if the running thread has not enough permission to exit the
     *             virtual machine.
     * @see SecurityManager#checkExit
     */
    //public static void exit(int code)

    /*
     * Return true if the two objects are equal, according to their equals method.  For reference types, this method
     * allows the objects to be null, returning true only if both are null.   And this method properly supports value
     * types for languages that support them (e.g. in C#).
     *
     * @param object1 object 1
     * @param object2 object 2
     * @return true if both objects are equal or both are null, false otherwise
     */
    //public static <T> boolean equals(@Nullable T object1, @Nullable T object2);
}
