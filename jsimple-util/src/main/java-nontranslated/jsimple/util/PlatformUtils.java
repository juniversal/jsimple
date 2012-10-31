package jsimple.util;

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
}
