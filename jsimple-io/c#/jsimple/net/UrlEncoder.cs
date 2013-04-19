using System.Text;

namespace jsimple.net
{

	using IOUtils = jsimple.io.IOUtils;

	/// <summary>
	/// @author Bret Johnson
	/// @since 11/25/12 9:03 PM
	/// </summary>
	public class UrlEncoder
	{
		/// <summary>
		/// Encodes the given string {@code s} in a x-www-form-urlencoded string using the specified encoding scheme {@code
		/// enc}.
		/// <p/>
		/// All characters except letters ('a'..'z', 'A'..'Z') and numbers ('0'..'9') and characters '.', '-', '*', '_' are
		/// converted into their hexadecimal value prepended by '%'. For example: '#' -> %23. In addition, spaces are
		/// substituted by '+'
		/// </summary>
		/// <param name="s"> the string to be encoded. </param>
		/// <returns> the encoded string. </returns>
		/// <exception cref="java.io.UnsupportedEncodingException">
		///          if the specified encoding scheme is invalid. </exception>
		public static string encode(string s)
		{
			// Guess a bit bigger for encoded form
			StringBuilder buffer = new StringBuilder(s.Length + 16);
			int start = -1;
			for (int i = 0; i < s.Length; i++)
			{
				char ch = s[i];
				if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9') || " .-*_".IndexOf(ch) > -1)
				{
					if (start >= 0)
					{
						convert(s.Substring(start, i - start), buffer);
						start = -1;
					}
					if (ch != ' ')
						buffer.Append(ch);
					else
						buffer.Append('+');
				}
				else
				{
					if (start < 0)
						start = i;
				}
			}

			if (start >= 0)
				convert(s.Substring(start, s.Length - start), buffer);

			return buffer.ToString();
		}

		private const string digits = "0123456789ABCDEF";

		private static void convert(string s, StringBuilder buffer)
		{
			int[] length = new int[1];
			sbyte[] bytes = IOUtils.toUtf8BytesFromString(s, length);
			for (int j = 0; j < length[0]; j++)
			{
				buffer.Append('%');
				sbyte currByte = bytes[j];
				buffer.Append(digits[(currByte & 0xf0) >> 4]);
				buffer.Append(digits[currByte & 0xf]);
			}
		}
	}

}