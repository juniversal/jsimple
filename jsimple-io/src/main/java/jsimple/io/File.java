package jsimple.io;

import jsimple.util.BasicException;

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

    public abstract OutputStream openForCreateAtomic() ;
    /*

    public OutputStream openForCreateAtomic() {
        // FIRST CREATE OutputStream, to file name + "-temp"

        // SET ClosedListener for OutputStream SO WHEN CALLBACK TO IT, AFTER CLOSE, CAN DELETE ORIGINAL FILE AND
        // RENAME -temp to ORIGINAL FILE

        /*

        File tempFile = new File(...);
        OutputStream stream = tempFile.openForCreate();
        stream.setCLosedHandler(new OutputStream.ClosedHandler {
            void onClosed() {
                // DELETE ORIGINAL FILE
                // RENAME -temp TO ORIGINAL NAME
            }
        });




        // TODO: IMPLEMENT THIS
        return null;
    }
    */

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

    public abstract  void rename(String newName);

    /*
    public  abstract void rename(String newName) {
        throw new BasicException("rename not implemented for this implementation");
    }
    */
}
