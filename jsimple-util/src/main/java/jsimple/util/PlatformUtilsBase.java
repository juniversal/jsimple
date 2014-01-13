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
public class PlatformUtilsBase {
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
        else return PlatformUtils.platformGetCurrentTimeMillis();
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
        PlatformUtilsBase.currentTimeOverride = currentTimeOverrideInMillis;
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
     * Use the whatever proxy settings are configured in the OS.  On some platform, by default no web proxy is used.
     * Calling this method causes the OS proxy default to be used instead.
     */
    //public static void useSystemProxy() {
}
