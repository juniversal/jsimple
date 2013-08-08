package jsimple.util;

/**
 * This class represents a subset of a byte array, starting at the specified position & of the specified length.
 * Basically, it's used as a shortcut to passing around the array, position, and length separately.
 *
 * @author Bret Johnson
 * @since 8/3/13 10:46 AM
 */
public class ByteArrayRange {
    private byte[] bytes;
    private int position;
    private int length;

    public ByteArrayRange(byte[] bytes, int position, int length) {
        this.bytes = bytes;
        this.position = position;
        this.length = length;
    }

    public ByteArrayRange(byte[] bytes) {
        this.bytes = bytes;
        this.position = 0;
        this.length = bytes.length;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getPosition() {
        return position;
    }

    public int getLength() {
        return length;
    }

    /**
     * Get a byte array just for this range.  If the range occupies the full source array, then the source array is
     * returned rather than making a copy.  The caller should be aware that the returned byte array may or may not be
     * the same as the source array.  Generally, it's best to only use this when you are sure both the source array and
     * returned byte array won't be changed.
     *
     * @return
     */
    public byte[] toByteArray() {
        if (position == 0 && bytes.length == length)
            return this.bytes;
        else {
            byte[] copy = new byte[length];
            System.arraycopy(bytes, position, copy, 0, length);
            return copy;
        }
    }
}
