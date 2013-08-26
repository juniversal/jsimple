package jsimple.util;

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
    public static long getCurrentTimeMillis() {
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


}
