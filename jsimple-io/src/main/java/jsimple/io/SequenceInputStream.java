package jsimple.io;

import jsimple.util.BasicException;

import java.util.ArrayList;

/**
 * This class was based on, and modified from, the Apache Harmony java.io.SequenceInputStream class.  Unlike the
 * standard Java SequenceInputStream class, uses as Iterator instead of the legacy JDK 1.0 Enumeration class to hold the
 * list of InputStreams.  That, plus of course using JSimple InputStreams, were the only changes.
 * <p/>
 * Concatenates two or more existing {@link InputStream}s. Reads are taken from the first stream until it ends, then the
 * next stream is used, until the last stream returns end of file.
 */
public class SequenceInputStream extends InputStream {
    /**
     * An iterator which will return types of InputStream.
     */
    private ArrayList<InputStream> inputStreams;
    private int currentInputStreamIndex;

    /**
     * The current input stream.
     */
    private InputStream currentInputStream;

    /**
     * Constructs a new {@code SequenceInputStream} using the two streams {@code s1} and {@code s2} as the sequence of
     * streams to read from.
     *
     * @param s1 the first stream to get bytes from.
     * @param s2 the second stream to get bytes from.
     * @throws NullPointerException if {@code s1} is {@code null}.
     */
    public SequenceInputStream(InputStream s1, InputStream s2) {
        if (s1 == null)
            throw new NullPointerException();

        ArrayList<InputStream> inVector = new ArrayList<InputStream>(1);
        inVector.add(s2);
        currentInputStreamIndex = 0;
        currentInputStream = s1;
    }

    /**
     * Constructs a new SequenceInputStream using the elements returned from {@code inputStreams} as the stream
     * sequence.
     *
     * @param inputStreams {@code ArrayList} of {@code InputStreams} to get bytes from
     */
    public SequenceInputStream(ArrayList<InputStream> inputStreams) {
        this.inputStreams = inputStreams;

        currentInputStreamIndex = 0;

        if (inputStreams.size() > 0)
            currentInputStream = inputStreams.get(currentInputStreamIndex);
        else currentInputStream = null;
    }

    /**
     * Closes all streams in this sequence of input stream.
     */
    @Override public void close() {
        while (currentInputStream != null)
            nextStream();
    }

    /**
     * Sets up the next InputStream or leaves it alone if there are none left.
     */
    private void nextStream() {
        if (currentInputStream != null)
            currentInputStream.close();

        if (currentInputStreamIndex < inputStreams.size() - 1) {
            ++currentInputStreamIndex;
            currentInputStream = inputStreams.get(currentInputStreamIndex);

            if (currentInputStream == null)
                throw new BasicException("InputStream at index {} unexpectedly null in SequenceInputStream list",
                        currentInputStreamIndex);
        }
        else currentInputStream = null;
    }

    /**
     * Reads a single byte from this sequence of input streams and returns it as an integer in the range from 0 to 255.
     * It tries to read from the current stream first; if the end of this stream has been reached, it reads from the
     * next one. Blocks until one byte has been read, the end of the last input stream in the sequence has been reached,
     * or an exception is thrown.
     *
     * @return the byte read or -1 if either the end of the last stream in the sequence has been reached or this input
     *         stream sequence is closed.
     */
    @Override public int read() {
        while (currentInputStream != null) {
            int result = currentInputStream.read();
            if (result >= 0)
                return result;
            nextStream();
        }
        return -1;
    }

    /**
     * Reads at most {@code count} bytes from this sequence of input streams and stores them in the byte array {@code
     * buffer} starting at {@code offset}. Blocks only until at least 1 byte has been read, the end of the stream has
     * been reached, or an exception is thrown.
     * <p/>
     * This SequenceInputStream shows the same behavior as other InputStreams. To do this it will read only as many
     * bytes as a call to read on the current substream returns. If that call does not return as many bytes as requested
     * by {@code count}, it will not retry to read more on its own because subsequent reads might block. This would
     * violate the rule that it will only block until at least one byte has been read.
     * <p/>
     * If a substream has already reached the end when this call is made, it will close that substream and start with
     * the next one. If there are no more substreams it will return -1.
     *
     * @param buffer the array in which to store the bytes read.
     * @param offset the initial position in {@code buffer} to store the bytes read from this stream.
     * @param count  the maximum number of bytes to store in {@code buffer}.
     * @return the number of bytes actually read; -1 if this sequence of streams is closed or if the end of the last
     *         stream in the sequence has been reached.
     * @throws IndexOutOfBoundsException if {@code offset < 0} or {@code count < 0}, or if {@code offset + count} is
     *                                   greater than the size of {@code buffer}.
     */
    @Override public int read(byte[] buffer, int offset, int count) {
        if (currentInputStream == null)
            return -1;

        while (currentInputStream != null) {
            int result = currentInputStream.read(buffer, offset, count);
            if (result >= 0)
                return result;

            nextStream();
        }

        return -1;
    }
}
