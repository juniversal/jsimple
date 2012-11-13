package jsimple.io;

/**
 * This class was based on, and modified from, the Apache Harmony java.io.InputStream class.  Unlike the Java
 * InputStream class, this doesn't support mark, reset, or skip, doesn't throw any checked exceptions, and none of the
 * methods are synchronized.
 * <p/>
 * The base class for all input streams. An input stream is a means of reading data from a source in a byte-wise
 * manner.
 * <p/>
 * Some input streams also support marking a position in the input stream and returning to this position later. This
 * abstract class does not provide a fully working implementation, so it needs to be subclassed, and at least the {@link
 * #read()} method needs to be overridden. Overriding some of the non-abstract methods is also often advised, since it
 * might result in higher efficiency.
 *
 * @author Bret Johnson
 * @see jsimple.io.OutputStream
 * @since 10/7/12 12:31 AM
 */
public abstract class InputStream extends java.io.InputStream {
    /**
     * Closes this stream.  If the stream is already closed, then this method should do nothing.  Concrete
     * implementations of this class should free any resources during close.
     *
     * @throws IOException if an error occurs while closing this stream
     */
    public void close() {
    }

    /**
     * Reads a single byte from this stream and returns it as an integer in the range from 0 to 255. Returns -1 if the
     * end of the stream has been reached. Blocks until one byte has been read, the end of the source stream is detected
     * or an exception is thrown.
     *
     * @return the byte read or -1 if the end of stream has been reached
     * @throws IOException if the stream is closed or another IOException occurs
     */
    public int read() {
        byte[] buffer = new byte[1];
        int result = read(buffer, 0, 1);
        return result == -1 ? -1 : buffer[0] & 0xff;
    }

    /**
     * Reads bytes from this stream and stores them in the byte array {@code b}.
     *
     * @param buffer the byte array in which to store the bytes read
     * @return the number of bytes actually read or -1 if the end of the stream has been reached
     * @throws IOException if this stream is closed or another IOException occurs
     */
    public int read(byte buffer[]) {
        return read(buffer, 0, buffer.length);
    }

    /**
     * Reads at most {@code length} bytes from this stream and stores them in the byte array {@code b} starting at
     * {@code offset}.
     *
     * @param buffer the byte array in which to store the bytes read
     * @param offset the initial position in {@code buffer} to store the bytes read from this stream
     * @param length the maximum number of bytes to store in {@code b}
     * @return the number of bytes actually read or -1 if the end of the stream has been reached
     * @throws IOException if the stream is closed or another IOException occurs reading the first byte
     */
    public int read(byte buffer[], int offset, int length) {
        for (int i = 0; i < length; i++) {
            int c;
            try {
                if ((c = read()) == -1)
                    return i == 0 ? -1 : i;
            } catch (IOException e) {
                if (i != 0)
                    return i;
                throw e;
            }
            buffer[offset + i] = (byte) c;
        }
        return length;
    }
}
