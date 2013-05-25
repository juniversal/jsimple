using System.Text;

namespace jsimple.io
{

	/// <summary>
	/// This class was based on, and modified from, the Apache Harmony java.io.StringWriter class.  Unlike the standard Java
	/// StringWriter class, this doesn't do locking, doesn't throw any checked exceptions, and uses a StringBuilder
	/// internally instead of a StringBuffer (so it should be more efficient, though it's not intended to be thread safe).
	/// <p/>
	/// A specialized <seealso cref="Writer"/> that writes characters to a {@code StringBuffer} in a sequential manner, appending them
	/// in the process. The result can later be queried using the <seealso cref="#StringWriter(int)"/> or <seealso cref="#toString()"/> methods.
	/// </summary>
	/// <seealso cref= StringReader </seealso>
	public class StringWriter : Writer
	{
		private StringBuilder buffer;

		/// <summary>
		/// Constructs a new {@code StringWriter} which has a <seealso cref="StringBuffer"/> allocated with the default size of 16
		/// characters. The {@code StringBuffer} is also the {@code lock} used to synchronize access to this writer.
		/// </summary>
		public StringWriter() : base()
		{
			buffer = new StringBuilder(16);
		}

		/// <summary>
		/// Constructs a new {@code StringWriter} which has a <seealso cref="StringBuffer"/> allocated with a size of {@code
		/// initialSize} characters. The {@code StringBuffer} is also the {@code lock} used to synchronize access to this
		/// writer.
		/// </summary>
		/// <param name="initialSize"> the initial size of the target string buffer. </param>
		public StringWriter(int initialSize)
		{
			buffer = new StringBuilder(initialSize);
		}

		/// <summary>
		/// Calling this method has no effect. In contrast to most {@code Writer} subclasses, the other methods in {@code
		/// StringWriter} do not throw an {@code IOException} if {@code close()} has been called.
		/// </summary>
		public override void close()
		{
		}

		/// <summary>
		/// Calling this method has no effect.
		/// </summary>
		public override void flush()
		{
		}

		/// <summary>
		/// Gets a reference to this writer's internal <seealso cref="StringBuilder"/>. Any changes made to the returned buffer are
		/// reflected in this writer.
		/// </summary>
		/// <returns> a reference to this writer's internal {@code StringBuilder}. </returns>
		public virtual StringBuilder Buffer
		{
			get
			{
				return buffer;
			}
		}

		/// <summary>
		/// Gets a copy of the contents of this writer as a string.
		/// </summary>
		/// <returns> this writer's contents as a string. </returns>
		public override string ToString()
		{
			return buffer.ToString();
		}

		/// <summary>
		/// Writes {@code count} characters starting at {@code offset} in {@code buffer} to this writer's {@code
		/// StringBuffer}.
		/// </summary>
		/// <param name="cbuf">   the non-null character array to write. </param>
		/// <param name="offset"> the index of the first character in {@code cbuf} to write. </param>
		/// <param name="count">  the maximum number of characters to write. </param>
		public override void write(char[] cbuf, int offset, int count)
		{
			buffer.Append(cbuf, offset, count);
		}

		/// <summary>
		/// Writes one character to this writer's {@code StringBuffer}. Only the two least significant bytes of the integer
		/// {@code oneChar} are written.
		/// </summary>
		/// <param name="oneChar"> the character to write to this writer's {@code StringBuffer}. </param>
		public override void write(int oneChar)
		{
			buffer.Append((char) oneChar);
		}

		/// <summary>
		/// Writes the characters from the specified string to this writer's {@code StringBuffer}.
		/// </summary>
		/// <param name="str"> the non-null string containing the characters to write. </param>
		public override void write(string str)
		{
			buffer.Append(str);
		}

		/// <summary>
		/// Writes {@code count} characters from {@code str} starting at {@code offset} to this writer's {@code
		/// StringBuffer}.
		/// </summary>
		/// <param name="str">    the non-null string containing the characters to write. </param>
		/// <param name="offset"> the index of the first character in {@code str} to write. </param>
		/// <param name="count">  the number of characters from {@code str} to write. </param>
		public override void write(string str, int offset, int count)
		{
			string sub = str.Substring(offset, count);
			buffer.Append(sub);
		}
	}

}