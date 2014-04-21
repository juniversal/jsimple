namespace jsimple.util {

    /// <summary>
    /// This class represents a subset of a byte array, starting at the specified position & of the specified length.
    /// Basically, it's used as a shortcut to passing around the array, position, and length separately.
    /// 
    /// @author Bret Johnson
    /// @since 8/3/13 10:46 AM
    /// </summary>
    public class ByteArrayRange {
        private sbyte[] bytes;
        private int position;
        private int length;

        public ByteArrayRange(sbyte[] bytes, int position, int length) {
            this.bytes = bytes;
            this.position = position;
            this.length = length;
        }

        public ByteArrayRange(sbyte[] bytes) {
            this.bytes = bytes;
            this.position = 0;
            this.length = bytes.Length;
        }

        public virtual sbyte[] Bytes {
            get {
                return bytes;
            }
        }

        public virtual int Position {
            get {
                return position;
            }
        }

        public virtual int Length {
            get {
                return length;
            }
        }

        /// <summary>
        /// Get a byte array just for this range.  If the range occupies the full source array, then the source array is
        /// returned rather than making a copy.  The caller should be aware that the returned byte array may or may not be
        /// the same as the source array.  Generally, it's best to only use this when you are sure both the source array and
        /// returned byte array won't be changed.
        /// 
        /// @return
        /// </summary>
        public virtual sbyte[] toByteArray() {
            if (position == 0 && bytes.Length == length)
                return this.bytes;
            else {
                sbyte[] copy = new sbyte[length];
                PlatformUtils.copyBytes(bytes, position, copy, 0, length);
                return copy;
            }
        }
    }

}