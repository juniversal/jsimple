using System;

namespace jsimple.io
{

	/// <summary>
	/// This class was based on, and modified from, the Apache Harmony java.io.ByteArrayInputStream class.  Unlike the Java
	/// InputStream class, this doesn't support mark, reset, or skip, doesn't throw any checked exceptions, and none of the
	/// methods are synchronized.
	/// <p/>
	/// A specialized <seealso cref="java.io.InputStream "/> for reading the contents of a byte array.
	/// 
	/// @author Bret Johnson
	/// @since 10/8/12 8:34 PM
	/// </summary>
	public class ByteArrayInputStream : InputStream
	{
		/// <summary>
		/// The {@code byte} array containing the bytes to stream over.
		/// </summary>
		protected internal sbyte[] buf;

		/// <summary>
		/// The current position within the byte array.
		/// </summary>
		protected internal int pos;

		/// <summary>
		/// The total number of bytes initially available in the byte array {@code buf}.
		/// </summary>
		protected internal int count;

		/// <summary>
		/// Constructs a new {@code ByteArrayInputStream} on the byte array {@code buf}.
		/// </summary>
		/// <param name="buf"> the byte array to stream over. </param>
		public ByteArrayInputStream(sbyte[] buf)
		{
			this.buf = buf;
			pos = 0;
			this.count = buf.Length;
		}

		public override void close()
		{
		}

		/// <summary>
		/// Constructs a new {@code ByteArrayInputStream} on the byte array {@code buf} with the initial position set to
		/// {@code offset} and the number of bytes available set to {@code offset} + {@code length}.
		/// </summary>
		/// <param name="buf">    the byte array to stream over. </param>
		/// <param name="offset"> the initial position in {@code buf} to start streaming from. </param>
		/// <param name="length"> the number of bytes available for streaming. </param>
		public ByteArrayInputStream(sbyte[] buf, int offset, int length)
		{
			this.buf = buf;
			pos = offset;
			count = offset + length > buf.Length ? buf.Length : offset + length;
		}

		/// <summary>
		/// Reads a single byte from the source byte array and returns it as an integer in the range from 0 to 255. Returns
		/// -1 if the end of the source array has been reached.
		/// </summary>
		/// <returns> the byte read or -1 if the end of this stream has been reached. </returns>
		public override int read()
		{
			return pos < count ? buf[pos++] & 0xFF : -1;
		}

		/// <summary>
		/// Reads at most {@code len} bytes from this stream and stores them in byte array {@code b} starting at {@code
		/// offset}. This implementation reads bytes from the source byte array.
		/// </summary>
		/// <param name="b">      the byte array in which to store the bytes read </param>
		/// <param name="offset"> the initial position in {@code b} to store the bytes read from this stream </param>
		/// <param name="length"> the maximum number of bytes to store in {@code b} </param>
		/// <returns> the number of bytes actually read or -1 if no bytes were read and the end of the stream was encountered </returns>
		public override int read(sbyte[] b, int offset, int length)
		{
			// Are there any bytes available?
			if (this.pos >= this.count)
				return -1;

			if (length == 0)
				return 0;

			int copyLength = this.count - pos < length ? this.count - pos : length;
			Array.Copy(buf, pos, b, offset, copyLength);
			pos += copyLength;
			return copyLength;
		}
	}

}