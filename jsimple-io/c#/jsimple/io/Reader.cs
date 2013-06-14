namespace jsimple.io
{

	/// <summary>
	/// This class was based on, and modified from, the Apache Harmony java.io.Reader class.  Unlike the Java Reader class,
	/// this doesn't do locking and doesn't throw any checked exceptions.
	/// <p/>
	/// The base class for all readers. A reader is a means of reading data from a source in a character-wise manner.
	/// <p/>
	/// This abstract class does not provide a fully working implementation, so it needs to be subclassed, and at least the
	/// <seealso cref="#read(char[], int, int)"/> and <seealso cref="#close()"/> methods needs to be overridden. Overriding some of the
	/// non-abstract methods is also often advised, since it might result in higher efficiency.
	/// </summary>
	public abstract class Reader : jsimple.lang.AutoCloseable
	{
		/// <summary>
		/// Closes this reader. Implementations of this method should free any resources associated with the reader.
		/// </summary>
		public abstract void close();

		/// <summary>
		/// Reads a single character from this reader and returns it as an integer with the two higher-order bytes set to 0.
		/// Returns -1 if the end of the reader has been reached.
		/// </summary>
		/// <returns> the character read or -1 if the end of the reader has been reached </returns>
		/// <exception cref="IOException"> if this reader is closed or some other I/O error occurs </exception>
		public virtual int read()
		{
			char[] charArray = new char[1];
			if (read(charArray, 0, 1) != -1)
				return charArray[0];
			return -1;
		}

		/// <summary>
		/// Reads characters from this reader and stores them in the character array {@code buf} starting at offset 0.
		/// Returns the number of characters actually read or -1 if the end of the reader has been reached.
		/// </summary>
		/// <param name="buffer"> character array to store the characters read. </param>
		/// <returns> the number of characters read or -1 if the end of the reader has been reached </returns>
		/// <exception cref="IOException"> if this reader is closed or some other I/O error occurs </exception>
		public virtual int read(char[] buffer)
		{
			return read(buffer, 0, buffer.Length);
		}

		/// <summary>
		/// Reads at most {@code count} characters from this reader and stores them at {@code offset} in the character array
		/// {@code buf}. Returns the number of characters actually read or -1 if the end of the reader has been reached.
		/// </summary>
		/// <param name="buffer">    the character array to store the characters read </param>
		/// <param name="offset"> the initial position in {@code buffer} to store the characters read from this reader </param>
		/// <param name="count">  the maximum number of characters to read </param>
		/// <returns> the number of characters read or -1 if the end of the reader has been reached </returns>
		/// <exception cref="IOException"> if this reader is closed or some other I/O error occurs </exception>
		public abstract int read(char[] buffer, int offset, int count);
	}

}