namespace jsimple.io {

    /// <summary>
    /// @author Bret Johnson
    /// @since 11/22/12 12:14 AM
    /// </summary>
    public abstract class File : Path {
        public abstract InputStream openForRead();

        /// <summary>
        /// Copy the contents of this file to the specified output stream.  If an error occurs when copying, the file input
        /// stream is closed (as you'd expect), an exception is thrown, and the output stream may be partially written to.
        /// </summary>
        /// <param name="outputStream"> output stream to write to </param>
        public virtual void copyTo(OutputStream outputStream) {
            using (InputStream inputStream = openForRead()) {
                inputStream.copyTo(outputStream);
            }
        }

        /// <summary>
        /// This is a convenience method to open a UTF8 text file for read.  It does the same thing as calling {@code new
        /// Utf8InputStreamReader(openForRead())}.
        /// </summary>
        /// <returns> Utf8InputStreamReader for reading from this text file </returns>
        public virtual Utf8InputStreamReader openUtf8ForRead() {
            return new Utf8InputStreamReader(openForRead());
        }

        /// <summary>
        /// Open the file for writing.  If the file already exists, it is truncated.  If the file doesn't exist, it is
        /// created. Note that the current JSimple file I/O model is that files, when written, are completely rewritten.
        /// </summary>
        /// <returns> output stream, to write the file contents </returns>
        public abstract OutputStream openForCreate();

        /// <summary>
        /// This is a convenience method to open a UTF8 text file for writing.  It does the same thing as calling {@code new
        /// Utf8OutputStreamWriter(openForWrite())}.
        /// </summary>
        /// <returns> Utf8OutputStreamWriter for writing to this text file </returns>
        public virtual Utf8OutputStreamWriter openUtf8ForCreate() {
            return new Utf8OutputStreamWriter(openForCreate());
        }

        /// <summary>
        /// This is a convenience method to open a UTF8 text file for writing.  It does the same thing as calling {@code new
        /// Utf8OutputStreamWriter(openForCreateAtomic())}.
        /// </summary>
        /// <returns> Utf8OutputStreamWriter for writing to this text file </returns>
        public virtual Utf8OutputStreamWriter openUtf8ForCreateAtomic() {
            return new Utf8OutputStreamWriter(openForCreateAtomic());
        }

        public virtual OutputStream openForCreateAtomic() {
            return openForCreateAtomic(0);
        }

        /// <summary>
        /// Open the file for writing/creating, replacing the current file if it already exists.
        /// <p/>
        /// All writes are first made to a temp file (filename + "-temp" suffix, in the same directory).  When the returned
        /// stream is closed, the new file is committed by renaming the -temp file to the original.  That way, the file is
        /// either completely updated or not updated at all, never left in a half-updated state.
        /// <p/>
        /// If lastModifiedTime is non-zero, then it's set as the last modified time on the file, as part of the atomic
        /// update (the timestamp is set on the temp file first & preserved on the rename).
        /// </summary>
        /// <param name="lastModifiedTime"> modification time in millis, set on updated file; ignored if zero </param>
        /// <returns> OutputStream, with a close handler attached to update the original file when closed </returns>
        public virtual OutputStream openForCreateAtomic(long lastModifiedTime) {
            string fileName = Name;
            File tempFile = Parent.getFile(fileName + "-temp");

            OutputStream stream = tempFile.openForCreate();

            stream.ClosedListener = new ClosedListenerAnonymousInnerClassHelper(this, lastModifiedTime, fileName, tempFile);

            return stream;
        }

        private class ClosedListenerAnonymousInnerClassHelper : ClosedListener {
            private readonly File outerInstance;

            private long lastModifiedTime;
            private string fileName;
            private jsimple.io.File tempFile;

            public ClosedListenerAnonymousInnerClassHelper(File outerInstance, long lastModifiedTime, string fileName, jsimple.io.File tempFile) {
                this.outerInstance = outerInstance;
                this.lastModifiedTime = lastModifiedTime;
                this.fileName = fileName;
                this.tempFile = tempFile;
            }

            public virtual void onClosed() {
                if (lastModifiedTime != 0)
                    tempFile.LastModifiedTime = lastModifiedTime;

                // TODO: Switch to do atomic rename when supported, using this:
                // http://stackoverflow.com/questions/167414/is-an-atomic-file-rename-with-overwrite-possible-on-windows
                // TODO: Add openForReadAtomic for case when it's not supported, cleaning up temp file and using temp
                // file if original deleted

                tempFile.renameTo(fileName);
            }
        }

        /// <summary>
        /// Delete this file if it exists.  If the file doesn't exist, no exception is thrown.
        /// </summary>
        public abstract void delete();

        /// <summary>
        /// See if the file exists.  If there's an error checking if the file exists, this method throws an exception when
        /// possible, though for some platform implementations it'll just return false if platform can't distinguish not
        /// existing from there being an error checking.
        /// </summary>
        /// <returns> true if the file exists </returns>
        public abstract bool exists();

        /// <summary>
        /// Rename this file, giving it a new name in the same directory.  If a file with the specified name already exists,
        /// it's replaced.  If/when there's the need to move a File to a different directory, we'll add separate support for
        /// that.
        /// </summary>
        /// <param name="newName"> </param>
        public abstract void renameTo(string newName);

        public abstract void moveTo(File destination);

        /// <summary>
        /// Get the last modified / last write timestamp of this file.  Of the 3 file timestamps (created, modified, and
        /// accessed) on files, modified is most important in Windows.  It's the one displayed by default with the "dir"
        /// command and shown in file details in Explorer.  It's also the timestamp used for most other applications. So
        /// that's the timestamp that JSimple best supports querying and changing.
        /// </summary>
        /// <returns> last modified timestamp for file </returns>
        public abstract long LastModifiedTime {get;set;}

        /// <summary>
        /// Get the size of this file.
        /// </summary>
        /// <returns> file size </returns>
        public abstract long Size {get;}


        public abstract Directory Parent {get;}
    }

}