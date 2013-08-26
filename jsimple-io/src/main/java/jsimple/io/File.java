package jsimple.io;

/**
 * @author Bret Johnson
 * @since 11/22/12 12:14 AM
 */
public abstract class File extends Path {
    public abstract InputStream openForRead();

    /**
     * Copy the contents of this file to the specified output stream.  If an error occurs when copying, the file input
     * stream is closed (as you'd expect), an exception is thrown, and the output stream may be partially written to.
     *
     * @param outputStream output stream to write to
     */
    public void copyTo(OutputStream outputStream) {
        try (InputStream inputStream = openForRead()) {
            inputStream.copyTo(outputStream);
        }
    }

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
     * created. Note that the current JSimple file I/O model is that files, when written, are completely rewritten.
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

    public OutputStream openForCreateAtomic() {
        return openForCreateAtomic(0);
    }

    /**
     * Open the file for writing/creating, replacing the current file if it already exists.
     * <p/>
     * All writes are first made to a temp file (filename + "-temp" suffix, in the same directory).  When the returned
     * stream is closed, the new file is committed by renaming the -temp file to the original.  That way, the file is
     * either completely updated or not updated at all, never left in a half-updated state.
     * <p/>
     * If lastModifiedTime is non-zero, then it's set as the last modified time on the file, as part of the atomic
     * update (the timestamp is set on the temp file first & preserved on the rename).
     *
     * @param lastModifiedTime modification time in millis, set on updated file; ignored if zero
     * @return OutputStream, with a close handler attached to update the original file when closed
     */
    public OutputStream openForCreateAtomic(final long lastModifiedTime) {
        final String fileName = getName();
        final File tempFile = getParent().getFile(fileName + "-temp");

        OutputStream stream = tempFile.openForCreate();

        stream.setClosedListener(new ClosedListener() {
            public void onClosed() {
                if (lastModifiedTime != 0)
                    tempFile.setLastModifiedTime(lastModifiedTime);

                // TODO: Switch to do atomic rename when supported, using this:
                // http://stackoverflow.com/questions/167414/is-an-atomic-file-rename-with-overwrite-possible-on-windows
                // TODO: Add openForReadAtomic for case when it's not supported, cleaning up temp file and using temp
                // file if original deleted

                tempFile.rename(fileName);
            }
        });

        return stream;
    }

    /**
     * Delete this file.  If the file doesn't exist, no excepting is thrown.
     */
    public abstract void delete();

    /**
     * See if the file exists.  If there's an error checking if the file exists, this method throws an exception when
     * possible, though for some platform implementations it'll just return false if platform can't distinguish not
     * existing from there being an error checking.
     *
     * @return true if the file exists
     */
    public abstract boolean exists();

    /**
     * Rename this file, giving it a new name in the same directory.  If a file with the specified name already exists,
     * it's replaced.  If/when there's the need to move a File to a different directory, we'll add separate support for
     * that.
     *
     * @param newName
     */
    public abstract void rename(String newName);

    /**
     * Get the last modified / last write timestamp of this file.  Of the 3 file timestamps (created, modified, and
     * accessed) on files, modified is most important in Windows.  It's the one displayed by default with the "dir"
     * command and shown in file details in Explorer.  It's also the timestamp used for most other applications. So
     * that's the timestamp that JSimple best supports querying and changing.
     *
     * @return last modified timestamp for file
     */
    public abstract long getLastModifiedTime();

    /**
     * Get the size of this file.
     *
     * @return file size
     */
    public abstract long getSize();

    /**
     * Set the last modified / last write timestamp of this file.  Of the 3 file timestamps (created, modified, and
     * accessed) on files, modified is most important in Windows.  It's the one displayed by default with the "dir"
     * command and shown in file details in Explorer.  It's also the timestamp used for most other applications. So
     * that's the timestamp that JSimple best supports querying and changing.
     *
     * @param time time in millis (milliseconds since Jan 1, 1970 UTC)
     */
    public abstract void setLastModifiedTime(long time);

    public abstract Directory getParent();
}
