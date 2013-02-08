using System;
using System.Diagnostics;

namespace jsimple.io
{



	/// <summary>
	/// This class was based on, and modified from, the Apache Harmony java.io.OutputStreamWriter class.  Unlike the standard
	/// Java Writer subclasses, this doesn't throw any checked exceptions.
	/// <p/>
	/// A class for turning a character stream into a byte stream. Data written to the target writer is converted into bytes,
	/// using UTF-8 encoding.
	/// 
	/// @author Bret Johnson </summary>
	/// <seealso cref= Utf8InputStreamReader
	/// @since 10/14/12 9:52 PM </seealso>
	public class Utf8OutputStreamWriter : Writer
	{
		private OutputStream @out;
		private sbyte[] destBuffer;
		private int destPosition = 0;
		private int queuedLeadSurrogate = -1;
		private const int BUFFER_SIZE = 1024;

		/// <summary>
		/// Constructs a new OutputStreamWriter using {@code out} as the target stream to write converted characters to.
		/// </summary>
		/// <param name="out"> the non-null target stream to write converted bytes to </param>
		public Utf8OutputStreamWriter(OutputStream @out)
		{
			this.@out = @out;
			destBuffer = new sbyte[BUFFER_SIZE];
		}

		/// <summary>
		/// @Override protected void finalize() {
		///    close();
		/// }
		/// 
		/// /**
		/// Closes this writer. This implementation flushes the buffer as well as the target stream. The target stream is
		/// then closed and the resources for the buffer and converter are released.
		/// <p/>
		/// Only the first invocation of this method has any effect. Subsequent calls do nothing.
		/// </summary>
		/// <exception cref="IOException"> if an error occurs while closing this writer. </exception>
		public override void close()
		{
			// If already closed, do nothing
			if (@out == null)
				return;

			flush();

			Debug.Assert(@out != null, "@SuppressWarnings(nullness)");
			@out.close();
			@out = null;
		}

		/// <summary>
		/// Flushes this writer. This implementation ensures that all buffered bytes are written to the target stream. After
		/// writing the bytes, the target stream is flushed as well.
		/// </summary>
		/// <exception cref="IOException"> if an error occurs while flushing this writer </exception>
		public override void flush()
		{
			if (@out == null)
				throw new Exception("Can't call flush on a Utf8OutputStreamWriter that's already closed");

			if (destPosition > 0)
			{
				@out.write(destBuffer, 0, destPosition);
				destPosition = 0;
			}

			Debug.Assert(@out != null, "@SuppressWarnings(nullness)");
			@out.flush();
		}

		/// <summary>
		/// Writes {@code count} characters starting at {@code offset} in {@code buf} to this writer. The characters are
		/// immediately converted to bytes by the character converter and stored in a local buffer. If the buffer gets full
		/// as a result of the conversion, this writer is flushed.
		/// </summary>
		/// <param name="buf">    the array containing characters to write. </param>
		/// <param name="offset"> the index of the first character in {@code buf} to write. </param>
		/// <param name="count">  the maximum number of characters to write. </param>
		/// <exception cref="IndexOutOfBoundsException"> if {@code offset < 0} or {@code count < 0}, or if {@code offset + count} is
		///                                   greater than the size of {@code buf}. </exception>
		/// <exception cref="IOException">               if this writer has already been closed or another I/O error occurs. </exception>
		public override void write(char[] buf, int offset, int count)
		{
			if (@out == null)
				throw new Exception("Can't call write on a Utf8OutputStreamWriter that's already closed");

			int srcPosition = offset;
			int srcEnd = offset + count;
			while (srcPosition < srcEnd)
			{
				// If the buffer is approaching full (< 256 bytes left), then write it to the output stream.  In this way
				// the inner loop runs from 256 character iterations down to 64 character iterations, but never goes below
				// that when large chunks are being written.
				if (destBuffer.Length - destPosition < 256)
				{
					Debug.Assert(@out != null, "@SuppressWarnings(nullness)");
					@out.write(destBuffer, 0, destPosition);
					destPosition = 0;
				}

				// Don't process any more source characters in the inner loop than will possibly fit in the buffer, encoded.
				// Worst case, each source character maps to 3 bytes, so only process floor(<bytes left in buffer> / 3).
				int srcCurrEnd = srcPosition + (destBuffer.Length - destPosition) / 3;

				// Don't process more source characters than actually available
				if (srcCurrEnd > srcEnd)
					srcCurrEnd = srcEnd;

				while (srcPosition < srcCurrEnd)
				{
					char c = buf[srcPosition++];

					if (queuedLeadSurrogate != -1)
					{
						int trailSurrogate = ((int) c) & 0xFFFF;

						if (trailSurrogate < 0xDC00 || trailSurrogate > 0xDFFF)
							throw new CharConversionException("Character " + trailSurrogate + " unexpected; only a valid trail surrogate should come after a lead surrogate");

						// Note, the Unicode scalar value n is defined as follows:
						// n = (jchar-0xD800)*0x400+(jchar2-0xDC00)+0x10000
						// Where jchar is a high-surrogate, jchar2 is a low-surrogate.
						int n = (queuedLeadSurrogate << 10) + trailSurrogate + -56613888; // 0xFCA02400

						destBuffer[destPosition++] = (sbyte)(0xF0 + ((n >> 18) & 0x07));
						destBuffer[destPosition++] = (sbyte)(0x80 + ((n >> 12) & 0x3F));
						destBuffer[destPosition++] = (sbyte)(0x80 + ((n >> 6) & 0x3F));
						destBuffer[destPosition++] = (sbyte)(0x80 + (n & 0x3F));

						queuedLeadSurrogate = -1;
					}
					else if (c <= 0x7F)
						destBuffer[destPosition++] = (sbyte) c;
					else if (c <= 0x7FF)
					{
						destBuffer[destPosition++] = (sbyte)(0xC0 + ((c >> 6) & 0x1F));
						destBuffer[destPosition++] = (sbyte)(0x80 + (c & 0x3F));
					}
					else if (c >= 0xD800 && c <= 0xDFFF)
					{
						int firstSurrogate = ((int) c) & 0xFFFF;
						if (firstSurrogate > 0xDBFF)
							throw new CharConversionException("Character " + firstSurrogate + " unexpected; surrogate pair must start with a lead surrogate");

						queuedLeadSurrogate = firstSurrogate;
					}
					else
					{
						destBuffer[destPosition++] = (sbyte)(0xE0 + ((c >> 12) & 0x0F));
						destBuffer[destPosition++] = (sbyte)(0x80 + ((c >> 6) & 0x3F));
						destBuffer[destPosition++] = (sbyte)(0x80 + (c & 0x3F));
					}
				}
			}
		}
	}

}