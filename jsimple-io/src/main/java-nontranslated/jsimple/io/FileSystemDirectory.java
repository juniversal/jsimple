package jsimple.io;

import java.nio.file.*;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.EnumSet;

/**
 * @author Bret Johnson
 * @since 11/24/12 4:37 PM
 */
public class FileSystemDirectory extends Directory {
    private java.nio.file.Path javaPath;

    public FileSystemDirectory(String directoryPath) {
        this.javaPath = java.nio.file.Paths.get(directoryPath);
    }

    public FileSystemDirectory(java.nio.file.Path javaPath) {
        this.javaPath = javaPath;
    }

    @Override public String toString() {
        return javaPath.toString();
    }

    @Override public String getName() {
        return javaPath.getFileName().toString();
    }

    @Override public File getFile(String name) {
        return new FileSystemFile(this, javaPath.resolve(name));
    }

    @Override public Directory getDirectory(String name) {
        return new FileSystemDirectory(javaPath.resolve(name));
    }

    @Override public boolean exists() {
        return javaPath.toFile().exists();
    }

    @Override public void create() {
        try {
            Files.createDirectories(javaPath);
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    /**
     * Visit the child elements of this path--basically list the files and subdirectories of a directory, calling the
     * visitor for each.  Just direct children are listed, not all descendants; callers can call this method recursively
     * if they want to visit all descendants.  If the path isn't a directory, an exception is thrown.
     */
    public void visitChildren(final DirectoryVisitor visitor) {
        try {
            Files.walkFileTree(javaPath, EnumSet.noneOf(FileVisitOption.class), 1, new FileVisitor<java.nio.file.Path>() {
                public FileVisitResult preVisitDirectory(java.nio.file.Path dir, BasicFileAttributes attrs) throws java.io.IOException {
                    return FileVisitResult.CONTINUE;
                }

                public FileVisitResult visitFile(java.nio.file.Path file, BasicFileAttributes attrs) throws java.io.IOException {
                    PathAttributes pathAttributes = new FileSystemPathAttributes(attrs);

                    if (attrs.isDirectory())
                        return visitor.visit(new FileSystemDirectory(file), pathAttributes) ?
                                FileVisitResult.CONTINUE : FileVisitResult.TERMINATE;
                    else
                        return visitor.visit(new FileSystemFile(FileSystemDirectory.this, file), pathAttributes) ?
                                FileVisitResult.CONTINUE : FileVisitResult.TERMINATE;
                }

                // TODO: Test error handling here
                public FileVisitResult visitFileFailed(java.nio.file.Path file, java.io.IOException exc) throws java.io.IOException {
                    return visitor.visitFailed(file.getFileName().toString(),
                            JavaIOUtils.jSimpleExceptionFromJavaIOException(exc)) ?
                            FileVisitResult.CONTINUE : FileVisitResult.TERMINATE;
                }


                // TODO: Test error handling here
                public FileVisitResult postVisitDirectory(java.nio.file.Path dir, java.io.IOException exc) throws java.io.IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public void delete() {
        try{
            Files.delete(javaPath);
        } catch (java.io.IOException e){
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public long getLastModifiedTime() {
        try {
            FileTime fileTime = Files.getLastModifiedTime(javaPath);
            return fileTime.toMillis();
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public void setLastModifiedTime(long time) {
        try {
            Files.setLastModifiedTime(javaPath, FileTime.fromMillis(time));
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public boolean isSetLastModifiedTimeSupported() {
        return true;
    }

    public Path getJavaPath() {
        return javaPath;
    }
}
