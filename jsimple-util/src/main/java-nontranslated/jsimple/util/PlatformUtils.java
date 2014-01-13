package jsimple.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Bret Johnson
 * @since 10/21/12 10:39 PM
 */
public class PlatformUtils extends PlatformUtilsBase {
    /**
     * Get the number of milliseconds since Jan 1, 1970, UTC time.  That's also known as epoch time.  It's the time unit
     * we generally use in JSimple.
     *
     * @return number of milliseconds since 1/1/70 UTC/GMT
     */
    public static long platformGetCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static <T extends Comparable<? super T>> void sortList(List<T> list) {
        Collections.sort(list);
    }

    public static <T> void sortList(List<T> list, Comparator<? super T> comparator) {
        Collections.sort(list, comparator);
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
        try (StringWriter stringWriter = new StringWriter()) {
            try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
                e.printStackTrace(printWriter);
            }

            return stringWriter.getBuffer().toString();
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
     * @param src source array
     * @param srcPos starting position in source array
     * @param dest destination array
     * @param destPos position in destination array
     * @param length number of elements to copy
     */
    public static void copyBytes(byte[] src, int srcPos, byte[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    /**
     * Copy data from source char array to destination char array.
     *
     * @param src source array
     * @param srcPos starting position in source array
     * @param dest destination array
     * @param destPos position in destination array
     * @param length number of elements to copy
     */
    public static void copyChars(char[] src, int srcPos, char[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    /**
     * Use the whatever proxy settings are configured in the OS.  On some platform, by default no web proxy is used.
     * Calling this method causes the OS proxy default to be used instead.
     */
    public static void useSystemProxy() {
        System.setProperty("java.net.useSystemProxies", "true");    // Use OS configured proxy
    }
}
