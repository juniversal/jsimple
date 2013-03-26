package jsimple.io;

/**
 * This class was based on, and modified from, the Apache Harmony java.io.ByteArrayOutputStream class.  Unlike the Java
 * OutputStream class, none of the methods are synchronized & this class provides a way to get the underlying byte array
 * without copying it.
 * <p/>
 * A specialized {@link OutputStream} for class for writing content to an (internal) byte array. As bytes are written to
 * this stream, the byte array may be expanded to hold more bytes. When the writing is considered to be finished, the
 * byte array can be requested from the class.
 *
 * @see ByteArrayInputStream
 */
public class ByteArrayOutputStream extends OutputStream {
    /**
     * The byte array containing the bytes written.
     */
    protected byte[] buffer;

    /**
     * The number of bytes written.
     */
    protected int count;

    /**
     * Constructs a new ByteArrayOutputStream with a default size of 32 bytes. If more than 32 bytes are written to this
     * instance, the underlying byte array will expand.
     */
    public ByteArrayOutputStream() {
        super();
        buffer = new byte[32];
    }

    /**
     * Constructs a new {@code ByteArrayOutputStream} with a default size of {@code size} bytes. If more than {@code
     * size} bytes are written to this instance, the underlying byte array will expand.
     *
     * @param size initial size for the underlying byte array, must be non-negative
     */
    public ByteArrayOutputStream(int size) {
        buffer = new byte[size];
    }

    /**
     * Closes this stream. This releases system resources used for this stream.
     */
    @Override public void close() {
        // Although the spec claims "A closed stream cannot perform output operations and cannot be reopened.", this
        // implementation must do nothing.
        super.close();
    }

    /**
     * Closes this stream and returns the byte array associated with it.  Note that the returned array may be the
     * original buffer used by this stream or a copy; the original is used if it's already exactly the right size (with
     * no extra space at the end), otherwise a copy is made with the right size.
     *
     * @return byte array associated with stream
     */
    public byte[] closeAndGetByteArray() {
        byte[] data;
        if (buffer.length == count)
            data = buffer;
        else data = toByteArray();

        close();
        buffer = null;

        return data;
    }

    /**
     * Closes the stream and returns its contents as a byte array.  Only the first length[0] bytes of this array contain
     * the stream data.  This just returns a reference to the internal byte array.  That has the advantage of
     * potentially better performance--no copy is needed--but less convenience than getting a byte array of exactly the
     * right size.  If the returned byte array will exist of a while, it can also be more memory efficient to get a
     * version that's no bigger than needed.
     *
     * @return this stream's current contents as a byte array; the array can be arbitrarily big, but only the first
     *         length[0] bytes contain stream data
     */
    public byte[] closeAndGetByteArray(int[] length) {
        length[0] = count;
        byte[] data = buffer;

        close();
        buffer = null;

        return data;
    }

    private void expand(int i) {
        // Can the buffer handle @i more bytes?  If so, return
        if (count + i <= buffer.length)
            return;

        byte[] newBuffer = new byte[(count + i) * 2];
        System.arraycopy(buffer, 0, newBuffer, 0, count);
        buffer = newBuffer;
    }

    /**
     * Resets this stream to the beginning of the underlying byte array. All subsequent writes will overwrite any bytes
     * previously stored in this stream.
     */
    public void reset() {
        count = 0;
    }

    /**
     * Returns the total number of bytes written to this stream so far.
     *
     * @return the number of bytes written to this stream
     */
    public int getLength() {
        return count;
    }

    /**
     * Returns the contents of this ByteArrayOutputStream as a byte array.  Only the first length[0] bytes of this array
     * contain the stream data.  Unlike the standard Java implementation, this just returns a reference to the internal
     * byte array.  That has the advantage of superior performance--no needless copy is needed--but the caller should be
     * careful not to modify the array & be aware that it can change as more data is written to the stream.
     *
     * @return this stream's current contents as a byte array; the array can be arbitrarily big, but only the first
     *         length[0] bytes contain stream data
     */
    public byte[] getByteArray(int[] length) {
        length[0] = count;
        return buffer;
    }

    /**
     * Returns the contents of this ByteArrayOutputStream as a byte array. Unlike getByteArray(int[] length) this method
     * returns an array of exactly the right size, containing just the data in question and no more, and the returned
     * array is a copy of the data, so the caller need not be concerned about the array changing as more data is
     * written.  However, that convenience comes at the expense of performance, as an extra copy is required.
     *
     * @return a copy of the contents of this stream
     */
    public byte[] toByteArray() {
        byte[] copy = new byte[count];
        System.arraycopy(buffer, 0, copy, 0, count);
        return copy;
    }

    /**
     * Writes {@code count} bytes from the byte array {@code buffer} starting at offset {@code index} to this stream.
     *
     * @param buffer the buffer to be written.
     * @param offset the initial position in {@code buffer} to retrieve bytes.
     * @param length the number of bytes of {@code buffer} to write.
     * @throws NullPointerException      if {@code buffer} is {@code null}.
     * @throws IndexOutOfBoundsException if {@code offset < 0} or {@code len < 0}, or if {@code offset + len} is greater
     *                                   than the length of {@code buffer}.
     */
    @Override public void write(byte[] buffer, int offset, int length) {
        // Expand if necessary
        expand(length);

        System.arraycopy(buffer, offset, this.buffer, this.count, length);
        this.count += length;
    }

    /**
     * Writes the specified byte {@code oneByte} to the OutputStream. Only the low order byte of {@code oneByte} is
     * written.
     *
     * @param oneByte the byte to be written.
     */
    @Override public void write(int oneByte) {
        if (count == buffer.length)
            expand(1);
        buffer[count++] = (byte) oneByte;
    }
}
