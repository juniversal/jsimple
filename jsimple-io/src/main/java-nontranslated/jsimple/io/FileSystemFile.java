package jsimple.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author Bret Johnson
 * @since 11/22/12 12:29 AM
 */
public class FileSystemFile extends File {
    private FileSystemDirectory parent;
    private java.io.File javaFile;

    public FileSystemFile(FileSystemDirectory parent, String filePath) {
        this.parent = parent;
        this.javaFile = new java.io.File(filePath);
    }

    public FileSystemFile(FileSystemDirectory parent, java.io.File javaFile) {
        this.parent = parent;
        this.javaFile = javaFile;
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
            return new JSimpleInputStreamOnJavaStream(new FileInputStream(javaFile));
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public OutputStream openForCreate() {
        try {
            return new JSimpleOutputStreamOnJavaStream(new FileOutputStream(javaFile));
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    @Override public String toString() {
        return javaFile.toString();
    }

    @Override public String getName() {
        return javaFile.getName();
    }

    @Override public boolean exists() {
        return javaFile.exists();
    }

    @Override public void delete() {
        javaFile.delete();
    }

    @Override public void rename(String newName) {
        java.io.File newFile = new java.io.File(parent.getJavaFile(), newName);

        if (!javaFile.renameTo(newFile)) {
            // If the rename fails, it may be because the target file already exists.   If so, delete it and try the
            // rename again
            newFile.delete();
            if (!javaFile.renameTo(newFile))
                throw new IOException("Rename of {} to {} failed", this, newName);
        }
    }

    @Override public long getLastModifiedTime() {
        long lastModified = javaFile.lastModified();
        if (lastModified == 0 && ! javaFile.exists())
            throw new PathNotFoundException(javaFile.toString());
        return lastModified;
    }

    @Override public void setLastModifiedTime(long time) {
        // TODO: Improve this, so doesn't try to change on Android but does when can
        javaFile.setLastModified(time);
/*
        if (! javaFile.setLastModified(time))
            throw new IOException("setLastModifiedTime failed on {}", javaFile.toString());
*/
    }

    @Override public long getSize() {
        long size = javaFile.length();
        // 0 is returned when the file doesn't exist as well as when its length is actually 0.   So check, when 0, to
        // see if it exists and fail if not
        if (size == 0 && !javaFile.exists())
            throw new PathNotFoundException(javaFile.toString());
        return size;
    }
}
