package jsimple.logging;

/**
 * Logging level.  Conceptually this is an enum, but creating it as a class works better across all languages.
 *
 * @author Bret Johnson
 * @since 4/7/13 11:42 PM
 */
public class Level {
    public static final Level OFF = new Level(0, "OFF");
    public static final Level TRACE = new Level(1, "TRACE");
    public static final Level DEBUG = new Level(2, "DEBUG");
    public static final Level INFO = new Level(3, "INFO");
    public static final Level WARN = new Level(4, "WARN");
    public static final Level ERROR = new Level(5, "ERROR");
    private final int value;
    private final String defaultDisplayName;

    private Level(int value, String defaultDisplayName) {
        this.value = value;
        this.defaultDisplayName = defaultDisplayName;
    }

    public int getIntValue() {
        return value;
    }

    public String getDefaultDisplayName() {
        return defaultDisplayName;
    }
}
