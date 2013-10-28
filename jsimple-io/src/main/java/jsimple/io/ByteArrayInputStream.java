package jsimple.io;

import jsimple.util.ByteArrayRange;
import jsimple.util.PlatformUtils;

/**
 * This class was based on, and modified from, the Apache Harmony java.io.ByteArrayInputStream class.  Unlike the Java
 * InputStream class, this doesn't support mark, reset, or skip, doesn't throw any checked exceptions, and none of the
 * methods are synchronized.
 * <p/>
 * A specialized {@link java.io.InputStream } for reading the contents of a byte array.
 *
 * @author Bret Johnson
 * @since 10/8/12 8:34 PM
 */
public class ByteArrayInputStream extends InputStream {
    /**
     * The {@code byte} array containing the bytes to stream over.
     */
    protected byte[] buf;

    /**
     * The current position within the byte array.
     */
    protected int pos;

    /**
     * The total number of bytes initially available in the byte array {@code buf}.
     */
    protected int count;

    /**
     * Constructs a new {@code ByteArrayInputStream} on the byte array {@code buf}.
     *
     * @param buf the byte array to stream over.
     */
    public ByteArrayInputStream(byte buf[]) {
        this.buf = buf;
        pos = 0;
        this.count = buf.length;
    }

    /**
     * Constructs a new {@code ByteArrayInputStream} on the byte array range.
     *
     * @param byteArrayRange the byte array range to stream over
     */
    public ByteArrayInputStream(ByteArrayRange byteArrayRange) {
        this(byteArrayRange.getBytes(), byteArrayRange.getPosition(), byteArrayRange.getLength());
    }

    /**
     * Constructs a new {@code ByteArrayInputStream} on the byte array {@code buf} with the initial position set to
     * {@code offset} and the number of bytes available set to {@code offset} + {@code length}.
     *
     * @param buf    the byte array to stream over.
     * @param offset the initial position in {@code buf} to start streaming from.
     * @param length the number of bytes available for streaming.
     */
    public ByteArrayInputStream(byte buf[], int offset, int length) {
        this.buf = buf;
        pos = offset;
        count = offset + length > buf.length ? buf.length : offset + length;
    }

    @Override public void close() {
    }

    /**
     * Reads a single byte from the source byte array and returns it as an integer in the range from 0 to 255. Returns
     * -1 if the end of the source array has been reached.
     *
     * @return the byte read or -1 if the end of this stream has been reached.
     */
    @Override public int read() {
        return pos < count ? buf[pos++] & 0xFF : -1;
    }

    /**
     * Reads at most {@code len} bytes from this stream and stores them in byte array {@code b} starting at {@code
     * offset}. This implementation reads bytes from the source byte array.
     *
     * @param b      the byte array in which to store the bytes read
     * @param offset the initial position in {@code b} to store the bytes read from this stream
     * @param length the maximum number of bytes to store in {@code b}
     * @return the number of bytes actually read or -1 if no bytes were read and the end of the stream was encountered
     */
    @Override public int read(byte b[], int offset, int length) {
        // Are there any bytes available?
        if (this.pos >= this.count)
            return -1;

        if (length == 0)
            return 0;

        int copyLength = this.count - pos < length ? this.count - pos : length;
        PlatformUtils.copyBytes(buf, pos, b, offset, copyLength);
        pos += copyLength;
        return copyLength;
    }
}
