package jsimple.io;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * @author Bret Johnson
 * @since 3/23/13 1:49 PM
 */
public class MemoryDirectory extends Directory {
    private @Nullable MemoryDirectory parent;
    private String name;
    private long lastModificationTime;
    private ArrayList<MemoryDirectory> subdirectories = new ArrayList<MemoryDirectory>();
    private ArrayList<MemoryFile> files = new ArrayList<MemoryFile>();

    public static MemoryDirectory createRootDirectory() {
        return new MemoryDirectory(null, "ROOT");
    }

    private MemoryDirectory(@Nullable MemoryDirectory parent, String name) {
        this.name = name;
    }

    /**
     * Get the file, which must already exist under the directory.  If the file doesn't exist, the results are undefined
     * (for some implementations it will fail right away & others will fail later, like when open it for read).
     *
     * @param name file name
     * @return File object, that's a child of this directory
     */
    @Override public File getFile(String name) {
        for (MemoryFile memoryFile : files) {
            if (memoryFile.getName().equals(name))
                return memoryFile;
        }

        throw new PathNotFoundException("MemoryFile " + name + " not found");
    }

    /**
     * Create a new file under this directory.  If a file with that name already exists, it will be overwritten.
     * File.openForCreate must be called at some point after this method to actually open the file; these methods must
     * be called as a pair--if one is called without the other, the results are undefined (meaning they are different
     * for different implementations). Some implementations actually create an empty file when this is called, while
     * others delay file creation until File.openForCreate is called and the contents are written (which is the
     * preferred implementation, as it's generally more efficient).
     * <p/>
     *
     * @param name file name
     * @return File object, that's a child of this directory
     */
    @Override public File createFile(String name) {
        for (MemoryFile memoryFile : files) {
            if (memoryFile.getName().equals(name))
                return memoryFile;
        }

        MemoryFile newMemoryFile = new MemoryFile(this, name);
        files.add(newMemoryFile);
        return newMemoryFile;
    }

    public void deleteFile(String name) {
        for (MemoryFile memoryFile : files) {
            if (memoryFile.getName().equals(name)) {
                files.remove(memoryFile);
                return;
            }
        }

        throw new PathNotFoundException("MemoryFile " + name + " not found");
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

        throw new PathNotFoundException("MemoryDirectory " + name + " not found");
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

        throw new PathNotFoundException("MemoryDirectory " + name + " not found");
    }

    /**
     * Visit the child elements of this path--basically list the files and subdirectories of a directory, calling the
     * visitor for each.  Just direct children are listed, not all descendants; callers can call this method recursively
     * if they want to visit all descendants.
     */
    @Override public void visitChildren(DirectoryVisitor visitor) {
        for (MemoryFile file : files)
            visitor.visit(file, new MemoryPathAttributes(file.getLastModificationTime(), file.getSize()));

        for (MemoryDirectory subdirectory : subdirectories)
            visitor.visit(subdirectory, new MemoryPathAttributes(subdirectory.getLastModificationTime(), 0));
    }

    /**
     * Get the name of this file/directory--the last component of the path.
     *
     * @return name of this file/directory
     */
    @Override public String getName() {
        return name;
    }

    public long getLastModificationTime() {
        return lastModificationTime;
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
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        /**
         * @return for a file its size in bytes and for a directory the results are implementation specific
         */
        @Override public long getSize() {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
