using System.Collections.Generic;

namespace jsimple.io
{

	using BasicException = jsimple.util.BasicException;

	/// <summary>
	/// This class was based on, and modified from, the Apache Harmony java.io.SequenceInputStream class.  Unlike the
	/// standard Java SequenceInputStream class, uses as Iterator instead of the legacy JDK 1.0 Enumeration class to hold the
	/// list of InputStreams.  That, plus of course using JSimple InputStreams, were the only changes.
	/// <p/>
	/// Concatenates two or more existing <seealso cref="InputStream"/>s. Reads are taken from the first stream until it ends, then the
	/// next stream is used, until the last stream returns end of file.
	/// </summary>
	public class SequenceInputStream : InputStream
	{
		/// <summary>
		/// An iterator which will return types of InputStream.
		/// </summary>
		private List<InputStream> inputStreams;
		private int currentInputStreamIndex;

		/// <summary>
		/// The current input stream.
		/// </summary>
		private InputStream currentInputStream;

		/// <summary>
		/// Constructs a new {@code SequenceInputStream} using the two streams {@code s1} and {@code s2} as the sequence of
		/// streams to read from.
		/// </summary>
		/// <param name="s1"> the first stream to get bytes from. </param>
		/// <param name="s2"> the second stream to get bytes from. </param>
		/// <exception cref="NullPointerException"> if {@code s1} is {@code null}. </exception>
		public SequenceInputStream(InputStream s1, InputStream s2)
		{
			if (s1 == null)
				throw new System.NullReferenceException();

			inputStreams = new List<InputStream>(2);
			inputStreams.Add(s1);
			inputStreams.Add(s2);

			currentInputStreamIndex = 0;
			currentInputStream = s1;
		}

		/// <summary>
		/// Constructs a new SequenceInputStream using the elements returned from {@code inputStreams} as the stream
		/// sequence.
		/// </summary>
		/// <param name="inputStreams"> {@code ArrayList} of {@code InputStreams} to get bytes from </param>
		public SequenceInputStream(List<InputStream> inputStreams)
		{
			this.inputStreams = inputStreams;

			currentInputStreamIndex = 0;

			if (inputStreams.Count > 0)
				currentInputStream = inputStreams[currentInputStreamIndex];
			else
				currentInputStream = null;
		}

		/// <summary>
		/// Closes all streams in this sequence of input stream.
		/// </summary>
		public override void close()
		{
			while (currentInputStream != null)
				nextStream();
		}

		/// <summary>
		/// Sets up the next InputStream or leaves it alone if there are none left.
		/// </summary>
		private void nextStream()
		{
			if (currentInputStream != null)
				currentInputStream.close();

			if (currentInputStreamIndex < inputStreams.Count - 1)
			{
				++currentInputStreamIndex;
				currentInputStream = inputStreams[currentInputStreamIndex];

				if (currentInputStream == null)
					throw new BasicException("InputStream at index {} unexpectedly null in SequenceInputStream list", currentInputStreamIndex);
			}
			else
				currentInputStream = null;
		}

		/// <summary>
		/// Reads a single byte from this sequence of input streams and returns it as an integer in the range from 0 to 255.
		/// It tries to read from the current stream first; if the end of this stream has been reached, it reads from the
		/// next one. Blocks until one byte has been read, the end of the last input stream in the sequence has been reached,
		/// or an exception is thrown.
		/// </summary>
		/// <returns> the byte read or -1 if either the end of the last stream in the sequence has been reached or this input
		/// stream sequence is closed. </returns>
		public override int read()
		{
			while (currentInputStream != null)
			{
				int result = currentInputStream.read();
				if (result >= 0)
					return result;
				nextStream();
			}
			return -1;
		}

		/// <summary>
		/// Reads at most {@code count} bytes from this sequence of input streams and stores them in the byte array {@code
		/// buffer} starting at {@code offset}. Blocks only until at least 1 byte has been read, the end of the stream has
		/// been reached, or an exception is thrown.
		/// <p/>
		/// This SequenceInputStream shows the same behavior as other InputStreams. To do this it will read only as many
		/// bytes as a call to read on the current substream returns. If that call does not return as many bytes as requested
		/// by {@code count}, it will not retry to read more on its own because subsequent reads might block. This would
		/// violate the rule that it will only block until at least one byte has been read.
		/// <p/>
		/// If a substream has already reached the end when this call is made, it will close that substream and start with
		/// the next one. If there are no more substreams it will return -1.
		/// </summary>
		/// <param name="buffer"> the array in which to store the bytes read. </param>
		/// <param name="offset"> the initial position in {@code buffer} to store the bytes read from this stream. </param>
		/// <param name="count">  the maximum number of bytes to store in {@code buffer}. </param>
		/// <returns> the number of bytes actually read; -1 if this sequence of streams is closed or if the end of the last
		/// stream in the sequence has been reached. </returns>
		/// <exception cref="IndexOutOfBoundsException"> if {@code offset < 0} or {@code count < 0}, or if {@code offset + count} is
		///                                   greater than the size of {@code buffer}. </exception>
		public override int read(sbyte[] buffer, int offset, int count)
		{
			if (currentInputStream == null)
				return -1;

			while (currentInputStream != null)
			{
				int result = currentInputStream.read(buffer, offset, count);
				if (result >= 0)
					return result;

				nextStream();
			}

			return -1;
		}
	}

}