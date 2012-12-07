package jsimple.io;

import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author Bret Johnson
 * @since 11/10/12 2:59 AM
 */
public class FileSystemPathAttributes extends PathAttributes {
    private BasicFileAttributes basicFileAttributes;

    public FileSystemPathAttributes(BasicFileAttributes basicFileAttributes) {
        this.basicFileAttributes = basicFileAttributes;
    }

    @Override public long getLastModifiedTime() {
        return basicFileAttributes.lastModifiedTime().toMillis();
    }

    @Override public long getSize() {
        return basicFileAttributes.size();
    }
}
