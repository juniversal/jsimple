package jsimple.io;

/**
 * @author Bret Johnson
 * @since 11/6/12 2:51 AM
 */
public interface PathVisitor {
    public boolean visit(Path path, PathAttributes pathAttributes);
    public boolean visitFailed(Path path, IOException exception);
}
