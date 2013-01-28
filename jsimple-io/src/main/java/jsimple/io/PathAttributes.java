package jsimple.io;

/**
 * @author Bret Johnson
 * @since 11/6/12 2:56 AM
 */
public abstract class PathAttributes {
    /**
     * @return last time the directory/file was modified, in millis.
     */
    public abstract long getLastModifiedTime();

    /**
     * @return for a file its size in bytes and for a directory the results are implementation specific
     */
    public abstract long getSize();
}
