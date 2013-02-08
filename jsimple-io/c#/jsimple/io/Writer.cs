namespace jsimple.io
{

	using PlatformUtils = jsimple.util.PlatformUtils;

	/// <summary>
	/// This class was based on, and modified from, the Apache Harmony java.io.Writer class.  Unlike the standard Java Writer
	/// class, this doesn't do locking and doesn't throw any checked exceptions.
	/// <p/>
	/// The base class for all writers. A writer is a means of writing data to a target in a character-wise manner. Most
	/// output streams expect the <seealso cref="#flush()"/> method to be called before closing the stream, to ensure all data is
	/// actually written out.
	/// <p/>
	/// This abstract class does not provide a fully working implementation, so it needs to be subclassed, and at least the
	/// <seealso cref="#write(char[], int, int)"/>, <seealso cref="#close()"/> and <seealso cref="#flush()"/> methods needs to be overridden. Overriding
	/// some of the non-abstract methods is also often advised, since it might result in higher efficiency.
	/// </summary>
	/// <seealso cref= Reader </seealso>
	public abstract class Writer
	{
		private string lineSeparator = PlatformUtils.LineSeparator;

		/// <summary>
		/// Closes this writer. Implementations of this method should free any resources associated with the writer.
		/// </summary>
		/// <exception cref="IOException"> if an error occurs while closing this writer. </exception>
		public abstract void close();

		/// <summary>
		/// Flushes this writer. Implementations of this method should ensure that all buffered characters are written to the
		/// target.
		/// </summary>
		/// <exception cref="IOException"> if an error occurs while flushing this writer. </exception>
		public abstract void flush();

		/// <summary>
		/// Writes the entire character buffer {@code buf} to the target.
		/// </summary>
		/// <param name="buf"> the non-null array containing characters to write </param>
		/// <exception cref="IOException"> if this writer is closed or another I/O error occurs. </exception>
		public virtual void write(char[] buf)
		{
			write(buf, 0, buf.Length);
		}

		/// <summary>
		/// Writes {@code count} characters starting at {@code offset} in {@code buf} to the target.
		/// </summary>
		/// <param name="buf">    the non-null character array to write. </param>
		/// <param name="offset"> the index of the first character in {@code buf} to write. </param>
		/// <param name="count">  the maximum number of characters to write. </param>
		/// <exception cref="IOException"> if this writer is closed or another I/O error occurs. </exception>
		public abstract void write(char[] buf, int offset, int count);

		/// <summary>
		/// Writes one character to the target. Only the two least significant bytes of the integer {@code oneChar} are
		/// written.
		/// </summary>
		/// <param name="oneChar"> the character to write to the target. </param>
		/// <exception cref="IOException"> if this writer is closed or another I/O error occurs. </exception>
		public virtual void write(int oneChar)
		{
			char[] oneCharArray = new char[1];
			oneCharArray[0] = (char) oneChar;
			write(oneCharArray);
		}

		/// <summary>
		/// Writes the characters from the specified string to the target.
		/// </summary>
		/// <param name="str"> the non-null string containing the characters to write </param>
		/// <exception cref="IOException"> if this writer is closed or another I/O error occurs </exception>
		public virtual void write(string str)
		{
			write(str, 0, str.Length);
		}

		/// <summary>
		/// Writes {@code count} characters from {@code str} starting at {@code offset} to the target.
		/// </summary>
		/// <param name="str">    the non-null string containing the characters to write </param>
		/// <param name="offset"> the index of the first character in {@code str} to write </param>
		/// <param name="count">  the number of characters from {@code str} to write. </param>
		/// <exception cref="IOException"> if this writer is closed or another I/O error occurs </exception>
		public virtual void write(string str, int offset, int count)
		{
			char[] charBuffer = new char[count];
			str.CopyTo(offset, charBuffer, 0, offset + count - offset);

			write(charBuffer, 0, charBuffer.Length);
		}

		/// <summary>
		/// Get the line separator being used for this writer.  By default, it's the platform default line separator.
		/// </summary>
		/// <returns> current line separator </returns>
		public virtual string LineSeparator
		{
			get
			{
				return lineSeparator;
			}
			set
			{
				this.lineSeparator = value;
			}
		}


		/// <summary>
		/// Write the line separator.
		/// </summary>
		public void writeln()
		{
			write(lineSeparator);
		}

		/// <summary>
		/// Writes the characters from the specified string followed by a line separator.
		/// </summary>
		/// <param name="str"> string to write </param>
		public void writeln(string str)
		{
			write(str);
			writeln();
		}
	}

}