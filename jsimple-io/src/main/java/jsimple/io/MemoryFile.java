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
    private String name;
    private long lastModificationTime;
    private @Nullable byte[] data = null;

    /**
     * Create a new MemoryFile, with the specified name.
     *
     * @param name file name
     */
    public MemoryFile(String name) {
        this.name = name;
        this.lastModificationTime = PlatformUtils.getCurrentTimeMillis();
    }

    @Override public InputStream openForRead() {
        if (data == null)
            throw new FileNotFoundException("MemoryFile " + name + " doesn't currently exist");
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
        return new MemoryFileByteArrayOutputStream(this);
    }

    /**
     * Get the name of this file/directory--the last component of the path.
     *
     * @return name of this file/directory
     */
    @Override public String getName() {
        return name;
    }

    /**
     * Override the data in the file, setting it to the passed byte array.
     *
     * @param data data for file
     */
    public void setData(byte[] data) {
        this.data = data;
        this.lastModificationTime = PlatformUtils.getCurrentTimeMillis();
    }

    public long getLastModificationTime() {
        return lastModificationTime;
    }

    public int getSize() {
        if (data == null)
            throw new IOException("File " + name + " hasn't yet been created");

        return data.length;
    }

    private static class MemoryFileByteArrayOutputStream extends ByteArrayOutputStream {
        private MemoryFile memoryFile;
        boolean closed = false;

        public MemoryFileByteArrayOutputStream(MemoryFile memoryFile) {
            this.memoryFile = memoryFile;
        }

        /**
         * Closes this stream. This releases system resources used for this stream.  The closed flag is needed so that the
         * stream (like any stream) can be closed multiple times, but only the first close actually does anything, grabbing
         * the data and saving it off.  Subsequent closes are ignored.
         */
        @Override public void close() {
            if (!closed) {
                // Get the data, making a copy if it's not already the exact length required
                int[] length = new int[1];
                byte[] data = getByteArray(length);
                if (data.length != length[0])
                    data = toByteArray();

                super.close();

                memoryFile.setData(data);
                closed = true;
            }
        }
    }
}
