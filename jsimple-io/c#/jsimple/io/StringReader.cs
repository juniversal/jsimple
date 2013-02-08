using System;

namespace jsimple.io
{



	/// <summary>
	/// This class was based on, and modified from, the Apache Harmony java.io.StringReader class.  Unlike the standard Java
	/// StringReader class, this doesn't do locking, doesn't throw any checked exceptions, and doesn't support
	/// mark/reset/skip.
	/// <p/>
	/// A specialized <seealso cref="Reader"/> that reads characters from a {@code String} in a sequential manner.
	/// </summary>
	/// <seealso cref= java.io.StringWriter </seealso>
	public class StringReader : Reader
	{
		private string str;
		private int pos;
		private int count;

		/// <summary>
		/// Construct a new {@code StringReader} with {@code str} as source. The size of the reader is set to the {@code
		/// length()} of the string.
		/// </summary>
		/// <param name="str"> the source string for this reader. </param>
		public StringReader(string str) : base()
		{
			this.str = str;
			this.count = str.Length;
		}

		/// <summary>
		/// Closes this reader. Once it is closed, read operations on this reader will throw an {@code IOException}. Only the
		/// first invocation of this method has any effect.
		/// </summary>
		public override void close()
		{
			str = null;
		}

		/// <summary>
		/// Reads a single character from the source string and returns it as an integer with the two higher-order bytes set
		/// to 0. Returns -1 if the end of the source string has been reached.
		/// </summary>
		/// <returns> the character read or -1 if the end of the source string has been reached </returns>
		/// <exception cref="IOException"> if this reader is closed </exception>
		public override int read()
		{
			if (str == null)
				throw new IOException("Reader is closed");
			if (pos == count)
				return -1;
			return str[pos++];
		}

		/// <summary>
		/// Reads at most {@code len} characters from the source string and stores them at {@code offset} in the character
		/// array {@code buf}. Returns the number of characters actually read or -1 if the end of the source string has been
		/// reached.
		/// </summary>
		/// <param name="buf">    the character array to store the characters read </param>
		/// <param name="offset"> the initial position in {@code buffer} to store the characters read from this reader </param>
		/// <param name="length"> the maximum number of characters to read </param>
		/// <returns> the number of characters read or -1 if the end of the reader has been reached </returns>
		/// <exception cref="IndexOutOfBoundsException"> if {@code offset < 0} or {@code len < 0}, or if {@code offset + len} is greater
		///                                   than the size of {@code buf}. </exception>
		/// <exception cref="IOException">               if this reader is closed </exception>
		public override int read(char[] buf, int offset, int length)
		{
			if (str == null)
				throw new IOException("Reader is closed");
			if (length < 0)
				throw new Exception("read length parameter can't be negative");
			if (pos == this.count)
				return -1;
			int end = pos + length > this.count ? this.count : pos + length;
			str.CopyTo(pos, buf, offset, end - pos);
			int charsRead = end - pos;
			pos = end;
			return charsRead;
		}
	}

}