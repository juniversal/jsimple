namespace jsimple.io {

    /// <summary>
    /// @author Bret Johnson
    /// @since 8/22/13 3:19 PM
    /// </summary>
    public class SubstreamInputStream : InputStream {
        private InputStream outerInputStream;
        private int lengthRemaining;

        public SubstreamInputStream(InputStream inputStream, int length) {
            this.outerInputStream = inputStream;
            this.lengthRemaining = length;
        }

        /// <summary>
        /// For a SubstreamInputStream we of course don't want to close the outer stream when this stream is closed, as
        /// there's more of the outer stream that follows this one.
        /// </summary>
        public override void close() {
        }

        public override int read() {
            // If at the end of our substream, return -1
            if (lengthRemaining <= 0)
                return -1;

            int byteRead = outerInputStream.read();
            if (byteRead == -1)
                throw new IOException("SubstreamInputStream outer stream returned end of stream prematurely; there are {} bytes left to read", lengthRemaining);

            lengthRemaining -= 1;
            return byteRead;
        }

        public override int read(sbyte[] buffer, int offset, int length) {
            // Return 0 if not asked to read anything, per the InputStream spec
            if (length == 0)
                return 0;

            // If at the end of our substream, return -1
            if (lengthRemaining <= 0)
                return -1;

            if (length > lengthRemaining)
                length = lengthRemaining;

            int amountRead = outerInputStream.read(buffer, offset, length);
            if (amountRead == -1)
                throw new IOException("SubstreamInputStream outer stream returned end of stream prematurely; there are {} bytes left to read", lengthRemaining);

            lengthRemaining -= amountRead;
            return amountRead;
        }
    }

}