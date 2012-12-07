package jsimple.io;

/**
 * @author Bret Johnson
 * @since 11/6/12 2:51 AM
 */
public abstract class DirectoryVisitor {
    public abstract boolean visit(File file, PathAttributes attributes);
    public abstract boolean visit(Directory directory, PathAttributes attributes);
    public abstract boolean visitFailed(String name, IOException exception);
}
