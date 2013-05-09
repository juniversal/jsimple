package jsimple.io;

/**
 * @author Bret Johnson
 * @since 11/22/12 12:14 AM
 */
public abstract class File extends Path {
    public abstract InputStream openForRead();

    /**
     * Open the file for writing.  If the file already exists, it is truncated.  If the file doesn't exist, it is
     * created (assuming Directory.createFile was called to get this File object, otherwise the results are undefined).
     * Note that the current JSimple file I/O model is that files, when written, are completely rewritten.
     *
     * @return output stream, to write the file contents
     */
    public abstract OutputStream openForCreate();

    public abstract OutputStream openForCreateAtomic();

    /**
     * Delete this file.
     */
    public abstract void delete();
}
