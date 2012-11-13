package jsimple.io;

/**
 * @author Bret Johnson
 * @since 11/3/12 7:19 PM
 */
public abstract class PathBase {
    /**
     * Visit the child elements of this path--basically list the files and subdirectories of a directory, calling the
     * visitor for each.  Just direct children are listed, not all descendents; callers can call this method recursively
     * if they want to visit all descendents.  If the path isn't a directory, an exception is thrown.
     */
    public abstract void visitChildren(PathVisitor visitor);

    public abstract String getName();
}
