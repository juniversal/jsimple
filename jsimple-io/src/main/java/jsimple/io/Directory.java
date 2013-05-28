package jsimple.io;

/**
 * @author Bret Johnson
 * @since 11/22/12 12:14 AM
 */
public abstract class Directory extends Path {
    /**
     * Get a file under the directory, which may or may not already exist.
     *
     * @param name file name
     * @return File object, that's a child of this directory
     */
    public abstract File getFile(String name);

    /**
     * Get the child directory, which must already exist under this directory.  If the directory doesn't exist, the
     * results are undefined (for some implementations it will fail right away & others will fail later, like when visit
     * the children of the directory or open a file).
     *
     * @param name directory name
     * @return Directory object, that's a child of this directory
     */
    public abstract Directory getDirectory(String name);

    /**
     * Get the child directory, creating it if it doesn't already exist.  For all implementations, the persistent
     * directory will actually exist after this method returns.
     *
     * @param name directory name
     * @return child directory
     */
    public abstract Directory getOrCreateDirectory(String name);

    /**
     * Visit the child elements of this path--basically list the files and subdirectories of a directory, calling the
     * visitor for each.  Just direct children are listed, not all descendants; callers can call this method recursively
     * if they want to visit all descendants.
     */
    public abstract void visitChildren(DirectoryVisitor visitor);

    /**
     * Delete this directory.  The directory must be empty; if it isn't the results are undefined--for some
     * implementations it will fail and for others delete the directory and its contents.
     */
    public abstract void delete();
}
