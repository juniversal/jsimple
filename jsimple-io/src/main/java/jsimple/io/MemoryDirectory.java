package jsimple.io;

import jsimple.util.PlatformUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * @author Bret Johnson
 * @since 3/23/13 1:49 PM
 */
public class MemoryDirectory extends Directory {
    private @Nullable MemoryDirectory parent;
    private String name;
    private long lastModifiedTime;
    private ArrayList<MemoryDirectory> subdirectories = new ArrayList<MemoryDirectory>();
    private ArrayList<MemoryFile> files = new ArrayList<MemoryFile>();

    public static MemoryDirectory createRootDirectory() {
        return new MemoryDirectory(null, "ROOT");
    }

    private MemoryDirectory(@Nullable MemoryDirectory parent, String name) {
        this.parent = parent;
        this.name = name;
        this.lastModifiedTime = PlatformUtils.getCurrentTimeMillis();
    }

    /**
     * Get the file, which must already exist under the directory.  If the file doesn't exist, the results are undefined
     * (for some implementations it will fail right away & others will fail later, like when open it for read).
     *
     * @param fileName file name
     * @return File object, that's a child of this directory
     */
    @Override public File getFile(String fileName) {
        for (MemoryFile memoryFile : files) {
            if (memoryFile.getName().equals(fileName))
                return memoryFile;
        }

        return new MemoryFile(this, fileName);
    }

    /**
     * Add the specified file to this directory, replacing a file with the same name if it already exists.  This method
     * is called from the close handler on the OutputStream returned from MemoryFile.openForCreate, as that's the public
     * API for creating/updating a file.
     *
     * @param file MemoryFile to add/replace
     */
    void addOrReplaceFile(MemoryFile file) {
        String fileName = file.getName();

        int length = files.size();
        for (int i = 0; i < length; i++) {
            if (files.get(i).getName().equals(fileName)) {
                files.set(i, file);
                return;
            }
        }

        files.add(file);
    }

    /**
     * Delete the specified file name, if it exists.
     *
     * @param name file name
     * @return true if file found and deleted; false if it wasn't found
     */
    boolean deleteFile(String name) {
        for (MemoryFile memoryFile : files) {
            if (memoryFile.getName().equals(name)) {
                files.remove(memoryFile);
                return true;
            }
        }

        return false;
    }

    /**
     * Get the child directory, which must already exist under this directory.  If the directory doesn't exist, the
     * results are undefined (for some implementations it will fail right away & others will fail later, like when visit
     * the children of the directory or open a file).
     *
     * @param name directory name
     * @return Directory object, that's a child of this directory
     */
    @Override public Directory getDirectory(String name) {
        for (MemoryDirectory subdirectory : subdirectories) {
            if (subdirectory.getName().equals(name))
                return subdirectory;
        }

        throw new PathNotFoundException("MemoryDirectory {} not found", name);
    }

    /**
     * Get the child directory, creating it if it doesn't already exist.  For all implementations, the persistent
     * directory will actually exist after this method returns.
     *
     * @param name directory name
     * @return child directory
     */
    @Override public Directory getOrCreateDirectory(String name) {
        for (MemoryDirectory memoryDirectory : subdirectories) {
            if (memoryDirectory.getName().equals(name))
                return memoryDirectory;
        }

        MemoryDirectory newMemoryDirectory = new MemoryDirectory(this, name);
        subdirectories.add(newMemoryDirectory);
        return newMemoryDirectory;
    }

    public void deleteDirectory(String name) {
        for (MemoryDirectory memoryDirectory : subdirectories) {
            if (memoryDirectory.getName().equals(name)) {
                subdirectories.remove(memoryDirectory);
                return;
            }
        }

        throw new PathNotFoundException("MemoryDirectory {} not found", name);
    }

    /**
     * Visit the child elements of this path--basically list the files and subdirectories of a directory, calling the
     * visitor for each.  Just direct children are listed, not all descendants; callers can call this method recursively
     * if they want to visit all descendants.
     */
    @Override public void visitChildren(DirectoryVisitor visitor) {
        for (MemoryFile file : files)
            visitor.visit(file, new MemoryPathAttributes(file.getLastModifiedTimeInternal(), file.getSize()));

        for (MemoryDirectory subdirectory : subdirectories)
            visitor.visit(subdirectory, new MemoryPathAttributes(subdirectory.getLastModifiedTime(), 0));
    }

    /**
     * Get the name of this file/directory--the last component of the path.
     *
     * @return name of this file/directory
     */
    @Override public String getName() {
        return name;
    }

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    @Override public void delete() {
        if (parent == null)
            throw new RuntimeException("Can't delete root directory");
        parent.deleteDirectory(name);
    }

    private static class MemoryPathAttributes extends PathAttributes {
        private long lastModifiedTime;
        private long size;

        public MemoryPathAttributes(long lastModifiedTime, long size) {
            this.lastModifiedTime = lastModifiedTime;
            this.size = size;
        }

        /**
         * @return last time the directory/file was modified, in millis.
         */
        @Override public long getLastModifiedTime() {
            return lastModifiedTime;
        }

        /**
         * @return for a file its size in bytes and for a directory the results are implementation specific
         */
        @Override public long getSize() {
            return 0;
        }
    }
}
