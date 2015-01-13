/*
 * Copyright (c) 2012-2015, Microsoft Mobile
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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

    @Override public String getName() {
        return javaFile.getName();
    }

    @Override public String getPathString() {
        return javaFile.getPath();
    }

    @Override public boolean exists() {
        return javaFile.exists();
    }

    @Override public void delete() {
        javaFile.delete();
    }

    @Override public void renameTo(String newName) {
        java.io.File newFile = new java.io.File(parent.getJavaFile(), newName);

        if (!javaFile.renameTo(newFile)) {
            // If the rename fails, it may be because the target file already exists.   If so, delete it and try the
            // rename again
            newFile.delete();
            if (!javaFile.renameTo(newFile))
                throw new IOException("Rename of {} to {} failed", this, newName);
        }
    }

    @Override public void moveTo(File destination) {
        if (!(destination instanceof FileSystemFile))
            throw new IOException("moveTo destination File must be of same type as source, a FileSystemFile in this case");

        if (!javaFile.renameTo(((FileSystemFile) destination).javaFile))
            throw new IOException("Move of {} to {} failed", this, destination);
    }

    @Override public long getLastModifiedTime() {
        long lastModified = javaFile.lastModified();
        if (lastModified == 0 && !javaFile.exists())
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
