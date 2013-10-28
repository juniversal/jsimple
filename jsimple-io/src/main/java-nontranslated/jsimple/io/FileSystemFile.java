package jsimple.io;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;

/**
 * @author Bret Johnson
 * @since 11/22/12 12:29 AM
 */
public class FileSystemFile extends File {
    private FileSystemDirectory parent;
    private java.nio.file.Path javaPath;

    public FileSystemFile(FileSystemDirectory parent, String filePath) {
        this.parent = parent;
        this.javaPath = java.nio.file.Paths.get(filePath);
    }

    public FileSystemFile(FileSystemDirectory parent, java.nio.file.Path javaPath) {
        this.parent = parent;
        this.javaPath = javaPath;
    }

    @Override public Directory getParent() {
        return parent;
    }

    /*
    @Override public File getAtomicFile(){
        System.out.println(javaPath.toFile().getAbsolutePath());
        FileSystemFile atomicFile = new FileSystemFile(this.parent, javaPath.toFile().getAbsolutePath() + "-temp");

        return atomicFile;
    }
    */

    @Override public InputStream openForRead() {
        try {
            return new JSimpleInputStreamOnJavaStream(Files.newInputStream(javaPath));
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public OutputStream openForCreate() {
        try {
            return new JSimpleOutputStreamOnJavaStream(Files.newOutputStream(javaPath,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE));
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public String toString() {
        return javaPath.toString();
    }

    @Override public String getName() {
        return javaPath.getFileName().toString();
    }

    @Override public boolean exists() {
        return Files.exists(javaPath);
    }

    @Override public void delete() {
        // TODO: Change to just catch FileNotFoundException, I think
        try {
            if (javaPath.toFile().exists())
                Files.delete(javaPath);
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public void rename(String newName) {
        java.nio.file.Path newPath = parent.getJavaPath().resolve(newName);
        try {
            Files.move(javaPath, newPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (java.io.IOException e) {
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

    @Override public long getSize() {
        try {
            return Files.size(javaPath);
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }
}
