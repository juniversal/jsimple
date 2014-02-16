package jsimple.io;

/**
 * @author Bret Johnson
 * @since 11/6/12 2:51 AM
 */
public abstract class DirectoryVisitor {
    public abstract boolean visit(File file);

    public abstract boolean visit(Directory directory);

    /**
     * Called to indicate that the visit to the file/directory fails (e.g. because don't have rights to read the
     * file/directory attributes).
     *
     * @param name
     * @param exception
     * @return true to continue, false to abort
     */
    public boolean visitFailed(String name, IOException exception) {
        throw exception;
    }
}
