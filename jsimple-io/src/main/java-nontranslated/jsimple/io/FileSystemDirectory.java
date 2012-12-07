package jsimple.io;

import org.jetbrains.annotations.Nullable;

import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

/**
 * @author Bret Johnson
 * @since 11/24/12 4:37 PM
 */
public class FileSystemDirectory extends Directory {
    private java.nio.file.Path javaPath;

    public FileSystemDirectory(String directoryPath) {
        this.javaPath = Paths.get(directoryPath);
    }

    public FileSystemDirectory(java.nio.file.Path javaPath) {
        this.javaPath = javaPath;
    }

    @Override public String getName() {
        return javaPath.getFileName().toString();
    }

    @Override public @Nullable Directory getParent() {
        java.nio.file.Path javaParentPath = javaPath.getParent();
        if (javaParentPath == null)
            return null;

        return new FileSystemDirectory(javaParentPath);
    }

    @Override public File getChildFile(String name) {
        return new FileSystemFile(javaPath.resolve(name));
    }

    @Override public Directory getChildDirectory(String name) {
        return new FileSystemDirectory(javaPath.resolve(name));
    }

    /**
     * Visit the child elements of this path--basically list the files and subdirectories of a directory, calling the
     * visitor for each.  Just direct children are listed, not all descendents; callers can call this method recursively
     * if they want to visit all descendents.  If the path isn't a directory, an exception is thrown.
     */
    public void visitChildren(final DirectoryVisitor visitor) {
        try {
            Files.walkFileTree(javaPath, EnumSet.noneOf(FileVisitOption.class), 1, new FileVisitor<java.nio.file.Path>() {
                @Override
                public FileVisitResult preVisitDirectory(java.nio.file.Path dir, BasicFileAttributes attrs) throws java.io.IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(java.nio.file.Path file, BasicFileAttributes attrs) throws java.io.IOException {
                    PathAttributes pathAttributes = new FileSystemPathAttributes(attrs);

                    if (attrs.isDirectory())
                        return visitor.visit(new FileSystemDirectory(file), pathAttributes) ?
                                FileVisitResult.CONTINUE : FileVisitResult.TERMINATE;
                    else
                        return visitor.visit(new FileSystemFile(file), pathAttributes) ?
                                FileVisitResult.CONTINUE : FileVisitResult.TERMINATE;
                }

                @Override
                public FileVisitResult visitFileFailed(java.nio.file.Path file, java.io.IOException exc) throws java.io.IOException {
                    return visitor.visitFailed(file.getFileName().toString(),
                            JavaIOUtils.jSimpleExceptionFromJavaIOException(exc)) ?
                            FileVisitResult.CONTINUE : FileVisitResult.TERMINATE;
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
