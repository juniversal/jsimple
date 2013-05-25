package jsimple.io;

/**
 * @author Bret Johnson
 * @since 11/22/12 12:14 AM
 */
public abstract class File extends Path {
    public abstract InputStream openForRead();

    /**
     * This is a convenience method to open a UTF8 text file for read.  It does the same thing as calling {@code new
     * Utf8InputStreamReader(openForRead())}.
     *
     * @return Utf8InputStreamReader for reading from this text file
     */
    public Utf8InputStreamReader openUtf8ForRead() {
        return new Utf8InputStreamReader(openForRead());
    }

    /**
     * Open the file for writing.  If the file already exists, it is truncated.  If the file doesn't exist, it is
     * created (assuming Directory.createFile was called to get this File object, otherwise the results are undefined).
     * Note that the current JSimple file I/O model is that files, when written, are completely rewritten.
     *
     * @return output stream, to write the file contents
     */
    public abstract OutputStream openForCreate();

    /**
     * This is a convenience method to open a UTF8 text file for writing.  It does the same thing as calling {@code new
     * Utf8OutputStreamWriter(openForWrite())}.
     *
     * @return Utf8OutputStreamWriter for writing to this text file
     */
    public Utf8OutputStreamWriter openUtf8ForCreate() {
        return new Utf8OutputStreamWriter(openForCreate());
    }

    /*
    public abstract File getAtomicFile();
    */

    public OutputStream openForCreateAtomic() {
        final String fileName = getName();
        final File tempFile= getParent().getFile(fileName + "-temp");

        OutputStream stream = tempFile.openForCreate();

        stream.setClosedListener(new ClosedListener() {
            @Override public void onClosed() {

                delete();
                tempFile.rename(fileName);
            }
        });

        return stream;
    }

    /**
     * Delete this file.
     */
    public abstract void delete();

    /**
     * See if the file exists.  If there's an error checking if the file exists, this method throws an exception when
     * possible, though for some platform implementations it'll just return false if platform can't distinguish not
     * existing from there being an error checking.
     *
     * @return true if the file exists
     */
    public boolean exists() {
        return false;
    }

    /**
     * Rename this file, giving it a new name in the same directory.  If a file with the specified name already exists,
     * an exception is thrown.  If/when there's the need to move a File to a different directory, we'll add separate
     * support for that.
     *
     * @param newName
     */

    public abstract void rename(String newName);

    public abstract Directory getParent();
}
