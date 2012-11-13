package jsimple.io;

import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author Bret Johnson
 * @since 11/10/12 2:59 AM
 */
public class PathAttributes extends PathAttributesBase {
    private BasicFileAttributes basicFileAttributes;

    public PathAttributes(BasicFileAttributes basicFileAttributes) {
        this.basicFileAttributes = basicFileAttributes;
    }

    @Override public long getLastModifiedTime() {
        return basicFileAttributes.lastModifiedTime().toMillis();
    }

    @Override public boolean isDirectory() {
        return basicFileAttributes.isDirectory();
    }

    @Override public long getSize() {
        return basicFileAttributes.size();
    }
}
