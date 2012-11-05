package jsimple.io;

/**
 * This class was based on, and modified from, the Apache Harmony java.io.OutputStream class.  Unlike the standard Java
 * OutputStream class, this doesn't throw any checked exceptions.
 * <p/>
 * The base class for all output streams. An output stream is a means of writing data to a target in a byte-wise manner.
 * Most output streams expect the {@link #flush()} method to be called before closing the stream, to ensure all data is
 * actually written through.
 * <p/>
 * This abstract class does not provide a fully working implementation, so it needs to be subclassed, and at least the
 * {@link #write(int)} method needs to be overridden. Overriding some of the non-abstract methods is also often advised,
 * since it might result in higher efficiency.
 *
 * @author Bret Johnson
 * @see jsimple.io.InputStream
 * @since 10/7/12 12:31 AM
 */
public abstract class OutputStream {
    /**
     * Closes this stream. Implementations of this method should free any resources used by the stream. This
     * implementation does nothing.
     *
     * @throws IOException if an error occurs while closing this stream.
     */
    public void close() {
    }

    /**
     * Flushes this stream. Implementations of this method should ensure that any buffered data is written out. This
     * implementation does nothing.
     *
     * @throws IOException if an error occurs while flushing this stream
     */
    public void flush() {
    }

    /**
     * Writes the entire contents of the byte array {@code buffer} to this stream.
     *
     * @param buffer the buffer to be written
     * @throws IOException if an error occurs while writing to this stream
     */
    public void write(byte buffer[]) throws IOException {
        write(buffer, 0, buffer.length);
    }

    /**
     * Writes {@code count} bytes from the byte array {@code buffer} starting at position {@code offset} to this
     * stream.
     *
     * @param buffer the buffer to be written
     * @param offset the start position in {@code buffer} from where to get bytes
     * @param length the number of bytes from {@code buffer} to write to this stream
     * @throws IOException if an error occurs while writing to this stream
     */
    public void write(byte buffer[], int offset, int length) {
        for (int i = offset; i < offset + length; i++)
            write(buffer[i]);
    }

    /**
     * Writes a single byte to this stream. Only the least significant byte of the integer {@code oneByte} is written to
     * the stream.
     *
     * @param oneByte the byte to be written.
     * @throws java.io.IOException if an error occurs while writing to this stream.
     */
    public abstract void write(int oneByte) throws IOException;

    /**
     * Write the string, assumed to contain only Latin1 characters (Unicode low 256 characters), to the stream.  A
     * single byte is written for each character; this the most commonly used single byte encoding for text.  If the
     * string contains any non-Latin1 characters, an exception is thrown.
     *
     * @param s string to write
     */
    public void writeLatin1EncodedString(String s) {
        write(IOUtils.toLatin1BytesFromString(s));
    }

    /**
     * Write the string to the stream using UTF-8 encoding.
     *
     * @param s string to write
     */
    public void writeUtf8EncodedString(String s) {
        int[] length = new int[1];
        byte[] bytes = IOUtils.toUtf8BytesFromString(s, length);

        write(bytes, 0, length[0]);
    }

    /**
     * Write the remaining contents of inputStream to this stream, closing the input stream when done.
     *
     * @param inputStream input stream to copy from
     */
    public void write(InputStream inputStream) {
        byte[] buffer = new byte[8*1024];

        while (true) {
            int bytesRead = inputStream.read(buffer);
            if (bytesRead < 0)
                break;
            write(buffer, 0, bytesRead);
        }

        inputStream.close();
    }
}
