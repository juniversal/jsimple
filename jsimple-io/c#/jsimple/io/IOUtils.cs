using System.Text;

namespace jsimple.io
{

	using ByteArrayRange = jsimple.util.ByteArrayRange;

	/// <summary>
	/// @author Bret Johnson
	/// @since 10/13/12 4:26 PM
	/// </summary>
	public class IOUtils
	{
		/// <summary>
		/// Converts the string to a UTF-8 byte array.  The array is returned, but it can be bigger than is otherwise needed.
		/// Only the first length[0] bytes of the array should be used.
		/// </summary>
		/// <param name="s"> string input </param>
		/// <returns> byte array, for the UTF-8 encoded string </returns>
		public static ByteArrayRange toUtf8BytesFromString(string s)
		{
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(s.Length);

			using (Utf8OutputStreamWriter utf8OutputStreamWriter = new Utf8OutputStreamWriter(byteArrayOutputStream))
			{
				utf8OutputStreamWriter.write(s);
				utf8OutputStreamWriter.flush();

				return byteArrayOutputStream.ByteArray;
			}
		}

		/// <summary>
		/// Converts the string to a UTF-8 byte array, which is returned.  Unlike the method above, which returns the
		/// length[0] bytes to use, the entire array array should be used here.  This method can be slightly less efficient
		/// than the above version, since a copy of the array is made in some cases, but the tradeoff is more convenience. If
		/// the input string only contains Latin1 characters, then both methods are the same, as no copy is made since we can
		/// easily predict the exact needed byte array length before hand.
		/// </summary>
		/// <param name="s"> string input </param>
		/// <returns> byte array, for the UTF-8 encoded string </returns>
	/*
	    public static byte[] toUtf8BytesFromString(String s) {
	        int[] length = new int[1];
	        byte[] bytes = toUtf8BytesFromString(s, length);
	
	        if (bytes.length == length[0])
	            return bytes;
	        else {
	            byte[] copy = new byte[length[0]];
	            System.arraycopy(bytes, 0, copy, 0, length[0]);
	            return copy;
	        }
	    }
	*/

		/// <summary>
		/// Converts the stream contents, assumed to be a UTF-8 character data, to a string.  The inputStream is closed on
		/// success.  If an exception is thrown,  the caller should close() inputStream (e.g. in a finally clause, since
		/// calling close multiple times is benign).
		/// </summary>
		/// <param name="inputStream"> input stream, assumed to be UTF-8 </param>
		/// <returns> string </returns>

		/// <summary>
		/// Convert the subset of the byte array, from position through position + length - 1, assumed to be a UTF-8
		/// character data, to a string.
		/// </summary>
		/// <param name="utf8Bytes"> UTF-8 byte array </param>
		/// <param name="position">  starting position in array </param>
		/// <param name="length">    number of bytes to read </param>
		/// <returns> String for UTF-8 data </returns>
		public static string toStringFromUtf8Bytes(sbyte[] utf8Bytes, int position, int length)
		{
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(utf8Bytes, position, length);
			return toStringFromReader(new Utf8InputStreamReader(byteArrayInputStream));
		}

		public static string toStringFromUtf8Bytes(ByteArrayRange byteArrayRange)
		{
			return toStringFromUtf8Bytes(byteArrayRange.Bytes, byteArrayRange.Position, byteArrayRange.Length);
		}

		/// <summary>
		/// Converts the stream contents, assumed to be a UTF-8 character data, to a string.  The inputStream is closed on
		/// success.  If an exception is thrown,  the caller should close() inputStream (e.g. in a finally clause, since
		/// calling close multiple times is benign).
		/// </summary>
		/// <param name="inputStream"> input stream, assumed to be UTF-8 </param>
		/// <returns> string </returns>
		public static string toStringFromUtf8Stream(InputStream inputStream)
		{
			return toStringFromReader(new Utf8InputStreamReader(inputStream));
		}

		/// <summary>
		/// Converts the reader contents to a string.  The reader is closed on success.  If an exception is thrown,  the
		/// caller should close() the reader or at least close the input stream that it's based on (e.g. it may close it in a
		/// finally clause, since calling close multiple times is benign).
		/// </summary>
		/// <param name="reader"> reader object </param>
		/// <returns> string contents of reader </returns>
		public static string toStringFromReader(Reader reader)
		{
			char[] buffer = new char[8 * 1024];
			StringBuilder @out = new StringBuilder();
			int charsRead;
			do
			{
				charsRead = reader.read(buffer, 0, buffer.Length);
				if (charsRead > 0)
					@out.Append(buffer, 0, charsRead);
			} while (charsRead >= 0);
			reader.close();
			return @out.ToString();
		}

		/// <summary>
		/// Read the remaining data from the input stream into a byte array, which is returned.  Only the first
		/// ByteArrayRange.getLength() bytes of the byte array should be used.  The inputStream is closed after it's
		/// completely read, though it won't be closed if an except occurs in the middle of reading from it.
		/// </summary>
		/// <param name="inputStream"> input stream </param>
		/// <returns> ByteArrayRange containing the data in the stream </returns>
		public virtual ByteArrayRange toBytesFromStream(InputStream inputStream)
		{
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			inputStream.copyTo(byteStream);
			return byteStream.closeAndGetByteArray();
		}
	}

}