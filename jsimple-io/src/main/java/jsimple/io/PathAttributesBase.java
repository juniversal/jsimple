package jsimple.io;

/**
 * @author Bret Johnson
 * @since 11/6/12 2:56 AM
 */
public abstract class PathAttributesBase {
    public abstract long getLastModifiedTime();
    public abstract boolean isDirectory();
    public abstract long getSize();
}
