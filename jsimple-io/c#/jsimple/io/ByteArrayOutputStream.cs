using System;

namespace jsimple.io
{

	/// <summary>
	/// This class was based on, and modified from, the Apache Harmony java.io.ByteArrayOutputStream class.  Unlike the Java
	/// OutputStream class, none of the methods are synchronized & this class provides a way to get the underlying byte array
	/// without copying it.
	/// <p/>
	/// A specialized <seealso cref="OutputStream"/> for class for writing content to an (internal) byte array. As bytes are written to
	/// this stream, the byte array may be expanded to hold more bytes. When the writing is considered to be finished, the
	/// byte array can be requested from the class.
	/// </summary>
	/// <seealso cref= ByteArrayInputStream </seealso>
	public class ByteArrayOutputStream : OutputStream
	{
		/// <summary>
		/// The byte array containing the bytes written.
		/// </summary>
		protected internal sbyte[] buffer;

		/// <summary>
		/// The number of bytes written.
		/// </summary>
		protected internal int count;

		/// <summary>
		/// Constructs a new ByteArrayOutputStream with a default size of 32 bytes. If more than 32 bytes are written to this
		/// instance, the underlying byte array will expand.
		/// </summary>
		public ByteArrayOutputStream() : base()
		{
			buffer = new sbyte[32];
		}

		/// <summary>
		/// Constructs a new {@code ByteArrayOutputStream} with a default size of {@code size} bytes. If more than {@code
		/// size} bytes are written to this instance, the underlying byte array will expand.
		/// </summary>
		/// <param name="size"> initial size for the underlying byte array, must be non-negative </param>
		public ByteArrayOutputStream(int size)
		{
			buffer = new sbyte[size];
		}

		/// <summary>
		/// Closes this stream. This releases system resources used for this stream.
		/// </summary>
		public override void close()
		{
			// Although the spec claims "A closed stream cannot perform output operations and cannot be reopened.", this
			// implementation must do nothing.
			base.close();
		}

		private void expand(int i)
		{
			// Can the buffer handle @i more bytes?  If so, return
			if (count + i <= buffer.Length)
				return;

			sbyte[] newBuffer = new sbyte[(count + i) * 2];
			Array.Copy(buffer, 0, newBuffer, 0, count);
			buffer = newBuffer;
		}

		/// <summary>
		/// Resets this stream to the beginning of the underlying byte array. All subsequent writes will overwrite any bytes
		/// previously stored in this stream.
		/// </summary>
		public virtual void reset()
		{
			count = 0;
		}

		/// <summary>
		/// Returns the total number of bytes written to this stream so far.
		/// </summary>
		/// <returns> the number of bytes written to this stream </returns>
		public virtual int Length
		{
			get
			{
				return count;
			}
		}

		/// <summary>
		/// Returns the contents of this ByteArrayOutputStream as a byte array.  Only the first length[0] bytes of this array
		/// contain the stream data.  Unlike the standard Java implementation, this just returns a reference to the internal
		/// byte array.  That has the advantage of superior performance--no needless copy is needed--but the caller should be
		/// careful not to modify the array & be aware that it can change as more data is written to the stream.
		/// </summary>
		/// <returns> this stream's current contents as a byte array; the array can be arbitrarily big, but only the first
		///         length[0] bytes contain stream data </returns>
		public virtual sbyte[] getByteArray(int[] length)
		{
			length[0] = count;
			return buffer;
		}

		/// <summary>
		/// Returns the contents of this ByteArrayOutputStream as a byte array. Unlike getByteArray(int[] length) this method
		/// returns an array of exactly the right size, containing just the data in question and no more, and the returned
		/// array is a copy of the data, so the caller need not be concerned about the array changing as more data is
		/// written.  However, that convenience comes at the expense of performance, as an extra copy is required.
		/// </summary>
		/// <returns> a copy of the contents of this stream </returns>
		public virtual sbyte[] toByteArray()
		{
			sbyte[] copy = new sbyte[count];
			Array.Copy(buffer, 0, copy, 0, count);
			return copy;
		}

		/// <summary>
		/// Writes {@code count} bytes from the byte array {@code buffer} starting at offset {@code index} to this stream.
		/// </summary>
		/// <param name="buffer"> the buffer to be written. </param>
		/// <param name="offset"> the initial position in {@code buffer} to retrieve bytes. </param>
		/// <param name="length"> the number of bytes of {@code buffer} to write. </param>
		/// <exception cref="NullPointerException">      if {@code buffer} is {@code null}. </exception>
		/// <exception cref="IndexOutOfBoundsException"> if {@code offset < 0} or {@code len < 0}, or if {@code offset + len} is greater
		///                                   than the length of {@code buffer}. </exception>
		public override void write(sbyte[] buffer, int offset, int length)
		{
			// Expand if necessary
			expand(length);

			Array.Copy(buffer, offset, this.buffer, this.count, length);
			this.count += length;
		}

		/// <summary>
		/// Writes the specified byte {@code oneByte} to the OutputStream. Only the low order byte of {@code oneByte} is
		/// written.
		/// </summary>
		/// <param name="oneByte"> the byte to be written. </param>
		public override void write(int oneByte)
		{
			if (count == buffer.Length)
				expand(1);
			buffer[count++] = (sbyte) oneByte;
		}
	}

}