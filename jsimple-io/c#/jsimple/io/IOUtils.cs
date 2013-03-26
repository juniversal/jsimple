using System;
using System.Text;

namespace jsimple.io
{

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
		/// <param name="s">      string input </param>
		/// <param name="length"> int[1] array, returning number of bytes at beginning of array containing the data </param>
		/// <returns> byte array, for the UTF-8 encoded string </returns>
		public static sbyte[] toUtf8BytesFromString(string s, int[] length)
		{
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(s.Length);

			Utf8OutputStreamWriter utf8OutputStreamWriter = new Utf8OutputStreamWriter(byteArrayOutputStream);
			utf8OutputStreamWriter.write(s);
			utf8OutputStreamWriter.flush();

			sbyte[] bytes = byteArrayOutputStream.getByteArray(length);

			utf8OutputStreamWriter.close();
			return bytes;
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
		public static sbyte[] toUtf8BytesFromString(string s)
		{
			int[] length = new int[1];
			sbyte[] bytes = toUtf8BytesFromString(s, length);

			if (bytes.Length == length[0])
				return bytes;
			else
			{
				sbyte[] copy = new sbyte[length[0]];
				Array.Copy(bytes, 0, copy, 0, length[0]);
				return copy;
			}
		}

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
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(utf8Bytes, 0, length);
			return toStringFromReader(new Utf8InputStreamReader(byteArrayInputStream));
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
		/// Read the remaining data from the input stream into a byte array, which is returned.  Only the first length[0]
		/// bytes of the byte array should be used.  The inputStream is closed after it's completely read.
		/// </summary>
		/// <param name="inputStream"> input stream </param>
		/// <param name="length">      int[1] array; length of the data is returned in length[0] </param>
		/// <returns> byte array contain the data in the stream, in the first length[0] bytes </returns>
		public static sbyte[] toBytesFromStream(InputStream inputStream, int[] length)
		{
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			inputStream.copyTo(byteStream);
			return byteStream.closeAndGetByteArray(length);
		}

		/// <summary>
		/// Read the remaining data from the input stream into a byte array, which is returned.  inputStream is closed after
		/// it's completely read.  Unlike the version with the length parameter, this method returns an array of exactly the
		/// right size, containing just the data in question and no more.  However, that convenience comes at the expense of
		/// performance, as normally an extra copy of the array is required.
		/// </summary>
		/// <param name="inputStream"> input stream </param>
		/// <returns> byte array contain the data in the stream </returns>
		public static sbyte[] toBytesFromStream(InputStream inputStream)
		{
			int[] length = new int[1];
			sbyte[] bytes = toBytesFromStream(inputStream, length);

			if (bytes.Length == length[0])
				return bytes;
			else
			{
				sbyte[] copy = new sbyte[length[0]];
				Array.Copy(bytes, 0, copy, 0, length[0]);
				return copy;
			}
		}
	}

}