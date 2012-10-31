package jsimple.io;

/*
 * This class was based on, and modified from, the Apache Harmony java.io.FileInputStream class.  Unlike the standard
 * Java class, this version doesn't support mark, reset, or skip, doesn't throw any checked exceptions, and none of the
 * methods are synchronized.
 *
 * A specialized {@link java.io.InputStream} that reads from a file in the file system.
 * All read requests made by calling methods in this class are directly
 * forwarded to the equivalent function of the underlying operating system.
 * Since this may induce some performance penalty, in particular if many small
 * read requests are made, a FileInputStream is often wrapped by a
 * BufferedInputStream.
 *
 * @author Bret Johnson
 * @since 10/9/12 12:10 PM
 */
public class FileInputStream extends InputStream {
    java.io.FileInputStream javaFileInputStream;

    /**
     * Constructs a new {@code FileInputStream} on the file named {@code fileName}. The path of {@code fileName} may be
     * absolute or relative.
     *
     * @param fileName the path and name of the file from which this stream reads
     * @throws FileNotFoundException if there is no file named {@code fileName}
     */
    public FileInputStream(String fileName) throws FileNotFoundException {
        try {
            javaFileInputStream = new java.io.FileInputStream(fileName);
        } catch (java.io.FileNotFoundException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    /**
     * Closes this stream.
     *
     * @throws IOException if an error occurs attempting to close this stream
     */
    @Override public void close() throws IOException {
        try {
            javaFileInputStream.close();
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    /**
     * Ensures that all resources for this stream are released when it is about to be garbage collected
     *
     * @throws java.io.IOException if an error occurs attempting to finalize this stream
     */
    @Override protected void finalize() throws IOException {
        close();
    }

    /**
     * Reads a single byte from this stream and returns it as an integer in the range from 0 to 255. Returns -1 if the
     * end of this stream has been reached.
     *
     * @return the byte read or -1 if the end of this stream has been reached.
     * @throws java.io.IOException if this stream is closed or another I/O error occurs.
     */
    @Override public int read() throws IOException {
        try {
            return javaFileInputStream.read();
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    /**
     * Reads bytes from this stream and stores them in the byte array {@code buffer}.
     *
     * @param buffer the byte array in which to store the bytes read.
     * @return the number of bytes actually read or -1 if the end of the stream has been reached.
     * @throws java.io.IOException if this stream is closed or another I/O error occurs.
     */
    @Override public int read(byte[] buffer) throws IOException {
        try {
            return javaFileInputStream.read(buffer);
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }

    /**
     * Reads at most {@code count} bytes from this stream and stores them in the byte array {@code buffer} starting at
     * {@code offset}.
     *
     * @param buffer the byte array in which to store the bytes read.
     * @param offset the initial position in {@code buffer} to store the bytes read from this stream.
     * @param count  the maximum number of bytes to store in {@code buffer}.
     * @return the number of bytes actually read or -1 if the end of the stream has been reached.
     * @throws IndexOutOfBoundsException if {@code offset < 0} or {@code count < 0}, or if {@code offset + count} is
     *                                   greater than the size of {@code buffer}.
     * @throws java.io.IOException       if the stream is closed or another IOException occurs.
     */
    @Override public int read(byte[] buffer, int offset, int count) throws IOException {
        try {
            return javaFileInputStream.read(buffer, offset, count);
        } catch (java.io.IOException e) {
            throw JavaIOUtils.jSimpleExceptionFromJavaIOException(e);
        }
    }
}
