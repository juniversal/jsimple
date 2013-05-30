package jsimple.io;

import jsimple.util.PlatformUtils;
import org.jetbrains.annotations.Nullable;

/**
 * A MemoryFile is a File object that exists completely in memory.  It can be used in unit tests, where it can be more
 * convenient to construct directories/files in memory rather than on disk.  It can also be used for "temporary" files.
 *
 * @author Bret Johnson
 * @since 3/23/13 1:51 PM
 */
public class MemoryFile extends File {
    MemoryDirectory parent;
    private String name;
    private long lastModifiedTime;
    private @Nullable byte[] data = null;

    /**
     * Construct a new MemoryFile, with the specified name.  Note that the MemoryFile doesn't actually exist in the
     * "file system" until openForCreate is called, some contents optionally written, and the stream is closed.  The
     * file is actually created when the stream is closed.
     *
     * @param name file name
     */
    public MemoryFile(MemoryDirectory parent, String name) {
        this.parent = parent;
        this.name = name;
        this.lastModifiedTime = PlatformUtils.getCurrentTimeMillis();
    }

    @Override public Directory getParent() {
        return parent;
    }

    @Override public InputStream openForRead() {
        if (data == null)
            throw new PathNotFoundException("MemoryFile {} doesn't currently exist", name);
        else return new ByteArrayInputStream(data);
    }

    /**
     * Open the file for writing.  If the file already exists, it is overwritten.  If the file doesn't exist, it is
     * created.  In both cases, the file contents only get set when the returned stream is closed, so the caller must be
     * sure to close it (like any stream, results are undefined if not closed).
     *
     * @return output stream, to write the file contents
     */
    @Override public OutputStream openForCreate() {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byteArrayOutputStream.setClosedListener(new ClosedListener() {
            @Override public void onClosed() {
                byte[] data = byteArrayOutputStream.closeAndGetByteArray();
                setData(data);

                parent.addOrReplaceFile(MemoryFile.this);
            }
        });

        return byteArrayOutputStream;
    }

    /**
     * Get the name of this file/directory--the last component of the path.
     *
     * @return name of this file/directory
     */
    @Override public String getName() {
        return name;
    }

    /*
    @Override public File getAtomicFile(){
        MemoryFile atomicFile = new MemoryFile(this.parent, this.parent.getName());
        return atomicFile;
    }
    */

    /**
     * Override the data in the file, setting it to the passed byte array.
     *
     * @param data data for file
     */
    public void setData(byte[] data) {
        this.data = data;
        this.lastModifiedTime = PlatformUtils.getCurrentTimeMillis();
    }

    @Override public void setLastModifiedTime(long time) {
        if (data == null)
            throw new IOException("File {} hasn't yet been created", name);

        lastModifiedTime = time;
    }

    public long getLastModifiedTimeInternal() {
        if (data == null)
            throw new IOException("File {} hasn't yet been created", name);

        return lastModifiedTime;
    }

    public int getSize() {
        if (data == null)
            throw new IOException("File {} hasn't yet been created", name);

        return data.length;
    }

    @Override public void delete() {
        parent.deleteFile(name);
    }

    @Override public boolean exists() {
        return data != null;
    }

    @Override public void rename(String newName) {
        if (data == null)
            throw new IOException("File {} hasn't yet been created", name);

        name = newName;
    }
}
