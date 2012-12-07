package jsimple.io;

/**
 * @author Bret Johnson
 * @since 11/22/12 12:14 AM
 */
public abstract class Directory extends Path {
    public abstract File getChildFile(String name);

    public abstract Directory getChildDirectory(String name);

    /**
     * Visit the child elements of this path--basically list the files and subdirectories of a directory, calling the
     * visitor for each.  Just direct children are listed, not all descendents; callers can call this method recursively
     * if they want to visit all descendents.
     */
    public abstract void visitChildren(DirectoryVisitor visitor);
}
