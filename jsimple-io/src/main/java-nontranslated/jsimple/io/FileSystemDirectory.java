package jsimple.io;

/**
 * @author Bret Johnson
 * @since 11/24/12 4:37 PM
 */
public class FileSystemDirectory extends Directory {
    private java.io.File javaFile;

    public FileSystemDirectory(String directoryPath) {
        this.javaFile = new java.io.File(directoryPath);
    }

    public FileSystemDirectory(java.io.File javaFile) {
        this.javaFile = javaFile;
    }

    @Override public String getName() {
        return javaFile.getName();
    }

    @Override public String getPathString() {
        return javaFile.getPath();
    }

    @Override public File getFile(String name) {
        return new FileSystemFile(this, new java.io.File(javaFile, name));
    }

    @Override public Directory getDirectory(String name) {
        return new FileSystemDirectory(new java.io.File(javaFile, name));
    }

    @Override public boolean exists() {
        return javaFile.exists();
    }

    @Override public void create() {
        if (!javaFile.mkdirs()) {
            // mkdirs returns false if the directory already exists (which is fine) or if an error occurs (which isn't).
            // Check if the directory exists to differentiate
            if (! javaFile.exists())
                throw new IOException("Creating directory(ies) failed for {}", javaFile.toString());
        }
    }

    /**
     * Visit the child elements of this path--basically list the files and subdirectories of a directory, calling the
     * visitor for each.  Just direct children are listed, not all descendants; callers can call this method recursively
     * if they want to visit all descendants.  If the path isn't a directory, an exception is thrown.
     */
    @Override public void visitChildren(final DirectoryVisitor visitor) {
        java.io.File[] children = javaFile.listFiles();

        if (children == null)
            throw new IOException("Getting contents of directory failed for {}", javaFile.toString());

        int length = children.length;
        for (int i = 0; i < length; i++) {
            java.io.File childFile = children[i];

            if (childFile.isDirectory()) {
                if (!visitor.visit(new FileSystemDirectory(childFile)))
                    break;
            } else {
                if (!visitor.visit(new FileSystemFile(this, childFile)))
                    break;
            }
        }
    }

    @Override public void delete() {
        if (!javaFile.delete())
            throw new IOException("Deleting directory failed for {}", javaFile.toString());
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
        if (!javaFile.setLastModified(time))
            throw new IOException("setLastModifiedTime failed on {}", javaFile.toString());
*/
    }

    @Override public boolean isSetLastModifiedTimeSupported() {
        return true;
    }

    public java.io.File getJavaFile() {
        return javaFile;
    }
}
