package jsimple.io;

import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

/**
 * @author Bret Johnson
 * @since 11/3/12 9:17 PM
 */
public class Path extends PathBase {
    private java.nio.file.Path javaPath;

    /**
     * Get the extension (the text after the period) from the specified file/directory name.  The period itself isn't
     * returned, just the text after.  If there's no extension, the empty string is returned. s
     *
     * @param name file/directory name
     * @return extension part of the name (minus the period) or the empty string if there's no extension
     */
    public static String getExtension(String name) {
        int periodIndex = name.lastIndexOf('.');

        if (periodIndex == -1)
            return "";
        else return name.substring(periodIndex + 1);
    }

    public Path(java.nio.file.Path javaPath) {
        this.javaPath = javaPath;
    }

    /**
     * Get the last component of the path, which is a file/directory name.
     *
     * @return name (final) component of the path
     */
    public String getName() {
        return javaPath.getFileName().toString();
    }

    @Override public void visitChildren(final PathVisitor visitor) {
        try {
            Files.walkFileTree(javaPath, EnumSet.noneOf(FileVisitOption.class), 1, new FileVisitor<java.nio.file.Path>() {
                @Override
                public FileVisitResult preVisitDirectory(java.nio.file.Path dir, BasicFileAttributes attrs) throws java.io.IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(java.nio.file.Path file, BasicFileAttributes attrs) throws java.io.IOException {
                    return visitor.visit(new Path(file), new PathAttributes(attrs)) ?
                            FileVisitResult.CONTINUE : FileVisitResult.TERMINATE;
                }

                @Override
                public FileVisitResult visitFileFailed(java.nio.file.Path file, java.io.IOException exc) throws java.io.IOException {
                    return visitor.visitFailed(new Path(file), JavaIOUtils.jSimpleExceptionFromJavaIOException(exc)) ?
                            FileVisitResult.CONTINUE :
                            FileVisitResult.TERMINATE;
                }

                @Override
                public FileVisitResult postVisitDirectory(java.nio.file.Path dir, java.io.IOException exc) throws java.io.IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }
}
