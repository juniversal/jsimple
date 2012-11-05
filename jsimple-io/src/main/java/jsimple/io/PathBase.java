package jsimple.io;

import java.util.List;

/**
 * @author Bret Johnson
 * @since 11/3/12 7:19 PM
 */
public abstract class PathBase {
    /**
     * List the child elements of this path--basically list the names of subdirectories of a directory.  Just the names
     * are listed, not full paths.  If the path isn't a directory, an exception is thrown.
     *
     * @return list of children of this path
     */
    public abstract List<String> listChildren();
}
