namespace jsimple.io
{

	using ByteArrayRange = jsimple.util.ByteArrayRange;
	using StringUtils = jsimple.util.StringUtils;


	/// <summary>
	/// This class was based on, and modified from, the Apache Harmony java.io.OutputStream class.  Unlike the standard Java
	/// OutputStream class, this doesn't throw any checked exceptions.
	/// <p/>
	/// The base class for all output streams. An output stream is a means of writing data to a target in a byte-wise manner.
	/// Most output streams expect the <seealso cref="#flush()"/> method to be called before closing the stream, to ensure all data is
	/// actually written through.
	/// <p/>
	/// This abstract class does not provide a fully working implementation, so it needs to be subclassed, and at least the
	/// <seealso cref="#write(int)"/> method needs to be overridden. Overriding some of the non-abstract methods is also often advised,
	/// since it might result in higher efficiency.
	/// 
	/// @author Bret Johnson </summary>
	/// <seealso cref= jsimple.io.InputStream
	/// @since 10/7/12 12:31 AM </seealso>
	public abstract class OutputStream : jsimple.lang.AutoCloseable
	{
		private ClosedListener closedListener = null;

		/// <summary>
		/// Writes the entire contents of the byte array {@code buffer} to this stream.
		/// </summary>
		/// <param name="buffer"> the buffer to be written </param>
		/// <exception cref="IOException"> if an error occurs while writing to this stream </exception>
		public virtual void write(sbyte[] buffer)
		{
			write(buffer, 0, buffer.Length);
		}

		/// <summary>
		/// Writes {@code count} bytes from the byte array {@code buffer} starting at position {@code offset} to this
		/// stream.
		/// </summary>
		/// <param name="buffer"> the buffer to be written </param>
		/// <param name="offset"> the start position in {@code buffer} from where to get bytes </param>
		/// <param name="length"> the number of bytes from {@code buffer} to write to this stream </param>
		/// <exception cref="IOException"> if an error occurs while writing to this stream </exception>
		public virtual void write(sbyte[] buffer, int offset, int length)
		{
			for (int i = offset; i < offset + length; i++)
				write(buffer[i]);
		}

		public virtual void write(ByteArrayRange byteArrayRange)
		{
			write(byteArrayRange.Bytes, byteArrayRange.Position, byteArrayRange.Length);
		}

		/// <summary>
		/// Writes a single byte to this stream. Only the least significant byte of the integer {@code oneByte} is written to
		/// the stream.
		/// </summary>
		/// <param name="oneByte"> the byte to be written. </param>
		/// <exception cref="java.io.IOException"> if an error occurs while writing to this stream. </exception>
		public abstract void write(int oneByte);

		/// <summary>
		/// Write the string, assumed to contain only Latin1 characters (Unicode low 256 characters), to the stream.  A
		/// single byte is written for each character; this the most commonly used single byte encoding for text.  If the
		/// string contains any non-Latin1 characters, an exception is thrown.
		/// </summary>
		/// <param name="s"> string to write </param>
		public virtual void writeLatin1EncodedString(string s)
		{
			write(StringUtils.toLatin1BytesFromString(s));
		}

		/// <summary>
		/// Flushes this stream. Implementations of this method should ensure that any buffered data is written out. This
		/// implementation does nothing.
		/// </summary>
		/// <exception cref="IOException"> if an error occurs while flushing this stream </exception>
		public virtual void flush()
		{
		}

		/// <summary>
		/// Write the string to the stream using UTF-8 encoding.
		/// </summary>
		/// <param name="s"> string to write </param>
		public virtual void writeUtf8EncodedString(string s)
		{
			using (Utf8OutputStreamWriter writer = new Utf8OutputStreamWriter(this, false))
			{
				writer.write(s);
			}
		}

		/// <summary>
		/// Closes this stream, freeing any resources associated with it and then calling the closedListener, if one is set.
		/// If the stream is closed multiple times, the closedListener is called exactly once, on the first close.
		/// </summary>
		/// <exception cref="IOException"> if an error occurs while closing this stream. </exception>
		public override void close()
		{
			doClose();

			// If there's a closedListener, null it out (so we don't call it multiple times), then call it.  Note that we
			// null it out before calling onClosed, so a close() call from inside onClosed won't generate another call
			// to onClosed.
			if (closedListener != null)
			{
				ClosedListener localClosedListener = closedListener;
				closedListener = null;
				localClosedListener.onClosed();
			}
		}

		/// <summary>
		/// This is an internal method, implemented by subclasses to close any resources associated with the stream. The
		/// external close() method calls this method, then calls all the closed listeners (if any), assuming this method
		/// didn't throw an exception, to notify of the stream being closed.  Implementations of this method should free any
		/// resources used by the stream.
		/// </summary>
		/// <exception cref="IOException"> if an error occurs while closing this stream. </exception>
		protected internal abstract void doClose();

		public virtual ClosedListener ClosedListener
		{
			get
			{
				return closedListener;
			}
			set
			{
				this.closedListener = value;
			}
		}

	}

}